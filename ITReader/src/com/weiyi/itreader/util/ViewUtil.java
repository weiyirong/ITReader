package com.weiyi.itreader.util;

import android.view.View;

public class ViewUtil {
	/**
	 * 获取View的中心点
	 * 
	 * @param view
	 *            View对象
	 * @return
	 * */
	public static int getCenterOfView(View view) {
		return view.getLeft() + view.getWidth() / 2;
	}
}
