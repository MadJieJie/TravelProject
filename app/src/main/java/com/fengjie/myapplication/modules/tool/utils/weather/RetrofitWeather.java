package com.fengjie.myapplication.modules.tool.utils.weather;

import com.fengjie.myapplication.BuildConfig;
import com.fengjie.myapplication.base.BaseApplication;
import com.fengjie.myapplication.base.weather.WeatherConstant;
import com.fengjie.myapplication.modules.tool.bean.Weather;
import com.fengjie.myapplication.modules.tool.db.weather.ApiInterface;
import com.fengjie.myapplication.modules.tool.db.weather.CityORM;
import com.fengjie.myapplication.modules.tool.db.weather.VersionAPI;
import com.fengjie.myapplication.utils.often.ToastUtils;
import com.fengjie.myapplication.utils.often.Utils;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.litesuits.orm.db.assit.WhereBuilder;

import java.io.File;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by zk on 2015/12/16. update by hugo thanks for brucezz
 */
public class RetrofitWeather
{

	private static ApiInterface sApiService = null;
	private static Retrofit sRetrofit = null;
	private static OkHttpClient sOkHttpClient = null;

	private void init ()
	{
		initOkHttp();
		initRetrofit();
		sApiService = sRetrofit.create(ApiInterface.class);
	}

	private RetrofitWeather ()
	{
		init();
	}

	public static RetrofitWeather getInstance ()
	{
		return SingletonHolder.INSTANCE;
	}



	private static class SingletonHolder
	{
		private static final RetrofitWeather INSTANCE = new RetrofitWeather();
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
		File cacheFile = new File(BaseApplication.getAppCacheDir(), "/NetCache");
		Cache cache = new Cache(cacheFile, 1024 * 1024 * 50);
		Interceptor cacheInterceptor = chain -> {
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
				            .baseUrl(ApiInterface.HOST)
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
//            PLog.w(Util.replaceInfo(t.getMessage()));
			ToastUtils.showShort("错误: " + t.getMessage());
		}
//        PLog.w(t.getMessage());
	}

	public ApiInterface getApiService ()
	{
		return sApiService;
	}

	public Observable< Weather > fetchWeather ( String city )
	{
//		sApiService = sRetrofit.create(ApiInterface.class);

		return sApiService.mWeatherAPI(city, WeatherConstant.KEY)
				       .flatMap(weatherAPI -> {
					       String status = weatherAPI.mHeWeatherDataService30s.get(0).status;
					       if ( "no more requests".equals(status) )
					       {
						       return Observable.error(new RuntimeException("/(ㄒoㄒ)/~~,API免费次数已用完"));
					       } else if ( "unknown city".equals(status) )
					       {
						       return Observable.error(new RuntimeException(String.format("API没有%s", city)));
					       }
					       return Observable.just(weatherAPI);
				       })
				       .map(weatherAPI -> weatherAPI.mHeWeatherDataService30s.get(0))
				       .compose(RxUtils.rxSchedulerHelper());
	}



    public Observable<VersionAPI > fetchVersion() {
        return sApiService.mVersionAPI(WeatherConstant.API_TOKEN).compose(RxUtils.rxSchedulerHelper());
    }
}
