package com.fengjie.myapplication.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Message;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * @author Created by MadJieJie on 2017/2/28-15:47.
 * @brief
 * @attention
 */

public class DefinedWeb extends WebView
{
	private static final String DEFAULT_URL = "http://www.baidu.com/";
	private Activity mActivity;

	public DefinedWeb ( Context context )
	{
		super(context);
		mActivity = ( Activity ) context;
		init(context);
	}

	@SuppressLint ( "SetJavaScriptEnabled" )
	private void init ( Context context )
	{
		WebSettings webSettings = this.getSettings();       /**Base set*/
		webSettings.setJavaScriptEnabled(true);             //支持JAVAScript
		webSettings.setSupportZoom(true);                   //
		//webSettings.setUseWideViewPort(true);
		this.setWebViewClient(mWebViewClientBase);
		this.setWebChromeClient(mWebChromeClientBase);
		this.loadUrl(DEFAULT_URL);
		this.onResume();
	}

	private WebViewClientBase mWebViewClientBase = new WebViewClientBase();

	private class WebViewClientBase extends WebViewClient
	{

		@Override
		public boolean shouldOverrideUrlLoading ( WebView view, String url )
		{
			// TODO Auto-generated method stub
			return super.shouldOverrideUrlLoading(view, url);
		}

		@Override
		public void onPageStarted ( WebView view, String url, Bitmap favicon )
		{
			// TODO Auto-generated method stub
			super.onPageStarted(view, url, favicon);
		}

		@Override
		public void onPageFinished ( WebView view, String url )
		{
			// TODO Auto-generated method stub
			super.onPageFinished(view, url);
		}

		@Override
		public void onReceivedError ( WebView view, int errorCode,
		                              String description, String failingUrl )
		{
			// TODO Auto-generated method stub
			super.onReceivedError(view, errorCode, description, failingUrl);
		}

		@Override
		public void doUpdateVisitedHistory ( WebView view, String url,
		                                     boolean isReload )
		{
			// TODO Auto-generated method stub
			super.doUpdateVisitedHistory(view, url, isReload);
		}
	}

	private WebChromeClientBase mWebChromeClientBase = new WebChromeClientBase();

	private class WebChromeClientBase extends WebChromeClient
	{

		@Override
		public void onProgressChanged ( WebView view, int newProgress )
		{
			mActivity.setProgress(newProgress * 1000);
		}

		@Override
		public void onReceivedTitle ( WebView view, String title )
		{
			// TODO Auto-generated method stub
			super.onReceivedTitle(view, title);
		}

		@Override
		public void onReceivedTouchIconUrl ( WebView view, String url,
		                                     boolean precomposed )
		{
			// TODO Auto-generated method stub
			super.onReceivedTouchIconUrl(view, url, precomposed);
		}

		@Override
		public boolean onCreateWindow ( WebView view, boolean isDialog,
		                                boolean isUserGesture, Message resultMsg )
		{
			// TODO Auto-generated method stub
			return super.onCreateWindow(view, isDialog, isUserGesture, resultMsg);
		}

	}
}
