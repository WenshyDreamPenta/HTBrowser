package com.uibaike.browser.launcher;

import com.uibaike.browser.utils.QNManageLog;
import com.uibaike.browser.BrowserView;
import com.uibaike.browser.utils.ApplicationUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.WebBackForwardList;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * 自定义webview
 * 
 * @author wmx
 * 
 */
public class CustomWebView extends WebView {
	public static final String URL_ABOUT_START = "about:start";

	private Context mContext;
	private int mProgress = 0;


	public CustomWebView(Context context) {
		super(context);
		mContext = context;
		initSetting();
	}

	public CustomWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		initSetting();
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		// TODO 自动生成的方法存根
		super.onScrollChanged(l, t,oldl, oldt );
		//this.getSettings().setUseWideViewPort(false);
		//this.setScrollX(0);
		//this.setScrollY(0);
		//this.setInitialScale(70);
		QNManageLog.D(BrowserView.TAG, "onScrollChanged" + l + ";" + t + ";"
				+ oldl + ";" + oldt);

	}

	public int getProgress() {
		return mProgress;
	}

	public void setProgress(int progress) {
		mProgress = progress;
	}

	/**
	 * 跳转网页
	 * 
	 * @param url
	 */
	public void navigateToUrl(String url) {
		if (URL_ABOUT_START.equals(url)) {
			// Log.e("text", ApplicationUtils.getTextFromAssets(mContext,
			// "homepage/home.html"));
			loadDataWithBaseURL("file:///android_asset/homepage/",
					ApplicationUtils.getTextFromAssets(mContext,
							"homepage/home.html"), "text/html", "UTF-8",
					URL_ABOUT_START);
		} else {
			loadUrl(url);
		}
	}

	/**
	 * webview初始化
	 */
	private void initSetting() {
		WebSettings setting = getSettings();

		setting.setJavaScriptEnabled(true);
		setting.setLoadsImagesAutomatically(true);

		setting.setSupportMultipleWindows(true);
		setting.setAppCacheEnabled(true);
		setting.setDatabaseEnabled(true);
		setting.setDomStorageEnabled(true);
		setLongClickable(true);
		setDrawingCacheEnabled(true);
		setting.setSupportZoom(false);
		setting.setLoadWithOverviewMode(true);
		setting.setUseWideViewPort(false);
		setting.setLoadWithOverviewMode(false);

		setWebViewClient(new WebViewClient());
		setWebChromeClient(new WebChromeClient());
	}

	@Override
	public void goBack() {
		super.goBack();
	}

	/**
	 * 获取WebView缩略图
	 * 
	 * @return
	 */
	public Bitmap captureWebViewVisibleSize() {
		return getDrawingCache();
	}

	/**
	 * WebView截图
	 * 
	 * @param webView
	 * @return
	 */
	private Bitmap captureWebView(WebView webView) {
		Picture snapShot = capturePicture();
		Bitmap bmp = Bitmap.createBitmap(snapShot.getWidth(),
				snapShot.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bmp);
		snapShot.draw(canvas);
		return bmp;
	}
}
