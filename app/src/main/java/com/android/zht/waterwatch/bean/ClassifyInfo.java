package com.android.zht.waterwatch.bean;

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
    private String typeName;
    private float  typeProportion;

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
}
