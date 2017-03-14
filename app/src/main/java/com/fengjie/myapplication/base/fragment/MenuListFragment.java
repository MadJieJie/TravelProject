
package com.fengjie.myapplication.base.fragment;

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
import com.fengjie.myapplication.event.Event;
import com.fengjie.myapplication.modules.city.ui.ChoiceCityActivity;
import com.fengjie.myapplication.modules.note.ui.NoteListActivity;
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
						} else if ( item.getItemId() == R.id.menu_unregister )
						{
							RxBus.getInstance()
									.post(new Event(Event.EVENT_UNREGISTER_USER));
						} else if ( item.getItemId() == R.id.menu_writeNote )
						{
							startActivity(new Intent(getContext(), NoteListActivity.class));
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
