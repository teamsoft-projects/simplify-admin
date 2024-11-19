package com.teamsoft.framework.sys.service;

import com.teamsoft.framework.common.service.CommonService;
import com.teamsoft.framework.common.util.CommonStandardUtil;
import com.teamsoft.framework.sys.mapper.RoleMapper;
import com.teamsoft.framework.sys.model.Role;
import com.teamsoft.framework.sys.vo.RoleMenuFuncVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 角色服务类
 * @author zhangcc
 * @version 2017/8/24
 */
@Service
public class RoleService extends CommonService<Role> implements IRoleService {
	private final RoleMapper roleMapper;

	@Autowired
	public RoleService(RoleMapper mapper) {
		this.mapper = mapper;
		roleMapper = mapper;
	}

	/**
	 * 保存角色授权信息
	 * @param roleId    角色编号
	 * @param roleMenus 角色-菜单关联数据
	 * @param menuFuncs 菜单-功能关联数据
	 */
	public void saveAuth(String roleId, List<RoleMenuFuncVO> roleMenus, List<RoleMenuFuncVO> menuFuncs) {
		// 先删除后保存
		roleMapper.removeRoleMenuFunc(roleId);
		// 保存角色-菜单关联
		if (roleMenus != null && !roleMenus.isEmpty()) {
			for (RoleMenuFuncVO vo : roleMenus) {
				vo.setId(CommonStandardUtil.generateUUId());
			}
			roleMapper.saveRoleMenu(roleMenus);
		}
		if (menuFuncs != null && !menuFuncs.isEmpty()) {
			for (RoleMenuFuncVO vo : menuFuncs) {
				vo.setId(CommonStandardUtil.generateUUId());
			}
			roleMapper.saveRoleMenuFunc(menuFuncs);
		}
	}
}