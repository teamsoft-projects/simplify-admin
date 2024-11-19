package com.teamsoft.framework.sys.service;

import com.teamsoft.framework.common.service.ICommonService;
import com.teamsoft.framework.sys.model.Role;
import com.teamsoft.framework.sys.vo.RoleMenuFuncVO;

import java.util.List;

/**
 * 角色服务接口
 * @author zhangcc
 * @version 2017/8/24
 */
public interface IRoleService extends ICommonService<Role> {
	/**
	 * 保存角色授权信息
	 * @param roleId    角色编号
	 * @param roleMenus 角色-菜单关联数据
	 * @param menuFunc  菜单-功能关联数据
	 */
	void saveAuth(String roleId, List<RoleMenuFuncVO> roleMenus, List<RoleMenuFuncVO> menuFunc);
}