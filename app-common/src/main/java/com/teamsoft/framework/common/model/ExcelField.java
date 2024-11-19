package com.teamsoft.framework.common.model;

import lombok.Data;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

/**
 * Excel读写注解数据实体类
 * @author zhangcc
 * @version 2017/10/16
 */
@Data
public class ExcelField {
	private Field field;
	private Integer index;
	private Type type;
}