package com.teamsoft.framework.common.service;

import com.teamsoft.framework.common.exception.BusinessException;
import com.teamsoft.framework.common.mapper.CommonMapper;
import com.teamsoft.framework.common.model.Page;
import com.teamsoft.framework.common.util.CommonStandardUtil;

import java.io.Serializable;
import java.util.List;

/**
 * 公共服务类
 * @author zhangcc
 * @version 2016-6-12
 */
public class CommonService<T> {
	/**
	 * 默认的Mapper映射
	 */
	protected CommonMapper<T> mapper;

	/**
	 * 获取数据列表
	 * @return 数据列表
	 */
	public List<T> list(Page page) {
		page = CommonStandardUtil.initializationPage(page);
		return mapper.list(page);
	}

	/**
	 * 列出所有数据列表(无分页)
	 * @return 所有数据列表
	 */
	public List<T> list() {
		return mapper.list(null);
	}

	/**
	 * 列出所有数据列表
	 * @param entity 查询条件
	 * @param page   分页条件(可为空)
	 * @return 所有数据列表
	 */
	public List<T> listByEntity(T entity, Page page) {
		page = CommonStandardUtil.initializationPage(page);
		return mapper.listByEntity(entity, page);
	}

	/**
	 * 获取数据总数
	 * @return 数据总数
	 */
	public Integer count() {
		return mapper.count();
	}

	/**
	 * 根据entity获取数据总数
	 * @param entity 查询条件
	 * @return 数据总数
	 */
	public Integer countByEntity(T entity) {
		return mapper.countByEntity(entity);
	}

	/**
	 * 根据编号获取数据
	 * @param id 数据编号
	 * @return 数据详情
	 */
	public T get(Serializable id) {
		return mapper.get(id);
	}

	/**
	 * 根据数据对象的属性获取指定数据
	 * @param entity 对象形式的参数
	 * @return 指定数据
	 */
	public T getByEntity(T entity) {
		return mapper.getByEntity(entity);
	}

	/**
	 * 获取排序码最大值
	 * @return 排序码最大值
	 */
	public Integer getMaxSort() {
		return mapper.getMaxSort();
	}

	/**
	 * 保存指定数据到数据库
	 * @param entity 待保存数据
	 */
	public Integer save(T entity) {
		return mapper.save(entity);
	}

	/**
	 * 批量保存数据
	 * @param entities 待保存的批量数据
	 */
	public Integer saveAll(List<T> entities) {
		return mapper.saveAll(entities);
	}

	/**
	 * 更新指定数据
	 * @param entity 待更新数据
	 */
	public Integer update(T entity) {
		return mapper.update(entity);
	}

	/**
	 * 批量更新数据
	 * @param entities 待更新的批量数据
	 */
	public Integer updateAll(List<T> entities) {
		return mapper.updateAll(entities);
	}

	/**
	 * 根据ID删除指定数据
	 * @param id 待删除的ID
	 */
	public Integer remove(Serializable id) {
		return mapper.remove(id);
	}

	/**
	 * 根据ID删除指定数据
	 * @param entity 待删除的对象(包含删除的条件)
	 */
	public Integer removeByEntity(T entity) {
		return mapper.removeByEntity(entity);
	}

	/**
	 * 通过ID集合批量删除数据
	 * @param ids 待删除的数据ID集合
	 */
	public Integer removeAll(List<Serializable> ids) throws BusinessException {
		return mapper.removeAll(ids);
	}

	/**
	 * 批量删除数据
	 * @param entities 待删除的批量数据
	 */
	public Integer removeByEntities(List<T> entities) {
		return mapper.removeByEntities(entities);
	}
}