package com.teamsoft.framework.sys.service;

import com.teamsoft.framework.common.service.ICommonService;
import com.teamsoft.framework.sys.model.Menu;

import java.util.List;

public interface IMenuService extends ICommonService<Menu> {
	/**
	 * 获取最大排序码+1
	 * @param parentId 上级菜单编号
	 * @return 最大排序码+1
	 */
	Integer getMenuMaxSort(String parentId);

	/**
	 * 获取包含菜单权限的菜单列表
	 */
	List<Menu> getMenuWithFunc(String roleId);

	/**
	 * 获取带层级结构的菜单列表
	 */
	List<Menu> listWithHierarchy();

	/**
	 * 根据角色权限获取菜单信息
	 */
	List<Menu> getMenuByRole(String roleId);
}