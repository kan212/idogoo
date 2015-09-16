package com.idogoo.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {
	private static String toHex(byte b) {
		return ("" + "0123456789ABCDEF".charAt(0xf & b >> 4) + "0123456789ABCDEF"
				.charAt(b & 0xf));
	}

	private static String convert_To_HexString(byte[] b) {
		String hex = "";
		for (byte element : b) {
			hex += toHex(element);
		}
		return hex;
	}

	public static String EncoderByMD5(String str) {
		String code = str;
		if (str != null) {
			try {
				MessageDigest md5 = MessageDigest.getInstance("MD5");
				code = convert_To_HexString(md5.digest(str.getBytes("utf-8")));
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return code;
	}

	public static String EncoderByMD5(byte[] data) {
		String code = "";
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			code = convert_To_HexString(md5.digest(data));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return code;
	}

	public static byte[] getByteEncoder(byte[] data) {
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			return md5.digest(data);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}
}
