package com.fengjie.myapplication.modules.tool.ui;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.fengjie.myapplication.R;
import com.fengjie.myapplication.base.AbstractRxPermissionEvent;
import com.fengjie.myapplication.event.Event;
import com.fengjie.myapplication.modules.MainActivity;
import com.fengjie.myapplication.modules.tool.adapter.weather.WeatherAdapter;
import com.fengjie.myapplication.modules.tool.base.weather.AbstractRetrofitFragment;
import com.fengjie.myapplication.modules.tool.bean.Weather;
import com.fengjie.myapplication.modules.tool.utils.weather.RetrofitWeather;
import com.fengjie.myapplication.utils.often.LogUtils;
import com.fengjie.myapplication.utils.often.SharedPreferenceUtil;
import com.fengjie.myapplication.utils.often.ToastUtils;
import com.fengjie.myapplication.utils.often.Utils;
import com.fengjie.myapplication.utils.rxbus.RxBus;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * @author Created by MadJieJie on 2017/1/31-21:49.
 * @brief 天气
 * @attention
 */

public class WeatherFragment extends AbstractRetrofitFragment
{
	
	private RecyclerView mRecyclerView = null;
	private SwipeRefreshLayout mRefreshLayout = null;
	private ProgressBar mProgressBar = null;
	private ImageView mIvError = null;
	
	private static Weather mWeather = new Weather();
	private WeatherAdapter mAdapter = null;
	private Context mContext = null;

//	声明AMapLocationClient类对象
//	public AMapLocationClient mLocationClient = null;
//	public AMapLocationClientOption mLocationOption = null;
	
	
	public static WeatherFragment newInstance ()
	{
		Bundle args = new Bundle();
		
		WeatherFragment fragment = new WeatherFragment();
		fragment.setArguments(args);
		return fragment;
	}
	
	@Override
	public void onAttach ( Context context )
	{
		super.onAttach(context);
	}
	
	@Nullable
	@Override
	public View onCreateView ( LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState )
	{
		if ( mView == null )
		{
			mView = inflater.inflate(R.layout.fragment_weather, container, false);
			mContext = getContext();
		}
		
		mIsCreateView = true;
		
		LogUtils.d("WeatherFragment - onCreateView");
		return mView;
	}
	
	@Override
	public void onActivityCreated ( @Nullable Bundle savedInstanceState )
	{
		super.onActivityCreated(savedInstanceState);
		
	}
	
	@Override
	public void onViewCreated ( View view, @Nullable Bundle savedInstanceState )
	{
		super.onViewCreated(view, savedInstanceState);
		initIcon();             //图标
		findView();             //widget获得实例
		initView();             //初始化widget
		requestPermission();    //请求权限
	}
	
	@Override
	public void onCreate ( @Nullable Bundle savedInstanceState )
	{
		super.onCreate(savedInstanceState);
		initRxBus();
	}
	
	@Override
	public void onDestroyView ()
	{
		super.onDestroyView();
	}
	
	@Override
	public void onDestroy ()
	{
		super.onDestroy();
//		mLocationClient = null;
//		mLocationOption = null;
	}
	
	/**
	 * 加载数据操作,在视图创建之前初始化
	 */
	@Override
	protected void lazyLoad ()
	{
//		load();
	}
	
	
	private void findView ()
	{
		mRecyclerView = ( RecyclerView ) mView.findViewById(R.id.content_rv_weather);
		mRefreshLayout = ( SwipeRefreshLayout ) mView.findViewById(R.id.fresh_rl_weather);
		mProgressBar = ( ProgressBar ) mView.findViewById(R.id.progress_pb_common);
		mIvError = ( ImageView ) mView.findViewById(R.id.error_iv_common);
	}
	
	private void initView ()
	{
		if ( mRefreshLayout != null )
		{
			mRefreshLayout.setColorSchemeResources(
					android.R.color.holo_blue_bright,
					android.R.color.holo_green_light,
					android.R.color.holo_orange_light,
					android.R.color.holo_red_light);
			mRefreshLayout.setOnRefreshListener(
					() -> mRefreshLayout.postDelayed(() -> load(), 500));
		}
		
		mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		mAdapter = new WeatherAdapter(mWeather);
		mRecyclerView.setAdapter(mAdapter);
	}
	
