package com.android.zht.waterwatch.ui;


import android.os.Bundle;

import com.android.zht.waterwatch.R;
import com.android.zht.waterwatch.widget.AppTitleLayout;
import com.hjh.baselib.base.LBaseActivity;

import butterknife.BindView;

public class WarningDetailActivity extends LBaseActivity {
    @BindView(R.id.title_bar)
    AppTitleLayout mTitleLayout;

    @Override
    public int getContentLayout() {
        return R.layout.activity_warning_detail;
    }

    @Override
    public void onAfterCreate(Bundle savedInstanceState) {
        super.onAfterCreate(savedInstanceState);
        mTitleLayout.setTitleText("告警详情");
        mTitleLayout.enableLeftButton();
        mTitleLayout.setTitleClickListener(this);
    }
}
