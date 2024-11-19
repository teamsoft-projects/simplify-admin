package com.teamsoft.framework.common.core;

import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.symmetric.AES;
import com.google.gson.Gson;
import com.teamsoft.framework.sys.model.Dictionary;
import com.teamsoft.framework.sys.model.Menu;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Pattern;

/**
 * 公共常量类
 * @author zhangcc
 * @version 2017/9/13
 */
public class CommonConstants {
	// 项目名
	public static String APP_PATH = "";
	// AES加/解密盐值
	public static Map<String, byte[]> AES_SALT = new HashMap<>();
	// 分页默认的起始数据
	public static int PAGE_START = 0;
	// 分页默认的单页数据条数
	public static int PAGE_SIZE = 10;
	/**
	 * 文件上传默认目录
	 */
	public static String UPLOAD_ROOT_DIRECTORY;
	/**
	 * 文件服务器URL
	 */
	public static String FILE_SERVER_URL;

	/** 系统缓存初始化已完成 */
	public static boolean IS_SYSTEM_CACHE_INITED;

	/**
	 * 框架相关常量
	 */
	public interface FrameWork {
		// json转换常量
		Gson GSON = new Gson();

		// json的ContentType
		String JSON_CONTENTTYPE = "application/json";

		// session存放user的key
		String SESSION_USER = "LOGIN_USER";

		// 返回时存放功能列表的key
		String RESPONSE_FUNCTIONS = "functions";

		// 用户登录页面路径
		String LOGIN_PAGE_URI = "/sys/user/login";
		// 文件上传统一名称
		String FILE_UPLOAD_NAME = "file";

		// 主键冲突正则
		Pattern PATTERN_DUPLICATE_KEY = Pattern.compile("^Duplicate entry '(\\w+)'.*");

		// 默认AES秘钥，密文
		String DEFAULT_AES_KEY_HIDDEN = "dyZVUURDZ1dDTSpoRWhiS3pKNnJedGFIbkNrJE5iQDQ=";
		// 默认加解密AES
		AES DEFAULT_ENCRY_AES = new AES(Mode.ECB, Padding.PKCS5Padding, Base64.getDecoder().decode(DEFAULT_AES_KEY_HIDDEN));

		// HTTP请求成功标识
		Integer STATUS_SUCCESS = 200;
		// HTTP请求失败标识
		Integer STATUS_FAILURE = 500;

		// 成功标识符
		Integer SUCCESS_FLAG = 100101;
		// 失败标识符
		Integer FAILURE_FLAG = 100102;
		// 无登陆权限标识符
		Integer PERMISSION_DENIED_FLAG = 100103;
		// 提示信息标识符
		Integer BUSSINESS_NOTICE_FLAG = 100303;
		// 提示信息+重定向标识符
		Integer NOTICE_AND_REDIRECT_FLAG = 100305;
		// 提示信息, 抛出到前台, 由回调函数自行处理
		Integer SELF_NOTICE_FLAG = 100306;
		// 重定向标识符
		Integer DO_REDIRECT_FLAG = 100405;
		// Json视图标识符
		Integer JSON_VIEW_FLAG = 100600;
		// Json业务消息标识符
		Integer JSON_NOTICE_FLAG = 100601;
		// 参数错误消息标识符
		Integer PARAMETER_ERROR_FLAG = 100701;

		// 请求成功消息
		String SUCCESS_MESSAGE = "操作成功";
		//请求失败消息
		String FAILURE_MESSAGE = "操作失败";
		// 无访问权限消息
		String PERMISSIONDENIED_MESSAGE = "无访问权限";

		// 参数错误提示
		String PARAM_ERROR = "参数错误";
		// 验证码错误
		String CODE_ERROR = "验证码错误";
		// 请求过于频繁
		String ERROR_QUEST_TOO_FREQUENT = "请求过于频繁";
		// 返回类型默认model名
		String RETURN_MODEL_NAME = "resultInfo";

		// 404页面路径
		String ERROR_PAGE_404 = "/404";
		// 500页面路径
		String ERROR_PAGE_500 = "/500";

		// 占位符正则
		Pattern PATTERN_PLACEHOLDER = Pattern.compile(":=(\\w+)");
	}

	/**
	 * 系统基础功能相关常量
	 */
	public interface System {
		// 系统配置缓存
		Map<String, String> SYSTEM_CONFIG_CACHE = new HashMap<>();
		// 所有二级菜单缓存
		Map<String, Menu> SECONDARY_MENU = new HashMap<>();
		// 数据字典缓存
		DualMap<String, String, Dictionary> DIC_CACHE = new DualMap<>();
		// 环境 - dev
		String SYSTEM_ENV_DEV = "dev";

		// 随机数生成器
		Random SYSTEM_RANDOM_MAKER = new Random();

