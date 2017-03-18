package com.fengjie.myapplication.modules.tool.bean;

/**
 * @author Created by MadJieJie on 2017/3/16-14:21.
 * @brief
 * @attention
 */

public class BillRV
{
	private int id ;
	private String money;
	private String tpye;
	
	
	public BillRV ( int id, String money, String tpye )
	{
		this.id = id;
		this.money = money;
		this.tpye = tpye;
	}
	
	
	public int getId ()
	{
		return id;
	}
	
	public void setId ( int id )
	{
		this.id = id;
	}
	
	public String getMoney ()
	{
		return money;
	}
	
	public void setMoney ( String money )
	{
		this.money = money;
	}
	
	public String getTpye ()
	{
		return tpye;
	}
	
	public void setTpye ( String tpye )
	{
		this.tpye = tpye;
	}
	
}
