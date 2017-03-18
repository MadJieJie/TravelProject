package com.fengjie.myapplication.modules.user.utils;

import com.fengjie.myapplication.BuildConfig;
import com.fengjie.myapplication.base.BaseApplication;
import com.fengjie.myapplication.modules.tool.db.weather.CityORM;
import com.fengjie.myapplication.modules.user.bean.IUserAPI;
import com.fengjie.myapplication.modules.user.bean.UserInfo;
import com.fengjie.myapplication.utils.often.LogUtils;
import com.fengjie.myapplication.utils.often.ToastUtils;
import com.fengjie.myapplication.modules.tool.utils.weather.OrmLite;
import com.fengjie.myapplication.modules.tool.utils.weather.RxUtils;
import com.fengjie.myapplication.utils.often.Utils;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.litesuits.orm.db.assit.WhereBuilder;

import java.io.File;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by MadJIeJIe on 2015/12/16.
 * this is Retrofit Singleton to help to get scenery data.
 */
public class RetrofitUser
{
	
	private static IUserAPI sApiService = null;
	private static Retrofit sRetrofit = null;
	private static OkHttpClient sOkHttpClient = null;
	
	private void init ()
	{
		initOkHttp();
		initRetrofit();
		sApiService = sRetrofit.create(IUserAPI.class);
	}
	
	private RetrofitUser ()
	{
		init();
	}
//	private RetrofitUser ()
//	{
//		init();
//	}
	
	public static RetrofitUser getInstance ()
	{
		return SingletonHolder.INSTANCE;
	}
	
	/**
	 * 单例持有者
	 */
	private static class SingletonHolder
	{
		private static final RetrofitUser INSTANCE = new RetrofitUser();
	}
	
	private static void initOkHttp ()
	{
		OkHttpClient.Builder builder = new OkHttpClient.Builder();
		if ( BuildConfig.DEBUG )
		{
			// https://drakeet.me/retrofit-2-0-okhttp-3-0-config
			HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
			loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
			builder.addInterceptor(loggingInterceptor);
		}
		// 缓存 http://www.jianshu.com/p/93153b34310e
		File cacheFile = new File(BaseApplication.getAppCacheDir(), "/user");/**缓存路径*/
		Cache cache = new Cache(cacheFile, 1024 * 1024 * 50);
		Interceptor cacheInterceptor = chain ->
		{
			Request request = chain.request();
			if ( ! Utils.isNetworkConnected(BaseApplication.getAppContext()) )
			{
				request = request.newBuilder()
						          .cacheControl(CacheControl.FORCE_CACHE)
						          .build();
			}
			Response response = chain.proceed(request);
			Response.Builder newBuilder = response.newBuilder();
			if ( Utils.isNetworkConnected(BaseApplication.getAppContext()) )
			{
				int maxAge = 0;
				// 有网络时 设置缓存超时时间0个小时
				newBuilder.header("Cache-Control", "public, max-age=" + maxAge);
			} else
			{
				// 无网络时，设置超时为4周
				int maxStale = 60 * 60 * 24 * 28;
				newBuilder.header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale);
			}
			return newBuilder.build();
		};
		builder.cache(cache).addInterceptor(cacheInterceptor);
		//设置超时
		builder.connectTimeout(15, TimeUnit.SECONDS);
		builder.readTimeout(20, TimeUnit.SECONDS);
		builder.writeTimeout(20, TimeUnit.SECONDS);
		//错误重连
		builder.retryOnConnectionFailure(true);
		sOkHttpClient = builder.build();
	}
	
	private static void initRetrofit ()
	{
		sRetrofit = new Retrofit.Builder()
				            .baseUrl(IUserAPI.HOST)          /**访问URL*/
				            .client(sOkHttpClient)
				            .addConverterFactory(GsonConverterFactory.create())
				            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
				            .build();
	}
	
	public static void disposeFailureInfo ( Throwable t )
	{
		if ( t.toString().contains("GaiException") || t.toString().contains("SocketTimeoutException") ||
				     t.toString().contains("UnknownHostException") )
		{
			ToastUtils.showShort("网络问题");
		} else if ( t.toString().contains("API没有") )
		{
			OrmLite.getInstance().delete(new WhereBuilder(CityORM.class).where("name=?", Utils.replaceInfo(t.getMessage())));
			LogUtils.w(Utils.replaceInfo(t.getMessage()));
			ToastUtils.showShort("错误: " + t.getMessage());
		}
//        PLog.w(t.getMessage());
	}
	
	public IUserAPI getApiService ()
	{
		return sApiService;
	}
	
	/**
	 * 返回-被观察者
	 *
	 * @param account
	 * @param password
	 * @return
	 */
	
	public Observable< UserInfo > getRegisterResult ( final String biz, final String account, final String password, final String userName )
	{
		
		return Observable.create(new ObservableOnSubscribe< UserInfo >()
		{
			@Override
			public void subscribe ( ObservableEmitter< UserInfo > e ) throws Exception
			{
				sApiService.register(biz, account, password, userName)
						.enqueue(new Callback< UserInfo >()
						{
							@Override
							public void onResponse ( Call< UserInfo > call, retrofit2.Response< UserInfo > response )
							{
								if ( response.isSuccessful() )
									e.onNext(response.body());
							}
							
							@Override
							public void onFailure ( Call< UserInfo > call, Throwable t )
							{
								ToastUtils.showShort("请求网络失败");
								LogUtils.e(t.toString());
							}
						});
			}
		}).compose(RxUtils.rxSchedulerHelper());
		
	}
	
	public Observable< UserInfo > getLoginResult ( final String biz, final String account, final String password )
	{
		
		return Observable.create(new ObservableOnSubscribe< UserInfo >()
		{
			@Override
			public void subscribe ( ObservableEmitter< UserInfo > e ) throws Exception
			{
				sApiService.login(biz, account, password)
						.enqueue(new Callback< UserInfo >()
						{
							@Override
							public void onResponse ( Call< UserInfo > call, retrofit2.Response< UserInfo > response )
							{
								if ( response.isSuccessful() )
									e.onNext(response.body());
							}
							
							@Override
							public void onFailure ( Call< UserInfo > call, Throwable t )
							{
								ToastUtils.showShort("请求网络失败");
								LogUtils.e(t.toString());
							}
						});
			}
		}).compose(RxUtils.rxSchedulerHelper());
		
	}
	
	
}
