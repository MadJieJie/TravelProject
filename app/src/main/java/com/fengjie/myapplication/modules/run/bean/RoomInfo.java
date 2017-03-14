package com.fengjie.myapplication.modules.run.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * @author Created by MadJieJie on 2017/3/5-9:33.
 * @brief
 * @attention
 */

public class RoomInfo implements Serializable
{
	
	/**
	 * error_code : 0
	 * reason : 成功
	 * result : [{"roomName":"【含早】标准房＋免费接机服务","area":"","bed":"","policyInfo":[{"policyName":"","price":"142","rebate":"0","breakfast":"单份        ","giftsDescription":""}]},{"roomName":"【含早】标准房＋门票","area":"","bed":"","policyInfo":[{"policyName":"[含早]桂林漓江大瀑布饭店(标准房＋免费接机服务)","price":"470","rebate":"27","breakfast":"无         ","giftsDescription":""}]},{"roomName":"【含早】标准房+环城水系游","area":"","bed":"","policyInfo":[{"policyName":"桂林漓江大瀑布饭店(标准房)","price":"549","rebate":"32","breakfast":"无         ","giftsDescription":""}]},{"roomName":"【含早】标准房+芦笛岩","area":"","bed":"","policyInfo":[{"policyName":"桂林漓江大瀑布饭店([含早]标准房+环城水系游)","price":"550","rebate":"32","breakfast":"无         ","giftsDescription":""}]},{"roomName":"【含早】标准房＋龙脊梯1日游","area":"","bed":"","policyInfo":[{"policyName":"桂林漓江大瀑布饭店([含早]标准房+芦笛岩)","price":"580","rebate":"33","breakfast":"无         ","giftsDescription":""}]},{"roomName":"标准房＋大漓江豪华游","area":"","bed":"","policyInfo":[{"policyName":"桂林漓江大瀑布饭店([含早]标准房＋龙脊梯1日游)","price":"630","rebate":"36","breakfast":"无         ","giftsDescription":""}]},{"roomName":"江景双床间","area":"30","bed":"双床","policyInfo":[{"policyName":"桂林漓江大瀑布饭店(标准房＋大漓江豪华游)","price":"700","rebate":"40","breakfast":"无         ","giftsDescription":""}]},{"roomName":"江景单间","area":"30","bed":"大床","policyInfo":[{"policyName":"","price":"1798","rebate":"0","breakfast":"无         ","giftsDescription":"赠送欢迎饮料一杯\n赠送价值50元的早茶券\n08:00-24:00提供免费接机服务（国庆、春节假期除外），请提前1天致电酒店前台0773-2822881预约（提前6小时取消）"},{"policyName":"(预付专享-无早)","price":"650","rebate":"0","breakfast":"无         ","giftsDescription":""}]},{"roomName":"行政大床房","area":"40","bed":"大床","policyInfo":[{"policyName":"","price":"1798","rebate":"72","breakfast":"无         ","giftsDescription":"赠送欢迎饮料一杯\n赠送价值50元的早茶券\n08:00-24:00提供免费接机服务（国庆、春节假期除外），请提前1天致电酒店前台0773-2822881预约（提前6小时取消）"},{"policyName":"(预付专享-无早)","price":"650","rebate":"0","breakfast":"无         ","giftsDescription":""}]},{"roomName":"行政双床间","area":"32","bed":"双床","policyInfo":[{"policyName":"(含早)","price":"1920","rebate":"78","breakfast":"双份        ","giftsDescription":"赠送欢迎饮料一杯\n赠送价值50元的早茶券\n08:00-24:00提供免费接机服务（国庆、春节假期除外），请提前1天致电酒店前台0773-2822881预约（提前6小时取消）"}]},{"roomName":"高级大床房","area":"30","bed":"大床","policyInfo":[{"policyName":"","price":"1920","rebate":"78","breakfast":"双份        ","giftsDescription":"赠送欢迎饮料一杯\n赠送价值50元的早茶券\n08:00-24:00提供免费接机服务（国庆、春节假期除外），请提前1天致电酒店前台0773-2822881预约（提前6小时取消）"}]},{"roomName":"高级双床间","area":"30","bed":"双床","policyInfo":[{"policyName":"不含早餐","price":"445","rebate":"0","breakfast":"无         ","giftsDescription":""},{"policyName":"(预付专享-无早)","price":"540","rebate":"0","breakfast":"无         ","giftsDescription":""}]}]
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
		 * roomName : 【含早】标准房＋免费接机服务
		 * area :
		 * bed :
		 * policyInfo : [{"policyName":"","price":"142","rebate":"0","breakfast":"单份        ","giftsDescription":""}]
		 */
		
		@SerializedName ( "roomName" )
		public String roomName;
		@SerializedName ( "area" )
		public String area;
		@SerializedName ( "bed" )
		public String bed;
		@SerializedName ( "policyInfo" )
		public List< PolicyInfoBean > policyInfo;
		
		public static class PolicyInfoBean
		{
			/**
			 * policyName :
			 * price : 142
			 * rebate : 0
			 * breakfast : 单份
			 * giftsDescription :
			 */
			
			@SerializedName ( "policyName" )
			public String policyName;
			@SerializedName ( "price" )
			public String price;
			@SerializedName ( "rebate" )
			public String rebate;
			@SerializedName ( "breakfast" )
			public String breakfast;
			@SerializedName ( "giftsDescription" )
			public String giftsDescription;
		}
	}
}
