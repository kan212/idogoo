package com.idogoo.request;

import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.idogoo.app.IDoGooApp;
import com.idogoo.inter.OnProtocolTaskListener;
import com.idogoo.model.AccessKeyModel;
import com.idogoo.paser.AccessKeyParser;
import com.idogoo.paser.BaseParser;
import com.idogoo.utils.Config;
import com.idogoo.utils.Variable;

/**
 * 
 * ClassName: HttpUtil <br/>
 * Function:  Http请求封装
 * date: 2015-3-23 下午3:09:53 <br/>
 *
 * @author xiaojian1
 * @version
 */
public class HttpUtil {

	/**
	 * 
	 * addRequest:加到同一个RequestQueue中。
	 *
	 * @author xiaojian
	 * @param request
	 */
	public static void addRequest(Request<BaseParser> request) {
		if (request == null) {
			return;
		}
		if (IDoGooApp.getRequestQueue() == null) {
			RequestQueue mRequestQueue = Volley.newRequestQueue(IDoGooApp
					.getContext());
			IDoGooApp.setRequestQueue(mRequestQueue);
		}
		IDoGooApp.getRequestQueue().add(request);
	}
	
	/**
	 * 
	 * addRequest:加到同一个RequestQueue中。
	 *
	 * @author xiaojian
	 * @param request
	 */
	public static void addRequest(final Request<BaseParser> request, boolean isVolid) {
		if(!AccessKeyModel.getInstance().isAccessKeyVolid()) {
			AccessKeyParser parser = new AccessKeyParser();
			Request<BaseParser> accessKeyRequest = new IDoGooRequest(RequestUrl.accessKeyUrl,
					RequestUrl.getClientTicket(), parser,
					new OnProtocolTaskListener() {

						@Override
						public void onProgressUpdate(BaseParser parser) {
							Config.e("验证码失效");
							if (parser.getCode() == BaseParser.SUCCESS) {
								AccessKeyModel.getInstance().requesetAccesskeyTime(((AccessKeyParser) parser)
										.getAccess_key());
								Variable.getInstance().setAccessKey(
										((AccessKeyParser) parser).getAccess_key());
							}
							addRequest(request);
						}
					});
			addRequest(accessKeyRequest);
		}else {
			addRequest(request);
		}
	}


	/**
	 * 
	 * cancelAllRequest:退出应用时间取消所有请求。 此方法是针对全局唯一队列进行取消
	 *
	 * @author xiaojian
	 */
	public static void cancelAllRequest() {
		RequestQueue requestQueue = IDoGooApp.getRequestQueue();
		if (requestQueue == null) {
			return ;
		}
		requestQueue.cancelAll(new RequestQueue.RequestFilter() {
			@Override
			public boolean apply(Request<?> request) {
				return true;
			}
		});
		// 关闭线程
		requestQueue.stop();
	}

	/**
	 * 
	 * cancelRequestByTag:(这里用一句话描述这个方法的作用)
	 * TODO 建议使用页面的class.getName,需保证此变量与其它页面请求不能重复 此方法是针对全局唯一队列进行取消
	 *
	 * @param tag
	 */
	public static void cancelRequestByTag(String tag) {
		if (TextUtils.isEmpty(tag)) {
			return;
		}
		RequestQueue requestQueue = IDoGooApp.getRequestQueue();
		if (requestQueue != null) {
			IDoGooApp.getRequestQueue().cancelAll(tag);
		}
	}


}
