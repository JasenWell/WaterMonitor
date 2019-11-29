package com.android.zht.waterwatch.net.imp;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.android.zht.waterwatch.callback.IAsynModel;
import com.android.zht.waterwatch.callback.IBaseCallBack;
import com.android.zht.waterwatch.constants.EConfig;
import com.android.zht.waterwatch.net.HttpHelper;
import com.android.zht.waterwatch.net.param.Params;
import com.hjh.baselib.entity.ResponseJson;
import com.hjh.baselib.net.OkHttpUtils;
import com.hjh.baselib.utils.NetworkUtil;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class AsynModelImp implements IAsynModel {

    private IBaseCallBack iBaseCallBack;
    private OkHttpUtils okHttpUtils;
    private String errorNet;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    private class  CallbackImp implements Callback{

        HttpHelper.BUSINESS business;

        public CallbackImp(HttpHelper.BUSINESS business){
            setBusiness(business);
        }

        public void setBusiness(HttpHelper.BUSINESS business) {
            this.business = business;
        }

        @Override
        public void onFailure(Call call,final IOException e) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    iBaseCallBack.getActivity().hideLoadDialog();
                    Log.e(getClass().toString(),e.toString());
                    iBaseCallBack.showErrorInfo(business.getCode()+1,e.toString());
                }
            });
        }
        @Override
        public void onResponse(Call call, final Response response) throws IOException {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    iBaseCallBack.getActivity().hideLoadDialog();
                    String json = null;
                    try {
                        json = response.body().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                        iBaseCallBack.showErrorInfo(business.getCode()+1,e.toString());
                        return;
                    }
                    Log.e(getClass().toString(), json);
                    if (!onCheckCode(response, business.getCode() + 1)) return;
                    iBaseCallBack.onSuccess(ResponseJson.fromJson(json, business.getClazz()), business.getCode());
                }
            });
        }
    }

    public AsynModelImp(IBaseCallBack iBaseCallBack){
        this.iBaseCallBack = iBaseCallBack;
        okHttpUtils = OkHttpUtils.getInstance();
        errorNet = "请检查网络连接";
    }

    @Override
    public void postJson(HttpHelper.BUSINESS business, String json) {
        if(!NetworkUtil.getInstance().isNetworkConnected()){
            iBaseCallBack.showErrorInfo(EConfig.HttpResult.ERR_NET,errorNet);
            return;
        }

        okHttpUtils.postJson(json,HttpHelper.PostGetUrl(business.getBusiness())).enqueue(new CallbackImp(business));
    }

    @Override
    public void login(final HttpHelper.BUSINESS business, String userName, String password) {
        if(!NetworkUtil.getInstance().isNetworkConnected()){
            iBaseCallBack.showErrorInfo(EConfig.HttpResult.ERR_NET,errorNet);
            return;
        }
        okHttpUtils.post(Params.loginParams(userName,password), HttpHelper.PostGetUrl(business.getBusiness())).enqueue(new CallbackImp(business));
    }

    @Override
    public void checkAppUpdate(final HttpHelper.BUSINESS business) {
        okHttpUtils.post(null, HttpHelper.PostGetUrl(business.getBusiness())).enqueue(new CallbackImp(business));
    }

    //用户密码修改
    @Override
    public void changePassword(final HttpHelper.BUSINESS business,String userid,String oldpassword,String newpassword) {
        okHttpUtils.post(Params.getPasswordParams(userid,newpassword), HttpHelper.PostGetUrl(business.getBusiness())).enqueue(new CallbackImp(business));
    }



    /*
     * 判断返回的状态码是否正确
     */
    private boolean onCheckCode(Response response,int...args){
        if(response.code() != 200){
            iBaseCallBack.showErrorInfo(args.length == 0 ? EConfig.RESPONSE_FAILED : args[0],response.message());
            return false;
        }

        return true;
    }
}
