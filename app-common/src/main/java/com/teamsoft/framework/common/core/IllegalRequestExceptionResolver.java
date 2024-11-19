package com.teamsoft.framework.common.core;

import com.teamsoft.framework.common.model.ResultInfo;
import com.teamsoft.framework.common.util.CommonWebUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 非法请求异常处理
 * @author dominealex
 * @version 2019/9/25
 */
public class IllegalRequestExceptionResolver extends DefaultHandlerExceptionResolver {
	private final Logger log = LogManager.getLogger(IllegalRequestExceptionResolver.class);

	/**
	 * 针对GET和POST等请求类型错误的情况进行处理
	 * json请求返回json串信息
	 */
	@Override
	public ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
		if (ex instanceof HttpRequestMethodNotSupportedException) {
			try {
				if (CommonWebUtil.isJsonRequest()) {
					// json请求, 返回异常信息json串
					try {
						response.setContentType("text/json;charset=UTF-8");
						response.setHeader("Access-Control-Allow-Origin", "*");
						ResultInfo resultInfo = ResultInfo.getFailureInfo("不支持的请求类型: " + request.getMethod());
						response.getWriter().write(CommonConstants.FrameWork.GSON.toJson(resultInfo));
					} catch (IOException e) {
						log.error("处理非法请求失败" + e.getMessage());
						if (log.isDebugEnabled()) {
							e.printStackTrace();
						}
					}
					return new ModelAndView();
				}
				return handleHttpRequestMethodNotSupported((HttpRequestMethodNotSupportedException) ex, request, response, handler);
			} catch (IOException handlerEx) {
				if (log.isWarnEnabled()) {
					log.warn("Failure while trying to resolve exception [" + ex.getClass().getName() + "]", handlerEx);
				}
			}
		}
		return null;
	}

	/**
	 * 全局异常的优先级为0
	 */
	public int getOrder() {
		return 0;
	}
}