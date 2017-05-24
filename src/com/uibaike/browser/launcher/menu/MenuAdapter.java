package com.uibaike.browser.launcher.menu;

import java.util.ArrayList;

import com.uibaike.browser.R;
import com.uibaike.browser.launcher.menu.MenuData.Menu;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

/**
 * popup≤Àµ•  ≈‰∆˜
 * @author wangcheng/494518071@qq.com
 *
 */
public class MenuAdapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<Menu> mMenuList;
	
	public MenuAdapter(Context context, ArrayList<Menu> menuList) {
		mMenuList = menuList;
		mContext = context;
	}
	
	@Override
	public int getCount() {
		return mMenuList.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView textView;
		
		if (convertView == null) {
			textView = new TextView(mContext);
			textView.setLayoutParams(new GridView.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			textView.setGravity(Gravity.CENTER);
			textView.setCompoundDrawablePadding(
					mContext.getResources().getDimensionPixelSize(R.dimen.popupmenu_icon_padding));
			textView.setTextColor(Color.WHITE);
			convertView = textView;
		} else {
			textView = (TextView) convertView;
		}
		
		Menu menu = mMenuList.get(position);
		textView.setText(menu.title);
		textView.setCompoundDrawablesWithIntrinsicBounds(0, menu.iconId, 0, 0);
		
		return textView;
	}

}
