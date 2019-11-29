package com.android.zht.waterwatch.ui;

import android.content.Intent;
import android.os.Message;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.zht.waterwatch.R;
import com.android.zht.waterwatch.widget.AppTitleLayout;
import com.hjh.baselib.base.LBaseActivity;
import com.hjh.baselib.constants.ModuleConfig;
import com.hjh.baselib.utils.AppPresences;
import com.hjh.baselib.utils.NetworkUtil;
import com.hjh.baselib.utils.StringTools;


import butterknife.BindView;
import butterknife.OnClick;

/**
 * 修改密码
 */
public class ModifyPwdActivity extends LBaseActivity {


    @BindView(R.id.title_bar)
    AppTitleLayout mTitle;

    @BindView(R.id.old_pwd)
    EditText mOldPwd;

    @BindView(R.id.new_pwd)
    EditText mNewPwd;

    @BindView(R.id.show_pwd)
    TextView mShow;

    private boolean show;

    @Override
    public int getContentLayout() {
        return R.layout.activity_modify;
    }

    @Override
    public void onLoadDefaultTitle() {
        super.onLoadDefaultTitle();
        mTitle.setTitleClickListener(this);
        mTitle.setTitleText("修改密码");
        mTitle.enableRightButtonText("确定");
    }

    @OnClick({R.id.show_pwd})
    public void onViewClicked(View v) {
        if(v.getId() == R.id.show_pwd){
            show = !show;
            mShow.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(show ? R.drawable.show_pwd : R.drawable.un_show_pwd),null,null,null);
            mNewPwd.setTransformationMethod(show ? HideReturnsTransformationMethod.getInstance() : PasswordTransformationMethod.getInstance());
            mOldPwd.setTransformationMethod(show ? HideReturnsTransformationMethod.getInstance() : PasswordTransformationMethod.getInstance());
        }
    }


    @Override
    public void onClickRightView() {
        super.onClickRightView();
        String oldPwd = mOldPwd.getText().toString().trim();
        String newPwd = mNewPwd.getText().toString().trim();
        if(StringTools.isEmpty(oldPwd)){
            showToast("请输入旧密码");
            return;
        }

        if(StringTools.isEmpty(newPwd)){
            showToast("请输入新密码");
            return;
        }

        if(!StringTools.isPassword(oldPwd)){
            showToast("旧密码格式不对");
            return;
        }

        if(!StringTools.isPassword(newPwd)){
            showToast("新密码格式不对");
            return;
        }

        if(!oldPwd.equals(AppPresences.getInstance().getString(ModuleConfig.KEY_LOGIN_PWD))){
            showToast("旧密码错误");
            return;
        }

        if(!NetworkUtil.getInstance().isNetworkConnected()){
            return;
        }

//        JsonParamter paramter = new JsonParamter();
//        UserBean userBean = new UserBean();
//        userBean.setUserId(AppPresences.getInstance().getInt(AppConfig.ACTION.KEY_USER_ID));
//        userBean.setPassword(newPwd);
//        userBean.setUserPhone(AppPresences.getInstance().getString(AppConfig.ACTION.KEY_USER_PHONE));
//        paramter.setRequestType(RequestType.USER_UPDATE_PWD.getType());
//        paramter.setRequestParamter(userBean);
//        component.modifyPwd(paramter,mHandler);

    }

//    protected void handleMessageOnMainThread(Message msg) {
//        super.handleMessageOnMainThread(msg);
//        switch (msg.what){
//            case Constants.RESULT_MODIFY_PWD_FAILED:
//                if(isConnectTimeOut((ResultObject) msg.obj,"修改密码")){}
//                break;
//            case Constants.RESULT_MODIFY_PWD_SUCCESS:
//                showToast("密码修改成功");
//                startActivityWithAnim(mActivity, new Intent(mContext, LoginActivity.class));
//                AppActivityManager.getInstance().finishAllActivity();
//                break;
//        }
//    }
}
