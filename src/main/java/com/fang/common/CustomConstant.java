
package com.fang.common;

import android.net.Uri;

/**
 * 定义常量
 * @author fang
 *
 */
public class CustomConstant {

    /** 收件箱Uri */
    public static final Uri SMS_INBOX_URI = Uri.parse("content://sms/inbox");

    /** 一小时 */
    public static final long ONE_HOUR = 1000 * 3600;

    /** 3小时 */
    public static final long THREE_HOUR = 1000 * 3600 * 3;

    /** 12小时 */
    public static final long HALF_DAY = ONE_HOUR * 12;

    /** 一天的毫秒 */
	public static final long ONE_DAY = HALF_DAY * 2;

	/** 一刻钟 */
	public static final long QUARTER_HOUR = 1000 * 60 * 15;
	
	/** 5s */
	public static final long FIVE_SECONDS = 5000;

    /** 最大线程数 */
    public static final int MAX_THREAD_COUNT = 10;

    public static final String UMENG_KEY = "55a7c3e567e58e3a34003ed5";

}

