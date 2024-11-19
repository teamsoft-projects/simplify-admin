package com.teamsoft.framework.sys.controller;

import com.teamsoft.framework.common.controller.CommonController;
import com.teamsoft.framework.common.model.Page;
import com.teamsoft.framework.common.model.ResultInfo;
import com.teamsoft.framework.common.util.CommonWebUtil;
import com.teamsoft.framework.sys.model.MenuFunction;
import com.teamsoft.framework.sys.model.User;
import com.teamsoft.framework.sys.service.IMenuFunctionService;
import com.teamsoft.framework.sys.vo.MenuFunctionParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 菜单功能控制类
 * @author zhangcc
 * @version 2017/8/28
 */
@Controller
@RequestMapping("sys/menuFunc")
public class MenuFunctionController extends CommonController<MenuFunction> {
	/**
	 * 构造方法注入
	 */
	@Autowired
	public MenuFunctionController(IMenuFunctionService service) {
		this.service = service;
	}

	/**
	 * 进入列表页
	 * @param page 分页参数
	 */
	public ResultInfo list(Page page) {
		String menuId = CommonWebUtil.getParam("menuId");
		if (!StringUtils.hasLength(menuId)) {
			return ResultInfo.getParamErrorInfo();
		}
		return ResultInfo.getSuccessInfo(menuId);
	}

	/**
	 * 查询菜单功能列表
	 */
	public ResultInfo listByEntity(MenuFunction menuFunction, Page page) {
		ResultInfo resultInfo = ResultInfo.getSuccessInfo();
		List<MenuFunction> result = service.listByEntity(menuFunction, null);
		resultInfo.setData(result);
		resultInfo.setTotal(result.size());

		return resultInfo;
	}

	/**
	 * 保存菜单功能列表
	 * @param menuFunction 包含菜单编号的条件
	 * @param param        数据列表
	 */
	@RequestMapping("saveMenuFunc")
	public ResultInfo saveMenuFunc(MenuFunction menuFunction, MenuFunctionParam param) {
		if (!StringUtils.hasLength(menuFunction.getMenuId())) {
			return ResultInfo.getParamErrorInfo();
		}
		((IMenuFunctionService) service).saveMenuFunc(menuFunction, param.getDatas());
		return ResultInfo.getSuccessInfo();
	}

	/**
	 * 获取登录用户的权限点列表
	 */
	@PostMapping("getMenuFuncForLoginUser")
	public ResultInfo getMenuFuncForLoginUser() {
		Map<String, MenuFunction> retMap = new HashMap<>();
		User loginUser = CommonWebUtil.getCurrendUser();
		if (Objects.isNull(loginUser)) {
			return ResultInfo.getSuccessInfo(retMap);
		}
		List<MenuFunction> menuFunctions = ((IMenuFunctionService) service).getMenuFuncsWithMenuId(loginUser.getRoleId());
		if (Objects.nonNull(menuFunctions)) {
			menuFunctions.stream()
					.filter(mf -> Objects.nonNull(mf.getIdentify()))
					.forEach(mf -> retMap.put(mf.getIdentify(), mf));
		}
		return ResultInfo.getSuccessInfo(retMap);
	}
}