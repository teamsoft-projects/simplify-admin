package com.teamsoft.framework.sys.model;

import com.teamsoft.framework.common.model.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.ibatis.type.Alias;

/**
 * 角色实体类
 * @author zhangcc
 * @version 2017/8/28
 */
@Data
@Alias("Role")
@EqualsAndHashCode(callSuper = true)
public class Role extends Entity {
	private static final long serialVersionUID = 1L;

	// 名称
	private String name;
	// 描述
	private String description;
}