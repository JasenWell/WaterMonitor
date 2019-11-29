package com.hjh.baselib.utils;

import android.content.Context;
import android.content.res.Resources;

public class PixelUtil {

    private static Context mContext ;
    
    public static void initContext(Context context){
    	mContext	= context ;
    }

    public static int dp2px(float value) {
    	
    	if(mContext == null) return (int) value ;
    	
        final float scale = mContext.getResources().getDisplayMetrics().densityDpi;
        return (int) (value * (scale / 160) + 0.5f);
    }

    public static int px2dp(float value) {
    	
    	if(mContext == null) return (int) value ;
    	
        final float scale = mContext.getResources().getDisplayMetrics().densityDpi;
        
        return (int) ((value * 160) / scale + 0.5f);
    }

    public static int sp2px(float value) {
    	
    	if(mContext == null) return (int) value ;
    	
        Resources r;
        
        if (mContext == null) {
            r = Resources.getSystem();
        } else {
            r = mContext.getResources();
        }
        
        float spvalue = value * r.getDisplayMetrics().scaledDensity;
        
        return (int) (spvalue + 0.5f);
    }

    public static int px2sp(float value) {
    	
    	if(mContext == null) return (int) value ;
    	
        final float scale = mContext.getResources().getDisplayMetrics().scaledDensity;
        return (int) (value / scale + 0.5f);
    }
}
