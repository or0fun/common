package com.fang.common.net;

/**
 * 网络请求返回码
 * @author fang
 *
 */
public class NetRequestResultCode {

	public static final int HTTP_OK = 0;
	/** 没有权限 */
	public static final int NO_PERMISSION = -1;
	/** 网络不可用 */
	public static final int NETWORK_NOT_AVAILABLE = -2;
	/** 连接池超时 */
	public static final int CONNECTION_POLL_TIMEOUT = -3;
	/** 连接超时 */
	public static final int CONNECT_TIMEOUT = -4;
	/** 请求超时 */
	public static final int SOCKET_TIMEOUT = -5;
	/** 客户端协议 */
	public static final int CLIENT_PROTOCOL = -6;
	/** IO错误 */
	public static final int IO_ERROR = -7;
	/** 没有用户ID */
	public static final int NO_USER_ID = -8;
}
