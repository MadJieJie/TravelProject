package com.fengjie.myapplication.modules.tool.db.bill;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.fengjie.myapplication.base.BaseApplication;
import com.fengjie.myapplication.modules.tool.bean.Bill;
import com.fengjie.myapplication.utils.often.LogUtils;
import com.fengjie.myapplication.utils.often.SQLiteOpenUtils;

/**
 * @author Created by MadJieJie on 2017/3/17-14:53.
 * @brief
 * @attention
 */

public class BillDao
{
	private static SQLiteOpenUtils helper = new SQLiteOpenUtils(BaseApplication.getAppContext());         //需要该对象,才能对数据库进行操作
	
	private static final String SQL_QUERY_DATA_EXIST = "SELECT  date,eat,traffic,buy,amusement,stay,ticket,treat,insurance,other FROM bill WHERE date = ?";
	private static final String SQL_INSERT_DATA = "INSERT INTO bill (date,eat,traffic,buy,amusement,stay,ticket,treat,insurance,other) VALUES(?,?,?,?,?,?,?,?,?,?)";
	private static final String SQL_UPDATE_DATA = "UPDATE  bill set " +
			                                              "eat= ?,traffic= ?,buy= ?,amusement= ?,stay= ?,ticket= ?,treat= ?,insurance= ?,other= ? " +
			                                              "WHERE date = ?";
	
	/**
	 * 通过日期查询数据库中的数据
	 *
	 * @param date
	 * @return
	 */
	public static Bill getQueryBillForDB ( String date )
	{
		SQLiteDatabase db = helper.getWritableDatabase();
		Cursor cursor = null;
		
		try
		{
			cursor = db.rawQuery(SQL_QUERY_DATA_EXIST, new String[]{ date });
			while ( cursor.moveToNext() )
			{
				Bill bill = new Bill();
//				bill.setDate(cursor.getString(cursor.getColumnIndex("date")));
				bill.setEat(cursor.getInt(cursor.getColumnIndex("eat")));
				bill.setTraffic(cursor.getInt(cursor.getColumnIndex("traffic")));
				bill.setBuy(cursor.getInt(cursor.getColumnIndex("buy")));
				bill.setAmusement(cursor.getInt(cursor.getColumnIndex("amusement")));
				bill.setStay(cursor.getInt(cursor.getColumnIndex("stay")));
				bill.setTicket(cursor.getInt(cursor.getColumnIndex("ticket")));
				bill.setInsurance(cursor.getInt(cursor.getColumnIndex("insurance")));
				bill.setOther(cursor.getInt(cursor.getColumnIndex("other")));
				return bill;
			}
			
		} catch( SQLException e )
		{
			e.printStackTrace();
			LogUtils.d("Debug", "getQueryBill: " + e.toString());
		} finally
		{
			if ( cursor != null )
				cursor.close();
			if ( db != null )
				db.close();
		}
		
		return null;
	}
	
	/**
	 * 插入数据进入数据库
	 *
	 * @param bill
	 * @return
	 */
	public static boolean insertDataToDB ( Bill bill )
	{
		SQLiteDatabase db = helper.getWritableDatabase();
		
		try
		{
			if ( bill != null )
			{
				db.execSQL(SQL_INSERT_DATA,
						new String[]{ bill.getDate(), bill.getEat() + "", bill.getTraffic() + "", bill.getBuy() + "", bill.getAmusement() + "",
								bill.getStay() + "", bill.getTicket() + "", bill.getTreat() + "", bill.getInsurance() + "", bill.getOther() + "" });
				return true;
			} else
			{
				return false;
			}
		} catch( SQLException e )
		{
			e.printStackTrace();
			LogUtils.d("Debug", "insertDataToDB: " + e.toString());
		} finally
		{
			db.close();
		}
		return true;
	}
	
	/**
	 * 通过ID查询进行更新
	 *
	 * @return
	 */
	public static boolean updateDataToDB ( Bill bill )
	{
		SQLiteDatabase db = helper.getWritableDatabase();
		
		try
		{
			db.execSQL(SQL_UPDATE_DATA
					, new String[]{ bill.getEat() + "", bill.getTraffic() + "", bill.getBuy() + "", bill.getAmusement() + "",
							bill.getStay() + "", bill.getTicket() + "", bill.getTreat() + "", bill.getInsurance() + "", bill.getOther() + ""
							, bill.getDate() });
			
			return true;
		} catch( SQLException e )
		{
			e.printStackTrace();
			LogUtils.d("Debug", "insertDataToDB: " + e.toString());
		} finally
		{
			db.close();
		}
		return true;
	}
	
}
		