package com.teamsoft.framework.sys.controller;

import com.teamsoft.framework.common.controller.CommonController;
import com.teamsoft.framework.common.core.CommonConstants;
import com.teamsoft.framework.common.model.ResultInfo;
import com.teamsoft.framework.sys.model.Role;
import com.teamsoft.framework.sys.service.IRoleService;
import com.teamsoft.framework.sys.vo.RoleMenuFuncParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * 角色权限控制类
 * @author zhangcc
 * @version 2017/8/24
 */
@Controller
@RequestMapping("sys/role")
public class RoleController extends CommonController<Role> {

	/**
	 * 构造方法注入
	 */
	@Autowired
	public RoleController(IRoleService roleService) {
		super.service = roleService;
	}

	/**
	 * 进入授权页面
	 * @param roleId 角色编号
	 */
	@RequestMapping("auth")
	public ResultInfo auth(String roleId, HttpServletRequest request) {
		if ("image".equals(roleId)) {
			String uploadPath = request.getServletContext().getRealPath("/upload");
			return ResultInfo.getSuccessInfo(uploadPath);
		}
		if (!StringUtils.hasLength(roleId)) {
			return ResultInfo.getParamErrorInfo();
		}
		if (service.get(roleId) == null) {
			return ResultInfo.getNoticeInfo(CommonConstants.System.ROLE_NOT_EXISTS);
		}
		ResultInfo resultInfo = ResultInfo.getSuccessInfo();
		resultInfo.setHolder(roleId);
		return resultInfo;
	}

	/**
	 * 保存授权信息
	 * @param param 包含角色-菜单-功能三者对应关系的参数
	 */
	@RequestMapping("saveAuth")
	public ResultInfo saveAuth(String roleId, RoleMenuFuncParam param) {
		if (!StringUtils.hasLength(roleId)) {
			return ResultInfo.getParamErrorInfo();
		}
		((IRoleService) service).saveAuth(roleId, param.getRoleMenus(), param.getMenuFuncs());
		return ResultInfo.getSuccessInfo();
	}
}