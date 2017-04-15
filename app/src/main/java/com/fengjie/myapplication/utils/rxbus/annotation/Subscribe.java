package com.fengjie.myapplication.utils.rxbus.annotation;

import android.support.annotation.NonNull;

import com.fengjie.myapplication.utils.rxbus.event.EventThread;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Created by MadJieJie on 2017/3/5-9:59.
 * @brief
 * @attention
 */
@Retention ( RetentionPolicy.RUNTIME)
@Target ( ElementType.METHOD)
public @interface Subscribe
{
	@NonNull int tag ();     //tag不能为空
	
	EventThread thread () default EventThread.MAIN_THREAD;   //默认为主线程
}
