package com.teamsoft.framework.common.exception;

/**
 * 业务异常定义
 * @author zhangcc
 * @version 2017/3/26.
 */
public class BusinessException extends RuntimeException {
	public BusinessException() {
	}

	public BusinessException(String message) {
		super(message);
	}
}