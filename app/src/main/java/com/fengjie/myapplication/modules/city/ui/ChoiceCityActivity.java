package com.fengjie.myapplication.modules.city.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.fengjie.myapplication.R;
import com.fengjie.myapplication.adapter.recyclerview.adapter.CommonAdapter;
import com.fengjie.myapplication.adapter.recyclerview.adapter.MultiItemTypeAdapter;
import com.fengjie.myapplication.adapter.recyclerview.base.ViewHolder;
import com.fengjie.myapplication.base.weather.WeatherConstant;
import com.fengjie.myapplication.modules.city.bean.City;
import com.fengjie.myapplication.modules.city.bean.Province;
import com.fengjie.myapplication.modules.city.db.DBManager;
import com.fengjie.myapplication.modules.city.db.WeatherDB;
import com.fengjie.myapplication.modules.tool.db.weather.CityORM;
import com.fengjie.myapplication.modules.tool.db.weather.SharedPreferenceUtil;
import com.fengjie.myapplication.utils.LogUtils;
import com.fengjie.myapplication.utils.weather.OrmLite;
import com.fengjie.myapplication.utils.weather.RxUtils;
import com.fengjie.myapplication.utils.weather.Util;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by hugo on 2016/2/19 0019. todo 需要统一 Activity 退出的效果
 */
public class ChoiceCityActivity extends RxAppCompatActivity
{

	private RecyclerView mRecyclerview;
	private ProgressBar mProgressBar;

	private ArrayList< String > dataList = new ArrayList<>();
	private Province selectedProvince;
	private City selectedCity;
	private List< Province > provincesList = new ArrayList<>();
	private List< City > cityList;
	private CommonAdapter mAdapter;

	public static final int LEVEL_PROVINCE = 1;
	public static final int LEVEL_CITY = 2;
	private int currentLevel;

	private boolean isChecked = false;


	@Override
	protected void onCreate ( Bundle savedInstanceState )
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choice_city);


		initView();

		Observable
				.defer(() -> {
					//mDBManager = new DBManager(ChoiceCityActivity.this);
					DBManager.getInstance().openDatabase();
					return Observable.just(1);
				})
				.compose(RxUtils.rxSchedulerHelper())
				.compose(this.bindToLifecycle())
				.subscribe(integer -> {
					initRecyclerView();
					queryProvinces();
				});
		Intent intent = getIntent();
		isChecked = intent.getBooleanExtra(WeatherConstant.MULTI_CHECK, false);
		if ( isChecked && SharedPreferenceUtil.getInstance().getBoolean("Tips", true) )
		{
			showTips();
		}
	}

	private void initView ()
	{
//		RxBus.get().register(this);     //RxBus注册

		mRecyclerview = ( RecyclerView ) findViewById(R.id.recyclerview);
		mProgressBar = ( ProgressBar ) findViewById(R.id.progress_pb_common);
		if ( mProgressBar != null )
		{
			mProgressBar.setVisibility(View.VISIBLE);
		}
	}

	private void initRecyclerView ()
	{
		mRecyclerview.setLayoutManager(new LinearLayoutManager(this));
		mRecyclerview.setHasFixedSize(true);
		//mRecyclerview.setItemAnimator(new FadeInUpAnimator());
		mAdapter = new CommonAdapter<String>(this, R.layout.item_city,dataList)
		{
			@Override
			protected void convert ( ViewHolder holder, String info, int position )
			{
				holder.setText(R.id.item_city,info);
			}
		};

		mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener()
		{
			@Override
			public void onItemClick ( View view, RecyclerView.ViewHolder holder, int position )
			{
				if ( currentLevel == LEVEL_PROVINCE )
				{
					selectedProvince = provincesList.get(position);
					mRecyclerview.smoothScrollToPosition(0);
					queryCities();
				} else if ( currentLevel == LEVEL_CITY )
				{
					String city = Util.replaceCity(cityList.get(position).CityName);     //获得改变的城市
					if ( isChecked )
					{
						OrmLite.getInstance().save(new CityORM(city));
//					RxBus.getDefault().post(new MultiUpdate());
						LogUtils.d("是多城市管理模式");
					} else
					{
						SharedPreferenceUtil.getInstance().setCityName(city);   //改变数据库内容
//					RxBus.get().post(new ChangeCityEvent());                   //发出事件
					}
					quit();
				}
			}

			@Override
			public boolean onItemLongClick ( View view, RecyclerView.ViewHolder holder, int position )
			{
				return false;
			}
		});

		mRecyclerview.setAdapter(mAdapter);

	}

	/**
	 * 查询全国所有的省，从数据库查询
	 */
	private void queryProvinces ()
	{
//		getToolbar().setTitle("选择省份");
		Observable
				.defer(() ->
				{
					if ( provincesList.isEmpty() )
					{
						provincesList.addAll(WeatherDB.loadProvinces(DBManager.getInstance().getDatabase()));
					}
					dataList.clear();
					return Observable.fromIterable(provincesList);
				})
				.map(province -> province.ProName)
				.toList()
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.compose(this.bindToLifecycle())
				.doFinally(() -> {
					currentLevel = LEVEL_PROVINCE;
					mAdapter.notifyDataSetChanged();
				})
				.subscribe(strings ->
				{
					dataList.addAll(strings);
					mProgressBar.setVisibility(View.GONE);
				});

	}

