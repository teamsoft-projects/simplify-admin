package com.teamsoft.framework.common.util;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.symmetric.AES;
import com.teamsoft.framework.common.annotation.Encrypt;
import com.teamsoft.framework.common.annotation.Translate;
import com.teamsoft.framework.common.model.EncryptType;
import com.teamsoft.framework.sys.model.Dictionary;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.Base64;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.teamsoft.framework.common.core.CommonConstants.System.DIC_CACHE;

/**
 * 反射工具类
 */
@Slf4j
public class ReflectUtil {
	/**
	 * 翻译DTO
	 */
	public static void translateDTO(Object dto) {
		if (Objects.isNull(dto)) {
			return;
		}
		/* 获取这个类下的所有字段 */
		Field[] declaredFields = dto.getClass().getDeclaredFields();
		for (Field field : declaredFields) {
			/* 处理嵌套在目标对象类中的集合类型翻译 */
			String fieldName = field.getName();
			Object fieldValue = BeanUtil.getFieldValue(dto, fieldName);
			if (fieldValue instanceof Collection) {
				Collection<?> list = (Collection<?>) fieldValue;
				if (CollectionUtil.isEmpty(list)) continue;
				for (Object row : list) {
					if (row.getClass().isPrimitive()) continue;
					ReflectUtil.translateDTO(row);
				}
			}

			/* 获取类上的@Translate注解 */
			Translate translate = field.getAnnotation(Translate.class);
			/* 如果没有此注解则跳过 */
			if (Objects.isNull(translate)) {
				continue;
			}

			/* 获取声明的字典来源信息 */
			String from = translate.from();
			String groupCode = translate.groupCode();
			boolean isMulti = translate.isMulti();
			String separator = translate.separator();

			Object fromVal = BeanUtil.getFieldValue(dto, from);
			if (ObjectUtil.isEmpty(fromVal)) {
				continue;
			}
			String fromValue = String.valueOf(fromVal);
			if (!isMulti) {
				/* 调用资源开始翻译 */
				Dictionary dic = DIC_CACHE.get(groupCode, fromValue);
				if (Objects.isNull(dic)) {
					continue;
				}
				BeanUtil.setFieldValue(dto, fieldName, dic.getName());
			} else {
				// 处理多标签翻译
				final String[] split = fromValue.split(separator);
				String multiResult = Stream.of(split)
						.map(val -> {
							Dictionary dic = DIC_CACHE.get(groupCode, val);
							if (Objects.isNull(dic)) {
								return null;
							}
							return dic.getName();
						})
						.filter(Objects::nonNull)
						.collect(Collectors.joining(separator));
				/* 赋值翻译字段 (多个) */
				BeanUtil.setFieldValue(dto, fieldName, multiResult);
			}
		}
	}

	/**
	 * 加密DTO
	 */
	public static void encryptDTO(Object dto) {
		if (Objects.isNull(dto)) {
			return;
		}

		/* 获取这个类下的所有字段 */
		Field[] declaredFields = dto.getClass().getDeclaredFields();
		for (Field field : declaredFields) {
			/* 处理嵌套在目标对象类中的集合类型翻译 */
			String fieldName = field.getName();
			Object fieldValue = BeanUtil.getFieldValue(dto, fieldName);
			if (fieldValue instanceof Collection) {
				Collection<?> list = (Collection<?>) fieldValue;
				if (CollectionUtil.isEmpty(list)) {
					continue;
				}
				for (Object row : list) {
					if (row.getClass().isPrimitive()) {
						continue;
					}
					ReflectUtil.encryptDTO(row);
				}
			}

			/* 获取类上的@Encrypt注解 */
			Encrypt encrypt = field.getAnnotation(Encrypt.class);
			/* 如果没有此注解则跳过 */
			if (Objects.isNull(encrypt)) {
				continue;
			}

			if (ObjectUtil.isEmpty(fieldValue)) {
				continue;
			}
			String fieldValueStr = String.valueOf(fieldValue);

			/* 获取声明的字典来源信息 */
			EncryptType type = encrypt.type();
			String encryKey = encrypt.encryKey();
			boolean isMulti = encrypt.isMulti();
			String separator = encrypt.separator();

			if (!isMulti) {
				if (EncryptType.ENCRY_AES.equals(type)) {
					AES encryAES = new AES(Mode.ECB, Padding.PKCS5Padding, Base64.getDecoder().decode(encryKey));
					try {
						String decrypt = encryAES.encryptBase64(fieldValueStr);
						BeanUtil.setFieldValue(dto, fieldName, decrypt);
					} catch (Exception e) {
						log.error("加密数据解密失败，字段：" + fieldName + "，原因：{}", e.getMessage());
					}
				} else if (EncryptType.ENCRY_BASE64.equals(type)) {
					try {
						String decrypt = Base64.getEncoder().encodeToString(fieldValueStr.getBytes());
						BeanUtil.setFieldValue(dto, fieldName, decrypt);
					} catch (Exception e) {
						log.error("加密数据解密失败，字段：" + fieldName + "，原因：{}", e.getMessage());
					}
				}
			} else {
				// 处理多标签翻译
				final String[] split = fieldValueStr.split(separator);
				if (EncryptType.ENCRY_AES.equals(type)) {
					AES encryAES = new AES(Mode.ECB, Padding.PKCS5Padding, Base64.getDecoder().decode(encryKey));
					String multiResult = Stream.of(split)
							.map(val -> {
								try {
									return encryAES.encryptBase64(String.valueOf(val));
								} catch (Exception e) {
									log.error("加密数据解密失败，字段：" + fieldName + "，原因：{}", e.getMessage());
									return null;
								}
							})
							.filter(Objects::nonNull)
							.collect(Collectors.joining(separator));

					/* 赋值翻译字段 (多个) */
					BeanUtil.setFieldValue(dto, fieldName, multiResult);
				} else if (EncryptType.ENCRY_BASE64.equals(type)) {
					String multiResult = Stream.of(split)
							.map(val -> {
								try {
									return Base64.getEncoder().encodeToString(val.getBytes());
								} catch (Exception e) {
									log.error("加密数据解密失败，字段：" + fieldName + "，原因：{}", e.getMessage());
									return null;
								}
							})
							.filter(Objects::nonNull)
							.collect(Collectors.joining(separator));

					/* 赋值翻译字段 (多个) */
					BeanUtil.setFieldValue(dto, fieldName, multiResult);
				}
			}
		}
	}

