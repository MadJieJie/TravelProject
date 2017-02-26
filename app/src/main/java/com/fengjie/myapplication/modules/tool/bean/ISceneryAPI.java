package com.fengjie.myapplication.modules.tool.bean;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


/**
 * @author Created by MadJieJie on 2017/2/23-16:00.
 * @brief
 * @attention
 */

public interface ISceneryAPI
{
	String HOST = "http://apis.haoservice.com/lifeservice/travel/";

	@GET ( "scenery" )
	Call< Scenery > GetScenery ( @Query ( "key" ) String key, @Query ( "pid" ) int pid, @Query ( "cid" ) int cid, @Query ( "page" ) int page, @Query ( "paybyvas" ) boolean paybyvas );

}
