package com.hcp.objective.web.model.response;

public class BaseResponse<T> {

	public static String SUCCESS = "SUCCESS";
	public static String FAILED = "FAILED";
	
	public static int SUCCESS_VALUE = 0;
	public static int FAILED_VALUE = -1;

	private int status = 0;
	private String message;
	private T object;

	public BaseResponse(T object) {
		this.setObject(object);
	}

	public BaseResponse(int status, String message, T object) {
		this.setStatus(status);
		if (message == null) {
			this.setMessage(status == 0 ? SUCCESS : FAILED);
		} else {
			this.setMessage(message);
		}
		this.setMessage(message);
		this.setObject(object);
	}

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

	public T getObject() {
		return object;
	}

	public void setObject(T object) {
		this.object = object;
	}
}
