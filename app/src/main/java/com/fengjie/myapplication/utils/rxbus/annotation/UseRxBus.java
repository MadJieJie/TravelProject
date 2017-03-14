package com.fengjie.myapplication.utils.rxbus.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Created by MadJieJie on 2017/3/5-10:01.
 * @brief
 * @attention
 */
@Retention ( RetentionPolicy.RUNTIME)
@Target ({ElementType.METHOD, ElementType.TYPE})
public @interface UseRxBus
{
}
