package com.teamsoft.framework.common.annotation;

import java.lang.annotation.*;

/**
 * 字典翻译注解
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Translate {
	// 翻译来源字段
	String from();

	// 分组码
	String groupCode();

	// 翻译的字段是否是多个的, 默认单个
	boolean isMulti() default false;

	// 如果是多个的，每个值的分隔符
	String separator() default ",";
}