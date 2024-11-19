package com.teamsoft.framework.sys.service;

import com.teamsoft.framework.common.service.CommonService;
import com.teamsoft.framework.sys.mapper.DictionaryMapper;
import com.teamsoft.framework.sys.model.Dictionary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 字典服务类
 * @author zhangcc
 * @version 2017/09/11
 */
@Service
public class DictionaryService extends CommonService<Dictionary> implements IDictionaryService {
	@Autowired
	public DictionaryService(DictionaryMapper mapper) {
		super.mapper = mapper;
	}

	/**
	 * 获取排序码最大值
	 * @return 排序码最大值
	 */
	public Integer getMaxSort(String groupCode) {
		return ((DictionaryMapper) mapper).getMaxSort(groupCode);
	}
}