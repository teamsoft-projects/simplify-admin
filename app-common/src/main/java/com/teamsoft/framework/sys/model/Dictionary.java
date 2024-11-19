package com.teamsoft.framework.sys.model;

import com.teamsoft.framework.common.model.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.ibatis.type.Alias;

/**
 * 字典实体类
 * @author zhangcc
 * @version 2017/09/11
 */
@Data
@Alias("Dictionary")
@EqualsAndHashCode(callSuper = true)
public class Dictionary extends Entity {
	private static final long serialVersionUID = 1L;

	// 编码
	private String code;
	// 分组编码
	private String groupCode;
	// 连接码
	private String joinCode;
	// 名称
	private String name;
	//排序码
	private Integer sortOrder;

	// VO
	private String groupName;
	private String groupCodeLike;

	public Dictionary() {
	}

	public Dictionary(String groupCode, String code, String name) {
		this.groupCode = groupCode;
		this.code = code;
		this.name = name;
	}
}