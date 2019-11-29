package com.hjh.baselib.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by hjh on 2015/7/17 16 : 00.
 * holder
 */
public class BaseHolder<T> extends RecyclerView.ViewHolder{

    private T holder;

    public BaseHolder(View itemView, T holder) {
        super(itemView);
        setHolder(holder);
    }

    public T getHolder() {
        return holder;
    }

    public void setHolder(T holder) {
        this.holder = holder;
    }
}
