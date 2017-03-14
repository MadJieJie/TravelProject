package com.fengjie.myapplication.modules.tool.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fengjie.myapplication.R;
import com.fengjie.myapplication.base.fragment.AbstractFragment;
import com.fengjie.myapplication.modules.run.adapter.HomePagerAdapter;
import com.fengjie.myapplication.view.DefinedMenu;

/**
 * @author Created by MadJieJie on 2017/1/31-21:49.
 * @brief
 * @attention
 */

public class ToolFragment extends AbstractFragment
{
	
	private ViewPager mViewPager;
	private View mView;
	
	public static ToolFragment newInstance ()
	{
		Bundle args = new Bundle();
		
		ToolFragment fragment = new ToolFragment();
		fragment.setArguments(args);
		return fragment;
	}
	
	@Nullable
	@Override
	public View onCreateView ( LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState )
	{
		if ( mView == null )
		{
			mView = inflater.inflate(R.layout.fragment_tool, container, false);
//			Utils.setWindowStatusBarColor(getActivity(),R.color.color_str_tool);
		}
		
		return mView;
	}
	
	@Override
	public void onViewCreated ( View view, @Nullable Bundle savedInstanceState )
	{
		super.onViewCreated(view, savedInstanceState);
		findView(view);
		initMenu(getString(R.string.tool), DefinedMenu.TAB_MENU);
		initView();
	}
	
	
	
	@Override
	protected void findView ( View view )
	{
		mMenu = ( DefinedMenu ) view.findViewById(R.id.menu_view_tool);
		mViewPager = ( ViewPager ) view.findViewById(R.id.content_viewPager_tool);

//		mRecyclerView = ( RecyclerView ) view.findViewById(R.id.weather_recyclerView_tool);
		
	}
	
	
	@Override
	protected void initView ()
	{
		HomePagerAdapter homePagerAdapter = new HomePagerAdapter(getFragmentManager());
		homePagerAdapter.addTab(WeatherFragment.newInstance(), getString(R.string.weather));
		homePagerAdapter.addTab(SceneryFragment.newInstance(), getString(R.string.scenery));
		homePagerAdapter.addTab(BillFragment.newInstance(), getString(R.string.billBook));
		mViewPager.setAdapter(homePagerAdapter);
		mMenu.setupWithViewPager(mViewPager);
//		mMenu.setLeftButtonListener(v -> {
//
//
//		});

//		initFragment();
	}
	
	
	private void initFragment ()
	{
//		getFragmentManager().beginTransaction().replace(R.id.weatherContent_fl_tool, WeatherFragment.newInstance()).commit();       //must commit can effect
		Log.d("Debug", "initFragment: ");
	}


//	@Override
//	public void onBackPressed ()
//	{
//		if ( mMenuDialogFragment != null && mMenuDialogFragment.isAdded() )
//		{
//			mMenuDialogFragment.dismiss();
//		} else
//		{
//			finish();
//		}
//	}
	
}