	/**
	 * 当城市发出更新事件，立即更新
	 */
	private void initRxBus ()
	{
		RxBus.getInstance().tObservable(Event.class).observeOn(AndroidSchedulers.mainThread()).subscribe(
				event ->
				{
					if ( event.getEvent() == Event.EVENT_CHANGE_CITY )
					{
						mRefreshLayout.setRefreshing(true);
						load();
					}
				}
		);
	}
	
	/**
	 * 请求权限
	 */
	private void requestPermission ()
	{
		Utils.isGetRxPermission(getActivity(), new AbstractRxPermissionEvent()
		{
			@Override
			public void canGetPermissionEvent ()
			{
				load();     /**从API获取数据通过适配器转换成界面*/
			}
			
			@Override
			public void notGetPermissionEvent ()
			{
				ToastUtils.showShort("未获网络权限权限");
			}
		},Manifest.permission.INTERNET);
	}
	
	private void load ()
	{
		getWeatherDataByNet()
				.doOnError(throwable ->
				{
					mIvError.setVisibility(View.VISIBLE);
					mRecyclerView.setVisibility(View.GONE);
					SharedPreferenceUtil.getInstance().setCityName("桂林");
					safeSetTitle("找不到城市啦");
				})
				.doOnNext(weather ->
				{
					mIvError.setVisibility(View.GONE);
					mRecyclerView.setVisibility(View.VISIBLE);
				})
				.doOnTerminate(() ->
				{
					mRefreshLayout.setRefreshing(false);
					mProgressBar.setVisibility(View.GONE);
				}).subscribe(new Observer< Weather >()
		{
			@Override
			public void onSubscribe ( Disposable d )
			{
				mRefreshLayout.setRefreshing(true);
			}   //当请求的时候，显示加载更新图标
			
			@Override
			public void onNext ( Weather weather )
			{
				mWeather.status = weather.status;
				mWeather.aqi = weather.aqi;
				mWeather.basic = weather.basic;
				mWeather.suggestion = weather.suggestion;
				mWeather.now = weather.now;
				mWeather.dailyForecast = weather.dailyForecast;
				mWeather.hourlyForecast = weather.hourlyForecast;
				//mActivity.getToolbar().setTitle(weather.basic.city);
				safeSetTitle(weather.basic.city);
				mAdapter.notifyDataSetChanged();
				normalStyleNotification(weather);
				
				LogUtils.d(mWeather.now.tmp);
			}
			
			@Override
			public void onError ( Throwable e )
			{
				RetrofitWeather.disposeFailureInfo(e);
			}
			
			@Override
			public void onComplete ()
			{
				ToastUtils.showShort(mContext, getString(R.string.addComplete));
			}
		});
	}
	
