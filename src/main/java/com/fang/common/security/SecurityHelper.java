package com.fang.common.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 加密解密处理的帮助类
 * @author fang
 *
 */
public class SecurityHelper {

	/**
	 * Byte To Hex
	 * @param value
	 * @param minlength
	 * @return
	 */
	public static String byteToHex(byte value, int minlength) { 
	    String s = Integer.toHexString(value & 0xff); 
	    if (s.length() < minlength) { 
	        for (int i = 0; i < (minlength - s.length()); i++) 
	            s = "0" + s; 
	    } 
	    return s; 
	} 
	  
	/**
	 * 计算MD5
	 * @param value
	 * @return
	 */
	public static String getMD5(String value) { 
	    try { 
	        MessageDigest md = MessageDigest.getInstance("MD5"); 
	        md.update(value.getBytes());     
	        byte[] buf = md.digest();
	        String tmp = "";
            for (byte b : buf) {
                tmp = tmp + byteToHex(b, 2);
            }
	        return tmp.toLowerCase();
	    } catch (NoSuchAlgorithmException e) { 
	        e.printStackTrace(); 
	    } 
	    return ""; 
	}
}
