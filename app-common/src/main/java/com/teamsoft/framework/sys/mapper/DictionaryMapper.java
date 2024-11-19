package com.teamsoft.framework.sys.mapper;

import com.teamsoft.framework.common.mapper.CommonMapper;
import com.teamsoft.framework.sys.model.Dictionary;
import org.apache.ibatis.annotations.Param;

/**
 * 字典数据库操作接口
 * @author zhangcc
 * @version 2017/09/11
 */
public interface DictionaryMapper extends CommonMapper<Dictionary> {
	/**
	 * 获取排序码最大值
	 * @return 排序码最大值
	 */
	Integer getMaxSort(@Param("groupCode") String groupCode);
}