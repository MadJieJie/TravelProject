package com.fengjie.myapplication.modules.travel.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.fengjie.myapplication.modules.tool.base.weather.AbstractFragment;
import com.fengjie.myapplication.utils.ToastUtils;
import com.fengjie.myapplication.view.DefinedMenu;
import com.yalantis.contextmenu.lib.MenuObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Created by MadJieJie on 2017/1/31-21:49.
 * @brief
 * @attention
 */

public class TravelFragment extends AbstractFragment
{

	private RecyclerView mRecyclerView;
	private List< String > mData = null;
	private CommonAdapter< String > mAdapter;

	public static TravelFragment newInstance ()
	{
		Bundle args = new Bundle();

		TravelFragment fragment = new TravelFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Nullable
	@Override
	public View onCreateView ( LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState )
	{
		View view = inflater.inflate(R.layout.fragment_travel, container, false);
		findView(view);
		initView();
		initMenu(getString(R.string.travel), getMenuObjects(), this, DefinedMenu.TITLE_MENU);
		return view;
	}


	@Override
	protected void findView ( View view )
	{
		mMenu = ( DefinedMenu ) view.findViewById(R.id.menu_view_travel);
		mRecyclerView = ( RecyclerView ) view.findViewById(R.id.content_recyclerView_travel);
	}

	@Override
	protected void initView ()
	{
		mData = Arrays.asList("11", "112", "113", "54");
		mAdapter = new CommonAdapter< String >(mContext, R.layout.item_travel, mData)
		{

			@Override
			protected void convert ( ViewHolder holder, String info, int position )
			{
				holder.setText(R.id.number_tv_travel, info);
			}
		};
		mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));      //设置RecyclerView垂直显示
		mRecyclerView.setAdapter(mAdapter);
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
			case AbstractFragment.FIRST:
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
}


