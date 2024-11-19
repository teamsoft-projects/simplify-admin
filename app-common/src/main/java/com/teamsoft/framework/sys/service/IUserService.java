package com.teamsoft.framework.sys.service;

import com.teamsoft.framework.common.exception.BusinessException;
import com.teamsoft.framework.common.service.ICommonService;
import com.teamsoft.framework.sys.model.User;

/**
 * 用户服务接口
 * @author zhangcc
 * @version 2017/8/24
 */
public interface IUserService extends ICommonService<User> {
	/**
	 * 保存用户
	 */
	@Override
	Integer save(User user);

	/**
	 * 登录
	 * @param loginName 用户名
	 * @param password  加密后的密码
	 */
	User doLogin(String loginName, String password);

	/**
	 * 修改密码
	 */
	void updatePasswd(String loginName, String oldPasswd, String password) throws BusinessException;

	/**
	 * 重置密码
	 * @param userId 用户编号
	 */
	void resetPasswd(String userId);

	/**
	 * 更新默认密码
	 * @param user 更新条件
	 */
	void updateDefaultPasswd(User user);
}