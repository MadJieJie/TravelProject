package com.fengjie.myapplication.modules.city.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.fengjie.myapplication.modules.city.bean.City;
import com.fengjie.myapplication.modules.city.bean.CityIdAndProId;
import com.fengjie.myapplication.modules.city.bean.Province;
import com.fengjie.myapplication.utils.often.SharedPreferenceUtil;
import com.fengjie.myapplication.utils.often.LogUtils;
import com.fengjie.myapplication.utils.often.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MadJieJie on 2015/9/30 0030. 封装数据库操作
 */
public class CityDB
{


	private static final String TB_CITY = "T_City";
	private static final String TB_PROVINCE = "T_Province";


	private static final String COL_CITY_ID = "cityId";
	private static final String COL_PRO_ID = "provinceId";


//	private static final String QUERY_CITY_ID = "SELECT cityId,provinceId FROM T_City WHERE CityName = ?";
	private static final String QUERY_CITY_ID = "SELECT * FROM T_City WHERE CityName = ?";

	public CityDB ()
	{
		throw new UnsupportedOperationException("not supported Operation");
	}

	public static List< Province > loadProvinces ( SQLiteDatabase db )
	{

		List< Province > list = new ArrayList<>();

		Cursor cursor = db.query("T_Province", null, null, null, null, null, null);

		if ( cursor.moveToFirst() )
		{
			do
			{
				Province province = new Province();
				province.ProSort = cursor.getInt(cursor.getColumnIndex("ProSort"));
				province.ProName = cursor.getString(cursor.getColumnIndex("ProName"));
				list.add(province);
			} while ( cursor.moveToNext() );
		}
		Utils.closeQuietly(cursor);
		return list;
	}

	public static List< City > loadCities ( SQLiteDatabase db, int ProID )
	{
		List< City > list = new ArrayList<>();
		Cursor cursor = db.query(TB_CITY, null, "ProID = ?", new String[]{ String.valueOf(ProID) }, null, null, null);
		if ( cursor.moveToFirst() )
		{
			do
			{
				City city = new City();
				city.CityName = cursor.getString(cursor.getColumnIndex("CityName"));
				city.ProID = ProID;
				city.CitySort = cursor.getInt(cursor.getColumnIndex("CitySort"));

				city.cityId = cursor.getInt(cursor.getColumnIndex(COL_CITY_ID));
				city.provinceId = cursor.getInt(cursor.getColumnIndex(COL_PRO_ID));

				list.add(city);
			} while ( cursor.moveToNext() );
		}
		Utils.closeQuietly(cursor);
		return list;
	}

	/**
	 * 根据当前城市,查询城市数据库，获得CityID & ProID
	 *
	 * @param db DBManager.getInstance().getDataBase();
	 * @return CityIdAndProId
	 */
	public static CityIdAndProId getCityIdAndProIdObject ( SQLiteDatabase db )
	{
		Cursor cursor = db.rawQuery(QUERY_CITY_ID, new String[]{ SharedPreferenceUtil.getInstance().getCityName().concat("市") });
		LogUtils.d(SharedPreferenceUtil.getInstance().getCityName().concat("市"));
//		Cursor cursor = db.query(TB_CITY, null, "CityName = ?", new String[]{ SharedPreferenceUtil.getInstance().getCityName().concat("市") }, null, null, null);

		CityIdAndProId cityIdAndProId = new CityIdAndProId();
		if ( cursor.moveToFirst() )
		{
			do
			{
			cityIdAndProId = new CityIdAndProId();
			cityIdAndProId.setCityId(cursor.getInt(cursor.getColumnIndex(COL_CITY_ID)));
			cityIdAndProId.setProId(cursor.getInt(cursor.getColumnIndex(COL_PRO_ID)));
			} while ( cursor.moveToNext() );
		}
		Utils.closeQuietly(cursor);
		return cityIdAndProId;
	}

}
