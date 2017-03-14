package com.fengjie.myapplication.modules.run.ui;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fengjie.myapplication.R;
import com.fengjie.myapplication.adapter.recyclerview.adapter.CommonAdapter;
import com.fengjie.myapplication.adapter.recyclerview.adapter.MultiItemTypeAdapter;
import com.fengjie.myapplication.adapter.recyclerview.base.ViewHolder;
import com.fengjie.myapplication.adapter.recyclerview.wrapper.EmptyWrapper;
import com.fengjie.myapplication.base.fragment.IInitView;
import com.fengjie.myapplication.event.Event;
import com.fengjie.myapplication.modules.run.bean.Hotel;
import com.fengjie.myapplication.modules.run.utils.RetrofitHotel;
import com.fengjie.myapplication.modules.tool.base.scenery.SceneryConstant;
import com.fengjie.myapplication.modules.tool.base.weather.AbstractRetrofitFragment;
import com.fengjie.myapplication.utils.often.LogUtils;
import com.fengjie.myapplication.utils.often.SharedPreferenceUtil;
import com.fengjie.myapplication.utils.often.ToastUtils;
import com.fengjie.myapplication.utils.often.Utils;
import com.fengjie.myapplication.utils.rxbus.RxBus;
import com.fengjie.myapplication.modules.tool.utils.weather.ImageLoader;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.header.bezierlayout.BezierLayout;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * @author Created by MadJieJie on 2017/1/31-21:49.
 * @brief
 * @attention
 */

public class HotelFragment extends AbstractRetrofitFragment implements IInitView
{
	
	private RecyclerView mRecyclerView = null;
	private List< Hotel.ResultBean > mDatas = new ArrayList< Hotel.ResultBean >();
	private Context mContext = null;
	private TwinklingRefreshLayout mTwinklingRefreshLayout = null;
	
	private CommonAdapter< Hotel.ResultBean > mAdapter = null;
	private EmptyWrapper mEmptyWrapper = null;
	
	public static HotelFragment newInstance ()
	{
		Bundle args = new Bundle();
		
		HotelFragment fragment = new HotelFragment();
		fragment.setArguments(args);
		return fragment;
	}
	
	@Nullable
	@Override
	public View onCreateView ( LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState )
	{
		if ( mView == null )
		{
			mView = inflater.inflate(R.layout.fragment_hotel, container, false);
			mContext = getContext();
		}
		
		return mView;
	}
	
	@Override
	public void onViewCreated ( View view, @Nullable Bundle savedInstanceState )
	{
		super.onViewCreated(view, savedInstanceState);
		findView(view);
		initView();
		initRecycleView();
		initRefreshLayout();
		initRxBus();
		mTwinklingRefreshLayout.startRefresh();     //导入数据
	}
	
	@Override
	public void findView ( View view )
	{
		mRecyclerView = ( RecyclerView ) view.findViewById(R.id.content_rv_hotel);
		mTwinklingRefreshLayout = ( TwinklingRefreshLayout ) view.findViewById(R.id.refresh_trl_hotel);
	}
	
	
	@Override
	public void initView ()
	{
		initRecycleView();
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
		RxBus.getInstance()
				.tObservable(Event.class)
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(event ->
						{
							if ( event.getEvent() == Event.EVENT_CHANGE_CITY )
							{
								SceneryConstant.PAGE = 1;
								mDatas.clear(); //清除所有对象
								lazyLoad();     //重新导入数据
							}
						}
				);
	}
	
	/**
	 * 导入景点列表数据
	 */
	private void loadData ()
	{
		
		getHotelDataByNet(SharedPreferenceUtil.getInstance().getCityId(), SceneryConstant.PAGE, false)
				.subscribe(new Observer< Hotel >()        /**观察者**/
				{
					@Override
					public void onSubscribe ( Disposable d )
					{
						
					}
					
					@Override
					public void onNext ( Hotel hotel )  //hotel must not null,because emitter can not send null object.
					{
						
						if ( hotel.result != null && ! mDatas.contains(hotel.result) )        //成立条件：获取数据不为空&原本容器不包含现获取数据
						{
							SceneryConstant.PAGE++;
							LogUtils.d(hotel.result.get(0).address);
							mDatas.addAll(hotel.result);
							mEmptyWrapper.notifyDataSetChanged();
						} else
						{
							ToastUtils.showShort(hotel.reason);
							LogUtils.d(hotel.reason);
						}
					}
					
					@Override
					public void onError ( Throwable e )
					{
						LogUtils.d(e.toString());
					}
					
					@Override
					public void onComplete ()
					{
						ToastUtils.showShort(mContext,getString(R.string.addComplete));
					}
				});
	}
	
