package com.fengjie.myapplication.base.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.fengjie.myapplication.utils.often.LogUtils;
import com.fengjie.myapplication.view.DefinedMenu;

import me.yokeyword.fragmentation.SupportFragment;

/**
 * Created by MadJieJie on 17/2/2.
 */
public abstract class AbstractFragment extends SupportFragment
{
	/** Parameters **/
//	private FragmentManager mFragmentManager;
//	private ContextMenuDialogFragment mMenuDialogFragment;

	
	protected Context mContext;
	
//	protected View mView;
	
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
		LogUtils.d("Debug", "onCreate: ");
//		mFragmentManager = getActivity().getSupportFragmentManager();
	}
	
	protected AbstractFragment initMenu ( String title, final int model )
	{
		LogUtils.d("Debug", "initMenu: ");
		mMenu.init(mContext, model);
		
		if ( model == DefinedMenu.TITLE_MENU )
			mMenu.setTitle(title);      //建立标题


//		if ( getMenuObjects != null )
//		{
//			MenuParams menuParams = new MenuParams();
//			menuParams.setActionBarSize(( int ) getResources().getDimension(R.dimen.tool_bar_height));
//			menuParams.setMenuObjects(getMenuObjects);
//			menuParams.setClosableOutside(false);
//
//			mMenuDialogFragment = ContextMenuDialogFragment.newInstance(menuParams);
//			mMenu.setRightButtonListener(new View.OnClickListener()
//			{
//				@Override
//				public void onClick ( View view )
//				{
//					mMenuDialogFragment.show(mFragmentManager, ContextMenuDialogFragment.TAG);   //this method can show menu
//				}
//			});
//		}

//		if ( itemClickListener != null )
//			mMenuDialogFragment.setItemClickListener(itemClickListener);

//		mMenuDialogFragment.setItemLongClickListener();     //Set Listener.
		return this;
	}
	
	protected abstract void findView ( View view );
	
	protected abstract void initView ();

//	protected abstract List< MenuObject > getMenuObjects ();
	
}
