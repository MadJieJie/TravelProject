package com.fengjie.myapplication.utils.rxbus.event;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author Created by MadJieJie on 2017/3/5-10:00.
 * @brief
 * @attention
 */

public enum EventThread
{
	/**
	 * 主线程
	 */
	MAIN_THREAD,
	/**
	 * 新的线程
	 */
	NEW_THREAD,
	/**
	 * 读写线程
	 */
	IO,
	/**
	 * 计算工作默认线程
	 */
	COMPUTATION,
	/**
	 * 在当前线程中按照队列方式执行
	 */
	TRAMPOLINE;
	
	public static Scheduler getScheduler ( EventThread threadMode )
	{
		Scheduler scheduler;
		switch ( threadMode )
		{
			case MAIN_THREAD:           //Main_thread
				scheduler = AndroidSchedulers.mainThread();
				break;
			case NEW_THREAD:            //New_thread
				scheduler = Schedulers.newThread();
				break;
			case IO:                    //IO_thread
				scheduler = Schedulers.io();
				break;
			case COMPUTATION:
				scheduler = Schedulers.computation();
				break;
			case TRAMPOLINE:
				scheduler = Schedulers.trampoline();
				break;
			default:
				scheduler = AndroidSchedulers.mainThread();
		}
		return scheduler;
	}
}
