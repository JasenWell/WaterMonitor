package com.hjh.baselib.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

public class PullableScrollView extends ScrollView implements Pullable {
	
	private boolean canLoadMore = false;
	private boolean canRefresh = true;
	private OnScrollListener onScrollListener;

	private boolean isScrolledToTop = true;// 初始化的时候设置一下值
	private boolean isScrolledToBottom = false;

	/**
	 * ScrollView正在向上滑动
	 */
	public static final int SCROLL_UP = 0x01;

	/**
	 * ScrollView正在向下滑动
	 */
	public static final int SCROLL_DOWN = 0x10;

	/**
	 * 最小的滑动距离
	 */
	private static final int SCROLLLIMIT = 40;

	public PullableScrollView(Context context)
	{
		super(context);
	}

	public PullableScrollView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public PullableScrollView(Context context, AttributeSet attrs, int defStyle){
		super(context, attrs, defStyle);
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		if(onScrollListener != null){
			onScrollListener.onScroll(t,1);
			if (oldt > t && oldt - t > SCROLLLIMIT) {// 向下
				onScrollListener.scrollOritention(SCROLL_DOWN);
			} else if (oldt < t && t - oldt > SCROLLLIMIT) {// 向上
				onScrollListener.scrollOritention(SCROLL_UP);
			}
		}

		if (getScrollY() == 0) {    // 小心踩坑1: 这里不能是getScrollY() <= 0
			isScrolledToTop = true;
			isScrolledToBottom = false;
		} else if (getScrollY() + getHeight() - getPaddingTop()-getPaddingBottom() == getChildAt(0).getHeight()) {
			// 小心踩坑2: 这里不能是 >=
			// 小心踩坑3（可能忽视的细节2）：这里最容易忽视的就是ScrollView上下的padding　
			isScrolledToBottom = true;
			isScrolledToTop = false;
		} else {
			isScrolledToTop = false;
			isScrolledToBottom = false;
		}
		notifyScrollChangedListeners();
		// 有时候写代码习惯了，为了兼容一些边界奇葩情况，上面的代码就会写成<=,>=的情况，结果就出bug了
		// 我写的时候写成这样：getScrollY() + getHeight() >= getChildAt(0).getHeight()
		// 结果发现快滑动到底部但是还没到时，会发现上面的条件成立了，导致判断错误
		// 原因：getScrollY()值不是绝对靠谱的，它会超过边界值，但是它自己会恢复正确，导致上面的计算条件不成立
		// 仔细想想也感觉想得通，系统的ScrollView在处理滚动的时候动态计算那个scrollY的时候也会出现超过边界再修正的情况
	}

	private void notifyScrollChangedListeners() {
		if (isScrolledToTop) {
			if (mSmartScrollChangedListener != null) {
				mSmartScrollChangedListener.onScrolledToTop();
			}
		} else if (isScrolledToBottom) {
			if (mSmartScrollChangedListener != null) {
				mSmartScrollChangedListener.onScrolledToBottom();
			}
		}
	}

	public boolean isScrolledToTop() {
		return isScrolledToTop;
	}

	public boolean isScrolledToBottom() {
		return isScrolledToBottom;
	}

	@Override
	public boolean canPullDown(){
		if (getScrollY() == 0)
			return canRefresh;
		else
			return false;
	}

	@Override
	public boolean canPullUp(){
		if (getScrollY() >= (getChildAt(0).getHeight() - getMeasuredHeight()))
			return canLoadMore;//true,全部不支持加载更多
		else
			return false;
	}

	public boolean isCanLoadMore() {
		return canLoadMore;
	}

	public void setCanLoadMore(boolean canLoadMore) {
		this.canLoadMore = canLoadMore;
	}

	public boolean isCanRefresh() {
		return canRefresh;
	}

	public void setCanRefresh(boolean canRefresh) {
		this.canRefresh = canRefresh;
	}

	/**
	 * 设置滚动接口
	 * @param onScrollListener
	 */
	public void setOnScrollListener(OnScrollListener onScrollListener) {
		this.onScrollListener = onScrollListener;
	}

	/**
	 *
	 * 滚动的回调接口
	 *
	 * @author xiaanming
	 *
	 */
	public interface OnScrollListener{
		/**
		 * 回调方法， 返回MyScrollView滑动的Y方向距离
		 * @param scrollY
		 * @param type 是否为滚动产生 1是 ，0否
		 * 				、
		 */
		public void onScroll(int scrollY, int type);

		/**
		 * 滚动方向回调
		 * @param direction
         */
		void scrollOritention(int direction);
	}

	private ISmartScrollChangedListener mSmartScrollChangedListener;

	/** 定义监听接口 */
	public interface ISmartScrollChangedListener {
		void onScrolledToBottom();
		void onScrolledToTop();
	}

	public void setSmartScrollChangedListener(ISmartScrollChangedListener smartScrollChangedListener) {
		mSmartScrollChangedListener = smartScrollChangedListener;
	}
}
