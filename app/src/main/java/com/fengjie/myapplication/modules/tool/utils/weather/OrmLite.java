package com.fengjie.myapplication.modules.tool.utils.weather;


import com.fengjie.myapplication.BuildConfig;
import com.fengjie.myapplication.base.weather.BaseApplication;
import com.fengjie.myapplication.base.weather.WeatherConstant;
import com.litesuits.orm.LiteOrm;

/**
 * Created by HugoXie on 16/7/24.
 * <p>
 * Email: Hugo3641@gamil.com
 * GitHub: https://github.com/xcc3641
 * Info:
 */
public class OrmLite
{
	
	static LiteOrm sLiteOrm;
	
	public static LiteOrm getInstance ()
	{
		getOrmHolder();
		return sLiteOrm;
	}
	
	private static OrmLite getOrmHolder ()
	{
		return OrmHolder.sInstance;
	}
	
	private OrmLite ()
	{
		if ( sLiteOrm == null )
		{
			sLiteOrm = LiteOrm.newSingleInstance(BaseApplication.getAppContext(), WeatherConstant.ORM_NAME);
		}
		sLiteOrm.setDebugged(BuildConfig.DEBUG);
	}
	
	private static class OrmHolder
	{
		private static final OrmLite sInstance = new OrmLite();
	}

//    public static <T> void OrmTest(Class<T> t) {
//        Observable.fromFuture(getInstance().query(t))
//            .compose(RxUtils.rxSchedulerHelper())
//            .subscribe(new SimpleSubscriber<T>() {
//                @Override
//                public void onNext(T t) {
//                    if (t instanceof CityORM) {
//                        PLog.w(((CityORM) t).getName());
//                    }
//                }
//            });
//    }
}
