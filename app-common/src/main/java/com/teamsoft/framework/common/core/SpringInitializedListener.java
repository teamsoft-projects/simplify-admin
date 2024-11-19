package com.teamsoft.framework.common.core;

import com.teamsoft.framework.sys.model.Dictionary;
import com.teamsoft.framework.sys.model.Menu;
import com.teamsoft.framework.sys.model.ParamConfig;
import com.teamsoft.framework.sys.model.User;
import com.teamsoft.framework.sys.service.IDictionaryService;
import com.teamsoft.framework.sys.service.IMenuService;
import com.teamsoft.framework.sys.service.IParamConfigService;
import com.teamsoft.framework.sys.service.IUserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

import static com.teamsoft.framework.common.core.CommonConstants.IS_SYSTEM_CACHE_INITED;
import static com.teamsoft.framework.common.core.CommonConstants.System.*;

/**
 * Spring容器加载完成事件侦听
 * @author zhangcc
 * @version 2017/9/1
 */
@Component
public class SpringInitializedListener implements ApplicationListener<ContextRefreshedEvent> {
	/**
	 * 事件上下文
	 */
	private ContextRefreshedEvent contextEvent;
	/**
	 * 文件上传默认目录
	 */
	@Value("${framework.file.upload.root}")
	private String uploadRoot;
	/**
	 * 文件服务器URL
	 */
	@Value("${framework.file.upload.url}")
	private String fileServerURL;
	private final IMenuService menuService;
	private final IParamConfigService paramConfigService;
	private final IDictionaryService dictionaryService;
	private final IUserService userService;

	/**
	 * 是否已触发，防止启动完成事件重复触发
	 */
	private boolean isTriggered;

	public SpringInitializedListener(IMenuService menuService, IParamConfigService paramConfigService, IDictionaryService dictionaryService, IUserService userService) {
		this.menuService = menuService;
		this.paramConfigService = paramConfigService;
		this.dictionaryService = dictionaryService;
		this.userService = userService;
	}

	/**
	 * 加载完成回调
	 * @param contextRefreshedEvent 事件上下文
	 */
	@Override
	public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
		if (isTriggered) {
			return;
		}
		isTriggered = true;
		this.contextEvent = contextRefreshedEvent;
		if (contextRefreshedEvent.getApplicationContext().getParent() != null) {
			return;
		}
		initFrameWorkSettins();
	}

	/**
	 * 初始化项目配置
	 */
	private void initFrameWorkSettins() {
		// 文件上传信息配置
		CommonConstants.UPLOAD_ROOT_DIRECTORY = uploadRoot;
		CommonConstants.FILE_SERVER_URL = fileServerURL;

		// 设置二级菜单和权限点缓存
		Menu query = new Menu();
		query.setParentIdNe(CommonConstants.System.MENU_TOP);
		List<Menu> menus = menuService.listByEntity(query, null);
		menus.stream()
				.filter(menu -> Objects.nonNull(menu) && StringUtils.hasLength(menu.getUrl()))
				.forEach(menu -> SECONDARY_MENU.put(menu.getUrl(), menu));

		// 设置系统配置缓存和字典缓存
		List<ParamConfig> paramConfigs = paramConfigService.listByEntity(new ParamConfig(), null);
		paramConfigs.stream()
				.filter(e -> !SYSTEM_CACHE_SPLIT.equals(e.getFoo1()))
				.forEach(e -> SYSTEM_CONFIG_CACHE.put(e.getParamKey(), e.getParamValue()));

		// 设置数据字典缓存
		List<Dictionary> dictionaryList = dictionaryService.listByEntity(new Dictionary(), null);
		dictionaryList.forEach(dic -> DIC_CACHE.put(dic.getGroupCode(), dic.getCode(), dic));

		// 设置用户数据缓存
		List<User> userList = userService.listByEntity(new User(), null);
		userList.forEach(user -> {
			Dictionary userDic = new Dictionary();
			userDic.setGroupCode(CACHE_KEY_USER_LIST);
			userDic.setCode(user.getId());
			userDic.setName(user.getName());
			DIC_CACHE.put(CACHE_KEY_USER_LIST, user.getId(), userDic);
		});
		// 设置特殊用户ID - 系统 缓存
		Dictionary systemUserDic = new Dictionary();
		systemUserDic.setGroupCode(CACHE_KEY_USER_LIST);
		systemUserDic.setCode(SMS_SENDER_ID_SYSTEM);
		systemUserDic.setName(SMS_SENDER_NAME_SYSTEM);
		DIC_CACHE.put(CACHE_KEY_USER_LIST, SMS_SENDER_ID_SYSTEM, systemUserDic);

		/// 增加默认的字典信息
		// flag 1:是 0:否
		DIC_CACHE.put(TRANSLATE_FLAG, YES_OPTION_STR, new Dictionary(TRANSLATE_FLAG, YES_OPTION_STR, YES_OPTION_NAME));
		DIC_CACHE.put(TRANSLATE_FLAG, NO_OPTION_STR, new Dictionary(TRANSLATE_FLAG, NO_OPTION_STR, NO_OPTION_NAME));

		IS_SYSTEM_CACHE_INITED = true;
	}
}