package com.fengjie.myapplication.modules.travel.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fengjie.myapplication.R;
import com.fengjie.myapplication.adapter.recyclerview.adapter.CommonAdapter;
import com.fengjie.myapplication.base.fragment.AbstractFragment;
import com.fengjie.myapplication.modules.travel.bean.TravelNote;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Created by MadJieJie on 2017/1/31-21:49.
 * @brief
 * @attention
 */

public class OldTravelFragment extends AbstractFragment
{
	
	private List< TravelNote > mData = new ArrayList<TravelNote>();
	private CommonAdapter< TravelNote > mAdapter = null;
	private View mView = null;
	private ViewPager mViewPager;
	
	public static OldTravelFragment newInstance ()
	{
		Bundle args = new Bundle();
		
		OldTravelFragment fragment = new OldTravelFragment();
		fragment.setArguments(args);
		return fragment;
	}
	
	@Nullable
	@Override
	public View onCreateView ( LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState )
	{
		if ( mView == null )
		{
			mView = inflater.inflate(R.layout.fragment_travel, container, false);
		}
		
		return mView;
	}
	
	
	@Override
	public void onViewCreated ( View view, @Nullable Bundle savedInstanceState )
	{
		super.onViewCreated(view, savedInstanceState);
		findView(mView);
//		initMenu(null, DefinedMenu.TAB_MENU);
		initView();
	}
	
	
	@Override
	protected void findView ( View view )
	{
//		mMenu = ( DefinedMenu ) mView.findViewById(R.id.menu_view_travel);
//		mViewPager = ( ViewPager ) mView.findViewById(R.id.content_vp_travel);
	}
	
	
	@Override
	protected void initView ()
	{
//		HomePagerAdapter homePagerAdapter = new HomePagerAdapter(getFragmentManager());
//		homePagerAdapter.addTab(MyTravelNoteFragment.newInstance(), getString(R.string.myTravelNote));
//		homePagerAdapter.addTab(OtherTravelNoteFragment.newInstance(), getString(R.string.otherTravelNote));
//		mViewPager.setAdapter(homePagerAdapter);
//		mMenu.setupWithViewPager(mViewPager);
	}
	
	
	
}


