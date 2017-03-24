package com.fengjie.myapplication.modules.travel.db;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.fengjie.myapplication.base.BaseApplication;
import com.fengjie.myapplication.modules.travel.bean.TravelNote;
import com.fengjie.myapplication.utils.often.LogUtils;
import com.fengjie.myapplication.utils.often.SQLiteOpenUtils;
import com.fengjie.myapplication.utils.often.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Created by MadJieJie on 2017/3/17-14:53.
 * @brief
 * @attention
 */

public class TravelNoteDao
{
	private static final SQLiteOpenUtils helper = new SQLiteOpenUtils(BaseApplication.getAppContext());         //需要该对象,才能对数据库进行操作
	
	private static final String SQL_QUERY_ALL_DATA_EXIST = "SELECT  id,title,author,content,create_time,update_time FROM travel_note";
	private static final String SQL_QUERY_DATA_EXIST_USE_TITLE = "SELECT  id,title,author,content,create_time,update_time FROM travel_note WHERE title = ?";
	private static final String SQL_QUERY_DATA_EXIST_USE_ID = "SELECT  id,title,author,content,create_time,update_time FROM travel_note WHERE id = ?";
	private static final String SQL_INSERT_DATA = "INSERT INTO travel_note (title,author,content,create_time,update_time) VALUES(?,?,?,?,?)";
	private static final String SQL_UPDATE_DATA = "UPDATE  travel_note  SET " +
			                                              "title= ?,author = ?,content= ?,update_time= ? " +
			                                              "WHERE id = ?";
	private static final String SQL_DELETE_DATA_USE_ID = "DELETE FROM travel_note WHERE id = ?";
	
