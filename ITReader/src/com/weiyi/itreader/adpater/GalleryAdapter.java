package com.weiyi.itreader.adpater;

import java.util.List;

import com.weiyi.itreader.common.Constant;
import com.weiyi.itreader.entity.Category;
import com.weiyi.itreader.entity.ITBlog;
import com.weiyi.itreader.ui.ITActivity;
import com.weiyi.itreader.util.ITBlogUtil;
import com.weiyi.itreader.util.ImageUtil;
import com.weiyi.itreader.view.MyNetImageView;
import com.weiyi.itreader.ui.R;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class GalleryAdapter extends BaseAdapter {
	List<Category> categorys;
	ITActivity itActivity;

	public GalleryAdapter(List<Category> categorys, ITActivity itActivity) {
		this.categorys = categorys;
		this.itActivity = itActivity;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return categorys.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View view, ViewGroup arg2) {
		Bitmap originalBitmap = getOriginalBitmapByType(categorys.get(position)
				.getTitle());
		Bitmap resultBitmap = ImageUtil
				.createWithReflectedImage(originalBitmap);
		MyNetImageView imageView = new MyNetImageView(itActivity);
		imageView.setImageBitmap(resultBitmap);
		imageView.setScaleType(MyNetImageView.ScaleType.CENTER_INSIDE);
		imageView.setLayoutParams(new Gallery.LayoutParams(resultBitmap
				.getWidth()*2/3, resultBitmap.getHeight()*5/6));
		imageView.setCategory(categorys.get(position));
		return imageView;
	}

	/**
	 * 根据传入的IT文章类型获取对应的源Bitmap，作为Gallery的每项背景
	 * 
	 * @param type
	 *            IT文章类别，如技术文章等
	 * @return
	 * */
	public Bitmap getOriginalBitmapByType(String type) {
		Bitmap originalBitmap = null;
		if (Constant.ITARTICLE_CATEGORY_TITLE.equals(type)) {
			originalBitmap = BitmapFactory.decodeResource(
					itActivity.getResources(), R.drawable.it_article);
		} else if (Constant.ITBUSINESS_CATEGORY_TITLE.equals(type)) {
			originalBitmap = BitmapFactory.decodeResource(
					itActivity.getResources(), R.drawable.it_business);
		} else if (Constant.ITCLASS_CATEGORY_TITLE.equals(type)) {
			originalBitmap = BitmapFactory.decodeResource(
					itActivity.getResources(), R.drawable.it_class);
		} else if (Constant.ITCELEBRITY_CATEGORY_TITLE.equals(type)) {
			originalBitmap = BitmapFactory.decodeResource(
					itActivity.getResources(), R.drawable.it_celebrity);
		} else if (Constant.ITPROJECT_CATEGORY_TITLE.equals(type)) {
			originalBitmap = BitmapFactory.decodeResource(
					itActivity.getResources(), R.drawable.it_project);
		} else if (Constant.ITJOB_CATEGORY_TITLE.equals(type)) {
			originalBitmap = BitmapFactory.decodeResource(
					itActivity.getResources(), R.drawable.it_job);
		} else if (Constant.ITRELAX_CATEGORY_TITLE.equals(type)) {
			originalBitmap = BitmapFactory.decodeResource(
					itActivity.getResources(), R.drawable.it_happy);
		} else if (Constant.ITINFO_CATEGORY_TITLE.equals(type)) {
			originalBitmap = BitmapFactory.decodeResource(
					itActivity.getResources(), R.drawable.it_info);
		} else {
			originalBitmap = BitmapFactory.decodeResource(
					itActivity.getResources(), R.drawable.default_novel_cover);
		}
		return originalBitmap;
	}
}