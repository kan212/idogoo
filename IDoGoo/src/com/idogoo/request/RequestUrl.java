package com.idogoo.request;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import com.android.volley.Request;
import com.idogoo.app.IDoGooApp;
import com.idogoo.inter.OnProtocolTaskListener;
import com.idogoo.paser.BaseParser;
import com.idogoo.paser.SpeListParser;
import com.idogoo.utils.Config;
import com.idogoo.utils.IDoGooUtils;
import com.idogoo.utils.MD5;
import com.idogoo.utils.Variable;

public class RequestUrl {

	static {
		FakeX509TrustManager.allowAllSSL();
	}

	public static final String speListUrl = "http://www.idogoo.com/meet/api/specialistTopListByType?";// 专家列表的获取接口
	public static final String registerUrl = "http://www.idogoo.com/data/cltapi/userReg";// 用户注册接口
	public static final String userInfoUrl = "http://www.idogoo.com/data/cltapi/userInfoReg";// 注册用户提交个人基本信息
	public static final String loginUrl = "http://www.idogoo.com/data/cltapi/login";// 用户登录接口
	public static final String accessKeyUrl = "https://www.idogoo.com/data/cltapi/genAccessKey";// 生成client_ticket
	public static final String ttlAccessKeyUrl = "https://www.idogoo.com/data/cltapi/ttlAccessKey";// 获取client_ticket剩余有效期（单位：秒）
	public static final String regUidTokenUrl = "https://www.idogoo.com/data/cltapi/regUidToken";// 注册用户与设备ID

	public static final String AUTH_TOKEN = "ba1b8cbe47c2a4466a6fb690b3d2f1b9";

	public static final String getSpeListReqeust(int begin) {
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		pairs.add(new BasicNameValuePair("type", 99 + ""));
		pairs.add(new BasicNameValuePair("begin", begin + ""));
		pairs.add(new BasicNameValuePair("end", (begin + 4) + ""));
		pairs.add(new BasicNameValuePair("num", "20"));
		pairs.add(new BasicNameValuePair("client_ticket", Variable
				.getInstance().getAccess_key()));
		String url = IDoGooUtils.format(speListUrl, pairs, true);
		Config.i(url);
		return url;
	}

	/**
	 * 用户注册接口
	 * 
	 * @param user_name
	 *            用户名
	 * @param phone
	 *            手机
	 * @param pass
	 *            密码
	 * @param scope
	 *            校验值
	 * @return
	 */
	public static final String postRegisterBody(String user_name, String phone,
			String pass, String phone_captcha) {
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		pairs.add(new BasicNameValuePair("user_name", user_name));
		pairs.add(new BasicNameValuePair("phone", phone));
		pairs.add(new BasicNameValuePair("pass", pass));
		pairs.add(new BasicNameValuePair("phone_captcha", phone_captcha));
		pairs.add(new BasicNameValuePair("client_ticket", Variable
				.getInstance().getAccess_key()));
		return URLEncodedUtils.format(pairs, "utf-8");
	}

	/**
	 * 注册用户提交个人基本信息
	 * 
	 * @param sexy
	 *            性别
	 * @param birthday
	 *            生日
	 * @param city
	 *            城市
	 * @param pic
	 *            图片
	 * @param scope
	 *            校验值
	 * @return
	 */
	public static final String getUserInfoBody(String sexy, String birthday,
			String city, String pic) {
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		pairs.add(new BasicNameValuePair("sexy", sexy));
		pairs.add(new BasicNameValuePair("birthday", birthday));
		pairs.add(new BasicNameValuePair("city", city));
		pairs.add(new BasicNameValuePair("pic", pic));
		pairs.add(new BasicNameValuePair("client_ticket", Variable
				.getInstance().getAccess_key()));
		return URLEncodedUtils.format(pairs, "utf-8");
	}

	/**
	 * 用户登录接口
	 * 
	 * @param user_name
	 *            用户名
	 * @param pass
	 *            密码
	 * @param scope
	 *            校验值
	 * @return
	 */
	public static final String getLoginBody(String user_name, String pass) {
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		pairs.add(new BasicNameValuePair("user_name", user_name));
		pairs.add(new BasicNameValuePair("pass", pass));
		pairs.add(new BasicNameValuePair("client_ticket", Variable
				.getInstance().getAccess_key()));
		return URLEncodedUtils.format(pairs, "utf-8");
	}

