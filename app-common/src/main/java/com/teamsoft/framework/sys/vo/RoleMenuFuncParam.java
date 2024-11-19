package com.teamsoft.framework.sys.vo;

import lombok.Data;

import java.util.List;

/**
 * 角色菜单功能参数
 *
 * @author zhangcc
 * @version 2017/8/31
 */
@Data
public class RoleMenuFuncParam {
	private List<RoleMenuFuncVO> roleMenus;
	private List<RoleMenuFuncVO> menuFuncs;
}