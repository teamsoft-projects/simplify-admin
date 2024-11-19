package com.teamsoft.framework.common.configure;

import com.teamsoft.framework.common.core.CommonConstants;
import com.teamsoft.framework.common.exception.BusinessException;
import com.teamsoft.framework.common.exception.VerifyException;
import com.teamsoft.framework.common.model.ResultInfo;
import com.teamsoft.framework.common.util.CommonWebUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

/**
 * 全局异常处理
 * @author alex
 * @version 2020/4/28
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
	/**
	 * 全局异常处理
	 * @param ex 异常信息
	 * @return 异常的html或json视图
	 */
	@ExceptionHandler(Exception.class)
	public Object handleException(Exception ex) {
		ResultInfo resultInfo = ResultInfo.getFailureInfo();
		// 单独处理业务级异常
		if (ex instanceof BusinessException) {
			resultInfo.setFlag(CommonConstants.FrameWork.FAILURE_FLAG);
			resultInfo.setMessage(ex.getMessage());
			log.error("业务异常: " + ex.getMessage(), ex);
		} else if (ex instanceof VerifyException) {
			VerifyException verifyException = (VerifyException) ex;
			resultInfo.setFlag(verifyException.getFlag());
			resultInfo.setMessage(verifyException.getMessage());
			log.error("校验异常: " + ex.getMessage(), ex);
		} else {
			resultInfo.setMessage("系统发生错误");
			log.error("系统异常" + ex.getMessage(), ex);
		}
		if (CommonWebUtil.isJsonRequest()) {
			return resultInfo;
		} else {
			return new ModelAndView("500");
		}
	}
}