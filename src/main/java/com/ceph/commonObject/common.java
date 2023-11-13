package com.ceph.commonObject;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL) // 该类为NULL的字段不参加序列化
public class common<T> implements Serializable {

	private int status;
	private String msg;
	private T data;

	private common(int status) {
		this.status = status;
	}
	private common(int status, String msg) {
		this.status = status;
		this.msg = msg;
	}
	private common(int status, T data) {
		this.status = status;
		this.data = data;
	}
	private common(int status, String msg, T data) {
		this.status = status;
		this.msg = msg;
		this.data = data;
	}

	public int getStatus() {
		return status;
	}
	public String getMsg() {
		return msg;
	}
	public T getData() {
		return data;
	}

	@JsonIgnore
	public boolean isSuccess() {
		return this.status == ResultStatusEnum.SUCCESS.getCode();
	}

	public static <T> common<T> createSuccess() {
		return new common<T >(ResultStatusEnum.SUCCESS.getCode(),
				ResultStatusEnum.SUCCESS.getDescription());
	}

	public static <T > common<T > createSuccess(String msg) {
		return new common<T >(ResultStatusEnum.SUCCESS.getCode(), msg);
	}

	public static <T> common<T > createSuccess(T data) {
		return new common<T>(ResultStatusEnum.SUCCESS.getCode(),
				ResultStatusEnum.SUCCESS.getDescription(), data);
	}

	public static <T > common<T > createSuccess(String msg, T data) {
		return new common<T >(ResultStatusEnum.SUCCESS.getCode(), msg, data);
	}

	public static <T > common<T > createSuccess(int code, T data) {
		return new common<T >(code, data);
	}

	public static <T > common<T > createSuccess(int code, String msg, T data) {
		return new common<T >(code, msg, data);
	}
	public static <T > common<T > createError() {
		return new common<T >(ResultStatusEnum.ERROR.getCode(),
				ResultStatusEnum.ERROR.getDescription());
	}

	public static <T > common<T > createError(String msg) {
		return new common<T >(ResultStatusEnum.ERROR.getCode(), msg);
	}

	public static <T > common<T > createError(String msg, T data) {
		return new common<T >(ResultStatusEnum.ERROR.getCode(), msg, data);
	}

	public static <T > common<T > createError(int code) {
		return new common<T >(code);
	}

	public static <T > common<T > createError(int code, String msg) {
		return new common<T >(code, msg);
	}
}
