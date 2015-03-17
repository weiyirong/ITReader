package com.weiyi.itreader.entity;

import java.io.Serializable;

/**
 * IT�Ķ�ʵ����
 * 
 * @author κ����
 * */
public class ITBlog implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer id;// ����
	private String category;// ���
	private String iconUrl;// ͼ���URL��ַ
	private String tilte;// ����
	private String content;// ����
	private String url;// ���µĵ�ַ
	private String date;// ��������

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
