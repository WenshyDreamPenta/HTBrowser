package com.uibaike.browser.activity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.uibaike.browser.utils.QNManageLog;
import com.uibaike.browser.BrowserApplication;
import com.uibaike.browser.R;
import com.uibaike.browser.launcher.CustomWebView;
import com.uibaike.browser.launcher.Launcher;
import com.uibaike.browser.launcher.menu.PopupMenu;
import com.uibaike.browser.BrowserView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.InputType;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnHoverListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebView.HitTestResult;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

/**
 * 浏览器主类
 * 
 * @author wangmx
 * 
 */
public class MainActivity extends Activity {

	private String TAG = "Browser--MainActivity";
	private Launcher mLauncher;
	private ViewFlipper mViewFlipper;
	private PopupMenu mPopupMenu;

	private AutoCompleteTextView mInputUrl;
	private ImageView mSearchIv;
	private ImageView mStopSearchIv;
	private ProgressBar mWebViewPb;

	private ImageButton mPopupMenuBtn;
	private FrameLayout mTabNumBtn;
	private TextView mTabNumTv;
	private ImageButton mPrePageIb;
	private ImageButton mNextPageIb;
	private ImageButton mHomeIb;

	private ImageView mPageCount;

	private CustomWebView mCurrentWebView;
	private boolean mIsWebViewHome = true;
	private boolean Isfirst = true;
	private boolean IsOpenNewWindow = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);
		QNManageLog.D(BrowserView.TAG, "onCreate");

		// 隐藏输入法
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		mLauncher = (Launcher) findViewById(R.id.launcher);
		mLauncher.setOnPageChangeListener(new MainPageChangeListener());
		mViewFlipper = (ViewFlipper) mLauncher.getPageList().get(0);
		mTabNumBtn = (FrameLayout) findViewById(R.id.tab_num_btn);
		mTabNumTv = (TextView) findViewById(R.id.tab_num_main);
		mPrePageIb = (ImageButton) findViewById(R.id.pre_page);
		mNextPageIb = (ImageButton) findViewById(R.id.next_page);
		mHomeIb = (ImageButton) findViewById(R.id.home_btn);
		mInputUrl = (AutoCompleteTextView) findViewById(R.id.input_url);
		mSearchIv = (ImageView) findViewById(R.id.search_btn);
		mStopSearchIv = (ImageView) findViewById(R.id.stop_search_btn);
		mWebViewPb = (ProgressBar) findViewById(R.id.WebViewProgress);
		// mPageCount = (ImageView) findViewById(R.id.page_count);

		mInputUrl.setOnClickListener(hiddenSoftInput);
		mInputUrl.setOnHoverListener(onHoverListener);
		/* 前进按钮设置监听 */
		mSearchIv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mCurrentWebView.navigateToUrl(mInputUrl.getText().toString());
				v.setVisibility(View.GONE);
				mStopSearchIv.setVisibility(View.VISIBLE);
			}
		});

		/* 停止按钮 */
		mStopSearchIv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mCurrentWebView.stopLoading();
				v.setVisibility(View.GONE);
				mSearchIv.setVisibility(View.VISIBLE);
			}
		});

		/* 前一页按钮 */
		mPrePageIb.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mCurrentWebView.canGoBack()) {
					mCurrentWebView.goBack();
					mInputUrl.setText(mCurrentWebView.getUrl());
				}
			}
		});

		/* 下一页按钮 */
		mNextPageIb.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mCurrentWebView.canGoForward()) {
					mCurrentWebView.goForward();
					mInputUrl.setText(mCurrentWebView.getUrl());
				}
			}
		});

		/* 去除这个按钮 */

