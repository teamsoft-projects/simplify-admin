package com.teamsoft.framework.sys.service;

import cn.hutool.crypto.digest.MD5;
import com.teamsoft.framework.common.core.CommonConstants;
import com.teamsoft.framework.common.exception.BusinessException;
import com.teamsoft.framework.common.exception.VerifyException;
import com.teamsoft.framework.common.service.CommonService;
import com.teamsoft.framework.common.util.EncryptUtil;
import com.teamsoft.framework.sys.mapper.UserMapper;
import com.teamsoft.framework.sys.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户服务类
 * @author zhangcc
 * @version 2017/8/24
 */
@Service
public class UserService extends CommonService<User> implements IUserService {
	private final UserMapper userMapper;

	/**
	 * 构造方法注入
	 */
	@Autowired
	public UserService(UserMapper userMapper) {
		this.userMapper = userMapper;
		this.mapper = userMapper;
	}

	/**
	 * 保存用户
	 */
	@Override
	public Integer save(User user) {
		String loginName = user.getLoginName();
		String passwdEncry = EncryptUtil.MD5.encode(loginName + CommonConstants.System.ENCRY_SALT + CommonConstants.System.DEFAULT_PASSWD);
		user.setPassword(EncryptUtil.MD5.encode(passwdEncry));
		return userMapper.save(user);
	}

	/**
	 * 登录
	 * @param loginName 用户名
	 * @param password  加密后的密码
	 */
	@Override
	public User doLogin(String loginName, String password) {
		User user = new User();
		user.setLoginName(loginName);
		user.setPassword(EncryptUtil.MD5.encode(password));
		return userMapper.getByEntity(user);
	}

	/**
	 * 修改密码
	 */
	@Override
	public void updatePasswd(String loginName, String oldPasswd, String password) throws BusinessException {
		User query = new User();
		query.setLoginName(loginName);
		query.setPassword(EncryptUtil.MD5.encode(oldPasswd));
		User loginUser = userMapper.getByEntity(query);
		if (loginUser == null) {
			throw new BusinessException("旧密码输入错误");
		}
		loginUser.setPassword(EncryptUtil.MD5.encode(password));
		userMapper.updatePasswd(loginUser);
	}

	/**
	 * 重置密码
	 * @param userId 用户编号
	 */
	@Override
	public void resetPasswd(String userId) {
		User user = mapper.get(userId);
		if (user == null) {
			throw new VerifyException(CommonConstants.FrameWork.FAILURE_FLAG, CommonConstants.FrameWork.FAILURE_MESSAGE);
		}
		String loginName = user.getLoginName();
		String passwdEncry = EncryptUtil.MD5.encode(loginName + CommonConstants.System.ENCRY_SALT + CommonConstants.System.DEFAULT_PASSWD);
		user.setPassword(EncryptUtil.MD5.encode(passwdEncry));
		((UserMapper) mapper).resetPasswd(user);
	}

	/**
	 * 更新默认密码
	 * @param user 更新条件
	 */
	@Override
	public void updateDefaultPasswd(User user) {
		User query = new User();
		query.setLoginName(user.getLoginName());
		User loginUser = userMapper.getByEntity(query);
		if (loginUser == null) {
			throw new VerifyException(CommonConstants.FrameWork.FAILURE_FLAG, CommonConstants.System.MESSAGE_USER_NOT_EXISTS);
		}
		user.setId(loginUser.getId());
		user.setPassword(EncryptUtil.MD5.encode(user.getPassword()));
		userMapper.updatePasswd(user);
	}
}