package com.fengjie.myapplication.utils;

import android.util.Log;

/**
 * Log统一管理类
 *
 * @author way
 */
public class LogUtils
{


	private LogUtils ()
	{
		/* cannot be instantiated */
		throw new UnsupportedOperationException("cannot be instantiated");
	}

	private static final int VERBOSE = 1;
	private static final int DEBUG = 2;
	private static final int INFO = 3;
	private static final int WARN = 4;
	private static final int ERROR = 5;
	private static final int NOTHING = 6;
	private static final int LEVEL = DEBUG;

	private static final String TAG = "Debug";

	// 下面四个是默认tag的函数


	public static void v ( String msg )
	{
		if ( LEVEL <= VERBOSE )
			Log.v(TAG, msg);
	}

	public static void d ( String msg )
	{
		if ( LEVEL <= DEBUG )
			Log.d(TAG, msg);
	}

	public static void i ( String msg )
	{
		if ( LEVEL <= INFO )
			Log.i(TAG, msg);
	}

	public static void w ( String msg )
	{
		if ( LEVEL <= WARN )
			Log.w(TAG, msg);
	}

	public static void e ( String msg )
	{
		if ( LEVEL <= ERROR )
			Log.e(TAG, msg);
	}

	// 下面是传入自定义tag的函数
	public static void v ( String tag, String msg )
	{
		if ( LEVEL <= VERBOSE )
			Log.v(tag, msg);
	}

	public static void d ( String tag, String msg )
	{
		if ( LEVEL <= DEBUG )
			Log.d(tag, msg);
	}

	public static void i ( String tag, String msg )
	{
		if ( LEVEL <= INFO )
			Log.i(tag, msg);
	}

	public static void w ( String tag, String msg )
	{
		if ( LEVEL <= WARN )
			Log.w(tag, msg);
	}

	public static void e ( String tag, String msg )
	{
		if ( LEVEL <= ERROR )
			Log.e(tag, msg);
	}

}