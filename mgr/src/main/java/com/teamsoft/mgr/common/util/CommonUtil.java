package com.teamsoft.mgr.common.util;

import com.teamsoft.framework.common.core.CommonConstants;
import com.teamsoft.framework.common.exception.BusinessException;
import com.teamsoft.framework.common.mapper.CommonMapper;
import com.teamsoft.framework.common.util.CommonStandardUtil;
import com.teamsoft.framework.common.util.EncryptUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 公共工具类
 * @author zhangcc
 * @version 2015-5-16 下午10:22:33
 */
@Slf4j
public class CommonUtil extends CommonStandardUtil {
	/**
	 * 根据所选范围生成随机整数
	 * @param min 最小值
	 * @param max 最大值
	 * @return 在最小(包含)和最大(不包含)之间的随机整数
	 */
	public static Integer getRandomNumByRange(Integer min, Integer max) {
		if (min == null || max == null || min > max) {
			throw new BusinessException("随机范围选择错误, 最小值: " + min + ", 最大值: " + max);
		}
		if (min.equals(max)) {
			return min;
		}
		return new Double(CommonConstants.System.SYSTEM_RANDOM_MAKER.nextDouble() * (max - min)).intValue() + min;
	}

	/**
	 * 获取异常详细信息
	 * @param e 异常
	 * @return 异常详细信息，包括第一异常堆栈的异常类型和异常消息
	 */
	public static String getExceptionMsg(Throwable e) {
		StackTraceElement[] stackTraces = e.getStackTrace();
		if (stackTraces == null || stackTraces.length == 0) {
			return e.getMessage();
		}
		StackTraceElement stackTrace = stackTraces[0];
		if (stackTrace == null) {
			return e.getMessage();
		}

		return stackTrace.getClassName() + ": " + e.getMessage();
	}

	/**
	 * 以定宽拆分字符串
	 * @return 拆分后的数组
	 */
	public static String[] splitStrByWidth(String source, int width) {
		if (!StringUtils.hasLength(source) || width <= 0) {
			return new String[0];
		}
		int len = source.length();
		int per = len % width == 0 ? len / width : len / width + 1;
		String[] ret = new String[per];
		for (int i = 0; i < per; i++) {
			int len1 = source.substring(i * width).length();
			if (len1 < width) {
				ret[i] = source.substring(i * width, i * width + len1);
			} else {
				ret[i] = source.substring(i * width, (i + 1) * width);
			}
		}
		return ret;
	}

	/**
	 * 通过调换规则, 混淆加密
	 */
	public static String mix(String appSecret, String mixRule) {
		int len = 64; // 混淆加密次数
		byte[] b;
		b = EncryptUtil.Base64.decode(mixRule).getBytes(StandardCharsets.UTF_8);
		StringBuilder sb = new StringBuilder(appSecret);
		for (int i = 0; i < len; i++) {
			int second = b[i * 2 + 1];
			int first = b[i * 2];
			String secondStr = String.valueOf(sb.charAt(second));
			String firstStr = String.valueOf(sb.charAt(first));
			sb.replace(second, second + 1, firstStr);
			sb.replace(first, first + 1, secondStr);
		}
		return sb.toString();
	}

	/**
	 * 通过正则表达式获取参数
	 */
	public static List<String> getMathersByRegex(String source, String regex) {
		List<String> ret = new ArrayList<>();
		if (!StringUtils.hasLength(source) || !StringUtils.hasLength(regex)) {
			return ret;
		}

		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(source);
		while (m.find()) {
			String key = m.group(1);
			ret.add(key);
		}

		return ret;
	}

	/**
	 * 带正则表达式替换
	 */
	public static String replaceWithRegex(String source, String regex, Map<String, String> dataMap) {
		if (!StringUtils.hasLength(source) || !StringUtils.hasLength(regex) || Objects.isNull(dataMap) || dataMap.isEmpty()) {
			return source;
		}
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(source);
		while (m.find()) {
			String key = m.group(1);
			String keyWithBracket = m.group();
			String value = dataMap.get(key);
			if (StringUtils.hasLength(value)) {
				source = source.replace(keyWithBracket, dataMap.get(key));
			}
		}

		return source;
	}
}