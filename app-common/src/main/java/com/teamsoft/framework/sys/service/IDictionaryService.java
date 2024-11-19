package com.teamsoft.framework.sys.service;

import com.teamsoft.framework.common.service.ICommonService;
import com.teamsoft.framework.sys.model.Dictionary;

/**
 * 字典服务接口
 * @author zhangcc
 * @version 2017/09/11
 */
public interface IDictionaryService extends ICommonService<Dictionary> {
	/**
	 * 获取排序码最大值
	 * @return 排序码最大值
	 */
	Integer getMaxSort(String groupCode);
}