	/**
	 * 解密DTO
	 */
	public static void decryptDTO(Object dto) {
		if (Objects.isNull(dto)) {
			return;
		}

		/* 获取这个类下的所有字段 */
		Field[] declaredFields = dto.getClass().getDeclaredFields();
		for (Field field : declaredFields) {
			/* 处理嵌套在目标对象类中的集合类型翻译 */
			String fieldName = field.getName();
			Object fieldValue = BeanUtil.getFieldValue(dto, fieldName);
			if (fieldValue instanceof Collection) {
				Collection<?> list = (Collection<?>) fieldValue;
				if (CollectionUtil.isEmpty(list)) {
					continue;
				}
				for (Object row : list) {
					if (row.getClass().isPrimitive()) {
						continue;
					}
					ReflectUtil.decryptDTO(row);
				}
			}

			/* 获取类上的@Encrypt注解 */
			Encrypt encrypt = field.getAnnotation(Encrypt.class);
			/* 如果没有此注解则跳过 */
			if (Objects.isNull(encrypt)) {
				continue;
			}

			if (ObjectUtil.isEmpty(fieldValue)) {
				continue;
			}
			String fieldValueStr = String.valueOf(fieldValue);

			/* 获取声明的字典来源信息 */
			EncryptType type = encrypt.type();
			String encryKey = encrypt.encryKey();
			boolean isMulti = encrypt.isMulti();
			String separator = encrypt.separator();

			if (!isMulti) {
				if (EncryptType.ENCRY_AES.equals(type)) {
					AES encryAES = new AES(Mode.ECB, Padding.PKCS5Padding, Base64.getDecoder().decode(encryKey));
					try {
						String decrypt = encryAES.decryptStr(fieldValueStr);
						BeanUtil.setFieldValue(dto, fieldName, decrypt);
					} catch (Exception e) {
						log.error("加密数据解密失败，字段：" + fieldName + "，原因：{}", e.getMessage());
					}
				} else if (EncryptType.ENCRY_BASE64.equals(type)) {
					try {
						String decrypt = new String(Base64.getDecoder().decode(fieldValueStr));
						BeanUtil.setFieldValue(dto, fieldName, decrypt);
					} catch (Exception e) {
						log.error("加密数据解密失败，字段：" + fieldName + "，原因：{}", e.getMessage());
					}
				}
			} else {
				// 处理多标签翻译
				final String[] split = fieldValueStr.split(separator);
				if (EncryptType.ENCRY_AES.equals(type)) {
					AES encryAES = new AES(Mode.ECB, Padding.PKCS5Padding, Base64.getDecoder().decode(encryKey));
					String multiResult = Stream.of(split)
							.map(val -> {
								try {
									return encryAES.decryptStr(String.valueOf(val));
								} catch (Exception e) {
									log.error("加密数据解密失败，字段：" + fieldName + "，原因：{}", e.getMessage());
									return null;
								}
							})
							.filter(Objects::nonNull)
							.collect(Collectors.joining(separator));

					/* 赋值翻译字段 (多个) */
					BeanUtil.setFieldValue(dto, fieldName, multiResult);
				} else if (EncryptType.ENCRY_BASE64.equals(type)) {
					String multiResult = Stream.of(split)
							.map(val -> {
								try {
									return new String(Base64.getDecoder().decode(val));
								} catch (Exception e) {
									log.error("加密数据解密失败，字段：" + fieldName + "，原因：{}", e.getMessage());
									return null;
								}
							})
							.filter(Objects::nonNull)
							.collect(Collectors.joining(separator));

					/* 赋值翻译字段 (多个) */
					BeanUtil.setFieldValue(dto, fieldName, multiResult);
				}
			}
		}
	}
}