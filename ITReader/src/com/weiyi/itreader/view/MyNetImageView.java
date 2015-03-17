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
 * �Զ�����ʾ����ͼƬ��ImageView
 * 
 * @author κ����
 * @version 1.0
 * */
public class MyNetImageView extends ImageView {
	private String imgUrl;// ͼƬ��ַ
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
	 * ����ͼƬ��ַ
	 * 
	 * @param url
	 *            ͼƬ��ַ
	 * */
	public void setImgUrl(String url) {
		this.imgUrl = url;
		loadImg(url);
	}

	/**
	 * ΪImageView����ͼƬ
	 * 
	 * @param url
	 * */
	private void loadImg(String url) {
		if (MyNetImageCacheManager.getInstance().isBitmapExistInLocal(url)) {// �Ƿ��л���
			Log.v("loadImg in MyNetImageView form cache", url);
			Bitmap bitmap = MyNetImageCacheManager.getInstance().getBitmapFromLocal(url);
			this.setBackgroundDrawable(new BitmapDrawable(bitmap));
		} else {// û�л��棬�����������
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
