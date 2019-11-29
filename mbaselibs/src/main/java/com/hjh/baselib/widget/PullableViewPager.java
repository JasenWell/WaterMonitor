package com.hjh.baselib.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

public class PullableViewPager extends ViewPager implements Pullable{

	private boolean canLoadMore = false;

	public PullableViewPager(Context context){
		super(context);
	}

	public PullableViewPager(Context context, AttributeSet attrs){
		super(context, attrs);
	}

	@Override
	public boolean canPullDown(){
		if(getChildCount() == 0){
			return false;
		}else if(canPullDown(getChildAt(getCurrentItem()))){
			return true;
		}
		
		return false;
	}
	
	private int getFirstVisiblePosition(RecyclerView recyclerView){
		RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
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
	
	private boolean canPullDown(View view){
		if(view instanceof RecyclerView){
			RecyclerView recyclerView = (RecyclerView) view;
			if (recyclerView.getAdapter().getItemCount() == 0){
				// 没有item的时候也可以下拉刷新
				return true;
			} else if (getFirstVisiblePosition(recyclerView) == 0 && recyclerView.getChildAt(0).getTop() >= 0){
				// 滑到顶部了
				return true;
			} else
				return false;
		}
		
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
}
