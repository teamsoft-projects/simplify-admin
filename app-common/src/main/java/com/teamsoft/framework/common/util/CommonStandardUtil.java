package com.teamsoft.framework.common.util;

import com.teamsoft.framework.common.core.CommonConstants;
import com.teamsoft.framework.common.model.Page;
import org.springframework.util.StringUtils;
import org.springframework.web.context.ContextLoader;

import javax.servlet.ServletContext;
import java.io.*;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 类描述：公共工具类
 * @author zhangcc
 * @version 2015-5-16 下午10:22:33
 */
public class CommonStandardUtil {
	// 生成随机字符串的元数据
	private static final String WORD_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";

	/**
	 * 获取异常完整堆栈信息
	 * @param e 异常信息
	 * @return 异常完整堆栈信息
	 */
	public static List<String> getExceptionStack(Throwable e) {
		List<String> retList = new ArrayList<>();
		StringWriter sw = null;
		PrintWriter pw = null;
		try {
			sw = new StringWriter();
			pw = new PrintWriter(sw);
			// 将出错的栈信息输出到printWriter中
			e.printStackTrace(pw);
			pw.flush();
			sw.flush();
		} finally {
			if (sw != null) {
				try {
					sw.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			if (pw != null) {
				pw.close();
			}
		}
		String[] arr = sw.toString().split("\r\n");
		Collections.addAll(retList, arr);
		return retList;
	}

	/**
	 * 获取servlet上下文
	 * @return ServletContext
	 */
	public static ServletContext getServletContext() {
		return Objects.requireNonNull(ContextLoader.getCurrentWebApplicationContext()).getServletContext();
	}

	/**
	 * 数字前导填零
	 * @param num 待填零的数字 width 需要的数字宽度
	 * @return 填零后的数字字符串形式
	 */
	public static String getPreFillNum(int num, int width) {
		StringBuilder strNum = new StringBuilder(String.valueOf(num));
		int len = strNum.length();
		if (num <= 0 || width - len <= 0) {
			return String.valueOf(num);
		}
		for (int i = 0; i < width - len; i++) {
			strNum.insert(0, "0");
		}
		return strNum.toString();
	}

	/**
	 * 根据位数获取随机数
	 * @param width 位数
	 * @return 随机数
	 */
	public static String getRandomNum(int width) {
		StringBuilder result = new StringBuilder();
		result.append(String.valueOf((long) (new Date().getTime() * Math.random() * 1000)));
		int len = result.length();
		if (len < width) {
			for (int i = width - len; i > 0; i--) {
				result.insert(0, "0");
			}
		} else {
			return result.substring(len - width, len);
		}
		return result.toString();
	}

	/**
	 * Description: 获取指定位数的随机字符串
	 * @param width 位数
	 * @return 随机字符串
	 */
	public static String getRandomStr(int width) {
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < width; i++) {
			int d = (int) (Math.random() * WORD_STRING.length());
			result.append(WORD_STRING.charAt(d));
		}
		return result.toString();
	}

	/**
	 * 获取Object的String值, null返回空串
	 */
	public static String getStringValue(Object value) {
		if (value == null) {
			return "";
		}

		return value.toString();
	}

	/**
	 * 字符串是否以指定后缀结尾
	 * @param str 待检查字符串 suffix 后缀
	 * @return 是否以指定后缀结尾
	 */
	public static boolean startsWithIgnoreCase(String str, String prefix) {
		return !(!StringUtils.hasLength(str) || !StringUtils.hasLength(prefix) || str.length() < prefix.length())
				&& str.substring(0, prefix.length()).equalsIgnoreCase(prefix);
	}

	/**
	 * 字符串是否以指定后缀结尾
	 * @param str 待检查字符串 suffix 后缀
	 * @return 是否以指定后缀结尾
	 */
	public static boolean endsWithIgnoreCase(String str, String suffix) {
		return !(!StringUtils.hasLength(str) || !StringUtils.hasLength(suffix) || str.length() < suffix.length())
				&& str.substring(str.length() - suffix.length()).equalsIgnoreCase(suffix);
	}

	/**
	 * 从集合中获取目标对象(如果集合中存在)
	 * @param c      待查找集合
	 * @param target 待比较对象
	 * @return 当集合中有对象满足e.equals(target)时, 返回e, 否则返回null
	 */
	public static <T> T getFromCollection(Collection<T> c, T target) {
		if (c == null || target == null) {
			return null;
		}
		for (T t : c) {
			if (t != null && t.equals(target)) {
				return t;
			}
		}
		return null;
	}

	/**
	 * 获取指定字符串中子串的出现位置, 忽略大小写
	 * @param source 目标字符串
	 * @param suffix 子串
	 * @return 子串在目标字符串中出现的位置
	 */
	public static int indexOfIgnoreCase(String source, String suffix) {
		return source.toLowerCase().indexOf(suffix.toLowerCase());
	}

	/**
	 * 初始化Page信息
	 * @param page 前台传入的page参数
	 */
	public static Page initializationPage(Page page) {
		if (page != null) {
			Integer start = page.getPageStart();
			Integer size = page.getPageSize();
			if (start == null && size != null) {
				page.setPageStart(CommonConstants.PAGE_START);
			} else if (start != null && size == null) {
				page.setPageSize(CommonConstants.PAGE_SIZE);
			} else if (start == null) {
				// 如果Page的两个成员变量都为null, 则将page置为null
				page = null;
			}
			if (page != null && start != null && start < 0) {
				page.setPageStart(CommonConstants.PAGE_START);
			}
		}
		return page;
	}

	/**
	 * 生成32位的UUId
	 * @return UUId
	 */
	public static String generateUUId() {
		return UUID.randomUUID().toString().replace("-", "");
	}

	/**
	 * 通过调换规则, 解密混淆后的密钥
	 */
	public static String unmix(String appSecret, String mixRule) {
		// int len = 64; // 混淆加密次数
		byte[] b = EncryptUtil.Base64.decodeToByte(mixRule);
		reverse(b); // 反向解密
		StringBuilder sb = new StringBuilder(appSecret);
		for (int i = 0; i < b.length / 2; i++) {
			int first = b[i * 2];
			int second = b[i * 2 + 1];
			String firstStr = String.valueOf(sb.charAt(first));
			String secondStr = String.valueOf(sb.charAt(second));
			sb.replace(first, first + 1, secondStr);
			sb.replace(second, second + 1, firstStr);
		}
		return sb.toString();
	}

	/**
	 * 数组反序
	 */
	public static void reverse(Object arr) {
		if (arr == null || !arr.getClass().isArray()) {
			return;
		}
		int len = Array.getLength(arr);
		for (int i = 0; i < len / 2; i++) {
			Object temp = Array.get(arr, i);
			Array.set(arr, i, Array.get(arr, len - i - 1));
			Array.set(arr, len - i - 1, temp);
		}
	}

	/**
	 * 判断集合中是否存在指定字符串, 忽略大小写
	 */
	public static boolean containsIgnoreCase(Collection<String> collection, String str) {
		for (String temp : collection) {
			if (str.equalsIgnoreCase(temp)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 文件转base64编码
	 * @param source 待转码文件
	 * @return 转成base64位编码的结果
	 */
	public static String fileToBase64(File source) {
		InputStream is = null;
		try {
			is = new FileInputStream(source);
			byte[] data = new byte[is.available()];
			is.read(data);
			return EncryptUtil.Base64.encodeFormByte(data);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * base64编码转文件
	 * @param source base64位编码的文件内容
	 * @param target 转码结果文件
	 */
	public static void base64ToFile(String source, File target) {
		if (source == null || target == null) {
			return;
		}
		OutputStream os = null;
		try {
			os = new FileOutputStream(target);
			byte[] data = EncryptUtil.Base64.decodeToByte(source);
			os.write(data);
			os.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (os != null) {
					os.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 创建一个List, 并将参数放入List内
	 * @param t 元素
	 * @return 包含传入参数作为元素的List集合
	 */
	@SafeVarargs
	public static <T> List<T> addToList(T... t) {
		List<T> list = new ArrayList<>();
		if (t == null) {
			return list;
		}
		Collections.addAll(list, t);
		return list;
	}

	/**
	 * 获取从父类中继承的字段(public/protected)
	 * @param source 源对象
	 * @return 从父类中继承的字段列表
	 */
	public static List<Field> getSuperFields(Object source) {
		List<Field> retFields = new ArrayList<>();
		if (source == null) {
			return retFields;
		}
		Class<?> clazz = source.getClass();
		for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
			if (clazz == null) {
				return retFields;
			}
			Field[] fs = clazz.getDeclaredFields();
			retFields.addAll(Arrays.asList(fs));
		}
		return retFields;
	}

	/**
	 * 获取类的所有声明字段，包括从父类中继承的字段(public/protected)
	 * @param source 源对象
	 * @return 自己的和从父类中继承的字段列表
	 */
	public static List<Field> getAllFieldsWithSuper(Object source) {
		List<Field> retFields = new ArrayList<>();
		if (source == null) {
			return retFields;
		}
		Class clazz = source.getClass();
		retFields.addAll(getSuperFields(source));
		Field[] fs = clazz.getDeclaredFields();
		if (fs != null && fs.length != 0) {
			retFields.addAll(Arrays.asList(fs));
		}

		return retFields;
	}

	/**
	 * 通过名称获取类的指定方法，包括从父类继承的方法
	 */
	public static Method getMethodByNameWithSuper(Class<?> clazz, String name) throws NoSuchMethodException {
		List<Method> ms = new ArrayList<>();
		Method[] m1 = clazz.getDeclaredMethods();
		if (m1 != null && m1.length != 0) {
			ms.addAll(Arrays.asList(m1));
		}
		for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
			Method[] m2 = clazz.getDeclaredMethods();
			if (m2 != null && m2.length != 0) {
				ms.addAll(Arrays.asList(m2));
			}
		}
		for (Method m : ms) {
			if (name.equalsIgnoreCase(m.getName())) {
				return m;
			}
		}
		throw new NoSuchMethodException(name);
	}
}