	/**
	 * 从网络获取
	 */
	private Observable< Weather > getWeatherDataByNet ()
	{
		String cityName = SharedPreferenceUtil.getInstance().getCityName();     //从SharedPreference获得数据
		return RetrofitWeather.getInstance()
				       .fetchWeather(cityName)
				       .compose(this.bindToLifecycle());   /**更新天气处*/
	}
	
	
	/**
	 * 初始化通知栏
	 *
	 * @param weather
	 */
	private void normalStyleNotification ( Weather weather )
	{
		Intent intent = new Intent(getActivity(), MainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent pendingIntent =
				PendingIntent.getActivity(getActivity(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		Notification.Builder builder = new Notification.Builder(getActivity());
		Notification notification = builder.setContentIntent(pendingIntent)
				                            .setContentTitle(weather.basic.city)
				                            .setContentText(String.format("%s 当前温度: %s℃ ", weather.now.cond.txt, weather.now.tmp))
				                            // 这里部分 ROM 无法成功
				                            .setSmallIcon(SharedPreferenceUtil.getInstance().getInt(weather.now.cond.txt, R.mipmap.none))
				                            .build();
		notification.flags = SharedPreferenceUtil.getInstance().getNotificationModel();
		NotificationManager manager = ( NotificationManager ) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
		// tag和id都是可以拿来区分不同的通知的
		manager.notify(1, notification);
	}
	
	/**
	 * 初始化Icon,加载图片到SharedPreference.
	 */
	private void initIcon ()
	{
//		if ( SharedPreferenceUtil.getInstance().getIconType() == 0 )
//		{
		SharedPreferenceUtil.getInstance().putInt("未知", R.mipmap.none);
		SharedPreferenceUtil.getInstance().putInt("晴", R.mipmap.type_one_sunny);
		SharedPreferenceUtil.getInstance().putInt("阴", R.mipmap.type_one_cloudy);
		SharedPreferenceUtil.getInstance().putInt("多云", R.mipmap.type_one_cloudy);
		SharedPreferenceUtil.getInstance().putInt("少云", R.mipmap.type_one_cloudy);
		SharedPreferenceUtil.getInstance().putInt("晴间多云", R.mipmap.type_one_cloudytosunny);
		SharedPreferenceUtil.getInstance().putInt("小雨", R.mipmap.type_one_light_rain);
		SharedPreferenceUtil.getInstance().putInt("中雨", R.mipmap.type_one_light_rain);
		SharedPreferenceUtil.getInstance().putInt("大雨", R.mipmap.type_one_heavy_rain);
		SharedPreferenceUtil.getInstance().putInt("阵雨", R.mipmap.type_one_thunderstorm);
		SharedPreferenceUtil.getInstance().putInt("雷阵雨", R.mipmap.type_one_thunder_rain);
		SharedPreferenceUtil.getInstance().putInt("霾", R.mipmap.type_one_fog);
		SharedPreferenceUtil.getInstance().putInt("雾", R.mipmap.type_one_fog);
//		}
		
	}

//	@Override
//	public void onMenuItemClick ( View clickedView, int position )
//	{
//
//	}
	
	
	//	/**
//	 * 高德定位
//	 */
//	private void location ()
//	{
//		mRefreshLayout.setRefreshing(true);
//		//初始化定位
//		mLocationClient = new AMapLocationClient(BaseApplication.getAppContext());
//		//设置定位回调监听
//		mLocationClient.setLocationListener(this);
//		mLocationOption = new AMapLocationClientOption();
//		//设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
//		mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
//		//设置是否返回地址信息（默认返回地址信息）
//		mLocationOption.setNeedAddress(true);
//		//设置是否只定位一次,默认为false
//		mLocationOption.setOnceLocation(false);
//		//设置是否强制刷新WIFI，默认为强制刷新
//		mLocationOption.setWifiActiveScan(true);
//		//设置是否允许模拟位置,默认为false，不允许模拟位置
//		mLocationOption.setMockEnable(false);
//		//设置定位间隔 单位毫秒
//		int tempTime = SharedPreferenceUtil.getInstance().getAutoUpdate();
//		if ( tempTime == 0 )
//		{
//			tempTime = 100;
//		}
//		mLocationOption.setInterval(tempTime * SharedPreferenceUtil.ONE_HOUR);
//		//给定位客户端对象设置定位参数
//		mLocationClient.setLocationOption(mLocationOption);
//		//启动定位
//		mLocationClient.startLocation();
//	}

//	@Override
//	public void onLocationChanged ( AMapLocation aMapLocation )
//	{
//		if ( aMapLocation != null )
//		{
//			if ( aMapLocation.getErrorCode() == 0 )
//			{
//				//定位成功回调信息，设置相关消息
//				aMapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
//				SharedPreferenceUtil.getInstance().setCityName(Util.replaceCity(aMapLocation.getCity()));
//			} else
//			{
//				if ( isAdded() )
//				{
//					ToastUtil.showShort(getString(R.string.errorLocation));
//				}
//			}
//			load();
//		}
//	}
	
}
