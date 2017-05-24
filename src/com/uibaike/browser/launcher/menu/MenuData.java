package com.uibaike.browser.launcher.menu;

import java.util.ArrayList;

import com.uibaike.browser.R;

import android.content.Context;
import android.content.res.TypedArray;

/**
 * 弹出菜单资源获取类
 * @author wangcheng/494518071@qq.com
 *
 */
public class MenuData {
	public final static int MENU_TYPE_NORMAL = 0x00;
	public final static int MENU_TYPE_SETTING = 0x01;
	public final static int MENU_TYPE_TOOL = 0x02;
	
	String[] mMenuTitle;
	int[] mMenuIcon;
	
	public MenuData(Context context) {
		mMenuTitle = context.getResources().getStringArray(R.array.popupmenu_srtirg_array);
		TypedArray typedArray = context.getResources().obtainTypedArray(R.array.popupmenu_icon_array);
		
		mMenuIcon = new int[typedArray.length()];
		for (int i = 0; i < typedArray.length(); i++) {
			mMenuIcon[i] = typedArray.getResourceId(i, 0);
		}	
		typedArray.recycle();
	}
	
	public ArrayList<Menu> getMenuList(int menuType) {
		ArrayList<Menu> list = new ArrayList<Menu>();
		
		int k, j;
		if (menuType == MENU_TYPE_NORMAL) {
			k = 0;
			j = 8;
		} else if (menuType == MENU_TYPE_SETTING) {
			k = 8;
			j = 15;
		} else {
			k = 15;
			j = 21;
		}
		
		for (int i = k; i < j; i++) {
			Menu menu = new Menu();
			menu.title = mMenuTitle[i];
			menu.iconId = mMenuIcon[i];
			list.add(menu);
		}
		return list;
	}
	
	public class Menu {
		public String title;
		public int iconId;
	}
}