		// 发送Post请求类型-FORM请求
		String POST_TYPE_FORM = "FORM";
		// 发送Post请求类型-JSON请求
		String POST_TYPE_JSON = "JSON";
		/** 特殊用户ID - 系统 */
		String SMS_SENDER_ID_SYSTEM = "0000";
		String SMS_SENDER_NAME_SYSTEM = "系统";

		// 系统缓存-分割标识
		String SYSTEM_CACHE_SPLIT = "SPLIT_SYMBOL";

		// 角色不存在提示信息
		String ROLE_NOT_EXISTS = "role not exists";
		// md5签名盐值
		String ENCRY_SALT = "layui";
		// 默认用户密码
		String DEFAULT_PASSWD = "a123456";
		//顶层菜单parentId
		String MENU_TOP = "top";
		// 是否~是
		Integer YES_OPTION = 1;
		// 是否~否
		Integer NO_OPTION = 0;
		// 状态 - 启用
		Integer STATUS_ENABLE = 1;
		// 状态 - 禁用
		Integer STATUS_DISABLE = 0;
		// 是否~是(字符串型)
		String YES_OPTION_STR = "1";
		String NO_OPTION_STR = "0";
		String YES_OPTION_NAME = "是";
		String NO_OPTION_NAME = "否";

		// 用户不存在
		String MESSAGE_USER_NOT_EXISTS = "user not exists";

		/**
		 * 数据字典部分
		 */
		String DIC_GROUP_CODE_EXISTS = "分组编码已存在";
		String DIC_GROUP_NAME_EXISTS = "分组名称已存在";
		String DIC_GROUP_HAS_DIC_UNDER = "存在未删除的数据字典信息, 请删除下级后重试";
		String DIC_SAME_GROUP_CODE_EXISTS = "同一分组已存在该数据字典编码";
		String DIC_NAME_EXISTS = "数据字典名称已存在";
		String DIC_SUPER_NOT_EXISTS = "上级编码不存在, 请输入正确的上级编码";
		/**
		 * 用户名或密码错误
		 */
		String MESSAGE_LOGINNAME_PASSWD_ERROR = "用户名或密码错误";

		/**
		 * 数字常量
		 */
		Integer NUMBER_ZERO = 0;
		Integer NUMBER_ONE = 1;
		Integer NUMBER_TWO = 2;
		Integer NUMBER_THREE = 3;
		Integer NUMBER_FOUR = 4;
		Integer NUMBER_FIVE = 5;

		/// JAVA8日期时间格式化
		/** 日期时间格式化(yyyy-MM-dd HH:mm:ss) */
		DateTimeFormatter FORMATTER_YMDHMS = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		/** 日期时间格式化(yyyyMMddHHmmss) */
		DateTimeFormatter FORMATTER_YMDHMS_WITHOUT_SYMBOL = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
		 /** 日期时间格式化(yyyy-MM-dd) */
		DateTimeFormatter FORMATTER_YMD = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		/** 日期时间格式化(yyyy-MM-dd) */
		DateTimeFormatter FORMATTER_MD = DateTimeFormatter.ofPattern("MM-dd");
		 /** 日期时间格式化(yyyyMMdd) */
		DateTimeFormatter FORMATTER_YMD_WITHOUT_SYMBOL = DateTimeFormatter.ofPattern("yyyyMMdd");

		/// 通用日期时间格式化
		// 日期时间格式化(yyyy-MM-dd HH:mm:ss)
		DateFormat FORMAT_YMDHMS_WITH_SYMBOL = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// 日期时间格式化(yyyyMMddHHmmss)
		DateFormat FORMAT_YMDHMS = new SimpleDateFormat("yyyyMMddHHmmss");
		// 日期时间格式化(yyyy-MM-dd)
		DateFormat FORMAT_YMD = new SimpleDateFormat("yyyy-MM-dd");
		//YYYY/mm/dd日期正则
		String DATE_REG = "\\d{4}/\\d{2}/\\d{2}";
		// 编码格式 - utf-8
		String CHARSET_UTF8 = "utf-8";

		String TRANSLATE_FLAG = "TRANSLATE_FLAG";
		// 系统缓存-group code 用户列表
		String CACHE_KEY_USER_LIST = "USER-LIST";
	}

	/**
	 * 加密/解密常量
	 */
	public interface Encry {
		String MD5_ENCRY = "MD5";
		String AES_ENCRY = "AES";
		String AESENCRY_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";
		Integer AES_KEY_SIZE = 128;
		String PARAMETER_APPID = "appId";
		String PARAMETER_KEY = "data";
	}

	/**
	 * 文件读写相关
	 */
	public interface Files {
		// 文件后缀xls
		String EXCEL_MARK_XLS = "xls";
		// 文件后缀xlsx
		String EXCEL_MARK_XLSX = "xlsx";
	}

	/**
	 * Excel读写枚举
	 * @author zhangcc
	 * @version 2017/9/16
	 */
	public enum POIEnum {
		SOURCEPATH,
		SOURCEFILE,
		DEFAULTSHEET,
		STARTROW,
		MODELCLASS
	}
}