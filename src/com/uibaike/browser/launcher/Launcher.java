package com.uibaike.browser.launcher;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.uibaike.browser.launcher.CustomWebView;

/**
 * �Զ���Launcher
 * @author wangcheng/494518071@qq.com
 *
 */
public class Launcher extends ViewPager {
	private Context mContext;
	private ArrayList<View> mPageList = new ArrayList<View>();
	
	public Launcher(Context context) {
		super(context);
		
		mContext = context;
		initPage();
	}
	
	public Launcher(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		mContext = context;
		initPage();
	}
	
	public ArrayList<View> getPageList() {
		return mPageList;
	}
	
	private void initPage() {
		ViewFlipper viewFlipper = new ViewFlipper(mContext);
		viewFlipper.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 
				ViewGroup.LayoutParams.MATCH_PARENT));
		
		TextView textView = new TextView(mContext);
		textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 
				ViewGroup.LayoutParams.WRAP_CONTENT));
		textView.setText("�ڶ���");
		textView.setTextColor(Color.BLACK);
				
		mPageList.add(viewFlipper);
		//mPageList.add(textView);
		setAdapter(new LanucherAdapter(mPageList));
	}
	
	/*
	 * ��ҳviewPage��ͼ������
	 */
	private class LanucherAdapter extends PagerAdapter {
		public ArrayList<View> arrayList;

		public LanucherAdapter(ArrayList<View> arrayList) {
			this.arrayList = arrayList;
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView(arrayList.get(arg1));
		}

		@Override
		public void finishUpdate(View arg0) {
		}

		@Override
		public int getCount() {
			return arrayList.size();
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(arrayList.get(arg1), 0);
			return arrayList.get(arg1);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == (arg1);
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
		}
	}
}
