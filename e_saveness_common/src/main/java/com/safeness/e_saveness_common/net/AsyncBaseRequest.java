package com.safeness.e_saveness_common.net;

import android.content.Context;
import android.util.Log;

import com.safeness.e_saveness_common.util.Constant;

import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.util.Map;



/**
 * 功能描述：通过HTTP协议发送网络请求线程类
 * @author laixuezhu
 *
 */
public abstract class AsyncBaseRequest implements Runnable, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 网络连接超时，默认值为5秒
	 */
	protected int connectTimeout = 50 * 1000;
	
	/**
	 * 网络数据读取超时，默认值为5秒
	 */
	protected int readTimeout = 50 * 1000;
	/**
	 * 线程中断标识
	 */
	private boolean interrupted;  

	public boolean isInterrupted() {
		return interrupted;
	}

	public void setInterrupted(boolean interrupted) {
		this.interrupted = interrupted;
	}

	protected void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

	protected void setReadTimeout(int readTimeout) {
		this.readTimeout = readTimeout;
	}

	protected String requestUrl;
	protected Map<String, String> parameter;
	private ParseCallback parseHandler;
	private ResultCallback requestCallback;

	protected HttpURLConnection mHttpURLConn;
	protected InputStream mInStream;
	protected int reqCode;
	protected Context mContext;

    public AsyncBaseRequest(String url, Map<String, String> parameter,
            ParseCallback handler, ResultCallback requestCallback, int reqCode, Context context) {
		this.parseHandler = handler;
		this.requestUrl = url;
		this.parameter = parameter;
		this.requestCallback = requestCallback;
		this.reqCode = reqCode;
		this.mContext = context;
	}

	/**
	 * 发送网络请求
	 * 
	 * @return 网络请求返回的InputStream数据流
	 * @throws java.io.IOException
	 */
	protected abstract InputStream getRequestResult() throws IOException;

	@Override
	public void run() {
		try {
			if (interrupted) {
				Log.e("network", "访问网络前中断业务处理线程(终止)");
				return;
			} 
			
			mInStream = getRequestResult();
			if (mInStream != null) {
				if(Constant.DEBUG) {
					Log.i("network", "网络请求成功，下面进行数据解析...");
				}
				if (interrupted) {
					Log.e("network", "解析数据前中断业务处理线程(终止)");
					return;
				} 
				//没有解析器时，直接返回输入流
				if (parseHandler == null) {
					requestCallback.onSuccess(mInStream, reqCode);
				}
				
				String result = new String(readInputStream(mInStream));
				
				//服务器返回空串
				if("".equals(result)||" ".equals(result)) {
					requestCallback.onFail(Constant.NETWORK_REQUEST_RETUN_NULL, reqCode); // 网络请求返回NULL  
					return;
				}
				
				Object obj = parseHandler.parse(result);
				
				if (interrupted) {
					Log.e("network", "刷新UI前中断业务处理线程(终止)");
					return;
				} 
				if(Constant.DEBUG) {
					Log.i("network", "数据解析成功---" + obj);
				}
				requestCallback.onSuccess(obj, reqCode);
			} else {
				requestCallback.onFail(Constant.NETWORK_REQUEST_RETUN_NULL, reqCode); // 网络请求返回NULL  
			}
		} catch (IOException e) {
			requestCallback.onFail(Constant.NETWORK_REQUEST_IOEXCEPTION_CODE, reqCode); // IO异常标识
			e.printStackTrace();
		} catch (JSONException e) {  
            requestCallback.onFail(Constant.NETWORK_REQUEST_RESULT_PARSE_ERROR, reqCode); // 网络请求返回结果解析出错  
            e.printStackTrace();  
      }  
	}

	/**
	 * 从输入流读取数据
	 * 
	 * @param inStream
	 * @return
	 * @throws java.io.IOException
	 * @throws Exception
	 */
	protected byte[] readInputStream(InputStream inStream) throws IOException {
		ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outSteam.write(buffer, 0, len);
		}
		outSteam.close();
//		inStream.close();
		return outSteam.toByteArray();
	}

	public HttpURLConnection getRequestConn() {
		return mHttpURLConn;
	}

}

