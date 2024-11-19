package com.teamsoft.framework.common.annotation;

import com.teamsoft.framework.common.model.EncryptType;

import java.lang.annotation.*;

import static com.teamsoft.framework.common.core.CommonConstants.FrameWork.DEFAULT_AES_KEY_HIDDEN;

/**
 * 字段加解密注解
 * <p>注意！用到Mybatis中查询的字段，不要设置这个注解</p>
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Encrypt {
	// 加密类型
	EncryptType type() default EncryptType.ENCRY_AES;

	// 翻译加密字段秘钥
	String encryKey() default DEFAULT_AES_KEY_HIDDEN;

	// 翻译的字段是否是多个的, 默认单个
	boolean isMulti() default false;

	// 多个值的分隔符
	String separator() default ",";
}