package com.teamsoft.framework.common.controller;

import cn.hutool.core.map.MapBuilder;
import cn.hutool.core.util.StrUtil;
import com.teamsoft.framework.common.core.CommonConstants;
import com.teamsoft.framework.common.model.ResultInfo;
import com.teamsoft.framework.common.util.CommonStandardUtil;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.*;

import static com.teamsoft.framework.common.core.CommonConstants.FrameWork.DEFAULT_ENCRY_AES;
import static com.teamsoft.framework.common.core.CommonConstants.System.SYSTEM_CONFIG_CACHE;
import static com.teamsoft.framework.common.core.CommonConstants.System.YES_OPTION;
import static com.teamsoft.framework.common.core.CommonConstants.UPLOAD_ROOT_DIRECTORY;

/**
 * 公共控制类
 * @author dominealex
 * @version 2020/3/12
 */
@Controller
public class IController {
	/**
	 * 进入首页
	 */
	@RequestMapping("index")
	public ResultInfo index() {
		return ResultInfo.getSuccessInfo(SYSTEM_CONFIG_CACHE);
	}

	/**
	 * 进入图标选择页面
	 */
	@RequestMapping("icon")
	public ResultInfo icon(Integer type) {
		return ResultInfo.getSuccessInfo(type);
	}

	/**
	 * 未找到页面
	 */
	@RequestMapping("404")
	public void notFound() {
	}

	/**
	 * 系统错误
	 */
	@RequestMapping("500")
	public void exception() {
	}

	/**
	 * 解决swagger-ui首页请求报错问题
	 */
	@RequestMapping("csrf")
	public void csrf() {
	}

	/**
	 * 上传图片
	 * @param dir            图片保存相对路径
	 * @param useDefaultName 是否采用默认文件名(随下载相同)
	 * @param req            请求上下文
	 */
	@RequestMapping("upload")
	public ResultInfo upload(String dir, Integer useDefaultName, HttpServletRequest req) {
		if (!StringUtils.hasLength(dir) || !(req instanceof MultipartRequest)) {
			return ResultInfo.getParamErrorInfo();
		}
		File directory = new File(UPLOAD_ROOT_DIRECTORY + File.separator + dir);
		if (!directory.exists()) {
			directory.mkdirs();
		}
		String relativePath = "upload/" + dir;
		MultipartRequest request = (MultipartRequest) req;
		MultipartFile file = request.getFile(CommonConstants.FrameWork.FILE_UPLOAD_NAME);
		if (file == null) {
			return ResultInfo.getNoticeInfo("文件不存在");
		}
		String tempName = file.getOriginalFilename();
		assert tempName != null;
		String suffix = tempName.substring(tempName.lastIndexOf("."));
		String fileName;
		File output;
		if (YES_OPTION.equals(useDefaultName)) {
			fileName = tempName;
			output = new File(directory.getAbsolutePath() + File.separator + fileName);
		} else {
			// 如果不是使用默认名字，判断文件是否已存在，如果存在，重新生成文件名
			fileName = CommonConstants.System.FORMAT_YMDHMS.format(new Date()) + CommonStandardUtil.getRandomNum(8) + suffix;
			output = new File(directory.getAbsolutePath() + File.separator + fileName);
			while (output.exists()) {
				fileName = CommonConstants.System.FORMAT_YMDHMS.format(new Date()) + CommonStandardUtil.getRandomNum(8) + suffix;
				output = new File(directory.getAbsolutePath() + File.separator + fileName);
			}
		}
		relativePath += "/" + fileName;
		try {
			file.transferTo(output);
		} catch (IOException e) {
			e.printStackTrace();
			return ResultInfo.getNoticeInfo("上传失败，系统异常");
		}
		Map<String, String> ret = new HashMap<>();
		ret.put("filePath", relativePath);
		ret.put("fileURL", CommonConstants.FILE_SERVER_URL + relativePath);
		return ResultInfo.getSuccessInfo(ret);
	}

	/**
	 * AES加密
	 */
	@GetMapping("aes")
	public ResultInfo aes(String source, Integer isMulti, String split) {
		if (StrUtil.isEmpty(source)) {
			return ResultInfo.getParamErrorInfo();
		}
		if (!YES_OPTION.equals(isMulti)) {
			Map<String, String> map = MapBuilder.<String, String>create()
					.put("source", source)
					.put("result", DEFAULT_ENCRY_AES.encryptBase64(source))
					.build();
			return ResultInfo.getSuccessInfo(map);
		} else {
			List<Map<String, String>> ret = new ArrayList<>();
			split = StrUtil.isEmpty(split) ? "," : split;
			for (String src : source.split(split)) {
				Map<String, String> map = MapBuilder.<String, String>create()
						.put("source", src)
						.put("result", DEFAULT_ENCRY_AES.encryptBase64(src))
						.build();
				ret.add(map);
			}
			return ResultInfo.getSuccessInfo(ret);
		}
	}
}