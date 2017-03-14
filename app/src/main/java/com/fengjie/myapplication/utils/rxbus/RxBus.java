package com.fengjie.myapplication.utils.rxbus;

import android.support.annotation.NonNull;

import com.fengjie.myapplication.utils.rxbus.annotation.Subscribe;
import com.fengjie.myapplication.utils.rxbus.annotation.UseRxBus;
import com.fengjie.myapplication.utils.rxbus.event.EventThread;
import com.fengjie.myapplication.utils.rxbus.pojo.Msg;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * @author Created by MadJieJie on 2017/3/5-9:33.
 * @brief RxBus
 * @attention
 */

public class RxBus
{
	private static RxBus INSTANCE = null;
	
	public static RxBus getInstance ()
	{
		if ( INSTANCE == null )
		{
			synchronized( RxBus.class )
			{
				if ( INSTANCE == null )
				{
					INSTANCE = new RxBus();
				}
			}
		}
		return INSTANCE;
	}
	
	//TAG默认值
	public static final int TAG_DEFAULT = - 1000;
	public static final int TAG_UPDATE = - 1010;
	public static final int TAG_CHANGE = - 1020;
	public static final int TAG_OTHER = - 1030;
	public static final int TAG_ERROR = - 1090;
	//TAG-class
	private static Map< Class, Integer > tag4Class = new HashMap<>();
	//发布者
	private final Subject bus;
	
	//存放订阅者信息
	private Map< Object, CompositeDisposable > subscriptions = new HashMap<>();
	
	/**
	 * PublishSubject 创建一个可以在订阅之后把数据传输给订阅者Subject
	 * SerializedSubject 序列化Subject为线程安全的Subject RxJava2 暂无
	 */
	public RxBus ()
	{
		bus = PublishSubject.create().toSerialized();
	}
	
	public void post ( @NonNull Object obj )
	{
		post(TAG_DEFAULT, obj);
	}
	
	/**
	 * 发布事件
	 *
	 * @param code 值使用RxBus.getInstance().getTag(class,value)获取
	 * @param obj  为需要被处理的事件
	 */
	public void post ( @NonNull int code, @NonNull Object obj )
	{
		bus.onNext(new Msg(code, obj));
	}
	
	public Observable< Object > tObservable ()
	{
		return tObservable(Object.class);
	}
	
	public < T > Observable< T > tObservable ( Class< T > eventType )
	{
		return tObservable(TAG_DEFAULT, eventType);
	}
	
	/**
	 * 订阅事件
	 *
	 * @return
	 */
	@SuppressWarnings ( "unchecked" )
	public < T > Observable tObservable ( int code, final Class< T > eventType )
	{
		return bus.ofType(Msg.class)//判断接收事件类型
				       .filter(new Predicate< Msg >()
				       {
					       @Override
					       public boolean test ( Msg msg ) throws Exception
					       {
						       return msg.code == code;
					       }
				       })
				       .map(new Function< Msg, Object >()
				       {
					       @Override
					       public Object apply ( Msg msg ) throws Exception
					       {
						       return msg.object;
					       }
				       })
				       .cast(eventType);
	}
	
	/**
	 * 判断是否需要订阅，如果需要订阅那么自动控制生命周期
	 */
	public void init ( @NonNull Object object )
	{
		Flowable.just(object)
				.map(o -> o.getClass().getAnnotation(UseRxBus.class))
				.filter(a -> a != null)
				.subscribe(u ->
				{
					addTag4Class(object.getClass());
					register(object);
				}, e -> e.getMessage());
	}
	
	/**
	 * 订阅者注册
	 *
	 * @param subscriber
	 */
	public RxBus register ( @NonNull Object subscriber )
	{
		Flowable.just(subscriber)
				.filter(s -> subscriptions.get(subscriber) == null) //判断订阅者没有在序列中
				.flatMap(s -> Flowable.fromArray(s.getClass().getDeclaredMethods()))
				.map(m ->
				{
					m.setAccessible(true);
					return m;
				})
				.filter(m -> m.isAnnotationPresent(Subscribe.class))
				.subscribe(m -> addSubscription(m, subscriber));
		return INSTANCE;
	}
	
