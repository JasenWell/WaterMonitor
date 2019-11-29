package com.hjh.baselib.entity;

/**
 * Created by zlang on 2018/1/2.
 */

public class OkHttpEntity {

    public OkHttpEntity(String key,String value){
        this.key = key;
        this.value = value;
    }

    private String key;
    private String value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
