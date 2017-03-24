package com.fengjie.myapplication.modules.run.bean;

import com.fengjie.myapplication.modules.tool.db.weather.Time;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * @author Created by MadJieJie on 2017/3/19-16:22.
 * @brief
 * @attention
 */

public class Note implements Serializable
{
	private static final long serialVersionUID = 11111L;
	
	@SerializedName ("id")
	public int id;
	@SerializedName("fk_user_uid")
	public int fk_user_uid;
	@SerializedName("title")
	public String title;
	@SerializedName("content")
	public String content;
	@SerializedName("createTime")
	public String createTime;
	@SerializedName("updateTime")
	public String updateTime;
	
	public int getId()
	{
		return id;
	}
	
	public void setId(int id)
	{
		this.id = id;
	}
	
	public int getFk_user_uid()
	{
		return fk_user_uid;
	}
	
	public void setFk_user_uid(int fk_user_uid)
	{
		this.fk_user_uid = fk_user_uid;
	}
	
	public String getTitle()
	{
		return title;
	}
	
	public void setTitle(String title)
	{
		this.title = title;
	}
	
	public String getContent()
	{
		return content;
	}
	
	public void setContent(String content)
	{
		this.content = content;
	}
	
	public String getCreate_time()
	{
		return createTime;
	}
	
	public void setCreate_time(String createTime)
	{
		this.createTime = createTime;
	}
	
	public String getUpdate_time()
	{
		return updateTime;
	}
	
	public void setUpdate_time(String updateTime)
	{
		this.updateTime = updateTime;
	}
	
	public Note ( String title, String content, String createTime, String updateTtime )
	{
		this.title = title;
		this.content = content;
		this.createTime = createTime;
		this.updateTime = updateTtime;
	}
	
	/**
	 * test
	 * @param title
	 */
	public Note(String title)
	{
		this.title = title;
		this.fk_user_uid = 26;
		this.content = "MadJIeJIe";
		this.createTime = Time.getNowYMDHMSTime();
		this.updateTime = Time.getNowYMDHMSTime();
	}
	
	public Note ()
	{
	}
}
