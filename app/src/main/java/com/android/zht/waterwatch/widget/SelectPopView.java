package com.android.zht.waterwatch.widget;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.android.zht.waterwatch.R;
import com.hjh.baselib.widget.PopView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by hjh on 2015/8/31.
 */

public class SelectPopView extends PopView{

    @BindView(R.id.top_view)
    View mTop;

    @BindView(R.id.container)
    LinearLayout mContainer;

    private OnClickPopView mListener;

    public SelectPopView(Activity owner, int width, int height,OnClickPopView listener){
        super(owner, width, height);
        mListener = listener;

        this.setClippingEnabled(false);
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.popupAnimation);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
    }

    @Override
    public int getContentLayout() {
        return R.layout.bottom_pop_view_layout;
    }

    public void show(){
        showAtLocation(mOwner.getWindow().getDecorView(), Gravity.BOTTOM| Gravity.CENTER_HORIZONTAL,0, 0);
    }

    public void addChild(boolean area){
        mContainer.removeAllViews();
        for(int i = 0; i < 10;i++) {
            View view = mInfalter.inflate(R.layout.item_area,null);
            TextView textView = view.findViewById(R.id.item_name);
            if(!area){
                textView.setGravity(Gravity.CENTER);
            }
            textView.setText("选项"+(i+1));
            textView.setOnClickListener(new SwitchListener(i));
            mContainer.addView(view);
        }
    }


    @OnClick({R.id.top_view})
    public void onViewClick(View v) {
       if(v.getId() == R.id.top_view){
            dismiss();
        }
    }

    public interface OnClickPopView{
        void onClickPop(int index);
    }

    public class SwitchListener implements  View.OnClickListener{

        private int index;

        public SwitchListener(int index ){
            this.index = index;
        }

        @Override
        public void onClick(View v) {
            mListener.onClickPop(index);
            dismiss();
        }
    }
}
