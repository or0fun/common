package com.fang.common.util;

/**
 * 匹配
 * @author fang
 *
 */
public class Patterns {

	public final static String MAIL_PATTERN = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
	public final static String PHONE_NUMBER_PATTERN = "^((\\d{3,4}-)?\\d{7,8})|(1[0-9]{10})$";
	public final static String NUMBER_PATTERN = "^([0-9]+)$";
	public final static String CODE_PATTERN = "^([0-9a-zA-Z]+)$";
	public final static String URL_PATTERN = "^[a-zA-z]+://[^\\s]*$";
}
