package com.android.zht.waterwatch.ui;


import android.content.Intent;
import android.os.Handler;
import android.os.Message;


import com.android.zht.waterwatch.R;
import com.hjh.baselib.base.LBaseActivity;
import com.hjh.baselib.constants.ModuleConfig;
import com.hjh.baselib.utils.AppPresences;



public class SplashActivity extends LBaseActivity {



	@Override
	public int getContentLayout() {
		return R.layout.activity_splash;
	}


	@Override
	public void onPreCreate() {
		super.onPreCreate();
		setAppTheme();
	}

	@Override
	protected void onStart() {
		super.onStart();
		mHandler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
//				if(isLogin()) {
//					startActivity(new Intent(SplashActivity.this, MainActivity.class));
//				}
				startActivityWithAnim(mActivity,new Intent(mActivity,LoginActivity.class));
				back(SplashActivity.this);
			}
		},1000);
	}


	private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
		}
	};
	

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	protected boolean isLogin(){
		if(AppPresences.getInstance().getString(ModuleConfig.KEY_TOKEN).equals("")){
			startActivityWithAnim(mActivity,new Intent(mActivity,LoginActivity.class));
			return false;
		}
		return true;
	}
	
}
