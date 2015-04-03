package com.safeness.e_saveness_common.util;


/**
 * @author EISAVISA
 *
 * 一些服务器配置的常量，如IP等
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
	
	private static final String PORT = "8080";
	//河北服务器端口
	//private static final String PORT = "5060";
	private static final String  SUFFIX = "/cdm/webservice/";
	
	

	//临时测试服务器
	public static  String SERVER = "114.215.154.226";

	
	

	public static void setServier(String server){
		SERVER = server;
		PRE_URL = "http://"+SERVER+":"+PORT+SUFFIX;
}

    public static String getServier(){
        return PRE_URL;
    }

    public  static String getUpdateUrl(){
        //TODO:这个地址是临时的，需要修改端口号
        return  "http://"+SERVER+":8001/saveness";
    }
	
	//public static final String SERVER = "192.168.0.103";
	/**
	 * 网络请求前缀
	 */

	private static  String PRE_URL = "http://"+SERVER+":"+PORT+SUFFIX;

	//
	
	
	
	

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


    //聊天
    public static final String NEW_FRIENDS_USERNAME = "item_new_friends";
    public static final String GROUP_USERNAME = "item_groups";
    public static final String MESSAGE_ATTR_IS_VOICE_CALL = "is_voice_call";
    public static final String ACCOUNT_REMOVED = "account_removed";
}
