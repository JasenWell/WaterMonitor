package com.hjh.baselib.listener;


import com.hjh.baselib.widget.WheelView;

/**
 * 执行滚动
 * @author hjh
 * @2015-7-12上午9:47:09
 */
public interface OnWheelScrollListener {

    /**
     * Callback method to be invoked when scrolling started.
     * @param wheel the wheel view whose state has changed.
     */
    void onScrollingStarted(WheelView wheel);

    /**
     * Callback method to be invoked when scrolling ended.
     * @param wheel the wheel view whose state has changed.
     */
    void onScrollingFinished(WheelView wheel);
}
