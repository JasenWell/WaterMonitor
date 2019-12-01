package com.android.zht.waterwatch.ui;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.zht.waterwatch.R;
import com.android.zht.waterwatch.bean.UserInfo;
import com.android.zht.waterwatch.fragment.ConsumeFragment;
import com.android.zht.waterwatch.fragment.HomeFragment;
import com.android.zht.waterwatch.fragment.HomeReplaceFragment;
import com.android.zht.waterwatch.fragment.MineFragment;
import com.android.zht.waterwatch.fragment.WarningFragment;
import com.android.zht.waterwatch.fragment.WarningReplaceFragment;
import com.android.zht.waterwatch.widget.TabBottomLayout;
import com.hjh.baselib.base.LBaseActivity;
import com.hjh.baselib.base.LBaseFragment;
import com.hjh.baselib.constants.ModuleConfig;
import com.hjh.baselib.utils.AppPresences;

import butterknife.BindView;

/**
 * Description:图标灰色调cdcdcd  底部bfbfbf
 * Created by hjh on 2019/3/25.
 */
public class MainReplaceActivity extends MainActivity implements View.OnClickListener {

    @BindView(R.id.buttonLayout)
    LinearLayout mContainer;

    @BindView(R.id.main_layout)
    RelativeLayout mainLayout;

    @BindView(R.id.hint_layout)
    LinearLayout hintLayout;


    private FragmentManager fragmentManager = getSupportFragmentManager();
    private LBaseFragment[] fragments;
    private int mCurrentIndex = 0;
    private int mTemp = 0;
    private long enterTime;
    private int count = 0;
    private boolean first = true;//是否首次进入
    private UserInfo userInfo;
    private boolean fromWebview = false;

    @Override
    protected void onProcessData() {
        userInfo = findObject("user_info",UserInfo.class);
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);// 必须要调用这句
    }

    @Override
    public void onLoadDefaultView(Bundle savedInstanceState) {
        addButtons();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(first && (!AppPresences.getInstance().getString(ModuleConfig.KEY_TOKEN).equals(""))){
            //首次进入且token有效
            first = false;
        }else if (AppPresences.getInstance().getString(ModuleConfig.KEY_TOKEN).equals("")) {
            return;
        }

        if(mTemp == 0 || fromWebview )return;
        showFragmentByIndex(mTemp,false);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onClick(View v) {
        mTemp = (int) v.getTag();
        showFragmentByIndex(mTemp, false);
    }

    private void addButtons(){
        FragmentTransaction ft = fragmentManager.beginTransaction();
        mContainer.removeAllViews();
        if(fragments != null){
            for(Fragment fragment : fragments){
                ft.remove(fragment);
            }
            fragments = null;
        }
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.WRAP_CONTENT);
        params.weight = 1;
        fragments = new LBaseFragment[3];
        fragments[0] = new HomeReplaceFragment().setOwner(this).setUserInfo(userInfo);
        fragments[1] = new WarningReplaceFragment().setOwner(this);
        fragments[2] = new MineFragment().setOwner(this);

        for(int i = 0;i < fragments.length ;i++){
            TabBottomLayout mTabBottomLayout = new TabBottomLayout(this,i,fragments.length);
            mTabBottomLayout.setPadding(0,15,0,10);
            mTabBottomLayout.setTextMargin(5);
            mTabBottomLayout.setTextSize(10).setTextColor(getAppColor(R.color.black));
            mTabBottomLayout.setTag(i);
            mTabBottomLayout.setOnClickListener(this);
            mContainer.addView(mTabBottomLayout, params);
            fragments[i].setBaseLayout(mTabBottomLayout);
        }

        fragments[mCurrentIndex].getBaseLayout().setCheck(true);
        fragments[1].getBaseLayout().setCheck(false);
        fragments[2].getBaseLayout().setCheck(false);
        ft.add(R.id.container, fragments[mCurrentIndex]);

//        ft.show(fragments[mCurrentIndex]).commit();
        ft.show(fragments[mCurrentIndex]).commitAllowingStateLoss();//commit()抛异常
        mContainer.measure(ModuleConfig.SCREEN_WIDTH, ModuleConfig.SCREEN_HEIGHT);
        ModuleConfig.TAB_HEIGHT = mContainer.getMeasuredHeight();
    }

    /**
     * 根据按下按钮的下标显示fragment
     * @param index 下标
     */
    private synchronized void showFragmentByIndex(int index,boolean refresh) {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        if (mCurrentIndex != index) {

            fragments[mCurrentIndex].getBaseLayout().setCheck(false);
            ft.hide(fragments[mCurrentIndex]);
            if (!fragments[index].isAdded()) {
                ft.add(R.id.container, fragments[index]);
            }

            ft.show(fragments[index]).commitAllowingStateLoss();//commit()抛异常
            mCurrentIndex = index;
            fragments[mCurrentIndex].getBaseLayout().setCheck(true);
            if (mCurrentIndex == 0) {
                fragments[mCurrentIndex].onRefreshView();
            }else if (mCurrentIndex == 1){
                Intent intent = new Intent(mActivity,WebViewActivity.class);
                intent.putExtra("url","http://148.70.97.197:9088/GIS/mapgis.html?sid=1006578&souid=1006578&datasourceid="+userInfo.getGisurl());
                startActivityWithAnim(mActivity,intent);
            }
        }else {

        }
    }

    @Override
    public void refreshHome() {
        super.refreshHome();
        fromWebview = true;
        showFragmentByIndex(0,false);
    }
}
