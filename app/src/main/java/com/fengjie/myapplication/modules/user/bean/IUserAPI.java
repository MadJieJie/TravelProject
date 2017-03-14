package com.fengjie.myapplication.modules.user.bean;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;


/**
 * @author Created by MadJieJie on 2017/2/23-16:00.
 * @brief
 * @attention
 */

public interface IUserAPI
{
	String HOST = "http://192.168.191.1:8080/ServletTest/";
	
	@POST ( "travel" )
	@FormUrlEncoded
	Call< UserInfo > register ( @Field ( "biz" ) String biz,         //事务
	                            @Field ( "register_account" ) String register_account,   //账号
	                            @Field ( "register_password" ) String register_password, //密码
	                            @Field ( "user_name" ) String user_name );   //用户名
	
	@POST ( "travel" )
	@FormUrlEncoded
	Call< UserInfo > login ( @Field ( "biz" ) String biz,         //事务
	                            @Field ( "account" ) String account,   //账号
	                            @Field ( "password" ) String password); //密码
	
}
