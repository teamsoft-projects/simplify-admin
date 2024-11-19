package com.teamsoft.framework.common.annotation;

import java.lang.annotation.*;

/**
 * Excel实体类的列注解
 * @author zhangcc
 * @version 2017/9/18
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface ExcelColumn {
	// excel文件中列下标
	int value();
}