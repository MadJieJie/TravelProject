package com.fengjie.myapplication.modules.run.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * @author Created by MadJieJie on 2017/2/22-15:27.
 * @brief
 * @attention
 */

public class Hotel implements Serializable
{
	
	
	/**
	 * error_code : 0
	 * reason : 成功
	 * result : [{"id":"2041","name":"桂林漓江大瀑布饭店","className":"五星级","intro":"桂林漓江大瀑布饭店地处桂林市中心的繁华地段，东临秀丽的漓江，正对碧波荡漾的杉湖，南邻象山公园，北望独秀峰、叠彩山，环境怡人。","dpNum":"369","Lat":"25.27908287","Lon":"110.30280419","address":"杉湖北路1号","largePic":"http://pic2.40017.cn/hotel/2014/10/28/15/25/G/851a8ed1078641a4a8321c2a6daaa1dd_400x300_01.jpg","cityId":"102","url":"http://www.ly.com/HotelInfo-2041.html","manyidu":"93%"},{"id":"400288","name":"维也纳酒店（桂林象山公园店）","className":"高档型","intro":"[维也纳酒店桂林象山公园]店坐落于两江四湖景区美丽的杉湖畔，毗邻正阳路步行街，与桂林的城徽\u201d象鼻山\u201d仅数步之遥，秉承维也纳独有的绅士般品味，淑女般亲切，精心打造83间具有不同观景视角的...","dpNum":"81","Lat":"25.27653221","Lon":"110.30266391","address":"滨江路11号","largePic":"http://pic2.40017.cn/hotel/2016/9/27/10/57/W/90607476422c4732b4f342d14b773930_400x300_01.jpg","cityId":"102","url":"http://www.ly.com/HotelInfo-400288.html","manyidu":"99%"},{"id":"82609","name":"金皇国际大酒店（桂林火车站店）","className":"高档型","intro":"金皇国际大酒店（桂林火车站店）位于桂林市中山南路46号，于2013年7月开业。","dpNum":"286","Lat":"25.26319281","Lon":"110.29057619","address":"中山南路46号","largePic":"http://pic2.40017.cn/hotel/2015/12/7/20/23/A/f303c1db6e584489be1f0a2324574c93_400x300_01.jpg","cityId":"102","url":"http://www.ly.com/HotelInfo-82609.html","manyidu":"98%"},{"id":"75443","name":"桂林唯美四季酒店（总店）","className":"舒适型","intro":"桂林唯美四季酒店（总店）地处风景如画的桂林市中心，距离汽车站和火车站仅百米之遥，周边有桂林大型的集购物、文化为一体的商业广场。","dpNum":"123","Lat":"25.26828589","Lon":"110.28746633","address":"环城西二路8号","largePic":"http://pic2.40017.cn/hotel/2014/8/9/a133bec6399847098209c70b23180dbd_400x300_01.jpg","cityId":"102","url":"http://www.ly.com/HotelInfo-75443.html","manyidu":"99%"},{"id":"147306","name":"桂林宾馆","className":"四星级","intro":"桂林宾馆位于桂林市区中心两江四湖景内，周围古树名木，叠石碑刻，亭台碧水，宛如一幅美丽的画卷。","dpNum":"283","Lat":"25.28015792","Lon":"110.29273937","address":"榕湖南路14号","largePic":"http://pic2.40017.cn/hotel/2016/4/18/8/8/L/b5b0d27688b84c26ad57177f06cb3944_400x300_01.jpg","cityId":"102","url":"http://www.ly.com/HotelInfo-147306.html","manyidu":"96%"},{"id":"638421","name":"维也纳酒店（桂林高铁北站店）","className":"高档型","intro":"维也纳酒店（桂林高铁北站店）位于桂林市高铁北站店正对面，地理位置优越，比邻漓江，周边景点有虞山公园、两江四湖景区，离沃尔玛商场仅3.5公里，离市中心6.6公里，让您购物旅游两不误。","dpNum":"57","Lat":"25.33533507","Lon":"110.3135434","address":"北辰路72号2栋新世纪汽配城","largePic":"http://pic2.40017.cn/hotel/2016/8/21/13/56/D/b4fdd4cb9f944031b94e8d07bdd4328e_400x300_01.jpg","cityId":"102","url":"http://www.ly.com/HotelInfo-638421.html","manyidu":"98%"},{"id":"505393","name":"桂林威航大酒店","className":"二星级","intro":"桂林威航大酒店位于安新洲民航大厦对面（前民航酒店南楼），属于庭院式酒店，有多种类型客房，装修豪华舒适，通风采光好，配套设施齐全，满足不同客人的需要。","dpNum":"57","Lat":"25.26524728","Lon":"110.30025721","address":"安新南路1号","largePic":"http://pic2.40017.cn/hotel/2016/9/18/14/37/B/29dda3547db94b4cba4b46f8b22abfec_400x300_01.jpg","cityId":"102","url":"http://www.ly.com/HotelInfo-505393.html","manyidu":"91%"},{"id":"2787","name":"贵客0773酒店（桂林象山景区店）","className":"三星级","intro":"贵客0773酒店（桂林象山景区店）以其便捷的交通、周到的服务、舒适、典雅、安全、卫生的环境，给人一种宾至如归的感觉。","dpNum":"703","Lat":"25.27530591","Lon":"110.29981315","address":"文明路31号","largePic":"http://pic2.40017.cn/hotel/2015/9/12/22/47/M/e8ff10d0e59a4e8d8c8519e52b7932c0_400x300_01.jpg","cityId":"102","url":"http://www.ly.com/HotelInfo-2787.html","manyidu":"93%"},{"id":"8263","name":"维也纳国际酒店（桂林中山店）","className":"豪华型","intro":"维也纳国际酒店（桂林中山店）地处城市中心地段，3分钟路程内便有公交站点可通达市内各处，步行10分钟即可抵达汽车站、火车站。","dpNum":"177","Lat":"25.27600546","Lon":"110.29518754","address":"中山中路3号","largePic":"http://pic2.40017.cn/hotel/2016/7/9/1/24/E/8a304de0a1944642a3b30fd4d15b1ae5_400x300_01.jpg","cityId":"102","url":"http://www.ly.com/HotelInfo-8263.html","manyidu":"92%"},{"id":"146938","name":"汉庭酒店（桂林伏波山公园店）","className":"经济型","intro":"汉庭酒店桂林伏波山公园店座落于风景秀丽的漓江河畔，位于桂林市 的旅游风景区伏波山、靖江王府之间，毗邻象鼻山公园、叠彩山。","dpNum":"143","Lat":"25.28765715","Lon":"110.30904369","address":"滨江路伏和巷1号","largePic":"http://pic2.40017.cn/hotel/2015/8/16/7/7/I/a3a98eb1fc7d48128424045f7630af19_400x300_01.jpg","cityId":"102","url":"http://www.ly.com/HotelInfo-146938.html","manyidu":"97%"},{"id":"14567","name":"桂林阳光王朝酒店（原中脉道和国际酒店）","className":"舒适型","intro":"桂林阳光王朝酒店（原中脉道和国际酒店）位于享有\u201c山水甲天下\u201d之称的桂林市中心，距离漓江仅百米，西门及正阳步行街（桂林市 热闹的商业区）。","dpNum":"73","Lat":"25.2792177","Lon":"110.30465583","address":"正阳路步行街2号","largePic":"http://pic2.40017.cn/hotel/2016/1/27/23/30/A/4b4977e0a1ee44e884c6ec31d22e59d9_400x300_01.jpg","cityId":"102","url":"http://www.ly.com/HotelInfo-14567.html","manyidu":"92%"},{"id":"23038","name":"桂林百悦酒店","className":"高档型","intro":"桂林百悦酒店位于金鸡路，临近七星高速路口；酒店至磨盘山码头约20分钟，可乘坐游船至阳朔。","dpNum":"29","Lat":"25.28685891","Lon":"110.34163789","address":"金鸡路1号","largePic":"http://pic2.40017.cn/hotel/2014/8/9/d91fed4bde944ff08f639959f0d013a6_400x300_01.jpg","cityId":"102","url":"http://www.ly.com/HotelInfo-23038.html","manyidu":"97%"},{"id":"25902","name":"维也纳酒店（桂林上海路店）","className":"舒适型","intro":"维也纳酒店（桂林上海路店）隶属深圳维也纳酒店管理集团旗下的精品商务连锁酒店之一，酒店是按照欧陆新古典主义风格打造的酒店。","dpNum":"186","Lat":"25.266354","Lon":"110.301727","address":"上海路安新小区138栋","largePic":"http://pic2.40017.cn/hotel/2016/8/23/14/5/C/8df005a0f23f463a8fbf58963ea0b1e8_400x300_01.jpg","cityId":"102","url":"http://www.ly.com/HotelInfo-25902.html","manyidu":"88%"},{"id":"55726","name":"桂林桂山华星酒店","className":"豪华型","intro":"桂山华星酒店坐落于\u201c山水甲天下\u201d的桂林漓江之畔，与城徽象鼻山隔江相望，紧邻七星公园、訾洲公园，距市中心、火车站、高新技术产业园、国际会展中心仅10分钟车程，距桂林两江国际机场45分钟车...","dpNum":"50","Lat":"25.27493641","Lon":"110.30905756","address":"穿山路42号","largePic":"http://pic2.40017.cn/hotel/2016/3/12/3/46/E/5e766f4e17f7468092ffb1299c8b89cd_400x300_01.jpg","cityId":"102","url":"http://www.ly.com/HotelInfo-55726.html","manyidu":"92%"},{"id":"25978","name":"桂林蓝宝石酒店","className":"高档型","intro":"桂林蓝宝石酒店地处桂林市中心繁华的西城路步行街上，近象山景区、榕景区、旅游购物街、美食街，离桃花江、榕湖、象鼻山只有几步之遥。","dpNum":"427","Lat":"25.27727096","Lon":"110.29560017","address":"西城路步行街9号","largePic":"http://pic2.40017.cn/hotel/2016/7/8/21/36/X/e1942684ed1e4f1c824e5edf5a5d90b2_400x300_01.jpg","cityId":"102","url":"http://www.ly.com/HotelInfo-25978.html","manyidu":"93%"},{"id":"2911","name":"桂林民航大厦","className":"三星级","intro":"桂林民航大厦位于漓江与桃花江交汇处，坐拥繁华地段，步行10分钟象鼻山、穿山、塔山等景点。","dpNum":"484","Lat":"25.26661946","Lon":"110.30029993","address":"上海路18号","largePic":"http://pic2.40017.cn/hotel/2014/8/10/fbc1ee3b513a49018b749f9730742a3b_400x300_01.jpg","cityId":"102","url":"http://www.ly.com/HotelInfo-2911.html","manyidu":"87%"},{"id":"93037","name":"金水湾国际大酒店（桂林高铁北站店）","className":"高档型","intro":"金水湾国际大酒店（桂林高铁北站店）地处八里街经济开发区繁华商务地带，临近桂林火车始发站，交通便利。","dpNum":"44","Lat":"25.34918431","Lon":"110.3183686","address":"八里街八里4路","largePic":"http://pic2.40017.cn/hotel/2014/8/13/15/37/S/752f708091d24d0da911bd3d8ca1c3f5_400x300_01.jpg","cityId":"102","url":"http://www.ly.com/HotelInfo-93037.html","manyidu":"95%"},{"id":"176472","name":"格林联盟（桂林象鼻山景区店）（原亚昇大酒店）","className":"经济型","intro":"格林联盟（桂林象鼻山景区店）位于桂林汽车总站斜对面，毗邻桂林市城徽\u2014\u2014象鼻山公园，北邻桂林闻名的两江四湖景区。","dpNum":"71","Lat":"25.273211","Lon":"110.293053","address":"中山南路104号","largePic":"http://pic2.40017.cn/hotel/2016/9/18/23/11/G/45145e0b80384c37b842c6a845ae8b44_400x300_01.jpg","cityId":"102","url":"http://www.ly.com/HotelInfo-176472.html","manyidu":"90%"},{"id":"63540","name":"佰宫精品酒店（桂林店）","className":"高档型","intro":"佰宫精品连锁酒店桂林店位于桂林市中山北路 ，在桂林较为繁华的北极广场商圈。","dpNum":"70","Lat":"25.30874771","Lon":"110.30625899","address":"中山北路97号(沃尔玛旁)","largePic":"http://pic2.40017.cn/hotel/2014/8/9/ffd3e498e4d449998656f2d1078a97db_400x300_01.jpg","cityId":"102","url":"http://www.ly.com/HotelInfo-63540.html","manyidu":"94%"},{"id":"313313","name":"维也纳酒店（桂林市府店）","className":"高档型","intro":"维也纳酒店（桂林市府店）坐落于桂林市临桂区中心公园新两江四湖边上，拥有美丽的湖景后院。","dpNum":"43","Lat":"25.24897022","Lon":"110.18465437","address":"西城大道新市政府旁","largePic":"http://pic2.40017.cn/hotel/2016/7/30/22/9/X/62b2ddbdcd7d47a196d2442a191783fc_400x300_01.jpg","cityId":"102","url":"http://www.ly.com/HotelInfo-313313.html","manyidu":"100%"}]
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
		 * id : 2041
		 * name : 桂林漓江大瀑布饭店
		 * className : 五星级
		 * intro : 桂林漓江大瀑布饭店地处桂林市中心的繁华地段，东临秀丽的漓江，正对碧波荡漾的杉湖，南邻象山公园，北望独秀峰、叠彩山，环境怡人。
		 * dpNum : 369
		 * Lat : 25.27908287
		 * Lon : 110.30280419
		 * address : 杉湖北路1号
		 * largePic : http://pic2.40017.cn/hotel/2014/10/28/15/25/G/851a8ed1078641a4a8321c2a6daaa1dd_400x300_01.jpg
		 * cityId : 102
		 * url : http://www.ly.com/HotelInfo-2041.html
		 * manyidu : 93%
		 */
		
		@SerializedName ( "id" )
		public String id;
		@SerializedName ( "name" )
		public String name;
		@SerializedName ( "className" )
		public String className;
		@SerializedName ( "intro" )
		public String intro;
		@SerializedName ( "dpNum" )
		public String dpNum;
		@SerializedName ( "Lat" )
		public String Lat;
		@SerializedName ( "Lon" )
		public String Lon;
		@SerializedName ( "address" )
		public String address;
		@SerializedName ( "largePic" )
		public String largePic;
		@SerializedName ( "cityId" )
		public String cityId;
		@SerializedName ( "url" )
		public String url;
		@SerializedName ( "manyidu" )
		public String manyidu;
	}
}
