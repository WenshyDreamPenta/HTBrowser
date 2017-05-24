package com.uibaike.browser.activity;

import com.uibaike.browser.BrowserView;
import com.uibaike.browser.R;
import com.uibaike.browser.adapter.TabManagerAdapter;
import com.uibaike.browser.launcher.CustomWebView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebSettings;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewFlipper;

/**
 * 多标签页activity
 * @author wangcheng/494518071@qq.com
 *
 */
public class BrowserTabManagerActivity extends Activity {
	private ListView mTabLv;
	private TextView mNewTabTv;
	private TextView mClearTabTv;
	private TextView mTabNumTv;
	
	private ViewFlipper mViewFlipper;
	private TabManagerAdapter mTabManagerAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.tab_manager);
		
		mViewFlipper = BrowserView.getInstance().getTabList();
		
		mTabLv = (ListView) findViewById(R.id.tab_list);
		mNewTabTv = (TextView) findViewById(R.id.new_tab);
		mClearTabTv = (TextView) findViewById(R.id.clear_tab);
		mTabNumTv = (TextView) findViewById(R.id.tab_num);
		
		//��������ͼ�б�
		mTabManagerAdapter = new TabManagerAdapter(this, mViewFlipper);
		mTabLv.setAdapter(mTabManagerAdapter);
		
		//�����·�tab��
		mTabNumTv.setText(mViewFlipper.getChildCount()+" 个页面");
		mTabNumTv.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		mNewTabTv.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View v) {
				//CustomWebView customWebView = new CustomWebView(BrowserApplication.getInstance());
				String url = "http://182.92.230.219/demo.html";
				CustomWebView customWebView = new CustomWebView(getApplicationContext());
				customWebView.navigateToUrl(BrowserView.getInstance().Url);//BrowserView.getInstance().Url
				
				mViewFlipper.addView(customWebView);
				WebSettings settings = customWebView.getSettings();
				settings.setDomStorageEnabled(true);
				settings.setJavaScriptEnabled(true);
				settings.setJavaScriptCanOpenWindowsAutomatically(true);
				mViewFlipper.setDisplayedChild(mViewFlipper.getChildCount()-1);
				mTabNumTv.setText(mViewFlipper.getChildCount()+" 个页面");
				Intent intent = new Intent("android.intent.action.HTBrowser");
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
				
				BrowserTabManagerActivity.this.finish();
			}
		});
		
		mClearTabTv.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				//CustomWebView customWebView = new CustomWebView(BrowserApplication.getInstance());
				CustomWebView customWebView = new CustomWebView(getApplicationContext());
				String url = "http://182.92.230.219/demo.html";
				customWebView.navigateToUrl(BrowserView.getInstance().Url);
				
				
				mViewFlipper.removeAllViews();
				mViewFlipper.addView(customWebView);
				WebSettings settings = customWebView.getSettings();
				settings.setDomStorageEnabled(true);
				settings.setJavaScriptEnabled(true);
				settings.setJavaScriptCanOpenWindowsAutomatically(true);
				mViewFlipper.setDisplayedChild(mViewFlipper.getChildCount()-1);
				BrowserView.getInstance().setUrlback(CustomWebView.URL_ABOUT_START);
				Intent intent = new Intent("android.intent.action.HTBrowser");
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
				BrowserTabManagerActivity.this.finish();
			}
		});	
	}
	
	
}
