package com.hjh.baselib.utils;

import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Descroption:超过3秒的处理
 * Created by hjh on 2014/2/1.
 */
public class CustomToast {

    private static Toast mToast;
    private static Handler mHandler = new Handler();
    private static boolean mCanceled = true;

    private static Runnable r = new Runnable() {
        public void run() {
            mCanceled = true;
            mToast.cancel();
        }
    };

    public static void showToast(Context mContext, String text, int duration, boolean pad){
        showToast(mContext,text,duration,pad,null);
    }

    public static void showToast(Context mContext, String text, int duration, boolean pad, final OnToastListener onToastListener) {

        mHandler.removeCallbacks(r);
        if (mToast != null) {
            mToast.setText(text);
        }else {
            mToast = Toast.makeText(mContext, text,duration);
            if(pad){
                mToast.setGravity(Gravity.CENTER, 0, 0);
            }
        }
        mHandler.postDelayed(r, duration);
        if(duration > 3000){//显示时间大于3�?
            mCanceled  = false;
            showAgain(duration,onToastListener);
        }else {
            mToast.show();
        }

    }

    private static void showAgain(int duration, final OnToastListener onToastListener){
        if(mCanceled)return;
        mToast.show();
        duration = duration - 1000;
        if(duration > 0) {
            final int t = duration;
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mHandler.removeCallbacks(r);
                    mCanceled = false;
                    mHandler.postDelayed(r, t);
                    showAgain(t, onToastListener);
                }
            }, 1000);
        }else {
            if(onToastListener != null) {
                onToastListener.onCancel();
            }
        }
    }

    public static void showToast(Context mContext, int resId, int duration, boolean pad) {
        showToast(mContext, mContext.getResources().getString(resId), duration,pad,null);
    }

    public static void showToast(Context mContext, int resId, int duration, boolean pad, final OnToastListener onToastListener) {
        showToast(mContext, mContext.getResources().getString(resId), duration,pad,onToastListener);
    }

    /**
     * 在提示时间到后，回调事件
     */
    public interface OnToastListener{
        void onCancel();
    }

}
