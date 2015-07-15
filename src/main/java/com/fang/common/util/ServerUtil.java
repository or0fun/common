package com.fang.common.util;

import android.content.Context;
import android.util.Log;

import com.fang.common.base.Global;
import com.fang.common.net.CustomHttpClient;
import com.fang.common.net.NetRequest;
import com.fang.common.net.NetRequestConstant;
import com.fang.common.net.NetRequestListener;
import com.fang.common.net.NetRequestResult;
import com.fang.common.net.NetRequestResultCode;
import com.fang.common.net.NetResuestCode;
import com.fang.common.security.AESUtil;
import com.fang.common.security.SecurityHelper;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

public class ServerUtil implements Runnable {

	public final String TAG = ServerUtil.class.getSimpleName();

	private Context mContext = null;
	private Thread mThread;
	private static volatile ServerUtil mInstance = null;
	private AESUtil defaultAesutil;
	/** 用户标识 */
	private String mUserID;
	/** 未上传成功的数据 */
	private List<NameValuePair> mParamsBuffer = new ArrayList<NameValuePair>();

    private final Object mLock = new Object();

    private String mURL;

	private ServerUtil(Context cxt) {
		mContext = cxt;

		defaultAesutil = new AESUtil();
		mThread = new Thread(this);
		mUserID = SharedPreferencesHelper.getInstance().getString(
                SharedPreferencesHelper.USER_ID, null);
		mParamsBuffer = JsonUtil.getNameValuePairList(SharedPreferencesHelper.getInstance()
                .getString(SharedPreferencesHelper.OFFLINE_DATA));
		mThread.start();
	}

	public static ServerUtil getInstance(Context cxt) {
        ServerUtil instance = mInstance;
		if (instance == null) {
			synchronized (ServerUtil.class) {
                instance = mInstance;
				if (instance == null) {
					if (cxt != null) {
                        mInstance = instance = new ServerUtil(cxt);
					}
				}
			}
		}
		return instance;
	}

    public static ServerUtil getInstance() {
        return mInstance;
    }

	/**
	 * 发送请求
	 * 
	 * @param request
	 * @param listener
	 */
	public void request(final NetRequest request, final NetRequestListener listener) {
		if (null == request) {
			return;
		}
        BaseUtil.excute(new Runnable() {
            @Override
            public void run() {
                NetRequestResult result = new NetRequestResult();
                result.setRequestCode(request.getRequestCode());
                // 网络不可用
                if (!NetWorkUtil.isNetworkConnected(mContext)) {
                    result.setResultCode(NetRequestResultCode.NETWORK_NOT_AVAILABLE);
                    if (request.getRequestType() == NetRequestConstant.TYPE_POST) {
                        addOfflineData(request.getValue());
                    }
                }
                //正常的上传地址，不是获取用户ID的情况下，当用户ID不存在时先存进离线数据
                else if (mUserID == null && request.getUrl().equals(mURL)
                        && request.getValue() != null && !request.getValue().getName().equals(NetResuestCode.USER_ID)) {
                    result.setResultCode(NetRequestResultCode.NO_USER_ID);
                    if (request.getRequestType() == NetRequestConstant.TYPE_POST) {
                        addOfflineData(request.getValue());
                    }
                } else {
                    int type = request.getRequestType();
                    NetRequestResult r = null;
                    if (NetRequestConstant.TYPE_GET == type) {
                        r = getHttpRequest(request.getUrl());
                    } else if (NetRequestConstant.TYPE_POST == type) {
                        // 加密处理
                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        NameValuePair nl = request.getValue();
                        if (null != nl) {
                            params.add(new BasicNameValuePair(SecurityHelper
                                    .getMD5(nl.getName()), defaultAesutil
                                    .encrypt(nl.getValue())));
                            if (!nl.getName().equals(NetResuestCode.USER_ID)) {
                                params.add(new BasicNameValuePair(SecurityHelper
                                        .getMD5(NetResuestCode.USER), defaultAesutil
                                        .encrypt(mUserID)));
                            } else {
                                String mtype = android.os.Build.MODEL; // 手机型号
                                params.add(new BasicNameValuePair(SecurityHelper
                                        .getMD5(NetResuestCode.MODEL), defaultAesutil
                                        .encrypt(mtype)));
                            }
                            //加上版本和渠道
                            params.add(new BasicNameValuePair(SecurityHelper
                                    .getMD5(NetResuestCode.VERSION), defaultAesutil
                                    .encrypt(BaseUtil.getVersionName(mContext))));
                            params.add(new BasicNameValuePair(SecurityHelper
                                    .getMD5(NetResuestCode.CHANNEL), defaultAesutil
                                    .encrypt(Global.channel)));
                            r = postHttpRequest(request.getUrl(), params);
                        }
                    }
                    if (null != r) {
                        result.setResultCode(r.getResultCode());
                        // 解密
                        result.setValue(defaultAesutil.decrypt(r.getValue()));

                        //日志收集,请求失败而且不是REQUEST_ERROR请求
                        if (r.getResultCode() != NetRequestResultCode.HTTP_OK &&
                                request.getValue().getName().equals(LogOperate.REQUEST_ERROR)) {
                            LogOperate.uploadRequestError("request:" + request.toString()
                                    + ", response:" + r.toString());
                        }
                    } else {
                        LogOperate.uploadRequestError("request:" + request.toString()
                                + ", response: null");
                    }
                }
                if (null != listener) {
                    listener.onResult(result);
                }
            }
        });
	}

