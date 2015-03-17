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
	public int screenWidth, screenHeight;// ���ߺ�����
	
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
		// ��ȡ���ߺ�����screenWidth,screenHeight
		DisplayMetrics displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		screenHeight = displayMetrics.heightPixels;
		screenWidth = displayMetrics.widthPixels;

		scrollView = (ScrollView) findViewById(R.id.scroll);
		toolbarBack = (ImageView) findViewById(R.id.toolbar_back);
		toolbarProgress = (ProgressBar) findViewById(R.id.toolbar_progress);
		//textRead = (TextView) findViewById(R.id.scrollview_text);// ��������
		webContent = (WebView) findViewById(R.id.webview_text);
		titleRead = (TextView) findViewById(R.id.scrollview_title);// ���±���
		
		webContent.getSettings().setDefaultTextEncodingName("UTF-8");
		webContent.getSettings().setTextSize(TextSize.LARGER);
		Bundle bundle = getIntent().getExtras();
		itBlog = (ITBlog) bundle.get("ITBlog");
		//Ϊ����ImageView��Ӽ���
		toolbarBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ReaderActivity.this.finish();
			}
		});
		//���ù�������toolBar״̬��
		toolbarBack.setVisibility(ImageView.GONE);
		toolbarProgress.setVisibility(ProgressBar.VISIBLE);
		// ��ʾ����
	//	imageGetter = new MyImageGetter(textRead, this);
	//	textRead.setMovementMethod(LinkMovementMethod.getInstance());// ��������
		titleRead.setText(itBlog.getTilte());
		// �첽������������
		new ContentGetAsyncTask(this).execute(itBlog.getUrl());

	}
}