	/**
	 * 通过title查询数据库中的数据
	 *
	 * @param title
	 * @return
	 */
	public static TravelNote getQueryInfoForDB ( String title )
	{
		SQLiteDatabase db = helper.getWritableDatabase();
		Cursor cursor = null;
		
		try
		{
			cursor = db.rawQuery(SQL_QUERY_DATA_EXIST_USE_TITLE, new String[]{ title });
			while ( cursor.moveToFirst() )      //移动向第一个
			{
				TravelNote note = new TravelNote();
				note.setId(cursor.getInt(cursor.getColumnIndex("id")));
				note.setTitle(cursor.getString(cursor.getColumnIndex("title")));
				note.setFk_user_uid(BaseApplication.sUserID);               //返回当前用户的外键
				note.setAuthor(cursor.getString(cursor.getColumnIndex("author")));
				note.setContent(cursor.getString(cursor.getColumnIndex("content")));
				note.setCreateTime(cursor.getString(cursor.getColumnIndex("create_time")));
				note.setUpdateTime(cursor.getString(cursor.getColumnIndex("update_time")));
				return note;
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
	 * 通过title查询数据库中的数据
	 *
	 * @param id
	 * @return
	 */
	public static TravelNote getQueryInfoForDB ( int id )
	{
		SQLiteDatabase db = helper.getWritableDatabase();
		Cursor cursor = null;
		
		try
		{
			cursor = db.rawQuery(SQL_QUERY_DATA_EXIST_USE_ID, new String[]{ id + "" });
			while ( cursor.moveToFirst() )      //移动向第一个
			{
				TravelNote note = new TravelNote();
				note.setId(cursor.getInt(cursor.getColumnIndex("id")));
				note.setFk_user_uid(BaseApplication.sUserID);               //返回当前用户的外键
				note.setTitle(cursor.getString(cursor.getColumnIndex("title")));
				note.setAuthor(cursor.getString(cursor.getColumnIndex("author")));
				note.setContent(cursor.getString(cursor.getColumnIndex("content")));
				note.setCreateTime(cursor.getString(cursor.getColumnIndex("create_time")));
				note.setUpdateTime(cursor.getString(cursor.getColumnIndex("update_time")));
				return note;
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
	 * 搜寻数据库中的所有数据
	 *
	 * @return List<TravelNote>
	 */
	public static ArrayList< TravelNote > getQueryAllInfoForDB ()
	{
		SQLiteDatabase db = helper.getWritableDatabase();
		Cursor cursor = null;
		ArrayList< TravelNote > list = new ArrayList<>();
		try
		{
			cursor = db.rawQuery(SQL_QUERY_ALL_DATA_EXIST, null);
			while ( cursor.moveToNext() )
			{
				TravelNote note = new TravelNote();
				note.setId(cursor.getInt(cursor.getColumnIndex("id")));
				note.setFk_user_uid(BaseApplication.sUserID);       //返回当前用户的外键
				note.setTitle(cursor.getString(cursor.getColumnIndex("title")));
				note.setAuthor(cursor.getString(cursor.getColumnIndex("author")));
				note.setContent(cursor.getString(cursor.getColumnIndex("content")));
				note.setCreateTime(cursor.getString(cursor.getColumnIndex("create_time")));
				note.setUpdateTime(cursor.getString(cursor.getColumnIndex("update_time")));
				list.add(note);
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
		
		return list;
	}
	
	/**
	 * 插入数据进入数据库
	 *
	 * @param note
	 * @return
	 */
	public static boolean insertDataToDB ( TravelNote note )
	{
		if ( note != null )
		{
			SQLiteDatabase db = helper.getWritableDatabase();
			
			try
			{
				if ( note != null )
				{
					db.execSQL(SQL_INSERT_DATA,
							new String[]{ note.getTitle(), note.getAuthor(), note.getContent(), note.getCreateTime(), note.getUpdateTime() });
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
		} else
		{
			ToastUtils.showShort(BaseApplication.sAppContext, "内容为空,请输入内容");
			return false;
		}
		return true;
	}
	
	/**
	 * 插入数据进入数据库
	 *
	 * @param list
	 * @return
	 */
	public static boolean insertDataToDB ( List< TravelNote > list )
	{
		if ( list != null )
		{
			SQLiteDatabase db = helper.getWritableDatabase();
			
			try
			{
				for ( TravelNote note : list )
				{
					db.execSQL(SQL_INSERT_DATA,
							new String[]{ note.getTitle(), note.getAuthor(), note.getContent(), note.getCreateTime(), note.getUpdateTime() });
				}
				
			} catch( SQLException e )
			{
				e.printStackTrace();
				LogUtils.d("Debug", "insertDataToDB: " + e.toString());
			} finally
			{
				db.close();
			}
		} else
		{
			ToastUtils.showShort(BaseApplication.sAppContext, "内容为空,请输入内容");
			return false;
		}
		return true;
	}
	
	/**
	 * 通过ID查询进行更新
	 *
	 * @param note
	 * @return
	 */
	
	public static boolean updateDataToDB ( TravelNote note )
	{
		SQLiteDatabase db = helper.getWritableDatabase();
		
		try
		{
			db.execSQL(SQL_UPDATE_DATA
					, new String[]{ note.getTitle(), note.getAuthor(), note.getContent(), note.getUpdateTime(),
							note.getId() + "" });
			
			return true;
		} catch( SQLException e )
		{
			e.printStackTrace();
			LogUtils.d("Debug", "insertDataToDB: " + e.toString());
			return false;
		} finally
		{
			db.close();
		}
	}
	
	/**
	 * 赋入ID进行删除
	 *
	 * @param id
	 * @return
	 */
	public static boolean deleteDataToDB ( int id )
	{
		SQLiteDatabase db = helper.getWritableDatabase();
		
		try
		{
			db.execSQL(SQL_DELETE_DATA_USE_ID, new String[]{ id + "" });
			return true;
		} catch( SQLException e )
		{
			e.printStackTrace();
			LogUtils.d("Debug", "insertDataToDB: " + e.toString());
			return false;
		} finally
		{
			db.close();
		}
//		return false;
	}
	
	
}
		