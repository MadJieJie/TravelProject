package com.fengjie.myapplication.modules.user.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class UserInfo implements Serializable
{
	/**
	 * uid : 0
	 * name : null
	 * age : 0
	 * result : 注册成功
	 * sex : null
	 * local : null
	 */
	
	@SerializedName ( "uid" )
	public int uid;
	@SerializedName ( "name" )
	public String name;
	@SerializedName ( "age" )
	public int age;
	@SerializedName ( "result" )
	public String result;
	@SerializedName ( "sex" )
	public String sex;
	@SerializedName ( "local" )
	public String local;


//	/**
//	 * uid : 0
//	 * name : 0
//	 * age : 0
//	 * result : null
//	 * sex : null
//	 * local : null
//	 */
//
//	@SerializedName ( "uid" )
//	public int uid;
//	@SerializedName ( "name" )
//	public int name;
//	@SerializedName ( "age" )
//	public int age;
//	@SerializedName ( "result" )
//	public String result;
//	@SerializedName ( "sex" )
//	public String sex;
//	@SerializedName ( "local" )
//	public String local;
	
}
