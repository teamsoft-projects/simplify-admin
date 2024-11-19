package com.teamsoft.framework.sys.model;

import com.teamsoft.framework.common.model.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.ibatis.type.Alias;

/**
 * 字典分组实体类
 *
 * @author zhangcc
 * @version 2017/09/11
 */
@Data
@Alias("DictionaryGroup")
@EqualsAndHashCode(callSuper = true)
public class DictionaryGroup extends Entity {
	private static final long serialVersionUID = 1L;

	// 编码
	private String code;
	// 名称
	private String name;
	//排序码
	private Integer sort;
	// 排序码大于
	private Integer sortMin;
	// 排序码小于
	private Integer sortMax;
}