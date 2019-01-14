package com.github.verils.transdoc.web.web;

public class Response<T> {

	private boolean success;

	private String message;

	private T data;

	public static <T> Response<T> success() {
		return success(null);
	}

	public static <T> Response<T> success(T data) {
		return new Response<T>(true, null, data);
	}

	public static <T> Response<T> fail(String message) {
		return new Response<T>(false, message, null);
	}

	public Response(boolean success, String message, T data) {
		super();
		this.success = success;
		this.message = message;
		this.data = data;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
}
