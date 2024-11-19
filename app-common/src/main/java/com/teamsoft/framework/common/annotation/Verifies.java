package com.teamsoft.framework.common.annotation;

import java.lang.annotation.*;

/**
 * 注解校验容器
 * @author zhangcc
 * @version 2017/9/25
 */
@Target(ElementType.FIELD)
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface Verifies {
	Verify[] value();
}