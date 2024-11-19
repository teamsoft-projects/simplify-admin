package com.teamsoft.mgr.base.controller;

import com.teamsoft.framework.common.model.ResultInfo;
import com.teamsoft.framework.sys.model.Dictionary;
import com.teamsoft.mgr.base.model.District;
import com.teamsoft.mgr.base.service.IDistrictService;
import com.teamsoft.mgr.common.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static com.teamsoft.framework.common.core.CommonConstants.System.DIC_CACHE;
import static com.teamsoft.framework.common.core.CommonConstants.System.FORMATTER_YMDHMS;
import static com.teamsoft.mgr.common.model.Constants.Business.DISTRICT_PARENTID_CACHE;
import static com.teamsoft.mgr.common.model.Constants.System.*;

/**
 * 区域控制类
 * @author zhangcc
 * @version 2023/10/16
 */
@Slf4j
@Controller
@RequestMapping("base/district")
public class DistrictController {
	@Autowired
	private IDistrictService districtService;

	/**
	 * 通过parentId获取区域列表
	 * @param parentId 区域上级ID
	 */
	@PostMapping("getByParentId")
	public ResultInfo getByParentId(Integer parentId) {
		return ResultInfo.getSuccessInfo(DISTRICT_PARENTID_CACHE.get(parentId));
	}

	/**
	 * 初始化缓存数据，项目启动时执行
	 */
	@PostConstruct
	public void initData() {
		List<District> districts = districtService.listByEntity(null, null);
		districts.forEach(d -> {
			Dictionary dic = new Dictionary();
			dic.setCode(String.valueOf(d.getId()));
			dic.setName(d.getName());
			String groupCode;
			switch (d.getLevel()) {
				case 1: {
					groupCode = CACHE_KEY_PROVINCE_LIST;
					break;
				}
				case 2: {
					groupCode = CACHE_KEY_CITY_LIST;
					break;
				}
				case 3: {
					groupCode = CACHE_KEY_AREA_LIST;
					break;
				}
				default: {
					groupCode = "unknown";
				}
			}
			DIC_CACHE.put(groupCode, dic.getCode(), dic);
			List<District> districtList = DISTRICT_PARENTID_CACHE.computeIfAbsent(d.getParentId(), k -> new ArrayList<>());
			districtList.add(d);
		});
	}
}