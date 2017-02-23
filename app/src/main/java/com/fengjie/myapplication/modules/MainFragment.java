package com.fengjie.myapplication.modules;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.fengjie.myapplication.R;
import com.fengjie.myapplication.base.fragment.IInitView;
import com.fengjie.myapplication.modules.run.ui.RunFragment;
import com.fengjie.myapplication.modules.tool.ui.ToolFragment;
import com.fengjie.myapplication.modules.travel.ui.TravelFragment;
import com.fengjie.myapplication.modules.user.ui.UserFragment;
import com.fengjie.myapplication.utils.ToastUtils;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;

import me.yokeyword.fragmentation.SupportFragment;

/**
 * @author Created by MadJieJie on 2017/2/1-9:35.
 * @brief
 * @attention
 */

public class MainFragment extends SupportFragment implements IInitView
{
//	private FlowingDrawer mDrawer;

	/** Widget **/

	private BottomBar mBottomBar;

	/** Fragment */
	public static final int TOOL_FRAGMENT = 0;
	public static final int RUN_FRAGMENT = 1;
	public static final int TRAVEL_FRAGMENT = 2;
	public static final int USER_FRAGMENT = 3;
	private SupportFragment[] mFragments = new SupportFragment[4];

	/** Parameters */
	private Context mContext;
	// 再点一次退出程序时间设置
	private static final long WAIT_TIME = 2000L;
	private long TOUCH_TIME = 0;
	private int SelectedTabNumber = USER_FRAGMENT;

	public static MainFragment newInstance ()
	{

		Bundle args = new Bundle();

		MainFragment fragment = new MainFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Nullable
	@Override
	public View onCreateView ( LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState )
	{

		View view = inflater.inflate(R.layout.fragment_main, container, false);
		mContext = getContext();

		if ( savedInstanceState == null )
		{
			mFragments[TOOL_FRAGMENT] = ToolFragment.newInstance();
			mFragments[RUN_FRAGMENT] = RunFragment.newInstance();
			mFragments[TRAVEL_FRAGMENT] = TravelFragment.newInstance();
			mFragments[USER_FRAGMENT] = UserFragment.newInstance();

			loadMultipleRootFragment(R.id.container_fl_main, TOOL_FRAGMENT,
					mFragments[TOOL_FRAGMENT],
					mFragments[RUN_FRAGMENT],
					mFragments[TRAVEL_FRAGMENT],
					mFragments[USER_FRAGMENT]);

		} else
		{
			// 这里库已经做了Fragment恢复,所有不需要额外的处理了, 不会出现重叠问题

			// 这里我们需要拿到mFragments的引用,也可以通过getChildFragmentManager.getFragments()自行进行判断查找(效率更高些),用下面的方法查找更方便些
			mFragments[TOOL_FRAGMENT] = findChildFragment(ToolFragment.class);
			mFragments[RUN_FRAGMENT] = findChildFragment(RunFragment.class);
			mFragments[TRAVEL_FRAGMENT] = findChildFragment(TravelFragment.class);
			mFragments[USER_FRAGMENT] = findChildFragment(UserFragment.class);
		}


		return view;
	}

	@Override
	public void onViewCreated ( View view, @Nullable Bundle savedInstanceState )
	{
		super.onViewCreated(view, savedInstanceState);
		findView(view);
		initView();
	}

	@Override
	public void findView ( View view )
	{
		mBottomBar = ( BottomBar ) view.findViewById(R.id.bottomBar_view_main);
	}

	@Override
	public void initView ()
	{
		mBottomBar.setOnTabSelectListener(new OnTabSelectListener()
		{
			@Override
			public void onTabSelected ( @IdRes int tabId )
			{
				switch ( tabId )
				{
					case R.id.tab1:
						showHideFragment(mFragments[TOOL_FRAGMENT], mFragments[SelectedTabNumber]);
						SelectedTabNumber = TOOL_FRAGMENT;
//						ToastUtils.showShort(mContext,"TOOL_FRAGMENT");
						break;
					case R.id.tab2:
						showHideFragment(mFragments[RUN_FRAGMENT], mFragments[SelectedTabNumber]);
						SelectedTabNumber = RUN_FRAGMENT;
//						ToastUtils.showShort(mContext,"RUN_FRAGMENT");
						break;
					case R.id.tab3:
						showHideFragment(mFragments[TRAVEL_FRAGMENT], mFragments[SelectedTabNumber]);
						SelectedTabNumber = TRAVEL_FRAGMENT;
//						ToastUtils.showShort(mContext,"TRAVEL_FRAGMENT");
						break;
					case R.id.tab4:
						showHideFragment(mFragments[USER_FRAGMENT], mFragments[SelectedTabNumber]);
						SelectedTabNumber = USER_FRAGMENT;
//						ToastUtils.showShort(mContext,"USER_FRAGMENT");
						break;
					default:
						break;
				}
			}
		});

		//当前的tab是tab1，而你又点击了tab1，会调用这个方法
		mBottomBar.setOnTabReselectListener(new OnTabReselectListener()
		{
			@Override
			public void onTabReSelected ( @IdRes int tabId )
			{
				switch ( tabId )
				{
					case R.id.tab1:
						ToastUtils.showShort(mContext, "Again-TOOL_FRAGMENT");
						break;
					case R.id.tab2:
						ToastUtils.showShort(mContext, "Again-RUN_FRAGMENT");
						break;
					case R.id.tab3:
						ToastUtils.showShort(mContext, "Again-TRAVEL_FRAGMENT");
						break;
					case R.id.tab4:
						ToastUtils.showShort(mContext, "Again-USER_FRAGMENT");
						break;
				}
			}
		});
//		setupMenu();
	}


	@Override
	protected void onFragmentResult ( int requestCode, int resultCode, Bundle data )
	{
		super.onFragmentResult(requestCode, resultCode, data);
	}


	@Override
	public void onDestroyView ()
	{
		super.onDestroyView();
	}

	/**
	 * 处理回退事件
	 *
	 * @return
	 */
	@Override
	public boolean onBackPressedSupport ()
	{
		if ( System.currentTimeMillis() - TOUCH_TIME < WAIT_TIME )
		{
			_mActivity.finish();
		} else
		{
			TOUCH_TIME = System.currentTimeMillis();
			Toast.makeText(_mActivity, "再按一次退出", Toast.LENGTH_SHORT).show();
		}
		return true;
	}



//	private void setupMenu() {
//		FragmentManager fm = getFragmentManager();
//		MenuListFragment mMenuFragment = (MenuListFragment) fm.findFragmentById(R.id.menu_fl_main);
//		if (mMenuFragment == null) {
//			mMenuFragment = new MenuListFragment();
//			fm.beginTransaction().add(R.id.menu_fl_main, mMenuFragment).commit();
//		}

//        mDrawer.setOnDrawerStateChangeListener(new ElasticDrawer.OnDrawerStateChangeListener() {
//            @Override
//            public void onDrawerStateChange(int oldState, int newState) {
//                if (newState == ElasticDrawer.STATE_CLOSED) {
//                    Log.i("MainActivity", "Drawer STATE_CLOSED");
//                }
//            }
//
//            @Override
//            public void onDrawerSlide(float openRatio, int offsetPixels) {
//                Log.i("MainActivity", "openRatio=" + openRatio + " ,offsetPixels=" + offsetPixels);
//            }
//        });
//	}
}
