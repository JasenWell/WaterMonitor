package com.android.zht.waterwatch.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.zht.waterwatch.R;
import com.android.zht.waterwatch.widget.AppTitleLayout;
import com.hjh.baselib.base.LBaseActivity;
import com.hjh.baselib.utils.AppPresences;
import com.hjh.baselib.utils.StringTools;


import butterknife.BindView;
import butterknife.OnClick;

/**
 * Description:
 * Created by hjh on 2017/8/19.
 */
public class ServerConfigActivity extends LBaseActivity {

    @BindView(R.id.title_bar)
    AppTitleLayout mTitleLayout;

    @BindView(R.id.server_ip)
    EditText ipView;

    @BindView(R.id.server_port)
    EditText portView;

    @BindView(R.id.btn_config)
    Button btn;

    @Override
    public int getContentLayout() {
        return R.layout.activity_config_server;
    }

    @Override
    public void onLoadDefaultTitle() {
        super.onLoadDefaultTitle();
        mTitleLayout.setTitleText("服务器配置");
        mTitleLayout.enableLeftButton();
        mTitleLayout.setTitleClickListener(this);
        ipView.setText(AppPresences.getInstance().getString("server_ip","139.199.165.130"));
        portView.setText(AppPresences.getInstance().getString("server_port","9092"));
    }

    @OnClick({R.id.btn_config})
    public void onViewClicked(View v) {
        if(v.getId() == R.id.btn_config){
            String ip = ipView.getText().toString();
            String port = portView.getText().toString();
            if(StringTools.isEmpty(ip) || StringTools.isEmpty(port)){
                showToast("ip和端口号不能为空");
                return;
            }

//            HttpHelper.WEB_HOST = "http://"+ip+":"+port;
//            mPresenceManager.putString("server_ip",ip);
//            mPresenceManager.putString("server_port",port);
//            mPresenceManager.putString(HttpHelper.ACTION.KEY_IP,HttpHelper.WEB_HOST);
            back(mActivity);
        }
    }
}
