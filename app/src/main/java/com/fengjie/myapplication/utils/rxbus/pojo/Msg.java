package com.fengjie.myapplication.utils.rxbus.pojo;

/**
 * @author Created by MadJieJie on 2017/3/5-9:58.
 * @brief
 * @attention
 */

public class Msg
{
	public int code;        //消息编号
	public Object object;   //类型
	
	public Msg ( int code, Object object )
	{
		this.code = code;
		this.object = object;
	}
	
}
