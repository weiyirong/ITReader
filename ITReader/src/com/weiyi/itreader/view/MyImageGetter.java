package com.weiyi.itreader.view;

import java.net.URL;

import com.weiyi.itreader.common.Constant;
import com.weiyi.itreader.common.ImageGetterAsyncTask;
import com.weiyi.itreader.ui.ReaderActivity;
import com.weiyi.itreader.util.ImageUtil;
import com.weiyi.itreader.util.MyNetImageCacheManager;
import com.weiyi.itreader.ui.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html.ImageGetter;
import android.util.Log;
import android.widget.TextView;

/**
 * TextView中显示图片的ImageGetter实现
 * 
 * @author 魏艺荣
 * @version 1.0
 * */
public class MyImageGetter implements ImageGetter {
	private ReaderActivity context;
	private TextView textRead;

	public MyImageGetter(TextView textRead, ReaderActivity context) {
		this.context = context;
		this.textRead = textRead;
	}

	@Override
	public Drawable getDrawable(String source) {
		Log.v("source", source);
		Drawable drawable = null;
		MyBitmapDrawable newDrawable = new MyBitmapDrawable();
		try {
			if (MyNetImageCacheManager.getInstance().isBitmapExistInLocal(source)) {// 存在本地缓存
				Bitmap bitmap = MyNetImageCacheManager.getInstance().getBitmapFromLocal(source);
				drawable = new BitmapDrawable(context.getResources(), bitmap);// 得到Drawable
				// 注意：还需要设置图片的显示位置等信息,缩放图片
				newDrawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
						drawable.getIntrinsicHeight());// 相当于设置Canvas
				newDrawable.setDrawbale(drawable);
				textRead.invalidate();// to call draw(Canvas canvas)
			} else {// 从网络载入此图片
				new ImageGetterAsyncTask(newDrawable, context).execute(source);
				// 加载本地图片，提示正在加载
				Drawable localDrawable = ImageUtil
						.getDefaultDrawableForTextRead(context);
				newDrawable.setBounds(0, 0, localDrawable.getIntrinsicWidth(),
						localDrawable.getIntrinsicHeight());
				newDrawable.setDrawbale(localDrawable);
				textRead.invalidate();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		Log.v("newDrawable", newDrawable.toString());
		return newDrawable;
	}
}
