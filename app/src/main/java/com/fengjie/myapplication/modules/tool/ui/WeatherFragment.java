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
import com.fengjie.myapplication.modules.MainActivity;
import com.fengjie.myapplication.modules.tool.adapter.weather.WeatherAdapter;
import com.fengjie.myapplication.modules.tool.base.weather.AbstractRetrofitFragment;
import com.fengjie.myapplication.modules.tool.bean.Weather;
import com.fengjie.myapplication.modules.tool.db.weather.SharedPreferenceUtil;
import com.fengjie.myapplication.utils.LogUtils;
import com.fengjie.myapplication.utils.ToastUtils;
import com.fengjie.myapplication.utils.weather.RetrofitWeather;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * @author Created by MadJieJie on 2017/1/31-21:49.
 * @brief
 * @attention
 */

public class WeatherFragment extends AbstractRetrofitFragment
{

	private RecyclerView mRecyclerView;
	private SwipeRefreshLayout mRefreshLayout;
	private ProgressBar mProgressBar;
	private ImageView mIvError;

	private static Weather mWeather = new Weather();
	private WeatherAdapter mAdapter;

	private static boolean mIsLoadData = false;
//	//����AMapLocationClient�����
//	public AMapLocationClient mLocationClient = null;
//	public AMapLocationClientOption mLocationOption = null;

	private View view;


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
		if ( view == null )
		{
			view = inflater.inflate(R.layout.fragment_weather, container, false);
//			ButterKnife.bind(this, view);
		}
		mIsCreateView = true;

		mRecyclerView = ( RecyclerView ) view.findViewById(R.id.content_rv_weather);
		mRefreshLayout = ( SwipeRefreshLayout ) view.findViewById(R.id.fresh_rl_weather);
		mProgressBar = ( ProgressBar ) view.findViewById(R.id.progress_pb_common);
		mIvError = ( ImageView ) view.findViewById(R.id.error_iv_common);

		LogUtils.d("WeatherFragment - onCreateView");
		return view;
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
		if ( mIsLoadData == false )
		{
			initIcon();
			initView();
//		// https://github.com/tbruyelle/RxPermissions
			RxPermissions rxPermissions = new RxPermissions(getActivity());
			rxPermissions.request(Manifest.permission.ACCESS_COARSE_LOCATION)       //��ȡ��λȨ��
					.subscribe(granted -> {
						if ( granted )      //���Ȩ��(Permission)
						{
							load();
//						location();     //��λ
						} else              //don't get
						{
							load();
						}
					});
			mIsLoadData = true;
//		CheckVersion.checkVersion(getActivity());
		}

	}

	@Override
	public void onCreate ( @Nullable Bundle savedInstanceState )
	{
		super.onCreate(savedInstanceState);

//		RxBus.get().wait(ChangeCityEvent.class).observeOn(AndroidSchedulers.mainThread()).subscribe(
//				new SimpleSubscriber< ChangeCityEvent >()
//				{
//					@Override
//					public void onNext ( ChangeCityEvent changeCityEvent )
//					{
//						if ( mRefreshLayout != null )
//						{
//							mRefreshLayout.setRefreshing(true);
//						}
//						load();
//						PLog.d("MainRxBus");
//					}
//				});

	}



	protected void initView ()
	{
		if ( mRefreshLayout != null )
		{
			mRefreshLayout.setColorSchemeResources(
					android.R.color.holo_blue_bright,
					android.R.color.holo_green_light,
					android.R.color.holo_orange_light,
					android.R.color.holo_red_light);
			mRefreshLayout.setOnRefreshListener(
					() -> mRefreshLayout.postDelayed(() -> load(), 1000));
		}

		mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		mAdapter = new WeatherAdapter(mWeather);
		mRecyclerView.setAdapter(mAdapter);
	}

	private void requestPermission ()
	{
		RxPermissions rxPermissions = new RxPermissions(getActivity());
		rxPermissions.request(Manifest.permission.ACCESS_COARSE_LOCATION)       //��ȡ��λȨ��
				.subscribe(granted -> {
					if ( granted )      //���Ȩ��(Permission)
					{
						load();
						LogUtils.d("onActivityCreated - ���Ȩ��");
//						location();     //��λ
					} else              //don't get
					{
						load();
						LogUtils.d("onActivityCreated - δ��Ȩ��");
					}
				});
	}

	private void load ()
	{
		fetchDataByNetWork()
//				.doOnRequest(new Action1< Long >()
//				{
//					@Override
//					public void call ( Long aLong )
//					{
//						mRefreshLayout.setRefreshing(true);             //�������ʱ����ʾ����ͼ��
//					}
//				})
				.doOnError(throwable -> {
					mIvError.setVisibility(View.VISIBLE);
					mRecyclerView.setVisibility(View.GONE);
					SharedPreferenceUtil.getInstance().setCityName("jiashanxian");
					safeSetTitle("�Ҳ���������");
				})
				.doOnNext(weather -> {
					mIvError.setVisibility(View.GONE);
					mRecyclerView.setVisibility(View.VISIBLE);
				})
				.doOnTerminate(() -> {
					mRefreshLayout.setRefreshing(false);
					mProgressBar.setVisibility(View.GONE);
				}).subscribe(new Observer< Weather >()
		{
			@Override
			public void onSubscribe ( Disposable d )
			{
				mRefreshLayout.setRefreshing(true);
			}   //�������ʱ����ʾ����ͼ��

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
				ToastUtils.showShort(getString(R.string.addComplete));
			}
		});
	}

	/**
	 * �������ȡ
	 */
	private Observable< Weather > fetchDataByNetWork ()
	{
		String cityName = SharedPreferenceUtil.getInstance().getCityName();
//		String cityName = "jiashanxian";            /**��ѯ�����ص�����(������)*/
		return RetrofitWeather.getInstance()
				       .fetchWeather(cityName)
				       .compose(this.bindToLifecycle());   /**����������*/
	}

