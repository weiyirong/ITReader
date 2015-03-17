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
 * IT阅读模块
 * 
 * @author 魏艺荣
 * */
public class ITActivity extends Activity implements OnClickListener {
	Bundle savedInstanceState;
	// 主界面3D
	MyGallery myGallery;
	GalleryAdapter galleryAdapter;
	// ListView主内容布局
	ListView listView;
	// ListView页脚布局
	View listViewFooter;// ListView底部View
	// Button askMoreButton;// 底部加载更多按钮
	LinearLayout footerLoadProcess;// 底部提示正在加载
	// 适配器及数据
	List<ITBlog> data;// sumData总数据 data当前列表显示的数据
	List<ITBlog> sumData;
	ITAdapter itAdapter;
	String currentCategoryUrl = Constant.ITARTICLE_CATEGORY_URL;// 当前分类文章的URL,默认为技术文章类
	// 设置一个最大的数据条数
	int pageRowNum = 0;
	// 标题头进度标志
	ImageView mProgressBarImage;// 主界面圆形进度条图片标志
	ProgressBar toolbarProgress;// 分类界面圆形进度条加载显示
	public ImageView toolbarBack;// 分类界面，圆形进度条加载完毕后显示返回ImageView,
	public TextView currentCategoryTitle;// 当前分类标题
	// 底部操作菜单
	// LinearLayout footerMenu;// 底部操作菜单
	TextView footerMain, footerHelp, footerItBuiness, footerItInfo;
	// 处理数据获取完毕更新界面
	Handler handler;
	ITBroadcastReceiver broadcastReceiver;
	// 异步加载文章列表
	BlogListAsyncTask blogListAsyncTask;
	int lastItem = 0;// 文章列表最后一项索引
	boolean isLoadmoreOver = true;// 加载更多动作是否完成

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.savedInstanceState = savedInstanceState;
		setContentView(R.layout.it_layout);
		data = new ArrayList<ITBlog>();
		sumData = new ArrayList<ITBlog>();
		initBroadCast();// 初始化广播接收器
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
	 * 初始化View,并未Category添加适配器及数据
	 * */
	public void initView() {
		// 主界面3D效果布局
		myGallery = (MyGallery) findViewById(R.id.novel_gallery);
		// 为适配器添加数据
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
		// 获取底部操作菜单View
		initFooterMenuView(Constant.IT_MAIN_PAGE);
		mProgressBarImage = (ImageView) findViewById(R.id.toolbar_progress_image);
		// 接受消息，更新界面
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
	 * 点击某一分类文章后，设置显示开始加载进度条
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
	 * 点击某一分类文章显示完毕后设置显示加载结束进度条的隐藏
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
	 * 注册点击监听事件
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
				// 发送Handler消息
				Message msg = new Message();
				Bundle bundle = new Bundle();
				bundle.putCharSequence("typeUrl", typeUrl);
				bundle.putCharSequence("typeTitle", typeTitle);
				msg.setData(bundle);
				msg.what = Constant.LOAD_BLOG_AFTER_CLICK;
				handler.sendMessage(msg);// 发送开始加载提示
			}
		});

		footerHelp.setOnClickListener(this);
		footerItBuiness.setOnClickListener(this);
		footerItInfo.setOnClickListener(this);
		footerMain.setOnClickListener(this);
	}

	/**
	 * 点击进入分类阅读后的根据传入的参数判断是那种分类的阅读,进行相应的数据装载
	 * 
	 * @param typeUrl
	 *            分类的类型URL
	 * @param typeTitle
	 *            分类类型标题
	 * */
	public void initAfterClickView(String typeUrl, String typeTitle) {
		setContentView(R.layout.it_blog);
		initFooterMenuView(typeTitle);
		// 清空原先内容
		clear();
		// 获取头部进度变量
		toolbarBack = (ImageView) findViewById(R.id.toolbar_back);
		toolbarProgress = (ProgressBar) findViewById(R.id.toolbar_progress);
		currentCategoryTitle = (TextView) findViewById(R.id.it_category_title);
		// 获取列表ListView
		listView = (ListView) findViewById(R.id.list_view);
		// 初始化底部布局View,要在适配器生效之前
		listViewFooter = View.inflate(listView.getContext(),
				R.layout.listview_footer, null);
		footerLoadProcess = (LinearLayout) listViewFooter
				.findViewById(R.id.footer_load_process);
		listView.addFooterView(listViewFooter);
		itAdapter = new ITAdapter(ITActivity.this, data);
		listView.setAdapter(itAdapter);
		addListener();
		// 初始化ListView的Data数据
		currentCategoryUrl = typeUrl;// 设置当前分类文章的URL
		currentCategoryTitle.setText(typeTitle);// 设置当前分类文章的标题名称
		blogListAsyncTask = new BlogListAsyncTask();
		blogListAsyncTask.execute(typeUrl);// 异步加载文章列表
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
	 * 注册必要的监听器,ListView和toolbarBack监听器
	 * */
	public void addListener() {
		// 为toolbarBack注册监听器
		toolbarBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onCreate(savedInstanceState);
			}
		});
		// 为ListView注册监听器
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
		// 设置ListView滚动监听
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
							&& isLoadmoreOver) {// 当前异步线程不在运行
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
								// 模拟加载更多等待画面
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
		// 底部监听器注册
		footerHelp.setOnClickListener(this);
		footerItBuiness.setOnClickListener(this);
		footerItInfo.setOnClickListener(this);
		footerMain.setOnClickListener(this);
	}

	/**
	 * 加载剩余数据，加载更多的数据
	 * */
	public void loadMoreData() {
		Log.v("loadMoreData1", itAdapter.getCount() + "");
		int count = itAdapter.getCount();
		if (count + Constant.LOAD_ROW_TIME < pageRowNum) {
			// 每次加载Constant.LOAD_ROW_TIME 条
			for (int i = count; i < count + Constant.LOAD_ROW_TIME; i++) {
				if (sumData.size() > i)
					data.add(sumData.get(i));
				Log.v("loadMoreData2", itAdapter.getCount() + "");
				// itAdapter.notifyDataSetChanged();
				// listView.requestFocusFromTouch(); ,如果加载完点击不了ListView调用此方法
			}
		} else {
			// 数据已经不足5条
			for (int i = count; i < pageRowNum; i++) {
				if (sumData.size() > i)
					data.add(sumData.get(i));
				Log.v("loadMoreData3", itAdapter.getCount() + "");
				// itAdapter.notifyDataSetChanged();
				// listView.requestFocusFromTouch(); ,如果加载完点击不了ListView调用此方法
			}
		}
		isLoadmoreOver = true;// 加载更多
		handler.sendEmptyMessage(Constant.LOAD_MORE_FINISH);
		// footerLoadProcess.setVisibility(LinearLayout.GONE);
		// hiddenProgress();
	}

	/**
	 * 点击进入分类文章响应事件
	 * */
	class MyClickListener implements OnClickListener {
		private String typeUrl;// 分类的文章URL（文章类型）
		private String typeTitle;// 分类文章标题（文章类型）

		public MyClickListener(String typeUrl, String typeTitle) {
			this.typeUrl = typeUrl;
			this.typeTitle = typeTitle;
		}

		@Override
		public void onClick(View v) {
			initAfterClickView(typeUrl, typeTitle);
		}
	}

	// 再按一次返回键退出程序”的实现
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
	 * 异步加载ＩＴＢｌｏｇ文章列表
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
						Constant.ITBLOG_TITLE_CLASS).tagName("a");// 获取所有class=link_title的标签元素
				Elements dates = doc
						.getElementsByClass(Constant.ITBlOG_DATE_CLASS);
				for (int i = 0; i < titles.size(); ++i) {
					if (!isCancelled()) {
						String blogUrl = Constant.ITBLOG_URL
								+ titles.get(i).child(0).attributes()
										.get("href");// 每篇文章的URL
						String iconUrl = ITBlogUtil
								.getIconUrlByBlogUrl(blogUrl);
						ITBlog itBlog = new ITBlog();
						if (iconUrl != null) {
							itBlog.setIconUrl(iconUrl);// 设置每篇文章的头图标URL
						}
						if (titles
								.get(i)
								.text()
								.trim()
								.regionMatches(0,
										Constant.SPECIAL_TITLE_UPDATE, 0,
										Constant.SPECIAL_TITLE_UPDATE.length())) {// 判断是否为特殊的文章标题--IT阅读更新通知
							String url = titles
									.get(i)
									.text()
									.substring(
											Constant.SPECIAL_TITLE_UPDATE
													.length());
							Log.v("is sepcial title", "yes" + url);
							sendBroadcast(new Intent(
									Constant.UPDATE_ITREADER_NOTIFY).putExtra(
									"updateUrl", url));// 发送广播通知提示
							continue;// 不将此项加入列表
						}
						itBlog.setTilte(titles.get(i).text());// 获取a标签内的文本，即文章标题
						itBlog.setDate(dates.get(i).text());// 获取文章发表日期
						itBlog.setUrl(blogUrl);// 获取超链接属性href的值
						if (data.size() < Constant.FIRST_ROW_NUM)
							data.add(itBlog);// 开始显示的数据
						// 设置随机数，模拟一次加载多条数据效果
						publishProgress((int) ((float) i / titles.size()) * 100);// 更新进度
						itBlogs.add(itBlog);
						++pageRowNum;
					} else {// 任务取消
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
			if (isCancelled()) {// 如果线程已取消，则数据不更新
				sumData.removeAll(sumData);
				data.removeAll(data);
				if (itAdapter != null)
					itAdapter.notifyDataSetChanged();
				return;
			}
			if (data.size() <= Constant.FIRST_ROW_NUM) {// 第一次加载几行
				itAdapter.notifyDataSetChanged();
			}
			super.onProgressUpdate(values);
		}

		@Override
		protected void onPostExecute(List<ITBlog> result) {
			Log.v("onPostExecute....", isCancelled() + "");
			if (isCancelled() || result == null) {// 如果线程已取消，则数据不更新
				itAdapter.notifyDataSetChanged();
				return;
			}
			if (result != null && result.size() == 0) {
				itAdapter.notifyDataSetChanged();
				Toast.makeText(ITActivity.this, R.string.not_data,
						Toast.LENGTH_SHORT).show();
			}
			// 所有网络文章列表全部加载完毕
			initSumData(result);
			if (itAdapter != null)
				itAdapter.notifyDataSetChanged();
			hiddenProgress();
			footerLoadProcess.setVisibility(LinearLayout.GONE);
			super.onPostExecute(result);
		}

		/**
		 * 初始化ListView的列表数据
		 * 
		 * @param lists
		 *            初始化哪一类的文章的数据
		 * */
		private void initSumData(List<ITBlog> lists) {
			Log.v("initDataList......", lists.size() + "");
			// 清空原先文章列表
			sumData.removeAll(sumData);
			sumData = lists;
			pageRowNum = sumData.size();// 总记录数
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
