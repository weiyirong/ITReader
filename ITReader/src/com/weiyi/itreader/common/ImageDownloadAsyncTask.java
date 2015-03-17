package com.weiyi.itreader.common;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpConnection;

import com.weiyi.itreader.ui.ITActivity;
import com.weiyi.itreader.util.ImageUtil;
import com.weiyi.itreader.util.MyNetImageCacheManager;
import com.weiyi.itreader.util.StreamUtil;
import com.weiyi.itreader.view.MyNetImageView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

/**
 * ���ܣ��첽����ͼƬBitmap��������ڲ���ΪͼƬ URL�����ڲ���ΪBitmap���� �������:ͼƬ�ģգң̵�ַ ���ؽ����ͼƬ��Bitmap��ʽ
 * 
 * @author κ����
 * @version 1.0
 * */
public class ImageDownloadAsyncTask extends AsyncTask<String, Void, Bitmap> {
	private String picUrl;
	private MyNetImageView imageView;

	public ImageDownloadAsyncTask(MyNetImageView myNetImageView) {
		this.imageView = myNetImageView;
	}

	@Override
	protected Bitmap doInBackground(String... params) {
		picUrl = params[0];
		return ImageUtil.getBitmapByUrl(picUrl);
	}

	@Override
	protected void onPostExecute(Bitmap result) {
		Log.v("picUrl in downloadImg head", picUrl);
		if (result != null) {
			Log.v("picUrl in downloadImg", picUrl);
			imageView.setBackgroundDrawable(new BitmapDrawable(result));
		}
		super.onPostExecute(result);
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
	}

	@Override
	protected void onProgressUpdate(Void... values) {
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);
	}

}
