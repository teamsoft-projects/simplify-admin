package com.teamsoft.framework.common.exception;

/**
 * 接口请求异常
 * @author zhangcc
 * @version 2017/9/27
 */
public class InterfaceException extends RuntimeException {
	public InterfaceException() {
	}

	public InterfaceException(String message) {
		super(message);
	}
}