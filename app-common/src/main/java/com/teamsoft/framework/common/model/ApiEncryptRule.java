package com.teamsoft.framework.common.model;

import lombok.Data;

/**
 * API加密规则实体类
 * @author dominealex
 * @version 2019/9/24
 */
@Data
public class ApiEncryptRule {
	// 别名
	private String alias;
	// appId
	private String appId;
	// appSecret(明文)
	private String appSecret;
	// appSecret(服务端密文)
	private String appSecretServerHidden;
	// 终端混淆规则
	private String mixRuleTerminal;
	// appSecret(终端密文)
	private String appSecretTerminalHidden;
	// spring配置项
	private String springConfig;

	@Override
	public String toString() {
		return "名称: " + alias +
				"\nappId: " + appId +
				"\nappSecret: " + appSecret +
				"\nappSecretServerHidden: " + appSecretServerHidden +
				"\nmixRuleTerminal: " + mixRuleTerminal +
				"\nappSecretTerminalHidden: " + appSecretTerminalHidden +
				"\nspringConfig: \n" + springConfig;
	}
}