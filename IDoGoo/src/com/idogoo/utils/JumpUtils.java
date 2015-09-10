package com.idogoo.utils;

import android.content.Context;
import android.content.Intent;

import com.alipay.PayActivity;
import com.idogoo.app.MyInfoActivity;
import com.idogoo.app.SubActivity;
import com.idogoo.fragment.AboutFragment;
import com.idogoo.fragment.AppointFragment;
import com.idogoo.fragment.BaseWebFragment;
import com.idogoo.fragment.CancelOrderFragment;
import com.idogoo.fragment.ExpertDetailFragment;
import com.idogoo.fragment.ExpertSetFragment;
import com.idogoo.fragment.LoginFragment;
import com.idogoo.fragment.MessageFragment;
import com.idogoo.fragment.OrderDetailFragment;
import com.idogoo.fragment.PayFragment;
import com.idogoo.fragment.RegisterFragment;
import com.idogoo.fragment.ResetPwdFragment;
import com.idogoo.fragment.WaitAppointFragment;

/**
 * 
 * @author kan
 * 
 *         跳转类
 * 
 */

public class JumpUtils {

	private static Intent getIntentSub(Context context, Class<?> cls,
			String title) {
		Intent intent = new Intent(context, SubActivity.class);
		intent.putExtra(Constant.EXTRA_TITLE, title);
		intent.putExtra(Constant.EXTRA_FRAGMENT_TYPE, cls.getName());
		return intent;
	}

	public static void startLogin(Context context) {
		Intent intent = getIntentSub(context, LoginFragment.class, "登录");
		intent.putExtra(Constant.EXTRA_HAS_TITLE, false);
		context.startActivity(intent);
	}

	public static void startRegister(Context context) {
		Intent intent = getIntentSub(context, RegisterFragment.class, "注册");
		context.startActivity(intent);
	}

	public static void startAbout(Context context, String title, String id,
			String utype) {
		Intent intent = getIntentSub(context, AboutFragment.class, title);
		intent.putExtra(Constant.KEY_USER_ID, id);
		intent.putExtra(Constant.KEY_USER_UTYPE, utype);
		context.startActivity(intent);
	}

	public static void startMessage(Context context) {
		Intent intent = getIntentSub(context, MessageFragment.class, "我的消息");
		context.startActivity(intent);
	}

	public static void startExpertDetail(Context context, String uid) {
		Intent intent = getIntentSub(context, ExpertDetailFragment.class,
				"专家详情");
		intent.putExtra(Constant.KEY_USER_ID, uid);
		context.startActivity(intent);
	}

	public static void startRestPwd(Context context) {
		Intent intent = getIntentSub(context, ResetPwdFragment.class, "修改密码");
		context.startActivity(intent);
	}

	public static void startAppoint(Context context, String topicId) {
		Intent intent = getIntentSub(context, AppointFragment.class, "约见");
		intent.putExtra(Constant.KEY_TOPIC_ID, topicId);
		context.startActivity(intent);
	}

	public static void startExpertSet(Context context) {
		Intent intent = getIntentSub(context, ExpertSetFragment.class, "设置时间地点");
		context.startActivity(intent);
	}

	public static void startWaitAppoint(Context context) {
		Intent intent = getIntentSub(context, WaitAppointFragment.class, "等待约见");
		context.startActivity(intent);
	}

	/**
	 * 支付页面
	 * @param context
	 * @param json
	 */
	public static void startPayFragment(Context context, String json) {
		Intent intent = new Intent(context, PayActivity.class);
		intent.putExtra(Constant.EXTRA_JSON, json);
		intent.putExtra(Constant.EXTRA_TITLE, "支付");
		context.startActivity(intent);
	}

	/**
	 * 订单详情
	 * 
	 * @param context
	 * @param title
	 * @param id
	 *            用户或者专家id
	 * @param utype
	 *            用户类型
	 * @param order_id
	 *            订单id
	 */
	public static void startOrderDetailFragment(Context context, String title,
			String id, String utype, String order_id) {
		Intent intent = getIntentSub(context, OrderDetailFragment.class, title);
		intent.putExtra(Constant.KEY_USER_ID, id);
		intent.putExtra(Constant.KEY_USER_UTYPE, utype);
		intent.putExtra(Constant.KEY_ORDER_ID, order_id);
		context.startActivity(intent);
	}
	
	public static void startCancelOrderFragment(Context context,String order_id, String json) {
		Intent intent = getIntentSub(context, CancelOrderFragment.class, "取消约见");
		intent.putExtra(Constant.KEY_ORDER_ID, order_id);
		intent.putExtra(Constant.EXTRA_JSON, json);
		context.startActivity(intent);
	}

	/**
	 * 跳转到基本的网页
	 * @param context
	 * @param url
	 * @param title
	 */
	public static void startBaseWebFragment(Context context,String url,String title) {
		Intent intent = getIntentSub(context, BaseWebFragment.class, title);
		intent.putExtra(Constant.EXTRA_URL, url);
		context.startActivity(intent);
	}
	
	public static void startUserInfo(Context context, String uid) {
		Intent intent = new Intent(context, MyInfoActivity.class);
		intent.putExtra(Constant.KEY_USER_ID, uid);
		context.startActivity(intent);
	}

}
