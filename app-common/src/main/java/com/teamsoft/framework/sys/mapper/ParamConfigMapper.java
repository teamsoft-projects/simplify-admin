package com.teamsoft.framework.sys.mapper;

import com.teamsoft.framework.common.mapper.CommonMapper;
import com.teamsoft.framework.sys.model.ParamConfig;
import org.apache.ibatis.annotations.Param;

/**
 * 系统配置数据库操作接口
 * @author zhangcc
 * @version 2017/12/20
 */
public interface ParamConfigMapper extends CommonMapper<ParamConfig> {
	/**
	 * 根据编码获取
	 * @param key 配置key
	 * @return 配置项内容
	 */
	ParamConfig getByParamKey(@Param("key") String key);
}