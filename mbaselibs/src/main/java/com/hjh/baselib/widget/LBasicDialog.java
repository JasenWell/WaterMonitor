package com.hjh.baselib.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.LayoutInflater;
import android.view.View;

import com.hjh.baselib.listener.BasicViewInterface;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by hjh on 2019/3/28.
 */

public abstract class LBasicDialog extends Dialog implements BasicViewInterface{

    protected Activity mOwner ;
    protected LayoutInflater mInfalter ;
    Unbinder unbinder;
    public Context mContext;
    public View root;

    public LBasicDialog(Activity owner, @StyleRes int themeResId) {
        super(owner, themeResId);
        mOwner		= owner ;
        mContext = owner;
        mInfalter	= LayoutInflater.from(owner);
        if(getContentLayout() != 0) {
            root = mInfalter.inflate(getContentLayout(),null, false);
            setContentView(root);
            unbinder = ButterKnife.bind(this, root);//绑定fragment
        }
        EventBus.getDefault().register(this);
        onAfterCreate(null);
    }

    @Subscribe
    public void onEventMainThread(Object object){
        //接收到发布者发布的事件后，进行相应的处理操作
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    public void onDestory(){
        EventBus.getDefault().unregister(this);
        if(unbinder != null) {
            unbinder.unbind();
        }
    }

    protected int getAppColor(int id){
        return mOwner.getResources().getColor(id);
    }

    @Override
    public void onLoadDefaultTitle() {

    }

    @Override
    public void onLoadDefaultData() {

    }

    @Override
    public void onLoadDefaultView(Bundle savedInstanceState) {

    }

    @Override
    public void onAfterCreate(Bundle savedInstanceState) {

    }

    @Override
    public void onRefreshView() {

    }
}
