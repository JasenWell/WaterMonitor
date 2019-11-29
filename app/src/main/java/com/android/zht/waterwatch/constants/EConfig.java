package com.android.zht.waterwatch.constants;

/**
 * Created by zlang on 2018/1/3.
 */

public class EConfig {

    //登录状态
    public static final int LOGIN_SUCCESS                                   = 100;
    public static final int LOGIN_FAILED                                    = LOGIN_SUCCESS + 1;
   //获取用户信息
    public static final int GET_USER_INFO_SUCCESS                           = LOGIN_FAILED + 1;
    public static final int GET_USER_INFO_FAILED                            = GET_USER_INFO_SUCCESS + 1;


    //判断返回状态码是否正确
    public static final int RESPONSE_SUCCESS                                = 1 + 1;
    public static final int RESPONSE_FAILED                                 = RESPONSE_SUCCESS + 1;


    //APK更新
    public static final int CHECK_NEWAPP_SUCCESS                                 = 1 + 1;
    public static final int CHECK_NEWAPP_FAILED                              = CHECK_NEWAPP_SUCCESS + 1;

    //修改密码
    public static final int CHANGE_PASSWORD_SUCCESS                                 = CHECK_NEWAPP_FAILED + 1;
    public static final int CHANGE_PASSWORD_FAILED                              = CHANGE_PASSWORD_SUCCESS + 1;



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
       //网络错误
       int ERR_NET = - 100;
   }
}
