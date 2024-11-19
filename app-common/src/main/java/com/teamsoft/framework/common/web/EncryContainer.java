package com.teamsoft.framework.common.web;

import com.teamsoft.framework.common.core.CommonConstants;
import com.teamsoft.framework.common.util.CommonStandardUtil;
import com.teamsoft.framework.common.util.EncryptUtil;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * 加密规则容器
 * @author zhangcc
 * @version 2017/9/7
 */
public class EncryContainer {
	// 密钥混淆规则
	private String mixRule;
	// AES加密盐值列表
	private Map<String, String> encrySalt;

	// 从配置文件中设置密钥混淆规则
	public void setMixRule(String mixRule) {
		this.mixRule = mixRule;
	}

	/**
	 * 从配置文件中获取加密规则并将规则的以byte数组的形式放入常量类
	 * @param encrySalt 加密规则列表
	 * @throws Exception 未配置或解析规则出错
	 */
	public void setEncrySalt(Map<String, String> encrySalt) throws Exception {
		if (encrySalt == null || encrySalt.isEmpty()) {
			throw new Exception("未配置加密规则");
		}
		for (Map.Entry<String, String> entry : encrySalt.entrySet()) {
			// 获取AES加密盐值
			String salt = entry.getValue();
			if (!StringUtils.hasLength(salt)) {
				throw new Exception();
			}
			// 解密混淆加密
			salt = CommonStandardUtil.unmix(salt, mixRule);
			// base64解密
			salt = EncryptUtil.Base64.decode(salt);
			CommonConstants.AES_SALT.put(entry.getKey(), salt.getBytes(StandardCharsets.UTF_8));
		}
		this.encrySalt = encrySalt;
	}
}