package com.fengjie.myapplication.modules.user.base;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * @author Created by MadJieJie on 2017/3/22-16:36.
 * @brief
 * @attention
 */

public class Biz implements Serializable
{
	private static final long serialVersionUID = 111111L;
	
	@SerializedName ("result")		//结果 :success/fail
	public String result;
	
	@SerializedName("reason")		//导致原因
	public String reason;
	
	public Biz ( String result, String reason )
	{
		this.result = result;
		this.reason = reason;
	}
}
