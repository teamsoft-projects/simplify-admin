package com.teamsoft.mgr.common.util;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.teaopenapi.models.Config;
import com.teamsoft.framework.common.core.CommonConstants;
import com.teamsoft.framework.common.exception.BusinessException;
import com.teamsoft.framework.common.exception.VerifyException;
import com.teamsoft.framework.common.util.CommonWebUtil;
import com.teamsoft.framework.common.util.EncryptUtil;
import com.teamsoft.mgr.common.model.Constants;
import com.teamsoft.mgr.common.model.HttpResponseModel;
import com.teamsoft.mgr.common.model.ImageModel;
import com.teamsoft.mgr.common.model.ImageType;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.teamsoft.framework.common.core.CommonConstants.IS_SYSTEM_CACHE_INITED;
import static com.teamsoft.framework.common.core.CommonConstants.System.SYSTEM_CONFIG_CACHE;
import static com.teamsoft.mgr.common.model.Constants.Sms.KEY_SMS_ACCESSKEY;
import static com.teamsoft.mgr.common.model.Constants.Sms.KEY_SMS_ACCESSKEY_SECRET;

/**
 * 类描述：请求相关工具类
 * @author zhangcc
 * @version 2015-5-30 下午2:17:02
 */
@Slf4j
public class WebUtil extends CommonWebUtil {
	/**
	 * 获取客户端请求IP
	 * @return 客户端请求IP
	 */
	public static String getClientIp() {
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		if (requestAttributes == null) {
			return "";
		}
		HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
		String remoteAddr = request.getHeader("X-FORWARDED-FOR");
		if (remoteAddr == null || remoteAddr.isEmpty()) {
			remoteAddr = request.getRemoteAddr();
		}
		return remoteAddr;
	}

	/**
	 * 解析base64位格式的图片
	 * @param imageData 图片内容，base64位格式
	 * @return 图片信息
	 */
	public static ImageModel resolveImage(String imageData) {
		String[] arr = imageData.split(";");
		if (arr.length != 2) {
			throw new VerifyException("待转换图片格式错误");
		}
		String imageType = arr[0];
		int typeIdx = imageType.indexOf("/");
		if (typeIdx == -1) {
			throw new VerifyException("待转换图片格式错误");
		}
		String suffix = imageType.substring(typeIdx + 1).toLowerCase();
		ImageModel ret = new ImageModel();
		if ("jpeg".equals(suffix)) {
			ret.setSuffix("jpg");
			ret.setType(ImageType.JPG);
		} else if ("png".equals(suffix)) {
			ret.setSuffix(suffix);
			ret.setType(ImageType.PNG);
		} else {
			throw new VerifyException("待转换图片格式错误");
		}
		String data = arr[1];
		int dataIdx = data.indexOf(",");
		if (dataIdx == -1) {
			throw new VerifyException("待转换图片格式错误");
		}
		data = data.substring(dataIdx + 1).trim();
		ret.setData(EncryptUtil.Base64.decodeToByte(data));
		return ret;
	}

	/**
	 * 保存字节数组中的图片
	 * @param dir   保存目录(相对upload目录)
	 * @param image 图片数据
	 * @return 图片相对路径
	 */
	public static String saveImageFromBytes(String dir, ImageModel image) {
		File directory = new File(CommonConstants.UPLOAD_ROOT_DIRECTORY + File.separator + dir);
		if (!directory.exists()) {
			directory.mkdirs();
		}
		String relativePath = "upload/" + dir;
		String fileName = CommonConstants.System.FORMAT_YMDHMS.format(new Date()) + CommonUtil.getRandomNum(8) + "." + image.getSuffix();
		relativePath += "/" + fileName;
		File output = new File(directory.getAbsolutePath() + File.separator + fileName);
		try {
			Files.write(Paths.get(output.toURI()), image.getData());
		} catch (IOException e) {
			throw new BusinessException("保存文件失败!");
		}
		return relativePath;
	}

	/**
	 * 发送Post请求
	 * @param url     请求地址
	 * @param headers 请求头信息
	 * @return 请求返回信息
	 */
	public static HttpResponseModel doGet(String url, Map<String, Object> headers) {
		return doGet(url, headers, null);
	}

	/**
	 * 发送Post请求
	 * @param url     请求地址
	 * @param params  请求参数
	 * @param headers 请求头参数
	 * @return 请求返回信息
	 */
	public static HttpResponseModel doGet(String url, Map<String, Object> headers, Map<String, Object> params) {
		HttpResponseModel resp = new HttpResponseModel();
		CloseableHttpClient httpClient = HttpClients.createDefault();
		// 设置请求和传输超时时间
		RequestConfig requestConfig = buildRequestConfig(6000, 6000);
		HttpGet request = null;
		try {
			// 设置请求参数URI
			URIBuilder uriBuilder = new URIBuilder(url);
			if (params != null && !params.isEmpty()) {
				for (Map.Entry<String, Object> entry : params.entrySet()) {
					String key = entry.getKey();
					String value = String.valueOf(entry.getValue());
					uriBuilder.setParameter(key, value);
				}
			}
			URI requestUri = uriBuilder.build();
			request = new HttpGet(requestUri);
			// 设置超时时间为6秒
			request.setConfig(requestConfig);
			// 设置请求header
			request.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:50.0) Gecko/20100101 Firefox/50.0");
			for (Map.Entry<String, Object> entry : headers.entrySet()) {
				String key = entry.getKey();
				String value = String.valueOf(entry.getValue());
				if (!StringUtils.hasLength(key)) {
					continue;
				}
				request.setHeader(key, value);
			}
			log.debug("请求URL： {}", requestUri);
			HttpResponse response = httpClient.execute(request);
			resp.setStatusCode(response.getStatusLine().getStatusCode());
			if (Constants.FrameWork.STATUS_SUCCESS.equals(resp.getStatusCode())) {
				resp.setResponseBody(EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8));
			}
		} catch (Exception e) {
			throw new BusinessException("远程请求GET|Exception|" + CommonUtil.getExceptionMsg(e));
		} finally {
			if (request != null) {
				request.releaseConnection();
			}
			try {
				httpClient.close();
			} catch (IOException e) {
				log.error("远程请求GET|Exception|{}", CommonUtil.getExceptionMsg(e));
			}
		}
		return resp;
	}

	/**
	 * 请求config配置
	 * @param socketTimeOut  socket超时
	 * @param connectTimeOut 连接超时
	 * @return requestConfig
	 */
	private static RequestConfig buildRequestConfig(int socketTimeOut, int connectTimeOut) {
		return RequestConfig.custom().setSocketTimeout(socketTimeOut).setConnectTimeout(connectTimeOut).build();
	}

	/**
	 * 等待系统缓存完成
	 * 注意，此方法必须在新线程中执行，否则死锁
	 */
	public static void waitForSystemCacheInited() {
		while (!IS_SYSTEM_CACHE_INITED) {
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException ignored) {
			}
		}
	}

	/**
	 * 获取阿里云短信平台配置
	 */
	public static Client createDysmsapiClient() throws Exception {
		String accessKey = SYSTEM_CONFIG_CACHE.get(KEY_SMS_ACCESSKEY);
		String accessSecret = EncryptUtil.Base64.decode(SYSTEM_CONFIG_CACHE.get(KEY_SMS_ACCESSKEY_SECRET));
		Config config = new Config()
				.setAccessKeyId(accessKey)
				.setAccessKeySecret(accessSecret);
		config.endpoint = "dysmsapi.aliyuncs.com";
		return new Client(config);
	}
}