package com.teamsoft.framework.sys.model;

import com.teamsoft.framework.common.model.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.ibatis.type.Alias;

/**
 * 菜单功能实体类
 *
 * @author zhangcc
 * @version 2017/8/28
 */
@Data
@Alias("MenuFunction")
@EqualsAndHashCode(callSuper = true)
public class MenuFunction extends Entity {
	private static final long serialVersionUID = 1L;

	// 菜单编号
	private String menuId;
	// 图标
	private String icon;
	// 唯一标识
	private String identify;
	// 名称
	private String name;
	// 描述
	private String description;
	// 链接地址
	private String url;
	// VO
	// 是否有权限
	private Integer hasAuth;
}