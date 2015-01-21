package com.safeness.e_saveness_common.net;

import android.content.Context;

import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class HttpUtil {
	
	protected InputStream getRequestResult(HashMap<String,String> paramMap,String reqUrl,Context context) throws IOException {
		StringBuilder sb = new StringBuilder();
		if(paramMap!=null && !paramMap.isEmpty()){
			for(Map.Entry<String, String> entry : paramMap.entrySet()){
				sb.append(entry.getKey()).append('=')
				  .append(URLEncoder.encode(entry.getValue(), HTTP.UTF_8)).append('&');
			}
			sb.deleteCharAt(sb.length()-1);
		}

		byte[] entitydata = sb.toString().getBytes();
		URL url = new URL(reqUrl);
		HttpURLConnection mHttpURLConn = (HttpURLConnection)url.openConnection();
		mHttpURLConn.setRequestMethod("POST");
		mHttpURLConn.setConnectTimeout(10 * 1000);

		mHttpURLConn.setDoOutput(true);
		mHttpURLConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		mHttpURLConn.setRequestProperty("Content-Length", String.valueOf(entitydata.length));
		mHttpURLConn.setRequestProperty("Cookie", SessionPrefenence.getInstance(context).getCookie());
		
		OutputStream outStream = mHttpURLConn.getOutputStream();
		outStream.write(entitydata);
		outStream.flush();
		outStream.close();
		 
		
		if(mHttpURLConn.getResponseCode()== HttpURLConnection.HTTP_OK){
			return mHttpURLConn.getInputStream();
		}
		return null;
	}
	
	
	protected byte[] readInputStream(InputStream inStream) throws IOException {
		ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outSteam.write(buffer, 0, len);
		}
		outSteam.close();
		return outSteam.toByteArray();
	}
	
	public String submitReq(String reqUrl,HashMap<String,String> paramMap,Context context){
		String res = "";
		try{
			res  = new String(readInputStream(this.getRequestResult(paramMap, reqUrl, context)),"utf-8");
		}catch(Exception e){
			e.printStackTrace();
		}
		return res;
	}
	
	public Object parse(String str) throws JSONException {
		// TODO Auto-generated method stub
		try {
			JSONObject jsonObj = new JSONObject(str);
			
			if(jsonObj.has("result")) {
				return jsonObj.getJSONArray("result");
			} else {
				return jsonObj;
			}
		} catch (JSONException e) {
			
			JSONArray json = new JSONArray(str);
			return json;
		}
	}
	
	
}
