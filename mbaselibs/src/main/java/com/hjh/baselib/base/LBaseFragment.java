package com.hjh.baselib.base;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;

import android.support.v4.app.Fragment;

import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import com.hjh.baselib.R;
import com.hjh.baselib.constants.IConfig;
import com.hjh.baselib.constants.ModuleConfig;
import com.hjh.baselib.listener.BasicViewInterface;
import com.hjh.baselib.listener.ITitleClickListener;
import com.hjh.baselib.utils.AppPresences;
import com.hjh.baselib.utils.CustomToast;
import com.hjh.baselib.widget.BaseLinearLayout;
import com.hjh.baselib.widget.BasicDialog;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.lang.reflect.Field;
import java.text.DecimalFormat;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.OkHttpClient;


public abstract class LBaseFragment extends Fragment implements BasicViewInterface,ITitleClickListener{

    Unbinder unbinder;
    public View mView;
    protected OkHttpClient okHttpClient ;
    protected Activity mActivity;
    protected Context mContext;
    protected LayoutInflater mInflater;
    protected BasicDialog basicDialog;

    protected ImageLoader mImageLoader;
    protected DecimalFormat decimalFormat=new DecimalFormat("0.00");
    protected BaseLinearLayout mBaseLayout;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mImageLoader = ImageLoader.getInstance();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mView = setRootView(mView,inflater,container);
        mActivity = getActivity();
        AppPresences.init(mActivity);
        okHttpClient = new OkHttpClient();
        mContext = getActivity().getApplicationContext();
        mInflater = LayoutInflater.from(getActivity());

        unbinder = ButterKnife.bind(this,mView);//绑定fragment
        if (basicDialog == null)
            basicDialog = new BasicDialog(mActivity, R.style.CustomDialog);
        EventBus.getDefault().register(this);
        switchSkin();
        onAfterCreate(savedInstanceState);
        return mView;
    }

    @Override
    public void onRefreshView() {

    }

    protected View setRootView(View rootLayout, LayoutInflater inflater, ViewGroup container) {
        if(rootLayout == null) {
            rootLayout = inflater.inflate(getContentLayout(), container, false);
        }

        if(rootLayout.getParent() != null){
            ViewGroup parent = (ViewGroup) rootLayout.getParent();

            if (parent != null) {
                parent.removeView(rootLayout);
            }
        }

        return rootLayout;
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

    public void switchSkin(){

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Subscribe
    public void onEventMainThread(Object object){
        //接收到发布者发布的事件后，进行相应的处理操作
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        unbinder.unbind();
        basicDialog.dismiss();
    }

    @Override
    public void onClickLeftView() {

    }

    @Override
    public void onClickRightView() {

    }

    @Override
    public void onClickOtherView() {

    }

    public void addLayoutAnimation(ViewGroup view,int animId) {
        Animation animation = AnimationUtils.loadAnimation(mContext,animId);
        LayoutAnimationController layoutAnimationController = new LayoutAnimationController(animation);
        layoutAnimationController.setDelay(0.3f);
        layoutAnimationController.setOrder(LayoutAnimationController.ORDER_NORMAL);
        view.setLayoutAnimation(layoutAnimationController);
    }

    public int  getMipmapResource(String imageName){
        Context ctx= getActivity().getBaseContext();
        int resId = getResources().getIdentifier(imageName, "mipmap", ctx.getPackageName());
        //如果没有在"mipmap"下找到imageName,将会返回0
        return resId;
    }

    public int dp2px(Context context, float dpVal){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }

    public BaseLinearLayout getBaseLayout() {
        return mBaseLayout;
    }

    public void setBaseLayout(BaseLinearLayout mBaseLayout) {
        this.mBaseLayout = mBaseLayout;
    }

    protected void showToast(String text){
        showToast(text, 3000);
    }

    protected void showToast(String text,int duration){
        if(text.endsWith("!")){
            text = text.substring(0,text.length()-1);
        }
        CustomToast.showToast(mContext, text, duration, isPad());
    }

    protected boolean isPad(){
        return (mContext.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    protected int getPixel(int id) {
        return	getResources().getDimensionPixelSize(id);
    }

    /**
     * @param id
     * @return
     */
    protected int getAppColor(int id){
        return getResources().getColor(id);
    }

    protected String getStringById(int id){
        return getResources().getString(id);
    }

    public void getScreenSize(){
        DisplayMetrics outMetrics = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        IConfig.SCREEN_WIDTH = outMetrics.widthPixels;
        IConfig.SCREEN_HEIGHT = outMetrics.heightPixels;
        getStatusBarHeight();
    }

    /**
     * 取得状态栏高度
     * @return
     */
    protected int getStatusBarHeight(){
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        ModuleConfig.STATUS_BAR_HEIGHT = statusBarHeight;
        return statusBarHeight;
    }
}