	/**
	 * 从网络获取
	 */
	private Observable< Hotel > getHotelDataByNet ( final int cityId, final int page, final boolean paybyvas )
	{
		
		return RetrofitHotel
				       .getInstance()
				       .getHotel(cityId, page, paybyvas)
				       .compose(this.bindToLifecycle());
	}
	
	private void initRecycleView ()
	{
		/**设置RecyclerView垂直放置内容*/
		mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
		
		
		mAdapter = new CommonAdapter< Hotel.ResultBean >(getActivity(), R.layout.item_hotel, mDatas)
		{
			@Override
			protected void convert ( ViewHolder holder, Hotel.ResultBean info, int position )
			{
				ImageLoader.loadURLImage(getActivity(), info.largePic, holder.getView(R.id.view_iv_hotel));         //Glide加载URL图片
				holder
						.setText(R.id.title_tv_hotel, info.name)
						.setText(R.id.money_tv_hotel, info.dpNum.concat("元起"))
						.setText(R.id.satisfaction_tv_hotel, "满意度:".concat(info.manyidu))
						.setText(R.id.address_tv_hotel, "地址:".concat(info.address))
						.setText(R.id.intro_tv_hotel, info.intro);
			}
		};
		
		mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener()
		{
			@Override
			public void onItemClick ( View view, RecyclerView.ViewHolder holder, int position )
			{
				String url = mDatas.get(position).url;
				String title = mDatas.get(position).name;
				LogUtils.d(url);
				
				/**build-WebView**/
				
				Utils.showWeb(mContext,title, url);

//				Intent intent = new Intent(mContext, WebActivity.class);
//				intent.putExtra("URL",url);
//				startActivity(intent);
			}
			
			@Override
			public boolean onItemLongClick ( View view, RecyclerView.ViewHolder holder, int position )
			{
				return false;
			}
		});
		
		mEmptyWrapper = new EmptyWrapper< Hotel.ResultBean >(mAdapter);
		mEmptyWrapper.setEmptyView(LayoutInflater.from(mContext).inflate(R.layout.view_empty, mRecyclerView, false));

//		mLoadMoreWrapper = new LoadMoreWrapper(mEmptyWrapper);
//		mLoadMoreWrapper.setLoadMoreView(LayoutInflater.from(mContext).inflate(R.layout.view_loading, mRecyclerView, false));
//		mLoadMoreWrapper.setOnLoadMoreListener(() ->
//		{
//			new Handler().postDelayed(() ->
//			{
//				loadData();
//				mLoadMoreWrapper.notifyDataSetChanged();            /**必须放在线程内，防止UI无响应*/
//			}, 1500);
//		});

//		mRecyclerView.setAdapter(mLoadMoreWrapper);
		
		mRecyclerView.setAdapter(mEmptyWrapper);
		
		
	}
	
	/**
	 *
	 */
	private void initRefreshLayout ()
	{
		BezierLayout headerView = new BezierLayout(mContext);
		mTwinklingRefreshLayout.setHeaderView(headerView);
		mTwinklingRefreshLayout.setOverScrollBottomShow(false);
		
		mTwinklingRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter()
		{
			@Override
			public void onRefresh ( TwinklingRefreshLayout refreshLayout )      //上拉监听
			{
				super.onRefresh(refreshLayout);
				new Handler().postDelayed(() ->
				{
					loadData();
					refreshLayout.finishRefreshing();       //关闭刷新图标
				}, 500);
			}
			
			@Override
			public void onLoadMore ( TwinklingRefreshLayout refreshLayout )     //下拉监听
			{
				super.onLoadMore(refreshLayout);
				new Handler().postDelayed(() ->
				{
					loadData();
					refreshLayout.finishLoadmore();
				}, 500);
			}
		});
	}
	
}


