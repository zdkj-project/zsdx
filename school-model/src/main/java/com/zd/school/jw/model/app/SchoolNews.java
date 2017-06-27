package com.zd.school.jw.model.app;


public class SchoolNews {
	private String newsId;
	private String newsTitle;
	private String newsRemark;
	private String createDate;
	
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getNewsId() {
		return newsId;
	}
	public void setNewsId(String newsId) {
		this.newsId = newsId;
	}
	public String getNewsTitle() {
		return newsTitle;
	}
	public void setNewsTitle(String newsTitle) {
		this.newsTitle = newsTitle;
	}
	public String getNewsRemark() {
		return newsRemark;
	}
	public void setNewsRemark(String newsRemark) {
		this.newsRemark = newsRemark;
	}
	
}
