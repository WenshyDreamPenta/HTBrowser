package com.uibaike.browser.utils;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;

public class ApplicationUtils {
	/**
	 * 从Asset目录中读取文本文件
	 * @param context
	 * @param filename
	 * @return
	 */
	public static String getTextFromAssets(Context context, String filename) {
		String result = null;
		
		try {
			InputStream in = context.getAssets().open(filename);
			byte[] bytes = new byte[in.available()];
			in.read(bytes);
			result = new String(bytes);
		} catch (IOException e) {
			e.printStackTrace();
		}				
		return result;
	}
}
