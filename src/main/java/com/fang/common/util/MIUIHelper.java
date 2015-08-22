package com.fang.common.util;

import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;

import com.fang.common.util.DebugLog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * MIUI 权限管理帮助类
 * 
 * @author fang
 * 
 */
public class MIUIHelper {

	private final String TAG = "MiUIHelper";

	/** 权限设置页面 */
	private final String ACTION_APP_PERM_EDITOR = "miui.intent.action.APP_PERM_EDITOR";
	
	/** 安全中心包名 */
	private final String SECURITYCENTER = "com.miui.securitycenter";

	private final String UNKNOWN = "UNKNOWN";

	private boolean mIsMIUI_V5;

	private boolean mIsMIUI_V6;

	private final String FLAG_FLOATING_WINDOW = "FLAG_SHOW_FLOATING_WINDOW";

	private final String FLAG_AUTO_REBOOT = "FLAG_DISABLE_AUTOSTART";

	private int BIT_FLOATING_WINDOW; // 悬浮窗所在标志位

	private int BIT_DISABLE_AUTOSTART; //  取消自启动所在标志位
	
	private final int OP_SYSTEM_ALERT_WINDOW = 24;

	private static MIUIHelper mInstance;

	private MIUIHelper() {
		String systemProperty = getSystemProperty();
		mIsMIUI_V5 = isMiUIVX(systemProperty, "V5");
		mIsMIUI_V6 = isMiUIVX(systemProperty, "V6");

		if (mIsMIUI_V5) {
			DebugLog.d(TAG, "this is MIUI 5");
		} else if (mIsMIUI_V6) {
			DebugLog.d(TAG, "this is MIUI 6");
		}

		getApplicationInfoFileds();
	}

	public static MIUIHelper getInstance() {
		if (null == mInstance) {
			synchronized (MIUIHelper.class) {
				if (null == mInstance) {
					mInstance = new MIUIHelper();
				}
			}
		}
		return mInstance;
	}
	
	/**
	 * 判断是否是MIUI
	 * @return
	 */
	public boolean isMIUI() {
		if (UNKNOWN ==getSystemProperty()) {
			return false;
		}
		return true;
	}

	/**
	 * 判断是否小米手机
	 * 
	 * @return
	 */
	public boolean isXiaomiPhone() {
		boolean isXiaomi = false;
		String modelString = Build.MODEL.substring(0, 2);
		String manufactureString = Build.MANUFACTURER;
		String brandString = Build.BRAND;
		if (!TextUtils.isEmpty(manufactureString)
				&& manufactureString.equalsIgnoreCase("Xiaomi")) {
			isXiaomi = true;
		} else if (!TextUtils.isEmpty(modelString)
				&& modelString.equalsIgnoreCase("Mi")) {
			isXiaomi = true;
		} else if (!TextUtils.isEmpty(brandString)
				&& brandString.equalsIgnoreCase("xiaomi")) {
			isXiaomi = true;
		}
		return isXiaomi;
	}
	

	/**
	 * 判断是否是MIUI V5
	 * 
	 * @return
	 */
	public boolean isMiUIV5() {
		return mIsMIUI_V5;
	}

	/**
	 * 判断是否是MIUI V6
	 * 
	 * @return
	 */
	public boolean isMiUIV6() {
		return mIsMIUI_V6;
	}
	
	/**
	 * 判断是否需要显示权限设置引导页
	 * @param context
	 * @return
	 */
	public boolean isNeedShowSettingGuide(Context context) {
		if (mIsMIUI_V5 || mIsMIUI_V6 ) {
			if (isAutoRebootAllowed(context) && isFloatWindowOpAllowed(context)) {
				return false;
			}
			return true;
		}
		return false;
	}
	
