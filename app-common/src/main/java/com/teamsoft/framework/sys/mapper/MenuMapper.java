package com.teamsoft.framework.sys.mapper;

import com.teamsoft.framework.common.mapper.CommonMapper;
import com.teamsoft.framework.sys.model.Menu;

import java.util.List;

public interface MenuMapper extends CommonMapper<Menu> {
	/**
	 * 获取最大排序码+1
	 * @param parentId 上级菜单编号
	 * @return 最大排序码+1
	 */
	Integer getMenuMaxSort(String parentId);

	/**
	 * 获取包含菜单权限的菜单列表
	 */
	List<Menu> getMenuWithRoleFunc(String roleId);
}