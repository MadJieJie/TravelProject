package com.fengjie.myapplication.event;

public class Event
{
	private int mEvent = 0;
	
	public static final int EVENT_CHANGE_CITY = 1;      //发送改变城市标志位
	public static final int EVENT_LOGIN_SUCCESS = 2;    //发送登入成功标志位
	public static final int EVENT_UNREGISTER_USER = 3;  //发送注销标志位
	
	public Event ( int event )
	{
		this.mEvent = event;
	}
	
	public int getEvent ()
	{
		return mEvent;
	}


	
	
	
	
//    String city;
//    boolean isSetting;
	//	public Event ()
	//	{
	//	}
//    public Event ( boolean isSetting) {
//        this.isSetting = isSetting;
//    }
//
//    public Event ( String city) {
//        this.city = city;
//    }
}
