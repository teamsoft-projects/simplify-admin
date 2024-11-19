package com.teamsoft.framework.common.annotation;

import java.lang.annotation.*;

/**
 * 禁用字典翻译注解
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface DisableTranslate {
}