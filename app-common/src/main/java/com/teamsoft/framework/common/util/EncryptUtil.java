package com.teamsoft.framework.common.util;

import com.teamsoft.framework.common.core.CommonConstants;
import com.teamsoft.framework.common.exception.BusinessException;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.List;

/**
 * 加密工具类
 * @author zhangcc
 * @version 2016/10/9 16:14
 */
public class EncryptUtil {
	/**
	 * MD5工具类
	 */
	public static class MD5 {
		// 签名源字符
		private final static String[] hexDigits = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

		/**
		 * 签名
		 */
		private static String byteArrayToHexString(byte[] b) {
			StringBuilder result = new StringBuilder();
			for (byte temp : b) {
				result.append(byteToHexString(temp));
			}
			return result.toString();
		}

		/**
		 * 提取摘要
		 */
		private static String byteToHexString(byte b) {
			int n = b;
			if (n < 0) {
				n = 256 + n;
			}
			int d1 = n / 16;
			int d2 = n % 16;
			return hexDigits[d1] + hexDigits[d2];
		}

		/**
		 * MD5签名
		 * @param origin 待签名字符串
		 * @return 签名结果
		 */
		public static String encode(String origin) {
			String result = origin;
			try {
				MessageDigest md = MessageDigest.getInstance(CommonConstants.Encry.MD5_ENCRY);
				result = byteArrayToHexString(md.digest(result.getBytes(StandardCharsets.UTF_8)));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}
	}

	/**
	 * AES加密工具类
	 */
	public static class AES {
		/**
		 * 加密
		 */
		public static String encode(byte[] encrySalt, String content) {
			try {
				SecretKeySpec key = new SecretKeySpec(Arrays.copyOf(encrySalt, 16), CommonConstants.Encry.AES_ENCRY);
				Cipher cipher = Cipher.getInstance(CommonConstants.Encry.AESENCRY_CIPHER_ALGORITHM);
				byte[] byteContent = content.getBytes(StandardCharsets.UTF_8);
				cipher.init(Cipher.ENCRYPT_MODE, key);
				byte[] byteAes = cipher.doFinal(byteContent);
				// 将加密后的数据转换为字符串
				return Base64.encodeFormByte(byteAes);
			} catch (Exception e) {
				e.printStackTrace();
			}
			// 如果有错就返加null
			return null;
		}

		/**
		 * 解密
		 */
		public static String decode(byte[] encrySalt, String content) {
			try {
				SecretKeySpec key = new SecretKeySpec(Arrays.copyOf(encrySalt, 16), CommonConstants.Encry.AES_ENCRY);
				Cipher cipher = Cipher.getInstance(CommonConstants.Encry.AESENCRY_CIPHER_ALGORITHM);
				byte[] byteContent = Base64.decodeToByte(content);
				cipher.init(Cipher.DECRYPT_MODE, key);
				// 解密
				byte[] byteDecode = cipher.doFinal(byteContent);
				return new String(byteDecode, StandardCharsets.UTF_8);
			} catch (Exception e) {
				e.printStackTrace();
			}

			//如果有错就返加null
			return null;
		}
	}

	/**
	 * Base64加密工具类
	 */
	public static class Base64 {
		// 加密
		public static String encode(String str) {
			byte[] b = str.getBytes(StandardCharsets.UTF_8);
			return new BASE64Encoder().encode(b);
		}

		// 加密
		public static String encodeFormByte(byte[] b) {
			String s = null;
			if (b != null) {
				s = new BASE64Encoder().encode(b);
			}
			return s;
		}

		// 解密
		public static String decode(String s) {
			byte[] b;
			String result = null;
			if (s != null) {
				BASE64Decoder decoder = new BASE64Decoder();
				try {
					b = decoder.decodeBuffer(s);
					result = new String(b, StandardCharsets.UTF_8);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return result;
		}

		// 解密成字符数组
		public static byte[] decodeToByte(String s) {
			if (s != null) {
				BASE64Decoder decoder = new BASE64Decoder();
				try {
					return decoder.decodeBuffer(s);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return null;
		}
	}

	/**
	 * hmacSHA加密
	 */
	public static class SHA {
		/**
		 * hmacSHA256加密
		 * @param encryptText 待加密明文
		 * @param encryptKey  加密密钥
		 */
		public static String sha256(String encryptText, String encryptKey) {
			try {
				String MAC_NAME = "HmacSHA256";
				byte[] data = encryptKey.getBytes(StandardCharsets.UTF_8);
				SecretKey secretKey = new SecretKeySpec(data, MAC_NAME);
				//生成一个指定 Mac 算法 的 Mac 对象
				Mac mac = Mac.getInstance(MAC_NAME);
				mac.init(secretKey);
				byte[] text = encryptText.getBytes(StandardCharsets.UTF_8);
				return Base64.encodeFormByte(mac.doFinal(text));
			} catch (Exception e) {
				throw new BusinessException("hmacSHA256加密加密失败.");
			}
		}

		/**
		 * 用SHA1算法生成安全签名
		 * @param str 待加密明文
		 * @return 加密后的秘闻
		 */
		public static String sha1(String str) {
			try {
				// SHA1签名生成
				MessageDigest md = MessageDigest.getInstance("SHA-1");
				md.update(str.getBytes());
				byte[] digest = md.digest();
				StringBuilder hexstr = new StringBuilder();
				String shaHex;
				for (byte aDigest : digest) {
					shaHex = Integer.toHexString(aDigest & 0xFF);
					if (shaHex.length() < 2) {
						hexstr.append(0);
					}
					hexstr.append(shaHex);
				}
				return hexstr.toString();
			} catch (Exception e) {
				throw new BusinessException("hmacSHA1加密加密失败.");
			}
		}

		/**
		 * 用SHA1算法生成安全签名
		 * @param params 待加密参数集合
		 * @return 安全签名
		 */
		public static String sha1(String... params) {
			List<String> paramsList = Arrays.asList(params);
			paramsList.sort((o1, o2) -> o1 == null ? -1 : o2 == null ? 1 : o1.compareTo(o2));
			StringBuilder str = new StringBuilder();
			for (String param : paramsList) {
				str.append(param);
			}
			return sha1(str.toString());
		}

		/**
		 * 用SHA1算法生成安全签名
		 * @param encryptKey 密文
		 * @param params     待加密参数集合
		 * @return 安全签名
		 */
		public static String sha1(String encryptKey, List<String> params) {
			params.add(encryptKey);
			params.sort((o1, o2) -> o1 == null ? -1 : o2 == null ? 1 : o1.compareTo(o2));
			StringBuilder str = new StringBuilder();
			for (String param : params) {
				str.append(param);
			}
			return sha1(str.toString());
		}
	}
}