package com.fang.common.net;

/**
 * 网络请求帮助类
 * @author fang
 *
 */
public class NetResuestCode {

	public static final String KEY = "key";
	public static final String DATA = "data";
	/** 日志类型 */
	public static final String CODE = "code";
	/** 获取用户ID 的参数 */
	public static final String USER_ID = "user_id";
	/** 携带用户ID 的参数 */
	public static final String VERSION = "version";
	/** 版本 的参数 */
	public static final String CHANNEL = "channel";
	/** 渠道 的参数 */
	public static final String USER = "user";
	/** 手机型号 */
	public static final String MODEL = "model";
	/** 反馈意见 */
	public static final String FEEDBACK = "feedback";
	/** 吐槽 */
	public static final String COMMENT = "comment";
	/** 获取吐槽 */
	public static final String GETCOMMENTS = "getcomments";
    /** 消息推送 */
    public static final String PUSH = "push";
    /** CRASH日志类型 */
    public static final String CRASH = "crash";
    /** 离线数据 */
    public static final String OFFLINE_NUMBER = "offline_number";
    /** url */
    public static final String url = "url";
	
	private static int code = 0;
	public static synchronized int getRequestCode() {
		code++;
		if (code > 10000) {
			code = 1;
		}
		return code;
	}
}
