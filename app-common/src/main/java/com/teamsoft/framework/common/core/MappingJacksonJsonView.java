package com.teamsoft.framework.common.core;

import com.teamsoft.framework.common.model.ResponseInfo;
import com.teamsoft.framework.common.model.ResultInfo;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.util.Map;

/**
 * .json后缀返回json数据过滤
 * @author zhangcc
 * @version 2015-5-16 下午11:51:44
 */
public class MappingJacksonJsonView extends MappingJackson2JsonView {
	/**
	 * 过滤返回json数据, 只保留ResultInfo模型的数据
	 * @param model 所有返回数据模型
	 * @return ResultInfo模型的数据
	 */
	protected Object filterModel(Map<String, Object> model) {
		Map<?, ?> result = (Map<?, ?>) super.filterModel(model);
		for (Object obj : result.values()) {
			if (obj instanceof ResultInfo || obj instanceof ResponseInfo) {
				return obj;
			}
		}
		return result;
	}
}