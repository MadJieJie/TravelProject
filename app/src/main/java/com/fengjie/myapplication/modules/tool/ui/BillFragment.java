package com.fengjie.myapplication.modules.tool.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fengjie.myapplication.R;
import com.fengjie.myapplication.adapter.recyclerview.adapter.CommonAdapter;
import com.fengjie.myapplication.adapter.recyclerview.base.ViewHolder;
import com.fengjie.myapplication.modules.tool.base.weather.AbstractRetrofitFragment;
import com.fengjie.myapplication.utils.rxbus.RxBus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Created by MadJieJie on 2017/1/31-21:49.
 * @brief
 * @attention
 */

public class BillFragment extends AbstractRetrofitFragment
{
	
	private RecyclerView mRecyclerView = null;
	private static List< String > mDatas = new ArrayList< String >();
	private Context mContext = null;
//	private TwinklingRefreshLayout mTwinklingRefreshLayout;
	
	private CommonAdapter< String > mAdapter = null;
//	private EmptyWrapper mEmptyWrapper = null;
	
	/**
	 * 工厂模式
	 *
	 * @return 返回新的实例
	 */
	public static BillFragment newInstance ()
	{
		Bundle args = new Bundle();
		
		BillFragment fragment = new BillFragment();
		fragment.setArguments(args);
		return fragment;
	}
	
	@Nullable
	@Override
	public View onCreateView ( LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState )
	{
		if ( mView == null )
		{
			mView = inflater.inflate(R.layout.fragment_bill, container, false);
			mContext = getContext();
			findView(mView);
		}
		mIsCreateView = true;
		
		return mView;
	}
	
	@Override
	public void onViewCreated ( View view, @Nullable Bundle savedInstanceState )
	{
		super.onViewCreated(view, savedInstanceState);
		
		initRecycleView();
//		initRefreshLayout();
//		initRxBus();
	}
	
	@Override
	public void onDestroy ()
	{
		super.onDestroy();
		RxBus.getInstance().unRegister(this);
	}
	
	private void findView ( View view )
	{
		mRecyclerView = ( RecyclerView ) view.findViewById(R.id.content_rv_bill);
//		mTwinklingRefreshLayout = ( TwinklingRefreshLayout ) view.findViewById(R.id.refresh_trl_scenery);
	}
	
	private void initRecycleView ()
	{
		mDatas = Arrays.asList("1","2","3","4","5","6","7","8","9");
		
		/**设置RecyclerView垂直放置内容*/
		mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.HORIZONTAL,false));
		
		
		mAdapter = new CommonAdapter< String >(mContext,R.layout.item_bill,mDatas)
		{
			@Override
			protected void convert ( ViewHolder holder, String info, int position )
			{
				holder.setText(R.id.name_btn_bill,info);
			}
		};
		mRecyclerView.setAdapter(mAdapter);
		
	}
//
	
	private void initRxBus ()
	{
//		RxBus.getInstance()
//				.register(this)
//				.tObservable(Event.class)
//				.observeOn(AndroidSchedulers.mainThread())
//				.subscribe(
//						event ->
//						{
//							if ( event.getEvent() == Event.EVENT_CHANGE_CITY )
//							{
//								SceneryConstant.PAGE = 1;       //将页数置为1
//								mDatas.clear(); //清除所有对象
//								lazyLoad();     //重新导入数据
//							}
//
//						}
//				);
	}
	
	/**
	 * 懒加载
	 */
	@Override
	protected void lazyLoad ()
	{
//		if ( mTwinklingRefreshLayout != null )
//			mTwinklingRefreshLayout.startRefresh();
	}

//	@Override
//	protected void onFragmentVisibleChange ( boolean isVisible )
//	{
//		if ( isVisible )    //   do things when fragment is visible
//		{
////			mTwinklingRefreshLayout.startRefresh();
////			initView();
////			initRecycleView();
////			initRefreshLayout();
////			loadData();
//		} else
//		{
////			mTwinklingRefreshLayout.finishRefreshing();
//		}
//	}
	
	/**
	 * 导入景点列表数据
	 */
