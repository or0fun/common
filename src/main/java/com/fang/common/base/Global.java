package com.fang.common.base;

import android.app.Activity;
import android.app.Application;

import java.util.concurrent.ExecutorService;

/**
 * Created by benren.fj on 6/11/15.
 */
public class Global {
    public static Application application;
    public static Activity context;
    public static int fullScreenWidth;
    public static int fullScreeHeight;

    public static boolean debug;
    /** 应用渠道 */
    //wandoujia  hiapk  blog baidu  qq 360 appchina lenovo mi anzhi
    public static String channel;

    public static ExecutorService fixedThreadPool;

    //网络广播时间
    public static long netBroadcastTime = 0;
}
