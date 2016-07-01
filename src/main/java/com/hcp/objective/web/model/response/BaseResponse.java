package com.hcp.objective.web.model.response;

public abstract class BaseResponse {

	public static String SUCCESS = "SUCCESS";
	public static String FAILED = "FAILED";

	private int status = 0;
	private String message;
	private Object object;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}
}
