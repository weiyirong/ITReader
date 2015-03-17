package com.weiyi.itreader.view;

import com.weiyi.itreader.common.ImageDownloadAsyncTask;
import com.weiyi.itreader.entity.Category;
import com.weiyi.itreader.entity.ITBlog;
import com.weiyi.itreader.util.ImageUtil;
import com.weiyi.itreader.util.MyNetImageCacheManager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

/**
 * 自定义显示网络图片的ImageView
 * 
 * @author 魏艺荣
 * @version 1.0
 * */
public class MyNetImageView extends ImageView {
	private String imgUrl;// 图片地址
	private Category category;
	
	public MyNetImageView(Context context) {
		super(context);
	}

	public MyNetImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public MyNetImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	/**
	 * 设置图片地址
	 * 
	 * @param url
	 *            图片地址
	 * */
	public void setImgUrl(String url) {
		this.imgUrl = url;
		loadImg(url);
	}

	/**
	 * 为ImageView加载图片
	 * 
	 * @param url
	 * */
	private void loadImg(String url) {
		if (MyNetImageCacheManager.getInstance().isBitmapExistInLocal(url)) {// 是否有缓存
			Log.v("loadImg in MyNetImageView form cache", url);
			Bitmap bitmap = MyNetImageCacheManager.getInstance().getBitmapFromLocal(url);
			this.setBackgroundDrawable(new BitmapDrawable(bitmap));
		} else {// 没有缓存，则从网络下载
			Log.v("loadImg in MyNetImageView form net", url);
			new ImageDownloadAsyncTask(this).execute(url);
		}
	}

	

	public String getImgUrl() {
		return imgUrl;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

}
