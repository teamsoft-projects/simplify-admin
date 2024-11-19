package com.teamsoft.framework.common.model;

import com.teamsoft.framework.common.core.CommonConstants;

import java.util.Map;

/**
 * 标准返回格式Model
 * @author zhangcc
 * @version 2015-8-17
 */
public class ResultInfo {
	/**
	 * 回传状态位, 标识请求是否正常返回
	 * @see CommonConstants.FrameWork#SUCCESS_FLAG
	 * @see CommonConstants.FrameWork#FAILURE_FLAG
	 * @see CommonConstants.FrameWork#PERMISSION_DENIED_FLAG
	 * @see CommonConstants.FrameWork#NOTICE_AND_REDIRECT_FLAG
	 * @see CommonConstants.FrameWork#BUSSINESS_NOTICE_FLAG
	 * @see CommonConstants.FrameWork#DO_REDIRECT_FLAG
	 * @see CommonConstants.FrameWork#SELF_NOTICE_FLAG
	 */
	private Integer flag;
	/**
	 * 回传消息
	 */
	private String message;
	/**
	 * 回传数据
	 */
	private Object data;
	/**
	 * 数据总数
	 */
	private Integer total;
	/**
	 * 占位, 用于临时存放某些数据
	 */
	private Object holder;

	private Map<String, ?> redirectAttributes;

	public ResultInfo() {
	}

	public ResultInfo(Integer flag) {
		this.flag = flag;
	}

	public ResultInfo(Integer flag, String message) {
		this.flag = flag;
		this.message = message;
	}

	public ResultInfo(Integer flag, String message, Object data) {
		this.flag = flag;
		this.message = message;
		this.data = data;
	}

	public ResultInfo(Integer flag, String message, Object data, Integer total) {
		this.flag = flag;
		this.message = message;
		this.data = data;
		this.total = total;
	}

	public ResultInfo(Integer flag, String message, Object data, Integer total, Map<String, ?> redirectAttributes) {
		this.flag = flag;
		this.message = message;
		this.data = data;
		this.total = total;
		this.redirectAttributes = redirectAttributes;
	}

	public static ResultInfo getSuccessInfo() {
		return new ResultInfo(CommonConstants.FrameWork.SUCCESS_FLAG, CommonConstants.FrameWork.SUCCESS_MESSAGE);
	}

	public static ResultInfo getSuccessInfo(Object data) {
		return new ResultInfo(CommonConstants.FrameWork.SUCCESS_FLAG, CommonConstants.FrameWork.SUCCESS_MESSAGE, data);
	}

	public static ResultInfo getFailureInfo() {
		return new ResultInfo(CommonConstants.FrameWork.FAILURE_FLAG, CommonConstants.FrameWork.FAILURE_MESSAGE, null);
	}

	public static ResultInfo getFailureInfo(String message) {
		return new ResultInfo(CommonConstants.FrameWork.FAILURE_FLAG, message, null);
	}

	public static ResultInfo getPermissionDeniedInfo() {
		return new ResultInfo(CommonConstants.FrameWork.PERMISSION_DENIED_FLAG, CommonConstants.FrameWork.PERMISSIONDENIED_MESSAGE, null);
	}

	public static ResultInfo getNoticeInfo(String message) {
		return new ResultInfo(CommonConstants.FrameWork.BUSSINESS_NOTICE_FLAG, message);
	}

	public static ResultInfo getParamErrorInfo() {
		return new ResultInfo(CommonConstants.FrameWork.PARAMETER_ERROR_FLAG, CommonConstants.FrameWork.PARAM_ERROR);
	}

	public static ResultInfo getSelfNoticeInfo(String message) {
		return new ResultInfo(CommonConstants.FrameWork.SELF_NOTICE_FLAG, message, null);
	}

	/**
	 * 获取重定向回传Model
	 * @param url 重定向地址
	 */
	public static ResultInfo getRedirectInfo(String url) {
		return new ResultInfo(CommonConstants.FrameWork.DO_REDIRECT_FLAG, null, url, null);
	}

	public static ResultInfo getRedirectInfo(String url, Map<String, ?> redirectAttributes) {
		return new ResultInfo(CommonConstants.FrameWork.DO_REDIRECT_FLAG, null, url, null, redirectAttributes);
	}

	/**
	 * 获取JSON回传Model
	 * 此回传Model会由com.teamsoft.common.web.GlobalInterceptor将返回视图转换为Json视图
	 */
	public static ResultInfo getJsonInfo(Object data) {
		return new ResultInfo(CommonConstants.FrameWork.JSON_VIEW_FLAG, null, data);
	}

	/**
	 * 获取JSON回传Model
	 * 此回传Model会由com.teamsoft.common.web.GlobalInterceptor将返回视图转换为Json视图
	 */
	public static ResultInfo getJsonInfo(String message, Object data) {
		return new ResultInfo(CommonConstants.FrameWork.JSON_VIEW_FLAG, message, data);
	}

	/**
	 * 获取JSON回传业务消息Model
	 * 此回传Model会由com.teamsoft.common.web.GlobalInterceptor将返回视图转换为Json视图
	 */
	public static ResultInfo getJsonNoticeInfo(String message) {
		return new ResultInfo(CommonConstants.FrameWork.JSON_NOTICE_FLAG, message, null);
	}

	/**
	 * 获取JSON回传业务消息Model
	 * 此回传Model会由com.teamsoft.common.web.GlobalInterceptor将返回视图转换为Json视图
	 */
	public static ResultInfo getJsonNoticeInfo(String message, Object data) {
		return new ResultInfo(CommonConstants.FrameWork.JSON_NOTICE_FLAG, message, data);
	}

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public Object getHolder() {
		return holder;
	}

	public void setHolder(Object holder) {
		this.holder = holder;
	}

	public Map<String, ?> getRedirectAttributes() {
		return redirectAttributes;
	}

	public void setRedirectAttributes(Map<String, ?> redirectAttributes) {
		this.redirectAttributes = redirectAttributes;
	}
}