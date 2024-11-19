package com.teamsoft.framework.common.model;

import com.teamsoft.framework.common.core.CommonConstants;

/**
 * 返回到接口的数据结构
 * @author zhangcc
 * @version 2017/9/6
 */
public class ResponseInfo {
	// 回传状态位
	private Integer flag;
	// 回传消息
	private String message;
	// 回传数据
	private Object data;

	private ResponseInfo(Integer flag, String message, Object data) {
		this.flag = flag;
		this.message = message;
		this.data = data;
	}

	private ResponseInfo(Integer flag, String message) {
		this.flag = flag;
		this.message = message;
	}

	public static ResponseInfo getSuccessInfo() {
		return new ResponseInfo(CommonConstants.FrameWork.SUCCESS_FLAG, CommonConstants.FrameWork.SUCCESS_MESSAGE, null);
	}

	public static ResponseInfo getSuccessInfo(Object data) {
		return new ResponseInfo(CommonConstants.FrameWork.SUCCESS_FLAG, CommonConstants.FrameWork.SUCCESS_MESSAGE, data);
	}

	public static ResponseInfo getSuccessInfo(ResultInfo resultInfo) {
		return new ResponseInfo(resultInfo.getFlag(), resultInfo.getMessage(), resultInfo.getData());
	}

	public static ResponseInfo getSuccessInfo(Integer flag, String message, Object data) {
		return new ResponseInfo(flag, message, data);
	}

	public static ResponseInfo getFailureInfo(String message) {
		return new ResponseInfo(CommonConstants.FrameWork.FAILURE_FLAG, message);
	}

	public static ResponseInfo getParamErrorInfo() {
		return new ResponseInfo(CommonConstants.FrameWork.PARAMETER_ERROR_FLAG, CommonConstants.FrameWork.PARAM_ERROR);
	}

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
}