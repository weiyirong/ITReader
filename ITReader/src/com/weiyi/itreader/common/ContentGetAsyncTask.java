package com.weiyi.itreader.common;

import com.weiyi.itreader.ui.ReaderActivity;
import com.weiyi.itreader.util.ITBlogUtil;
import com.weiyi.itreader.util.StringUtil;

import android.os.AsyncTask;
import android.os.Handler;
import android.text.Html;
import android.widget.ImageView;
import android.widget.ProgressBar;

/**
 * 异步加载网络文章内容
 * 
 * @author 魏艺荣
 * @version 1.0
 * */
public class ContentGetAsyncTask extends AsyncTask<String, Void, String> {
	ReaderActivity readerActivity;

	public ContentGetAsyncTask(ReaderActivity readerActivity) {
		this.readerActivity = readerActivity;
	}

	@Override
	protected String doInBackground(String... params) {
		String content = ITBlogUtil.getContentByURL(params[0]);
		return content;
	}

	@Override
	protected void onPostExecute(String result) {
		// String content = StringUtil.stringFilter(result);
		readerActivity.itBlog.setContent(result);
		readerActivity.toolbarBack.setVisibility(ImageView.VISIBLE);
		readerActivity.toolbarProgress.setVisibility(ProgressBar.GONE);
		readerActivity.webContent.loadDataWithBaseURL(null, result,
				"text/html", "utf-8", null);
		super.onPostExecute(result);
	}

}
