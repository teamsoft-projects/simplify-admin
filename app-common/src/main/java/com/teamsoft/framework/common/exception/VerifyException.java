package com.teamsoft.framework.common.exception;

import com.teamsoft.framework.common.core.CommonConstants;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * 校验异常
 * @author zhangcc
 * @version 2017/9/21
 */
@Getter
@Setter
public class VerifyException extends RuntimeException {
	// 业务标识
	private Integer flag = CommonConstants.FrameWork.FAILURE_FLAG;
	// 异常消息
	private String message = CommonConstants.FrameWork.PARAM_ERROR;

	public VerifyException() {
	}

	public VerifyException(String message) {
		this.message = message;
	}

	public VerifyException(Integer flag, String message) {
		this.flag = flag;
		this.message = message;
	}
}