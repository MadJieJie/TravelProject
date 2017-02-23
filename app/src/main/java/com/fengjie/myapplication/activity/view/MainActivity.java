package com.fengjie.myapplication.activity.view;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.fengjie.myapplication.R;
import com.fengjie.myapplication.base.MenuListFragment;
import com.fengjie.myapplication.modules.MainFragment;
import com.fengjie.myapplication.utils.LogUtils;

import me.yokeyword.fragmentation.SupportActivity;
import me.yokeyword.fragmentation.SupportFragment;
import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator;
import me.yokeyword.fragmentation.anim.FragmentAnimator;
import me.yokeyword.fragmentation.helper.FragmentLifecycleCallbacks;

public class MainActivity extends SupportActivity implements View.OnClickListener
{


	@Override
	protected void onCreate ( Bundle savedInstanceState )
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		findView();
		initView();
		createFragment(savedInstanceState);
		setupMenu();
	}

	private void createFragment(Bundle savedInstanceState)
	{
		if (savedInstanceState == null) {
			loadRootFragment(R.id.container_frameLayout_main, MainFragment.newInstance());
			LogUtils.d("isNeedToCreateFragment-loadRootFragment()");
		}

		registerFragmentLifecycleCallbacks(new FragmentLifecycleCallbacks() {

			@Override
			public void onFragmentSupportVisible(SupportFragment fragment) {
			}

			@Override
			public void onFragmentCreated(SupportFragment fragment, Bundle savedInstanceState) {
				super.onFragmentCreated(fragment, savedInstanceState);
			}
			// 省略其余生命周期方法
		});
	}

	private void findView ()
	{

	}

	private void initView ()
	{


//		if ( Build.VERSION.SDK_INT >= 21 )                              //System must be more than 5.0
//		{
//			View decorView = getWindow().getDecorView();                //get screen View.
//			int UIFlag = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN          //use UI flag full screen and display stable.
////					             | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//					             | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
//			decorView.setSystemUiVisibility(UIFlag);
////			getWindow().setNavigationBarColor(Color.TRANSPARENT);
//			getWindow().setStatusBarColor(Color.TRANSPARENT);           //as transparent.
//		}


	}

	@Override
	public void onClick ( View view )
	{
		switch ( view.getId() )
		{
//			case R.id.btn1:
			default:
				break;
		}
	}

	@Override
	public void onExceptionAfterOnSaveInstanceState ( Exception e )
	{
		// TODO: 16/12/7 在此可以监听到警告： Can not perform this action after onSaveInstanceState!
		// 建议在线上包中，此处上传到异常检测服务器（eg. 自家异常检测系统或Bugtags等崩溃检测第三方），来监控该异常
	}

	@Override
	public void onBackPressedSupport ()
	{
		// 对于 4个类别的主Fragment内的回退back逻辑,已经在其onBackPressedSupport里各自处理了
		super.onBackPressedSupport();
	}

	@Override
	public FragmentAnimator onCreateFragmentAnimator ()
	{
		// 设置横向(和安卓4.x动画相同)
		return new DefaultHorizontalAnimator();
	}

	private void setupMenu()
	{
		FragmentManager fm = getSupportFragmentManager();
		MenuListFragment mMenuFragment = ( MenuListFragment ) fm.findFragmentById(R.id.menu_fl_main);
		if ( mMenuFragment == null )
		{
			mMenuFragment = new MenuListFragment();
			fm.beginTransaction().add(R.id.menu_fl_main, mMenuFragment).commit();
		}
	}

}
