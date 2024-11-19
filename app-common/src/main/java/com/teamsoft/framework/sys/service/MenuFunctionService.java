package com.teamsoft.framework.sys.service;

import com.teamsoft.framework.common.service.CommonService;
import com.teamsoft.framework.common.util.CommonStandardUtil;
import com.teamsoft.framework.sys.mapper.MenuFunctionMapper;
import com.teamsoft.framework.sys.model.MenuFunction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Iterator;
import java.util.List;

/**
 * 菜单功能服务类
 * @author zhangcc
 * @version 2017/8/24
 */
@Service
public class MenuFunctionService extends CommonService<MenuFunction> implements IMenuFunctionService {
	@Autowired
	public MenuFunctionService(MenuFunctionMapper mapper) {
		this.mapper = mapper;
	}

	/**
	 * 保存所有菜单功能数据
	 * @param entities 待保存的批量数据
	 */
	public void saveMenuFunc(MenuFunction menuFunction, List<MenuFunction> entities) {
		// 先删除后保存
		mapper.removeByEntity(menuFunction);
		if (entities != null && !entities.isEmpty()) {
			String menuId = menuFunction.getMenuId();
			int maxSort = 1;
			Iterator<MenuFunction> iter = entities.iterator();
			while (iter.hasNext()) {
				MenuFunction temp = iter.next();
				if (temp == null || !StringUtils.hasLength(temp.getName()) || temp.getIcon() == null) {
					iter.remove();
					continue;
				}
				temp.setMenuId(menuId);
				temp.setSortOrder(maxSort++);
				if (temp.getId() == null) {
					temp.setId(CommonStandardUtil.generateUUId());
				}
			}
			mapper.saveAll(entities);
		}
	}

	/**
	 * 根据角色获取菜单权限点按钮列表
	 * @param roleId 角色编号
	 * @return 菜单功能信息
	 */
	public List<MenuFunction> getMenuFuncsWithMenuId(String roleId) {
		return ((MenuFunctionMapper) mapper).getMenuFuncsWithMenuId(roleId);
	}
}