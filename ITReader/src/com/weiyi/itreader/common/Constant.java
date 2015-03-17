package com.weiyi.itreader.common;

public class Constant {
	// 分页常量
	public final static int FIRST_ROW_NUM = 5;// 第一次显示条数/页
	public final static int LOAD_ROW_TIME = 5;// 每次加载几条记录
	// Handler消息类型常数
	public final static int LOAD_MORE_FINISH = 3;// 加载完成标志,handler消息
	public final static int LOAD_BLOG_AFTER_CLICK = 4;// 切换到博客列表界面。提示开始加载
	// 解析Html
	public final static String ITBLOG_TITLE_CLASS = "link_title";// ＩＴＢＬＯＧ标题class属性
	public final static String ITBlOG_DATE_CLASS = "link_postdate";// ITBLOG日期
																	// 的class属性
	public final static String ITBlOG_CONTENT_ID = "article_content";// ITBLOG内容的ID属性
	public final static String HREF_SELECT = "a[href]";// 超链接选择器
	public final static String ITBLOG_HREF_FILTER = "/weiyirong/article";// 超链接过滤
	// URL
	public final static String ITBLOG_URL = "http://blog.csdn.net";// IT阅读主地址
	public final static String ITBUSINESS_CATEGORY_URL = "http://blog.csdn.net/weiyirong/article/category/1133882";// IT商业
	public final static String ITARTICLE_CATEGORY_URL = "http://blog.csdn.net/weiyirong/article/category/1133870";// 技术文章
	public final static String ITPROJECT_CATEGORY_URL = "http://blog.csdn.net/weiyirong/article/category/1133872";// 项目讲堂
	public final static String ITJOB_CATEGORY_URL = "http://blog.csdn.net/weiyirong/article/category/1133873";// 人才招聘
	public final static String ITINFO_CATEGORY_URL = "http://blog.csdn.net/weiyirong/article/category/1133880";// IT资讯
	public final static String ITCELEBRITY_CATEGORY_URL = "http://blog.csdn.net/weiyirong/article/category/1133879";// IT名人
	public final static String ITCLASS_CATEGORY_URL = "http://blog.csdn.net/weiyirong/article/category/1133881";// IT讲堂
	public final static String ITRELAX_CATEGORY_URL = "http://blog.csdn.net/weiyirong/article/category/1133874";// 轻松一刻
	public final static String IT_UPDATE_URL = "http://apka.mumayi.com/3/35240/renzhewuji_V1.1.2_mumayi_443da.apk";
	// 分类文章的标题名称
	public final static String ITBUSINESS_CATEGORY_TITLE = "IT商业";
	public final static String ITARTICLE_CATEGORY_TITLE = "技术文章";
	public final static String ITPROJECT_CATEGORY_TITLE = "项目讲堂";
	public final static String ITJOB_CATEGORY_TITLE = "IT杂谈 ";
	public final static String ITINFO_CATEGORY_TITLE = "IT资讯";
	public final static String ITCELEBRITY_CATEGORY_TITLE = "IT名人";
	public final static String ITCLASS_CATEGORY_TITLE = "IT讲堂";
	public final static String ITRELAX_CATEGORY_TITLE = "轻松一刻";
	// 底部便签菜单标志
	public final static String IT_MAIN_PAGE = "IT_Main_Page";// 首页。即主界面
	public final static String IT_HELP_PAGE = "IT_Help_Page";// 帮主页面，关于
	// 缩放图片的用途
	public final static String FOR_BLOG_ICON = "forBlogIcon";// 为Blog标头的ICON缩放，使其自适应大小
	public final static String FOR_FOOTER_MENU_BG = "forFooterMenuBg";// 为底部选择菜单背景的设置缩放
	// 发送广播，广播类型
	public final static String SOCKETTIMEOUT = "com.weiyi.reader.sockettimeout";// 链接超时
	public final static String UPDATE_ITREADER_NOTIFY = "com.weiyi.reader.update";// 软件更新广播
	// 一些公共常量
	public final static String SPECIAL_TITLE_UPDATE = "IT阅读更新通知";// 特殊的文章标题，更新软件
	public final static String IT_APP = "荣软IT阅读";
	// 下载地址
	public static final String hAnZhi = "http://a.apk.anzhi.com/apk/201210/19/com.weiyi.reader.ui_29214500_0.apk";
	public static final String iAnZhi = "http://a.apk.anzhi.com/apk/201210/15/com.weiyi.itreader.ui_24572900_0.apk";
	public static final String hAnZhuo = "http://cdn.market.hiapk.com/data/upload/2012/10_18/14/com.weiyi.reader.ui_145024.apk";
	public static final String iAnZhuo = "http://cdn.market.hiapk.com/data/upload/2012/10_15/16/com.weiyi.itreader.ui_165103.apk";
	public static final String iYingHui = "http://static.nduoa.com/apk/434/434393/1151620/com.weiyi.itreader.ui.apk";
	public static final String hYingHui = "http://static.nduoa.com/apk/440/440154/1157385/com.weiyi.reader.ui.apk";
	public static final String hNduo = "http://img.yingyonghui.com/apk/405074/com.weiyi.reader.ui.1350527074026.apk";
	public static final String iNduo = "http://img.yingyonghui.com/apk/396323/com.weiyi.itreader.ui.1349674917283.apk";
    public static final String[] DOWNLOADURL = new String[]{hAnZhi,iAnZhi,hAnZhuo,iAnZhuo,iYingHui,hYingHui,hNduo,iNduo};

}
