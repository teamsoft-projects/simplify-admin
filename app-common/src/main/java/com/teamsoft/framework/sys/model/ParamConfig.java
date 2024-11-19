package com.teamsoft.framework.sys.model;

import com.teamsoft.framework.common.model.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.ibatis.type.Alias;

/**
 * 系统配置实体类
 *
 * @author zhangcc
 * @version 2017/12/20
 */
@Data
@Alias("ParamConfig")
@EqualsAndHashCode(callSuper = true)
public class ParamConfig extends Entity {
	private static final long serialVersionUID = 1L;

	// 配置项名称
	private String paramName;
	// 配置项
	private String paramKey;
	// 对应数值
	private String paramValue;
	// 拓展1
	private String foo1;
	// 拓展2
	private String foo2;
	// 提示信息
	private String tips;
}