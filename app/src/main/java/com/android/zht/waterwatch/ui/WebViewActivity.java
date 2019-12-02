package com.android.zht.waterwatch.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.zht.waterwatch.R;
import com.android.zht.waterwatch.widget.AppTitleLayout;
import com.hjh.baselib.base.LBaseActivity;
import com.hjh.baselib.entity.MessageEvent;


import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;

public class WebViewActivity extends LBaseActivity {

    @BindView(R.id.web_view)
    WebView mWebView;

    @BindView(R.id.title_bar)
    AppTitleLayout mTitleLayout;

//    private LoadingDialog dialog;
    private Animation animation;
    private ImageView imageView;
    private TextView textView;
    private String mUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        animation = AnimationUtils.loadAnimation(mContext, R.anim.rotate_progress);
        mUrl = findString("url");
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mTitleLayout.getLayoutParams();
        layoutParams.height = dp2px(mContext,36);
        layoutParams = (LinearLayout.LayoutParams) mTitleLayout.getLayout().getLayoutParams();
        layoutParams.height = dp2px(mContext,36);
        mTitleLayout.getTitleView().setTextSize(TypedValue.COMPLEX_UNIT_DIP,14);
        mTitleLayout.setTitleText("查询");
        mTitleLayout.setTitleClickListener(this);
        mTitleLayout.enableLeftButton();
        config();
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_web_view;
    }

    private void config(){
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setAllowFileAccess(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            webSettings.setAllowUniversalAccessFromFileURLs(true);
            webSettings.setAllowFileAccessFromFileURLs(true);
        }
        webSettings.setJavaScriptEnabled(true);
        // 设置可以支持缩放
        webSettings.setSupportZoom(true);
        // 设置出现缩放工具
        webSettings.setBuiltInZoomControls(true);
        //扩大比例的缩放
        webSettings.setUseWideViewPort(true);
        //自适应屏幕
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setBlockNetworkImage(false);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setAppCacheMaxSize(1024 * 1024 * 8);
        String appCachePath = getCacheDir().getAbsolutePath();
        webSettings.setAppCachePath(appCachePath);

        mWebView.setHorizontalScrollBarEnabled(false);
        mWebView.setVerticalScrollBarEnabled(false);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                try {
                    mWebView.stopLoading();
                    mWebView.clearView();
                } catch (Exception e) {
                }
                if (mWebView.canGoBack()) {
                    mWebView.goBack();
                }
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
//                if (dialog == null) {
//                    dialog = new LoadingDialog(mActivity, R.style.Dialog, getConnectView());
//                    imageView = (ImageView) dialog.getContentView().findViewById(R.id.img_progress);
//                    textView = (TextView) dialog.getContentView().findViewById(R.id.tv_progress);
//                    textView.setText("正在加载...");
//                    imageView.setAnimation(animation);
//                }
//
//                dialog.show();
//                animation.start();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
//                animation.cancel();
//                if(dialog != null && dialog.isShowing()) {
//                    dialog.dismiss();
//                }
            }
        });
        mWebView.setWebChromeClient(new WebChromeClient() {


        });
        mWebView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        mWebView.loadUrl(mUrl);
    }

//    private View getConnectView(){//getIdByName("layout", "loading_dialog_layout")
//        View view =	LayoutInflater.from(mContext).inflate(R.layout.loadingprogress_dialog, null);
//        return view;
//    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack(); // goBack()表示返回WebView的上一页面
            return true;
        }
        return super.onKeyDown(keyCode,event);
    }

    @Override
    public void back(Activity mActivity) {
        MessageEvent event = new MessageEvent();
        event.setType(MessageEvent.EventType.REFRESH_HOME);
        EventBus.getDefault().post(event);
        super.back(mActivity);
    }
}
