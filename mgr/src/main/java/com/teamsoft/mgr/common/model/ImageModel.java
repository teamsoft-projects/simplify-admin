package com.teamsoft.mgr.common.model;

import lombok.Data;

/**
 * 图片转换Model
 * @author alex
 * @version 2020/5/7
 */
@Data
public class ImageModel {
	// 图片类型
	private ImageType type;
	// 图片后缀
	private String suffix;
	// 图片内容(字节数组)
	private byte[] data;
}