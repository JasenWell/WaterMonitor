package com.android.zht.waterwatch.net.param;


import com.android.zht.waterwatch.net.HttpType;
import com.hjh.baselib.constants.ModuleConfig;
import com.hjh.baselib.entity.OkHttpEntity;
import com.hjh.baselib.utils.AppPresences;
import com.hjh.baselib.utils.DateTools;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Params {
    public static String getToken(){
        return AppPresences.getInstance().getString(ModuleConfig.KEY_TOKEN);
    }
    public static String getUserName(){
        return AppPresences.getInstance().getString(ModuleConfig.KEY_LOGIN_NAME);
    }
    public static Map<String,String> loginParams(String userName, String password){
        Map<String,String> map = new HashMap<>();
        map.put("account",userName);
        map.put("password",password);
        map.put("requestType", HttpType.USER_LOGIN+"");
        return map;
    }

    public static List<OkHttpEntity> getChangeMeetParams(String meetid){
        List<OkHttpEntity> map = new ArrayList<>();
        OkHttpEntity okHttpEntity = new OkHttpEntity("token",getToken());
        OkHttpEntity okHttpEntity2 = new OkHttpEntity("meetingid",meetid); ;
        if(meetid != null){
            okHttpEntity2 = new OkHttpEntity("meetingid",meetid);
        }

        String value = AppPresences.getInstance().getString("meet_file_search_time");
        String time = DateTools.toDateString(new Date(DateTools.DATE_TIME_FORMAT));
//        time = time.substring(0,time.length() - 9);
        if(value == null || value.equals("")){
            AppPresences.getInstance().putString("meet_file_search_time",time);
        }else {
            AppPresences.getInstance().putString("meet_file_search_time",time);
        }
        OkHttpEntity okHttpEntity3 = new OkHttpEntity("stime",time);
        map.add(okHttpEntity);
        map.add(okHttpEntity2);
//        map.add(okHttpEntity3);
        return map;
    }

    public static  Map<String,String> getPasswordParams(String userId, String newPassword){
        Map<String,String> map = new HashMap<>();
        map.put("token",getToken());
        map.put("userid",userId);
        map.put("newpasswd",newPassword);
        return map;
    }

}
