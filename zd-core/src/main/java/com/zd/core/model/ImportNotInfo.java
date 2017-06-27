package com.zd.core.model;

import com.zd.core.annotation.FieldInfo;

public class ImportNotInfo {

    @FieldInfo(name = "序号")
    private  Integer orderIndex;

    @FieldInfo(name = "标题")
    private  String  title;
    
    @FieldInfo(name = "出错级别")		//警告、错误
    private  String  errorLevel;
    
    @FieldInfo(name = "出错信息")
    private  String  errorInfo;
    
    
    public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	

	public String getErrorLevel() {
		return errorLevel;
	}

	public void setErrorLevel(String errorLevel) {
		this.errorLevel = errorLevel;
	}

	public String getErrorInfo() {
		return errorInfo;
	}

	public void setErrorInfo(String errorInfo) {
		this.errorInfo = errorInfo;
	}

    public Integer getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(Integer orderIndex) {
        this.orderIndex = orderIndex;
    }
    
    
}
