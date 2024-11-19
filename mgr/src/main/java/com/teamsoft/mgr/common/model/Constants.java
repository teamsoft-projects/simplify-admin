package com.teamsoft.mgr.common.model;

import com.teamsoft.framework.common.core.CommonConstants;
import com.teamsoft.mgr.base.model.District;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 常量
 * @author dominealex
 * @version 2020/3/18
 */
public class Constants extends CommonConstants {
	/**
	 * 系统相关常量
	 */
	public interface System {
		/// 系统缓存KEY-group code
		// 省份列表
		String CACHE_KEY_PROVINCE_LIST = "PROVINCE-LIST";
		// 城市列表
		String CACHE_KEY_CITY_LIST = "CITY-LIST";
		// 区域列表
		String CACHE_KEY_AREA_LIST = "AREA-LIST";
	}

	/**
	 * 业务相关常量
	 */
	public interface Business {
		/**
		 * 区域缓存
		 */
		Map<Integer, List<District>> DISTRICT_PARENTID_CACHE = new HashMap<>();
	}

	/**
	 * 短信相关常量
	 */
	public interface Sms {
		/**
		 * 短信平台AccessKey - key
		 */
		String KEY_SMS_ACCESSKEY = "SMS_ACCESSKEY";
		/**
		 * 短信平台AccessSecret - key
		 */
		String KEY_SMS_ACCESSKEY_SECRET = "SMS_ACCESSKEY_SECRET";

		/**
		 * 异常信息：同步短信模板失败
		 */
		String ERROR_SYNC_SMS_TEMPLATE_FAILURE = "同步短信模板失败";
		String ERROR_SMS_TEMPLET_NOT_EXISTS = "短信模板不存在";
		String ERROR_MANUAL_PARAM_NO_VALUE = "手动输入的参数未输入值";
	}
}