package com.teamsoft.framework.common.model;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * ID列表参数
 * @author zhangcc
 * @version 2017/8/25
 */
@Data
public class IdParam {
	private List<Serializable> ids = new ArrayList<>();
}