package com.android.zht.waterwatch.bean;

import com.android.zht.waterwatch.util.DateUtil;
import com.google.gson.annotations.SerializedName;
import com.hjh.baselib.utils.DateTools;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.List;

/**
 * @date    on 2019/4/1
 * @author  hjh
 * @org     hjh
 * @version
 * @description 水效
 */

public class WaterEffectInfo implements Serializable {

    private int id;
    private int schoolId;
    private String date;
    @SerializedName("classifyInfo")
    private List<ClassifyInfo> classifyInfoList;

    public WaterEffectInfo() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(int schoolId) {
        this.schoolId = schoolId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<ClassifyInfo> getClassifyInfoList() {
        return classifyInfoList;
    }

    public void setClassifyInfoList(List<ClassifyInfo> classifyInfoList) {
        this.classifyInfoList = classifyInfoList;
    }

}
