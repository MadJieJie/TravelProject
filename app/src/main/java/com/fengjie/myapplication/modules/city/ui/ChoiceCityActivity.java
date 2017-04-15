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
import com.fengjie.myapplication.event.Event;
import com.fengjie.myapplication.modules.city.bean.City;
import com.fengjie.myapplication.modules.city.bean.Province;
import com.fengjie.myapplication.modules.city.db.CityDB;
import com.fengjie.myapplication.modules.city.db.DBManager;
import com.fengjie.myapplication.modules.tool.utils.weather.RxUtils;
import com.fengjie.myapplication.utils.often.LogUtils;
import com.fengjie.myapplication.utils.often.SharedPreferenceUtil;
import com.fengjie.myapplication.utils.often.Utils;
import com.fengjie.myapplication.utils.rxbus.RxBus;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by hugo on 2016/2/19 0019.
 * todo 需要统一 Activity 退出的效果
 */
public class ChoiceCityActivity extends RxAppCompatActivity
{
	
	private RecyclerView mRecyclerview;
	private ProgressBar mProgressBar;
	
	private ArrayList< String > dataList = new ArrayList<>();
	private Province selectedProvince;
//	private City selectedCity;
	private List< Province > provincesList = new ArrayList<>();
	private List< City > cityList;
	private CommonAdapter mAdapter;
	
	public static final int LEVEL_PROVINCE = 1;
	public static final int LEVEL_CITY = 2;
	private int currentLevel;
	
//	private boolean isChecked = false;
	
	
	@Override
	protected void onCreate ( Bundle savedInstanceState )
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choice_city);
		
		
		initView();
		
		Observable
				.defer(() ->
				{
					//mDBManager = new DBManager(ChoiceCityActivity.this);
					DBManager.getInstance().openDatabase();
					return Observable.just(1);
				})
				.compose(RxUtils.rxSchedulerHelper())
				.compose(this.bindToLifecycle())
				.subscribe(integer ->
				{
					initRecyclerView();
					queryProvinces();
				});
//		Intent intent = getIntent();
//		isChecked = intent.getBooleanExtra(WeatherConstant.MULTI_CHECK, false);
//		if ( isChecked && SharedPreferenceUtil.getInstance().getBoolean("Tips", true) )
//		{
//			showTips();
//		}
	}
	
	private void initView ()
	{
		RxBus.getInstance().register(this);     //RxBus注册
		
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
		mAdapter = new CommonAdapter< String >(this, R.layout.item_city, dataList)
		{
			@Override
			protected void convert ( ViewHolder holder, String info, int position )
			{
				holder.setText(R.id.item_city, info);
			}
		};
		
		mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener()
		{
			@Override
			public void onItemClick ( View view, RecyclerView.ViewHolder holder, int position )
			{
				if ( currentLevel == LEVEL_PROVINCE )       /**选择省份*/
				{
					selectedProvince = provincesList.get(position);
					mRecyclerview.smoothScrollToPosition(0);
					queryCities();
				} else if ( currentLevel == LEVEL_CITY )        /**选择城市*/
				{
					City city = cityList.get(position);
					String cityName = Utils.replaceCity(cityList.get(position).CityName);     //更改原本城市的名字，去掉市等字体
					/**用SharedPreference缓存cityID & provinceId*/
					SharedPreferenceUtil.getInstance().setCityId(city.cityId);
					SharedPreferenceUtil.getInstance().setCityProId(city.provinceId);
					
					LogUtils.d("API need :" + city.CityName);
					LogUtils.d("API need :" + city.cityId);
					LogUtils.d("API need :" + city.provinceId);

//					if ( isChecked )
//					{
//						OrmLite.getInstance().save(new CityORM(cityName));
////					RxBus.getDefault().post(new MultiUpdate());
//						LogUtils.d("是多城市管理模式");
//					} else
					{
						SharedPreferenceUtil.getInstance().setCityName(cityName);   /**改变数据库内容*/
						RxBus.getInstance().post(new Event(Event.EVENT_CHANGE_CITY));                   //RxBus.post  发出事件
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
						provincesList.addAll(CityDB.loadProvinces(DBManager.getInstance().getDatabase()));
					}
					dataList.clear();
					return Observable.fromIterable(provincesList);
				})
				.map(province -> province.ProName)
				.toList()
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.compose(this.bindToLifecycle())
				.doFinally(() ->
				{
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
					cityList = CityDB.loadCities(DBManager.getInstance().getDatabase(), selectedProvince.ProSort);
					return Observable.fromIterable(cityList);
				})
				.map(city -> city.CityName)
				.toList()
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.compose(this.bindToLifecycle())
				.doFinally(() ->
				{
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
		RxBus.getInstance().unRegister(this);   //销毁注册，防止内存泄露
	}
	
	/**
	 * 退出界面
	 */
	private void quit ()
	{
		ChoiceCityActivity.this.finish();
		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);//动画
	}
}
