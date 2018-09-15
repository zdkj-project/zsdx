package com.zd.school.te;

import com.zd.core.util.JsonBuilder;

import java.util.List;

/**
 * @description
 * @author yz
 * @date 2018/9/14 11:50
 * @method
 */
public class TeQrCodeForApp<T> {

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

	public TeQrCodeForApp(String message, boolean result, List<Object> data) {
		this.message = message;
		this.result = result;
		this.data = data;
	}

	public TeQrCodeForApp() {
	}

	public static TeQrCodeForApp<?> fromJson(String json) {
		TeQrCodeForApp<?> result = (TeQrCodeForApp<?>) JsonBuilder.getInstance().fromJson(json, TeQrCodeForApp.class);
		return result;
	}

	public static TeQrCodeForApp<?> fromJson(String json, Class<?>... elementClasses) {
		TeQrCodeForApp<?> result = (TeQrCodeForApp<?>) JsonBuilder.getInstance().fromJson(json, TeQrCodeForApp.class, elementClasses);
		return result;
	}

}
