package com.weiyi.itreader.util;

import android.view.View;

public class ViewUtil {
	/**
	 * ��ȡView�����ĵ�
	 * 
	 * @param view
	 *            View����
	 * @return
	 * */
	public static int getCenterOfView(View view) {
		return view.getLeft() + view.getWidth() / 2;
	}
}
