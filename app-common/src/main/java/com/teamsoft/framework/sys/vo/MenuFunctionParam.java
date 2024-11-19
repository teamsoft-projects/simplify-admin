package com.teamsoft.framework.sys.vo;

import com.teamsoft.framework.sys.model.MenuFunction;
import lombok.Data;

import java.util.List;

/**
 * 菜单功能列表入参
 *
 * @author zhangcc
 * @version 2017/8/30
 */
@Data
public class MenuFunctionParam {
	private List<MenuFunction> datas;
}