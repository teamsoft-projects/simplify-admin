package com.teamsoft.framework.common.util;

import com.teamsoft.framework.common.annotation.Verify;
import com.teamsoft.framework.common.core.CommonConstants;
import com.teamsoft.framework.common.exception.VerifyException;
import com.teamsoft.framework.common.model.Entity;
import com.teamsoft.framework.common.model.VerifyScene;
import com.teamsoft.framework.common.model.VerifyType;
import com.teamsoft.framework.sys.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.URLDecoder;
import java.net.UnknownHostException;
import java.util.*;

import static com.teamsoft.framework.common.core.CommonConstants.FrameWork.PARAMETER_ERROR_FLAG;

/**
 * 类描述：请求相关工具类
 * @author zhangcc
 * @version 2015-5-30 下午2:17:02
 */
public class CommonWebUtil {
	private static final Logger log = LogManager.getLogger(CommonWebUtil.class);

	/**
	 * 获取请求URI, 忽略项目名称部分(如:/minicard/login.json返回/login.json)
	 * @return 处理后的URI
	 */
	public static String getRequstURIIgnoreAppPath() {
		HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
		String uri = request.getRequestURI();
		return uri.replace(CommonConstants.APP_PATH, "");
	}

	/**
	 * 发送重定向
	 * @param url 重定向地址
	 */
	public static void sendRedirect(String url) {
		HttpServletResponse response = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getResponse();
		try {
			assert response != null;
			response.sendRedirect(CommonConstants.APP_PATH + url);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取Request
	 */
	public static HttpServletRequest getRequest() {
		return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
	}

	/**
	 * 在Session中设置值
	 * @param name 键 value 值
	 */
	public static void sessionVal(String name, Object value) {
		HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
		HttpSession session = request.getSession();
		session.setAttribute(name, value);
	}

	/**
	 * 获取session中的值
	 * @param name 键
	 * @return 值
	 */
	public static Object sessionVal(String name) {
		HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
		HttpSession session = request.getSession();
		return session.getAttribute(name);
	}

	/**
	 * 获取当前session
	 * @return session对象
	 */
	public static HttpSession getSession() {
		HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
		return request.getSession();
	}

	/**
	 * 从Request里获取参数
	 * @param key 参数名
	 */
	public static String getParam(String key) {
		HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
		return request.getParameter(key);
	}

	/**
	 * 判断是否为json请求
	 */
	public static boolean isJsonRequest() {
		HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
		if (CommonStandardUtil.endsWithIgnoreCase(request.getRequestURI(), ".json")) {
			return true;
		}
		String accept = request.getHeader("Accept");
		if (accept != null) {
			String[] contentTypes = accept.split(",");
			return Arrays.stream(contentTypes).anyMatch(t -> t != null && CommonConstants.FrameWork.JSON_CONTENTTYPE.equalsIgnoreCase(t.trim()));
		}
		return false;
	}

	/**
	 * 判断是否是Api接口请求
	 */
	public static boolean isApiRequest() {
		return CommonStandardUtil.startsWithIgnoreCase(getRequstURIIgnoreAppPath(), "/api");
	}

	/**
	 * 解密已加密参数
	 * @param encodedStr 加密后的参数
	 * @param rule       加/解密规则
	 * @return 解密结果, 键值对形式
	 * @throws Exception 发生任何解析错误, 直接抛出异常
	 */
	public static Map<String, String[]> decodeParameter(String encodedStr, byte[] rule) throws Exception {
		if (!StringUtils.hasLength(encodedStr)) {
			throw new VerifyException("参数格式错误");
		}
		// 通过AES解密获取base64密文
		String aesDecodeStr = EncryptUtil.AES.decode(rule, encodedStr);
		// 通过base64解密获取明文
		String plaintext = EncryptUtil.Base64.decode(aesDecodeStr);
		Map<String, String[]> retMap = new HashMap<>();
		String[] mapArr = plaintext.split("&");
		for (String temp : mapArr) {
			String[] tempArr = temp.split("=");
			if (tempArr.length < 2) {
				continue;
			}
			String value = URLDecoder.decode(tempArr[1], CommonConstants.System.CHARSET_UTF8);
			retMap.put(tempArr[0], new String[]{value});
		}
		return retMap;
	}

	/**
	 * 是否包含指定元素
	 * @param source 源数组/集合
	 * @param item   元素
	 */
	public static boolean isContains(Object source, Object item) {
		if (source == null) {
			return false;
		}
		if (source.getClass().isArray()) {
			int len = Array.getLength(source);
			if (len < 1) {
				return false;
			}
			for (int i = 0; i < len; i++) {
				Object temp = Array.get(source, i);
				if (temp == null) {
					if (item == null) {
						return true;
					}
					continue;
				}
				if (temp instanceof Enum) {
					if (temp == item) {
						return true;
					}
					continue;
				}
				if (temp.equals(item)) {
					return true;
				}
			}
		} else if (source instanceof Collection) {
			return ((Collection<?>) source).contains(item);
		}
		return false;
	}

	/**
	 * 对象级参数校验
	 * @param source 待校验对象
	 * @param scence 使用场景
	 */
	public static void verify(Object source, VerifyScene scence) {
		if (source == null) {
			throw new VerifyException(PARAMETER_ERROR_FLAG, "待校验参数为NULL");
		}
		try {
			List<Field> fields = CommonStandardUtil.getAllFieldsWithSuper(source);
			for (Field f : fields) {
				Verify[] verifyArr = f.getAnnotationsByType(Verify.class);
				for (Verify verify : verifyArr) {
					if (scence != null && scence != VerifyScene.ALL) { // 如果指定了场景, 需判断场景是否包含在配置的校验场景中
						VerifyScene[] scenceTemp = verify.scene();
						if (!isContains(scenceTemp, VerifyScene.ALL) && !isContains(scenceTemp, scence)) {
							// 对应场景不符, 不进行校验
							continue;
						}
					}
					VerifyType[] enums = verify.value();
					if (enums.length == 0) {
						continue;
					}
					f.setAccessible(true);
					Object val = f.get(source);
					String valStr = String.valueOf(val);
					for (VerifyType verifyType : enums) {
						switch (verifyType) {
							case NONE: {
								break;
							}
							case REQUIRED: {
								if (val == null || valStr.isEmpty()) {
									throw new VerifyException(PARAMETER_ERROR_FLAG, "必填参数 " + f.getName() + " 未传");
								}
								break;
							}
							case INTEGER: {
								if (val == null) {
									break;
								}
								if (!valStr.matches("^-?\\d+$")) {
									throw new VerifyException(PARAMETER_ERROR_FLAG, "参数 " + f.getName() + " 必须传入整数型");
								}
								break;
							}
							case DOUBLE: {
								if (val == null) {
									break;
								}
								if (!valStr.matches("^(-?\\d+)(\\.\\d+)?$")) {
									throw new VerifyException(PARAMETER_ERROR_FLAG, "参数 " + f.getName() + " 必须传入浮点型");
								}
								break;
							}
							case LENGTH: {
								if (val == null) {
									break;
								}
								int[] lens = verify.length();
								int len = lens[0];
								if (len == -1) {
									break;
								}
								if (valStr.length() != len) {
									throw new VerifyException(PARAMETER_ERROR_FLAG, "参数 " + f.getName() + " 的长度必须为 " + len);
								}
								break;
							}
							case LENLT: {
								if (val == null) {
									break;
								}
								int[] len = verify.length();
								if (valStr.length() >= len[0]) {
									throw new VerifyException(PARAMETER_ERROR_FLAG, "参数 " + f.getName() + " 的长度必须小于 " + len[0]);
								}
								break;
							}
							case LENLTE: {
								if (val == null) {
									break;
								}
								int[] len = verify.length();
								if (valStr.length() > len[0]) {
									throw new VerifyException(PARAMETER_ERROR_FLAG, "参数 " + f.getName() + " 的长度必须小于等于 " + len[0]);
								}
								break;
							}
							case LENGT: {
								if (val == null) {
									break;
								}
								int[] len = verify.length();
								if (valStr.length() <= len[0]) {
									throw new VerifyException(PARAMETER_ERROR_FLAG, "参数 " + f.getName() + " 的长度必须大于 " + len[0]);
								}
								break;
							}
							case LENGTE: {
								if (val == null) {
									break;
								}
								int[] len = verify.length();
								if (valStr.length() < len[0]) {
									throw new VerifyException(PARAMETER_ERROR_FLAG, "参数 " + f.getName() + " 的长度必须大于等于 " + len[0]);
								}
								break;
							}
							case LENRANGE: {
								if (val == null) {
									break;
								}
								int[] len = verify.length();
								if (valStr.length() < len[0] || valStr.length() > len[1]) {
									throw new VerifyException(PARAMETER_ERROR_FLAG, "参数 " + f.getName() + " 的长度必须在 " + len[0] + " 和 " + len[1] + " 之间");
								}
								break;
							}
							case REGEX: {
								if (val == null || "".equals(val)) {
									break;
								}
								String[] regexs = verify.regex();
								for (String regex : regexs) {
									if (!StringUtils.hasLength(regex)) {
										continue;
									}
									if (!valStr.matches(regex)) {
										throw new VerifyException(PARAMETER_ERROR_FLAG, "参数 " + f.getName() + " 的格式必须满足正则表达式 " + regex);
									}
								}
								break;
							}
						}
					}
				}
			}
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new VerifyException();
		}
	}

	/**
	 * 对象级参数校验
	 * @param obj 待校验对象
	 */
	public static void verify(Object obj) {
		verify(obj, VerifyScene.ALL);
	}

	/**
	 * 获取本机IP地址
	 * @return IP地址
	 */
	public static String getLocalHostAddress() {
		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			return null;
		}
	}

	/**
	 * 设置时间间隔
	 * 读取参数中的interval字段, 设置到entity的timeBegin和timeEnd中
	 * @param entity      字段参数
	 * @param isAddSuffix 是否增加日期后时间后缀
	 */
	public static void setTimeInterval(Entity entity, boolean isAddSuffix) {
		Class<? extends Entity> clazz = entity.getClass();
		List<Field> fs = CommonStandardUtil.getAllFieldsWithSuper(entity);
		for (Field f : fs) {
			String name = f.getName();
			if (!name.endsWith("Interval")) {
				continue;
			}
			try {
				f.setAccessible(true);
				Object value = f.get(entity);
				if (!(value instanceof String)) {
					continue;
				}
				String strVal = (String) value;
				if (!strVal.matches("\\d{4}/\\d{2}/\\d{2}\\s*-\\s*\\d{4}/\\d{2}/\\d{2}") && !strVal.matches("\\d{4}/\\d{2}/\\d{2}(\\s*\\d{2}:\\d{2}:\\d{2})?\\s*-\\s*\\d{4}/\\d{2}/\\d{2}(\\s*\\d{2}:\\d{2}:\\d{2})?")) {
					return;
				}
				String timeArr[] = strVal.split("-");
				String timeBegin = timeArr[0].trim();
				String timeEnd = timeArr[1].trim();
				if (strVal.matches("\\d{4}/\\d{2}/\\d{2}\\s*-\\s*\\d{4}/\\d{2}/\\d{2}")) {
					String suffix = isAddSuffix ? " 00:00:00" : "";
					timeBegin = timeBegin.replace("/", "-") + suffix;
					timeEnd = timeEnd.replace("/", "-") + suffix;
				} else {
					timeBegin = timeBegin.replace("/", "-");
					timeEnd = timeEnd.replace("/", "-");
				}
				String methodPrefix = name.substring(0, name.indexOf("Interval"));
				methodPrefix = "set" + methodPrefix.substring(0, 1).toUpperCase() + methodPrefix.substring(1);
				Method methodBegin = CommonStandardUtil.getMethodByNameWithSuper(clazz, methodPrefix + "Begin");
				methodBegin.invoke(entity, timeBegin);
				Method methodEnd = CommonStandardUtil.getMethodByNameWithSuper(clazz, methodPrefix + "End");
				methodEnd.invoke(entity, timeEnd);
			} catch (Exception e) {
				log.info("框架|设置时间间隔失败。");
				if (log.isDebugEnabled()) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 获取格式化后的请求参数
	 * @param params 请求参数
	 */
	public static String formatParameters(Map<String, String[]> params) {
		if (params == null || params.isEmpty()) {
			return "";
		}
		Map<String, String[]> sortMap = new TreeMap<>(params);
		StringBuilder sb = new StringBuilder(32);
		for (Map.Entry<String, String[]> entry : sortMap.entrySet()) {
			sb.append(entry.getKey()).append("=>{");
			String[] arr = entry.getValue();
			int len = arr.length;
			if (len > 0) {
				for (int i = 0; i < len; i++) {
					sb.append(arr[i]);
					if (i != len - 1) {
						sb.append(", ");
					}
				}
			}
			sb.append("}, ");
		}
		return sb.substring(0, sb.length() - 2);
	}

	/**
	 * 获取当前用户
	 * @return sys 当前登陆用户
	 */
	public static User getCurrendUser() {
		Object obj = sessionVal(CommonConstants.FrameWork.SESSION_USER);
		if (!(obj instanceof User)) {
			return null;
		}
		return (User) obj;
	}
}