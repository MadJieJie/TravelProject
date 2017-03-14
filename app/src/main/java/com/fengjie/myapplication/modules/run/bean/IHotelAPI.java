package com.fengjie.myapplication.modules.run.bean;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


/**
 * @author Created by MadJieJie on 2017/2/23-16:00.
 * @brief
 * @attention
 */

public interface IHotelAPI
{
	String HOST = "http://apis.haoservice.com/lifeservice/travel/";

	@GET ( "HotelList" )
	Call< Hotel > GetHotel ( @Query ( "key" ) String key, @Query ( "cityid" ) int cityid, @Query ( "page" ) int page, @Query ( "paybyvas" ) boolean paybyvas );

}