//	@Override
//	public boolean onCreateOptionsMenu ( Menu menu )
//	{
//		getMenuInflater().inflate(R.menu.multi_city_menu, menu);
//		menu.getItem(0).setChecked(isChecked);
//		return true;
//	}

//	@Override
//	public boolean onOptionsItemSelected ( MenuItem item )
//	{
//		if ( item.getItemId() == R.id.multi_check )
//		{
//			if ( isChecked )
//			{
//				item.setChecked(false);
//			} else
//			{
//				item.setChecked(true);
//			}
//			isChecked = item.isChecked();
//		}
//		return super.onOptionsItemSelected(item);
//	}

	/**
	 * 查询选中省份的所有城市，从数据库查询
	 */
	private void queryCities ()
	{
//		getToolbar().setTitle("选择城市");
		dataList.clear();
		mAdapter.notifyDataSetChanged();

		Observable
				.defer(() ->
				{
					cityList = WeatherDB.loadCities(DBManager.getInstance().getDatabase(), selectedProvince.ProSort);
					return Observable.fromIterable(cityList);
				})
				.map(city -> city.CityName)
				.toList()
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.compose(this.bindToLifecycle())
				.doFinally(() -> {
					currentLevel = LEVEL_CITY;
					mAdapter.notifyDataSetChanged();
					//定位到第一个item
					mRecyclerview.smoothScrollToPosition(0);
				})
				.subscribe(strings ->
				{
					dataList.addAll(strings);
					mProgressBar.setVisibility(View.GONE);
				});

	}

	@Override
	public void onBackPressed ()
	{
		//super.onBackPressed();  http://www.eoeandroid.com/thread-275312-1-1.html 这里的坑
		if ( currentLevel == LEVEL_PROVINCE )
		{
			quit();
		} else
		{
			queryProvinces();
			mRecyclerview.smoothScrollToPosition(0);
		}
	}

	public static void launch ( Context context )
	{
		context.startActivity(new Intent(context, ChoiceCityActivity.class));
	}

	@Override
	protected void onDestroy ()
	{
		super.onDestroy();
		DBManager.getInstance().closeDatabase();
//		RxBus.get().unregister(this);   //销毁注册，防止内存泄露
	}

	private void showTips ()
	{
//		new AlertDialog.Builder(this).setTitle("多城市管理模式").setMessage("您现在是多城市管理模式,直接点击即可新增城市.如果暂时不需要添加,"
//				                                                             + "在右上选项中关闭即可像往常一样操作.\n因为 api 次数限制的影响,多城市列表最多三个城市.(๑′ᴗ‵๑)").setPositiveButton("明白", ( dialog, which ) -> dialog.dismiss()).setNegativeButton("不再提示", ( dialog, which ) -> SharedPreferenceUtil.getInstance().putBoolean("Tips", false)).show();
	}

	private void quit ()
	{
		ChoiceCityActivity.this.finish();
		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
	}
}