	/**
	 * 请求数据
	 * 
	 * @param key
	 * @param listener
	 */
	public void request(String key, String value, NetRequestListener listener) {
		if (null != key) {
			NetRequest request = new NetRequest();
			request.setUrl(mURL);
			request.setRequestType(NetRequestConstant.TYPE_POST);
			request.setRequestCode(NetResuestCode.getRequestCode());
			final NameValuePair nl = new BasicNameValuePair(key, value);
			request.setValue(nl);
			request(request, listener);
		}
	}
	/**
	 * 请求数据
	 * 
	 * @param key
	 * @param listener
	 */
	public void request(String key, NetRequestListener listener) {
		if (null != key) {
			NetRequest request = new NetRequest();
			request.setUrl(mURL);
			request.setRequestType(NetRequestConstant.TYPE_POST);
			request.setRequestCode(NetResuestCode.getRequestCode());
			final NameValuePair nl = new BasicNameValuePair(key, "");
			request.setValue(nl);
			request(request, listener);
		}
	}
	/**
	 * 请求数据
	 * 
	 * @param nameValuePair
	 * @param listener
	 */
	public void request(NameValuePair nameValuePair, NetRequestListener listener) {
		if (null != nameValuePair) {
			NetRequest request = new NetRequest();
			request.setUrl(mURL);
			request.setRequestType(NetRequestConstant.TYPE_POST);
			request.setRequestCode(NetResuestCode.getRequestCode());
			request.setValue(nameValuePair);
			request(request, listener);
		}
	}

	/**
	 * GET 请求实现
	 * 
	 * @param url
	 * @return
	 */
	private NetRequestResult getHttpRequest(String url) {
		return httpRequest(url, null);
	}

	/**
	 * POST 请求
	 * 
	 * @param url
	 * @param params
	 * @return
	 */
	private NetRequestResult postHttpRequest(String url,
			List<NameValuePair> params) {
		return httpRequest(url, params);
	}
	/**
	 * 网络请求
	 * @param url
	 * @param params
	 * @return
	 */
	private NetRequestResult httpRequest(String url,
			List<NameValuePair> params) {
		NetRequestResult result = new NetRequestResult();
		String strResult = "";
		try {
			HttpClient httpClient = CustomHttpClient.getHttpClient();
			HttpPost httpPost = new HttpPost(url);
			if (null != params) {
				httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			}
			HttpResponse httpResponse = httpClient.execute(httpPost);
			int status = httpResponse.getStatusLine().getStatusCode();
			result.setResultCode(status);
			if (status == HttpStatus.SC_OK) {
				strResult = getRealString(EntityUtils.toString(httpResponse
						.getEntity()));
				result.setResultCode(NetRequestResultCode.HTTP_OK);
			}
		} catch (ConnectionPoolTimeoutException e) {
			Log.e(TAG, e.toString());
			result.setResultCode(NetRequestResultCode.CONNECTION_POLL_TIMEOUT);
		} catch (ConnectTimeoutException e) {
			Log.e(TAG, e.toString());
			result.setResultCode(NetRequestResultCode.CONNECT_TIMEOUT);
		} catch (SocketTimeoutException e) {
			Log.e(TAG, e.toString());
			result.setResultCode(NetRequestResultCode.SOCKET_TIMEOUT);
		} catch (ClientProtocolException e) {
			Log.e(TAG, e.toString());
			result.setResultCode(NetRequestResultCode.CLIENT_PROTOCOL);
		} catch (IOException e) {
			Log.e(TAG, e.toString());
			result.setResultCode(NetRequestResultCode.IO_ERROR);
		}
		result.setValue(strResult);
		return result;
	}
	@Override
	public void run() {
		while (true) {
			try {
				Thread.currentThread();
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 去除无用的前缀
	 * 
	 * @param str
	 * @return
	 */
	private String getRealString(String str) {
		if (str != null) {
			int len = str.length();
			return str.substring(len % 16);
		}
		return "";
	}

	/**
	 * 获取用户ID
	 */
	private void getUSerID(final Context context) {
		ServerUtil server = ServerUtil.getInstance(context);
		server.request(NetResuestCode.USER_ID, new NetRequestListener() {
			@Override
			public void onResult(NetRequestResult result) {
				if (result.getResultCode() == NetRequestResultCode.HTTP_OK) {
					mUserID = result.getValue();
					SharedPreferencesHelper.getInstance().setString(
                            SharedPreferencesHelper.USER_ID, mUserID);
					checkOffLineData(context);
				}
			}
		});
	}

	/**
	 * 添加未成功上传的数据
	 * 
	 * @param param
	 */
	private void addOfflineData(NameValuePair param) {
		synchronized (mLock) {
			mParamsBuffer.add(param);
			SharedPreferencesHelper.getInstance().setString(
                    SharedPreferencesHelper.OFFLINE_DATA,
                    JsonUtil.getJsonString(mParamsBuffer));
		}
	}

	/**
	 * 获取用户ID
	 * 
	 * @return
	 */
	public void checkUserID(final Context context) {
		if (null == mUserID) {
			getUSerID(context);
		}
		;
	}

	/**
	 * 获取用户ID
	 * 
	 * @return
	 */
	public void checkOffLineData(final Context context) {
		if (null == mParamsBuffer) {
			synchronized (mLock) {
				if (mParamsBuffer.size() > 0) {
					if (NetWorkUtil.isNetworkConnected(context)) {
						request(mParamsBuffer.remove(0), null);
					}
				}
			}
		}
	}
	/**
	 * 获取用户ID
	 * @return
	 */
	public String getUserID() {
		return mUserID;
	}

    /**
     * 获取api地址
     * @return
     */
    public void setPostUrl(String url) {
        mURL = url;
    }

}
