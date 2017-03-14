package com.fengjie.myapplication.modules.tool.utils.weather;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by MadJieJie on 16/4/30.
 * <p>
 * 图片加载类,统一适配(方便换库,方便管理)
 */
public class ImageLoader
{
	/**
	 * 本地资源图片加载
	 * @param context
	 * @param imageRes
	 * @param view
	 */
	public static void load ( Context context, @DrawableRes int imageRes, ImageView view )
	{
		Glide.with(context).load(imageRes).crossFade().into(view);  //平滑淡出加载
	}

	/**
	 * URL资源加载
	 * @param context
	 * @param url
	 * @param view
	 */
	public static void loadURLImage ( Context context, final String url, ImageView view )
	{
		Glide.with(context).load(url).crossFade().into(view);  //平滑淡出加载
	}

	public static void clearMemory ( Context context )
	{
		Glide.get(context).clearMemory();
	}
}
