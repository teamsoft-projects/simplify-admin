package com.teamsoft.framework.sys.service;

import com.teamsoft.framework.common.model.Page;
import com.teamsoft.framework.sys.model.ParamConfig;

import java.util.List;

/**
 * 系统配置服务接口
 * @author zhangcc
 * @version 2017/12/20
 */
public interface IParamConfigService {
	/**
	 * 查询所有配置参数
	 * @return list
	 */
	List<ParamConfig> list();

	/**
	 * 查询所有配置参数
	 * @return list
	 */
	List<ParamConfig> listByEntity(ParamConfig entity, Page page);

	/**
	 * 根据编码获取
	 * @param key 配置key
	 * @return 配置项内容
	 */
	ParamConfig getByParamKey(String key);

	/**
	 * 更新配置参数
	 * @param paramConfig 配置对象
	 */
	void update(ParamConfig paramConfig);

	/**
	 * 更新所有配置参数
	 * @param paramConfigs 参数列表
	 */
	void updateAll(List<ParamConfig> paramConfigs);
}