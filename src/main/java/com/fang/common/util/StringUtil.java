package com.fang.common.util;

public class StringUtil {

	public static String toString(Object o) {
		if (o == null)
			return "";
		else
			return o.toString();
	}
	public static boolean isEmpty(String str) {
		if (null == str || str.length() == 0) {
			return true;
		}
		return false;
	}
}
