
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
import android.widget.Toast;

import com.fengjie.myapplication.R;
import com.fengjie.myapplication.modules.city.ui.ChoiceCityActivity;

/**
 * Created by MadJieJie on 2016/12/13. MenuListFragment
 */

public class MenuListFragment extends Fragment
{

	private ImageView ivMenuUserProfilePhoto;

	@Override
	public void onCreate ( @Nullable Bundle savedInstanceState )
	{
		super.onCreate(savedInstanceState);
	}


	@Override
	public View onCreateView ( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
	{
		View view = inflater.inflate(R.layout.fragment_menu, container, false);

		ivMenuUserProfilePhoto = ( ImageView ) view.findViewById(R.id.ivMenuUserProfilePhoto);
		NavigationView vNavigation = ( NavigationView ) view.findViewById(R.id.vNavigation);
		vNavigation.setNavigationItemSelectedListener(
				item -> {
					startActivity(new Intent(getActivity(), ChoiceCityActivity.class));
					Toast.makeText(getActivity(), item.getTitle(), Toast.LENGTH_SHORT).show();
					return false;
				});
		setupHeader();
		return view;
	}

	private void setupHeader ()
	{
//        int avatarSize = getResources().getDimensionPixelSize(R.dimen.global_menu_avatar_size);
//        String profilePhoto = getResources().getString(R.string.user_profile_photo);
//        Picasso.with(getActivity())
//                .load(profilePhoto)
//                .placeholder(R.drawable.img_circle_placeholder)
//                .resize(avatarSize, avatarSize)
//                .centerCrop()
//                .transform(new CircleTransformation())
//                .into(ivMenuUserProfilePhoto);
	}

}