	/**
	 * 添加订阅
	 *
	 * @param m          方法
	 * @param subscriber 订阅者
	 */
	@SuppressWarnings ( "unchecked" )
	private void addSubscription ( Method m, Object subscriber )
	{
		//获取方法内参数
		Class[] parameterType = m.getParameterTypes();
		//只获取第一个方法参数，否则默认为Object
		Class cla = Object.class;
		if ( parameterType.length > 1 )
		{
			cla = parameterType[0];
		}
		//获取注解
		Subscribe sub = m.getAnnotation(Subscribe.class);
		//订阅事件
		Disposable disposable = tObservable(getTag(subscriber.getClass(), sub.tag()), cla)
				                        .observeOn(EventThread.getScheduler(sub.thread()))
				                        .subscribe(o ->
						                        {
							                        try
							                        {
								                        m.invoke(subscriber, o);
							                        } catch( IllegalAccessException e )
							                        {
								                        e.printStackTrace();
							                        } catch( InvocationTargetException e )
							                        {
								                        e.printStackTrace();
							                        }
						                        },
						                        e -> System.out.println("this object is not invoke"));
		putSubscriptionsData(subscriber, disposable);
	}
	
	/**
	 * 添加订阅者到map空间来unRegister
	 *
	 * @param subscriber 订阅者
	 * @param disposable 订阅者 Subscription
	 */
	private void putSubscriptionsData ( Object subscriber, Disposable disposable )
	{
		CompositeDisposable subs = subscriptions.get(subscriber);
		if ( subs == null )
		{
			subs = new CompositeDisposable();
		}
		subs.add(disposable);
		subscriptions.put(subscriber, subs);
	}
	
	/**
	 * 添加序列
	 * 根据object 生成唯一id
	 */
	private Integer tag = - 1000;
	
	private void addTag4Class ( Class cla )
	{
		tag4Class.put(cla, tag);
		tag--;
	}
	
	/**
	 * tag值使用RxBus.getInstance().getTag(class,value)获取
	 * 使用getTag主要用于后期维护方便，可以及时找到发布事件的对应处理。
	 *
	 * @param cla   为Rxbus事件处理的类
	 * @param value 是事件处理的tag
	 * @return tag
	 */
	public int getTag ( Class cla, int value )
	{
		return tag4Class.get(cla).intValue() + value;
	}
	
	/**
	 * 解除订阅者
	 *
	 * @param subscriber 订阅者
	 */
	public void unRegister ( Object subscriber )
	{
		Flowable.just(subscriber)
				.filter(s -> s != null)
				.map(s -> subscriptions.get(s))
				.filter(subs -> subs != null)
				.subscribeWith(new Subscriber< CompositeDisposable >()
				{
					@Override
					public void onSubscribe ( Subscription s )
					{
						
					}
					
					@Override
					public void onNext ( CompositeDisposable compositeDisposable )
					{
						compositeDisposable.dispose();      //截流
						subscriptions.remove(subscriber);   //删除订阅者
					}
					
					@Override
					public void onError ( Throwable t )
					{
						
					}
					
					@Override
					public void onComplete ()
					{
						
					}
				});
	}


//    // 主题
//    private final Subject mBus;
//    // PublishSubject只会把在订阅发生的时间点之后来自原始Observable的数据发射给观察者
//    private RxBus () {
//        mBus = PublishSubject.create().toSerialized();
//    }
//
//    public static RxBus getDefault() {
//        return RxBusHolder.sInstance;
//    }
//
//    private static class RxBusHolder {
//        private static final RxBus sInstance = new RxBus();
//    }
//
//
//    // 提供了一个新的事件
//    public void post(Object o) {
//        mBus.onNext(o);
//    }
//
//    // 根据传递的 eventType 类型返回特定类型(eventType)的 被观察者
//    public <T> Observable<T> toObservable( Class<T> eventType) {
//        return mBus.ofType(eventType);
//    }
}