/*		mPopupMenuBtn = (ImageButton) findViewById(R.id.popup_menu_btn);
		mPopupMenuBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mPopupMenu == null) {
					mPopupMenu = new PopupMenu(MainActivity.this);
				}
				if (mPopupMenu.isShowing()) {
					mPopupMenu.dismiss();
				} else {
					mPopupMenu.showAtLocation(findViewById(R.id.root),
							Gravity.BOTTOM, 0, 60);
				}
			}
		});*/

		/* Home键按钮 */
		mHomeIb.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 返回平安主页
				Intent i = new Intent(Intent.ACTION_MAIN);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // android123提示如果是服务里调用，必须加入new
															// // task标识
				i.addCategory(Intent.CATEGORY_HOME);
				startActivity(i);

			}
		});

		/* 多标签按钮 */
		mTabNumBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// setTabUrlList();
				QNManageLog.D(
						BrowserView.TAG,
						" ----- mTabNumBtn.setOnClickListener -----，mCurrentWebView:"
								+ ((CustomWebView) mViewFlipper
										.getCurrentView()).getUrl().toString()
								+ ";mCurrentWebView--"
								+ mCurrentWebView.getUrl().toString());
				mCurrentWebView.setWebChromeClient(null);
				mCurrentWebView.setWebViewClient(null);
				Intent intent = new Intent(MainActivity.this,
						BrowserTabManagerActivity.class);
				startActivityForResult(intent, 0x00);
			}
		});

		// 当前网页BrowserView.getInstance().Url

		if (mViewFlipper != null && mViewFlipper.getChildCount() != 0) {
			mViewFlipper.removeAllViews();
		}
		mCurrentWebView = new CustomWebView(this);
		//mCurrentWebView.setOnClickListener(hiddenSoftInput);
		setCurrentWebView();
		mCurrentWebView.navigateToUrl(BrowserView.getInstance().Url);

		mViewFlipper.addView(mCurrentWebView);
		BrowserView.getInstance().setTabList(mViewFlipper);
		mTabNumTv.setText(mViewFlipper.getChildCount() + "");

		// 设置进度条
		mWebViewPb.setProgress(0);

		QNManageLog.D(BrowserView.TAG, " Oncreate----- mTabNumTv -----:"
				+ mViewFlipper.getChildCount());
	}

	@Override
	protected void onDestroy() {

		super.onDestroy();
		Isfirst = false;
		IsOpenNewWindow = false;
		QNManageLog.D(BrowserView.TAG, " ----- onDestroy() -----");
	}

	@Override
	protected void onResume() {
		QNManageLog.D(
				BrowserView.TAG,
				" ----- onResume() -----，mViewFlipper.getCount:"
						+ mViewFlipper.getChildCount());
		super.onResume();
		IsOpenNewWindow = false;
		mCurrentWebView = (CustomWebView) mViewFlipper.getCurrentView();
		setCurrentWebView();
		mInputUrl.setText(mCurrentWebView.getUrl().toString());
		QNManageLog.D(BrowserView.TAG,
				" mCurrentWebView.getUrl().toString()---"
						+ mCurrentWebView.getUrl().toString());

	}

	@Override
	protected void onStop() {
		super.onStop();
		QNManageLog.D(BrowserView.TAG, " ----- onStop() -----");
	}

	OnClickListener hiddenSoftInput = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			InputMethodManager imm = (InputMethodManager) getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(arg0.getWindowToken(), 0);
		}
	};
	OnHoverListener onHoverListener = new OnHoverListener() {

		@Override
		public boolean onHover(View arg0, MotionEvent arg1) {
			InputMethodManager imm = (InputMethodManager) getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(arg0.getWindowToken(), 0);
			return false;
		}
	};

	private void setTabUrlList() {
		List<String> urllist = BrowserView.getInstance().getUrlList();
		for (int i = 0; i < urllist.size(); i++) {
			CustomWebView currentWebView = new CustomWebView(getBaseContext());
			currentWebView
					.navigateToUrl(BrowserView.getInstance().getUrlback());
			mViewFlipper.addView(currentWebView);
		}
		BrowserView.getInstance().setTabList(mViewFlipper);

	}

	/**
	 * 设置webview的格式
	 */
	private void setCurrentWebView() {
		mWebViewPb.setProgress(mCurrentWebView.getProgress());
		WebSettings settings = mCurrentWebView.getSettings();
		mCurrentWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
		settings.setDomStorageEnabled(true);
		settings.setJavaScriptEnabled(true);
		settings.setSupportZoom(true);
		settings.setUseWideViewPort(false);
		settings.setLoadWithOverviewMode(false);
		settings.setJavaScriptCanOpenWindowsAutomatically(true);
		mCurrentWebView.setWebViewClient(new MyWebViewClient());
		mCurrentWebView.setWebChromeClient(new MyWebChromeClient());
		mCurrentWebView
				.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
					@Override
					public void onCreateContextMenu(ContextMenu menu, View v,
							ContextMenuInfo menuInfo) {
						HitTestResult result = ((WebView) v).getHitTestResult();

						int resultType = result.getType();
						if ((resultType == HitTestResult.ANCHOR_TYPE)
								|| (resultType == HitTestResult.IMAGE_ANCHOR_TYPE)
								|| (resultType == HitTestResult.SRC_ANCHOR_TYPE)
								|| (resultType == HitTestResult.SRC_IMAGE_ANCHOR_TYPE)) {
							Toast.makeText(MainActivity.this, "刷新",
									Toast.LENGTH_LONG).show();
						} else if (resultType == HitTestResult.IMAGE_TYPE) {
						} else if (resultType == HitTestResult.EMAIL_TYPE) {

						}
					}

				});
		mCurrentWebView.setOnFocusChangeListener(new View.OnFocusChangeListener() {  
		    @Override  
		    public void onFocusChange(View v, boolean hasFocus) {  
		        if(hasFocus)  
		        {  
		            try {  
		                Field defaultScale = WebView.class.getDeclaredField("mDefaultScale");  
		                defaultScale.setAccessible(true);  
		                //WebViewSettingUtil.getInitScaleValue(VideoNavigationActivity.this, false )/100.0f 是我的程序的一个方法，可以用float 的scale替代  
		                defaultScale.setFloat(mCurrentWebView , 1.0f);  
		            } catch (SecurityException e) {  
		                e.printStackTrace();  
		            } catch (IllegalArgumentException e) {  
		                e.printStackTrace();  
		            } catch (IllegalAccessException e) {  
		                e.printStackTrace();  
		            } catch (NoSuchFieldException e) {  
		                e.printStackTrace();  
		            }   
		        }  
		    }  
		});
	}

	/*
	 * lanucher页数变换监听
	 */
	private class MainPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageSelected(int arg0) {
			mPageCount.setImageLevel(arg0);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add("menu"); //
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onMenuOpened(int featureId, Menu menu) {
		if (mPopupMenu == null) {
			mPopupMenu = new PopupMenu(MainActivity.this);
		}
		if (mPopupMenu.isShowing()) {
			mPopupMenu.dismiss();
		} else {
			mPopupMenu.showAtLocation(findViewById(R.id.root), Gravity.BOTTOM,
					0, 60);
		}
		return false;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 0x00) {
			mTabNumTv.setText(mViewFlipper.getChildCount() + "");
			QNManageLog.D(
					BrowserView.TAG,
					"onActivityResult ----- mTabNumTv -----:"
							+ mViewFlipper.getChildCount());
			mCurrentWebView = (CustomWebView) mViewFlipper.getCurrentView();
			QNManageLog.D(BrowserView.TAG,
					"onActivityResult-----mCurrentWebView ："
							+ mCurrentWebView.getUrl().toString());
			setCurrentWebView();
		}
	}

	/**
	 * 自定义WebViewClient
	 */
	private class MyWebViewClient extends WebViewClient {

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			super.shouldOverrideUrlLoading(view, url);

			/*
			 * 调用默认浏览器 Intent i = new Intent(Intent.ACTION_VIEW);
			 * i.setData(Uri.parse(url)); startActivity(i);
			 */

			QNManageLog.D(BrowserView.TAG, "shouldOverrideUrlLoading-----");
			openNewWindow(url);
			mInputUrl.setText(url);
			mViewFlipper.setDisplayedChild(mViewFlipper.getChildCount() - 1);

			return true;
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {

			QNManageLog.D(BrowserView.TAG, "onPageStarted -----" + url);
			mSearchIv.setVisibility(View.GONE);
			mStopSearchIv.setVisibility(View.VISIBLE);
			// 在这里打开新网页
			mInputUrl.setText(mCurrentWebView.getUrl().toString());
			super.onPageStarted(view, url, favicon);
			CustomWebView currentWebView1 = new CustomWebView(getBaseContext());
			currentWebView1.navigateToUrl(url);
			currentWebView1.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
			WebSettings settings = currentWebView1.getSettings();
			settings.setDomStorageEnabled(true);
			settings.setJavaScriptEnabled(true);
			settings.setSupportZoom(true);
			settings.setUseWideViewPort(false);
			settings.setLoadWithOverviewMode(false);
			settings.setBuiltInZoomControls(false);
			settings.setJavaScriptCanOpenWindowsAutomatically(true);
			/*
			 * if (Isfirst) { mViewFlipper.removeAllViews(); Isfirst = false; }
			 */
			if (!url.contains("http://123.57.90.218:8082/ccbmsHome/privilege/loginAction.do")) {
				for(int i = 0; i<mViewFlipper.getChildCount();i++){
					if(!((CustomWebView)mViewFlipper.getChildAt(i)).getUrl().contains(url)){
						//mViewFlipper.addView(currentWebView1);
						mViewFlipper.setDisplayedChild(mViewFlipper.getChildCount()-1);
					}
				}
				
			}
			
			BrowserView.getInstance().setTabList(mViewFlipper);
			// mTabNumTv.setText(mViewFlipper.getChildCount() + "");
			QNManageLog.D(
					BrowserView.TAG,
					"onPageStarted ----- mTabNumTv -----:"
							+ mViewFlipper.getChildCount());

		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			QNManageLog.D(BrowserView.TAG, "----- onPageFinished -----" + url);
			// setCurrentWebView();
			mInputUrl.setText(mCurrentWebView.getUrl().toString());
			// 解决第一次进入无法弹窗的问题
			for (int i = 0; i < mViewFlipper.getChildCount(); i++) {

				if (mViewFlipper.getChildAt(i) != mViewFlipper.getCurrentView()
						&& url.equals(BrowserView.getInstance().Url)) {
					QNManageLog.D(BrowserView.TAG, " ----- 删除重复的 -----，i:" + i);
				//	mViewFlipper.removeViewAt(i);
				}
			}
			mTabNumTv.setText(mViewFlipper.getChildCount() + "");
			mWebViewPb.setProgress(0);
			mSearchIv.setVisibility(View.VISIBLE);
			mStopSearchIv.setVisibility(View.GONE);
		}
	}

	private class MyWebChromeClient extends WebChromeClient {
		@Override
		public boolean onJsAlert(WebView view, String url, String message,
				JsResult result) {

			QNManageLog.D(BrowserView.TAG, "onJsAlert-----"
					+ mInputUrl.getText().toString());

			return true;
		}

		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			super.onProgressChanged(view, newProgress);
			mCurrentWebView.setProgress(newProgress);
			mWebViewPb.setProgress(newProgress);
			view.requestFocus();
		}
		 @Override  
	        public void onCloseWindow(WebView window) {  
	            //TODO something  
	            super.onCloseWindow(window);  
	            QNManageLog.D(BrowserView.TAG, "----- onCloseWindow()-----"
						+ mInputUrl.getText().toString());
	            mViewFlipper.removeView((CustomWebView)mViewFlipper.getCurrentView());
	          //  mViewFlipper.setDisplayedChild(mViewFlipper.getChildCount() - 1);
	            BrowserView.getInstance().setTabList(mViewFlipper);
	            mTabNumTv.setText(mViewFlipper.getChildCount() + "");	            
	            Intent intent = new Intent("android.intent.action.HTBrowser");
	            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
	        } 

		@Override
		public boolean onCreateWindow(WebView view, boolean dialog,
				boolean userGesture, Message resultMsg) {
			QNManageLog.D(BrowserView.TAG, "----- onCreateWindow()-----"
					+ mInputUrl.getText().toString());
			CustomWebView mCurrentWebView1 = new CustomWebView(
					view.getContext());
			view.addView(mCurrentWebView1);
			mCurrentWebView1.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
			WebSettings settings = mCurrentWebView1.getSettings();
			settings.setDomStorageEnabled(true);
			settings.setJavaScriptEnabled(true);
			settings.setSupportZoom(true);
			settings.setUseWideViewPort(false);
			settings.setLoadWithOverviewMode(false);
			settings.setBuiltInZoomControls(false);
			
			settings.setJavaScriptCanOpenWindowsAutomatically(true);
			// 这个setWebViewClient要加上，否则window.open弹出浏览器打开。
			mCurrentWebView1.setWebViewClient(new MyWebViewClient());
			mCurrentWebView1.setWebChromeClient(this);

			WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
			transport.setWebView(mCurrentWebView1);
			// QNManageLog.D("MainActivity", "mCurrentWebView -----" +
			// mCurrentWebView.getUrl().toString());
			resultMsg.sendToTarget();

			return true;
		}

	}

	/**
	 * 打开新网址方法
	 * 
	 * @param url
	 */
	private void openNewWindow(String url) {
	    mInputUrl.setText(url);
		QNManageLog.D(BrowserView.TAG, "-----openNewWindow()--url:[" + url
				+ "]");
		// 原网页停止加载
		mCurrentWebView.stopLoading();
		CustomWebView currentWebView1 = new CustomWebView(getBaseContext());
		currentWebView1.navigateToUrl(url);
		mViewFlipper.addView(currentWebView1);
		currentWebView1.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
		currentWebView1.setWebViewClient(new MyWebViewClient());
		currentWebView1.setWebChromeClient(new MyWebChromeClient());
		WebSettings settings = currentWebView1.getSettings();
		currentWebView1.setInitialScale(100);
		settings.setDomStorageEnabled(true);
		settings.setJavaScriptEnabled(true);
		settings.setSupportZoom(true);
		settings.setUseWideViewPort(false);
		settings.setLoadWithOverviewMode(false);
		settings.setBuiltInZoomControls(false);
		settings.setJavaScriptCanOpenWindowsAutomatically(true);
		mTabNumTv.setText(mViewFlipper.getChildCount() + "");
		QNManageLog.D(BrowserView.TAG,
				" ----- mTabNumTv -----:" + mViewFlipper.getChildCount());
		// Setwebview(currentWebView1);
		BrowserView.getInstance().setTabList(mViewFlipper);
	}

}
