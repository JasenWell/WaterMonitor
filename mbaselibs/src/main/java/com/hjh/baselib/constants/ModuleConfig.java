package com.hjh.baselib.constants;



/**
 * Descroption:
 * Created by hjh on 2014/3/1.
 */
public class ModuleConfig extends IConfig {

    public static int PRIVATE_DB_VERSION	= 1;
    public static int SYSTEM_DB_VERSION 	= 1;
    public static String DB_NAME = "whcd_flower";
    private static ModuleConfig instance;
    public static double latitude = -1f;//30.67
    public static double longtitude = -1f;//104.06
    public static String mCode = "FE:E4:F8:52:BB:7F:03:04:04:0B:21:C1:E4:69:89:FD:7C:9B:EB:21;com.whcd.flower";
    public static String address;
    public static boolean result;
    public static String ERROR_NET = "暂无网络信号或数据连接";
    public static boolean checkToken = false;
    private int version = 1;


    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public static ModuleConfig getInstance(){
        if(instance == null){
            syncInit();
        }

        return instance;
    }

    private static synchronized  void syncInit(){
        if(instance == null){
//            tablePackageName = "com.whcd.android.furniture.bean";
            instance = new ModuleConfig();
        }
    }

    public static class ACTION{

        
        //url
        public static final String KEY_URL_PROTOCAL = "key_url_protocal";
        public static final String KEY_URL_ABOUT = "key_url_about";
        

    }
}
