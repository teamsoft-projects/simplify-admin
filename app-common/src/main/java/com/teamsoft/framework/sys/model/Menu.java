package com.teamsoft.framework.sys.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.teamsoft.framework.common.model.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.util.List;

/**
 * 菜单表
 *
 * @author zhangcc
 * @version 2016-6-7 下午3:40:23
 */
@Data
@Alias("Menu")
@EqualsAndHashCode(callSuper = true)
public class Menu extends Entity implements Serializable {
	private static final long serialVersionUID = -1L;

	// 上级菜单id
	private String parentId;
	// 菜单名
	@JsonProperty("title")
	private String name;
	// 菜单url
	@JsonProperty("href")
	private String url;
	// 菜单图标
	private String icon;
	// 下级子菜单
	private List<Menu> children;
	// 是否展开菜单
	private Boolean spread;

	// VO
	// 菜单功能列表
	private List<MenuFunction> funcs;
	// 是否有权限
	private Integer hasAuth;
	// 角色ID
	private String roleId;
	// 上级菜单不等于
	@JsonIgnore
	private String parentIdNe;

	/**
	 * 获取parentId为指定值的menu对象
	 */
	public static Menu getParentMenu(String parentId) {
		Menu menu = new Menu();
		menu.setParentId(parentId);
		return menu;
	}
}