package com.hjh.baselib.listener;

import android.os.Bundle;

/**
 * Created by zlang on 2017/12/22.
 */

public interface BasicViewInterface {

    /**
     * 资源文件xml
     * @return
     */
    int getContentLayout();

    void onAfterCreate(Bundle savedInstanceState);

    /**
     * 标题定制改变
     */
    void onLoadDefaultTitle();

    /**
     * 加载默认数据，从数据库查询
     */
    void onLoadDefaultData();

    /**
     * 根据查询的数据加载view
     * @param savedInstanceState
     */
    void onLoadDefaultView(Bundle savedInstanceState);


    void onRefreshView();
}
