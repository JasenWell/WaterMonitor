package com.hjh.baselib.constants;

import android.Manifest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zlang on 2017/12/14.
 */

public class Constants {

    //权限列表
    public static String[] VIDEO_PERMISSION = {
            Manifest.permission.CAMERA,//相机权限
            Manifest.permission.RECORD_AUDIO,//录音权限
            Manifest.permission.WRITE_EXTERNAL_STORAGE,//写入SDCard权限
            Manifest.permission.READ_EXTERNAL_STORAGE,//读取SDCard权限
//            Manifest.permission.CALL_PHONE,//打电话权限
            Manifest.permission.READ_PHONE_STATE,//读取手机状态权限
            Manifest.permission.ACCESS_COARSE_LOCATION//定位权限
    };
    public static List<String> NO_VIDEO_PERMISSION = new ArrayList<>();
    public static final int REQUEST_CAMERA = 0;

    public static String KEY_ADD                                        = "key_add";
    public static String KEY_SET_TYPESIZE                               = "key_set_typesize";
    public static String KEY_SET_BACKGROUND                             = "key_set_background";



}
