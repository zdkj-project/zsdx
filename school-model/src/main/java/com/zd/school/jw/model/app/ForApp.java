package com.zd.school.jw.model.app;

import com.zd.core.util.JsonBuilder;

import java.util.List;

/**
 * @description
 * @author yz
 * @date 2018/9/14 11:50
 * @method
 */
public class ForApp<T> {

	private String message;

	private boolean result=true;

	private List<Object> data;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	public Object getData() {
		return data;
	}

	public void setData(List<Object> data) {
		this.data = data;
	}

	public ForApp(String message, boolean result, List<Object> data) {
		this.message = message;
		this.result = result;
		this.data = data;
	}

	public ForApp() {
	}

	public static ForApp<?> fromJson(String json) {
		ForApp<?> result = (ForApp<?>) JsonBuilder.getInstance().fromJson(json, ForApp.class);
		return result;
	}

	public static ForApp<?> fromJson(String json, Class<?>... elementClasses) {
		ForApp<?> result = (ForApp<?>) JsonBuilder.getInstance().fromJson(json, ForApp.class, elementClasses);
		return result;
	}

}
