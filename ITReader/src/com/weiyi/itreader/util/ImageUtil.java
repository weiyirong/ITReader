package com.weiyi.itreader.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.weiyi.itreader.common.Constant;
import com.weiyi.itreader.ui.ReaderActivity;
import com.weiyi.itreader.ui.R;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.graphics.Shader.TileMode;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.NetworkInfo.DetailedState;
import android.util.Log;
import android.view.ViewGroup.MarginLayoutParams;

/**
 * 功能：图片获取、处理工具类
 * 
 * @author 魏艺荣
 * @version 1.0
 * */
public class ImageUtil {
	/**
	 * 根据URL获取网络图片，并转换成Bitmap
	 * 
	 * @param url
	 *            图片资源的URL
	 * @return Bitmap 返回的图片资源位图
	 * */
	public static Bitmap getBitmapByUrl(String url) {
		Log.v("bitmap name", url);
		URL myUrl = null;
		Bitmap myBitmap = null;
		// 字符串转化成URL
		try {
			myUrl = new URL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		// 从网络流中读取图片资源
		try {
			HttpURLConnection con = (HttpURLConnection) myUrl.openConnection();
			con.setDoInput(true);// 以后就可以使用conn.getInputStream().read() ;
			con.connect();
			InputStream is = con.getInputStream();
			myBitmap = BitmapFactory.decodeStream(is);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return myBitmap;
	}

	/**
	 * 将Drawable转换成Bitmap
	 * 
	 * @param drawable
	 *            图片资源Drawable
	 * @return Bitmap 返回的图片资源位图
	 * */
	public static Bitmap drawableToBitmap(Drawable drawable) {
		int width = drawable.getIntrinsicWidth();
		int height = drawable.getIntrinsicHeight();
		Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
				: Bitmap.Config.RGB_565;// 取得drawable颜色
		Bitmap bitmap = Bitmap.createBitmap(width, height, config);// 新建对应的Bitmap
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, width, height);
		drawable.draw(canvas);
		return bitmap;
	}

	/**
	 * 缩放对应的Drawable图片,自适应屏幕
	 * 
	 * @param drawable
	 *            图片资源Drawable
	 * @param screenWidth
	 *            屏幕宽度
	 * @param screenHeight
	 *            屏幕高度
	 * @param type
	 *            缩放后的用途
	 * @return Drawable 返回缩放后的图片
	 * */
	public static Drawable zoomDrawable(Drawable drawable, int screenWidth,
			int screenHeight, String type) {
		int width = drawable.getIntrinsicWidth();
		int height = drawable.getIntrinsicHeight();
		Bitmap bitmap = drawableToBitmap(drawable);
		Matrix matrix = new Matrix();
		// 计算缩放比例
		float scaleWidth = 0, scaleHeight = 0;
		if (Constant.FOR_BLOG_ICON.equals(type)) {// 为自适应屏幕宽高而缩放
			scaleHeight = (float) (screenHeight / 3+30) / height;
			scaleWidth = (float) (screenWidth - 50) / width;
		} else if (Constant.FOR_FOOTER_MENU_BG.equals(type)) {
			scaleHeight = (float) (screenHeight) / height;
			scaleWidth = (float) (scaleWidth) / width;
		}
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height,
				matrix, true);
		return new BitmapDrawable(newBitmap);// 把Bitmap转换成Drawable输出
	}

	/**
	 * 根据传入的Bitmap对象构建带有倒影的Bitmap
	 * 
	 * @param originalBitmap
	 *            图片位图
	 * @return Bitmap 返回带有倒影的位图Bitmap
	 * */
	public static Bitmap createWithReflectedImage(Bitmap originalBitmap) {
		int reflectionGap = 4;// 图片与倒影之间的距离间隔
		int width = originalBitmap.getWidth();
		int height = originalBitmap.getHeight();
		// 变换所需的Matrix,完成 图片旋转，缩放等控制
		Matrix matrix = new Matrix();
		matrix.preScale(1, -1);
		// 获取倒影Bitmap
		Bitmap reflectionBitmap = Bitmap.createBitmap(originalBitmap, 0,
				height / 2, width, height / 2, matrix, false);
		// 获取带倒影的Bitmap.即整体的效果图位图对象
		Bitmap withReflectionBitmap = Bitmap.createBitmap(width, height
				+ height / 2, Config.ARGB_8888);
		/** Bitmap的显示还需要画布Canvas来完成 */
		// 由该位图对象创建初始画布(规定了画布的宽高)
		Canvas canvas = new Canvas(withReflectionBitmap);
		canvas.drawBitmap(originalBitmap, 0, 0, null);
		// 绘制出原图与倒影之间的间隔，用矩形来描绘
		Paint paint1 = new Paint();
		canvas.drawRect(0, height, width, height + reflectionGap, paint1);
		// 绘制出倒影的Bitmap
		canvas.drawBitmap(reflectionBitmap, 0, height + reflectionGap, paint1);
		// 绘制线性渐变对象
		Paint paint2 = new Paint();
		LinearGradient shader = new LinearGradient(0,
				originalBitmap.getHeight(), 0, withReflectionBitmap.getHeight()
						+ reflectionGap, 0x70ffffff, 0x00ffffff, TileMode.CLAMP);
		// 把渐变效果应用在画笔上
		paint2.setShader(shader);
		// 设置倒影的阴影度，使其与原来的图像颜色区别开来，此处显示灰度，会被染上下面的底部的原图片的倒影颜色，实现倒影的修饰
		paint2.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
		// 用设置好的paint2绘制此倒影
		canvas.drawRect(0, height, width, withReflectionBitmap.getHeight()
				+ reflectionGap, paint2);
		return withReflectionBitmap;
	}

	/**
	 * 查看网络文章时，图片未加载完毕时，先使用默认图片，提示正在加载,此方法即获取默认背景图
	 * @param readerActivity
	 * */
	public static Drawable getDefaultDrawableForTextRead(
			ReaderActivity readerActivity) {
		return zoomDrawable(
				new BitmapDrawable(BitmapFactory.decodeResource(
						readerActivity.getResources(),
						R.drawable.textread_loadimg_bg)),
				readerActivity.screenWidth, readerActivity.screenHeight,
				Constant.FOR_BLOG_ICON);
	}
}
