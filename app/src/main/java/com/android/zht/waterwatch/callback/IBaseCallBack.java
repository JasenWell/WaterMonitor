package com.android.zht.waterwatch.callback;


import com.hjh.baselib.base.LBaseActivity;

public interface IBaseCallBack<T> {

    /**
     * 显示错误信息
     * @param code        错误码
     * @param devMsg      技术性提示信息
     */
    void showErrorInfo(int code, String devMsg);

    /**
     * 处理成功
     * @param object 解析后的对象
     * @param type   响应对应码
     */
    void onSuccess(T object, int type);

    LBaseActivity getActivity();

}
