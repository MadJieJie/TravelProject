package com.fengjie.myapplication.utils.weather;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.ViewConfiguration;

import com.fengjie.myapplication.utils.ToastUtils;

import java.io.Closeable;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * @author Created by MadJieJie on 2017/1/31-21:49.
 * @brief
 * @attention ����ת����GBK,��ΪURL�˽��յ�������,��Ȼ���ڻ�����
 */

public class Util
{

    /**
     * ��ȡ�汾��
     *
     * @return ��ǰӦ�õİ汾��
     */

    public static String getVersion( Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            return info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return "�Ҳ����汾��";
//            return context.getString(R.string.can_not_find_version_name);
        }
    }

    /**
     * @return �汾��
     */
    public static int getVersionCode(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * ֻ��ע�Ƿ�����
     */
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager =
                (ConnectivityManager ) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * �жϵ�ǰ���������ڼ�������ת����GBK,��ΪURL�˽��յ�������,��Ȼ���ڻ�����
     *
     * @param pTime ��Ҫ�жϵ�ʱ��
     * @return dayForWeek �жϽ��
     * @Exception �����쳣
     */
    public static String dayForWeek( String pTime) throws Exception
    {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.setTime(format.parse(pTime));
        int dayForWeek;
        String week = "";
        dayForWeek = c.get(Calendar.DAY_OF_WEEK);
        switch (dayForWeek) {
            case 1:
                week = "������";
                break;
            case 2:
                week = "����һ";
                break;
            case 3:
                week = "���ڶ�";
                break;
            case 4:
                week = "������";
                break;
            case 5:
                week = "������";
                break;
            case 6:
                week = "������";
                break;
            case 7:
                week = "������";
                break;
        }
        return week;
    }

    /**
     * ��ȫ�� String ����
     *
     * @param prefix Ĭ���ֶ�
     * @param obj ƴ���ֶ� (����)
     */
    public static String safeText( String prefix, String obj) {
        if ( TextUtils.isEmpty(obj)) return "";
        return TextUtils.concat(prefix, obj).toString();
    }

    public static String safeText( String msg) {
        if (null == msg) {
            return "";
        }
        return safeText("", msg);
    }

    /**
     * �������� 100 Ϊ�� 101-213 500-901 Ϊ�� 300-406Ϊ��
     *
     * @param code ��������
     * @return �������
     */
    public static String getWeatherType( int code) {
        if (code == 100) {
            return "��";
        }
        if ((code >= 101 && code <= 213) || (code >= 500 && code <= 901)) {
            return "��";
        }
        if (code >= 300 && code <= 406) {
            return "��";
        }
        return "����";
    }

    /**
     * ƥ���������Ϣ
     */
    public static String replaceCity( String city) {
        city = safeText(city).replaceAll("(?:ʡ|��|������|�ر�������|����|��)", "");
        return city;
    }

    /**
     * ƥ����޹���Ϣ
     */

    public static String replaceInfo( String city) {
        city = safeText(city).replace("APIû��", "");
        return city;
    }

    /**
     * Java ����һ�� Closeable �ӿ�,��ʶ��һ���ɹرյĶ���,��ֻ��һ�� close ����.
     */
    public static void closeQuietly(Closeable closeable) {
        if (null != closeable) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * ��ȡ����status bar �߶�
     */
    public static int getStatusBarHeight(Activity mActivity) {
        Resources resources = mActivity.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        return height;
    }

    /**
     * ��ȡ�ײ� navigation bar �߶�
     */
    public static int getNavigationBarHeight(Activity mActivity) {
        Resources resources = mActivity.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        return height;
    }

    /**
     *
     * @param context
     * @param dipValue
     * @return
     */
    public static int dip2px( Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    @SuppressLint ("NewApi")
    public static boolean checkDeviceHasNavigationBar(Context activity) {
        //ͨ���ж��豸�Ƿ��з��ؼ����˵���(���������,���ֻ���Ļ��İ���)��ȷ���Ƿ���navigation bar
        boolean hasMenuKey = ViewConfiguration.get(activity)
            .hasPermanentMenuKey();
        boolean hasBackKey = KeyCharacterMap
            .deviceHasKey(KeyEvent.KEYCODE_BACK);
        return !hasMenuKey && !hasBackKey;
    }

    public static void copyToClipboard( String info, Context context) {
        ClipboardManager manager = (ClipboardManager ) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("msg", info);
        manager.setPrimaryClip(clipData);
        ToastUtils.showShort(String.format("[%s] �Ѿ����Ƶ����а���( ?? .? ?? )?", info));
    }
}