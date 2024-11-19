package com.teamsoft.framework.common.core;

import org.springframework.core.io.ResourceLoader;

import java.util.List;
import java.util.Locale;

/**
 * 可重载的国际化资源数据源
 * @author zhangcc
 * @version 2018/9/12
 */
public class ReloadableResourceBundleMessageSource extends org.springframework.context.support.ReloadableResourceBundleMessageSource {
	/**
	 * <p>重写获取资源文件名列表方法, 修复如下两个问题:</p>
	 * <p>1.针对给定的resource中的资源文件i18n/a，XmlWebApplicationContext读取basename配置时必须在指定的文件位置i18n/a前加入classpath:</p>
	 * <p>2.针对idea编辑器，默认的资源文件格式为i18n.a，而<code>org.springframework.context.support.ReloadableResourceBundleMessageSource</code>
	 * 却只能识别i18n/a，否则报错</p>
	 * <p>3.针对如上两种，此方法进行优化，使得<code>org.springframework.context.support.ReloadableResourceBundleMessageSource</code>能正确识别i18n.a形式的basename</p>
	 * @param basename 国际化资源文件根文件名
	 * @param locale   区域
	 * @return 符合配置的国际化资源文件列表
	 */
	protected List<String> calculateAllFilenames(String basename, Locale locale) {
		if (basename.indexOf(".") > 0) {
			basename = basename.replace(".", "/");
		}
		int prefixLen = ResourceLoader.CLASSPATH_URL_PREFIX.length();
		if (basename.length() < prefixLen || !ResourceLoader.CLASSPATH_URL_PREFIX.equalsIgnoreCase(basename.substring(0, prefixLen))) {
			basename = ResourceLoader.CLASSPATH_URL_PREFIX + basename;
		}
		return super.calculateAllFilenames(basename, locale);
	}
}