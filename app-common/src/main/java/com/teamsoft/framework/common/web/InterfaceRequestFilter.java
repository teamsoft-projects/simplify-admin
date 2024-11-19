package com.teamsoft.framework.common.web;

import com.teamsoft.framework.common.core.CommonConstants;
import com.teamsoft.framework.common.exception.BusinessException;
import com.teamsoft.framework.common.exception.InterfaceException;
import com.teamsoft.framework.common.exception.VerifyException;
import com.teamsoft.framework.common.model.ResponseInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 接口请求过滤器
 * @author zhangcc
 * @version 2017/9/7
 */
@Slf4j
public class InterfaceRequestFilter extends OncePerRequestFilter {
	/**
	 * 过滤接口请求
	 * 执行解密和参数重新制定操作
	 */
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		InterfaceRequestWrapper requestWrapper;
		try {
			requestWrapper = new InterfaceRequestWrapper(request);
		} catch (Exception e) {
			// 发生异常, 返回参数错误信息
			log.error("接口请求|Exception|" + e.getMessage());
			response.setHeader("Content-Type", "application/json;charset=utf-8");
			response.setHeader("Access-Control-Allow-Origin", "*");
			String message = "system error";
			if (e instanceof InterfaceException || e instanceof BusinessException || e instanceof VerifyException) {
				message = e.getMessage();
			}
			String retJson = CommonConstants.FrameWork.GSON.toJson(ResponseInfo.getFailureInfo(message));
			String callback = request.getParameter("callback");
			if (StringUtils.hasLength(callback)) {
				response.getWriter().print(callback + "(" + retJson + ")");
			} else {
				response.getWriter().print(retJson);
			}
			return;

		}
		filterChain.doFilter(requestWrapper, response);
	}
}