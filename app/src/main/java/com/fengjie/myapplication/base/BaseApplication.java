package com.fengjie.myapplication.base;

import android.app.Application;
import android.content.Context;

import com.fengjie.myapplication.modules.tool.utils.weather.CrashHandler;


public class BaseApplication extends Application
{
	
	private static String sCacheDir;
	public static Context sAppContext;
	/**缓存用户信息*/
	public static int sUserID = -1;
	public static String sUserName = "anonymity";


	
	@Override
	public void onCreate ()
	{
		super.onCreate();
		sAppContext = getApplicationContext();          //主要用于需要Context的情景,例如,Toast如何直接赋入Class.this,当摧毁Activity时，Toast得不到执行会报错.
		CrashHandler.init(new CrashHandler(getApplicationContext()));
//        if (! BuildConfig.DEBUG) {.
//            FIR.init(this);
//        }
//        BlockCanary.install(this, new AppBlockCanaryContext()).start();
//        LeakCanary.install(this);
		/**
		 * 如果存在SD卡则将缓存写入SD卡,否则写入手机内存
		 */
		if ( getApplicationContext().getExternalCacheDir() != null && ExistSDCard() )
		{
			sCacheDir = getApplicationContext().getExternalCacheDir().toString();
		} else
		{
			sCacheDir = getApplicationContext().getCacheDir().toString();
		}
		
	}
	
	private boolean ExistSDCard ()
	{
		return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
	}
	
	public static Context getAppContext ()
	{
		return sAppContext;
	}
	
	public static String getAppCacheDir ()
	{
		return sCacheDir;
	}
}
