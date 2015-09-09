/**
 * Project Name:SinaSports
 * File Name:BasicWebActivity.java
 * Package Name:cn.com.sina.sports.app
 * Date:2013-9-9下午3:15:18
 * Copyright (c) 2013, hushuan@staff.sina.com.cn All Rights Reserved.
 *
 */

package com.idogoo.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.DownloadListener;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.idogoo.R;
import com.idogoo.utils.Config;
import com.idogoo.utils.Constant;

public class BaseWebFragment extends Fragment {
	protected String mUrl;
	public View mViews;
	protected WebView mWebView;
	protected WebSettings mWebSettings;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Bundle args = getArguments();
		if (null != args) {
			mUrl = args.getString(Constant.EXTRA_URL);
		}
		if (TextUtils.isEmpty(mUrl)) {
			mUrl = "file:///android_asset/404.html";
		}
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mViews = inflater.inflate(R.layout.fragment_web, container, false);

		mWebView = (WebView) mViews.findViewById(R.id.wb_content);
		mWebView.setVisibility(View.INVISIBLE);
		mWebView.setDownloadListener(new MyWebViewDownLoadListener());
		return mViews;
	}

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mWebSettings = mWebView.getSettings();
		mWebSettings.setJavaScriptEnabled(true);
		mWebSettings.setBuiltInZoomControls(true);
		mWebSettings.setSaveFormData(false);
		mWebSettings.setUseWideViewPort(true);
		mWebSettings.setLoadWithOverviewMode(true);
		mWebSettings.setSupportZoom(false);
		mWebSettings.setDefaultTextEncodingName("UTF-8");
		loadUrl();
		setWebViewClient();
		setWebChromeClient();
	}

	protected void loadUrl() {
		if (!TextUtils.isEmpty(mUrl)) {
			mWebView.loadUrl(mUrl);
		}
	}

	public boolean isNeedsynCookies(String type) {
		CookieManager cookieManager = CookieManager.getInstance();
		String session = cookieManager.getCookie(type);
		if (TextUtils.isEmpty(session)) {
			return true;
		}
		return false;
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	protected OnClickListener mOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.iv_title_left:
				if (mWebView.canGoBack()) {
					mWebView.goBack();
				} else {
					getActivity().finish();
				}
				break;
			}
		}
	};

	public void setWebViewClient() {
		mWebView.setWebViewClient(new BasicWebViewClient());
	};

	public void setWebChromeClient() {
		mWebView.setWebChromeClient(new BasicWebChromeClient());
	};

	protected class BasicWebViewClient extends WebViewClient {

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			Config.e("============" + url);
			if ("file:///android_asset/jump.html".equals(mUrl)) {
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
				return true;
			}
			return super.shouldOverrideUrlLoading(view, url);
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
			Config.e("============" + url);
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			Config.i(url);
			if (mWebView.getVisibility() != View.VISIBLE) {
				mWebView.setVisibility(View.VISIBLE);
			}
		}

		@Override
		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {
			view.loadUrl("file:///android_asset/404.html");
		}

		@Override
		public void onReceivedSslError(WebView view, SslErrorHandler handler,
				SslError error) {
			handler.proceed();
		}

	}

	protected class BasicWebChromeClient extends WebChromeClient {

		@Override
		public boolean onJsAlert(WebView view, String url, String message,
				JsResult result) {
			return super.onJsAlert(view, url, message, result);
		}

		@Override
		public boolean onJsConfirm(WebView view, String url, String message,
				JsResult result) {
			Config.d("url:" + url);
			return super.onJsConfirm(view, url, message, result);
		}

		@Override
		public boolean onJsPrompt(WebView view, String url, String message,
				String defaultValue, JsPromptResult result) {
			return super.onJsPrompt(view, url, message, defaultValue, result);
		}

		@Override
		public boolean onJsBeforeUnload(WebView view, String url,
				String message, JsResult result) {
			return super.onJsBeforeUnload(view, url, message, result);
		}

	}

	private class MyWebViewDownLoadListener implements DownloadListener {

		@Override
		public void onDownloadStart(String url, String userAgent,
				String contentDisposition, String mimetype, long contentLength) {
			if (isAdded()) {
				Uri uri = Uri.parse(url);
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(intent);
			}
		}
	}

}
