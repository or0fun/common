package com.fang.common.net;

/**
 * 网络请求监听类
 * @author fang
 *
 */
public interface NetRequestListener {

	/**
	 * 服务端返回结果回调，包含成功或失败的结果
	 * @param result 解析后的业务结果，需根据不同业务结果类别进行类型转换
	 */
	void onResult(NetRequestResult result);
}
