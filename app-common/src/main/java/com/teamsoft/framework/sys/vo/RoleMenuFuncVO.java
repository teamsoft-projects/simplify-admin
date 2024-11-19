package com.teamsoft.framework.sys.vo;

import lombok.Data;

/**
 * 角色菜单功能VO
 *
 * @author zhangcc
 * @version 2017/8/31
 */
@Data
public class RoleMenuFuncVO {
	private String id;
	private String roleId;
	private String menuId;
	private String funcId;
}