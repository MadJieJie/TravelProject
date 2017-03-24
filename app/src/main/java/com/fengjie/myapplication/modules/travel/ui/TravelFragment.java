package com.fengjie.myapplication.modules.travel.ui;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fengjie.myapplication.R;
import com.fengjie.myapplication.adapter.recyclerview.adapter.CommonAdapter;
import com.fengjie.myapplication.adapter.recyclerview.adapter.MultiItemTypeAdapter;
import com.fengjie.myapplication.adapter.recyclerview.base.ViewHolder;
import com.fengjie.myapplication.adapter.recyclerview.wrapper.EmptyWrapper;
import com.fengjie.myapplication.base.AbstractRxPermissionEvent;
import com.fengjie.myapplication.base.fragment.AbstractFragment;
import com.fengjie.myapplication.event.Event;
import com.fengjie.myapplication.modules.tool.utils.weather.RxUtils;
import com.fengjie.myapplication.modules.travel.bean.TravelNote;
import com.fengjie.myapplication.modules.travel.db.TravelNoteDao;
import com.fengjie.myapplication.utils.often.ToastUtils;
import com.fengjie.myapplication.utils.often.Utils;
import com.fengjie.myapplication.utils.rxbus.RxBus;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;

import static com.fengjie.myapplication.modules.travel.base.TravelNoteConstant.EDIT_MODE;
import static com.fengjie.myapplication.modules.travel.base.TravelNoteConstant.ID;
import static com.fengjie.myapplication.modules.travel.base.TravelNoteConstant.MODE;


/**
 * @author Created by MadJieJie on 2017/1/31-21:49.
 * @brief
 * @attention
 */

public class TravelFragment extends AbstractFragment
{
	/***/
	private RecyclerView mRecyclerView;
	private FloatingActionButton mAdd_fab_travel = null;
	private View mView = null;
	/***/
	private static final int SUMMARY_LIMIT_LENGTH = 20;
	private List< TravelNote > mDatas = new ArrayList< TravelNote >();
	private CommonAdapter< TravelNote > mAdapter = null;
	private EmptyWrapper< TravelNote > mEmptyWrapper = null;
	public static TravelFragment newInstance ()
	{
		Bundle args = new Bundle();
		
		TravelFragment fragment = new TravelFragment();
		fragment.setArguments(args);
		return fragment;
	}
	
	@Nullable
	@Override
	public View onCreateView ( LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState )
	{
		if ( mView == null )
		{
			mView = inflater.inflate(R.layout.fragment_my_travel_note, container, false);
		}
		
		return mView;
	}
	
	
	@Override
	public void onViewCreated ( View view, @Nullable Bundle savedInstanceState )
	{
		super.onViewCreated(view, savedInstanceState);
		findView(mView);
		initView();
		initRecyclerView();
		initRxBus();
	}
	
	@Override
	public void onResume ()
	{
		super.onResume();
		loadData();         //导入数据库数据
	}
	
	@Override
	public void onDestroy ()
	{
		super.onDestroy();
		RxBus.getInstance().unRegister(this);
	}
	
	@Override
	protected void findView ( View view )
	{
		mRecyclerView = ( RecyclerView ) mView.findViewById(R.id.content_rv_myTravelNote);
		mAdd_fab_travel = ( FloatingActionButton ) mView.findViewById(R.id.add_fab_travel);
	}
	
	
	@Override
	protected void initView ()
	{
		mAdd_fab_travel.setOnClickListener(v ->
		{
			Utils.isGetRxPermission(getActivity(), new AbstractRxPermissionEvent()
			{
				@Override
				public void canGetPermissionEvent ()
				{
					startActivity(new Intent(getContext(), NewTravelNoteActivity.class));
				}
				
				@Override
				public void notGetPermissionEvent ()
				{
					
				}
			}, Manifest.permission.WRITE_EXTERNAL_STORAGE);     //若获得写入权限,则实现跳转
		});
	}
	
	private void initRecyclerView ()
	{
		mAdapter = new CommonAdapter< TravelNote >(mContext, R.layout.item_trave_note, mDatas)
		{
			@Override
			protected void convert ( ViewHolder holder, TravelNote info, int position )
			{
				holder
						.setText(R.id.title_tv_travel, info.getTitle())
						.setText(R.id.author_tv_travel, info.getAuthor())
						.setText(R.id.summary_tv_travel,
								info.getContent().length() > SUMMARY_LIMIT_LENGTH ? info.getContent().substring(0, SUMMARY_LIMIT_LENGTH) : info.getContent());//截取20个字做为摘要
			}
		};
		
		mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener()
		{
			@Override
			public void onItemClick ( View view, RecyclerView.ViewHolder holder, int position )
			{
				Intent intent = new Intent(mContext, NewTravelNoteActivity.class);
				intent.putExtra(MODE, EDIT_MODE);
				intent.putExtra(ID, mDatas.get(position).getId());
				startActivity(intent);
			}
			
			/**长按删除游记*/
			@Override
			public boolean onItemLongClick ( View view, RecyclerView.ViewHolder holder, int position )
			{
				isNeedToDeleteItem(position);
				return false;
			}
		});
		
		mEmptyWrapper = new EmptyWrapper<>(mAdapter);
		mEmptyWrapper.setEmptyView(R.layout.view_empty);
		
		mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));      //设置RecyclerView垂直显示
		mRecyclerView.setAdapter(mEmptyWrapper);
	}
	
	/**
	 * 导入数据库数据
	 */
	private void loadData ()
	{
		Observable
				.create(new ObservableOnSubscribe< ArrayList< TravelNote > >()
				{
					@Override
					public void subscribe ( ObservableEmitter< ArrayList< TravelNote > > e ) throws Exception
					{
						e.onNext(TravelNoteDao.getQueryAllInfoForDB());           //从数据库查询数据
						e.onComplete();
					}
				}).compose(RxUtils.rxSchedulerHelper())
				.subscribe(noteList ->
				{
					if ( noteList != null )
					{
						mDatas.clear();
						mDatas.addAll(noteList);
					}
					mEmptyWrapper.notifyDataSetChanged();
				});
		
	}
	
	/**
	 * 接收到同步游记事件,更新RecyclerView
	 */
	private void initRxBus ()
	{
		RxBus.getInstance()
				.register(this)
				.tObservable(Event.class)
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(event ->
				{
					if ( event.getEvent() == Event.EVENT_DOWNLOAD_TRAVEL_NOTE )
					{
						mEmptyWrapper.notifyDataSetChanged();
					}
				});
	}
	
	private void isNeedToDeleteItem(final int position)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setCancelable(true);        //点击外部不能消失
		builder.setTitle("您确定要删除此篇游记吗？");
		builder.setNegativeButton("取消", ( ( dialog, which ) -> dialog.dismiss() ));
		builder.setPositiveButton("确定", ( ( dialog, which ) ->
		{
			if ( TravelNoteDao.deleteDataToDB(mDatas.get(position).getId()) )
			{
				mDatas.remove(position);            //删除当前数据源的数据
				mEmptyWrapper.notifyDataSetChanged();
				ToastUtils.showShort(mContext, "删除成功");
			} else
			{
				ToastUtils.showShort(mContext, "删除失败");
			}
		} ));
		AlertDialog dialog = builder.create();
		dialog.show();
	}
	
}


