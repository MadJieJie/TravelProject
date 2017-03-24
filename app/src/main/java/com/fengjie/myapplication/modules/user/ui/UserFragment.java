package com.fengjie.myapplication.modules.user.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fengjie.myapplication.R;
import com.fengjie.myapplication.adapter.recyclerview.adapter.CommonAdapter;
import com.fengjie.myapplication.adapter.recyclerview.adapter.MultiItemTypeAdapter;
import com.fengjie.myapplication.adapter.recyclerview.base.ViewHolder;
import com.fengjie.myapplication.base.BaseApplication;
import com.fengjie.myapplication.base.fragment.AbstractFragment;
import com.fengjie.myapplication.event.Event;
import com.fengjie.myapplication.modules.travel.bean.TravelNote;
import com.fengjie.myapplication.modules.travel.db.TravelNoteDao;
import com.fengjie.myapplication.modules.user.base.Biz;
import com.fengjie.myapplication.modules.user.utils.RetrofitUser;
import com.fengjie.myapplication.utils.often.AbstractSimpleObserver;
import com.fengjie.myapplication.utils.often.ToastUtils;
import com.fengjie.myapplication.utils.rxbus.RxBus;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * @author Created by MadJieJie on 2017/1/31-21:49.
 * @brief
 * @attention
 */

public class UserFragment extends AbstractFragment
{
	/***/
	private RecyclerView mRecyclerView = null;
	
	/**Parameters*/
	private View mView = null;
	private List< String > mDatas = null;
	private CommonAdapter< String > mAdapter = null;
	private TextView mName_tv_user = null;

	
	public static UserFragment newInstance ()
	{
		Bundle args = new Bundle();
		
		UserFragment fragment = new UserFragment();
		fragment.setArguments(args);
		return fragment;
	}
	
	@Override
	public void onCreate ( @Nullable Bundle savedInstanceState )
	{
		super.onCreate(savedInstanceState);
		RxBus.getInstance()
				.register(this);
	}
	
	@Nullable
	@Override
	public View onCreateView ( LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState )
	{
		if ( mView == null )
		{
			mView = inflater.inflate(R.layout.fragment_user_ui, container, false);
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
		mRecyclerView = ( RecyclerView ) mView.findViewById(R.id.content_rv_user);
		mName_tv_user = ( TextView ) mView.findViewById(R.id.name_tv_user);
	}
	
	@Override
	protected void initView ()
	{
	}
	
	private void initRecyclerView()
	{
		mDatas = Arrays.asList("上传游记", "同步游记");
		
		mAdapter = new CommonAdapter< String >(mContext, R.layout.item_common_line, mDatas)
		{
			@Override
			protected void convert ( ViewHolder holder, String info, int position )
			{
				holder.setText(R.id.text_tv_common, mDatas.get(position));
			}
		};
		
		mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener()
		{
			@Override
			public void onItemClick ( View view, RecyclerView.ViewHolder holder, int position )
			{
				TextView tv = ( TextView ) view.findViewById(R.id.text_tv_common);
				if ( tv.getText().toString().equals("上传游记") )
				{
					uploadNote();
				} else if ( tv.getText().toString().equals("同步游记") )
				{
					postDownloadTravelNoteEvent();
					getUserNote(BaseApplication.sUserID);
				}
				
			}
			
			@Override
			public boolean onItemLongClick ( View view, RecyclerView.ViewHolder holder, int position )
			{
				return false;
			}
		});
		mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
		mRecyclerView.setAdapter(mAdapter);
	}
	
	/**
	 * 上传所有Note数据
	 */
	private void uploadNote ()
	{
		
		List< TravelNote > noteList = new ArrayList< TravelNote >();
		noteList.addAll(TravelNoteDao.getQueryAllInfoForDB());        //获得数据库所有内容
		
		Gson gson = new Gson();
		String json = gson.toJson(noteList);        //转换成JSon数据

		uploadNoteDataToServer("upload_note", json)
				.subscribe(new AbstractSimpleObserver< Biz >()
				{
					@Override
					public void onNext ( Biz Info )
					{
						ToastUtils.showShort(mContext, Info.result);
					}
				});
		
	}
	
	/**
	 * 下载备忘录数据到本地
	 */
	private void getUserNote (final int fk_user_uid)
	{
		getUserAllNoteDataForServer("download_note", fk_user_uid)
				.subscribe(new AbstractSimpleObserver< Biz >()
				{
					@Override
					public void onNext ( Biz biz )
					{
						ToastUtils.showShort(mContext,biz.result);
					}
				});
	}
	
	/**
	 * 从网络获取
	 */
	private Observable< Biz > uploadNoteDataToServer ( final String biz, final String jsonString )
	{
		
		return RetrofitUser
				       .getInstance()
				       .getUploaResult(biz, jsonString);
//				       .compose(this.bindToLifecycle());    //防止内存泄露
	}
	
	/**
	 * 从网络获取
	 */
	private Observable< Biz > getUserAllNoteDataForServer ( final String biz, final int fk_user_uid )
	{
		
		return RetrofitUser
				       .getInstance()
				       .getUserResult(biz, fk_user_uid);
//				       .compose(this.bindToLifecycle());    //防止内存泄露
	}
	
	/**
	 * 送出同步游记事件
	 */
	private void postDownloadTravelNoteEvent()
	{
		RxBus.getInstance()
				.post(new Event(Event.EVENT_DOWNLOAD_TRAVEL_NOTE));
	}
	
	private void initRxBus()
	{
		RxBus.getInstance()
				.register(this)
				.tObservable(Event.class)
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(event ->
				{
					if(event.getEvent() == Event.EVENT_LOGIN_SUCCESS)
						mName_tv_user.setText(BaseApplication.sUserName);
				});
	}
	
}


