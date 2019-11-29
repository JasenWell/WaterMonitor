package com.hjh.baselib.adapter;

import android.support.v7.widget.RecyclerView;


import android.view.ViewGroup;

import java.util.List;

/**
 * Created by hjh on 2015/7/17 15 : 24.
 * RecyclerView 数据装载适配器
 */
public class RecyclerAdapter<T> extends RecyclerView.Adapter<BaseHolder<T>> {
    private OnLoadRecycleViewData<T> onLoadData;
    private List list;
    private boolean hasLoad;
    private int currentPage = 1;
	private int pageNumber = 10;
	private boolean hasMore;

    public RecyclerAdapter(List list, OnLoadRecycleViewData<T> onLoadData){
        this.onLoadData = onLoadData;
        this.list = list;
    }

    @Override
    public BaseHolder<T> onCreateViewHolder(ViewGroup viewGroup, int i) {
        if(onLoadData != null){
            return  onLoadData.onCreateHolder(i);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(BaseHolder<T> viewHolder, int i) {
        if(onLoadData != null){
            onLoadData.onLoadData(viewHolder,i);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public List getList() {
        return list;
    }

    public void setList(List list) {
        this.list = list;
        notifyDataSetChanged();
    }

    /**
     * recycleview回调
     */
    public interface  OnLoadRecycleViewData<T>{
        void onLoadData(BaseHolder<T> viewHolder, int i);
        BaseHolder<T> onCreateHolder(int i);
    }

    public boolean isHasLoad() {
        return hasLoad;
    }

    public void setHasLoad(boolean hasLoad) {
        this.hasLoad = hasLoad;
    }

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public boolean isHasMore() {
		return hasMore;
	}

	public void setHasMore(boolean hasMore) {
		this.hasMore = hasMore;
	}
    
    
}
