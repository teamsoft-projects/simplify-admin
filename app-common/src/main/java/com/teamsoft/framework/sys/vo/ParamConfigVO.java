package com.teamsoft.framework.sys.vo;

import com.teamsoft.framework.sys.model.ParamConfig;
import lombok.Data;

import java.util.List;

/**
 * 系统配置VO
 * @author zhangcc
 * @version 2018/6/12
 */
@Data
public class ParamConfigVO {
	private List<ParamConfig> paramConfigs;
}