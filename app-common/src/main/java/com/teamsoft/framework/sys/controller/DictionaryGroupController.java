package com.teamsoft.framework.sys.controller;

import com.teamsoft.framework.common.controller.CommonController;
import com.teamsoft.framework.sys.model.DictionaryGroup;
import com.teamsoft.framework.sys.service.IDictionaryGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 字典分组控制类
 * @author zhangcc
 * @version 2017/09/11
 */
@Controller
@RequestMapping("sys/dictionaryGroup")
public class DictionaryGroupController extends CommonController<DictionaryGroup> {
	/**
	 * 构造方法注入
	 */
	@Autowired
	public DictionaryGroupController(IDictionaryGroupService service) {
		this.service = service;
	}
}