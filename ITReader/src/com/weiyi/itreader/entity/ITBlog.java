package com.weiyi.itreader.entity;

import java.io.Serializable;

/**
 * IT阅读实体类
 * 
 * @author 魏艺荣
 * */
public class ITBlog implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer id;// 主键
	private String category;// 类别
	private String iconUrl;// 图标的URL地址
	private String tilte;// 标题
	private String content;// 内容
	private String url;// 文章的地址
	private String date;// 发表日期

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getTilte() {
		return tilte;
	}

	public void setTilte(String tilte) {
		this.tilte = tilte;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

}
