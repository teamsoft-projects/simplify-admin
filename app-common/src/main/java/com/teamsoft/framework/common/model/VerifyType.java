package com.teamsoft.framework.common.model;

/**
 * 参数校验相关枚举
 * @author zhangcc
 * @version 2017/9/21
 */
public enum VerifyType {
	// 无校验
	NONE,
	// 必填
	REQUIRED,
	// 整型
	INTEGER,
	// 浮点型
	DOUBLE,
	// 指定长度(定长)
	LENGTH,
	// 长度小于X
	LENLT,
	// 长度小于等于X
	LENLTE,
	// 长度大于X
	LENGT,
	// 长度大于等于X
	LENGTE,
	// 范围长度
	LENRANGE,
	// 正则表达式
	REGEX
}