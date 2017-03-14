package com.fengjie.myapplication.modules.run.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fengjie.myapplication.R;
import com.fengjie.myapplication.base.fragment.AbstractFragment;
import com.fengjie.myapplication.modules.run.adapter.HomePagerAdapter;
import com.fengjie.myapplication.modules.travel.ui.TravelFragment;
import com.fengjie.myapplication.utils.often.LogUtils;
import com.fengjie.myapplication.view.DefinedMenu;

/**
 * @author Created by MadJieJie on 2017/1/31-21:49.
 * @brief
 * @attention
 */

public class RunFragment extends AbstractFragment
{
	
	private ViewPager mViewPager;
	private View mView = null;
	
	public static RunFragment newInstance ()
	{
		Bundle args = new Bundle();
		
		RunFragment fragment = new RunFragment();
		fragment.setArguments(args);
		return fragment;
	}
	
	@Nullable
	@Override
	public View onCreateView ( LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState )
	{
		if(mView==null)
		{
			mView = inflater.inflate(R.layout.fragment_run, container, false);
		}
		return mView;
	}
	
	
	@Override
	public void onResume ()
	{
		super.onResume();
		LogUtils.d("color_str_run");
	}
	
	@Override
	public void onViewCreated ( View view, @Nullable Bundle savedInstanceState )
	{
		super.onViewCreated(view, savedInstanceState);
		findView(mView);
		initMenu(getString(R.string.travel), DefinedMenu.TAB_MENU);
		initView();
	}
	
	@Override
	protected void findView ( View view )
	{
		mMenu = ( DefinedMenu ) view.findViewById(R.id.menu_view_run);
		mViewPager = ( ViewPager ) view.findViewById(R.id.content_viewPager_run);
	}
	
	@Override
	protected void initView ()
	{
		HomePagerAdapter homePagerAdapter = new HomePagerAdapter(getFragmentManager());
		homePagerAdapter.addTab(HotelFragment.newInstance(), getString(R.string.hotel));
		homePagerAdapter.addTab(TravelFragment.newInstance(), getString(R.string.memorandum));
		mViewPager.setAdapter(homePagerAdapter);
		mMenu.setupWithViewPager(mViewPager);
	}
	
}


