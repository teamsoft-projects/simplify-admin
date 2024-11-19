package com.teamsoft.framework.sys.controller;

import com.teamsoft.framework.common.model.ResultInfo;
import com.teamsoft.framework.sys.model.ParamConfig;
import com.teamsoft.framework.sys.service.IParamConfigService;
import com.teamsoft.framework.sys.vo.ParamConfigVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import static com.teamsoft.framework.common.core.CommonConstants.System.SYSTEM_CONFIG_CACHE;

/**
 * 系统配置控制类
 * @author zhangcc
 * @version 2017/12/20
 */
@Controller
@RequestMapping("sys/paramConfig")
public class ParamConfigController {
	private final IParamConfigService paramConfigService;

	/**
	 * 构造方法注入
	 * @param paramConfigService 系统配置服务接口
	 */
	@Autowired
	public ParamConfigController(IParamConfigService paramConfigService) {
		this.paramConfigService = paramConfigService;
	}

	/**
	 * 获取系统参数列表
	 */
	@RequestMapping("list")
	public ResultInfo list() {
		return ResultInfo.getSuccessInfo(paramConfigService.list());
	}

	/**
	 * 更新系统配置参数
	 * @param paramConfig 配置参数列表
	 */
	@RequestMapping("update")
	public ResultInfo save(ParamConfigVO paramConfig) {
		// 获取参数列表
		List<ParamConfig> paramConfigs = paramConfig.getParamConfigs();
		paramConfigs.removeIf(config -> config.getParamValue() == null || "SPLIT_SYMBOL".equals(config.getFoo1()));

		// 更新所有配置到数据库
		paramConfigService.updateAll(paramConfigs);

		// 更新缓存
		for (ParamConfig config : paramConfigs) {
			SYSTEM_CONFIG_CACHE.put(config.getParamKey(), config.getParamValue());
		}

		return ResultInfo.getSuccessInfo();
	}
}