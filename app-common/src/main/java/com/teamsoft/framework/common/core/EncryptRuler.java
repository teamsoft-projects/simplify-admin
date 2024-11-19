package com.teamsoft.framework.common.core;

import com.teamsoft.framework.common.model.ApiEncryptRule;
import com.teamsoft.framework.common.util.CommonStandardUtil;
import com.teamsoft.framework.common.util.EncryptUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * 接口加密规则类
 * @author dominealex
 * @version 2019/9/24
 */
public class EncryptRuler {
	// 签名源字符
	private static final String[] chars = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
	// 源字符长度
	private static final int charsLen = chars.length;

	/**
	 * 创建一套接口加密规则
	 * @param terminals 终端列表
	 */
	public static void create(List<String> terminals) {
		// 生成服务端统一混淆规则
		String mixRule = generateMixRule();
		create(terminals, mixRule);
	}

	/**
	 * 创建一套接口加密规则
	 * @param terminals     终端列表
	 * @param mixRuleServer 服务端混淆规则
	 */
	public static void create(List<String> terminals, String mixRuleServer) {
		List<ApiEncryptRule> rules = generateEncryptRules(terminals, mixRuleServer);
		System.out.println("规则生成完成. 本次生成规则数: " + terminals.size());
		System.out.println("服务端混淆规则: " + mixRuleServer);
		rules.forEach(System.out::println);
	}

	/**
	 * 解密已混淆密钥
	 * @param mixRule         混淆规则
	 * @param appSecretHidden 密钥(密文)
	 * @return 解密后的密钥
	 */
	public static String unMixSecret(String mixRule, String appSecretHidden) {
		String appSecret = CommonStandardUtil.unmix(appSecretHidden, mixRule);
		return EncryptUtil.Base64.decode(appSecret);
	}

	/**
	 * 生成加密请求参数
	 * @param appId      appId
	 * @param appSecret  密钥
	 * @param parameters 参数
	 * @return 生成的加密请求参数
	 */
	public static String generateParams(String appId, String appSecret, Map<String, Object> parameters) {
		byte[] salt = appSecret.getBytes(StandardCharsets.UTF_8);
		StringBuilder paramBuilder = new StringBuilder(32);
		if (!parameters.isEmpty()) {
			boolean isFirst = true;
			for (Map.Entry<String, Object> entry : parameters.entrySet()) {
				if (isFirst) {
					isFirst = false;
				} else {
					paramBuilder.append("&");
				}
				paramBuilder.append(entry.getKey()).append("=").append(entry.getValue());
			}
			paramBuilder.append("&");
		}
		paramBuilder.append("timeStamp=").append(new Date().getTime());
		String param = EncryptUtil.Base64.encode(paramBuilder.toString());
		String data = EncryptUtil.AES.encode(salt, param);
		assert data != null;
		try {
			data = URLEncoder.encode(data, StandardCharsets.UTF_8.name());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "appId=" + appId + "&data=" + data;
	}

	/**
	 * 生成加密请求参数
	 * @param appId           appId
	 * @param mixRule         混淆规则
	 * @param appSecretHidden 密钥(密文)
	 * @param parameters      参数
	 * @return 生成的加密请求参数
	 */
	public static String generateParams(String appId, String mixRule, String appSecretHidden, Map<String, Object> parameters) {
		String appSecret = unMixSecret(mixRule, appSecretHidden);
		return generateParams(appId, appSecret, parameters);
	}

	/**
	 * 生成混淆规则
	 */
	private static String generateMixRule() {
		int len = 53; // 待调换字符长度
		int count = 64; // 调换次数
		Random r = new Random();
		byte[] b = new byte[count * 2];
		for (int i = 0; i < count; i++) {
			int first = r.nextInt(len);
			int second = -1;
			while (second == -1 || first == second) {
				second = r.nextInt(len);
			}
			b[i * 2] = (byte) first;
			b[i * 2 + 1] = (byte) second;
		}
		String encoded = EncryptUtil.Base64.encode(new String(b, StandardCharsets.UTF_8));
		return encoded.replace("\r\n", "");
	}

	/**
	 * 生成加密规则
	 * @param terminals     待生成终端列表
	 * @param mixRuleServer 服务端混淆规则
	 */
	private static List<ApiEncryptRule> generateEncryptRules(List<String> terminals, String mixRuleServer) {
		List<ApiEncryptRule> rules = new ArrayList<>();
		for (String terminal : terminals) {
			// 终端混淆规则
			ApiEncryptRule rule = new ApiEncryptRule();
			rule.setAlias(terminal);
			// 终端混淆规则
			String mixRuleTerminal = generateMixRule();
			rule.setMixRuleTerminal(mixRuleTerminal);
			// appId
			String appId = getRandomChar(12, -1);
			rule.setAppId(appId);
			// appSecret(明文)
			String appSecret = getRandomChar(48, 8);
			rule.setAppSecret(appSecret);
			String appSecretBase64 = EncryptUtil.Base64.encode(appSecret);
			// appSecret(服务端密文)
			String appSecretServerHidden = mixSecret(mixRuleServer, appSecretBase64);
			rule.setAppSecretServerHidden(appSecretServerHidden);
			// appSecret(终端密文)
			String appSecretTerminalHidden = mixSecret(mixRuleTerminal, appSecretBase64);
			rule.setAppSecretTerminalHidden(appSecretTerminalHidden);
			String sb = "<!-- " + terminal + " -->\n" +
					"<entry key=\"" + appId + "\" value=\"" +
					appSecretServerHidden + "\"/>\n";
			rule.setSpringConfig(sb);
			rules.add(rule);
		}
		return rules;
	}

	/**
	 * 获取指定长度的随机字符
	 * @param width      字符长度
	 * @param splitIndex 分隔符位置, 如果传入<=0的数表示不分割
	 * @return 生成的随机字符
	 */
	private static String getRandomChar(int width, int splitIndex) {
		StringBuilder source = new StringBuilder();
		Random ran = new Random();
		for (int i = 0; i < width; i++) {
			source.append(chars[ran.nextInt(charsLen)]);
			if (splitIndex > 0 && i != width - 1 && (i + 1) % splitIndex == 0) {
				source.append("-");
			}
		}
		return source.toString();
	}

	/**
	 * 通过调换规则, 混淆加密
	 */
	private static String mixSecret(String replaceRule, String appSecret) {
		int len = 64; // 混淆加密次数
		byte[] b;
		b = EncryptUtil.Base64.decode(replaceRule).getBytes(StandardCharsets.UTF_8);
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
}