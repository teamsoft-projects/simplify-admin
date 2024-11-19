package com.teamsoft.framework.sys.service;

import com.teamsoft.framework.common.service.CommonService;
import com.teamsoft.framework.sys.mapper.DictionaryGroupMapper;
import com.teamsoft.framework.sys.model.DictionaryGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 字典分组服务类
 * @author zhangcc
 * @version 2017/09/11
 */
@Service
public class DictionaryGroupService extends CommonService<DictionaryGroup> implements IDictionaryGroupService {
	@Autowired
	public DictionaryGroupService(DictionaryGroupMapper mapper) {
		super.mapper = mapper;
	}
}