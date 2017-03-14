package com.fengjie.myapplication.utils.rxbus.event;

/**
 * @author Created by MadJieJie on 2017/3/5-10:00.
 * @brief
 * @attention
 */

public enum EventTag
{
	DEFAULT(0),
	SUCCESS(- 100),
	FAILED(- 300),
	UPDATE(- 200),
	ERROR(- 404);
	
	private int value = 0;
	
	EventTag ( int value )
	{
		this.value = value;
	}
	
	public int value ()
	{
		return value;
	}
	
	public static int getTag ( EventTag eventTag )
	{
		int i = 0;
		switch ( eventTag )
		{
			case DEFAULT:
				i = 0;
				break;
			case SUCCESS:
				i = - 100;
				break;
			case FAILED:
				i = - 1;
				break;
			case UPDATE:
				i = - 200;
				break;
			case ERROR:
				i = - 404;
				break;
		}
		return i;
	}
}
