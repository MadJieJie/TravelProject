package com.fengjie.myapplication.utils.often;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.fengjie.myapplication.R;
import com.thefinestartist.finestwebview.FinestWebView;

import java.io.Closeable;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Created by MadJieJie on 2017/1/31-21:49.
 * @brief
 * @attention 必须转换成GBK, 因为URL端接收的是中文, 不然日期会乱码
 */

public class Utils
{
	
	/**
	 * 获取版本号
	 *
	 * @return 当前应用的版本号
	 */
	
	public static String getVersion ( Context context )
	{
		try
		{
			PackageManager manager = context.getPackageManager();
			PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
			return info.versionName;
		} catch( Exception e )
		{
			e.printStackTrace();
			return "找不到版本号";
//            return context.getString(R.string.can_not_find_version_name);
		}
	}
	
	/**
	 * @return 版本号
	 */
	public static int getVersionCode ( Context context )
	{
		try
		{
			PackageManager manager = context.getPackageManager();
			PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
			return info.versionCode;
		} catch( Exception e )
		{
			e.printStackTrace();
			return 0;
		}
	}
	
	/**
	 * 获取应用程序名称
	 */
	public static String getAppName(Context context)
	{
		try
		{
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(
					context.getPackageName(), 0);
			int labelRes = packageInfo.applicationInfo.labelRes;
			return context.getResources().getString(labelRes);
		} catch (NameNotFoundException e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * [获取应用程序版本名称信息]
	 *
	 * @param context
	 * @return 当前应用的版本名称
	 */
	public static String getVersionName(Context context)
	{
		try
		{
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(
					context.getPackageName(), 0);
			return packageInfo.versionName;
			
		} catch (NameNotFoundException e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 判断网络是否连接
	 */
	public static boolean isNetworkConnected ( Context context )
	{
		if ( context != null )
		{
			ConnectivityManager mConnectivityManager =
					( ConnectivityManager ) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
			if ( mNetworkInfo != null )
			{
				return mNetworkInfo.isAvailable();
			}
		}
		return false;
	}
	
	/**
	 * 判断当前日期是星期几，必须转换成GBK,因为URL端接收的是中文,不然日期会乱码
	 *
	 * @param pTime 修要判断的时间
	 * @return dayForWeek 判断结果
	 * @Exception 发生异常
	 */
	public static String dayForWeek ( String pTime ) throws Exception
	{
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		c.setTime(format.parse(pTime));
		int dayForWeek;
		String week = "";
		dayForWeek = c.get(Calendar.DAY_OF_WEEK);
		switch ( dayForWeek )
		{
			case 1:
				week = "星期日";
				break;
			case 2:
				week = "星期一";
				break;
			case 3:
				week = "星期二";
				break;
			case 4:
				week = "星期三";
				break;
			case 5:
				week = "星期四";
				break;
			case 6:
				week = "星期五";
				break;
			case 7:
				week = "星期六";
				break;
		}
		return week;
	}
	
	/**
	 * 安全的 String 返回
	 *
	 * @param prefix 默认字段
	 * @param obj    拼接字段 (需检查)
	 */
	public static String safeText ( String prefix, String obj )
	{
		if ( TextUtils.isEmpty(obj) ) return "";
		return TextUtils.concat(prefix, obj).toString();
	}
	
	public static String safeText ( String msg )
	{
		if ( null == msg )
		{
			return "";
		}
		return safeText("", msg);
	}
	
	/**
	 * 天气代码 100 为晴 101-213 500-901 为阴 300-406为雨
	 *
	 * @param code 天气代码
	 * @return 天气情况
	 */
	public static String getWeatherType ( int code )
	{
		if ( code == 100 )
		{
			return "晴";
		}
		if ( ( code >= 101 && code <= 213 ) || ( code >= 500 && code <= 901 ) )
		{
			return "阴";
		}
		if ( code >= 300 && code <= 406 )
		{
			return "雨";
		}
		return "错误";
	}
	
	/**
	 * 匹配掉错误信息
	 */
	public static String replaceCity ( String city )
	{
		city = safeText(city).replaceAll("(?:省|市|自治区|特别行政区|地区|盟)", "");
		return city;
	}
	
	/**
	 * 匹配掉无关信息
	 */
	
	public static String replaceInfo ( String city )
	{
		city = safeText(city).replace("API没有", "");
		return city;
	}
	
	/**
	 * Java 中有一个 Closeable 接口,标识了一个可关闭的对象,它只有一个 close 方法.
	 */
	public static void closeQuietly ( Closeable closeable )
	{
		if ( null != closeable )
		{
			try
			{
				closeable.close();
			} catch( IOException e )
			{
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 获取顶部status bar 高度
	 */
	public static int getStatusBarHeight ( Activity mActivity )
	{
		Resources resources = mActivity.getResources();
		int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
		int height = resources.getDimensionPixelSize(resourceId);
		return height;
	}
	
	private static final int INVALID_VAL = -1;
	private static final int COLOR_DEFAULT = Color.parseColor("#20000000");
	
	@TargetApi (Build.VERSION_CODES.LOLLIPOP)
	public static void compat(Activity activity, int statusColor)
	{
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
		{
			if (statusColor != INVALID_VAL)
			{
				activity.getWindow().setStatusBarColor(statusColor);
			}
			return;
		}
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
		{
			int color = COLOR_DEFAULT;
			ViewGroup contentView = (ViewGroup) activity.findViewById(android.R.id.content);
			if (statusColor != INVALID_VAL)
			{
				color = statusColor;
			}
			View statusBarView = new View(activity);
			ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
					                                                      getStatusBarHeight(activity));
			statusBarView.setBackgroundColor(color);
			contentView.addView(statusBarView, lp);
		}
		
	}
	
	public static void compat(Activity activity)
	{
		compat(activity, INVALID_VAL);
	}
	
	
	public static int getStatusBarHeight(Context context)
	{
		int result = 0;
		int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
		if (resourceId > 0)
		{
			result = context.getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}
	
	public static void setWindowStatusBarColor(Activity activity, int colorResId) {
		try {
			if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
				Window window = activity.getWindow();
				window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
				window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
				window.setStatusBarColor(activity.getResources().getColor(colorResId));
				
				//底部导航栏
				//window.setNavigationBarColor(activity.getResources().getColor(colorResId));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void setWindowStatusBarColor( Dialog dialog, int colorResId) {
		try {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
				Window window = dialog.getWindow();
				window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
				window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
				window.setStatusBarColor(dialog.getContext().getResources().getColor(colorResId));
				
				//底部导航栏
				//window.setNavigationBarColor(activity.getResources().getColor(colorResId));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 获取底部 navigation bar 高度
	 */
	public static int getNavigationBarHeight ( Activity mActivity )
	{
		Resources resources = mActivity.getResources();
		int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
		int height = resources.getDimensionPixelSize(resourceId);
		return height;
	}
	
	/**
	 * @param context
	 * @param dipValue
	 * @return
	 */
	public static int dip2px ( Context context, float dipValue )
	{
		final float scale = context.getResources().getDisplayMetrics().density;
		return ( int ) ( dipValue * scale + 0.5f );
	}
	
	@SuppressLint ( "NewApi" )
	public static boolean checkDeviceHasNavigationBar ( Context activity )
	{
		//通过判断设备是否有返回键、菜单键(不是虚拟键,是手机屏幕外的按键)来确定是否有navigation bar
		boolean hasMenuKey = ViewConfiguration.get(activity)
				                     .hasPermanentMenuKey();
		boolean hasBackKey = KeyCharacterMap
				                     .deviceHasKey(KeyEvent.KEYCODE_BACK);
		return ! hasMenuKey && ! hasBackKey;
	}
	
	public static void copyToClipboard ( String info, Context context )
	{
		ClipboardManager manager = ( ClipboardManager ) context.getSystemService(Context.CLIPBOARD_SERVICE);
		ClipData clipData = ClipData.newPlainText("msg", info);
		manager.setPrimaryClip(clipData);
		ToastUtils.showShort(String.format("[%s] 已经复制到剪切板啦( ?? .? ?? )?", info));
	}
	
	/**
	 *
	 * @param mobiles   mobile number
	 * @return If it is mobile number will return true,and otherwise.
	 */
	public static boolean isMobileNumber ( String mobiles )
	{
		Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
		
		Matcher m = p.matcher(mobiles);
		
		return m.matches();
	}
	
	/**
	 * display web.
	 * @param context
	 * @param title
	 * @param url
	 */
	public static void showWeb ( Context context, String title, String url )
	{
		new FinestWebView.Builder(context).theme(R.style.FinestWebViewTheme)
				.titleDefault(title)
				.showUrl(false)     //不显示网址
				.statusBarColorRes(R.color.deepskyblue)
				.toolbarColorRes(R.color.dodgerblue)
				.titleColorRes(R.color.finestWhite)
				.urlColorRes(R.color.steelblue)
				.iconDefaultColorRes(R.color.finestWhite)
				.progressBarColorRes(R.color.finestWhite)
				.stringResCopiedToClipboard(R.string.copied_to_clipboard)
				.showSwipeRefreshLayout(true)
				.swipeRefreshColorRes(R.color.dodgerblue)
				.menuSelector(R.drawable.selector_light_theme)
				.menuTextGravity(Gravity.CENTER)
				.menuTextPaddingRightRes(R.dimen.defaultMenuTextPaddingLeft)
				.dividerHeight(0)
				.gradientDivider(false)
				.setCustomAnimations(R.anim.slide_up, R.anim.hold, R.anim.hold, R.anim.slide_down)
				.show(url);
	}
	
}