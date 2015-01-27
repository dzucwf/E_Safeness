package com.safeness.e_saveness_common.util;


/**
 * @author EISAVISA
 *
 */
public class Constant {
	/**
	 * debug状态标志，用于控制是否输出开发log
	 */
	public static final boolean DEBUG = true;
	/**
	 * 数据返回成功
	 */
	public static final int SUCCESS = 200;
	/**
	 * IO异常代码
	 */
	public static final int NETWORK_REQUEST_IOEXCEPTION_CODE = 0;
	/**
	 * 网络未返回数据
	 */
	public static final int NETWORK_REQUEST_RETUN_NULL = 1;
	/**
	 * 解析网络数据出错
	 */
	public static final int NETWORK_REQUEST_RESULT_PARSE_ERROR = 2;
	/**
	 * 选择的json解析器不合适，未返回正确数据
	 */
	public static final int JSON_HANDLER_ERROR = 3;
	
	private static final String PORT = "5020";
	//河北服务器端口
	//private static final String PORT = "5060";
	private static final String  SUFFIX = "/rcsServer/";
	
	

	//临时测试服务器
	public static  String SERVER = "10.18.11.199";

	
	

	public static void setServier(String server){
		SERVER = server;
		PRE_URL = "http://"+SERVER+":"+PORT+SUFFIX;
	}
	
	//public static final String SERVER = "192.168.0.103";
	/**
	 * 网络请求前缀
	 */

	public static  String PRE_URL = "http://"+SERVER+":"+PORT+SUFFIX;

	//
	
	
	
	
	/**
	 * 用于铃声问题
	 */
	public static  boolean isMIUI;
	

	public static final int  SIP_PORT = 5060;

	public static final String INVITE_STRING="快来使用RCS吧，网址是"+PRE_URL+"RCS.apk";
	public static final String REMIND_STRING="刚打你融信不在线，上来后请回话";
	/**
	 * 单条短信最大长度
	 */
	public static final int MAX_SINGLE_SMS_LENGTH = 70;
	/**
	 * 微信开放平台id
	 */
	public static final String APP_ID = "wxb6d99fe683b4d69e";
	
	//服务器处理成功失败标志
	public static final String KEY_SUCCESS = "success";
}
