package com.weiyi.itreader.entity;

/**
 * 封装一个类别,主要为Gallery布局封装数据
 * 
 * @author 魏艺荣
 * @version 1.0
 * */
public class Category {
	private String title;// 所属类别
	private String url;// 地址

	public Category(String title, String url) {
		super();
		this.title = title;
		this.url = url;
	}

	public String getTitle() {
		return title;
	}

	public void setCategory(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
