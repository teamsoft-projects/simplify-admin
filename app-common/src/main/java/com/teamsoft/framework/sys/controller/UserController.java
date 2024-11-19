package com.teamsoft.framework.sys.controller;

import com.teamsoft.framework.common.controller.CommonController;
import com.teamsoft.framework.common.core.CommonConstants;
import com.teamsoft.framework.common.exception.BusinessException;
import com.teamsoft.framework.common.model.ResultInfo;
import com.teamsoft.framework.common.util.CommonWebUtil;
import com.teamsoft.framework.sys.model.Dictionary;
import com.teamsoft.framework.sys.model.Menu;
import com.teamsoft.framework.sys.model.User;
import com.teamsoft.framework.sys.service.IMenuService;
import com.teamsoft.framework.sys.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.teamsoft.framework.common.core.CommonConstants.FrameWork.ERROR_QUEST_TOO_FREQUENT;
import static com.teamsoft.framework.common.core.CommonConstants.System.*;

/**
 * 后台用户控制类
 * @author zhangcc
 * @version 2017/8/24
 */
@Controller
@Api(tags = "用户管理")
@RequestMapping("sys/user")
public class UserController extends CommonController<User> {
	@Value("${framework.env}")
	private String env;
	private final IMenuService menuService;
	private final Map<String, LocalDateTime> loginTimeCache = new HashMap<>();

	@Autowired
	public UserController(IUserService service, IMenuService menuService) {
		this.service = service;
		this.menuService = menuService;
	}

	/**
	 * 进入登录页面
	 */
	@RequestMapping("login")
	public ResultInfo login() {
		return ResultInfo.getSuccessInfo(SYSTEM_CONFIG_CACHE);
	}

	/**
	 * 登录
	 * @param loginName 用户名
	 * @param password  加密后的密码
	 */
	@PostMapping("doLogin.json")
	public ResultInfo doLogin(String loginName, String password) {
		if (!StringUtils.hasLength(loginName) || !StringUtils.hasLength(password)) {
			return ResultInfo.getNoticeInfo(CommonConstants.FrameWork.PARAM_ERROR);
		}
		LocalDateTime cacheTime = loginTimeCache.get(loginName);
		if (cacheTime != null && cacheTime.until(LocalDateTime.now(), ChronoUnit.SECONDS) < 2) {
			return ResultInfo.getNoticeInfo(ERROR_QUEST_TOO_FREQUENT);
		}
		loginTimeCache.put(loginName, LocalDateTime.now());

		User user = ((IUserService) service).doLogin(loginName, password);
		if (user == null) {
			return ResultInfo.getSelfNoticeInfo(CommonConstants.System.MESSAGE_LOGINNAME_PASSWD_ERROR);
		}
		// 将登陆用户的信息放入session缓存
		CommonWebUtil.sessionVal(CommonConstants.FrameWork.SESSION_USER, user);
		// 初始化二级菜单列表, 以便拦截器确定权限
		Menu query = new Menu();
		query.setParentIdNe(CommonConstants.System.MENU_TOP);
		List<Menu> menus = menuService.listByEntity(query, null);
		menus.stream()
				.filter(menu -> Objects.nonNull(menu) && StringUtils.hasLength(menu.getUrl()))
				.forEach(menu -> SECONDARY_MENU.put(menu.getUrl(), menu));
		return ResultInfo.getSuccessInfo(user);
	}

	/**
	 * 修改密码
	 */
	@RequestMapping("updatePasswd")
	public ResultInfo updatePasswd(String loginName, String oldPasswd, String password) throws BusinessException {
		if (!StringUtils.hasLength(loginName) || !StringUtils.hasLength(oldPasswd) || !StringUtils.hasLength(password)) {
			return ResultInfo.getParamErrorInfo();
		}
		((IUserService) service).updatePasswd(loginName, oldPasswd, password);
		return ResultInfo.getSuccessInfo();
	}

	/**
	 * 注销
	 */
	@RequestMapping("logout")
	public ModelAndView logout() {
		CommonWebUtil.sessionVal(CommonConstants.FrameWork.SESSION_USER, null);
		if (!CommonWebUtil.isJsonRequest()) {
			return new ModelAndView("redirect:/sys/user/login");
		}
		// json请求, 填写返回json数据
		ModelAndView mv = new ModelAndView();
		mv.addObject("flag", 100101);
		return mv;
	}

	/**
	 * 重置密码
	 */
	@RequestMapping("resetPasswd")
	public ResultInfo resetPasswd(String userId) {
		((IUserService) service).resetPasswd(userId);
		return ResultInfo.getSuccessInfo();
	}

	/**
	 * 修改默认密码
	 * @param user 条件
	 */
	@RequestMapping("updateDefaultPasswd")
	public ResultInfo updateDefaultPasswd(User user) {
		((IUserService) service).updateDefaultPasswd(user);
		return ResultInfo.getSuccessInfo();
	}

	/**
	 * 登录测试
	 */
	@ApiOperation(value = "登录测试", notes = "登录测试")
	@PostMapping("loginTest")
	public ResultInfo loginTest() {
		if (!SYSTEM_ENV_DEV.equals(env)) {
			return ResultInfo.getPermissionDeniedInfo();
		}
		User query = new User();
		query.setLoginName("admin");
		User user = service.getByEntity(query);
		if (user == null) {
			return ResultInfo.getPermissionDeniedInfo();
		}
		// 将登陆用户的信息放入session缓存
		CommonWebUtil.sessionVal(CommonConstants.FrameWork.SESSION_USER, user);
		return ResultInfo.getSuccessInfo(user);
	}

	/**
	 * 保存后操作
	 */
	protected void afterSave(User entity) {
		// 更新缓存信息
		Dictionary dic = new Dictionary();
		dic.setCode(entity.getId());
		dic.setName(entity.getName());
		DIC_CACHE.put(CACHE_KEY_USER_LIST, entity.getId(), dic);
	}

	/**
	 * 删除后操作
	 * @param ids 待操作数据
	 */
	protected void afterRemove(List<Serializable> ids) {
		// 删除缓存信息
		ids.forEach(id -> DIC_CACHE.remove(CACHE_KEY_USER_LIST, String.valueOf(id)));
	}
}