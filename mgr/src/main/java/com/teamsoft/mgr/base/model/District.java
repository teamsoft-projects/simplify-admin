package com.teamsoft.mgr.base.model;

import lombok.Data;
import org.apache.ibatis.type.Alias;

/**
 * 区域实体类
 *
 * @author zhangcc
 * @version 2023/10/16
 */
@Data
@Alias("District")
public class District {
	/**
	 * 编号
	 */
	private Integer id;
	/**
	 * 城市中心点坐标
	 */
	private String center;
	/**
	 * 城市编码
	 */
	private String cityCode;
	/**
	 * 地区编码
	 */
	private String code;
	/**
	 * 地区级别（1:省份province,2:市city,3:区县district）
	 */
	private Integer level;
	/**
	 * 地区名
	 */
	private String name;
	/**
	 * 地区父节点
	 */
	private Integer parentId;
}