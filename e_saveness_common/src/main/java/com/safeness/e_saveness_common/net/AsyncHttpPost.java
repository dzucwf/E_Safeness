package com.safeness.e_saveness_common.net;

import android.content.Context;
import android.util.Log;

import org.apache.http.protocol.HTTP;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

public class AsyncHttpPost extends AsyncBaseRequest {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3L;
	
	private static final String TAG  = "AsyncHttpPost";

	public AsyncHttpPost(String url, Map<String, String> parameter,
			ParseCallback handler, ResultCallback requestCallback, int reqCode, Context context) {
		super(url, parameter, handler, requestCallback, reqCode, context);
	}

	@Override
	protected InputStream getRequestResult() throws IOException {
		try{
			StringBuilder sb = new StringBuilder();
			if(parameter!=null && !parameter.isEmpty()){
				for(Map.Entry<String, String> entry : parameter.entrySet()){
					sb.append(entry.getKey()).append('=')
						.append(URLEncoder.encode(entry.getValue(), HTTP.UTF_8)).append('&');
				}
				sb.deleteCharAt(sb.length()-1);
			}
			String string=sb.toString();
			Log.d(TAG, requestUrl+string);
			// �õ�ʵ��Ķ���������
			byte[] entitydata = sb.toString().getBytes();
			URL url = new URL(requestUrl);
		    mHttpURLConn = (HttpURLConnection)url.openConnection();
			mHttpURLConn.setRequestMethod("POST");
			mHttpURLConn.setConnectTimeout(5 * 1000);
			// ���ͨ��post�ύ���ݣ�����������������������
			mHttpURLConn.setDoOutput(true);
			mHttpURLConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			mHttpURLConn.setRequestProperty("Content-Length", String.valueOf(entitydata.length));
			mHttpURLConn.setRequestProperty("Cookie", SessionPrefenence.getInstance(mContext).getCookie());
			
			OutputStream outStream = mHttpURLConn.getOutputStream();
			outStream.write(entitydata);
			outStream.flush();
			outStream.close();
			 
			
			if(mHttpURLConn.getResponseCode()== HttpURLConnection.HTTP_OK){
				return mHttpURLConn.getInputStream();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

}
