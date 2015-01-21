package com.safeness.e_saveness_common.net;

import org.json.JSONException;

/**
 * 功能描述：网络请求返回的数据解析接口
 *
 */
public interface ParseCallback {
	 /**
     * 对网络请求返回的数据进行解析
     * @param str 要解析的字符串，JSON格式或XML
     * @return 解析的结果
     */
	public Object parse(String str) throws JSONException;
}
