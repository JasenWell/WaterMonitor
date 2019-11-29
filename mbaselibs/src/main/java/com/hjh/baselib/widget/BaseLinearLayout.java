package com.hjh.baselib.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;


import com.hjh.baselib.constants.IConfig;
import com.hjh.baselib.constants.ModuleConfig;
import com.hjh.baselib.listener.BasicViewInterface;
import com.hjh.baselib.utils.CustomToast;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.DecimalFormat;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.OkHttpClient;

public abstract class BaseLinearLayout extends LinearLayout implements View.OnClickListener,BasicViewInterface {

	protected Context mContext;
    protected DecimalFormat decimalFormat=new DecimalFormat("0.00");
    protected LayoutInflater inflater ;
    protected ImageLoader mImageLoader;
    protected OkHttpClient okHttpClient ;
    Unbinder unbinder;

    public BaseLinearLayout(Context context){
        this(context, null);
    }

    public BaseLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        inflater = LayoutInflater.from(mContext);
        mImageLoader = ImageLoader.getInstance();
        okHttpClient = new OkHttpClient();
        if(getContentLayout() != 0) {
            View view = inflater.inflate(getContentLayout(), this, false);
            addView(view);
            unbinder = ButterKnife.bind(this, view);//绑定fragment
        }
        EventBus.getDefault().register(this);
        onAfterCreate(null);
    }

    @Subscribe
    public void onEventMainThread(Object object){
        //接收到发布者发布的事件后，进行相应的处理操作
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        EventBus.getDefault().unregister(this);
        if(unbinder != null) {
            unbinder.unbind();
        }
    }

    @Override
    public void onAfterCreate(Bundle savedInstanceState) {
        onLoadDefaultTitle();
        onLoadDefaultData();
        onLoadDefaultView(savedInstanceState);
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
    public void onRefreshView() {

    }

    protected IConfig getConfig(){
    	return ModuleConfig.getInstance();
    }
    
    protected void showToast(String text){
        showToast(text,1000);
    }

    private void showToast(String text, int duration){
        CustomToast.showToast(mContext, text, duration, isPad());
    }

    public void setCheck(boolean check){

    }

    /**
     * 是否是pad
     * @return
     */
    protected boolean isPad(){
        return (getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    protected int getPixel(int id){
        return getResources().getDimensionPixelSize(id);
    }

    protected int getAppColor(int id){
        return getResources().getColor(id);
    }
    
    protected String getAppString(int id){
        return getResources().getString(id);
    }

    @Override
    public void onClick(View arg0) {

    }

    public void onResume(){

    }

    public void onReload(){

    }

    public void onDestory(){

    }

}
