package com.teamsoft.mgr.common.model;

import java.io.InputStream;

/**
 * 类描述：HTTP请求返回实体
 * @author zhangcc
 * @version 2015-6-18 下午8:17:35
 */
public class HttpResponseModel {
	/** 返回状态码 */
	private Integer statusCode;
	/** 返回内容 */
	private String responseBody;
	/** 返回数据流 */
	private InputStream inputStream;

	public static HttpResponseModel getSuccessResp() {
		HttpResponseModel model = new HttpResponseModel();
		model.setStatusCode(Constants.FrameWork.STATUS_SUCCESS);
		return model;
	}

	public static HttpResponseModel getFailResp() {
		HttpResponseModel model = new HttpResponseModel();
		model.setStatusCode(Constants.FrameWork.STATUS_FAILURE);
		return model;
	}

	public static HttpResponseModel getFailResp(String responseBody) {
		HttpResponseModel model = new HttpResponseModel();
		model.setResponseBody(responseBody);
		model.setStatusCode(Constants.FrameWork.STATUS_FAILURE);
		return model;
	}

	@Override
	public String toString() {
		return "HttpResponseModel{" +
				"statusCode=" + statusCode +
				", responseBody='" + responseBody + '\'' +
				'}';
	}

	public Integer getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(Integer statusCode) {
		this.statusCode = statusCode;
	}

	public String getResponseBody() {
		return responseBody;
	}

	public void setResponseBody(String responseBody) {
		this.responseBody = responseBody;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}
}