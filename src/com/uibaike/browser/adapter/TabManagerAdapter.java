package com.uibaike.browser.adapter;

import com.channelsoft.android.qnn8library.util.QNManageLog;
import com.uibaike.browser.BrowserApplication;
import com.uibaike.browser.BrowserView;
import com.uibaike.browser.R;
import com.uibaike.browser.activity.BrowserTabManagerActivity;
import com.uibaike.browser.launcher.CustomWebView;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

/**
 * tab适配器
 * 
 * @author wmx
 * 
 */
public class TabManagerAdapter extends BaseAdapter {
	private ViewFlipper mViewFlipper;
	private LayoutInflater mInfalter;
	private Context mContext;
	private static String TAG = "TabManagerAdapter";

	public TabManagerAdapter(Context context, ViewFlipper viewFlipper) {
		mContext = context;
		mViewFlipper = viewFlipper;
		mInfalter = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return mViewFlipper.getChildCount();

	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		Holder holder;

		if (convertView == null) {
			holder = new Holder();
			convertView = (LinearLayout) mInfalter.inflate(
					R.layout.tab_manager_item, null);
			holder.title = (TextView) convertView.findViewById(R.id.tab_title);
			holder.close = (ImageView) convertView
					.findViewById(R.id.tab_close_btn);
			holder.thumb = (ImageView) convertView.findViewById(R.id.tab_thumb);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}

		CustomWebView webView = (CustomWebView) mViewFlipper
				.getChildAt(position);
		holder.title.setText(webView.getTitle());
		QNManageLog.D(BrowserView.TAG,
				"title,childposition:" + webView.getTitle() + ","
						+ mViewFlipper.getChildCount());
		holder.thumb.setImageBitmap(webView.captureWebViewVisibleSize());

		/* webview点击关闭事件 */
		holder.close.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (position == 0 && mViewFlipper.getChildCount() == 1) {
					CustomWebView customWebView = new CustomWebView(mContext);
					customWebView.navigateToUrl(BrowserView.getInstance().Url);
					mViewFlipper.removeAllViews();
					mViewFlipper.addView(customWebView);
					Intent intent = new Intent(
							"android.intent.action.HTBrowser");
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					mContext.startActivity(intent);
					((BrowserTabManagerActivity) mContext).finish();
				} else {
					mViewFlipper.removeViewAt(position);
					notifyDataSetChanged();
				}
			}
		});

		/* 点击WebView缩略图触发事件 */
		holder.thumb.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				CustomWebView customWebView = (CustomWebView) mViewFlipper
						.getChildAt(position);

				mViewFlipper.setDisplayedChild(position);
				Intent intent = new Intent("android.intent.action.HTBrowser");
				mContext.startActivity(intent);
				((BrowserTabManagerActivity) mContext).finish();
			}
		});
		return convertView;
	}

	private class Holder {
		TextView title;
		ImageView close;
		ImageView thumb;
	}
}
