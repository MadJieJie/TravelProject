package com.fengjie.myapplication.modules.user.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fengjie.myapplication.R;
import com.fengjie.myapplication.modules.tool.base.weather.AbstractFragment;
import com.fengjie.myapplication.view.DefinedMenu;

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

}


