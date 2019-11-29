package com.hjh.baselib.widget;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hjh.baselib.R;
import com.hjh.baselib.constants.ModuleConfig;

/**
 * 各式加载框
 */

public class BasicDialog extends LBasicDialog {

    private TextView hintView;

    public BasicDialog(Activity context, int theme){
        super(context, theme);
        LinearLayout layout = findViewById(R.id.layout);
        hintView = findViewById(R.id.tv_load_dialog);
        int size = mContext.getResources().getDimensionPixelSize(R.dimen.margin_normal_size)*15;
        layout.getLayoutParams().width = size;
        layout.getLayoutParams().height = size;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        init(getContext());
    }

    private void init(Context context){
        //设置不可取消，点击其他区域不能取消，实际中可以抽出去封装供外包设置
        setCancelable(false);
        setCanceledOnTouchOutside(false);

//        setContentView(R.layout.loading_dialog);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(params);
    }

    public void setHintText(String hint){
        hintView.setText(hint);
    }

    @Override
    public void show()
    {
        super.show();
    }

    @Override
    public int getContentLayout() {
        return R.layout.loading_dialog;
    }
}
