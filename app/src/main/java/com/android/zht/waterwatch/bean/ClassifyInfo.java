package com.android.zht.waterwatch.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


/**
 * @date    on 2019/4/1
 * @author  hjh
 * @org     hjh
 * @version
 * @description 添加描述
 */
public class ClassifyInfo implements Serializable {
    private int id;
    @SerializedName("typename")
    private String typeName;
    private float  typeProportion;
    private float todaydata;
    private float lastdata;

    public ClassifyInfo() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public float getTypeProportion() {
        return typeProportion;
    }

    public void setTypeProportion(float typeProportion) {
        this.typeProportion = typeProportion;
    }

    public float getTodaydata() {
        return todaydata;
    }

    public void setTodaydata(float todaydata) {
        this.todaydata = todaydata;
    }

    public float getLastdata() {
        return lastdata;
    }

    public void setLastdata(float lastdata) {
        this.lastdata = lastdata;
    }
}
