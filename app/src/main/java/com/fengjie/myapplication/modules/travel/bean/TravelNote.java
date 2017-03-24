package com.fengjie.myapplication.modules.travel.bean;

import com.fengjie.myapplication.modules.tool.db.weather.Time;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * @author Created by MadJieJie on 2017/3/19-16:22.
 * @brief
 * @attention
 */

public class TravelNote implements Serializable
{
	private static final long serialVersionUID = 111111L;
	
	@SerializedName ("id")
	public int id;
	@SerializedName("fk_user_uid")
	public int fk_user_uid;
	@SerializedName("title")
	public String title;
	@SerializedName("author")
	public String author;
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
	
	public String getAuthor ()
	{
		return author;
	}
	
	public void setAuthor ( String anthor)
	{
		this.author = anthor;
	}
	
	public String getContent()
	{
		return content;
	}
	
	public void setContent(String content)
	{
		this.content = content;
	}
	
	public String getCreateTime()
	{
		return createTime;
	}
	
	public void setCreateTime(String createTime)
	{
		this.createTime = createTime;
	}
	
	
	public String getUpdateTime()
	{
		return updateTime;
	}
	
	
	public void setUpdateTime(String updateTime)
	{
		this.updateTime = updateTime;
	}
	
	public TravelNote ( String title, String author, String content, String createTime )
	{
		this.title = title;
		this.author = author;
		this.content = content;
		this.createTime = createTime;
	}
	
	/**
	 * test
	 */
	public TravelNote returnTestInfo()
	{
		this.title = "MadJieJIe";
		this.fk_user_uid = 26;
		this.content = "MadJIeJIe";
		this.createTime = Time.getNowYMDHMSTime();
		return this;
	}
	
	public TravelNote ()
	{
	}
}
