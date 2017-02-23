package com.fengjie.myapplication.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.fengjie.myapplication.R;
import com.fengjie.percentlibrary.PercentRelativeLayout;

public class DefinedMenu extends PercentRelativeLayout
{

	public final static boolean HIDE = false;
	public final static boolean SHOW = true;

	public final static int TITLE_MENU = 0;
	public final static int TAB_MENU = 1;

	private View mView;

	public DefinedMenu ( Context context, AttributeSet attrs )
	{
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.view_title_menu, this);
	}

	public DefinedMenu initMenu ( Context context, final int model )
	{
//		switch ( model )
//		{
//			case TITLE_MENU:
//				mView = LayoutInflater.from(context).inflate(R.layout.view_title_menu, this);       //传入需要加载的布局文件,传入父布局
//				break;
//			case TAB_MENU:
//				mView = LayoutInflater.from(context).inflate(R.layout.view_tab_menu, this);       //传入需要加载的布局文件,传入父布局
//				break;
//			default:
//				mView = LayoutInflater.from(context).inflate(R.layout.view_title_menu, this);       //传入需要加载的布局文件,传入父布局
//				break;
//		}
		return this;
	}


	public DefinedMenu setupWithViewPager ( ViewPager viewPager )
	{
//		( ( TabLayout ) findViewById(R.id.tab_tabLayout_menu) ).setupWithViewPager(viewPager, false);
		return this;
	}

	/**
	 * 赋入OnclickListener 实例化接口,对点击事件进行实现
	 *
	 * @param onClickListener
	 */
	public DefinedMenu setLeftButtonListener ( View.OnClickListener onClickListener )
	{
		findViewById(R.id.left_btn_menu).setOnClickListener(onClickListener);
		return this;
	}

	/**
	 * 赋入OnclickListener 实例化接口,对点击事件进行实现
	 *
	 * @param onClickListener
	 */
	public DefinedMenu setRightButtonListener ( View.OnClickListener onClickListener )
	{
		findViewById(R.id.right_btn_menu).setOnClickListener(onClickListener);
		return this;
	}

	/**
	 * 构建item点击事件
	 *
	 * @param onItemClickListener
	 */
	public void setOnItemClickListener ( AdapterView.OnItemClickListener onItemClickListener )
	{
	}

	/**
	 * 变更标题方法
	 *
	 * @param title
	 */
	public DefinedMenu setTitle ( String title )
	{
		( ( TextView ) findViewById(R.id.title_tv_menu) ).setText(title);
		return this;
	}

	/**
	 * 更改字体大小
	 *
	 * @param textSize
	 */
	public void setTitleSize_textView_menu ( float textSize )
	{
//		title_textView_menu.setTextSize(textSize);
	}

	/**
	 * 更改字体颜色方法
	 *
	 * @param textColor
	 */
	public void setTitleColor_textView_menu ( int textColor )
	{
//		title_textView_menu.setTextColor(textColor);
	}

	/**
	 * 更换右边按钮的图片
	 *
	 * @param ID
	 */
	public void setRightButtonBackground ( int ID )
	{
//		operate_imageButton_menu.setBackgroundResource(ID);
	}

	public void setLeftButtonBackground ( int ID )
	{
//		back_button_menu.setImageDrawable(getResources().getDrawable(ID));
	}


	/**
	 * 隐藏Menu-操作Button方法
	 *
	 * @param flag 为真则显示,反之隐藏
	 */
	public void setLeftButtonVisibility ( boolean flag )
	{
//		back_button_menu.setVisibility(flag ? View.VISIBLE : View.GONE);
	}

	public void setRightButtonVisibility ( boolean flag )
	{
//		operate_imageButton_menu.setVisibility(flag ? View.VISIBLE : View.GONE);
	}

	/**
	 */
	public void setRightButtonOnClickListener ()
	{
//		if ( newOnClickListener != null )
//			operate_imageButton_menu.setOnClickListener(newOnClickListener);    //建立新的监听事件
//		else
//			operate_imageButton_menu.setOnClickListener(new oldOnClickListener());//建立原本的监听事件
	}

//
//	/**
//	 * open/close menu method
//	 */
//	private class oldOnClickListener implements View.OnClickListener
//	{
//
//		@Override
//		public void onClick ( View v )
//		{
////			close();
//		}
//	}

	/**
	 * open/close menu method
	 */
//	public void close ()
//	{
////		if ( mPopupWindow.isShowing() )
////		{
////			mPopupWindow.dismiss();// 关闭
////		} else
////		{
////			mPopupWindow.showAsDropDown(operate_imageButton_menu);// 显示
////		}
//	}

}
