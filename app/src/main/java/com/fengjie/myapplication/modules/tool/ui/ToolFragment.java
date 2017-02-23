package com.fengjie.myapplication.modules.tool.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fengjie.myapplication.R;
import com.fengjie.myapplication.modules.run.adapter.HomePagerAdapter;
import com.fengjie.myapplication.modules.tool.base.weather.AbstractFragment;
import com.fengjie.myapplication.utils.ToastUtils;
import com.fengjie.myapplication.view.DefinedMenu;
import com.yalantis.contextmenu.lib.MenuObject;

import java.util.ArrayList;
import java.util.List;

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
		mView = inflater.inflate(R.layout.fragment_tool, container, false);

		return mView;
	}

	@Override
	public void onViewCreated ( View view, @Nullable Bundle savedInstanceState )
	{
		super.onViewCreated(view, savedInstanceState);
		findView(view);
//		initMenu(getString(R.string.tool), getMenuObjects(), this, DefinedMenu.TAB_MENU);
		initView();
	}

	@Override
	protected void findView ( View view )
	{
		mContext = getActivity();
		mMenu = ( DefinedMenu ) view.findViewById(R.id.menu_view_tool);
		mViewPager = ( ViewPager ) view.findViewById(R.id.content_viewPager_tool);

//		mRecyclerView = ( RecyclerView ) view.findViewById(R.id.weather_recyclerView_tool);

	}


	@Override
	protected void initView ()
	{
		HomePagerAdapter homePagerAdapter = new HomePagerAdapter(getFragmentManager());
		homePagerAdapter.addTab(new WeatherFragment(), getString(R.string.weather));
		homePagerAdapter.addTab(new WeatherFragment(), getString(R.string.scenery));
		homePagerAdapter.addTab(new WeatherFragment(), getString(R.string.billBook));
		mViewPager.setAdapter(homePagerAdapter);
//		mMenu.setupWithViewPager(mViewPager);
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



	@Override
	protected List< MenuObject > getMenuObjects ()
	{
		List< MenuObject > menuObjects = new ArrayList<>();

		MenuObject close = new MenuObject();
		close.setResource(R.mipmap.btn_close);


		MenuObject send = new MenuObject("Send message");
		send.setResource(R.mipmap.btn_add);

		MenuObject like = new MenuObject("Like profile");
		Bitmap b = BitmapFactory.decodeResource(getResources(), R.mipmap.btn_add);
		like.setBitmap(b);

		menuObjects.add(close);
		menuObjects.add(send);
		menuObjects.add(like);
		return menuObjects;
	}

	@Override
	public void onMenuItemClick ( View clickedView, int position )
	{
		switch ( position )
		{
			case 0:
				ToastUtils.showShort(mContext, "close");
				break;
			case 1:
				ToastUtils.showShort(mContext, "1");
				break;
			case 2:
				ToastUtils.showShort(mContext, "2");
				break;
		}
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


