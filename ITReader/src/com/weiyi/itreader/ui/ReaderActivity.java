package com.weiyi.itreader.ui;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebSettings.TextSize;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.weiyi.itreader.common.ContentGetAsyncTask;
import com.weiyi.itreader.entity.ITBlog;
import com.weiyi.itreader.view.MyImageGetter;
import com.weiyi.itreader.ui.R;

public class ReaderActivity extends Activity {
	public ITBlog itBlog;
	public int screenWidth, screenHeight;// 屏高和屏宽
	
	ScrollView scrollView;
//	public TextView textRead;
	public WebView webContent;
	public MyImageGetter imageGetter;
	public TextView titleRead;
	public ProgressBar toolbarProgress;
	public ImageView toolbarBack;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.reader_layout);
		setContentView(R.layout.scrollview_layout);
		// 获取屏高和屏宽screenWidth,screenHeight
		DisplayMetrics displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		screenHeight = displayMetrics.heightPixels;
		screenWidth = displayMetrics.widthPixels;

		scrollView = (ScrollView) findViewById(R.id.scroll);
		toolbarBack = (ImageView) findViewById(R.id.toolbar_back);
		toolbarProgress = (ProgressBar) findViewById(R.id.toolbar_progress);
		//textRead = (TextView) findViewById(R.id.scrollview_text);// 文章内容
		webContent = (WebView) findViewById(R.id.webview_text);
		titleRead = (TextView) findViewById(R.id.scrollview_title);// 文章标题
		
		webContent.getSettings().setDefaultTextEncodingName("UTF-8");
		webContent.getSettings().setTextSize(TextSize.LARGER);
		Bundle bundle = getIntent().getExtras();
		itBlog = (ITBlog) bundle.get("ITBlog");
		//为返回ImageView添加监听
		toolbarBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ReaderActivity.this.finish();
			}
		});
		//设置工具条（toolBar状态）
		toolbarBack.setVisibility(ImageView.GONE);
		toolbarProgress.setVisibility(ProgressBar.VISIBLE);
		// 显示内容
	//	imageGetter = new MyImageGetter(textRead, this);
	//	textRead.setMovementMethod(LinkMovementMethod.getInstance());// 设置链接
		titleRead.setText(itBlog.getTilte());
		// 异步加载文章内容
		new ContentGetAsyncTask(this).execute(itBlog.getUrl());

	}
}
