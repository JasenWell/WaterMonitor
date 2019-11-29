package com.android.zht.waterwatch.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @date    on 2019/4/1
 * @author  hjh
 * @org     hjh
 * @version
 * @description 单位信息
 */

public class DepartmentInfo implements Serializable{

    private int id;
    private int userId;
    private String name;//单位名称
    private SchoolInfo schoolInfo;// 普通用户返回，否则为null
    private List<SchoolInfo> schoolList;

    public DepartmentInfo() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SchoolInfo getSchoolInfo() {
        return schoolInfo;
    }

    public void setSchoolInfo(SchoolInfo schoolInfo) {
        this.schoolInfo = schoolInfo;
    }

    public List<SchoolInfo> getSchoolList() {
        return schoolList;
    }

    public void setSchoolList(List<SchoolInfo> schoolList) {
        this.schoolList = schoolList;
    }
}
