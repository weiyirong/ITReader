package com.weiyi.itreader.ui;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.weiyi.itreader.adpater.GalleryAdapter;
import com.weiyi.itreader.adpater.ITAdapter;
import com.weiyi.itreader.common.Constant;
import com.weiyi.itreader.common.DownLoadThread;
import com.weiyi.itreader.entity.Category;
import com.weiyi.itreader.entity.ITBlog;
import com.weiyi.itreader.util.ITBlogUtil;
import com.weiyi.itreader.util.MyNetImageCacheManager;
import com.weiyi.itreader.view.MyGallery;
import com.weiyi.itreader.view.MyNetImageView;

/**
 * IT�Ķ�ģ��
 * 
 * @author κ����
 * */
public class ITActivity extends Activity implements OnClickListener {
	Bundle savedInstanceState;
	// ������3D
	MyGallery myGallery;
	GalleryAdapter galleryAdapter;
	// ListView�����ݲ���
	ListView listView;
	// ListViewҳ�Ų���
	View listViewFooter;// ListView�ײ�View
	// Button askMoreButton;// �ײ����ظ��ఴť
	LinearLayout footerLoadProcess;// �ײ���ʾ���ڼ���
	// ������������
	List<ITBlog> data;// sumData������ data��ǰ�б���ʾ������
	List<ITBlog> sumData;
	ITAdapter itAdapter;
	String currentCategoryUrl = Constant.ITARTICLE_CATEGORY_URL;// ��ǰ�������µ�URL,Ĭ��Ϊ����������
	// ����һ��������������
	int pageRowNum = 0;
	// ����ͷ���ȱ�־
	ImageView mProgressBarImage;// ������Բ�ν�����ͼƬ��־
	ProgressBar toolbarProgress;// �������Բ�ν�����������ʾ
	public ImageView toolbarBack;// ������棬Բ�ν�����������Ϻ���ʾ����ImageView,
	public TextView currentCategoryTitle;// ��ǰ�������
	// �ײ������˵�
	// LinearLayout footerMenu;// �ײ������˵�
	TextView footerMain, footerHelp, footerItBuiness, footerItInfo;
	// �������ݻ�ȡ��ϸ��½���
	Handler handler;
	ITBroadcastReceiver broadcastReceiver;
	// �첽���������б�
	BlogListAsyncTask blogListAsyncTask;
	int lastItem = 0;// �����б����һ������
	boolean isLoadmoreOver = true;// ���ظ��ද���Ƿ����

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.savedInstanceState = savedInstanceState;
		setContentView(R.layout.it_layout);
		data = new ArrayList<ITBlog>();
		sumData = new ArrayList<ITBlog>();
		initBroadCast();// ��ʼ���㲥������
		initView();
		addClickListener();
	}

	private void initBroadCast() {
		broadcastReceiver = new ITBroadcastReceiver(this);
		if (MyNetImageCacheManager.getInstance().isExistCachePath() == null) {
			Toast.makeText(this, R.string.not_sdcard, Toast.LENGTH_SHORT)
					.show();
		}
	}

	/**
	 * ��ʼ��View,��δCategory���������������
	 * */
	public void initView() {
		// ������3DЧ������
		myGallery = (MyGallery) findViewById(R.id.novel_gallery);
		// Ϊ�������������
		List<Category> categorys = new ArrayList<Category>();
		categorys.add(new Category(Constant.ITARTICLE_CATEGORY_TITLE,
				Constant.ITARTICLE_CATEGORY_URL));
		categorys.add(new Category(Constant.ITPROJECT_CATEGORY_TITLE,
				Constant.ITPROJECT_CATEGORY_URL));
		categorys.add(new Category(Constant.ITJOB_CATEGORY_TITLE,
				Constant.ITJOB_CATEGORY_URL));
		categorys.add(new Category(Constant.ITINFO_CATEGORY_TITLE,
				Constant.ITINFO_CATEGORY_URL));
		categorys.add(new Category(Constant.ITCELEBRITY_CATEGORY_TITLE,
				Constant.ITCELEBRITY_CATEGORY_URL));
		categorys.add(new Category(Constant.ITCLASS_CATEGORY_TITLE,
				Constant.ITCLASS_CATEGORY_URL));
		categorys.add(new Category(Constant.ITBUSINESS_CATEGORY_TITLE,
				Constant.ITBUSINESS_CATEGORY_URL));
		categorys.add(new Category(Constant.ITRELAX_CATEGORY_TITLE,
				Constant.ITRELAX_CATEGORY_URL));
		galleryAdapter = new GalleryAdapter(categorys, this);
		myGallery.setAdapter(galleryAdapter);
		// ��ȡ�ײ������˵�View
		initFooterMenuView(Constant.IT_MAIN_PAGE);
		mProgressBarImage = (ImageView) findViewById(R.id.toolbar_progress_image);
		// ������Ϣ�����½���
		handler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case Constant.LOAD_MORE_FINISH:
					hiddenProgress();
					listView.smoothScrollToPosition(lastItem + 1);
					itAdapter.notifyDataSetChanged();
					footerLoadProcess.setVisibility(LinearLayout.GONE);
					break;
				case Constant.LOAD_BLOG_AFTER_CLICK:
					Bundle bundle = msg.getData();
					initAfterClickView(bundle.get("typeUrl").toString(), bundle
							.get("typeTitle").toString());
					break;
				}
			}
		};
	}

	private void initFooterMenuView(String selectedPage) {
		footerMain = (TextView) findViewById(R.id.footer_main);
		footerHelp = (TextView) findViewById(R.id.footer_help);
		footerItInfo = (TextView) findViewById(R.id.footer_it_info);
		footerItBuiness = (TextView) findViewById(R.id.footer_it_business);
		if (selectedPage.equals(Constant.IT_MAIN_PAGE)) {
			footerMain.setTextColor(Color.BLACK);
		} else if (selectedPage.equals(Constant.IT_HELP_PAGE)) {
			footerHelp.setTextColor(Color.BLACK);
		} else if (Constant.ITINFO_CATEGORY_TITLE.equals(selectedPage)) {
			footerItInfo.setTextColor(Color.BLACK);
		} else if (Constant.ITBUSINESS_CATEGORY_TITLE.equals(selectedPage)) {
			footerItBuiness.setTextColor(Color.BLACK);
					
		}
	}

	private void changeFooterViewBg() {
		footerMain.setTextColor(Color.GRAY);
		footerHelp.setTextColor(Color.GRAY);
		footerItInfo.setTextColor(Color.GRAY);
		footerItBuiness.setTextColor(Color.GRAY);
	}

	/**
	 * ���ĳһ�������º�������ʾ��ʼ���ؽ�����
	 * */
	public void showProgress() {
		if (toolbarProgress != null)
			toolbarProgress.setVisibility(ProgressBar.VISIBLE);
		if (mProgressBarImage != null)
			mProgressBarImage.setVisibility(ImageView.GONE);
		if (toolbarBack != null)
			toolbarBack.setVisibility(ImageView.GONE);
		if (footerLoadProcess != null)
			footerLoadProcess.setVisibility(TextView.VISIBLE);

	}

	/**
	 * ���ĳһ����������ʾ��Ϻ�������ʾ���ؽ���������������
	 * */
	public void hiddenProgress() {
		if (toolbarProgress != null)
			toolbarProgress.setVisibility(ProgressBar.GONE);
		if (toolbarBack != null)
			toolbarBack.setVisibility(ImageView.VISIBLE);
		if (footerLoadProcess != null)
			footerLoadProcess.setVisibility(TextView.GONE);

	}

	/**
	 * ע���������¼�
	 * */
	public void addClickListener() {
		myGallery.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long arg3) {
				new DownLoadThread().start();
				Log.v("Gallery ClickListener", position + "");
				MyNetImageView myView = (MyNetImageView) view;
				String typeUrl = myView.getCategory().getUrl();
				String typeTitle = myView.getCategory().getTitle();
				// ����Handler��Ϣ
				Message msg = new Message();
				Bundle bundle = new Bundle();
				bundle.putCharSequence("typeUrl", typeUrl);
				bundle.putCharSequence("typeTitle", typeTitle);
				msg.setData(bundle);
				msg.what = Constant.LOAD_BLOG_AFTER_CLICK;
				handler.sendMessage(msg);// ���Ϳ�ʼ������ʾ
			}
		});

		footerHelp.setOnClickListener(this);
		footerItBuiness.setOnClickListener(this);
		footerItInfo.setOnClickListener(this);
		footerMain.setOnClickListener(this);
	}

	/**
	 * �����������Ķ���ĸ��ݴ���Ĳ����ж������ַ�����Ķ�,������Ӧ������װ��
	 * 
	 * @param typeUrl
	 *            ���������URL
	 * @param typeTitle
	 *            �������ͱ���
	 * */
	public void initAfterClickView(String typeUrl, String typeTitle) {
		setContentView(R.layout.it_blog);
		initFooterMenuView(typeTitle);
		// ���ԭ������
		clear();
		// ��ȡͷ�����ȱ���
		toolbarBack = (ImageView) findViewById(R.id.toolbar_back);
		toolbarProgress = (ProgressBar) findViewById(R.id.toolbar_progress);
		currentCategoryTitle = (TextView) findViewById(R.id.it_category_title);
		// ��ȡ�б�ListView
		listView = (ListView) findViewById(R.id.list_view);
		// ��ʼ���ײ�����View,Ҫ����������Ч֮ǰ
		listViewFooter = View.inflate(listView.getContext(),
				R.layout.listview_footer, null);
		footerLoadProcess = (LinearLayout) listViewFooter
				.findViewById(R.id.footer_load_process);
		listView.addFooterView(listViewFooter);
		itAdapter = new ITAdapter(ITActivity.this, data);
		listView.setAdapter(itAdapter);
		addListener();
		// ��ʼ��ListView��Data����
		currentCategoryUrl = typeUrl;// ���õ�ǰ�������µ�URL
		currentCategoryTitle.setText(typeTitle);// ���õ�ǰ�������µı�������
		blogListAsyncTask = new BlogListAsyncTask();
		blogListAsyncTask.execute(typeUrl);// �첽���������б�
	}

	private void clear() {
		sumData.removeAll(sumData);
		data.removeAll(data);
		if (itAdapter != null)
			itAdapter.notifyDataSetChanged();
		if (blogListAsyncTask != null) {
			blogListAsyncTask.cancel(true);
			Log.v("clear.....", blogListAsyncTask.isCancelled() + "");
		}
	}

	/**
	 * ע���Ҫ�ļ�����,ListView��toolbarBack������
	 * */
	public void addListener() {
		// ΪtoolbarBackע�������
		toolbarBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onCreate(savedInstanceState);
			}
		});
		// ΪListViewע�������
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long id) {
				Log.v("onItemClick", "onItemClickListener" + id + ".."
						+ position);
				if (position >= data.size()) {
					Toast.makeText(ITActivity.this, R.string.wait_load,
							Toast.LENGTH_SHORT).show();
					return;
				}
				new DownLoadThread().start();
				if (data.get(position) != null) {
					ITBlog itBlog = data.get(position);
					Intent intent = new Intent(ITActivity.this,
							ReaderActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("ITBlog", itBlog);
					intent.putExtras(bundle);
					startActivity(intent);
				}
			}
		});
		// ����ListView��������
		listView.setOnScrollListener(new OnScrollListener() {
			int sumItemCount = 0;

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
						&& lastItem == itAdapter.getCount()) {
					Log.v("onScrollStateChanged", scrollState + "adapter"
							+ itAdapter.getCount());
					if ((data.size() == pageRowNum
							|| data.size() == sumData.size() || sumItemCount == sumData
							.size() + 1)
							&& (blogListAsyncTask.getStatus() != AsyncTask.Status.RUNNING)
							&& isLoadmoreOver) {// ��ǰ�첽�̲߳�������
						listView.removeFooterView(listViewFooter);
						Toast.makeText(ITActivity.this, R.string.load_over,
								Toast.LENGTH_SHORT).show();
						return;
					}
					if (blogListAsyncTask.getStatus() != AsyncTask.Status.RUNNING
							&& isLoadmoreOver) {
						footerLoadProcess.setVisibility(LinearLayout.VISIBLE);
						listView.smoothScrollToPosition(lastItem + 2);
						itAdapter.notifyDataSetChanged();
						new Thread() {
							public void run() {
								isLoadmoreOver = false;
								// ģ����ظ���ȴ�����
								try {
									Thread.sleep(1000);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								loadMoreData();
							}
						}.start();
					}
					if (blogListAsyncTask.getStatus() == AsyncTask.Status.RUNNING
							|| !isLoadmoreOver) {
						Toast.makeText(ITActivity.this,
								R.string.loading_can_not_page,
								Toast.LENGTH_SHORT).show();
					}
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				Log.v("onScroll", firstVisibleItem + ".." + lastItem);
				lastItem = firstVisibleItem + visibleItemCount - 1;
				sumItemCount = totalItemCount;
				if (!isLoadmoreOver)
					footerLoadProcess.setVisibility(LinearLayout.VISIBLE);
			}
		});
		// �ײ�������ע��
		footerHelp.setOnClickListener(this);
		footerItBuiness.setOnClickListener(this);
		footerItInfo.setOnClickListener(this);
		footerMain.setOnClickListener(this);
	}

	/**
	 * ����ʣ�����ݣ����ظ��������
	 * */
	public void loadMoreData() {
		Log.v("loadMoreData1", itAdapter.getCount() + "");
		int count = itAdapter.getCount();
		if (count + Constant.LOAD_ROW_TIME < pageRowNum) {
			// ÿ�μ���Constant.LOAD_ROW_TIME ��
			for (int i = count; i < count + Constant.LOAD_ROW_TIME; i++) {
				if (sumData.size() > i)
					data.add(sumData.get(i));
				Log.v("loadMoreData2", itAdapter.getCount() + "");
				// itAdapter.notifyDataSetChanged();
				// listView.requestFocusFromTouch(); ,���������������ListView���ô˷���
			}
		} else {
			// �����Ѿ�����5��
			for (int i = count; i < pageRowNum; i++) {
				if (sumData.size() > i)
					data.add(sumData.get(i));
				Log.v("loadMoreData3", itAdapter.getCount() + "");
				// itAdapter.notifyDataSetChanged();
				// listView.requestFocusFromTouch(); ,���������������ListView���ô˷���
			}
		}
		isLoadmoreOver = true;// ���ظ���
		handler.sendEmptyMessage(Constant.LOAD_MORE_FINISH);
		// footerLoadProcess.setVisibility(LinearLayout.GONE);
		// hiddenProgress();
	}

	/**
	 * ����������������Ӧ�¼�
	 * */
	class MyClickListener implements OnClickListener {
		private String typeUrl;// ���������URL���������ͣ�
		private String typeTitle;// �������±��⣨�������ͣ�

		public MyClickListener(String typeUrl, String typeTitle) {
			this.typeUrl = typeUrl;
			this.typeTitle = typeTitle;
		}

		@Override
		public void onClick(View v) {
			initAfterClickView(typeUrl, typeTitle);
		}
	}

	// �ٰ�һ�η��ؼ��˳����򡱵�ʵ��
	private long exitTime = 0;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			onCreate(this.savedInstanceState);
			hiddenProgress();
			if (System.currentTimeMillis() - exitTime > 2000) {
				Toast.makeText(this, R.string.exit_hint, Toast.LENGTH_SHORT)
						.show();
				exitTime = System.currentTimeMillis();
			} else {
				finish();
				android.os.Process.killProcess(android.os.Process.myPid());
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * �첽���أɣԣ£��������б�
	 * */
	class BlogListAsyncTask extends AsyncTask<String, Integer, List<ITBlog>> {

		@Override
		protected void onPreExecute() {
			showProgress();
			super.onPreExecute();
		}

		@Override
		protected List<ITBlog> doInBackground(String... params) {
			List<ITBlog> itBlogs = new ArrayList<ITBlog>();
			try {
				Document doc = Jsoup.connect(params[0]).get();
				Elements titles = doc.getElementsByClass(
						Constant.ITBLOG_TITLE_CLASS).tagName("a");// ��ȡ����class=link_title�ı�ǩԪ��
				Elements dates = doc
						.getElementsByClass(Constant.ITBlOG_DATE_CLASS);
				for (int i = 0; i < titles.size(); ++i) {
					if (!isCancelled()) {
						String blogUrl = Constant.ITBLOG_URL
								+ titles.get(i).child(0).attributes()
										.get("href");// ÿƪ���µ�URL
						String iconUrl = ITBlogUtil
								.getIconUrlByBlogUrl(blogUrl);
						ITBlog itBlog = new ITBlog();
						if (iconUrl != null) {
							itBlog.setIconUrl(iconUrl);// ����ÿƪ���µ�ͷͼ��URL
						}
						if (titles
								.get(i)
								.text()
								.trim()
								.regionMatches(0,
										Constant.SPECIAL_TITLE_UPDATE, 0,
										Constant.SPECIAL_TITLE_UPDATE.length())) {// �ж��Ƿ�Ϊ��������±���--IT�Ķ�����֪ͨ
							String url = titles
									.get(i)
									.text()
									.substring(
											Constant.SPECIAL_TITLE_UPDATE
													.length());
							Log.v("is sepcial title", "yes" + url);
							sendBroadcast(new Intent(
									Constant.UPDATE_ITREADER_NOTIFY).putExtra(
									"updateUrl", url));// ���͹㲥֪ͨ��ʾ
							continue;// ������������б�
						}
						itBlog.setTilte(titles.get(i).text());// ��ȡa��ǩ�ڵ��ı��������±���
						itBlog.setDate(dates.get(i).text());// ��ȡ���·�������
						itBlog.setUrl(blogUrl);// ��ȡ����������href��ֵ
						if (data.size() < Constant.FIRST_ROW_NUM)
							data.add(itBlog);// ��ʼ��ʾ������
						// �����������ģ��һ�μ��ض�������Ч��
						publishProgress((int) ((float) i / titles.size()) * 100);// ���½���
						itBlogs.add(itBlog);
						++pageRowNum;
					} else {// ����ȡ��
						return null;
					}
				}
			} catch (Exception e) {
				sendBroadcast(new Intent(Constant.SOCKETTIMEOUT));
				e.printStackTrace();
			}
			return itBlogs;
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			Log.v("onProgressUpdate....", isCancelled() + "");
			if (isCancelled()) {// ����߳���ȡ���������ݲ�����
				sumData.removeAll(sumData);
				data.removeAll(data);
				if (itAdapter != null)
					itAdapter.notifyDataSetChanged();
				return;
			}
			if (data.size() <= Constant.FIRST_ROW_NUM) {// ��һ�μ��ؼ���
				itAdapter.notifyDataSetChanged();
			}
			super.onProgressUpdate(values);
		}

		@Override
		protected void onPostExecute(List<ITBlog> result) {
			Log.v("onPostExecute....", isCancelled() + "");
			if (isCancelled() || result == null) {// ����߳���ȡ���������ݲ�����
				itAdapter.notifyDataSetChanged();
				return;
			}
			if (result != null && result.size() == 0) {
				itAdapter.notifyDataSetChanged();
				Toast.makeText(ITActivity.this, R.string.not_data,
						Toast.LENGTH_SHORT).show();
			}
			// �������������б�ȫ���������
			initSumData(result);
			if (itAdapter != null)
				itAdapter.notifyDataSetChanged();
			hiddenProgress();
			footerLoadProcess.setVisibility(LinearLayout.GONE);
			super.onPostExecute(result);
		}

		/**
		 * ��ʼ��ListView���б�����
		 * 
		 * @param lists
		 *            ��ʼ����һ������µ�����
		 * */
		private void initSumData(List<ITBlog> lists) {
			Log.v("initDataList......", lists.size() + "");
			// ���ԭ�������б�
			sumData.removeAll(sumData);
			sumData = lists;
			pageRowNum = sumData.size();// �ܼ�¼��
		}

		@Override
		protected void onCancelled() {
			sumData.removeAll(sumData);
			data.removeAll(data);
			itAdapter.notifyDataSetChanged();
			Log.v("onCancelled......", data.size() + "");
			super.onCancelled();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.footer_main:
			changeFooterViewBg();
			footerMain.setTextColor(Color.RED);
			onCreate(savedInstanceState);
			break;
		case R.id.footer_help:
			changeFooterViewBg();
			startActivity(new Intent(this, HelpActivity.class));
			break;
		case R.id.footer_it_info:
			changeFooterViewBg();
			footerItInfo.setTextColor(Color.RED);
			initAfterClickView(Constant.ITINFO_CATEGORY_URL,
					Constant.ITINFO_CATEGORY_TITLE);
			break;
		case R.id.footer_it_business:
			changeFooterViewBg();
			footerItBuiness.setTextColor(Color.RED);
			initAfterClickView(Constant.ITBUSINESS_CATEGORY_URL,
					Constant.ITBUSINESS_CATEGORY_TITLE);
			break;
		}
	}
}
