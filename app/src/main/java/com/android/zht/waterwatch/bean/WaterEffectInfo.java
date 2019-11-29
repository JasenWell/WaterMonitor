package com.android.zht.waterwatch.bean;

import java.io.Serializable;
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