//	private void loadData ()
//	{
//		/**观察者**/
//		getSceneryDataByNet(
//				SharedPreferenceUtil.getInstance().getCityProId(), SharedPreferenceUtil.getInstance().getCityId(), SceneryConstant.PAGE, false)
//				.subscribe(new Observer< Scenery >()
//				{
//					@Override
//					public void onSubscribe ( Disposable d )
//					{
//
//					}
//
//					@Override
//					public void onNext ( Scenery scenery )  //scenery must not null,because emitter can not send null object.
//					{
//
//						if ( scenery.result != null && ! mDatas.contains(scenery.result) )        //成立条件：获取数据不为空&原本容器不包含现获取数据
//						{
//							SceneryConstant.PAGE++;
//							LogUtils.d(scenery.result.get(0).address);
//							mDatas.addAll(scenery.result);
//							mEmptyWrapper.notifyDataSetChanged();
//						} else
//						{
//							ToastUtils.showShort(mContext, scenery.reason);
//							LogUtils.d(scenery.reason);
//						}
//					}
//
//					@Override
//					public void onError ( Throwable e )
//					{
//						LogUtils.d(e.toString());
//					}
//
//					@Override
//					public void onComplete ()
//					{
//						ToastUtils.showShort(mContext, getString(R.string.addComplete));
//					}
//				});
//	}
//
//	/**
//	 * 从网络获取景点列表数据
//	 *
//	 * @param proId    API need.
//	 * @param cityId   API need.
//	 * @param page     第几页
//	 * @param paybyvas 是否密文
//	 * @return
//	 */
//	private Observable< Scenery > getSceneryDataByNet ( final int proId, final int cityId, final int page, final boolean paybyvas )
//	{
//
//		return RetrofitScenery.getInstance()
//				       .getScenery(proId, cityId, page, paybyvas)
//				       .compose(this.bindToLifecycle());   /**更新天气处*/
//	}
//

//		mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener()
//		{
//			@Override
//			public void onItemClick ( View view, RecyclerView.ViewHolder holder, int position )
//			{
//				String url = mDatas.get(position).url;
//				String title = mDatas.get(position).title;
//				LogUtils.d(url);
//
//				/**build-WebView**/
//
//				Utils.showWeb(mContext,title, url);
//
////				Intent intent = new Intent(mContext, WebActivity.class);
////				intent.putExtra("URL",url);
////				startActivity(intent);
//			}
//
//			@Override
//			public boolean onItemLongClick ( View view, RecyclerView.ViewHolder holder, int position )
//			{
//				return false;
//			}
//		});
//
//		mEmptyWrapper = new EmptyWrapper< Scenery.ResultBean >(mAdapter);
//		mEmptyWrapper.setEmptyView(LayoutInflater.from(mContext).inflate(R.layout.view_empty, mRecyclerView, false));
//
//		mRecyclerView.setAdapter(mEmptyWrapper);
//
//
//	}
//
//	/**
//	 * 初始化上下拉刷新布局
//	 */
//	private void initRefreshLayout ()
//	{
//		BezierLayout headerView = new BezierLayout(mContext);
//		mTwinklingRefreshLayout.setHeaderView(headerView);
//		mTwinklingRefreshLayout.setOverScrollBottomShow(false);
//
//		mTwinklingRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter()
//		{
//			@Override
//			public void onRefresh ( TwinklingRefreshLayout refreshLayout )      //上拉监听
//			{
//				super.onRefresh(refreshLayout);
//				new Handler().postDelayed(() ->
//				{
//					loadData();
//					refreshLayout.finishRefreshing();       //关闭刷新图标
//				}, 500);
//			}
//
//			@Override
//			public void onLoadMore ( TwinklingRefreshLayout refreshLayout )     //下拉监听
//			{
//				super.onLoadMore(refreshLayout);
//				new Handler().postDelayed(() ->
//				{
//					loadData();
//					refreshLayout.finishLoadmore();
//				}, 500);
//			}
//		});
//	}
	
}


