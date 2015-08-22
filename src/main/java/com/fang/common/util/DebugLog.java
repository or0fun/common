package com.fang.common.util;

import android.util.Log;

import com.fang.common.base.Global;

import java.io.PrintWriter;
import java.io.StringWriter;


/**
 * 日志统一
 * @author fang
 *
 */
public class DebugLog {
	/** log 开头 */
	private static final String LOG_HEAD = "FANG_";

	public static void d(String tag, String msg) {
		if (Global.debug) {
			Log.d(LOG_HEAD + tag, msg);
		}
	}

	public static void e(String tag, String msg) {
		if (Global.debug) {
			Log.e(LOG_HEAD + tag, msg);
		} else {
            LogOperate.uploadExceptionError(tag + ":" + msg);
        }
	}

	public static void i(String tag, String msg) {
		if (Global.debug) {
			Log.i(LOG_HEAD + tag, msg);
		}
	}

	public static void v(String tag, String msg) {
		if (Global.debug) {
			Log.v(LOG_HEAD + tag, msg);
		}
	}

    public static void e(String tag, Throwable throwable) {
        if (Global.debug) {
            Log.e(LOG_HEAD + tag, getStackTrace(throwable));
        } else {
            LogOperate.uploadExceptionError(tag + ":" + getStackTrace(throwable));
        }
    }

    public static String getStackTrace(Throwable e) {
        try {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            return "\r\n" + sw.toString() + "\r\n";
        } catch (Exception e2) {
            return "bad getErrorInfoFromException";
        }
    }
}