	/**
	 * 生成client_ticket
	 * 
	 * @param auth_token
	 *            客户端分配的静态凭证
	 * @param device_token
	 *            设备ID
	 * @param version
	 *            当前版本
	 * @param os
	 *            操作系统
	 * @return
	 */
	public static final String getClientTicket() {
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		pairs.add(new BasicNameValuePair("auth_token", AUTH_TOKEN));
		pairs.add(new BasicNameValuePair("device_token", IDoGooUtils
				.getIMEI(IDoGooApp.getContext())));
		pairs.add(new BasicNameValuePair("version", "1.0.0"));
		pairs.add(new BasicNameValuePair("os", "android"));
		return URLEncodedUtils.format(pairs, "utf-8");
	}

	/**
	 * 获取client_ticket剩余有效期（单位：秒）
	 * 
	 * @param client_ticket
	 * @return
	 */
	public static final String getVolidTime(String client_ticket) {
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		pairs.add(new BasicNameValuePair("client_ticket", client_ticket));
		return URLEncodedUtils.format(pairs, "utf-8");
	}

	/**
	 * 注册用户与设备ID
	 * 
	 * @param client_ticket
	 *            Api系统接入key
	 * @param uid
	 *            用户ID
	 * @param device_token
	 *            设备ID
	 * @param os
	 *            操作系统
	 * @return
	 */
	public static final String getRegUidToken(String client_ticket, String uid,
			String device_token, String os) {
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		pairs.add(new BasicNameValuePair("client_ticket", client_ticket));
		pairs.add(new BasicNameValuePair("device_token", device_token));
		pairs.add(new BasicNameValuePair("uid", uid));
		pairs.add(new BasicNameValuePair("os", os));
		pairs.add(new BasicNameValuePair("client_ticket", Variable
				.getInstance().getAccess_key()));
		return URLEncodedUtils.format(pairs, "utf-8");
	}

	private static final String UrlExpertDetail = "http://www.idogoo.com/profile/cltapi/specialistDetial?";

	/**
	 * 获取专家详情
	 * 
	 * @param uid
	 * @return
	 */
	public static final String getExpertDetail(String uid) {
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		pairs.add(new BasicNameValuePair("uid", uid));
		pairs.add(new BasicNameValuePair("client_ticket", Variable
				.getInstance().getAccess_key()));
		String url = IDoGooUtils.format(UrlExpertDetail, pairs, true);
		Config.i(url);
		return url;
	}

	public static final String urlEvaluateList = "http://www.idogoo.com/profile/cltapi/topicEvaluateList?";

	public static final String getEvaluateList(String uid, int page,
			int pageSize) {
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		pairs.add(new BasicNameValuePair("uid", uid));
		pairs.add(new BasicNameValuePair("page", page + ""));
		pairs.add(new BasicNameValuePair("pageSize", pageSize + ""));
		pairs.add(new BasicNameValuePair("client_ticket", Variable
				.getInstance().getAccess_key()));
		String url = IDoGooUtils.format(urlEvaluateList, pairs, true);
		Config.i(url);
		return url;
	}

	public static final String UrlSmsCode = "http://www.idogoo.com/data/cltapi/sendCaptchaSms";

	/**
	 * 发送短信
	 * 
	 * @param phone
	 * @return
	 */
	public static final String getSmsCode(String phone) {
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		pairs.add(new BasicNameValuePair("phone", phone));
		pairs.add(new BasicNameValuePair("access_ticket", Variable
				.getInstance().getAccess_key()));
		String url = IDoGooUtils.format(UrlSmsCode, pairs, true);
		Config.i(url);
		return url;
	}

	public static final String UrlResetPwd = "https://www.idogoo.com/data/cltapi/changePassByPhone";

	/**
	 * 用户重新设置密码接口
	 * 
	 * @param phone
	 * @param captcha
	 *            短信校验码
	 * @param pass
	 * @return
	 */
	public static final String postResetPwd(String phone, String captcha,
			String pass) {
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		pairs.add(new BasicNameValuePair("phone", phone));
		pairs.add(new BasicNameValuePair("pass", pass));
		pairs.add(new BasicNameValuePair("captcha", captcha));
		pairs.add(new BasicNameValuePair("client_ticket", Variable
				.getInstance().getAccess_key()));
		return URLEncodedUtils.format(pairs, "utf-8");
	}

	public static final String UrlUserMeetList = "http://www.idogoo.com/profile/cltapi/userMeetList?";

	/**
	 * 获取用户约见列表
	 * 
	 * @param uid
	 * @return
	 */
	public static final String getAbout(String uid) {
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		pairs.add(new BasicNameValuePair("uid", uid));
		pairs.add(new BasicNameValuePair("client_ticket", Variable
				.getInstance().getAccess_key()));
		String url = IDoGooUtils.format(UrlUserMeetList, pairs, true);
		Config.i(url);
		return url;
	}

