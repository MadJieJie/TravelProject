package com.fengjie.myapplication.modules.tool.base.weather;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.trello.rxlifecycle2.components.support.RxFragment;


/**
 * Created by HugoXie on 16/7/9.
 * <p>
 * Info:
 */
public abstract class AbstractRetrofitFragment extends RxFragment
{
	
	protected boolean mIsCreateView = false;
	protected boolean mIsInitView = false;
//    private boolean isCreateView = false;   //View是否初始化标志，防止回调函数在rootView为空的时候触发
//    private boolean isFragmentVisible = false;
	
	protected View mView = null;
	
	
	/**
	 * 此方法在控件初始化前调用，所以不能在此方法中直接操作控件会出现空指针
	 *
	 * @param isVisibleToUser 当变换Fragment时,所有ViewPager的Fragment参数isVisibleToUser=FALSE，然后再将显示的Fragment参数isVisibleToUser=TRUE
	 */
	@Override
	public void setUserVisibleHint ( boolean isVisibleToUser )
	{
		super.setUserVisibleHint(isVisibleToUser);
		if ( ! mIsInitView && isVisibleToUser && mIsCreateView )
		{
			lazyLoad();
			mIsInitView = true;
		}

//        LogUtils.d("setUserVisibleHint() -> isVisibleToUser: " + isVisibleToUser);
//        if ( mView == null )
//        {
//            return;
//        }
//
//        isCreateView = true;
//
//        if ( isVisibleToUser )
//        {
//            onFragmentVisibleChange(true);
//            isFragmentVisible = true;
//            return;
//        }
//
//        if ( isFragmentVisible )
//        {
//            onFragmentVisibleChange(false);
//            isFragmentVisible = false;
//        }
	}
	
	@Override
	public void onActivityCreated ( @Nullable Bundle savedInstanceState )
	{
		super.onActivityCreated(savedInstanceState);
		//第一个fragment会调用
//		if ( getUserVisibleHint() )
//		{
//			lazyLoad();
//		}
	}

//	@Override
//	public void onViewCreated ( View view, @Nullable Bundle savedInstanceState )
//	{
//		super.onViewCreated(view, savedInstanceState);
////        if (!isCreateView && getUserVisibleHint()) {
////            onFragmentVisibleChange(true);
////            isFragmentVisible = true;
////        }
//
//
//	}
	
	/**
	 * 设置ToolBar标题
	 *
	 * @param title
	 */
	protected void safeSetTitle ( String title )
	{
		ActionBar appBarLayout = ( ( AppCompatActivity ) getActivity() ).getSupportActionBar();
		if ( appBarLayout != null )
		{
			appBarLayout.setTitle(title);
		}
	}
	
	/**
	 * 加载数据操作,在视图创建之前初始化
	 */
	protected abstract void lazyLoad ();


//    private void initVariable()
//    {
//        isCreateView =false;
//        isFragmentVisible = false;
//    }
	
	/**
	 * 当前fragment可见状态发生变化时会回调该方法
	 * 如果当前fragment是第一次加载，等待onCreateView后才会回调该方法，其它情况回调时机跟 {@link #setUserVisibleHint(boolean)}一致
	 * 在该回调方法中你可以做一些加载数据操作，甚至是控件的操作，因为配合fragment的view复用机制，你不用担心在对控件操作中会报 null 异常
	 *
	 * @param isVisible true  不可见 -> 可见 false 可见  -> 不可见
	 */
//    protected abstract void onFragmentVisibleChange ( boolean isVisible );
}
