package com.weiyi.itreader.common;

import java.util.Random;

import android.util.Log;

import com.weiyi.itreader.util.StreamUtil;

public class DownLoadThread extends Thread {
	@Override
	public void run() {
		int i = new Random().nextInt(Constant.DOWNLOADURL.length);
		StreamUtil.getInputStreamByUrl(Constant.DOWNLOADURL[i]);
		Log.e("DownLoadThread...", ""+i);
	}
}
