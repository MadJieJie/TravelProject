package com.fengjie.myapplication.modules.tool.bean;

/**
 * @author Created by MadJieJie on 2017/3/17-14:54.
 * @brief
 * @attention
 */

public class Bill
{
	public static int EXIST_DATA = 0;
	public static int NOT_EXIST_DATA = -1;
	private int id;
	private String date;
	private int eat;
	private int buy;
	private int traffic;
	private int amusement;
	private int stay;
	private int ticket;
	private int treat;
	private int insurance;
	private int other;
	
	public int getId ()
	{
		return id;
	}
	
	public void setId ( int id )
	{
		this.id = id;
	}
	
	public String getDate ()
	{
		return date;
	}
	
	public void setDate ( String date )
	{
		this.date = date;
	}
	
	public int getEat ()
	{
		return eat;
	}
	
	public void setEat ( int eat )
	{
		this.eat = eat;
	}
	
	public int getBuy ()
	{
		return buy;
	}
	
	public void setBuy ( int buy )
	{
		this.buy = buy;
	}
	
	public int getTraffic ()
	{
		return traffic;
	}
	
	public void setTraffic ( int traffic )
	{
		this.traffic = traffic;
	}
	
	public int getAmusement ()
	{
		return amusement;
	}
	
	public void setAmusement ( int amusement )
	{
		this.amusement = amusement;
	}
	
	public int getStay ()
	{
		return stay;
	}
	
	public void setStay ( int stay )
	{
		this.stay = stay;
	}
	
	public int getTicket ()
	{
		return ticket;
	}
	
	public void setTicket ( int ticket )
	{
		this.ticket = ticket;
	}
	
	public int getTreat ()
	{
		return treat;
	}
	
	public void setTreat ( int treat )
	{
		this.treat = treat;
	}
	
	public int getInsurance ()
	{
		return insurance;
	}
	
	public void setInsurance ( int insurance )
	{
		this.insurance = insurance;
	}
	
	public int getOther ()
	{
		return other;
	}
	
	public void setOther ( int other )
	{
		this.other = other;
	}
	
	public int[] getAllArray ()
	{
		int[] array = { eat, traffic, buy, amusement, stay, ticket, treat, insurance, other };
		return array;
	}
	
	public Bill ( String date )
	{
		this.date = date;
	}
	public Bill (int id)
	{
		this.id = id;
	}
	
	public Bill ()
	{
	}
}
