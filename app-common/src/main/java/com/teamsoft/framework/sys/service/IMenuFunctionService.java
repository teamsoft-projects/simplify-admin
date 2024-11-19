package com.teamsoft.framework.sys.service;

import com.teamsoft.framework.common.service.ICommonService;
import com.teamsoft.framework.sys.model.MenuFunction;

import java.util.List;

/**
 * 菜单功能服务接口
 * @author zhangcc
 * @version 2017/8/24
 */
public interface IMenuFunctionService extends ICommonService<MenuFunction> {
	/**
	 * 保存所有菜单功能数据
	 * @param entities 待保存的批量数据
	 */
	void saveMenuFunc(MenuFunction menuFunction, List<MenuFunction> entities);

	/**
	 * 根据角色获取菜单权限点按钮列表
	 * @param roleId 角色编号
	 * @return 菜单功能信息
	 */
	List<MenuFunction> getMenuFuncsWithMenuId(String roleId);
}