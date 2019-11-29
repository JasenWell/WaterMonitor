package com.hjh.baselib.widget;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

public class PullableRecyclerView extends RecyclerView implements Pullable{

	private boolean canLoadMore = false;

	public PullableRecyclerView(Context context){
		super(context);
	}

	public PullableRecyclerView(Context context, AttributeSet attrs){
		super(context, attrs);
	}

	public PullableRecyclerView(Context context, AttributeSet attrs, int defStyle){
		super(context, attrs, defStyle);
	}

	@Override
	public boolean canPullDown(){
		if (getAdapter().getItemCount() == 0){
			// 没有item的时候也可以下拉刷新
			return true;
		} else if (getFirstVisiblePosition() == 0 && getChildAt(0).getTop() >= 0){
			// 滑到顶部了
			return true;
		} else
			return false;
	}
	
	private int getFirstVisiblePosition(){
		LayoutManager layoutManager = getLayoutManager();
		int lastItemPosition = 0;
        //判断是当前layoutManager是否为LinearLayoutManager
        // 只有LinearLayoutManager才有查找第一个和最后一个可见view位置的方法
        if (layoutManager instanceof LinearLayoutManager) {
            LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
            //获取最后一个可见view的位置
            lastItemPosition = linearManager.findLastVisibleItemPosition();
            //获取第一个可见view的位置
            int firstItemPosition = linearManager.findFirstVisibleItemPosition();
            return firstItemPosition;
        }else if (layoutManager instanceof GridLayoutManager) {
        	GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            //获取最后一个可见view的位置
            lastItemPosition = gridLayoutManager.findLastVisibleItemPosition();
            //获取第一个可见view的位置
            int firstItemPosition = gridLayoutManager.findFirstVisibleItemPosition();
            return firstItemPosition;
		}
        
        return -1;
	}
	
	private int getLastVisiblePosition(){
		LayoutManager layoutManager = getLayoutManager();
		int lastItemPosition = 0;
        //判断是当前layoutManager是否为LinearLayoutManager
        // 只有LinearLayoutManager才有查找第一个和最后一个可见view位置的方法
        if (layoutManager instanceof LinearLayoutManager) {
            LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
            //获取最后一个可见view的位置
            lastItemPosition = linearManager.findLastVisibleItemPosition();
            //获取第一个可见view的位置
            return lastItemPosition;
        }else if (layoutManager instanceof GridLayoutManager) {
        	GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            //获取最后一个可见view的位置
            lastItemPosition = gridLayoutManager.findLastVisibleItemPosition();
            //获取第一个可见view的位置
            return lastItemPosition;
		}
        
        return -1;
	}

	@Override
	public boolean canPullUp(){
		if (getAdapter().getItemCount() == 0){
			// 没有item的时候也可以上拉加载
			return canLoadMore;//true
		} else if (getLastVisiblePosition() == (getAdapter().getItemCount() - 1)){
			// 滑到底部了
			if (getChildAt(getLastVisiblePosition() - getFirstVisiblePosition()) != null
					&& getChildAt(
							getLastVisiblePosition()
									- getFirstVisiblePosition()).getBottom() <= getMeasuredHeight())
				return canLoadMore;//true
		}
		return false;
	}

	public boolean isCanLoadMore() {
		return canLoadMore;
	}

	public void setCanLoadMore(boolean canLoadMore) {
		this.canLoadMore = canLoadMore;
	}
}
