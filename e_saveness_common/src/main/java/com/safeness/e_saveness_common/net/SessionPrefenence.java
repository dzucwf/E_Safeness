package com.safeness.e_saveness_common.net;


import android.content.Context;
import android.content.SharedPreferences;
/**
 * ��SharedPreference����ϵͳ���й����е�һЩ����
 * @author laixuezhu
 *
 */
public class SessionPrefenence {
	public static final String PREF_NAME = "session";
	public static final String KEY_LOGIN_NAME = "name";
	public static final String KEY_LOGIN_PASSWD = "passwd";
	public static final String KEY_COOKIE = "cookie";
	private static SessionPrefenence mSessionPreference;
	private SharedPreferences mPerferences;
	private Context mContext;
	private SessionPrefenence(Context context) {
		mContext = context;
		mPerferences = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
	}
	/**
	 * �õ����������
	 * @param context
	 * @return
	 */
	public static SessionPrefenence getInstance(Context context) {
		if(mSessionPreference == null) {
			mSessionPreference = new SessionPrefenence(context);
		}
		return mSessionPreference;
	}
	/**
	 * �����û���¼��Ϣ
	 * @param name
	 * @param passwd
	 */
	public void saveUserSession(String name, String passwd, String cookie) {
		SharedPreferences.Editor editor = mPerferences.edit(); 
		editor.putString(KEY_LOGIN_NAME, name);
		editor.putString(KEY_LOGIN_PASSWD, passwd);
		editor.putString(KEY_COOKIE, cookie);
		editor.commit();
	}
	public String getUserName() {
		return mPerferences.getString(KEY_LOGIN_NAME, null);
	}
	public String getUserPasswd() {
		return mPerferences.getString(KEY_LOGIN_PASSWD, null);
	}
	public String getCookie() {
		return mPerferences.getString(KEY_COOKIE, null);
	}
}
