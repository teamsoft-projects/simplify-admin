package com.teamsoft.framework.common.web;

import com.teamsoft.framework.common.core.CommonConstants;
import com.teamsoft.framework.common.model.ResultInfo;
import com.teamsoft.framework.common.util.CommonWebUtil;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;

/**
 * 登陆权限拦截器
 * @author zhangcc
 * @version 2015-5-27 下午11:48:29
 */
@Component
public class LoginPermissionInterceptor extends HandlerInterceptorAdapter {
	/**
	 * 登陆权限拦截器
	 */
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		Object loginUser = CommonWebUtil.sessionVal(CommonConstants.FrameWork.SESSION_USER);
		String uri = CommonWebUtil.getRequstURIIgnoreAppPath();
		if (loginUser == null) { // 未登陆用户
			if (CommonWebUtil.isJsonRequest()) { // json请求, 返回无权限信息json串
				ResultInfo resultInfo = ResultInfo.getPermissionDeniedInfo();
				try {
					response.setContentType("text/json;charset=UTF-8");
					PrintWriter out = response.getWriter();
					out.write(CommonConstants.FrameWork.GSON.toJson(resultInfo));
					out.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				return false;
			}
			CommonWebUtil.sendRedirect(CommonConstants.FrameWork.LOGIN_PAGE_URI); // 返回登陆页面
			return false;
		}
		return true;
	}
}