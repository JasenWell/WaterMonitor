package com.android.zht.waterwatch.widget;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.zht.waterwatch.R;
import com.hjh.baselib.widget.PopView;

import butterknife.BindView;
import butterknife.OnClick;

/**从容器顶部开始展示视图
 * Created by hjh on 2015/8/31.
 */

public class SelectTopPopView extends PopView{


    @BindView(R.id.container)
    LinearLayout mContainer;

    private OnClickPopView mListener;

    public SelectTopPopView(Activity owner, int width, int height, OnClickPopView listener){
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
        //https://blog.csdn.net/u013693649/article/details/49252963
    }

    @Override
    public int getContentLayout() {
        return R.layout.top_pop_view_layout;
    }

    public void show(){
        showAtLocation(mOwner.getWindow().getDecorView(), Gravity.BOTTOM| Gravity.CENTER_HORIZONTAL,0, 0);
    }

    public void show(View parent){
        showAsDropDown(parent, 0, 0);
    }

    public void addChild(int size){
        mContainer.removeAllViews();
        for(int i = 0; i < size;i++) {
            View view = mInfalter.inflate(R.layout.item_area,null);
            TextView textView = view.findViewById(R.id.item_name);
            textView.setGravity(Gravity.CENTER);
            textView.setText(mListener.getShowText(i));
            textView.setOnClickListener(new SwitchListener(i));
            mContainer.addView(view);
        }
    }

    public interface OnClickPopView{
        void onClickPop(int index);
        String getShowText(int index);
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
