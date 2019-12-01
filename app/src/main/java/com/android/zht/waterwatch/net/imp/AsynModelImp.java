package com.android.zht.waterwatch.net.imp;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.android.zht.waterwatch.callback.IAsynModel;
import com.android.zht.waterwatch.callback.IBaseCallBack;
import com.android.zht.waterwatch.constants.EConfig;
import com.android.zht.waterwatch.net.HttpHelper;
import com.android.zht.waterwatch.net.param.Params;
import com.hjh.baselib.entity.MessageEvent;
import com.hjh.baselib.entity.ResponseJson;
import com.hjh.baselib.net.OkHttpUtils;
import com.hjh.baselib.utils.NetworkUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;


public class AsynModelImp implements IAsynModel {

    private IBaseCallBack iBaseCallBack;
    private OkHttpUtils okHttpUtils;
    private String errorNet;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 1000){
                iBaseCallBack.getActivity().hideLoadDialog();
            }else if(msg.what  == 1233){
                HttpHelper.BUSINESS business = (HttpHelper.BUSINESS) msg.obj;
                iBaseCallBack.showErrorInfo(business.getCode()+1,business.getErrorMsg());
            }else if(msg.what == 1234){
                HttpHelper.BUSINESS business = (HttpHelper.BUSINESS) msg.obj;
                iBaseCallBack.onSuccess(business.getResponseJson(), business.getCode());
            }else if(msg.what == 1235){
                MessageEvent event = (MessageEvent) msg.obj;
                iBaseCallBack.showErrorInfo(event.getType(),event.getDes());
            }
        }
    };

    public  byte[] readBytes(InputStream is){
        try {
            byte[] buffer = new byte[1024*5];
            int len = -1 ;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            while((len = is.read(buffer)) != -1){
                baos.write(buffer, 0, len);
            }
            baos.close();
            return baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null ;
    }

    public  String readString(InputStream is){
        return new String(readBytes(is));
    }

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
            handler.sendEmptyMessage(1000);
            Log.e(getClass().toString(),e.toString());
            Message message = new Message();
            message.what = 1233;
            business.setErrorMsg(e.toString());
            message.obj = business;
            handler.sendMessage(message);

        }
        @Override
        public void onResponse(Call call, final Response response) throws IOException {
            new Thread(new Runnable(){
                @Override
                public void run() {
                    handler.sendEmptyMessage(1000);
                    int status = 0;
                    Message message = new Message();
                    business.setResponse(response);

                    String json = null;
                    try {
                        json = response.body().string();
                        status = 1;
                        Log.e(getClass().toString(), json);
                        if (!onCheckCode(response, business.getCode() + 1)) {
                            return;
                        }
                        ResponseJson responseJson = ResponseJson.fromJson(json, business.getClazz());
                        business.setResponseJson(responseJson);
                    } catch (Exception e) {
                        e.printStackTrace();
                        business.setErrorMsg(e.toString());
                        return;
                    }

                    if(status == 0){
                        message.what = 1233;
                    }else {
                        message.what = 1234;
                    }
                    message.obj = business;
                    handler.sendMessage(message);
                }
            }).start();
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
            MessageEvent event = new MessageEvent();
            event.setType(args.length == 0 ? EConfig.RESPONSE_FAILED : args[0]);
            event.setDes(response.message());
            Message message = handler.obtainMessage();
            message.obj = event;
            message.what = 1235;
            handler.sendMessage(message);
            return false;
        }

        return true;
    }
}
