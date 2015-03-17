package com.weiyi.itreader.common;

import java.io.InputStream;

import com.weiyi.itreader.ui.ReaderActivity;
import com.weiyi.itreader.util.ImageUtil;
import com.weiyi.itreader.util.MyNetImageCacheManager;
import com.weiyi.itreader.util.StreamUtil;
import com.weiyi.itreader.view.MyBitmapDrawable;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;

/**
 * ���ܣ��첽����ͼƬDrawable��������ڲ���ΪͼƬ URL�����ڲ���ΪDrawable����,
 * ��Ҫ��Ϊ�˴�����TextView��ʾ����ͼƬʱ�����������
 * 
 * @author κ����
 * @version 1.0
 * */
public class ImageGetterAsyncTask extends AsyncTask<String, Void, Drawable> {
	private MyBitmapDrawable myDrawable;
	private ReaderActivity readerActivity;
	private String imgUrl;

	public ImageGetterAsyncTask(MyBitmapDrawable myDrawable,
			ReaderActivity readerActivity) {
		this.myDrawable = myDrawable;
		this.readerActivity = readerActivity;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected Drawable doInBackground(String... params) {
		imgUrl = params[0];
		InputStream is = StreamUtil.getInputStreamByUrl(params[0]);
		Drawable drawable = Drawable.createFromStream(is, "src");
		Log.v("doInBackground", drawable.toString());
		return drawable;
	}

	@Override
	protected void onPostExecute(Drawable result) {
		if (result != null) {
			if (MyNetImageCacheManager.getInstance().isExistCachePath() != null) {// ���л���
				MyNetImageCacheManager.getInstance().putLocal(imgUrl,
						ImageUtil.drawableToBitmap(result));
			}
			/*// ע�⣺����Ҫ����ͼƬ����ʾλ�õ���Ϣ,����ͼƬ
			result = ImageUtil.zoomDrawable(result, readerActivity.screenWidth,
					readerActivity.screenHeight, Constant.FOR_BLOG_ICON);
			myDrawable.setBounds(0, 0, result.getIntrinsicWidth(),
					result.getIntrinsicHeight());*/	
			//����˵����ͼƬ�����Ŵ���,����./.....................................................................
			myDrawable.setDrawbale(result);
			//readerActivity.textRead.invalidate();
		}
	}
}