//	/**
//	 * �ߵ¶�λ
//	 */
//	private void location ()
//	{
//		mRefreshLayout.setRefreshing(true);
//		//��ʼ����λ
//		mLocationClient = new AMapLocationClient(BaseApplication.getAppContext());
//		//���ö�λ�ص�����
//		mLocationClient.setLocationListener(this);
//		mLocationOption = new AMapLocationClientOption();
//		//���ö�λģʽΪ�߾���ģʽ��Battery_SavingΪ�͹���ģʽ��Device_Sensors�ǽ��豸ģʽ
//		mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
//		//�����Ƿ񷵻ص�ַ��Ϣ��Ĭ�Ϸ��ص�ַ��Ϣ��
//		mLocationOption.setNeedAddress(true);
//		//�����Ƿ�ֻ��λһ��,Ĭ��Ϊfalse
//		mLocationOption.setOnceLocation(false);
//		//�����Ƿ�ǿ��ˢ��WIFI��Ĭ��Ϊǿ��ˢ��
//		mLocationOption.setWifiActiveScan(true);
//		//�����Ƿ�����ģ��λ��,Ĭ��Ϊfalse��������ģ��λ��
//		mLocationOption.setMockEnable(false);
//		//���ö�λ��� ��λ����
//		int tempTime = SharedPreferenceUtil.getInstance().getAutoUpdate();
//		if ( tempTime == 0 )
//		{
//			tempTime = 100;
//		}
//		mLocationOption.setInterval(tempTime * SharedPreferenceUtil.ONE_HOUR);
//		//����λ�ͻ��˶������ö�λ����
//		mLocationClient.setLocationOption(mLocationOption);
//		//������λ
//		mLocationClient.startLocation();
//	}

//	@Override
//	public void onLocationChanged ( AMapLocation aMapLocation )
//	{
//		if ( aMapLocation != null )
//		{
//			if ( aMapLocation.getErrorCode() == 0 )
//			{
//				//��λ�ɹ��ص���Ϣ�����������Ϣ
//				aMapLocation.getLocationType();//��ȡ��ǰ��λ�����Դ�������綨λ����������λ���ͱ�
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
	 * �������ݲ���,����ͼ����֮ǰ��ʼ��
	 */
	@Override
	protected void lazyLoad ()
	{

	}

	/**
	 * ��ʼ��֪ͨ��
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
				                            .setContentText(String.format("%s ��ǰ�¶�: %s�� ", weather.now.cond.txt, weather.now.tmp))
				                            // ���ﲿ�� ROM �޷��ɹ�
				                            .setSmallIcon(SharedPreferenceUtil.getInstance().getInt(weather.now.cond.txt, R.mipmap.none))
				                            .build();
		notification.flags = SharedPreferenceUtil.getInstance().getNotificationModel();
		NotificationManager manager = ( NotificationManager ) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
		// tag��id���ǿ����������ֲ�ͬ��֪ͨ��
		manager.notify(1, notification);
	}

	/**
	 * ��ʼ��Icon,����ͼƬ��SharedPreference.
	 */
	private void initIcon ()
	{
//		if ( SharedPreferenceUtil.getInstance().getIconType() == 0 )
//		{
		SharedPreferenceUtil.getInstance().putInt("δ֪", R.mipmap.none);
		SharedPreferenceUtil.getInstance().putInt("��", R.mipmap.type_one_sunny);
		SharedPreferenceUtil.getInstance().putInt("��", R.mipmap.type_one_cloudy);
		SharedPreferenceUtil.getInstance().putInt("����", R.mipmap.type_one_cloudy);
		SharedPreferenceUtil.getInstance().putInt("����", R.mipmap.type_one_cloudy);
		SharedPreferenceUtil.getInstance().putInt("������", R.mipmap.type_one_cloudytosunny);
		SharedPreferenceUtil.getInstance().putInt("С��", R.mipmap.type_one_light_rain);
		SharedPreferenceUtil.getInstance().putInt("����", R.mipmap.type_one_light_rain);
		SharedPreferenceUtil.getInstance().putInt("����", R.mipmap.type_one_heavy_rain);
		SharedPreferenceUtil.getInstance().putInt("����", R.mipmap.type_one_thunderstorm);
		SharedPreferenceUtil.getInstance().putInt("������", R.mipmap.type_one_thunder_rain);
		SharedPreferenceUtil.getInstance().putInt("��", R.mipmap.type_one_fog);
		SharedPreferenceUtil.getInstance().putInt("��", R.mipmap.type_one_fog);
//		}

	}

//	@Override
//	public void onMenuItemClick ( View clickedView, int position )
//	{
//
//	}


}
