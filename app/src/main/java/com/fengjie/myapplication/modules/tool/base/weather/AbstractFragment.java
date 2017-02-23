package com.fengjie.myapplication.modules.tool.base.weather;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.fengjie.myapplication.R;
import com.fengjie.myapplication.view.DefinedMenu;
import com.yalantis.contextmenu.lib.ContextMenuDialogFragment;
import com.yalantis.contextmenu.lib.MenuObject;
import com.yalantis.contextmenu.lib.MenuParams;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemClickListener;

import java.util.List;

import me.yokeyword.fragmentation.SupportFragment;

/**
 * Created by MadJieJie on 17/2/2.
 */
public abstract class AbstractFragment extends SupportFragment implements OnMenuItemClickListener
{
	/** Parameters **/
	private FragmentManager mFragmentManager;
	private ContextMenuDialogFragment mMenuDialogFragment;
	protected Context mContext;

	protected static final int FIRST = 0;
	protected static final int SECONED = 1;
	protected static final int THIRD = 2;
	protected static final int FOURTH = 3;



	/** Widget **/
	protected DefinedMenu mMenu;


	/**
	 * 引用型变量先实例化
	 *
	 * @param savedInstanceState
	 */
	@Override
	public void onCreate ( @Nullable Bundle savedInstanceState )
	{
		super.onCreate(savedInstanceState);
		mContext = getContext();
		mFragmentManager = getActivity().getSupportFragmentManager();
	}

	protected AbstractFragment initMenu ( String title, List< MenuObject > getMenuObjects, OnMenuItemClickListener itemClickListener, final int model )
	{
		mMenu.initMenu(mContext, model);

		if ( model == DefinedMenu.TITLE_MENU )
			mMenu.setTitle(title);      //建立标题



		if ( getMenuObjects != null )
		{
			MenuParams menuParams = new MenuParams();
			menuParams.setActionBarSize(( int ) getResources().getDimension(R.dimen.tool_bar_height));
			menuParams.setMenuObjects(getMenuObjects);
			menuParams.setClosableOutside(false);

			mMenuDialogFragment = ContextMenuDialogFragment.newInstance(menuParams);
			mMenu.setRightButtonListener(new View.OnClickListener()
			{
				@Override
				public void onClick ( View view )
				{
					mMenuDialogFragment.show(mFragmentManager, ContextMenuDialogFragment.TAG);   //this method can show menu
				}
			});
		}

		if ( itemClickListener != null )
			mMenuDialogFragment.setItemClickListener(itemClickListener);

//		mMenuDialogFragment.setItemLongClickListener();     //Set Listener.
		return this;
	}

	protected abstract void findView ( View view );

	protected abstract void initView ();

	protected abstract List< MenuObject > getMenuObjects ();

}
