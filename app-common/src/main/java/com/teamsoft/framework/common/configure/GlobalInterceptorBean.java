package com.teamsoft.framework.common.configure;

import com.teamsoft.framework.common.web.GlobalInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 全局拦截器Bean
 * @author wangyg
 * @version 2020/4/3
 */
@Configuration
public class GlobalInterceptorBean {
	/**
	 * 注入全局拦截器Bean
	 */
	@Bean
	public GlobalInterceptor registerGlobalInterceptor() {
		return new GlobalInterceptor();
	}
}