package com.teamsoft.framework.common.model;

import com.teamsoft.framework.common.annotation.Verify;
import lombok.Data;

import java.io.Serializable;

/**
 * 数据实体类的超类
 * @author zhangcc
 * @version 2017/8/28
 */
@Data
public class Entity implements Serializable {
	// 编号
	@Verify(value = VerifyType.REQUIRED, scene = VerifyScene.UPDATE)
	protected String id;
	// 排序码
	protected Integer sortOrder;
	// 创建人编号
	protected String creatorId;

	// VO
	// 开始时间
	protected String timeBegin;
	// 结束时间
	protected String timeEnd;
	// 时间段
	protected String timeInterval;
}