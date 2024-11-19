package com.teamsoft.framework.sys.controller;

import com.teamsoft.framework.common.controller.CommonController;
import com.teamsoft.framework.common.model.ResultInfo;
import com.teamsoft.framework.common.model.VerifyScene;
import com.teamsoft.framework.common.util.CommonStandardUtil;
import com.teamsoft.framework.common.util.CommonWebUtil;
import com.teamsoft.framework.sys.model.Dictionary;
import com.teamsoft.framework.sys.service.IDictionaryService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.teamsoft.framework.common.core.CommonConstants.System.DIC_CACHE;

/**
 * 字典控制类
 * @author zhangcc
 * @version 2017/09/11
 */
@Controller
@RequestMapping("sys/dictionary")
public class DictionaryController extends CommonController<Dictionary> {
	/**
	 * 构造方法注入
	 */
	@Autowired
	public DictionaryController(IDictionaryService service) {
		this.service = service;
	}

	/**
	 * 从缓存中获取制定分组的数据字典列表
	 * @param groupCode 分组code
	 */
	@PostMapping("getDicByGroupFromCache")
	public ResultInfo getDicByGroupFromCache(String groupCode) {
		return ResultInfo.getSuccessInfo(DIC_CACHE.getList(groupCode));
	}

	/**
	 * 更新/保存，带排序码
	 */
	@ApiOperation(value = "带排序码更新", notes = "更新/保存，带排序码")
	@PostMapping("saveWithSort")
	public ResultInfo saveWithSort(Dictionary dic) {
		String id = dic.getId();
		if (StringUtils.hasLength(id)) { // 更新
			CommonWebUtil.verify(dic, VerifyScene.UPDATE);
			service.update(dic);
		} else {
			CommonWebUtil.verify(dic, VerifyScene.SAVE);
			dic.setId(CommonStandardUtil.generateUUId()); // 设置UUId为数据主键
			if (dic.getSortOrder() == null) {
				dic.setSortOrder(((IDictionaryService) service).getMaxSort(dic.getGroupCode()));
			}
			service.save(dic);
		}
		// 更新字典缓存
		DIC_CACHE.put(dic.getGroupCode(), dic.getCode(), dic);
		return ResultInfo.getSuccessInfo();
	}
}