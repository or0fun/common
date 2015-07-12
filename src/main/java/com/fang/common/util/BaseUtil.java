package com.fang.common.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.provider.ContactsContract;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.fang.common.base.Global;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class BaseUtil {

    private static final String TAG = "Util";

	/**
	 * 复制
	 * 
	 * @param context
     * @param content
	 **/

	@SuppressLint("NewApi")
	public static void copy(Context context, String content) {
		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		if (currentapiVersion >= android.os.Build.VERSION_CODES.HONEYCOMB) {
			android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context
					.getSystemService(Context.CLIPBOARD_SERVICE);
			ClipData clip = ClipData.newPlainText("label", content);
			clipboard.setPrimaryClip(clip);
		} else {
			android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context
					.getSystemService(Context.CLIPBOARD_SERVICE);
			clipboard.setText(content);
		}
        Toast.makeText(context, " 已复制 " + content,Toast.LENGTH_SHORT).show();
	}

	/**
	 * 获取粘贴板数据
	 * 
	 * @param context
	 * @return
	 */
	public static String paste(Context context) {
		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		if (currentapiVersion >= android.os.Build.VERSION_CODES.HONEYCOMB) {
			android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context
					.getSystemService(Context.CLIPBOARD_SERVICE);
			CharSequence data = clipboard.getText();
			if (null != data) {
				return data.toString();
			}
			return "";
		} else {
			android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context
					.getSystemService(Context.CLIPBOARD_SERVICE);
			CharSequence data = clipboard.getText();
			if (null != data) {
				return data.toString();
			}
			return "";
		}
	}

	/**
	 * 拨打电话
	 * 
	 * @param number
	 */
	public static void gotoCall(Context context, String number) {
		Uri uri = Uri.parse("tel:" + number);
		Intent intent = new Intent(Intent.ACTION_CALL, uri);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}

	/**
	 * 启动新的Activity
	 * 
	 * @param context
	 * @param intent
	 */
	public static void startActivityNewTask(Context context, Intent intent) {
		if (null != intent) {
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
					| Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);
		}
	}

	/**
	 * 启动新的Activity
	 * 
	 * @param context
	 * @param name
	 */
	public static void startActivity(Context context, Class<?> name) {
		Intent intent = new Intent(context, name);
		context.startActivity(intent);
	}

	/**
	 * 发送邮件
	 * 
	 * @param context
	 * @param email
	 */
	public static void email(Context context, String email) {
		Uri uri = Uri.parse("mailto:" + email);
		Intent intent = new Intent(Intent.ACTION_SENDTO, uri);

		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}

	/**
	 * 打开链接
	 * 
	 * @param context
	 * @param url
	 */
	public static void openURL(Context context, String url) {
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_VIEW);
		Uri content_url = Uri.parse(url);
		intent.setData(content_url);

		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}

	/**
	 * 添加fragment
	 * 
	 * @param activity
	 * @param fragment
	 * @param viewLayout
	 */
	public static void addFragment(Activity activity, Fragment fragment,
			int viewLayout) {
		FragmentManager fragmentManager = activity.getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		fragmentTransaction.add(viewLayout, fragment);
		fragmentTransaction.commit();

	}

	/**
	 * 添加悬浮窗
	 * 
	 * @param windowManager
	 * @param view
	 */
	public static void addView(WindowManager windowManager, View view) {
		WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
		layoutParams.type = LayoutParams.TYPE_SYSTEM_ALERT;
		layoutParams.gravity = Gravity.CENTER;
		layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
		layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
		layoutParams.flags |= LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
				| WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
		try {
            windowManager.addView(view, layoutParams);
        } catch (Exception e) {
            DebugLog.e(TAG, e.toString());
        }
	}

    /**
     * 移除悬浮窗
     * @param windowManager
     * @param view
     */
    public static void removeView(WindowManager windowManager, View view) {
        try {
            windowManager.removeView(view);
        } catch (Exception e) {
            DebugLog.e(TAG, e.toString());
        }
    }

	/**
	 * 安装应用
	 */
	public static void installAPK(Context context, Uri uri) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(uri, "application/vnd.android.package-archive");
		startActivityNewTask(context, intent);
	}

	/**
	 * 时间戳 long to string
	 * 
	 * @param d
	 */
	public static String longDateToStringDate(long d) {
		String time = "";
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(d);

        Calendar now = Calendar.getInstance();
		if (calendar.get(Calendar.YEAR) == now.get(Calendar.YEAR)) {
			if (calendar.get(Calendar.MONTH) == now.get(Calendar.MONTH)) {
				if (calendar.get(Calendar.DAY_OF_MONTH) == now.get(Calendar.DAY_OF_MONTH)) {
					SimpleDateFormat sfd = new SimpleDateFormat("HH:mm",
							Locale.US);
					time = "今天" + sfd.format(calendar.getTime());
				} else if (calendar.get(Calendar.DAY_OF_MONTH) == now.get(Calendar.DAY_OF_MONTH) - 1) {
					SimpleDateFormat sfd = new SimpleDateFormat("HH:mm",
							Locale.US);
					time = "昨天" + sfd.format(calendar.getTime());
				} else if (calendar.get(Calendar.DAY_OF_MONTH) == now.get(Calendar.DAY_OF_MONTH) - 2) {
					SimpleDateFormat sfd = new SimpleDateFormat("HH:mm",
							Locale.US);
					time = "前天" + sfd.format(calendar.getTime());
				} else {
					SimpleDateFormat sfd = new SimpleDateFormat("MM-dd HH:mm",
							Locale.US);
					time = sfd.format(calendar.getTime());
				}
			} else {
				SimpleDateFormat sfd = new SimpleDateFormat("MM-dd HH:mm",
						Locale.US);
				time = sfd.format(calendar.getTime());
			}
		} else {
			SimpleDateFormat sfd = new SimpleDateFormat("yyyy-MM-dd HH:mm",
					Locale.US);
			time = sfd.format(calendar.getTime());
		}
		return time;
	}

	/**
	 * 秒数转成字符串形式的时间
	 * 
	 * @param d
	 * @return
	 */
	public static String secondsToString(long d) {
        StringBuilder ptime = new StringBuilder();
		if (d >= 3600) {
			ptime.append(d / 3600);
			ptime.append("小时");
			d = d % 3600;
		}
		if (d >= 60) {
			ptime.append(d / 60);
			ptime.append("分");
			d = d % 60;
		}
		if (d > 0) {
			ptime.append(d);
			ptime.append("秒");
		}
		return ptime.toString();
	}

	/**
	 * 注册闹钟
	 * 
	 * @param context
	 * @param intent
	 * @param requestCode
	 * @param l
	 */
	public static void registerAlarm(Context context, Intent intent,
			int requestCode, long l) {
		AlarmManager am = (AlarmManager) context
				.getSystemService(Activity.ALARM_SERVICE);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
				requestCode, intent, 0);
		am.set(AlarmManager.RTC_WAKEUP, l, pendingIntent);
		DebugLog.d(TAG, "register" + requestCode + "  " + l / 1000);
	}

	/**
	 * 获取短信URI
	 * 
	 * @return
	 */
	public static Uri getSmsUriALL() {
		return Uri.parse("content://sms/");
	}

	/**
	 * 获取通讯录URI
	 * 
	 * @return
	 */
	public static Uri getContactUriALL() {
		return ContactsContract.Contacts.CONTENT_URI;
	}

	/**
	 * 判断耳机是否插上
	 * 
	 * @return
	 */
	public static boolean isWiredHeadsetOn(Context context) {
		AudioManager localAudioManager = (AudioManager) context
				.getSystemService(Context.AUDIO_SERVICE);
		return localAudioManager.isWiredHeadsetOn();
	}

	private static int getSystemVersion() {
		return android.os.Build.VERSION.SDK_INT;
	}

    /**
     * 记录crash日志
     * @param throwable
     */
    public static void addCrashException(Throwable throwable) {
        String info = null;
        ByteArrayOutputStream baos = null;
        PrintStream printStream = null;
        try {
            baos = new ByteArrayOutputStream();
            printStream = new PrintStream(baos);
            throwable.printStackTrace(printStream);
            byte[] data = baos.toByteArray();
            info = new String(data);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (printStream != null) {
                    printStream.close();
                }
                if (baos != null) {
                    baos.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sDateFormat.format(new java.util.Date());
        String str = SharedPreferencesHelper.getInstance().getString(SharedPreferencesHelper.CRASH_EXCEPTION, "");
        str += date + ":" + info + "|";
        SharedPreferencesHelper.getInstance().setString(SharedPreferencesHelper.CRASH_EXCEPTION, str);
    }

    /** 增加线程 */
    public static void excute(Runnable runnable) {
        Global.fixedThreadPool.execute(runnable);
    }

    /**
     * 强制显示或者关闭系统键盘
     */
    public static void showKeyBoard(final EditText txtSearchKey,final boolean visiable) {
        InputMethodManager m = (InputMethodManager)
                txtSearchKey.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (visiable) {
            m.showSoftInput(txtSearchKey, InputMethodManager.SHOW_FORCED);
        } else {
            m.hideSoftInputFromWindow(txtSearchKey.getWindowToken(), 0);
        }
    }
}
