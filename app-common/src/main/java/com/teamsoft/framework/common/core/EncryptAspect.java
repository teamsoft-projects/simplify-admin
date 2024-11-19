package com.teamsoft.framework.common.core;

import com.teamsoft.framework.common.model.ResultInfo;
import com.teamsoft.framework.common.util.ReflectUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Objects;

/**
 * 数据加密/解密切面处理
 */
@Aspect
@Component
public class EncryptAspect {
	@Pointcut("execution(public * com.teamsoft.*.*.controller.*.*(..))")
	public void pointcut() {
	}

	/**
	 * 进入Controller方法前加密字段
	 */
	@Before(value = "pointcut()")
	public void encrypt(JoinPoint point) {
		Object[] args = point.getArgs();

		if (Objects.isNull(args)) {
			return;
		}

		for (Object arg : args) {
			if (Objects.isNull(arg) || args.getClass().isPrimitive()) {
				continue;
			}
			if (arg instanceof Collection) {
				Collection<?> list = (Collection<?>) arg;
				if (CollectionUtils.isEmpty(list)) {
					continue;
				}
				for (Object row : list) {
					ReflectUtil.encryptDTO(row);
				}
			} else {
				ReflectUtil.encryptDTO(arg);
			}
		}
	}

	/**
	 * 从Controller方法返回时解密字段
	 */
	@AfterReturning(pointcut = "pointcut()", returning = "result")
	public Object decrypt(Object result) {
		if (Objects.isNull(result)) {
			return null;
		}
		if (result.getClass().isPrimitive()) {
			return result;
		}

		if (result instanceof ResultInfo) {
			result = ((ResultInfo) result).getData();
		}

		if (result instanceof Collection) {
			Collection<?> list = (Collection<?>) result;
			if (CollectionUtils.isEmpty(list)) {
				return result;
			}
			for (Object row : list) {
				ReflectUtil.decryptDTO(row);
			}
		} else {
			ReflectUtil.decryptDTO(result);
		}

		return result;
	}
}