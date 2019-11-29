package com.android.zht.waterwatch.bean;

import java.io.Serializable;


/**
 * @date    on 2019/4/2
 * @author  hjh
 * @org     hjh
 * @version
 * @description 区域信息
 */
public class AreaInfo implements Serializable {
    private int id;
    private int schoolId;
    private String name;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
