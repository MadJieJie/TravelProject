package com.fengjie.myapplication.modules.user.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fengjie.myapplication.R;
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

public class UserFragment extends AbstractFragment
{

	public static UserFragment newInstance ()
	{
		Bundle args = new Bundle();

		UserFragment fragment = new UserFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Nullable
	@Override
	public View onCreateView ( LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState )
	{
		View view = inflater.inflate(R.layout.fragment_login, container, false);
//		findView(view);
//		initView();
//		initMenu(getString(R.string.user),getMenuObjects(),this);
		return view;
	}


	@Override
	protected void findView ( View view )
	{
		mMenu = ( DefinedMenu ) view.findViewById(R.id.menu_view_user);
	}

	@Override
	protected void initView ()
	{

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
		switch(position)
		{
			case 0:
				ToastUtils.showShort(mContext,"close");
				break;
			case 1:
				ToastUtils.showShort(mContext,"1");
				break;
			case 2:
				ToastUtils.showShort(mContext,"2");
				break;
		}
	}
}


