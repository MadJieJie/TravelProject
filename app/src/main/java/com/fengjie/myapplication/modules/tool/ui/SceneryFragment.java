package com.fengjie.myapplication.modules.tool.ui;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fengjie.myapplication.R;
import com.fengjie.myapplication.adapter.recyclerview.adapter.CommonAdapter;
import com.fengjie.myapplication.adapter.recyclerview.base.ViewHolder;
import com.fengjie.myapplication.adapter.recyclerview.wrapper.EmptyWrapper;
import com.fengjie.myapplication.base.fragment.IInitView;
import com.fengjie.myapplication.base.scenery.SceneryConstant;
import com.fengjie.myapplication.modules.tool.base.weather.AbstractRetrofitFragment;
import com.fengjie.myapplication.modules.tool.bean.Scenery;
import com.fengjie.myapplication.utils.LogUtils;
import com.fengjie.myapplication.utils.ToastUtils;
import com.fengjie.myapplication.utils.scenery.RetrofitScenery;
import com.fengjie.myapplication.utils.weather.ImageLoader;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.header.bezierlayout.BezierLayout;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * @author Created by MadJieJie on 2017/1/31-21:49.
 * @brief
 * @attention
 */

public class SceneryFragment extends AbstractRetrofitFragment implements IInitView
{

	private RecyclerView mRecyclerView = null;
	private List< Scenery.ResultBean > mDatas = new ArrayList< Scenery.ResultBean >();
	private View mView = null;
	private Context mContext = null;
	private TwinklingRefreshLayout mTwinklingRefreshLayout;

	private CommonAdapter< Scenery.ResultBean > mAdapter = null;
	private EmptyWrapper mEmptyWrapper = null;

	public static SceneryFragment newInstance ()
	{
		Bundle args = new Bundle();

		SceneryFragment fragment = new SceneryFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Nullable
	@Override
	public View onCreateView ( LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState )
	{
		if ( mView == null )
		{
			mView = inflater.inflate(R.layout.fragment_scenery, container, false);
			mContext = getContext();
		}

		return mView;
	}

	@Override
	public void onViewCreated ( View view, @Nullable Bundle savedInstanceState )
	{
		super.onViewCreated(view, savedInstanceState);
		loadData();
		findView(view);
		initView();
		initRecycleView();
		initRefreshLayout();
	}

	@Override
	public void findView ( View view )
	{
		mRecyclerView = ( RecyclerView ) view.findViewById(R.id.content_rv_scenery);
		mTwinklingRefreshLayout = ( TwinklingRefreshLayout ) view.findViewById(R.id.refresh_trl_scenery);
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
		loadData();
	}


	private void loadData ()
	{
		getScenery(31, 385, SceneryConstant.PAGE, false)
				.subscribe(new Observer< Scenery >()        /**观察者**/
				{
					@Override
					public void onSubscribe ( Disposable d )
					{

					}

					@Override
					public void onNext ( Scenery scenery )  //scenery must not null,because emitter can not send null object.
					{

						if ( scenery.result != null && !mDatas.retainAll(scenery.result))
						{
							SceneryConstant.PAGE++;
							LogUtils.d(scenery.result.get(0).address);
							mDatas.addAll(scenery.result);
						} else
						{
							ToastUtils.showShort(scenery.reason);
							LogUtils.d(scenery.reason);
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

					}
				});
	}

	/**
	 * 从网络获取
	 */
	private Observable< Scenery > getScenery ( final int pid, final int cid, final int page, final boolean paybyvas )
	{

		return RetrofitScenery.getInstance()
				       .getScenery(pid, cid, page, paybyvas)
				       .compose(this.bindToLifecycle());   /**更新天气处*/
	}

	private void initRecycleView ()
	{
		/**设置RecyclerView垂直放置内容*/
		mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


		mAdapter = new CommonAdapter< Scenery.ResultBean >(getActivity(), R.layout.item_scenery, mDatas)
		{
			@Override
			protected void convert ( ViewHolder holder, Scenery.ResultBean info, int position )
			{
				ImageLoader.loadURLImage(getActivity(), info.imgurl, holder.getView(R.id.view_iv_scenery));
				holder.setText(R.id.title_tv_scenery, info.title);
			}
		};

//		mEmptyWrapper = new EmptyWrapper< Scenery.ResultBean >(mAdapter);
//		mEmptyWrapper.setEmptyView(LayoutInflater.from(mContext).inflate(R.layout.view_empty, mRecyclerView, false));

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
		mRecyclerView.setAdapter(mAdapter);


	}

	private void initRefreshLayout()
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
				new Handler().postDelayed(()->{
					loadData();
					mAdapter.notifyDataSetChanged();
					refreshLayout.finishRefreshing();
				},1000);
			}

			@Override
			public void onLoadMore ( TwinklingRefreshLayout refreshLayout )     //下拉监听
			{
				super.onLoadMore(refreshLayout);
				new Handler().postDelayed(()->{
					loadData();
					mAdapter.notifyDataSetChanged();
					refreshLayout.finishLoadmore();
				},1000);
			}
		});
	}
}


