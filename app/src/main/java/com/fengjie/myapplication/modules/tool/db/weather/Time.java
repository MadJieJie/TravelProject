package com.fengjie.myapplication.modules.tool.db.weather;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Time
{
	
	/**
	 * yyyy-MM-dd HH:mm:ss
	 */
	@SuppressLint ( "SimpleDateFormat" )
	public static String getNowYMDHMSTime ()
	{
		SimpleDateFormat mDateFormat = new SimpleDateFormat(
				                                                   "yyyy-MM-dd HH:mm:ss");
		return mDateFormat.format(new Date());
	}
	
	/**
	 * MM-dd HH:mm:ss
	 */
	@SuppressLint ( "SimpleDateFormat" )
	public static String getNowMDHMSTime ()
	{
		SimpleDateFormat mDateFormat = new SimpleDateFormat(
				                                                   "MM-dd HH:mm:ss");
		return mDateFormat.format(new Date());
	}
	
	/**
	 * MM-dd
	 */
	@SuppressLint ( "SimpleDateFormat" )
	public static String getNowYMD ()
	{
		
		SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return mDateFormat.format(new Date());
	}
	
	/**
	 * yyyy
	 */
	@SuppressLint ( "SimpleDateFormat" )
	public static String getNowYear ()
	{
		
		SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy");
		return mDateFormat.format(new Date());
	}
	
	/**
	 * mm
	 */
	@SuppressLint ( "SimpleDateFormat" )
	public static String getNowMonth ()
	{
		
		SimpleDateFormat mDateFormat = new SimpleDateFormat(
				                                                   "MM");
		return mDateFormat.format(new Date());
	}
	/**
	 * dd
	 */
	@SuppressLint ( "SimpleDateFormat" )
	public static String getNowDay ()
	{
		
		SimpleDateFormat mDateFormat = new SimpleDateFormat(
				                                                   "dd");
		return mDateFormat.format(new Date());
	}
	
	/**
	 * yyyy-MM-dd
	 */
	@SuppressLint ( "SimpleDateFormat" )
	public static String getYMD ( Date date )
	{
		
		SimpleDateFormat mDateFormat = new SimpleDateFormat(
				                                                   "yyyy-MM-dd");
		return mDateFormat.format(date);
	}
	
	@SuppressLint ( "SimpleDateFormat" )
	public static String getMD ( Date date )
	{
		
		SimpleDateFormat mDateFormat = new SimpleDateFormat(
				                                                   "MM-dd");
		return mDateFormat.format(date);
	}
	
}
