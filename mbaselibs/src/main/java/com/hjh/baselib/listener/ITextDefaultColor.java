package com.hjh.baselib.listener;

import android.widget.TextView;

public interface ITextDefaultColor {

	/**
	 * 设置默认文本颜色，可根据position位置不同设置不同的颜色
	 * @param textView
	 * @param position
	 */
	void setTextViewDefaultColor(TextView textView, int position);
}
