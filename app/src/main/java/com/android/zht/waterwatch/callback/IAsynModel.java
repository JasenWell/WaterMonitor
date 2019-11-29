package com.android.zht.waterwatch.callback;


import com.android.zht.waterwatch.net.HttpHelper;

public interface IAsynModel {

    /**
     * 登录
     * @param userName 用户名
     * @param password 用户密码
     */
    void login(HttpHelper.BUSINESS business,String userName, String password);

    void changePassword(HttpHelper.BUSINESS business,String userid,String oldpassword,String newpassword);

    void checkAppUpdate(HttpHelper.BUSINESS business);

    void postJson(HttpHelper.BUSINESS business,String json);





}
