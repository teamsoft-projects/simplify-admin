package com.teamsoft.framework.common.configure;

import com.teamsoft.framework.common.core.MappingJacksonJsonView;
import com.teamsoft.framework.common.web.GlobalInterceptor;
import com.teamsoft.framework.common.web.LoginPermissionInterceptor;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.boot.web.server.ErrorPageRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.*;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static com.teamsoft.framework.common.core.CommonConstants.System.SYSTEM_ENV_DEV;

/**
 * MVC相关配置
 * @author dominealex
 * @version 2020/3/17
 */
@Configuration
@EnableWebMvc
@EnableSwagger2
public class WebMvcConfigure implements WebMvcConfigurer, ErrorPageRegistrar {
	//是否开启swagger，正式环境一般是需要关闭的，可根据springboot的多环境配置进行设置
	@Value(value = "${framework.env}")
	private String env;
	// 无需权限接口配置
	@Value(value = "${framework.non-auth:}")
	private List<String> nonAuth;

	// 静态资源映射配置
	private static final String[][] RESOURCE_MAPPING = {
			{"/static/**", "classpath:/static/"},
			{"/swagger-ui.html", "classpath:/META-INF/resources/"},
			{"/doc.html", "classpath:/META-INF/resources/"},
			{"/webjars/**", "classpath:/META-INF/resources/webjars/"},
			{"/swagger-resources/**", "classpath:/META-INF/resources/"},
			{"/swagger/**", "classpath:/META-INF/resources/swagger*"},
			{"/v2/api-docs/**", "classpath:/META-INF/resources/v2/api-docs/"}
	};

	// 全局拦截器
	@Resource
	private GlobalInterceptor globalInterceptor;
	// 登陆权限拦截器
	@Resource
	private LoginPermissionInterceptor loginPermissionInterceptor;

	/**
	 * 配置视图解析器
	 * 1. 配置默认视图为HTML/TEXT(无后缀或.xxx任意非json的后缀)
	 * 2. 配置json视图请求后缀，.json的请求返回json视图
	 * 3. 识别请求头中的Accept，为text/html时，返回网页视图，为application/json时，返回json视图
	 * ignoreAcceptHeader默认值为false，即表示开启Accept识别支持
	 */
	@Override
	public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
		configurer.defaultContentType(MediaType.TEXT_HTML);
	}

	/**
	 * 配置json视图解析器适用范围(application/json, text/html)
	 * 解决Swagger请求时，报406错误的问题
	 */
	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		List<MediaType> list = new ArrayList<>();
		list.add(new MediaType("application", "json", StandardCharsets.UTF_8));
		list.add(new MediaType("application", "*+json", StandardCharsets.UTF_8));
		list.add(new MediaType("text", "html", StandardCharsets.UTF_8));
		converter.setSupportedMediaTypes(list);
		converters.add(converter);
	}

	/**
	 * 配置默认的JSON视图解析器
	 */
	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {
		registry.enableContentNegotiation(new MappingJacksonJsonView());
		registry.viewResolver(new FreeMarkerConfigure().getFreemarkViewResolver());
	}

	/**
	 * 设置欢迎页
	 */
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		// 设置默认地址请求(/)重定向到index页面
		registry.addRedirectViewController("/", "/index");
		registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
	}

	/**
	 * 添加拦截器配置
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// 配置全局拦截器
		InterceptorRegistration globalInterceptorRegistration = registry.addInterceptor(globalInterceptor).addPathPatterns("/**");
		// 配置登录权限拦截器
		InterceptorRegistration loginInterceptorRegistration = registry.addInterceptor(loginPermissionInterceptor)
				.addPathPatterns("/**")
				.excludePathPatterns("/api/**")
				.excludePathPatterns("/sys/user/login")
				.excludePathPatterns("/sys/user/doLogin.json")
				.excludePathPatterns("/sys/user/loginTest.json")
				.excludePathPatterns("/aes.json")
				.excludePathPatterns("/404")
				.excludePathPatterns("/500")
				.excludePathPatterns("/error");
		// 循环排除静态资源拦截
		for (String[] mapping : RESOURCE_MAPPING) {
			loginInterceptorRegistration.excludePathPatterns(mapping[0]);
			globalInterceptorRegistration.excludePathPatterns(mapping[0]);
		}
		// 循环排除外部无需权限配置拦截
		for (String noAuth : nonAuth) {
			loginInterceptorRegistration.excludePathPatterns(noAuth);
		}
	}

	/**
	 * 跨域支持
	 */
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
				.allowedOrigins("*")
				.allowCredentials(true)
				.allowedMethods("GET", "POST", "DELETE", "PUT")
				.maxAge(3600 * 24);
	}

	/**
	 * 将静态资源目录、swagger映射为指定的静态路径
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// 映射静态资源
		for (String[] mapping : RESOURCE_MAPPING) {
			registry.addResourceHandler(mapping[0]).addResourceLocations(mapping[1]).setCachePeriod(31536000);
		}
	}

	/**
	 * 注册Swagger2
	 * 配置Swagger2 UI首页显示的内容
	 */
	@Bean
	public Docket createRestApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.enable(SYSTEM_ENV_DEV.equals(env))
				.pathMapping("/")
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.teamsoft"))
				.apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
				.paths(PathSelectors.any())
				.build()
				.apiInfo(new ApiInfoBuilder()
						.title("接口列表")
						.version("1.0")
						.build());
	}

	/**
	 * 错误页面配置
	 */
	@Override
	public void registerErrorPages(ErrorPageRegistry errorPageRegistry) {
		ErrorPage page404 = new ErrorPage(HttpStatus.NOT_FOUND, "/404");
		ErrorPage page500 = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/500");
		errorPageRegistry.addErrorPages(page404, page500);
	}
}