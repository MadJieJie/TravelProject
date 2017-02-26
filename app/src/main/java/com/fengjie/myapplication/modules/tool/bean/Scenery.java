package com.fengjie.myapplication.modules.tool.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * @author Created by MadJieJie on 2017/2/22-15:27.
 * @brief
 * @attention
 */

public class Scenery implements Serializable
{


	/**
	 * error_code : 0
	 * reason : 成功
	 * result : [{"title":"西塘古镇",
	 * "grade":"AAAA",
	 * "price_min":"80",
	 * "comm_cnt":null,
	 * "cityId":"385",
	 * "address":"浙江省嘉兴市嘉善县西塘镇南苑路258号",
	 * "sid":"1951",
	 * "url":"http://www.ly.com/scenery/BookSceneryTicket_1951.html",
	 * "imgurl":"http://pic4.40017.cn/scenery/destination/2016/07/24/18/4FNRaw_240x135_00.jpg"},
	 * {"title":"乌镇景区","grade":"AAAAA","price_min":"100","comm_cnt":null,"cityId":"385","address":"浙江省嘉兴桐乡市乌镇石佛南路18号","sid":"1944","url":"http://www.ly.com/scenery/BookSceneryTicket_1944.html","imgurl":"http://pic4.40017.cn/scenery/destination/2016/07/21/16/jC0frS_240x135_00.jpg"},{"title":"嘉兴南北湖","grade":"AAAA","price_min":"70","comm_cnt":null,"cityId":"385","address":"浙江省嘉兴市南北湖杭州湾北岸海盐县境内","sid":"1964","url":"http://www.ly.com/scenery/BookSceneryTicket_1964.html","imgurl":"http://pic3.40017.cn/scenery/destination/2015/06/10/14/hVAcj2_240x135_00.jpg"},{"title":"南湖风景名胜区","grade":"AAAAA","price_min":"55","comm_cnt":null,"cityId":"385","address":"浙江省嘉兴市城南嘉兴市海盐塘路与南溪路交叉口。邮编：321000","sid":"1935","url":"http://www.ly.com/scenery/BookSceneryTicket_1935.html","imgurl":"http://pic4.40017.cn/scenery/destination/2016/07/22/16/nVJSzh_240x135_00.jpg"},{"title":"梅花洲景区","grade":"AAAA","price_min":"45","comm_cnt":null,"cityId":"385","address":"浙江省嘉兴市南湖区凤桥镇三星路10号","sid":"28175","url":"http://www.ly.com/scenery/BookSceneryTicket_28175.html","imgurl":"http://pic3.40017.cn/scenery/destination/2015/08/06/19/XDhevu_240x135_00.jpg"},{"title":"嘉兴清池温泉","grade":"","price_min":"138","comm_cnt":null,"cityId":"385","address":"浙江嘉兴市秀洲区嘉湖公路与濮新线路口","sid":"48636","url":"http://www.ly.com/scenery/BookSceneryTicket_48636.html","imgurl":"http://pic4.40017.cn/scenery/destination/2016/09/21/20/yRouMw_240x135_00.jpg"},{"title":"盐官观潮景区","grade":"","price_min":"65","comm_cnt":null,"cityId":"385","address":"浙江省嘉兴市海宁市盐官镇","sid":"21052","url":"http://www.ly.com/scenery/BookSceneryTicket_21052.html","imgurl":"http://pic3.40017.cn/scenery/destination/2015/04/18/04/nCj0rx_240x135_00.jpg"},{"title":"歌斐颂巧克力小镇","grade":"","price_min":"55","comm_cnt":null,"cityId":"385","address":"浙江省嘉善县大云镇巧克力大道1号","sid":"183197","url":"http://www.ly.com/scenery/BookSceneryTicket_183197.html","imgurl":"http://pic4.40017.cn/scenery/destination/2016/07/23/23/nu52uU_240x135_00.jpg"},{"title":"假日牧场度假乐园","grade":"","price_min":"28","comm_cnt":null,"cityId":"385","address":"浙江省海宁市镇保路1号（S101省道与嘉海公路交叉口）","sid":"191262","url":"http://www.ly.com/scenery/BookSceneryTicket_191262.html","imgurl":"http://pic3.40017.cn/scenery/destination/2015/08/12/19/EB6h89_240x135_00.jpg"},{"title":"绮园景区","grade":"AAAA","price_min":"35","comm_cnt":null,"cityId":"385","address":"浙江省嘉兴市海盐县武原街道海滨东路37号","sid":"1979","url":"http://www.ly.com/scenery/BookSceneryTicket_1979.html","imgurl":"http://pic3.40017.cn/scenery/destination/2015/04/18/00/RK5615_240x135_00.jpg"}]
	 */

	@SerializedName ( "error_code" )
	public int errorCode;
	@SerializedName ( "reason" )
	public String reason;
	@SerializedName ( "result" )
	public List< ResultBean > result;

	public static class ResultBean
	{
		/**
		 * title : 西塘古镇
		 * grade : AAAA
		 * price_min : 80
		 * comm_cnt : null
		 * cityId : 385
		 * address : 浙江省嘉兴市嘉善县西塘镇南苑路258号
		 * sid :1951
		 * url : http://www.ly.com/scenery/BookSceneryTicket_1951.html imgurl :
		 * http://pic4.40017.cn/scenery/destination/2016/07/24/18/4FNRaw_240x135_00.jpg
		 */

		@SerializedName ( "title" )
		public String title;
		@SerializedName ( "grade" )
		public String grade;
		@SerializedName ( "price_min" )
		public String priceMin;
		@SerializedName ( "comm_cnt" )
		public Object commCnt;
		@SerializedName ( "cityId" )
		public String cityId;
		@SerializedName ( "address" )
		public String address;
		@SerializedName ( "sid" )
		public String sid;
		@SerializedName ( "url" )
		public String url;
		@SerializedName ( "imgurl" )
		public String imgurl;
	}
}
