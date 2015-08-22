package com.fang.common.util;

import android.content.Context;

import com.fang.common.net.NetRequestListener;
import com.fang.common.net.NetRequestResult;
import com.fang.common.net.NetRequestResultCode;
import com.umeng.analytics.MobclickAgent;


public class LogOperate {

    /** 日志类型 */
    public static final String CODE = "code";
    /** CRASH日志类型 */
    public static final String CRASH = "crash";
    /** request error */
    public static final String REQUEST_ERROR = "request_error";
    public static final String EXCEPTION_ERROR = "exception_error";

	/**
	 * 上传日志
	 * @param code
	 */
	static public void updateLog(final Context context, final String code) {
		ServerUtil.getInstance(context).request(CODE, code, null);
        MobclickAgent.onEvent(context, code);
	}

    /**
     * 上次crash日志
     * @param context
     */
    public static void uploadCrashLog(final Context context) {
        String str = SharedPreferencesHelper.getInstance().getString(SharedPreferencesHelper.CRASH_EXCEPTION, "");
        if (StringUtil.isEmpty(str)) {
            return;
        }
        ServerUtil.getInstance(context).request(CRASH, str, new NetRequestListener() {
            @Override
            public void onResult(NetRequestResult result) {
                if (null != result && NetRequestResultCode.HTTP_OK == result.getResultCode()) {
                    SharedPreferencesHelper.getInstance().setString(SharedPreferencesHelper.CRASH_EXCEPTION, "");
                }
            }
        });
    }

    /**
     * 上次request error日志
     * @param str
     */
    public static void uploadRequestError(final String str) {
        ServerUtil.getInstance().request(REQUEST_ERROR, str, null);
    }

    /**
     * 上次exception error日志
     * @param str
     */
    public static void uploadExceptionError(final String str) {
        ServerUtil.getInstance().request(EXCEPTION_ERROR, str, null);
    }
}
