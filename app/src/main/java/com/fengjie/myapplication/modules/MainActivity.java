package com.fengjie.myapplication.modules;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.fengjie.myapplication.R;
import com.fengjie.myapplication.base.fragment.MenuListFragment;
import com.fengjie.myapplication.event.Event;
import com.fengjie.myapplication.utils.often.LogUtils;
import com.fengjie.myapplication.utils.rxbus.RxBus;
import com.mxn.soul.flowingdrawer_core.FlowingDrawer;

import io.reactivex.android.schedulers.AndroidSchedulers;
import me.yokeyword.fragmentation.SupportActivity;
import me.yokeyword.fragmentation.SupportFragment;
import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator;
import me.yokeyword.fragmentation.anim.FragmentAnimator;
import me.yokeyword.fragmentation.helper.FragmentLifecycleCallbacks;

public class MainActivity extends SupportActivity implements View.OnClickListener
{
	private FlowingDrawer mFlowingDrawer = null;
	private MenuListFragment mMenuFragment = null;
	
	@Override
	protected void onCreate ( Bundle savedInstanceState )
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		RxBus.getInstance().register(this);         //注册RxBus
		
		findView();
		initView();
		createFragment(savedInstanceState);     //先创建主Fragment管理其他Fragment
		initMenu();
		initRxBus();
	}
	
	@Override
	protected void onDestroy ()
	{
		super.onDestroy();
		RxBus.getInstance().unRegister(this);
	}
	
	private void createFragment ( Bundle savedInstanceState )
	{
		if ( savedInstanceState == null )
		{
			loadRootFragment(R.id.container_frameLayout_main, MainFragment.newInstance());
			LogUtils.d("isNeedToCreateFragment-loadRootFragment()");
		}
		
		registerFragmentLifecycleCallbacks(new FragmentLifecycleCallbacks()
		{
			
			@Override
			public void onFragmentSupportVisible ( SupportFragment fragment )
			{
			}
			
			@Override
			public void onFragmentCreated ( SupportFragment fragment, Bundle savedInstanceState )
			{
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
	
	/**
	 * 初始化左侧Menu
	 */
	private void initMenu ()
	{
		mFlowingDrawer = ( FlowingDrawer ) findViewById(R.id.LeftMenu_drawerLayout_main);
		FragmentManager fm = getSupportFragmentManager();
		mMenuFragment = ( MenuListFragment ) fm.findFragmentById(R.id.menu_fl_main);
		if ( mMenuFragment == null )
		{
			mMenuFragment = new MenuListFragment();
			fm.beginTransaction().add(R.id.menu_fl_main, mMenuFragment).commit();
		}
		
	}
	
	private void initRxBus ()
	{
		RxBus.getInstance()
				.tObservable(Event.class)
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(event ->
				{
					if ( event.getEvent() == Event.EVENT_CHANGE_CITY || event.getEvent() == Event.EVENT_UNREGISTER_USER )
					{
						mFlowingDrawer.closeMenu(true);
						LogUtils.d("Debug", "initRxBus: " + "toggleMenu()");
					}
				});
		
	}
	
}
