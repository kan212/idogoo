package com.idogoo.request;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.RequestFuture;
import com.idogoo.inter.OnProtocolTaskListener;
import com.idogoo.paser.BaseParser;
import com.idogoo.utils.Variable;

public class IDoGooRequest extends Request<BaseParser> {

	/** Default charset for JSON request. */
	protected final String PROTOCOL_CHARSET = "utf-8";

	private BaseParser parser;
	private Map<String, String> headers;
	private final Listener<BaseParser> mListener;
	private final String mRequestBody;

	/**
	 * Post请求构造方法。
	 * 
	 * @param url
	 *            请求URL
	 * @param requestBody
	 *            Post请求传URLEncodedUtils.format(pairs, "UTF-8").
	 * @param parser
	 *            请求结果解析parse，如果不需要解析，则传空
	 * @param callBack
	 *            回调
	 */
	public IDoGooRequest(String url, String requestBody,
			final BaseParser parser, final OnProtocolTaskListener callBack) {
		super(Method.POST, url, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				if (parser == null || callBack == null) {
					return;
				}
				callBack.onProgressUpdate(parser);
			}
		});
		mListener = new Response.Listener<BaseParser>() {
			@Override
			public void onResponse(BaseParser response) {
				if (parser == null || callBack == null) {
					return;
				}
				callBack.onProgressUpdate(response);
			}
		};
		this.parser = parser;
		this.mRequestBody = requestBody;
		setShouldCache(false);
	}

	/**
	 * Get请求专用
	 * 
	 * @param url
	 *            请求URL
	 * @param parser
	 *            请求结果解析parse，如果不需要解析，则传空
	 * @param callBack
	 *            回调
	 */
	public IDoGooRequest(String url, final BaseParser parser,
			final OnProtocolTaskListener callBack) {
		super(Method.GET, url, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				if (parser == null || callBack == null) {
					return;
				}
				callBack.onProgressUpdate(parser);
			}
		});
		mListener = new Response.Listener<BaseParser>() {
			@Override
			public void onResponse(BaseParser response) {
				if (parser == null || callBack == null) {
					return;
				}
				callBack.onProgressUpdate(response);
			}
		};
		this.parser = parser;
		this.mRequestBody = null;
		setShouldCache(false);
	}

	/**
	 * 同步请求
	 * 
	 * @param method
	 * @param url
	 * @param requestBody
	 *            Get请求传Null。Post请求传URLEncodedUtils.format(pairs, "UTF-8").
	 * @param parser
	 * @param futureSuc
	 * @param futureError
	 */
	public IDoGooRequest(int method, String url, String requestBody,
			final BaseParser parser, RequestFuture<BaseParser> futureSuc,
			RequestFuture<BaseParser> futureError) {
		super(method, url, futureError);
		mListener = futureSuc;
		this.parser = parser;
		this.mRequestBody = requestBody;
		setShouldCache(false);
	}

	public void setHeader(Map<String, String> header) {
		this.headers = header;
	}

	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		Map<String, String> headers = super.getHeaders();

		if (headers == null || headers.equals(Collections.emptyMap())) {
			headers = new HashMap<String, String>();
		}
		if(!TextUtils.isEmpty(Variable.getInstance().getSessionId())) {
			headers.put("Cookie", Variable.getInstance().getSessionId());
		}
		return headers;
	}

	@Override
	protected Response<BaseParser> parseNetworkResponse(NetworkResponse response) {
		try {
			String json = new String(response.data,
					HttpHeaderParser.parseCharset(response.headers));
			if (null != parser) {
				parser.parse(json);
			}
			if (isLogin) {
//				String phpsessid = response.headers.get("Set-Cookie");
//				Variable.getInstance().setSessionId(phpsessid);
				for(String s : response.headers.keySet()) {
					if(s.contains("Set-Cookie")) {
						Variable.getInstance().setSessionId(response.headers.get(s));
					}
				}
			}
			Response<BaseParser> success = Response.success(parser,
					HttpHeaderParser.parseCacheHeaders(response));
			return success;
		} catch (UnsupportedEncodingException e) {
			return Response.error(new ParseError(e));
		}
	}

	private boolean isLogin = false;

	public void setLogin(boolean isLogin) {
		this.isLogin = isLogin;
	}

	@Override
	protected void deliverResponse(BaseParser response) {
		mListener.onResponse(response);
	}

	/**
	 * @deprecated Use {@link #getBodyContentType()}.
	 */
	@Override
	public String getPostBodyContentType() {
		return getBodyContentType();
	}

	/**
	 * @deprecated Use {@link #getBody()}.
	 */
	@Override
	public byte[] getPostBody() {
		return getBody();
	}

	@Override
	public String getBodyContentType() {
		return "application/x-www-form-urlencoded; charset="
				+ getParamsEncoding();
	}

	@Override
	public byte[] getBody() {
		try {
			return mRequestBody == null ? null : mRequestBody
					.getBytes(PROTOCOL_CHARSET);
		} catch (UnsupportedEncodingException uee) {
			VolleyLog
					.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
							mRequestBody, PROTOCOL_CHARSET);
			return null;
		}
	}

}
