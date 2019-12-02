package com.hjh.baselib.base;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.view.LayoutInflaterFactory;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;


import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hjh.baselib.R;
import com.hjh.baselib.constants.Constants;
import com.hjh.baselib.constants.IConfig;
import com.hjh.baselib.constants.ModuleConfig;
import com.hjh.baselib.entity.MessageEvent;
import com.hjh.baselib.entity.ResponseJson;
import com.hjh.baselib.listener.BasicViewInterface;
import com.hjh.baselib.listener.ITitleClickListener;
import com.hjh.baselib.utils.AppPresences;
import com.hjh.baselib.utils.CustomToast;
import com.hjh.baselib.widget.AppActivityManager;
import com.hjh.baselib.widget.BasicDialog;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.lang.reflect.Field;

import butterknife.ButterKnife;
import butterknife.Unbinder;


public abstract class LBaseActivity extends AppCompatActivity implements BasicViewInterface,ITitleClickListener{

    protected Unbinder mUnBinder;
    protected LBaseActivity mActivity;
    protected Context mContext;
    protected LayoutInflater mInflater;
    protected BasicDialog basicDialog;
    private View mRootView;
    private LinearLayout mRootLayout;
    private BroadcastReceiver receiver;
    protected ImageLoader mImageLoader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        setAppTypeFace();
        super.onCreate(savedInstanceState);
        onPreCreate();
        onProcessData();
        if(getActionBar() != null){
            getActionBar().hide();
        }

//        super.setContentView(R.layout.app_root);
//        mRootLayout = (LinearLayout) super.findViewById(R.id.app_root_layout);
//        if(getRootResId() != 0) {
//            mRootLayout.setBackgroundResource(getRootResId());
//        }
        getScreenSize();
        mContext = getApplicationContext();
        mActivity = this;
        mImageLoader = ImageLoader.getInstance();
        setContentView(getContentLayout());
        registerBroadrecevicer();
        mUnBinder = ButterKnife.bind(this);
        AppActivityManager.getInstance().addActivity(this);
        mInflater = LayoutInflater.from(this);
        if (basicDialog == null)
            basicDialog = new BasicDialog(mActivity, R.style.Dialog);
        EventBus.getDefault().register(this);
        switchSkin();
        onAfterCreate(savedInstanceState);
    }

    @Override
    public void onRefreshView() {

    }

    //主题等的改变
    public void onPreCreate(){

    }

    //处理上一界面传输的数据
    protected void onProcessData(){

    }

    public void onAfterCreate(Bundle savedInstanceState){
        onLoadDefaultTitle();
        onLoadDefaultData();
        onLoadDefaultView(savedInstanceState);
    }

    /**
     * 标题定制改变
     */
    public void onLoadDefaultTitle(){

    }

    /**
     * 加载默认数据，从数据库查询
     */
    public void onLoadDefaultData(){

    }

    /**
     * 根据查询的数据加载view
     * @param savedInstanceState
     */
    public void onLoadDefaultView(Bundle savedInstanceState){

    }

    /**
     * onresume后重新获取数据,singleTask 调用onNewIntent()后执行
     */
    protected void onResumeProcessData(){

    }


    protected void setAppTheme(){
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public int getRootResId(){
        return 0;
    }

    public void switchSkin(){

    }

    public void addLayoutAnimation(ViewGroup view,int animId) {
        Animation animation = AnimationUtils.loadAnimation(mContext,animId);
        LayoutAnimationController layoutAnimationController = new LayoutAnimationController(animation);
        layoutAnimationController.setDelay(0.3f);
        layoutAnimationController.setOrder(LayoutAnimationController.ORDER_NORMAL);
        view.setLayoutAnimation(layoutAnimationController);
    }

    public void showLoadDialog(){
        if(basicDialog != null) {
            basicDialog.show();
        }
    }

    public void hideLoadDialog(){
        if(basicDialog != null) {
            basicDialog.dismiss();
        }
    }

    public void setLoadHint(String hint){
        if(basicDialog != null) {
            basicDialog.setHintText(hint);
        }
    }

    @Override
    public void setContentView(int layoutResID) {
        mRootView = LayoutInflater.from(this).inflate(layoutResID, null);
        if(mRootLayout != null) {
            mRootLayout.removeAllViews();
            mRootLayout.addView(mRootView);
        }else {
            setContentView(mRootView);
        }
    }

    @Subscribe
    public void onEventMainThread(MessageEvent event){
        //接收到发布者发布的事件后，进行相应的处理操作
        if(event.getType() == MessageEvent.EventType.RESET_TYPE_SIZE){
//            resetAppTypeFace(mRootView);
//            recreate();//会闪屏，体验不好
            resetActivity();
        }else if(event.getType() == MessageEvent.EventType.RESET_BACKGROUND){
//            if(getRootResId() != 0) {
//                mRootLayout.setBackgroundResource(getRootResId());
//            }

            switchSkin();
        }else if(event.getType() == MessageEvent.EventType.REFRESH_MEETING_FILE){
            dispatchRefreshTask(event);
        }else if(event.getType() == MessageEvent.EventType.DELETE_ALL_FILES){
            onDeleteAllFiles(event);
        }else if(event.getType() == MessageEvent.EventType.REFRESH_HOME){
            refreshHome();
        }
    }

    public void refreshHome(){

    }

    //分发会议文件改变任务
    public void dispatchRefreshTask(MessageEvent event){

    }

    public void onDeleteAllFiles(MessageEvent event){

    }

    public void resetActivity(){

    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getScreenSize();
        onResumeProcessData();
    }

    @Override
    protected void onPause() {
        try {
            super.onPause();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        basicDialog.dismiss();
        if (mUnBinder != null)
            mUnBinder.unbind();
        if(receiver!=null){
            unregisterReceiver(receiver);
            receiver=null;
        }
    }

    @Override
    public void onClickLeftView() {
        back(mActivity);
    }

    @Override
    public void onClickRightView() {

    }

    @Override
    public void onClickOtherView() {

    }

    public int  getMipmapResource(String imageName){
        Context ctx=getBaseContext();
        int resId = getResources().getIdentifier(imageName, "mipmap", ctx.getPackageName());
        //如果没有在"mipmap"下找到imageName,将会返回0
        return resId;
    }

    protected void checkCameraPermission() {
        Constants.NO_VIDEO_PERMISSION.clear();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (int i = 0; i < Constants.VIDEO_PERMISSION.length; i++) {
                if (ActivityCompat.checkSelfPermission(this, Constants.VIDEO_PERMISSION[i]) != PackageManager.PERMISSION_GRANTED) {
                    Constants.NO_VIDEO_PERMISSION.add(Constants.VIDEO_PERMISSION[i]);
                }
            }
            if (Constants.NO_VIDEO_PERMISSION.size() == 0) {

            } else {
                ActivityCompat.requestPermissions(this, Constants.NO_VIDEO_PERMISSION.toArray(new String[Constants.NO_VIDEO_PERMISSION.size()]), Constants.REQUEST_CAMERA);
            }
        } else {

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == Constants.REQUEST_CAMERA) {
            boolean flag = false;
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    flag = true;
                } else {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                Toast.makeText(this, "已授权", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "授权失败", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    @Deprecated
    private synchronized void resetAppTypeFace(View view){
        if(view instanceof ViewGroup){
            ViewGroup viewGroup = (ViewGroup) view;
            int childCount = viewGroup.getChildCount();
            for(int index = 0;index < childCount;index++){
                resetAppTypeFace(viewGroup.getChildAt(index));
            }
        }else if(view instanceof TextView || isExtendTextView(view.getClass().getName())){
            TextView textView = (TextView) view;
            float size = textView.getTextSize();
            float o = AppPresences.getInstance().getFloat(Constants.KEY_SET_TYPESIZE);
            boolean flag = AppPresences.getInstance().getBoolean(Constants.KEY_ADD);
            if(o != -1 && o!= 0) {
                if(o == 0.3f){
                    if(flag){
                        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,size* (o+1));
                    }else {
                        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,(float) (size/ (1+0.6)*(1+0.3)));
                    }
                }else {
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,(float) (size/(1+0.3)*(0.6+1)));
                }
            }else if(o == 0 && !flag){
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,(float) (size/ (1+0.3)));
            }
        }
    }

    public boolean isExtendTextView(String name){
        return name.equals("com.scrd.meet.widget.ScrollTextView");
    }

    @Deprecated
    private void setAppTypeFace(){
//        if (typeface == null){
//            typeface = Typeface.createFromAsset(getAssets(), "hwxk.ttf");
//        Typeface.createFromAsset(getAssets(), "fonts/huawen_songti.ttf");
//        }
//
        LayoutInflaterCompat.setFactory(LayoutInflater.from(this), new LayoutInflaterFactory(){
            @Override
            public View onCreateView(View parent, String name, Context context, AttributeSet attrs){
                AppCompatDelegate delegate = getDelegate();
                View view = delegate.createView(parent, name, context, attrs);
                if(name.equals("com.scrd.meet.widget.ScrollTextView")){
                    try {
                        view = LayoutInflater.from(LBaseActivity.this).createView(name,null,attrs);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                if ( view!= null && (view instanceof TextView || isExtendTextView(name))){
                    TextView textView = (TextView) view;
                    float size = textView.getTextSize();
                    float o = AppPresences.getInstance().getFloat(Constants.KEY_SET_TYPESIZE);
                    if(o != -1 && o!= 0) {
                        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,size * (o+1));
                    }
//                    ((TextView) view).setTypeface(typeface);
                }
                return view;
            }
        });
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     * @param pxValue
     * @return
     */
    public  int px2sp(float pxValue) {
        final float fontScale = getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     * @param spValue
     * @return
     */
    public  int sp2px(float spValue) {
        final float fontScale = getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public int dp2px(Context context, float dpVal){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }

    private void registerBroadrecevicer() {
        //获取广播对象
        receiver = new IntenterBoradCastReceiver();
        //创建意图过滤器
        IntentFilter filter=new IntentFilter();
        //添加动作，监听网络
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(receiver, filter);
    }
    //监听网络状态变化的广播接收器
    public class IntenterBoradCastReceiver extends BroadcastReceiver{

        private ConnectivityManager mConnectivityManager;
        private NetworkInfo netInfo;

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            String action = intent.getAction();
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                mConnectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                netInfo = mConnectivityManager.getActiveNetworkInfo();
                if(netInfo != null && netInfo.isAvailable()) {

                    /////////////网络连接
                    String name = netInfo.getTypeName();

                    if(netInfo.getType()==ConnectivityManager.TYPE_WIFI){
                        /////WiFi网络
                        onNetWorkConnect(netInfo.getType());
                    }else if(netInfo.getType()==ConnectivityManager.TYPE_ETHERNET){
                        /////有线网络
                        onNetWorkConnect(netInfo.getType());
                    }else if(netInfo.getType()==ConnectivityManager.TYPE_MOBILE){
                        /////////3g网络
                        onNetWorkConnect(netInfo.getType());
                    }
                } else {
                    ////////网络断开
                    onNetWorkDisConnect();
                }
            }

        }
    }

    public void onNetWorkConnect(int type){

    }

    public void onNetWorkDisConnect(){

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (basicDialog != null) {
            basicDialog.dismiss();
        }
        back(mActivity);
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

    protected void showToast(String text,int duration,CustomToast.OnToastListener onToastListener){
        CustomToast.showToast(mContext, text, duration, isPad(),onToastListener);
    }

    public void showToast(int strId){
        CustomToast.showToast(mContext, strId, 3000, isPad());
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


    /**
     * @category 渐变加载
     * @param imageView
     * @param bitmap
     */
    protected void fadeInDisplay(ImageView imageView, Bitmap bitmap) {
        final TransitionDrawable transitionDrawable = new TransitionDrawable(
                new Drawable[] { TRANSPARENT_DRAWABLE,
                        new BitmapDrawable(imageView.getResources(), bitmap) });
        imageView.setImageDrawable(transitionDrawable);
        transitionDrawable.startTransition(500);
    }

    private  final ColorDrawable TRANSPARENT_DRAWABLE = new ColorDrawable(
            (android.R.color.transparent));

    public void getScreenSize(){
        DisplayMetrics outMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        IConfig.SCREEN_WIDTH = outMetrics.widthPixels;
        IConfig.SCREEN_HEIGHT = outMetrics.heightPixels;
        getStatusBarHeight();
    }

    protected boolean findBoolean(String key,boolean defaultValue) {
        return getIntent().getBooleanExtra(key, defaultValue);
    }

    protected <T> T findObject(String key,Class<T> clazz){
        Object o = getIntent().getSerializableExtra(key);
        String json = ResponseJson.objectToJson(o);
        T t = new Gson().fromJson(json,clazz);
        return t;
    }

    protected String findString(String key){
        return getIntent().getStringExtra(key);
    }

    protected int findInteger(String key,int defaultValue) {
        return getIntent().getIntExtra(key, defaultValue);
    }

    protected float findFloat(String key,float defaultValue) {
        return getIntent().getFloatExtra(key, defaultValue);
    }

    protected void selectLast(EditText editText){
        String text = editText.getText().toString().trim();
        if(text != null && text.length() >= 1){
            editText.setSelection(text.length());
        }
    }

    /** start activity with left in anim */
    public  void startActivityWithAnim(Activity old, Intent intent){

        if(old == null || intent == null){
            return ;
        }

        // start activity
        old.startActivity(intent) ;
        // set adnim
        old.overridePendingTransition(getIdByName(old,"anim", "push_left_in"),getIdByName(old,"anim", "push_not_move"));
    }

    public  void startActivityWithAnim(Activity old, Intent intent, int requestCode){

        if(old == null || intent == null){
            return ;
        }

        // start activity
        old.startActivityForResult(intent, requestCode);
        // set adnim
        old.overridePendingTransition(getIdByName(old,"anim", "push_left_in"),getIdByName(old,"anim", "push_not_move"));
    }

    /** finish activity with left out anim */
    public  void finishWithAnim(Activity old){
        old.finish() ;
        old.overridePendingTransition(getIdByName(old,"anim", "push_not_move"),getIdByName(old,"anim", "push_left_out"));
    }

    public  void finishWithAnim(Activity old,int resultCode){
        old.setResult(resultCode);
        old.finish() ;
        old.overridePendingTransition(getIdByName(old,"anim", "push_not_move"),getIdByName(old,"anim", "push_left_out"));
    }

    public  int getIdByName(Activity mActivity,String className, String name) {
        String packageName = mActivity.getPackageName();
        Class r = null;
        int id = 0;
        try {
            r = Class.forName(packageName + ".R");

            Class[] classes = r.getClasses();
            Class desireClass = null;

            for (int i = 0; i < classes.length; ++i) {
                if (classes[i].getName().split("\\$")[1].equals(className)) {
                    desireClass = classes[i];
                    break;
                }
            }

            if (desireClass != null)
                id = desireClass.getField(name).getInt(desireClass);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return id;
    }



    public  void back(Activity mActivity){
        mActivity.finish();
        AppActivityManager.getInstance().removeActivity(mActivity);
        if(AppActivityManager.getInstance().getActivityStack().size() != 0){
            mActivity.overridePendingTransition(getIdByName(mActivity,"anim", "push_not_move"),getIdByName(mActivity,"anim", "push_left_out"));
        }
    }


    /**
     * 设置添加屏幕的背景透明度
     * @param bgAlpha
     */
    public  void backgroundAlpha(Activity context, float bgAlpha) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        context.getWindow().setAttributes(lp);
    }


    /**
     * 扩大点击范围
     * @param view 点击的视图
     * @param expandTouchWidth 扩大多大 200
     */
    public  void setTouchDelegate(final View view, final int expandTouchWidth) {
        final View parentView = (View) view.getParent();
        parentView.post(new Runnable() {
            @Override
            public void run() {
                final Rect rect = new Rect();
                view.getHitRect(rect);
                rect.top -= expandTouchWidth;
                rect.bottom += expandTouchWidth;
                rect.left -= expandTouchWidth;
                rect.right += expandTouchWidth;
                TouchDelegate touchDelegate = new TouchDelegate(rect, view);
                parentView.setTouchDelegate(touchDelegate);
            }
        });
    }


    private static final double EARTH_RADIUS = 6378137.0;
    // 返回单位是米
    public double getDistance(double longitude1, double latitude1,
                              double longitude2, double latitude2) {
        double Lat1 = rad(latitude1);
        double Lat2 = rad(latitude2);
        double a = Lat1 - Lat2;
        double b = rad(longitude1) - rad(longitude2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(Lat1) * Math.cos(Lat2)
                * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;
    }

    private  double rad(double d) {
        return d * Math.PI / 180.0;
    }
}
