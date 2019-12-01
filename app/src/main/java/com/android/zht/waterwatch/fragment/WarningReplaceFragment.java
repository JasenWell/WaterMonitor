package com.android.zht.waterwatch.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewCompat;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.zht.waterwatch.R;
import com.android.zht.waterwatch.ui.MainActivity;
import com.android.zht.waterwatch.ui.WarningDetailActivity;
import com.android.zht.waterwatch.ui.WarningGatherActivity;
import com.android.zht.waterwatch.util.DateUtil;
import com.android.zht.waterwatch.widget.AppTitleLayout;
import com.hjh.baselib.base.LBaseFragment;
import com.hjh.baselib.widget.PullToRefreshLayout;
import com.hjh.baselib.widget.PullableScrollView;

import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * @author hjh
 * 2016-5-21下午9:26:41
 */
public class WarningReplaceFragment extends LBaseFragment {

	@BindView(R.id.title_bar)
	AppTitleLayout mTitleLayout;

    @BindView(R.id.web_view)
	WebView mWebView;


	private MainActivity mActivity;
	private String mUrl;

	public WarningReplaceFragment(){

	}
	

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
    }

	@Override
	public void onAfterCreate(Bundle savedInstanceState) {
		super.onAfterCreate(savedInstanceState);
		mTitleLayout.setTitleText("告警");
		mTitleLayout.disableLeftButton();
//		mUrl = "http://www.baidu.com";
//		config();
	}

	@Override
	public void onLoadDefaultData() {
		super.onLoadDefaultData();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public int getContentLayout() {
		return R.layout.fragment_warning_replace;
	}

	private void onRequest(){

	}

	public WarningReplaceFragment setOwner(MainActivity mainActivity){
		mActivity = mainActivity;
		return this;
	}

    @Override
    public void onPause() {
    	super.onPause();
    }

    @Override
    public void onResume() {
    	super.onResume();
		onRequest();
    }

	@Override
    public void onRefreshView(){
    }

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	private void config(){
		WebSettings webSettings = mWebView.getSettings();
		webSettings.setAllowFileAccess(true);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			webSettings.setAllowUniversalAccessFromFileURLs(true);
			webSettings.setAllowFileAccessFromFileURLs(true);
		}
		webSettings.setJavaScriptEnabled(true);
		webSettings.setBlockNetworkImage(false);
		webSettings.setDomStorageEnabled(true);
		webSettings.setAppCacheEnabled(true);
		webSettings.setAppCacheMaxSize(1024 * 1024 * 8);
		String appCachePath = mContext.getCacheDir().getAbsolutePath();
		webSettings.setAppCachePath(appCachePath);

		mWebView.setHorizontalScrollBarEnabled(false);//
		mWebView.setVerticalScrollBarEnabled(false); //
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

				try {
					if (mWebView.canGoBack()) {
						mWebView.goBack();
					}
				}catch (Exception	 e){

				}
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);

			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);

			}
		});
		mWebView.setWebChromeClient(new WebChromeClient() {
			@Override
			public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
				return true;
			}

			public void openFileChooser(ValueCallback<Uri> uploadMsg) {

			}

			// For Android 3.0+
			public void openFileChooser(ValueCallback uploadMsg, String acceptType) {
			}

			//For Android 4.1
			public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {

			}

		});
		mWebView.setOverScrollMode(View.OVER_SCROLL_NEVER);
//		mWebView.addJavascriptInterface(new AppJavaScriptInterface(), "Android");
		mWebView.loadUrl(mUrl);
	}

}
