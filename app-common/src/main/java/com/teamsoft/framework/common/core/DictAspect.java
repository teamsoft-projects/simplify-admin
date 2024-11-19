package com.teamsoft.framework.common.core;

import com.teamsoft.framework.common.annotation.DisableTranslate;
import com.teamsoft.framework.common.model.ResultInfo;
import com.teamsoft.framework.common.util.ReflectUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.aspectj.lang.reflect.SourceLocation;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Objects;

/**
 * 数据字典翻译切面处理
 */
@Aspect
@Component
public class DictAspect {
	@Pointcut("execution(public * com.teamsoft.*.*.controller.*.*(..))")
	public void pointcut() {
	}

	@AfterReturning(pointcut = "pointcut()", returning = "result")
	public Object translate(final JoinPoint point, Object result) {
		Signature signature = point.getSignature();
		MethodSignature methodSignature = (MethodSignature) signature;
		Method method = methodSignature.getMethod();
		DisableTranslate dtAnnotation = method.getDeclaredAnnotation(DisableTranslate.class);
		if (Objects.nonNull(dtAnnotation)) {
			return result;
		}

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
				ReflectUtil.translateDTO(row);
			}
		} else {
			ReflectUtil.translateDTO(result);
		}

		return result;
	}
}