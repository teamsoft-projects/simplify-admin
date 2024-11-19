package com.teamsoft.framework.common.model;

/**
 * 分页模型封装
 * @author zhangcc
 * @version 2016-6-3
 */
public class Page {
	private Integer pageStart;
	private Integer pageSize;

	/**
	 * 判断分页对象是不是空对象
	 */
	public boolean isNotNull() {
		return pageStart != null && pageSize != null;
	}

	public Integer getPageStart() {
		return pageStart;
	}

	public void setPageStart(Integer pageStart) {
		this.pageStart = pageStart;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
}