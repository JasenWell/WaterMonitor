package com.hjh.baselib.listener;


import com.hjh.baselib.widget.WheelView;

/**
 * Created by hjh on 2015/8/18.
 * 时间控件滚动改变监听器
 * @author hjh
 * @2015-7-12上午9:46:11
 */
public interface OnWheelChangedListener {
    void onChanged(WheelView wheel, int oldValue, int newValue);
}
