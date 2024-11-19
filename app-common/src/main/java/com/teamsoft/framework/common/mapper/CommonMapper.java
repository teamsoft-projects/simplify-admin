package com.teamsoft.framework.common.mapper;

import com.teamsoft.framework.common.model.Page;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;

/**
 * 公共的Dao接口
 * @author zhangcc
 * @version 2016-6-3
 */
public interface CommonMapper<T> {
	/**
	 * 列出所有数据列表
	 * @param page 分页条件(可为空)
	 * @return 所有数据列表
	 */
	List<T> list(@Param("page") Page page);

	/**
	 * 列出所有数据列表
	 * @param entity 查询条件
	 * @param page   分页条件(可为空)
	 * @return 所有数据列表
	 */
	List<T> listByEntity(@Param("entity") T entity, @Param("page") Page page);

	/**
	 * 获取数据总数
	 * @return 数据总数
	 */
	Integer count();

	/**
	 * 根据entity获取数据总数
	 * @param entity 查询条件
	 * @return 数据总数
	 */
	Integer countByEntity(@Param("entity") T entity);

	/**
	 * 根据编号获取数据
	 * @param id 数据编号
	 * @return 数据详情
	 */
	T get(@Param("id") Serializable id);

	/**
	 * 根据数据对象的属性获取指定数据
	 * @param entity 对象形式的参数
	 * @return 指定数据
	 */
	T getByEntity(@Param("entity") T entity);

	/**
	 * 获取排序码最大值
	 * @return 排序码最大值
	 */
	Integer getMaxSort();

	/**
	 * 保存指定数据到数据库
	 * @param entity 待保存数据
	 */
	Integer save(@Param("entity") T entity);

	/**
	 * 批量保存数据
	 * @param entities 待保存的批量数据
	 */
	Integer saveAll(@Param("entities") List<T> entities);

	/**
	 * 更新指定数据
	 * @param entity 待更新数据
	 */
	Integer update(@Param("entity") T entity);

	/**
	 * 根据条件更新数据
	 * @param entity 更新条件
	 * @return 更新数量
	 */
	Integer updateByEntity(@Param("entity") T entity);

	/**
	 * 批量更新数据
	 * @param entities 待更新的批量数据
	 */
	Integer updateAll(@Param("entities") List<T> entities);

	/**
	 * 根据ID删除指定数据
	 * @param id 待删除的ID
	 */
	Integer remove(@Param("id") Serializable id);

	/**
	 * 根据ID删除指定数据
	 * @param entity 待删除的对象(包含删除的条件)
	 */
	Integer removeByEntity(@Param("entity") T entity);

	/**
	 * 通过ID集合批量删除数据
	 * @param ids 待删除的数据ID集合
	 */
	Integer removeAll(@Param("ids") List<Serializable> ids);

	/**
	 * 批量删除数据
	 * @param entities 待删除的批量数据
	 */
	Integer removeByEntities(@Param("entities") List<T> entities);
}