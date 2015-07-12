package com.fang.common.net;

/**
 * 网络请求返回结果
 * @author fang
 *
 */
public class NetRequestResult {

	/** 请求码 */
	private int requestCode = -1;
	/** 返回码 */
	private int resultCode = 0;
	/** 返回值 */
	private String value = null;
	
	public NetRequestResult(int requestCode, int resultCode, String value) {
		this.requestCode = requestCode;
		this.resultCode = resultCode;
		this.value = value;
	}
	
	public NetRequestResult(int requestCode, int resultCode) {
		this.requestCode = requestCode;
		this.resultCode = resultCode;
	}
	
	public NetRequestResult() {
	}

	public int getRequestCode() {
		return requestCode;
	}
	public void setRequestCode(int requestCode) {
		this.requestCode = requestCode;
	}
	public int getResultCode() {
		return resultCode;
	}
	public void setResultCode(int resultCode) {
		this.resultCode = resultCode;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}

    @Override
    public String toString() {
        return "requestCode:" + requestCode + ";resultCode:" + resultCode + ";value:" + value ;
    }
}
