
package com.fengjie.myapplication.base.fragment;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.fengjie.myapplication.R;
import com.fengjie.myapplication.base.AbstractRxPermissionEvent;
import com.fengjie.myapplication.base.BaseApplication;
import com.fengjie.myapplication.event.Event;
import com.fengjie.myapplication.modules.city.ui.ChoiceCityActivity;
import com.fengjie.myapplication.modules.run.ui.NewNoteActivity;
import com.fengjie.myapplication.modules.travel.ui.NewTravelNoteActivity;
import com.fengjie.myapplication.utils.often.Utils;
import com.fengjie.myapplication.utils.rxbus.RxBus;

/**
 * Created by MadJieJie on 2016/12/13. MenuListFragment
 */

public class MenuListFragment extends Fragment
{
	private View mView = null;
	
	private ImageView ivMenuUserProfilePhoto;
	
	public MenuListFragment ()
	{
	}

//	public MenuListFragment ( NavigationView.OnNavigationItemSelectedListener listener )
//	{
//		this.listener = listener;
//	}
	
	@Override
	public void onCreate ( @Nullable Bundle savedInstanceState )
	{
		super.onCreate(savedInstanceState);
		RxBus.getInstance().register(this);
	}
	
	
	@Override
	public View onCreateView ( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
	{
		if ( mView == null )
		{
			mView = inflater.inflate(R.layout.fragment_menu, container, false);
			
			ivMenuUserProfilePhoto = ( ImageView ) mView.findViewById(R.id.ivMenuUserProfilePhoto);
			NavigationView vNavigation = ( NavigationView ) mView.findViewById(R.id.vNavigation);
			vNavigation.setNavigationItemSelectedListener(
					item ->
					{
						if ( item.getItemId() == R.id.menu_city )
						{
							startActivity(new Intent(getContext(), ChoiceCityActivity.class));
						} else if ( item.getItemId() == R.id.menu_quit )
						{
							getActivity().finish();
						} else if ( item.getItemId() == R.id.menu_unregister )      //注销
						{
							BaseApplication.sUserName = "anonymity";
							RxBus.getInstance()
									.post(new Event(Event.EVENT_UNREGISTER_USER));
						} else if ( item.getItemId() == R.id.menu_writeNote )       //写备忘
						{
							Utils.isGetRxPermission(getActivity(), new AbstractRxPermissionEvent()
							{
								@Override
								public void canGetPermissionEvent ()
								{
									startActivity(new Intent(getContext(), NewNoteActivity.class));
								}
								
								@Override
								public void notGetPermissionEvent ()
								{
									
								}
							}, Manifest.permission.WRITE_EXTERNAL_STORAGE);     //若获得写入权限,则实现跳转
						} else if ( item.getItemId() == R.id.menu_writeTravelNote ) //写游记
						{
							Utils.isGetRxPermission(getActivity(), new AbstractRxPermissionEvent()
							{
								@Override
								public void canGetPermissionEvent ()
								{
									startActivity(new Intent(getContext(), NewTravelNoteActivity.class));
								}
								
								@Override
								public void notGetPermissionEvent ()
								{
									
								}
							}, Manifest.permission.WRITE_EXTERNAL_STORAGE);     //若获得写入权限,则实现跳转
						}
						return false;
					}
			);
			setupHeader();      //设置上端导航条
		}
		
		return mView;
	}
	
	@Override
	public void onDestroyView ()
	{
		super.onDestroyView();
		RxBus.getInstance().unRegister(this);
	}
	
	
	private void setupHeader ()
	{
//        int avatarSize = getResources().getDimenlsionPixelSize(R.dimen.global_menu_avatar_size);
//        String profilePhoto = getResources().getString(R.string.user_profie_photo);
//        Picasso.with(getActivity())
//                .load(profilePhoto)
//                .placeholder(R.drawable.img_circle_placeholder)
//                .resize(avatarSize, avatarSize)
//                .centerCrop()
//                .transform(new CircleTransformation())
//                .into(ivMenuUserProfilePhoto);
	}
	
}
