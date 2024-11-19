package com.teamsoft.framework.common.web;

import com.teamsoft.framework.common.core.CommonConstants;
import com.teamsoft.framework.common.exception.InterfaceException;
import com.teamsoft.framework.common.model.ArrayEnumeration;
import com.teamsoft.framework.common.util.CommonWebUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * 重写ServletRequestWrapper
 * 针对加密请求, 将解密后的参数放入request域
 * @author zhangcc
 * @version 2017/9/7
 */
public class InterfaceRequestWrapper extends HttpServletRequestWrapper {
	private static Log log = LogFactory.getLog(InterfaceRequestWrapper.class);
	// 参数key-value列表
	private Map<String, String[]> parameters = new HashMap<>();
	// 参数名列表
	private ArrayEnumeration<String> parameterNames = new ArrayEnumeration<>();

	/**
	 * 封装请求参数获取方式
	 * 解密并重新对参数赋值
	 * @param request 请求上下文
	 */
	InterfaceRequestWrapper(HttpServletRequest request) {
		super(request);
		// 获取appId
		String appId = request.getParameter("appId");
		if (!StringUtils.hasLength(appId)) {
			throw new InterfaceException("param appid not found");
		}
		// 获取加密参数data
		String data = request.getParameter(CommonConstants.Encry.PARAMETER_KEY);
		if (!StringUtils.hasLength(data)) {
			throw new InterfaceException("param data not found");
		}
		// 获取AES加密盐值
		byte[] rule = CommonConstants.AES_SALT.get(appId);
		if (rule == null || rule.length == 0) {
			throw new InterfaceException("param appid error");
		}
		// 将解密后的参数列表放入自定义参数Map中
		Map<String, String[]> params;
		try {
			params = CommonWebUtil.decodeParameter(data, rule);
		} catch (Exception e) {
			e.printStackTrace();
			throw new InterfaceException("param error");
		}
		String[] timeStampArr = params.get("timeStamp");
		if (timeStampArr == null || timeStampArr.length < 1 || timeStampArr[0] == null) {
			throw new InterfaceException("need timestamp");
		}
		long timeStamp;
		try {
			timeStamp = Long.parseLong(timeStampArr[0]);
		} catch (NumberFormatException e) {
			throw new InterfaceException("timestamp " + timeStampArr[0] + " format error");
		}
		long nowStamp = new Date().getTime();
		if (nowStamp - timeStamp > 5 * 60 * 1000) {
			throw new InterfaceException("timestamp expired");
		}
		// 删除默认的data
		parameters.remove("data");
		parameters.putAll(request.getParameterMap());
		// 将加密参数的data字段改名，防止和真正的入参冲突
		parameters.put("data_from_encrypt", new String[]{data});
		parameters.putAll(params);
		// 将所有参数key放入参数列表
		for (Map.Entry<String, String[]> entry : parameters.entrySet()) {
			parameterNames.add(entry.getKey());
		}
	}

	/**
	 * 获取参数Map
	 * @return 参数Map集合
	 */
	public Map<String, String[]> getParameterMap() {
		return this.parameters;
	}

	/**
	 * 参数从成员变量pamameters中获取
	 * @param key 参数名
	 * @return 参数值
	 */
	public String getParameter(String key) {
		String[] values = parameters.get(key);
		return values == null || values.length == 0 ? null : values[0];
	}

	/**
	 * 数组型参数获取方式重写
	 * @param key 参数名
	 * @return 参数值
	 */
	public String[] getParameterValues(String key) {
		return parameters.get(key);
	}

	/**
	 * 获取参数名列表
	 * @return 参数名枚举列表
	 */
	public Enumeration<String> getParameterNames() {
		return this.parameterNames;
	}
}