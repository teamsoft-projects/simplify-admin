package com.teamsoft.framework.common.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Map常用工具封装
 * @author zhangcc
 * @version 2017/10/25
 */
public class LocalMap {
	// 内部存放数据的实体
	private static Map<String, Object> map = new HashMap<>();

	/**
	 * 创建Map对象，并将key和val赋入
	 * @param key 键
	 * @param val 值
	 * @return 带指定键值的Map对象
	 */
	public static Map<String, Object> create(String key, Object val) {
		map.clear();
		map.put(key, val);
		return map;
	}
}