package com.teamsoft.framework.sys.controller;

import com.teamsoft.framework.common.controller.CommonController;
import com.teamsoft.framework.common.model.Page;
import com.teamsoft.framework.common.model.ResultInfo;
import com.teamsoft.framework.common.util.CommonStandardUtil;
import com.teamsoft.framework.common.util.CommonWebUtil;
import com.teamsoft.framework.sys.model.Menu;
import com.teamsoft.framework.sys.model.User;
import com.teamsoft.framework.sys.service.IMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 菜单
 * @author zhangcc
 * @version 2016-6-8 上午10:28:06
 */
@Controller
@RequestMapping("sys/menu")
public class MenuController extends CommonController<Menu> {
	private final IMenuService menuService;

	/**
	 * 构造方法注入
	 */
	@Autowired
	public MenuController(IMenuService menuService) {
		super.service = menuService;
		this.menuService = menuService;
	}

	/**
	 * 进入菜单列表页
	 * 如果是json请求, 返回带层级结构的菜单列表
	 */
	public ResultInfo list(Page page) {
		ResultInfo resultInfo = ResultInfo.getSuccessInfo();
		if (!CommonWebUtil.isJsonRequest()) {
			return resultInfo;
		}
		resultInfo.setData(menuService.listWithHierarchy());
		return resultInfo;
	}

	/**
	 * 根据角色权限获取菜单信息
	 */
	@RequestMapping("listByRole")
	public ResultInfo listByRole() {
		User loginUser = CommonWebUtil.getCurrendUser();
		assert loginUser != null;
		return ResultInfo.getSuccessInfo(menuService.getMenuByRole(loginUser.getRoleId()));
	}

	/**
	 * 获取带功能按钮的菜单列表
	 */
	@RequestMapping("listWithFunc")
	public ResultInfo listWithFunc(String roleId) {
		if (!StringUtils.hasLength(roleId)) {
			return ResultInfo.getParamErrorInfo();
		}
		return ResultInfo.getSuccessInfo(menuService.getMenuWithFunc(roleId));
	}

	/**
	 * 更新/保存菜单信息
	 * @param menu 菜单信息
	 */
	@RequestMapping("save")
	public ResultInfo save(Menu menu) {
		String id = menu.getId();
		if (!StringUtils.hasLength(menu.getParentId())) {
			menu.setParentId("top");
		}
		if (StringUtils.hasLength(id)) { // 更新
			menuService.update(menu);
		} else {
			menu.setId(CommonStandardUtil.generateUUId()); // 设置UUId为数据主键
			if (menu.getSortOrder() == null) {
				menu.setSortOrder(menuService.getMenuMaxSort(menu.getParentId()));
			}
			menuService.save(menu);
		}
		return ResultInfo.getSuccessInfo();
	}
}