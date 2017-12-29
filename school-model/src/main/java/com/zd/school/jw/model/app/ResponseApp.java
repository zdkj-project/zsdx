package com.zd.school.jw.model.app;

public class ResponseApp <T>{

	/**
	 * 成功与否
	 */
	private boolean success=false;

	public boolean getSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}


    /**
     * 调用返回的消息
     */
	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

    /**
     * 返回的其他信息
     */
	private T obj;
	public T getObj() {
		return obj;
	}

	public void setObj(T obj) {
		this.obj = obj;
	}




}
