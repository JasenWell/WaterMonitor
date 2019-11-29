package com.android.zht.waterwatch.net;


import android.os.Environment;

import com.android.zht.waterwatch.bean.UserInfo;
import com.android.zht.waterwatch.bean.VersionInfo;
import com.android.zht.waterwatch.constants.EConfig;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Descroption:
 * Created by hjh on 2016/3/1.
 */
public class HttpHelper {

    public static String WEB_HOST = "http://139.199.165.130:9092/";//139.199.165.130,//192.168.1.102//112.74.218.80//a.wushangxiupin.com//120.25.74.232
    public static final int PORT  = 9092;
    public static final String WEB_BUSINESS = "/ZHTWaterMeterServer/";
    public static final String KEY_BUSINESS = "requestData";

    public static String getURl(){
        StringBuilder sb = new StringBuilder(WEB_HOST);//内网
        return "http://10.0.10.32:9119/scpc2018/padapi/";//内网
//        return sb.append(URL).append(":").append(PORT).append(ACTION).toString();
    }

    public static String getDownloadDir(){
        return Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator +"ssa/sas";
    }

    public static String PostGetUrl(String url){
        StringBuilder sb = new StringBuilder(getURl());
        return sb.append(url).toString();
    }

    public  interface Method{
        int POST = 1;
        int GET = 2;
    }

    public enum BUSINESS{
    	
    	REQUEST_TEST("", Method.POST,true,Object.class, EConfig.LOGIN_SUCCESS),
        REQUEST_LOGIN("api/user.html", Method.POST,true,UserInfo.class,EConfig.LOGIN_SUCCESS),
        REQUEST_UPDATE_INFO("api/user.html", Method.POST,true,Integer.class,EConfig.LOGIN_SUCCESS),
        REQUEST_UPDATE_PWD("api/user.html", Method.POST,true,Integer.class,EConfig.LOGIN_SUCCESS),
        REQUEST_UPLOAD_SUGGEST("api/user.html", Method.POST,true,Integer.class,EConfig.LOGIN_SUCCESS),
//        REQUEST_GET_RECHARGE_LIST("api/user.html", Method.POST,true,Object.class,new TypeToken<List<Object>>(){}.getType()),
        REQUEST_CHECK_VERSION("api/user.html", Method.POST,true,VersionInfo.class,EConfig.LOGIN_SUCCESS)




    	;

        private String business;
        private int method;
        private int code; //   失败为成功+1
        private boolean object;
        private Class clazz;
        private Type type;

        /**
         *
         * @param business 具体业务
         * @param method 请求方法
         * @param object 返回对象 or数组
         * @param clazz 返回data的对象字节类型
         */
        private BUSINESS(String business,int method,boolean object,Class clazz,int code,Type ...args){
            setBusiness(business);
            setMethod(method);
            setCode(code);
            setObject(object);
            setClazz(clazz);
            if(args != null && args.length > 0) {
                setType(args[0]);
            }
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getBusiness() {
            return business;
        }

        public void setBusiness(String business) {
            this.business = business;
        }

        public int getMethod() {
            return method;
        }

        public void setMethod(int method) {
            this.method = method;
        }

        public boolean isObject() {
            return object;
        }

        public void setObject(boolean object) {
            this.object = object;
        }

        public Class getClazz() {
            return clazz;
        }

        public void setClazz(Class clazz) {
            this.clazz = clazz;
        }


        public Type getType() {
            return type;
        }

        public void setType(Type type) {
            this.type = type;
        }
    }


    public static class ACTION{
        public static String KEY_IP = "key_ip";
        public static String KEY_AUTO_LOGIN = "key_auto_login";
        public static String KEY_USER_NAME = "key_user_name";
        public static String KEY_USER_NICK = "key_user_nick";
        public static String KEY_USER_ID = "key_user_id";
        public static String KEY_USER_NO = "key_user_no";
        public static String KEY_USER_ICON = "key_user_icon";
        public static String KEY_USER_PWD = "key_user_pwd";
        public static String KEY_USER_PHONE = "key_user_phone";
        public static String KEY_INSTALL_TIME = "key_install_time";
        public static String KEY_INSTALL_FIRST = "key_install_first";
        public static String KEY_LOGIN_TYPE = "key_login_type";
        public static String KEY_USER_CARD = "key_user_card";
        public static String KEY_SPLASH_AD = "key_splash_ad";

        public static String KEY_RECEIVED_MSG = "key_received_msg";
        public static String KEY_READ_MSG = "key_read_msg";
        public static String KEY_TTS_SWITCH = "key_tts_switch";
    }
    
    public interface HttpResult{
        //系统错误
        int SYSTEM_ERROR = -1;
        //成功
        int SUCCESS = 1;
        //失败
        int FAILED = 0;
        //登录信息过期
        int INVALID_TOKEN = 2;
        //参数错误
        int ERROR_PARAM = 3;
        //无数据
        int NO_DATA = 4;
        //禁用
        int DISABLED = 5;

        //远程服务器错误（Ping++ 服务器/第三方支付渠道出现错误
        int ERR_REMOTE_SERV_ERR = -9;


        //超时异常
        int TIME_OUT = -98;

        //json解析错误
        int ERR_PARSE = - 99;
        //volley分发错误
        int ERR_NET = - 100;
    }
}
