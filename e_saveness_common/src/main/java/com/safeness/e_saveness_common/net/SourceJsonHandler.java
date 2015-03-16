package com.safeness.e_saveness_common.net;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



public class SourceJsonHandler implements ParseCallback {
	private static final String TAG = DefaultJsonHandler.class.getName();
	@Override
	public Object parse(String str) throws JSONException {
		// TODO Auto-generated method stub
		try {
			JSONObject jsonObj = new JSONObject(str);
			
			return jsonObj;
		} catch (JSONException e) {
			
			JSONArray json = new JSONArray(str);
			return json;
		}
	}

}
