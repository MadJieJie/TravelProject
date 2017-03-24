package com.fengjie.myapplication.modules.run.ui;

import android.content.Context;
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
import com.fengjie.myapplication.modules.run.bean.Note;
import com.fengjie.myapplication.modules.run.db.NoteDao;
import com.fengjie.myapplication.modules.tool.base.weather.AbstractRetrofitFragment;
import com.fengjie.myapplication.modules.tool.utils.weather.RxUtils;
import com.fengjie.myapplication.modules.travel.db.TravelNoteDao;
import com.fengjie.myapplication.utils.often.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

import static com.fengjie.myapplication.modules.run.base.NoteConstant.EDIT_MODE;
import static com.fengjie.myapplication.modules.run.base.NoteConstant.ID;
import static com.fengjie.myapplication.modules.run.base.NoteConstant.MODE;
import static com.fengjie.myapplication.modules.run.base.NoteConstant.NEW_NOTE_MODE;

/**
 * @author Created by MadJieJie on 2017/1/31-21:49.
 * @brief
 * @attention
 */

public class NoteFragment extends AbstractRetrofitFragment
{
	
	private RecyclerView mRecyclerView = null;
	private CommonAdapter< Note > mAdapter = null;
	private List< Note > mDatas = new ArrayList< Note >();
	private EmptyWrapper mEmptyWrapper = null;
	private FloatingActionButton add_fab_note = null;
	
	private Context mContext = null;
	

	
	public static NoteFragment newInstance ()
	{
		Bundle args = new Bundle();
		
		NoteFragment fragment = new NoteFragment();
		fragment.setArguments(args);
		return fragment;
	}
	
	@Nullable
	@Override
	public View onCreateView ( LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState )
	{
		if ( mView == null )
		{
			mView = inflater.inflate(R.layout.fragment_note_list, container, false);
			mContext = getContext();
		}
		
		return mView;
	}
	
	@Override
	public void onViewCreated ( View view, @Nullable Bundle savedInstanceState )
	{
		super.onViewCreated(view, savedInstanceState);
		findView();
		initView();
		initRecycleView();

//		initRefreshLayout();
//		initRxBus();
//		mTwinklingRefreshLayout.startRefresh();     //导入数据
	}
	
	@Override
	public void onResume ()
	{
		super.onResume();
		load();
	}
	
	private void findView ()
	{
		mRecyclerView = ( RecyclerView ) mView.findViewById(R.id.content_rv_noteList);
		add_fab_note = ( FloatingActionButton ) mView.findViewById(R.id.fab_main);
	}
	
	
	private void initView ()
	{
		add_fab_note.setOnClickListener(v ->
		{
			Intent intent = new Intent(mContext, NewNoteActivity.class);
			intent.putExtra(MODE,NEW_NOTE_MODE);
			startActivity(intent);
		});
	}
	
	/**
	 * 懒加载
	 */
	@Override
	protected void lazyLoad ()
	{
//		mTwinklingRefreshLayout.startLoadMore();
	}
	
	private void initRxBus ()
	{
//		RxBus.getInstance()
//				.tObservable(Event.class)
//				.observeOn(AndroidSchedulers.mainThread())
//				.subscribe(event ->
//						{
//							if ( event.getEvent() == Event.EVENT_CHANGE_CITY )
//							{
//								SceneryConstant.PAGE = 1;
//								mDatas.clear(); //清除所有对象
//								lazyLoad();     //重新导入数据
//							}
//						}
//				);
	}
	
	
	/**
	 * 导入数据库数据
	 */
	private void load ()
	{
		Observable
				.create(new ObservableOnSubscribe< List< Note > >()
				{
					@Override
					public void subscribe ( ObservableEmitter< List< Note > > e ) throws Exception
					{
						e.onNext(NoteDao.getQueryAllInfoForDB());           //从数据库查询数据
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
	
	
	private void initRecycleView ()
	{
		
		/****************** 设置XRecyclerView属性 **************************/
//		mRecyclerView.addItemDecoration(new SpacesItemDecoration(0));//设置item间距

		mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));

//		mRecyclerView.setLoadingMoreEnabled(true);//开启上拉加载
//		mRecyclerView.setPullRefreshEnabled(true);//开启下拉刷新
//		mRecyclerView.setRefreshProgressStyle(ProgressStyle.SquareSpin);
//		mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallScale);
		/****************** 设置XRecyclerView属性 **************************/
		
		mAdapter = new CommonAdapter< Note >(mContext, R.layout.item_note, mDatas)
		{
			@Override
			protected void convert ( ViewHolder holder, Note info, int position )
			{
				holder
						.setText(R.id.title_tv_list, info.getTitle())
						.setText(R.id.summary_tv_list, info.getContent())
						.setText(R.id.time_tv_list, info.getCreate_time());
			}
		};
		
		mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener()
		{
			@Override
			public void onItemClick ( View view, RecyclerView.ViewHolder holder, int position )
			{
				Intent intent = new Intent(mContext,NewNoteActivity.class);
				intent.putExtra(MODE,EDIT_MODE);
				intent.putExtra(ID,mDatas.get(position).getId());
				startActivity(intent);
			}
			
			@Override
			public boolean onItemLongClick ( View view, RecyclerView.ViewHolder holder, int position )
			{
				isNeedToDeleteItem(position);
				return false;
			}
		});


//		mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener()
//		{
//			@Override
//			public void onRefresh ()
//			{
//				mRecyclerView.postDelayed(() -> mRecyclerView.refreshComplete(), 1000);
//			}
//
//			@Override
//			public void onLoadMore ()
//			{
//				mRecyclerView.postDelayed(() -> mRecyclerView.refreshComplete(), 1000);
//			}
//		});
		
		mEmptyWrapper = new EmptyWrapper< Note >(mAdapter);
		mEmptyWrapper.setEmptyView(R.layout.view_empty);
		
		mRecyclerView.setAdapter(mEmptyWrapper);
		
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


