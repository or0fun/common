package com.fang.common.security;


import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES 加密解密类
 * @author fang
 *
 */
public class AESUtil {
	Cipher cipherEnc;
	Cipher cipherDec;
	SecretKeySpec key;
	IvParameterSpec iv;
    private final static String defaultRandStr = "heUitbsNS1DND83B";
	public AESUtil(String randStr) {
		try {
			cipherEnc = Cipher.getInstance("AES/CBC/NoPadding");
		} catch (NoSuchAlgorithmException ex) {
			ex.printStackTrace();
		} catch (NoSuchPaddingException ex) {
			ex.printStackTrace();
		}
		try {
			cipherDec = Cipher.getInstance("AES/CBC/NoPadding");
		} catch (NoSuchAlgorithmException ex) {
			ex.printStackTrace();
		} catch (NoSuchPaddingException ex) {
			ex.printStackTrace();
		}
		String md5 = getMD5(randStr.getBytes());
		String keyStr = md5.substring(0, 16);
		String ivStr = md5.substring(16);
		key = new SecretKeySpec(keyStr.getBytes(), "AES");
		iv = new IvParameterSpec(ivStr.getBytes());
	}
    public AESUtil(){
    	this(defaultRandStr);
    }
	
	public String encrypt(String str) {
		return encrypt(key, iv, str);
	}
	
	public String decrypt(String str) {
		String re = decrypt(key, iv, str);
		if(re != null)
			return re.trim();
		return null;
	}
	
	private byte[] padRight(byte[] data, int n) {
		int len = 16 - data.length % 16 + data.length;
		byte[] newdata = new byte[len];
		int i = 0;
		for(i = 0; i < data.length; i++ ){
			newdata[i] = data[i];
		}
		for(;i < len; i++){
			newdata[i] = 32;
		}
		return newdata;
	}
	private String encrypt(SecretKeySpec enc_key, IvParameterSpec enc_iv,
			String str) {
		byte[] ret = null;

		try {
			cipherEnc.init(Cipher.ENCRYPT_MODE, enc_key, enc_iv);
			ret = padRight(str.getBytes("utf-8"), 16);
			ret = cipherEnc.doFinal(ret);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}

		return byteArray2HexString(ret);
	}

	/*
	 * str is Hex String
	 */
	private String decrypt(SecretKeySpec dec_key, IvParameterSpec dec_iv,
			String str) {
		byte[] ret = null;

		try {
			cipherDec.init(Cipher.DECRYPT_MODE, dec_key, dec_iv);
			ret = cipherDec.doFinal(hexString2ByteArray(str));
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}

		try {
			if(ret != null)
				return new String(ret, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return null;
		}
		return null;
	}

	static final char[] HEX_CHAR_TABLE = { '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	public String byteArray2HexString(byte[] b) {
		if (b == null) {
			return null;
		}
		final StringBuilder hex = new StringBuilder(2 * b.length);
		for (final byte by : b) {
			hex.append(HEX_CHAR_TABLE[(by & 0xF0) >> 4]).append(
					HEX_CHAR_TABLE[(by & 0x0F)]);
		}
		return hex.toString();
	}

	public byte[] hexString2ByteArray(String s) {
		if (s == null) {
			return null;
		}
		byte high, low;
		int len = s.length() / 2;
		byte[] b = new byte[len];
		for (int i = 0, k = 0; i < len; i++, k += 2) {
			high = (byte) (Character.digit(s.charAt(k), 16) & 0x0F);
			low = (byte) (Character.digit(s.charAt(k + 1), 16) & 0x0F);
			b[i] = (byte) ((high << 4) | low);
		}
		return b;
	} 
	
	protected String getMD5(byte[] source) {
		String s = null;
		char hexDigits[] = {
		'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
				'e', 'f' };
		try {
			java.security.MessageDigest md = java.security.MessageDigest
					.getInstance("MD5");
			md.update(source);
			byte tmp[] = md.digest();

			char str[] = new char[16 * 2];

			int k = 0;
			for (int i = 0; i < 16; i++) {

				byte byte0 = tmp[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			s = new String(str);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}
}