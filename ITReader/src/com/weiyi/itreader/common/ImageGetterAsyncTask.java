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
 * 功能：异步下载图片Drawable，其中入口参数为图片 URL，出口参数为Drawable对象,
 * 主要是为了处理在TextView显示网络图片时界面黑屏问题
 * 
 * @author 魏艺荣
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
			if (MyNetImageCacheManager.getInstance().isExistCachePath() != null) {// 进行缓存
				MyNetImageCacheManager.getInstance().putLocal(imgUrl,
						ImageUtil.drawableToBitmap(result));
			}
			/*// 注意：还需要设置图片的显示位置等信息,缩放图片
			result = ImageUtil.zoomDrawable(result, readerActivity.screenWidth,
					readerActivity.screenHeight, Constant.FOR_BLOG_ICON);
			myDrawable.setBounds(0, 0, result.getIntrinsicWidth(),
					result.getIntrinsicHeight());*/	
			//更新说明：图片不缩放处理,待定./.....................................................................
			myDrawable.setDrawbale(result);
			//readerActivity.textRead.invalidate();
		}
	}
}
