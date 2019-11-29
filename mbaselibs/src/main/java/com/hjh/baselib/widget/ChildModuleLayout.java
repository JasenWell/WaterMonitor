package com.hjh.baselib.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hjh.baselib.R;



public class ChildModuleLayout extends LinearLayout{

    private ImageView leftImageView;
    private TextView leftTextView;
    private TextView rightTextView;
    private RelativeLayout titleLayout;
    private LinearLayout container;
    private View bottomLineView;

    public ChildModuleLayout(Context context){
        this(context, null);
    }

    public ChildModuleLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater mInflater = LayoutInflater.from(context);
        View myView = mInflater.inflate(R.layout.public_child_module_layout, null);
        leftImageView = (ImageView) myView.findViewById(R.id.icon);
        leftTextView = (TextView) myView.findViewById(R.id.left_text);
        rightTextView = (TextView) myView.findViewById(R.id.right_text);
        titleLayout = (RelativeLayout) myView.findViewById(R.id.title_layout);
        container = (LinearLayout) myView.findViewById(R.id.container);
        bottomLineView = (View) findViewById(R.id.bottom_line);
        addView(myView);
    }

    public void setLeftImageView(int resId){
        leftImageView.setImageResource(resId);
    }

    public void setLeftText(String text){
        leftTextView.setText(text);
    }

    public void setRightText(String text){
        rightTextView.setText(text);
    }

    public void setRightVisible(int visiable){
        rightTextView.setVisibility(visiable);
    }

    public void setLeftTextColor(int textColor){
        leftTextView.setTextColor(getResources().getColor(textColor));
    }

    public void setRightTextColor(int textColor){
        rightTextView.setTextColor(getResources().getColor(textColor));
    }

    public void setRightImageView(int resId){
        Drawable drawable = getResources().getDrawable(resId);
        // 这一步必须要做,否则不会显示.
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        rightTextView.setCompoundDrawables(null,null,drawable,null);
    }

    /**
     * 标题点击监听
     */
    public void setClickTitleListener(View.OnClickListener onClickListener){
        titleLayout.setOnClickListener(onClickListener);
    }

    public LinearLayout getContainer() {
        return container;
    }

    public void setContainer(LinearLayout container) {
        this.container = container;
    }

    public ImageView getLeftImageView() {
        return leftImageView;
    }

    public void setLeftImageView(ImageView leftImageView) {
        this.leftImageView = leftImageView;
    }

    public View getBottomLineView() {
        return bottomLineView;
    }

    public void setBottomLineView(View bottomLineView) {
        this.bottomLineView = bottomLineView;
    }

    public TextView getLeftTextView() {
        return leftTextView;
    }

    public void setLeftTextView(TextView leftTextView) {
        this.leftTextView = leftTextView;
    }

    public TextView getRightTextView() {
        return rightTextView;
    }

    public void setRightTextView(TextView rightTextView) {
        this.rightTextView = rightTextView;
    }

    public RelativeLayout getTitleLayout() {
        return titleLayout;
    }

    public void setTitleLayout(RelativeLayout titleLayout) {
        this.titleLayout = titleLayout;
    }

}