	public static final String UrlSpecialistOrderList = "http://www.idogoo.com/profile/cltapi/specialistOrderList?uid=14341894795241&page=1&pageSize=5";

	public static final String getSpecialistOrderList(String uid, int page,
			int pageSize) {
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		pairs.add(new BasicNameValuePair("uid", uid));
		pairs.add(new BasicNameValuePair("page", 1 + ""));
		pairs.add(new BasicNameValuePair("pageSize", 5 + ""));
		pairs.add(new BasicNameValuePair("client_ticket", Variable
				.getInstance().getAccess_key()));
		String url = IDoGooUtils.format(UrlUserMeetList, pairs, true);
		Config.i(url);
		return url;
	}

	public static final String UrlUserInfo = "http://www.idogoo.com/data/cltapi/getUserInfoById";

	/**
	 * 获取用户的详细信息
	 * 
	 * @param uid
	 * @return
	 */
	public static final String getUserInfo(String uid) {
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		pairs.add(new BasicNameValuePair("id", uid));
		pairs.add(new BasicNameValuePair("client_ticket", Variable
				.getInstance().getAccess_key()));
		String url = IDoGooUtils.format(UrlUserInfo, pairs, true);
		Config.i(url);
		return url;
	}

	public static final String UrlUserLogout = "http://www.idogoo.com/data/cltapi/logout";

	public static final String getUserLogout() {
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		pairs.add(new BasicNameValuePair("client_ticket", Variable
				.getInstance().getAccess_key()));
		String url = IDoGooUtils.format(UrlUserLogout, pairs, true);
		Config.i(url);
		return url;
	}

	public static final String UrlGetTopicById = "http://www.idogoo.com/meet/cltapi/getTopicById";

	/**
	 * 通过id获取话题信息
	 * 
	 * @param topicId
	 * @return
	 */
	public static final String getTopicById(String topicId) {
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		pairs.add(new BasicNameValuePair("id", topicId));
		pairs.add(new BasicNameValuePair("client_ticket", Variable
				.getInstance().getAccess_key()));
		String url = IDoGooUtils.format(UrlGetTopicById, pairs, true);
		Config.i(url);
		return url;
	}

	public static final String UrlPostEditOrder = "http://www.idogoo.com/meet/cltapi/editOrderMeetByUser";

	public static final String postEditOrder(String order_id,
			String requirements, String user_summary, String meet_time,
			String meet_location) {
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		pairs.add(new BasicNameValuePair("order_id", order_id));
		pairs.add(new BasicNameValuePair("requirements", requirements));
		pairs.add(new BasicNameValuePair("user_summary", user_summary));
		pairs.add(new BasicNameValuePair("meet_time", meet_time));
		pairs.add(new BasicNameValuePair("meet_location", meet_location));
		pairs.add(new BasicNameValuePair("client_ticket", Variable
				.getInstance().getAccess_key()));
		return URLEncodedUtils.format(pairs, "utf-8");
	}
	
	public static final String UrlGenOrderMeetByUser = "http://www.idogoo.com/meet/cltapi/genOrderMeetByUser";
	/**
	 * 
	 * @param order_id 
	 * 话题id
	 * @param spclt_id
	 * 专家id
	 * @param topic_title
	 * 话题名称
	 * @param price
	 * 价格
	 * @param requirements
	 * 用户需求
	 * @param user_summary
	 * 用户简介
	 * @param meet_time
	 * 约见时间
	 * @param meet_location
	 * 约见地点
	 * @return
	 */
	public static final String postGenOrderMeetByUser(String user_id,String topic_id,String spclt_id,String topic_title,String price,String requirements, String user_summary, String meet_time,
			String meet_location) {
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		pairs.add(new BasicNameValuePair("topic_id", topic_id));
		pairs.add(new BasicNameValuePair("spclt_id", spclt_id));
		pairs.add(new BasicNameValuePair("topic_title", topic_title));
		pairs.add(new BasicNameValuePair("mocc", MD5.EncoderByMD5(user_id + topic_id + spclt_id).toLowerCase()));
		pairs.add(new BasicNameValuePair("price", price));
		pairs.add(new BasicNameValuePair("requirements", requirements + "12"));
		pairs.add(new BasicNameValuePair("user_summary", user_summary+ "12"));
		pairs.add(new BasicNameValuePair("meet_time", meet_time+ "12"));
		pairs.add(new BasicNameValuePair("meet_location", meet_location+ "12"));
		pairs.add(new BasicNameValuePair("client_ticket", Variable
				.getInstance().getAccess_key()));
		Config.e(pairs.toString());
		return URLEncodedUtils.format(pairs, "utf-8");
	}

