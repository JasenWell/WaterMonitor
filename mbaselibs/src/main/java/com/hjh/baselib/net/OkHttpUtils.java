package com.hjh.baselib.net;

import android.util.Log;

import com.hjh.baselib.entity.OkHttpEntity;
import com.hjh.baselib.entity.ResponseJson;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.net.URLEncoder;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * 自定义快捷网络请求
 * Created by zlang on 2018/1/2.
 */

public class OkHttpUtils {
    private volatile static OkHttpUtils mInstance;
    private OkHttpClient mOkHttpClient;
    protected static final String UTF_8 = "UTF-8";

    /**
     * 构造方法
     * @param okHttpClient
     */
    public OkHttpUtils(OkHttpClient okHttpClient)
    {
        if (okHttpClient == null)
        {
            mOkHttpClient = new OkHttpClient();
        } else
        {
            mOkHttpClient = okHttpClient;
        }
    }

    /**
     * 初始化 OkHttpClient
     * @param okHttpClient
     * @return
     */
    public static OkHttpUtils initClient(OkHttpClient okHttpClient)
    {
        if (mInstance == null)
        {
            synchronized (OkHttpUtils.class)
            {
                if (mInstance == null)
                {
                    mInstance = new OkHttpUtils(okHttpClient);
                }
            }
        }
        return mInstance;
    }

    public static OkHttpUtils getInstance()
    {
        return initClient(null);
    }

    //拼装get参数
    protected String pingGetParams(StringBuilder url,Map<String, Object> params){
        if(url.indexOf("?") < 0){
            url.append('?');
        }

        StringBuilder parampart = new StringBuilder();
        Object temp = null;

        try {
            for (String name : params.keySet()) {
                temp = params.get(name);
                if (temp == null) {
                    continue;
                }

                parampart.append('&');
                parampart.append(URLEncoder.encode(String.valueOf(name), UTF_8));
                parampart.append('=');
                parampart.append(URLEncoder.encode(String.valueOf(temp), UTF_8));
            }
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException(e);
        }

        url.append(parampart);

        return url.toString().replace("?&", "?");
    }

    //拼装get参数
    protected String pingPostParams(Map<String, String> params){
        StringBuilder parampart = new StringBuilder();
        parampart.append("?");
        Object temp = null;

        try {
            for (String name : params.keySet()) {
                temp = params.get(name);
                if (temp == null) {
                    continue;
                }

                parampart.append('&');
                parampart.append(String.valueOf(name));
                parampart.append('=');
                parampart.append(String.valueOf(temp));
            }
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }

        return parampart.toString().replace("?&", "");
    }

    public Call postWrapper(String data,String url){
        Call call;
        MediaType MEDIA_TYPE_NORAML_FORM = MediaType.parse("application/x-www-form-urlencoded;charset=utf-8");
        RequestBody requestBody=RequestBody.create(MEDIA_TYPE_NORAML_FORM,data);
        Request requestPost=new Request.Builder().url(url).post(requestBody).build();
        call =  mOkHttpClient.newBuilder()
                .connectTimeout(60 * 1000, TimeUnit.SECONDS)
                .readTimeout(60 * 1000, TimeUnit.SECONDS)
                .writeTimeout(60 * 1000, TimeUnit.SECONDS)
                .build()
                .newCall(requestPost);
        return call;
    }

    /**
     * post带参数请求数据
     * @param map 参数
     * @param url 网络请求地址
     * @return
     */
    public Call post(Map<String,String> map,String url){
        if(false){
            return postWrapper(pingPostParams(map),url);
        }
        Call call;
        /**
         * 创建请求的参数body
         */
        FormBody.Builder builder = new FormBody.Builder();
        if(map == null || map.isEmpty()){
            //没有参数情况
        }else {
            /**
             * 遍历参数
             */
            for(String key : map.keySet()){
                builder.add(key,map.get(key));
            }
        }
        RequestBody body = builder.build();
        Request request = new Request.Builder()
                .url(url)
                //.addHeader("Connection","close")
                .post(body)
                .build();
        call =  mOkHttpClient.newBuilder()
                .connectTimeout(60 * 1000, TimeUnit.SECONDS)
                .readTimeout(60 * 1000, TimeUnit.SECONDS)
                .writeTimeout(60 * 1000, TimeUnit.SECONDS)
                .build()
                .newCall(request);
        return call;
//        return mOkHttpClient.newCall(request);
    }

