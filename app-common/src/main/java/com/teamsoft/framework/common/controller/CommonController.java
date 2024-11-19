package com.teamsoft.framework.common.controller;

import com.teamsoft.framework.common.exception.BusinessException;
import com.teamsoft.framework.common.model.*;
import com.teamsoft.framework.common.service.ICommonService;
import com.teamsoft.framework.common.util.BeanUtils;
import com.teamsoft.framework.common.util.CommonStandardUtil;
import com.teamsoft.framework.common.util.CommonWebUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.Serializable;
import java.util.List;

/**
 * 控制器公共类
 * 封装增删改查逻辑
 * @author zhangcc
 * @version 2017/8/28
 */
public class CommonController<T extends Entity> {
	// 公共服务类接口
	protected ICommonService<T> service;

	/**
	 * 获取数据列表
	 * @param page 分页参数
	 */
	@ApiOperation(value = "查询列表", notes = "获取数据列表")
	@RequestMapping("list")
	public ResultInfo list(Page page) {
		ResultInfo resultInfo = ResultInfo.getSuccessInfo();
		if (!CommonWebUtil.isJsonRequest()) {
			return resultInfo;
		}
		resultInfo.setData(service.list(page));
		if (page.isNotNull()) {
			resultInfo.setTotal(service.count());
		}
		return resultInfo;
	}

	/**
	 * 查询前操作
	 * 对查询参数进行修改等
	 */
	public void beforeQuery(T entity, Page page) {
	}

	/**
	 * 查询后操作
	 * 对查询结果进行二次处理
	 */
	public void afterQuery(T entity, Page page, ResultInfo resultInfo) {
	}

	/**
	 * 条件查询数据列表
	 * @param entity 查询条件
	 * @param page   分页条件
	 * @return 指定条件查询结果
	 */
	@ApiOperation(value = "条件查询列表", notes = "根据查询条件获取数据列表")
	@PostMapping("listByEntity")
	public ResultInfo listByEntity(T entity, Page page) {
		this.beforeQuery(entity, page);
		CommonWebUtil.verify(entity, VerifyScene.QUERY);
		ResultInfo resultInfo = ResultInfo.getSuccessInfo();
		// 设置时间段参数
		CommonWebUtil.setTimeInterval(entity, true);
		resultInfo.setData(service.listByEntity(entity, page));
		if (page.isNotNull()) {
			resultInfo.setTotal(service.countByEntity(entity));
		}
		this.afterQuery(entity, page, resultInfo);
		return resultInfo;
	}

	/**
	 * 条件查询数据数量
	 * @param entity 查询条件
	 * @param page   分页条件
	 * @return 指定条件查询结果
	 */
	@ApiOperation(value = "条件查询数量", notes = "根据查询条件获取数据数量")
	@PostMapping("countByEntity")
	public ResultInfo countByEntity(T entity, Page page) {
		CommonWebUtil.verify(entity, VerifyScene.QUERY);
		ResultInfo resultInfo = ResultInfo.getSuccessInfo();
		// 设置时间段参数
		CommonWebUtil.setTimeInterval(entity, true);
		resultInfo.setData(service.countByEntity(entity));
		return resultInfo;
	}

	/**
	 * 进入编辑页面
	 * @param id 数据主键
	 * @return 查询结果
	 */
	@ApiOperation(value = "编辑", notes = "获取编辑数据")
	@RequestMapping("edit")
	public ResultInfo edit(String id) {
		ResultInfo resultInfo = ResultInfo.getSuccessInfo();
		if (id == null) {
			return resultInfo;
		}
		// 编辑
		resultInfo.setData(service.get(id));
		return resultInfo;
	}

	/**
	 * 保存前操作
	 * @param entity 待操作数据
	 */
	protected void beforeSave(T entity) {
	}

	/**
	 * 保存后操作
	 * @param entity 待操作数据
	 */
	protected void afterSave(T entity) {
	}

	/**
	 * 更新/保存
	 */
	@ApiOperation(value = "更新/保存", notes = "保存或更新数据，没有ID保存，否则更新")
	@PostMapping("save")
	public ResultInfo save(T entity) {
		this.beforeSave(entity);
		String id = entity.getId();
		if (StringUtils.hasLength(id)) { // 更新
			CommonWebUtil.verify(entity, VerifyScene.UPDATE);
			T entityTemp = service.get(id);
			BeanUtils.copyProperties(entity, entityTemp);
			service.update(entityTemp);
		} else {
			CommonWebUtil.verify(entity, VerifyScene.SAVE);
			entity.setId(CommonStandardUtil.generateUUId()); // 设置UUId为数据主键
			service.save(entity);
		}
		this.afterSave(entity);
		return ResultInfo.getSuccessInfo();
	}

	/**
	 * 更新/保存，带排序码
	 */
	@ApiOperation(value = "带排序码更新", notes = "更新/保存，带排序码")
	@RequestMapping("saveWithSort")
	public ResultInfo saveWithSort(T entity) {
		this.beforeSave(entity);
		String id = entity.getId();
		if (StringUtils.hasLength(id)) { // 更新
			CommonWebUtil.verify(entity, VerifyScene.UPDATE);
			service.update(entity);
		} else {
			CommonWebUtil.verify(entity, VerifyScene.SAVE);
			entity.setId(CommonStandardUtil.generateUUId()); // 设置UUId为数据主键
			if (entity.getSortOrder() == null) {
				entity.setSortOrder(service.getMaxSort());
			}
			service.save(entity);
		}
		this.afterSave(entity);
		return ResultInfo.getSuccessInfo();
	}

	/**
	 * 删除前操作
	 * @param ids 待操作数据
	 */
	protected void beforeRemove(List<Serializable> ids) {
	}

	/**
	 * 删除后操作
	 * @param ids 待操作数据
	 */
	protected void afterRemove(List<Serializable> ids) {
	}

	/**
	 * 批量删除
	 */
	@ApiOperation(value = "批量删除", notes = "批量删除")
	@RequestMapping("remove")
	public ResultInfo remove(IdParam param) throws BusinessException {
		List<Serializable> ids = param.getIds();
		if (ids.isEmpty()) {
			return ResultInfo.getParamErrorInfo();
		}
		this.beforeRemove(ids);
		service.removeAll(ids);
		this.afterRemove(ids);
		return ResultInfo.getSuccessInfo();
	}
}