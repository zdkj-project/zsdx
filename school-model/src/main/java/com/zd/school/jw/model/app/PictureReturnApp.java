package com.zd.school.jw.model.app;

import java.util.List;

public class PictureReturnApp {
	private int totalCount;
	private List<PictureForApp> picList;
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	public List<PictureForApp> getPicList() {
		return picList;
	}
	public void setPicList(List<PictureForApp> picList) {
		this.picList = picList;
	}
	
}
