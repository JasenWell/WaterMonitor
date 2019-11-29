package com.hjh.baselib.constants;

/**
 * Descroption:配置基类
 * Created by hjh on 2014/3/1.
 */
public abstract class IConfig {

    public static int SCREEN_WIDTH			    = 480;
    public static int SCREEN_HEIGHT			= 800;

    public static final String PATH_AUDIO 	 	     = "audio/";
    public static final String PATH_IMAGE 	 		 = "image/";
    public static final String PATH_TEMP 	  		 = "temp/";
    public static final String PATH_ERROR 	 		 = "error/";
    public static final String PATH_LOG			 	 = "log/";
    public static final String PATH_VERSION	     = "version/";

    public static final String KEY_TOKEN = "key_token";
    public static final String KEY_LOGIN_SUCCESS = "key_login_success";
    public static String KEY_LOGIN_NAME						 = "key_login_name";
    public static String KEY_LOGIN_ACCOUNT = "key_login_account";
    public static String KEY_LOGIN_PWD							 = "key_login_pwd";
    public static String KEY_LOGIN_TRUE_NAME			 = "key_login_true_name";
    public static String KEY_LOGIN_CODE						 = "key_login_code";//验证码
    public static String KEY_LOGIN_ROLE						 = "key_login_role";//角色
    public static String KEY_THIRD_LOGIN_TYPE						 = "key_third_login_type";//第三方登录类型
    public static final String KEY_START_FIRST = "key_start_first";
    public static final String KEY_AUTO_LOGIN  = "key_auto_login";

    public static String KEY_USER_ID							 = "key_userid";
    public static String KEY_USER_SEX							 = "key_user_sex";
    public static String KEY_USER_BIRTHDAY				 	 = "key_user_birthday";
    public static String KEY_USER_QQ				 			     = "key_user_qq";
    public static String KEY_USER_WW				 			 = "key_user_ww";
    public static String KEY_USER_REGISTER_TIME		 = "key_user_register_time";
    public static String KEY_USER_ICON							 = "key_user_icon";
    public static String KEY_USER_PHONE                = "key_user_phone";

    public static String KEY_LAST_LOGIN_TIME				 = "key_last_login_time";
    public static String KEY_LAST_LOGIN_ACCOUNT		 = "key_last_login_account";
    public static String KEY_LAST_LOGIN_PWD				 = "key_last_login_pwd";

    public static final String KEY_SWITCH_MSG = "key_switch_msg";
    public static final String KEY_SWITCH_VIBRATE = "key_switch_vibrate";
    public static final String KEY_SWITCH_SOUND = "key_switch_sound";

    public static final String KEY_REMENBER_PWD = "key_remenber_pwd";



    public static String ERROR_NET = "暂无网络信号或数据连接";
    public static boolean checkToken = false;
    public static long timeStamp = 0;//服务器与本地手机时间差
    
    public static int  TAB_HEIGHT = 20;
    public static int  STATUS_BAR_HEIGHT = 20;
    public static String tablePackageName = "";

}
