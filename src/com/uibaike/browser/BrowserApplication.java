package com.uibaike.browser;

import javax.xml.namespace.QName;

import com.uibaike.browser.utils.QNBaseApplication;

import android.app.Application;
import android.widget.ViewFlipper;

/**
 * Ӧ��ȫ����
 * @author wangcheng/494518071@qq.com
 *
 */
public class BrowserApplication extends QNBaseApplication {
	private static BrowserApplication instance;
	
	private ViewFlipper mTabList; 
	private String urlback;
	
	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
	}
	
	public String getUrlback() {
		return urlback;
	}

	public void setUrlback(String urlback) {
		this.urlback = urlback;
	}

	public static BrowserApplication getInstance() {
		return instance;
	}
	
	public void setTabList(ViewFlipper viewFlipper) {
		mTabList = viewFlipper;
	}
	
	public ViewFlipper getTabList() {
		return mTabList;
	}
}
