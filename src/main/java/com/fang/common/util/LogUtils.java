package com.fang.common.util;

import android.text.TextUtils;
import android.util.Log;

public class LogUtils {

    public static String TAG = "baiwanlu";

    public static boolean LOG_DEBUG = false;

    public static int maxLength = 4000;

    private static final String SUFFIX = ".java";

    static ErrorListener sErrorListener;
    static LogListener sLogListener;

    public interface ErrorListener {
        void error(Throwable e);
        void error(String error);
    }

    public interface LogListener {
        void log(Throwable e);
        void log(String error);
    }

    public static void setErrorListener(ErrorListener errorListener) {
        sErrorListener = errorListener;
    }

    public static void setLogListener(LogListener logListener) {
        sLogListener = logListener;
    }


    private static String getClassAndMethod() {

        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

        if (null == stackTrace) {
            return "";
        }

        StackTraceElement destStackTraceElement = null;
        boolean next = true;
        for (StackTraceElement traceElement : stackTrace) {
            if (traceElement.getClassName().equals(LogUtils.class.getName())) {
                next = false;
            } else {
                if (next) {
                    continue;
                } else {
                    destStackTraceElement = traceElement;
                    break;
                }
            }
        }

        if (null == destStackTraceElement) {
            return "";
        }

        String className = destStackTraceElement.getClassName();
        String[] classNameInfo = className.split("\\.");
        if (classNameInfo.length > 0) {
            className = classNameInfo[classNameInfo.length - 1] + SUFFIX;
        }

        if (className.contains("$")) {
            className = className.split("\\$")[0] + SUFFIX;
        }

        String methodName = destStackTraceElement.getMethodName();
        int lineNumber = destStackTraceElement.getLineNumber();

        if (lineNumber < 0) {
            lineNumber = 0;
        }

        String methodNameShort = methodName.substring(0, 1).toUpperCase() + methodName.substring(1);


        String headString = "[(" + className + ":" + lineNumber + ")#" + methodNameShort + "]";

        return headString;
    }

    public static void d(Object... messages) {
        if (LOG_DEBUG) {
            StringBuilder stringBuilder = new StringBuilder();
            for (Object msg : messages) {
                if (null == msg) {
                    continue;
                }
                stringBuilder.append(msg.toString());
                stringBuilder.append(" ");
            }
            if (stringBuilder.length() > 0) {
                LogUtils.d(stringBuilder.toString());
            }
        }
    }

    public static void e(String... messages) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String msg : messages) {
            stringBuilder.append(msg);
            stringBuilder.append("\n");
        }

        if (LOG_DEBUG) {
            LogUtils.e(stringBuilder.toString());
        }
        if (null != sErrorListener) {
            sErrorListener.error(stringBuilder.toString());
        }
    }

    //调用原生log接口

    public static void d(String msg) {
        if (LOG_DEBUG) {
            if (TextUtils.isEmpty(msg)) {
                return;
            }

            String header = getClassAndMethod();

            Log.d(TAG, header + Thread.currentThread().toString() + ":");

            int index = 0;
            int countOfSub = msg.length() / maxLength;

            if (countOfSub > 0) {
                for (int i = 0; i < countOfSub; i++) {
                    String sub = msg.substring(index, index + maxLength);
                    Log.d(TAG,  sub);
                    index += maxLength;
                }
                Log.d(TAG, msg.substring(index, msg.length()));
            } else {
                Log.d(TAG, msg);
            }

            if (null != sLogListener) {
                sLogListener.log(Thread.currentThread().toString() + ":");
                sLogListener.log(header + msg);
            }
        }
    }

    public static void e(String msg) {

        String header = getClassAndMethod();

        if (LOG_DEBUG) {
            if (TextUtils.isEmpty(msg)) {
                return;
            }

            Log.e(TAG, header + Thread.currentThread().toString() + ":");

            int index = 0;
            int countOfSub = msg.length() / maxLength;

            if (countOfSub > 0) {
                for (int i = 0; i < countOfSub; i++) {
                    String sub = msg.substring(index, index + maxLength);
                    Log.e(TAG,  sub);
                    index += maxLength;
                }
                Log.e(TAG, msg.substring(index, msg.length()));
            } else {
                Log.e(TAG, msg);
            }

        }

        if (null != sErrorListener) {
            sErrorListener.error(header + msg);
        }
    }

    public static void e(Throwable throwable) {
        if (LOG_DEBUG) {
            String header = getClassAndMethod();
            Log.e(TAG, Thread.currentThread().toString() + header, throwable);
        }
        if (null != sErrorListener) {
            sErrorListener.error(throwable);
        }
    }
}
