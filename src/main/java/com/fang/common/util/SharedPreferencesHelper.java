package com.fang.common.util;

import android.content.Context;

public class SharedPreferencesHelper extends AbsSharedPre {

    private final static String PREFS_FILE_NAME = "com.fang.callsms.common";

	/** 短信最后时间*/
	public static final String SENT_SMS_LAST_TIME = "SENT_SMS_LAST_TIME";

	/** 设置项 */
	public static final String SETTING_SMS_POPUP = "SETTING_SMS_POPUP";
	public static final String SETTING_NEW_CALL_POPUP = "SETTING_NEW_CALL_POPUP";
	public static final String SETTING_MISSED_CALL_POPUP = "SETTING_MISSED_CALL_POPUP";
	public static final String SETTING_OUTGOING_CALL_POPUP = "SETTING_OUTGOING_CALL_POPUP";
	public static final String SETTING_BROADCAST_WHEN_WIREDHEADSETON = "SETTING_BROADCAST_WHEN_WIREDHEADSETON";
	public static final String SETTING_EXPRESS_TRACK = "SETTING_EXPRESS_TRACK";
	public static final String SETTING_WEATHER_NOTIFICATION = "SETTING_WEATHER_NOTIFICATION";

    public static final String CRASH_EXCEPTION = "CRASH_EXCEPTION";

    /** 用户ID */
    public static final String USER_ID = "USER_ID";
    /** 未上传成功的数据 */
    public static final String OFFLINE_DATA = "OfflineData";

    @Override
    public void init(Context context) {
        mSharedPreferences = context.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE);
    }
}
