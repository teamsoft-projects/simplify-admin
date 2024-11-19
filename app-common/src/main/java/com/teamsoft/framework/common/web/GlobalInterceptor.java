package com.teamsoft.framework.common.web;

import com.teamsoft.framework.common.core.CommonConstants;
import com.teamsoft.framework.common.core.MappingJacksonJsonView;
import com.teamsoft.framework.common.model.ResponseInfo;
import com.teamsoft.framework.common.model.ResultInfo;
import com.teamsoft.framework.common.util.CommonStandardUtil;
import com.teamsoft.framework.common.util.CommonWebUtil;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.Map;

/**
 * 全局请求拦截器
 * @author zhangcc
 * @version 16-4-15.
 */
public class GlobalInterceptor extends HandlerInterceptorAdapter {
	/**
	 * 请求时拦截
	 */
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		// 以XML、ATOM结尾的请求，返回404页面
		String uri = CommonWebUtil.getRequstURIIgnoreAppPath();
		if (CommonStandardUtil.endsWithIgnoreCase(uri, ".xml") || CommonStandardUtil.endsWithIgnoreCase(uri, ".atom")) {
			// 返回404页面
			CommonWebUtil.sendRedirect("/404");
			return false;
		}
		return true;
	}

	/**
	 * 返回时拦截
	 */
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView mv) {
		// 判断返回时是否存在页面跳转
		Map<String, Object> model = mv == null ? null : mv.getModel();
		if (model == null) {
			return;
		}
		Object result = model.get(CommonConstants.FrameWork.RETURN_MODEL_NAME);
		if (!(result instanceof ResultInfo)) {
			return;
		}
		ResultInfo resultInfo = (ResultInfo) result;
		Integer flag = resultInfo.getFlag();
		Object data = resultInfo.getData();
		// 处理api接口请求, 将ResultInfo转换为ResponseInfo
		if (CommonWebUtil.isApiRequest()) {
			model.put(CommonConstants.FrameWork.RETURN_MODEL_NAME, ResponseInfo.getSuccessInfo(resultInfo));
			return;
		}
		// 忽略json请求
		if (CommonWebUtil.isJsonRequest()) {
			return;
		}
		// 有重定向标记的返回, 执行页面重定向
		if (CommonConstants.FrameWork.DO_REDIRECT_FLAG.equals(flag) && data != null) {
			StringBuilder viewName = new StringBuilder("redirect:" + data);
			boolean isFirst = true;
			Map<String, ?> redirectAttributes = resultInfo.getRedirectAttributes();
			if (redirectAttributes != null) {
				// 拼接重定向参数
				for (Map.Entry<String, ?> entry : redirectAttributes.entrySet()) {
					String key = entry.getKey();
					Object val = entry.getValue();
					if (!StringUtils.hasLength(key) || val == null) {
						continue;
					}
					String value;
					try {
						value = URLEncoder.encode(val.toString(), CommonConstants.System.CHARSET_UTF8);
					} catch (Exception e) {
						continue;
					}
					if (isFirst) {
						viewName.append("?").append(key).append("=").append(value);
						isFirst = false;
					} else {
						viewName.append("&").append(key).append("=").append(value);
					}
				}
			}
			mv.setViewName(viewName.toString());
		}
		// 有提示信息标记的返回, 将返回结果转换为JSON视图，打印提示信息
		if (CommonConstants.FrameWork.JSON_VIEW_FLAG.equals(flag) || CommonConstants.FrameWork.JSON_NOTICE_FLAG.equals(flag)) {
			mv.setView(new MappingJacksonJsonView());
			mv.addObject(resultInfo);
		}
	}
}