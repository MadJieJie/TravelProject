package com.fengjie.myapplication.modules.travel.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.fengjie.myapplication.base.fragment.AbstractFragment;
import com.fengjie.myapplication.modules.tool.utils.weather.RxUtils;
import com.fengjie.myapplication.modules.travel.bean.TravelNote;
import com.fengjie.myapplication.modules.travel.db.TravelNoteDao;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 * @author Created by MadJieJie on 2017/1/31-21:49.
 * @brief
 * @attention
 */

public class OtherTravelNoteFragment extends AbstractFragment
{
	
	private RecyclerView mRecyclerView;
	private List< TravelNote > mDatas = new ArrayList<TravelNote>();
	private EmptyWrapper<TravelNote> mEmptyWrapper = null;
	private CommonAdapter< TravelNote > mAdapter = null;
	private View mView = null;
	
	
	public static OtherTravelNoteFragment newInstance ()
	{
		Bundle args = new Bundle();
		
		OtherTravelNoteFragment fragment = new OtherTravelNoteFragment();
		fragment.setArguments(args);
		return fragment;
	}
	
	@Nullable
	@Override
	public View onCreateView ( LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState )
	{
		if ( mView == null )
		{
			mView = inflater.inflate(R.layout.fragment_other_travel_note, container, false);
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
	}
	
	
	@Override
	protected void findView ( View view )
	{
		mRecyclerView = ( RecyclerView ) mView.findViewById(R.id.content_rv_otherTravelNote);
	}
	
	
	@Override
	protected void initView ()
	{
		
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
						.setText(R.id.summary, info.getContent().length() > 20 ? info.getContent().substring(0, 20) : info.getContent());//截取20个字做为摘要
			}
		};
		
		mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener()
		{
			@Override
			public void onItemClick ( View view, RecyclerView.ViewHolder holder, int position )
			{
				
			}
			
			@Override
			public boolean onItemLongClick ( View view, RecyclerView.ViewHolder holder, int position )
			{
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
	
	
}


