package com.fengjie.myapplication.utils.other;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

public class ActivityCollectorUtils
{

	public static List< Activity > activities = new ArrayList< Activity >();

	public static void addActivity ( Activity activitiy )
	{
		activities.add(activitiy);
	}

	public static void removeActivity ( Activity activitiy )
	{
		activities.remove(activitiy);
	}

	public static void finishAll ()
	{
		for ( Activity activitiy : activities )
		{
			if ( ! activitiy.isFinishing() )
			{
				activitiy.finish();
			}
		}
	}

}
