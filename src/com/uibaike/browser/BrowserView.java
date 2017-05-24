package com.uibaike.browser;

import java.util.ArrayList;
import java.util.List;

import android.widget.ViewFlipper;

/**
 * 单例模式
 * 
 * @author wangmx
 * 
 */
public class BrowserView {
	private static BrowserView instance = null;
	private ViewFlipper mTabList;
	private String urlback = "";
	private List<String> UrlList = new ArrayList<String>();
	public static String Url = "http://123.57.90.218:8082/ccbmsHome/login.jsp";
	public static String TAG = "HTBrowser";

	public List<String> getUrlList() {
		return UrlList;
	}

	public void setUrlList(String url) {
		
		UrlList.add(url);
	}
	public void setUrlList(List<String>  list) {
		
		UrlList = list;
	}

	public static BrowserView getInstance() {
		if (instance == null) { // line 12
			instance = new BrowserView(); // line 13
		}
		return instance;
	}

	public String getUrlback() {
		return urlback;
	}

	public void setUrlback(String urlback) {
		this.urlback = urlback;
	}

	public void setTabList(ViewFlipper viewFlipper) {
		mTabList = viewFlipper;
	}

	public ViewFlipper getTabList() {
		return mTabList;
	}
}
