package com.teamsoft.framework.sys.service;

import com.teamsoft.framework.common.core.CommonConstants;
import com.teamsoft.framework.common.exception.BusinessException;
import com.teamsoft.framework.common.service.CommonService;
import com.teamsoft.framework.sys.mapper.MenuMapper;
import com.teamsoft.framework.sys.model.Menu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Service
public class MenuService extends CommonService<Menu> implements IMenuService {
	private final MenuMapper menuMapper;

	/**
	 * 构造方法注入
	 */
	@Autowired
	public MenuService(MenuMapper mapper) {
		this.menuMapper = mapper;
		this.mapper = mapper;
	}

	/**
	 * 获取带层级结构的菜单列表
	 */
	@Override
	public List<Menu> listWithHierarchy() {
		List<Menu> root = menuMapper.list(null);
		return tansferMenuWithHierarchy(root);
	}

	/**
	 * 批量删除数据重写, 判断下级是否存在
	 * @param ids 待删除的数据ID集合
	 */
	@Override
	public Integer removeAll(List<Serializable> ids) throws BusinessException {
		if (ids == null || ids.isEmpty()) {
			return 0;
		}
		for (Serializable id : ids) {
			if (!(id instanceof String)) {
				continue;
			}
			Menu menu = menuMapper.get(id);
			if (menu == null) {
				continue;
			}
			if (CommonConstants.System.MENU_TOP.equals(menu.getParentId())) {
				List<Menu> children = menuMapper.listByEntity(Menu.getParentMenu((String) id), null);
				for (Menu child : children) {
					if (!ids.contains(child.getId())) {
						throw new BusinessException("该菜单存在子菜单, 禁止删除");
					}
				}
			}
		}
		return menuMapper.removeAll(ids);
	}

	/**
	 * 获取最大排序码+1
	 * @param parentId 上级菜单编号
	 * @return 最大排序码+1
	 */
	@Override
	public Integer getMenuMaxSort(String parentId) {
		return menuMapper.getMenuMaxSort(parentId);
	}

	/**
	 * 获取包含菜单权限的菜单列表
	 */
	@Override
	public List<Menu> getMenuWithFunc(String roleId) {
		List<Menu> result = new ArrayList<>();
		List<Menu> root = menuMapper.getMenuWithRoleFunc(roleId);
		if (root == null || root.isEmpty()) {
			return result;
		}

		return tansferMenuWithHierarchy(root);
	}

	/**
	 * 根据角色权限获取菜单信息
	 */
	public List<Menu> getMenuByRole(String roleId) {
		List<Menu> result = new ArrayList<>();
		if (!StringUtils.hasLength(roleId)) {
			return result;
		}
		Menu menu = new Menu();
		menu.setRoleId(roleId);
		List<Menu> root = menuMapper.listByEntity(menu, null);
		if (root == null || root.isEmpty()) {
			return result;
		}

		return tansferMenuWithHierarchy(root);
	}

	/**
	 * 将菜单转换为带层级结构的形式
	 * @param root 待转换的菜单
	 * @return 转换结果
	 */
	private List<Menu> tansferMenuWithHierarchy(List<Menu> root) {
 		List<Menu> result = new ArrayList<>();
		if (root == null || root.isEmpty()) {
			return result;
		}
		boolean isFirst = true;
 		for (int i = 0;i < root.size();i++) {
			Menu parent = root.get(i);
			if (parent == null || parent.getId() == null || !CommonConstants.System.MENU_TOP.equals(parent.getParentId())) {
				continue;
			}
			if (isFirst) {
				parent.setSpread(true);
				isFirst = false;
			}
			List<Menu> children = new ArrayList<>();
			parent.setChildren(children);
			result.add(parent);
			for (Menu child : root) { // 遍历菜单, 找到该菜单下的所有子菜单
				if (child == null) {
					continue;
				}
				if (parent.getId().equals(child.getParentId())) { // 子菜单parentId与父菜单ID相同, 放入children容器
					children.add(child); // 将子菜单放入父菜单的children容器中
				}
			}
		}

		return result;
	}
}