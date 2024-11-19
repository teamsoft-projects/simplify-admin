package com.teamsoft.framework.sys.mapper;

import com.teamsoft.framework.common.mapper.CommonMapper;
import com.teamsoft.framework.sys.model.User;
import org.apache.ibatis.annotations.Param;

/**
 * 用户数据库操作接口
 * @author zhangcc
 * @version 2017/8/24
 */
public interface UserMapper extends CommonMapper<User> {
	/**
	 * 更新密码
	 */
	void updatePasswd(@Param("entity") User user);

	/**
	 * 重置密码
	 * @param user 用户信息
	 */
	void resetPasswd(@Param("entity") User user);
}