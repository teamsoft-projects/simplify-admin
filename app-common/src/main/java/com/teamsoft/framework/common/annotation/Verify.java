package com.teamsoft.framework.common.annotation;

import com.teamsoft.framework.common.model.VerifyScene;
import com.teamsoft.framework.common.model.VerifyType;

import java.lang.annotation.*;

/**
 * 参数校验注解
 * @author zhangcc
 * @version 2017/9/21
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Repeatable(Verifies.class)
public @interface Verify {
	// 使用场景
	VerifyScene[] scene() default VerifyScene.ALL;

	// 校验类型(多选), 与VerifyEnum枚举类型对应
	VerifyType[] value() default VerifyType.NONE;

	// 长度限制(定长)
	int[] length() default -1;

	// 正则
	String[] regex() default "";
}