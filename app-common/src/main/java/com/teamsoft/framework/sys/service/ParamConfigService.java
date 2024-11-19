package com.teamsoft.framework.sys.service;

import com.teamsoft.framework.common.model.Page;
import com.teamsoft.framework.sys.mapper.ParamConfigMapper;
import com.teamsoft.framework.sys.model.ParamConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.teamsoft.framework.common.core.CommonConstants.System.SYSTEM_CONFIG_CACHE;

/**
 * 系统配置服务类
 * @author zhangcc
 * @version 2017/12/20
 */
@Service
public class ParamConfigService implements IParamConfigService {
	private final ParamConfigMapper paramConfigMapper;

	@Autowired
	public ParamConfigService(ParamConfigMapper paramConfigMapper) {
		this.paramConfigMapper = paramConfigMapper;
	}

	/**
	 * 获取配置信息列表
	 * @return 配置信息列表
	 */
	public List<ParamConfig> list() {
		return paramConfigMapper.list(null);
	}

	/**
	 * 查询所有配置参数
	 * @return list
	 */
	public List<ParamConfig> listByEntity(ParamConfig entity, Page page) {
		return paramConfigMapper.listByEntity(entity, page);
	}

	/**
	 * 根据编码获取
	 * @param key 配置key
	 * @return 配置项内容
	 */
	public ParamConfig getByParamKey(String key) {
		return paramConfigMapper.getByParamKey(key);
	}

	/**
	 * 更新配置参数
	 * @param paramConfig 配置对象
	 */
	public void update(ParamConfig paramConfig) {
		paramConfigMapper.update(paramConfig);
		SYSTEM_CONFIG_CACHE.put(paramConfig.getParamKey(), paramConfig.getParamValue());
	}

	/**
	 * 更新所有配置参数
	 * @param paramConfigs 参数列表
	 */
	public void updateAll(List<ParamConfig> paramConfigs) {
		paramConfigMapper.updateAll(paramConfigs);
		paramConfigs.forEach(paramConfig -> SYSTEM_CONFIG_CACHE.put(paramConfig.getParamKey(), paramConfig.getParamValue()));
	}
}