package com.android.zht.waterwatch.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;


import com.android.zht.waterwatch.R;
import com.android.zht.waterwatch.bean.GenerateData;
import com.android.zht.waterwatch.bean.UserInfo;
import com.android.zht.waterwatch.callback.IBaseCallBack;
import com.android.zht.waterwatch.net.HttpHelper;
import com.android.zht.waterwatch.net.HttpType;
import com.android.zht.waterwatch.net.imp.AsynModelImp;
import com.hjh.baselib.base.LBaseActivity;
import com.hjh.baselib.constants.ModuleConfig;
import com.hjh.baselib.entity.RequestData;
import com.hjh.baselib.entity.ResponseJson;
import com.hjh.baselib.utils.AppPresences;
import com.hjh.baselib.utils.KeyBoardManageTools;
import com.hjh.baselib.utils.SoftKeyBoardListener;
import com.hjh.baselib.utils.StringTools;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;



public class LoginActivity extends LBaseActivity implements IBaseCallBack<ResponseJson<UserInfo>> {
	@BindView(R.id.login_layout)
	ScrollView scrollView;

	@BindView(R.id.login_phone)
    EditText mAccountText;
	
	@BindView(R.id.login_pwd)
    EditText mPwdText;
	
	@BindView(R.id.login_btn)
    Button mLoginBtn;
	
	@BindView(R.id.config_server)
	TextView configServer;

	@BindView(R.id.checkbox)
	CheckBox checkBox;

	@BindView(R.id.checktext)
	TextView checktext;

	private AsynModelImp asynModelImp;
	String account,pwd;

	@Override
	public void onAfterCreate(Bundle savedInstanceState) {
		AppPresences.getInstance().putString(ModuleConfig.KEY_TOKEN, "");
		AppPresences.getInstance().putInt(ModuleConfig.KEY_LOGIN_ROLE, -1);
		AppPresences.getInstance().putString(ModuleConfig.KEY_USER_PHONE,"");
		super.onAfterCreate(savedInstanceState);
		asynModelImp = new AsynModelImp(this);
		SoftKeyBoardListener.setListener(this, new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
			@Override
			public void keyBoardShow(int height) {
				if(mAccountText.hasFocus()) {
					scrollView.smoothScrollTo(0,300);
				}else if(mPwdText.hasFocus()){
					scrollView.smoothScrollTo(0,300);
				}
			}

			@Override
			public void keyBoardHide(int height) {
				scrollView.smoothScrollTo(0,0);
			}
		});

		if(AppPresences.getInstance().getBoolean(ModuleConfig.KEY_AUTO_LOGIN,false)){
			String pwd = AppPresences.getInstance().getString(ModuleConfig.KEY_LOGIN_PWD);
			mAccountText.setText(AppPresences.getInstance().getString(ModuleConfig.KEY_LOGIN_ACCOUNT));
			if(!StringTools.isEmpty(pwd)) {
				mPwdText.setText(pwd);
				selectLast(mAccountText);
				selectLast(mPwdText);
//				mAccountText.setFocusable(false);
//				mPwdText.setFocusable(true);
				checkBox.setChecked(true);
				onLogin();
			}
		}

		checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					AppPresences.getInstance().putBoolean(ModuleConfig.KEY_AUTO_LOGIN,true);
				}else {
					AppPresences.getInstance().putBoolean(ModuleConfig.KEY_AUTO_LOGIN,false);
				}
			}
		});
	}

	@Override
	public int getContentLayout() {
		return R.layout.activity_login;
	}

	@Override
	public void onLoadDefaultView(Bundle savedInstanceState) {
		super.onLoadDefaultView(savedInstanceState);
		checkCameraPermission();
	}

	@OnClick({R.id.login_btn, R.id.config_server,R.id.checktext})
	public void onViewClicked(View v) {
		switch (v.getId()) {
		case R.id.login_btn:
			onLogin();
			break;
		case R.id.config_server:
			startActivityWithAnim(mActivity,new Intent(mContext,ServerConfigActivity.class));
			break;
		case R.id.checktext:
			if(checkBox.isChecked()){
				checkBox.setChecked(false);
			}else {
				checkBox.setChecked(true);
			}
			break;
		default:
			break;
		}
	}
	
	private void onLogin(){
		account = mAccountText.getText().toString();
		pwd = mPwdText.getText().toString();
		if(StringTools.isEmpty(account)){
			showToast("账号不能为空!");
			return;
		}
		
		if(StringTools.isEmpty(pwd)){
			showToast("密码不能为空!");
			return;
		}
		
		if(!StringTools.isPassword(pwd)){
			showToast("密码格式错误!");
			return;
		}

		KeyBoardManageTools.hideVirtualKeyPad(mContext,mPwdText);
		RequestData requestData = new RequestData(HttpType.USER_LOGIN);
		Map<String,String> map = new HashMap<>();
		map.put("account",account);
		map.put("password",pwd);
		requestData.setRequestParameters(map);
		setLoadHint("登录中...");
		showLoadDialog();
		asynModelImp.login(HttpHelper.BUSINESS.REQUEST_LOGIN,account,pwd);
//		asynModelImp.postJson(HttpHelper.BUSINESS.REQUEST_LOGIN, ResponseJson.objectToJson(requestData));
	}


	@Override
	public void showErrorInfo(int code, String devMsg) {
		showToast(devMsg);
	}

	@Override
	public void onSuccess(ResponseJson<UserInfo> responseJson, int type) {
		if(responseJson.getStatus() != 1){
			showErrorInfo(responseJson.getStatus(),responseJson.getInfo());
			return;
		}
		UserInfo userInfo = responseJson.getData();
		userInfo.setGisurl(responseJson.getGisurl());
		userInfo.setYanzhenma(responseJson.getYanzhenma());
		AppPresences.getInstance().putString(ModuleConfig.KEY_USER_PHONE,userInfo.getUserPhone());
		AppPresences.getInstance().putInt(ModuleConfig.KEY_USER_ID,userInfo.getDepartmentInfo().getUserId());

		AppPresences.getInstance().putString(ModuleConfig.KEY_LOGIN_ACCOUNT,account);
		AppPresences.getInstance().putString(ModuleConfig.KEY_TOKEN,account);
		AppPresences.getInstance().putString(ModuleConfig.KEY_LOGIN_PWD,pwd);
		AppPresences.getInstance().putString(ModuleConfig.KEY_LOGIN_NAME,userInfo.getUserName());
		AppPresences.getInstance().putInt(ModuleConfig.KEY_LOGIN_ROLE,userInfo.getUserType());
		switchPage(userInfo);
	}

	private void switchPage(UserInfo userInfo){
		Intent intent =  new Intent(mActivity,MainReplaceActivity.class);
		intent.putExtra("user_info",userInfo);
		startActivityWithAnim(mActivity,intent);
		back(mActivity);
	}

	@Override
	public LBaseActivity getActivity() {
		return mActivity;
	}
}