	public static final String UrlCheckUserExists = "http://www.idogoo.com/data/cltapi/checkUserExists";

	/**
	 * 检查用户是否存在
	 * 
	 * @param user_name
	 * @return
	 */
	public static final String checkUserExists(String user_name) {
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		pairs.add(new BasicNameValuePair("user_name", user_name));
		pairs.add(new BasicNameValuePair("client_ticket", Variable
				.getInstance().getAccess_key()));
		String url = IDoGooUtils.format(UrlCheckUserExists, pairs, true);
		Config.i(url);
		return url;
	}

	public static final String UrlCheckPhoneExists = "http://www.idogoo.com/data/cltapi/checkPhoneExists";

	/**
	 * 检查手机号码是否注册
	 * 
	 * @param phone
	 * @return
	 */
	public static final String checkPhoneExists(String phone) {
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		pairs.add(new BasicNameValuePair("phone", phone));
		pairs.add(new BasicNameValuePair("client_ticket", Variable
				.getInstance().getAccess_key()));
		String url = IDoGooUtils.format(UrlCheckPhoneExists, pairs, true);
		Config.i(url);
		return url;
	}

	public static final String UrlCheckPhoneCaptcha = "http://www.idogoo.com/data/cltapi/checkPhoneCaptcha";

	/**
	 * 短信验证码校验接口
	 * 
	 * @param phone
	 * @param captcha
	 * @return
	 */
	public static final String checkPhoneCaptcha(String phone, String captcha) {
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		pairs.add(new BasicNameValuePair("phone", phone));
		pairs.add(new BasicNameValuePair("captcha", captcha));

		pairs.add(new BasicNameValuePair("client_ticket", Variable
				.getInstance().getAccess_key()));
		String url = IDoGooUtils.format(UrlCheckPhoneExists, pairs, true);
		Config.i(url);
		return url;
	}

	public static final String UrlOrderInfoBySpecialist = "http://www.idogoo.com/meet/cltapi/orderInfoBySpecialist";

	/**
	 * 专家获取订单信息
	 * 
	 * @param id
	 *            订单id
	 * @param uid
	 *            用户id
	 * @return
	 */
	public static final String getOrderInfoBySpecialist(String id, String uid) {
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		pairs.add(new BasicNameValuePair("id", id));
		pairs.add(new BasicNameValuePair("uid", uid));
		pairs.add(new BasicNameValuePair("client_ticket", Variable
				.getInstance().getAccess_key()));
		String url = IDoGooUtils.format(UrlOrderInfoBySpecialist, pairs, true);
		Config.i(url);
		return url;
	}

	public static final String UrlOrderInfoByUser = "http://www.idogoo.com/meet/cltapi/orderInfoByUser";

	/**
	 * 用户获取订单信息
	 * 
	 * @param id
	 *            订单id
	 * @param uid
	 *            用户id
	 * @return
	 */
	public static final String getOrderInfoByUser(String id, String uid) {
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		pairs.add(new BasicNameValuePair("id", id));
		pairs.add(new BasicNameValuePair("uid", uid));
		pairs.add(new BasicNameValuePair("client_ticket", Variable
				.getInstance().getAccess_key()));
		String url = IDoGooUtils.format(UrlOrderInfoByUser, pairs, true);
		Config.i(url);
		return url;
	}

	public static final String UrlCancelOrderMeetByUser = "http://www.idogoo.com/meet/cltapi/cancelOrderMeetByUser";

	/**
	 * 取消订单
	 * 
	 * @param order_id
	 *            订单id
	 * @param reason
	 *            原因
	 * @return
	 */
	public static final String postCancelOrderMeetByUser(String order_id,
			String reason) {
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		pairs.add(new BasicNameValuePair("order_id", order_id));
		pairs.add(new BasicNameValuePair("reason", reason));
		pairs.add(new BasicNameValuePair("client_ticket", Variable
				.getInstance().getAccess_key()));
		return URLEncodedUtils.format(pairs, "utf-8");
	}

	public static final String urlGetUpToken = "https://www.idogoo.com/data/cltapi/getUpToken";

	public static final String getUpToken() {
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		pairs.add(new BasicNameValuePair("client_ticket", Variable
				.getInstance().getAccess_key()));
		String url = IDoGooUtils.format(urlGetUpToken, pairs, true);
		Config.i(url);
		return url;
	}

}
