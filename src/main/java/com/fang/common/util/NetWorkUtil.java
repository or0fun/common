package com.fang.common.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

import com.fang.common.security.AESUtil;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class NetWorkUtil {
	private static final String TAG = "NetWorkUtil";
	private static NetWorkUtil mInstance = new NetWorkUtil();
	private AESUtil mAesUtil;

	private NetWorkUtil() {
		mAesUtil = new AESUtil();
	}

	public static NetWorkUtil getInstance() {
		return mInstance;
	}

	/**
	 * 网络是否连接
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isNetworkConnected(Context context) {
		if (context != null) {
			ConnectivityManager connectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo = connectivityManager
					.getActiveNetworkInfo();
			if (networkInfo != null) {
				return networkInfo.isConnected();
			}
		}
		return false;
	}

    /**
     * wifi连接
     * @param context
     * @return
     */
    public static boolean isWifiConnected(Context context) {
        if (context != null) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager
                    .getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                return networkInfo.getType() == ConnectivityManager.TYPE_WIFI;
            }
        }
        return false;
    }

	/**
	 * 移动网络是否连接
	 * 
	 * @param context
	 * @return
	 */
	public static boolean is3GAvailable(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
			DebugLog.d(TAG, "newwork is off");
			return false;
		} else {
			NetworkInfo info = connectivity.getActiveNetworkInfo();
			if (info == null) {
				DebugLog.d(TAG, "newwork is off");
				return false;
			} else {
				if (info.isAvailable()) {
					DebugLog.d(TAG, "newwork is on");
					return true;
				}
			}
		}
		DebugLog.d(TAG, "newwork is off");
		return false;
	}

	/**
	 * WIFI 是否可用
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isWiFiAnable(Context context) {
		WifiManager mWifiManager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		if (mWifiManager.isWifiEnabled()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * WIFI 是否开启
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isWiFiActive(Context context) {
		ConnectivityManager connManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mWifi = connManager
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

		if (mWifi.isConnected()) {
			DebugLog.d(TAG, "newwork is on");
			return true;
		}
		DebugLog.d(TAG, "newwork is off");
		return false;
	}

	/**
	 * 发起请求
	 * 
	 * @param url
	 * @return
	 */
	protected String getHttpRequest(String url) {
		String strResult = "";
		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(url);
			HttpResponse httpResponse = httpClient.execute(httpGet);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				strResult = EntityUtils.toString(httpResponse.getEntity(),
						"UTF-8");
			}
			DebugLog.d(TAG, strResult);

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return strResult;

	}
}
