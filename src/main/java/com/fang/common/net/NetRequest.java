package com.fang.common.net;

import org.apache.http.NameValuePair;

public class NetRequest {
	/** 请求码 */
	private int requestCode = -1;
	/** 请求类型 */
	private int requestType = -1;
	/** 请求数据 */
	private NameValuePair value;
	/** 请求地址 */
	private String url;
	
	public int getRequestCode() {
		return requestCode;
	}
	public void setRequestCode(int requestCode) {
		this.requestCode = requestCode;
	}
	public NameValuePair getValue() {
		return value;
	}
	public void setValue(NameValuePair value) {
		this.value = value;
	}
	public int getRequestType() {
		return requestType;
	}
	public void setRequestType(int requestType) {
		this.requestType = requestType;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}

    @Override
    public String toString() {
        if (null != value) {
            return url + ";" + value.getName() + ":" + value.getName();
        } else {
            return url + ";value is null";
        }
    }
}