	/**
	 * 打开设置悬浮窗的设置
	 * 
	 * @param context
	 */
	public void openFloatWindowSetting(Context context) {
		Intent intent = new Intent();
		if (mIsMIUI_V5) {
			intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
			Uri uri = Uri.parse("package:" + context.getPackageName());
			intent.setData(uri);
		} else if (mIsMIUI_V6) {
			intent.setAction(ACTION_APP_PERM_EDITOR);
			intent.setComponent(new ComponentName(SECURITYCENTER,
					"com.miui.permcenter.permissions.AppPermissionsEditorActivity"));
			intent.putExtra("extra_pkgname", context.getPackageName());
		}
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_SINGLE_TOP);
		startActivity(context, intent);
	}

	/**
	 * 跳转到设置自启动
	 * @param context
	 */
	public void openAutoRebootSetting(Context context) {
		Intent intent = new Intent();
		if (mIsMIUI_V5) {
			intent.setAction(ACTION_APP_PERM_EDITOR);
			try {
				intent.putExtra(
						"extra_package_uid",
						context.getPackageManager().getPackageInfo(
								context.getPackageName(), 0).applicationInfo.uid);
			} catch (NameNotFoundException e) {
				DebugLog.d(TAG, e.toString());
				// 部分V5版本此处跳转不过去，只能跳到本应用应用信息界面，即上方第一个跳转
				intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
				Uri uri = Uri.parse("package:" + context.getPackageName());
				intent.setData(uri);
			}
		} else if (mIsMIUI_V6) {
			intent.setComponent(new ComponentName("com.miui.securitycenter",
					"com.miui.securitycenter.MainActivity"));
//			intent.setComponent(new ComponentName(SECURITYCENTER,
//					"com.miui.permcenter.permissions.AppPermissionsEditorActivity"));
//			intent.setComponent(new ComponentName(SECURITYCENTER,
//					"com.miui.permcenter.autostart.AutoStartManagementActivity"));
		}
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_SINGLE_TOP);
		
		startActivity(context, intent);
	}

	/**
	 * 是否开启自启动
	 * @param context
	 * @return
	 */
	public boolean isAutoRebootAllowed(Context context) {
		if((context.getApplicationInfo().flags & BIT_DISABLE_AUTOSTART) == 0) {
			return true;
		}
		return false;
	}
	
	/**
	 * 判断MIUI的悬浮窗权限
	 * 
	 * @param context
	 * @return
	 */
	@TargetApi(Build.VERSION_CODES.KITKAT)
	public boolean isFloatWindowOpAllowed(Context context) {

		if (mIsMIUI_V5) {
			int flags = context.getApplicationInfo().flags;
			int flag = flags & BIT_FLOATING_WINDOW;
			boolean op = checkOp(context, OP_SYSTEM_ALERT_WINDOW);
			int flag2 = flags & 1 << 27;
			DebugLog.d(TAG, "flags:" + flags + ", flag:" + flag + ", flag2:"
					+ flag2 + ", op:" + op);
			if (flag > 0) {
				return true;
			} else {
				return false;
			}
		} else if (mIsMIUI_V6) {
			final int version = Build.VERSION.SDK_INT;
			if (version >= 19) {
				// 自己写就是24 为什么是24?看AppOpsManager：OP_SYSTEM_ALERT_WINDOW
				return checkOp(context, OP_SYSTEM_ALERT_WINDOW); 
			} else {
				if ((context.getApplicationInfo().flags & 1<<27) == 1) {
					return true;
				} else {
					return false;
				}
			}
		} else {
			return true;
		}
	}

	@TargetApi(Build.VERSION_CODES.KITKAT)
	private boolean checkOp(Context context, int op) {
		final int version = Build.VERSION.SDK_INT;

		if (version >= 19) {
			AppOpsManager appOpsManager = (AppOpsManager) context
					.getSystemService(Context.APP_OPS_SERVICE);

			try {
				Class<AppOpsManager> clazz = AppOpsManager.class;
				Method method = clazz.getMethod("checkOp", int.class,
						int.class, String.class);
				int value = (Integer) method.invoke(appOpsManager, op,
						Binder.getCallingUid(), context.getPackageName());

				if (AppOpsManager.MODE_ALLOWED == value) { // 这儿反射就自己写吧
					return true;
				} else {
					return false;
				}
			} catch (Exception e) {
				DebugLog.e(TAG, e.getMessage());
			}
		} else {
			DebugLog.e(TAG, "Below API 19 cannot invoke!");
		}
		return false;
	}

	/**
	 * 获取小米版本字符串
	 * 
	 * @return
	 */
	private String getSystemProperty() {
		String line = null;
		BufferedReader reader = null;
		try {
			Process p = Runtime.getRuntime().exec(
					"getprop ro.miui.ui.version.name");
			reader = new BufferedReader(new InputStreamReader(
					p.getInputStream()), 1024);
			line = reader.readLine();
			reader.close();
			return line;
		} catch (IOException e) {
			DebugLog.e(TAG, e.toString());
		}

		return UNKNOWN;
	}

	/**
	 * 判断是否是MIUI VX, X 可以是任意数字
	 * 
	 * @return
	 */
	private boolean isMiUIVX(final String systemProperty,
			final String versionName) {
		if (!TextUtils.isEmpty(systemProperty)) {
			if (systemProperty.equalsIgnoreCase(versionName)) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	/**
	 * 反射获得ApplicationInfo类所有成员
	 *
	 */
	private void getApplicationInfoFileds() {
		Class<ApplicationInfo> clazz = ApplicationInfo.class;
		Field[] field = clazz.getDeclaredFields();
		for (Field field2 : field) {
			// 允许访问私有成员
			field2.setAccessible(true);
			// 获取该属性字段的名称
			String fieldName = field2.getName();
			DebugLog.d(TAG, "fieldName:" + fieldName);
			if (fieldName.equalsIgnoreCase(FLAG_FLOATING_WINDOW)) {
				try {
					BIT_FLOATING_WINDOW = field2.getInt(ApplicationInfo.class);
				} catch (Exception e) {
					DebugLog.d(TAG, e.toString());
				}
				DebugLog.d(TAG, "Bit_Floating_Window:" + BIT_FLOATING_WINDOW);
			} else if (fieldName.equalsIgnoreCase(FLAG_AUTO_REBOOT)) {
				try {
					BIT_DISABLE_AUTOSTART = field2.getInt(ApplicationInfo.class);
				} catch (Exception e) {
					DebugLog.d(TAG, e.toString());
				}
				DebugLog.d(TAG, "Bit_Auto_Reboot:" + BIT_DISABLE_AUTOSTART);
			}
		}
	}
	
	/**
	 * 启动Activity
	 * @param context
	 * @param intent
	 */
	private void startActivity(Context context, Intent intent) {
		if (isIntentAvailable(context, intent)) {
			BaseUtil.startActivity(context, intent);
		}else {
			DebugLog.d(TAG, "intent is illegal");
		}
	}
	
	/**
     * 判断是否有可以接受的Activity
     * @param context
     * @param intent
     * @return
     */
    public static boolean isIntentAvailable(Context context, Intent intent) {
        if (intent == null) 
        	return false;
        return context.getPackageManager().queryIntentActivities(intent, PackageManager.GET_ACTIVITIES).size() > 0;
    }
}
