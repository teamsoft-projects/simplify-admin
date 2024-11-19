package com.teamsoft.framework.sys.mapper;

import com.teamsoft.framework.common.mapper.CommonMapper;
import com.teamsoft.framework.sys.model.MenuFunction;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 菜单功能数据库操作接口
 * @author zhangcc
 * @version 2017/8/24
 */
public interface MenuFunctionMapper extends CommonMapper<MenuFunction> {
	/**
	 * 根据角色获取菜单功能列表
	 * @param roleId 角色编号
	 * @return 菜单功能信息
	 */
	List<MenuFunction> getMenuFuncsWithMenuId(@Param("roleId") String roleId);
}