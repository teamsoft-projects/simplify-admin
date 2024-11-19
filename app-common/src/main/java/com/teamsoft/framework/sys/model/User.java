package com.teamsoft.framework.sys.model;

import com.teamsoft.framework.common.model.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.ibatis.type.Alias;


@Data
@Alias("User")
@EqualsAndHashCode(callSuper = true)
public class User extends Entity {
	private static final long serialVersionUID = 1L;

	// 登录号
	private String loginName;
	// 登录密码
	private String password;
	// 名字(昵称)
	private String name;
	// 角色编号
	private String roleId;
	// 手机号
	private String mobile;
	// 描述
	private String description;
	// 备注
	private String remarks;
	// 是否默认密码(1.是 0.否)
	private Integer isDefaultPasswd;
	// 创建时间
	private String createTime;
	// VO
	// 角色名
	private String roleName;
	// 排序码大于
	private Integer sortMin;
	// 排序码小于
	private Integer sortMax;
}