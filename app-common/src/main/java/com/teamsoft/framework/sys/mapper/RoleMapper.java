package com.teamsoft.framework.sys.mapper;

import com.teamsoft.framework.common.mapper.CommonMapper;
import com.teamsoft.framework.sys.model.Role;
import com.teamsoft.framework.sys.vo.RoleMenuFuncVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色数据库操作接口
 * @author zhangcc
 * @version 2017/8/24
 */
public interface RoleMapper extends CommonMapper<Role> {
	/**
	 * 根据角色编号删除角色关联菜单和关联功能
	 * @param roleId 角色列表
	 */
	void removeRoleMenuFunc(@Param("roleId") String roleId);

	/**
	 * 保存角色-菜单关联关系
	 */
	void saveRoleMenu(@Param("entities") List<RoleMenuFuncVO> entities);

	/**
	 * 保存角色-菜单-功能关联关系
	 */
	void saveRoleMenuFunc(@Param("entities") List<RoleMenuFuncVO> entities);
}