    public Call postJson(String json,String url){
        Call call;
        //MediaType  设置Content-Type 标头中包含的媒体类型值
        RequestBody body = FormBody.create(MediaType.parse("application/json; charset=utf-8") , json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        call =  mOkHttpClient.newBuilder()
                .connectTimeout(60 * 1000, TimeUnit.SECONDS)
                .readTimeout(60 * 1000, TimeUnit.SECONDS)
                .writeTimeout(60 * 1000, TimeUnit.SECONDS)
                .build()
                .newCall(request);
        return call;
    }

    /**
     * post带参数请求数据
     * @param map 参数
     * @param url 网络请求地址
     * @return
     */
    public Call postMongoDB(Map<String,String> map,String url){
        Call call;
        /**
         * 创建请求的参数body
         */
        FormBody.Builder builder = new FormBody.Builder();
        if(map == null || map.isEmpty()){
            //没有参数情况
        }else {
            /**
             * 遍历参数
             */
            builder.add("params", ResponseJson.objectToJson(map));
        }
        RequestBody body = builder.build();
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        call =  mOkHttpClient.newBuilder()
                .connectTimeout(60 * 1000, TimeUnit.SECONDS)
                .readTimeout(60 * 1000, TimeUnit.SECONDS)
                .writeTimeout(60 * 1000, TimeUnit.SECONDS)
                .build()
                .newCall(request);
        return call;
//        return mOkHttpClient.newCall(request);
    }

    /**
     * post 上传多个文件以及带参数到服务器
     * @param file
     * @param map
     * @param url
     * @return
     */
    public Call postFileFrom(File file,Map<String, String> map, String url){
        Call call;
        MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        if (file == null){
            //没有文件或者说文件为空的情况
        }else {
            // MediaType.parse() 里面是上传的文件类型。
            RequestBody body = RequestBody.create(MediaType.parse("application/octet-stream"), file);
            // 参数分别为， 请求key ，文件名称 ， RequestBody
            requestBody.addFormDataPart("file", file.getName(), body);
        }
        if(map == null || map.isEmpty()){
            //没有参数情况
        }else {
            /**
             * 遍历参数
             */
            for(String key : map.keySet()){
                requestBody.addFormDataPart(key,map.get(key));
            }
        }

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody.build())
                .build();
        call =  mOkHttpClient.newBuilder()
                .connectTimeout(60 * 1000, TimeUnit.SECONDS)
                .readTimeout(60 * 1000, TimeUnit.SECONDS)
                .writeTimeout(60 * 1000, TimeUnit.SECONDS)
                .build()
                .newCall(request);
        return call;
//        return mOkHttpClient.newCall(request);
    }

    /**
     * get带参数
     * @param entities 参数的key 和value值
     * @param url 数据请求的url
     * @return
     */
    public Call getFrom(List<OkHttpEntity> entities, String url){
        Call call;
        String buildUrl;
        StringBuilder sb = new StringBuilder();
        if (entities == null || entities.isEmpty()){
            buildUrl = url;
        }else {
            /**
             * 遍历参数
             */
            for (int i = 0; i < entities.size(); i++) {
                if (i == 0){
                    sb.append("?" + entities.get(i).getKey() + "=" + entities.get(i).getValue());
                }else {
                    sb.append("&" + entities.get(i).getKey() + "=" + entities.get(i).getValue());
                }
            }
            buildUrl = (url + sb.toString());
        }
        Log.e("buildUrl",buildUrl);
        Request request = new Request.Builder()
                .url(buildUrl)
                .get()
                .build();
        call =  mOkHttpClient.newBuilder()
                .connectTimeout(60 * 1000, TimeUnit.SECONDS)
                .readTimeout(60 * 1000, TimeUnit.SECONDS)
                .writeTimeout(60 * 1000, TimeUnit.SECONDS)
                .build()
                .newCall(request);
        Log.e("AsynModelImp", "getFrom: " + mOkHttpClient.connectTimeoutMillis());
//        return mOkHttpClient.newCall(request);
        return call;
    }

    /**
     * 单文件下载
     * @param imgUrl 文件地址
     * @return
     */
    public Call downLoad(String imgUrl){
        Request request=new Request.Builder()
                .get()
                .url(imgUrl)
                .build();
        return mOkHttpClient.newCall(request);
    }

}
