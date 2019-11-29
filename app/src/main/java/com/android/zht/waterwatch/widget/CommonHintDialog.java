package com.android.zht.waterwatch.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.android.zht.waterwatch.R;
import com.hjh.baselib.constants.ModuleConfig;
import com.hjh.baselib.widget.LBasicDialog;

import butterknife.BindView;


/**
 * Descroption:
 * Created by hjh on 2016/5/5.
 */

public class CommonHintDialog extends LBasicDialog {
	@BindView(R.id.layout)
	LinearLayout mLayout;
	
	@BindView(R.id.notice_layout)
	LinearLayout mNoticeLayout;
	
	@BindView(R.id.notice_view)
	ImageView mNoticeView;
	
    @BindView(R.id.hint_title)
    TextView mTitle;

    @BindView(R.id.hint_text)
    TextView mContent;
    
    @BindView(R.id.hint_second_text)
    TextView mNextContent;

    @BindView(R.id.left_view)
    TextView mLeft;

    @BindView( R.id.right_view)
    TextView mRight;
    
    @BindView( R.id.input_edittext)
    EditText mEditText;

    @BindView(R.id.input_edittext_two)
    EditText mEditTextTwo;
    
    private String strFlag;
    private int intFlag;
    private OnClickHintDialogListener mListener;

    private View.OnClickListener listener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.left_view) {
                if(mListener.onClickItem(0)){
                	dismiss();
                }
            } else {
                if(mListener.onClickItem(1)){
                	dismiss();
                }
            }
        }
    };

    @Override
    public int getContentLayout() {
        return R.layout.dialog_common_hint_layout;
    }

    public CommonHintDialog(Activity activity, OnClickHintDialogListener listener) {
        super(activity, R.style.Dialog);
        this.mListener = listener;
        setLayoutWidth(ModuleConfig.SCREEN_WIDTH - mContext.getResources().getDimensionPixelSize(R.dimen.margin_normal_size)*8);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(root);
        setCanceledOnTouchOutside(true);
        mLeft.setOnClickListener(listener);
        mRight.setOnClickListener(listener);
    }

    public CommonHintDialog setTitle(String text){
        mTitle.setText(text);
        return this;
    }

    public CommonHintDialog setContent(String text){
        mContent.setText(text);
        return this;
    }
    
    public CommonHintDialog setContentGravity(int gravity){
        mContent.setGravity(gravity);
        return this;
    }

    public CommonHintDialog setLeftText(String text){
        mLeft.setText(text);
        return this;
    }

    public CommonHintDialog setRightText(String text){
        mRight.setText(text);
        return this;
    }
    
    public CommonHintDialog setTitleColor(int color){
    	mTitle.setTextColor(color);
    	return this;
    }
    
    public CommonHintDialog setContentColor(int color){
    	mContent.setTextColor(color);
    	return this;
    }
    
    public CommonHintDialog setContentTextSize(int size){
    	mContent.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
    	return this;
    }
    
    public CommonHintDialog setLeftTxtColor(int color){
        mLeft.setTextColor(color);
        return this;
    }
    
    public CommonHintDialog setRightTxtColor(int color){
        mRight.setTextColor(color);
        return this;
    }
    
    
    public CommonHintDialog setNextContentText(String text){
    	mNextContent.setText(text);
    	mNextContent.setVisibility(View.VISIBLE);
    	return this;
    }
    
    public CommonHintDialog setRightViewVisibility(int visibility){
        mRight.setVisibility(visibility);
        return this;
    }

    public CommonHintDialog setRightViewBg(int resid){
        mRight.setBackgroundResource(resid);
        return this;
    }
    
    public CommonHintDialog setContentMargin(int margin){
        LayoutParams params = (LayoutParams) mContent.getLayoutParams();
        params.bottomMargin = margin;
        return this;
    }
    
    /**
     * 此方法在左按钮gone情况下设置
     * @param margin
     * @return
     */
    public CommonHintDialog setRightViewMargin(int margin){
        LayoutParams params = (LayoutParams) mRight.getLayoutParams();
        params.leftMargin = params.rightMargin = margin;
        return this;
    }

    public CommonHintDialog setLeftViewVisibility(int visibility){
        mLeft.setVisibility(visibility);
        return this;
    }
    
    public CommonHintDialog setInputVisibility(int visibility){
    	mEditText.setVisibility(visibility);
        return this;
    }

    public CommonHintDialog setInputBottomVisibility(int visibility){
        mEditTextTwo.setVisibility(visibility);
        return this;
    }
    
    public CommonHintDialog setTitleVisibility(int visibility){
        mTitle.setVisibility(visibility);
        return this;
    }
    
    public CommonHintDialog setHintVisibility(int visibility){
        mContent.setVisibility(visibility);
        return this;
    }
    
    public CommonHintDialog setNoticeVisibility(int visibility){
        mNoticeLayout.setVisibility(visibility);
        return this;
    }
    
    public CommonHintDialog setNoticeResourse(int resid){
        mNoticeView.setImageResource(resid);
        return this;
    }
    
    public CommonHintDialog setLayoutWidth(int width){
        mLayout.getLayoutParams().width = width;
        return this;
    }
    
    public String getInputText(){
        return mEditText.getText().toString();
    }
    
    public CommonHintDialog setInputText(String text){
        mEditText.setText(text);
        return this;
    }
    
    public CommonHintDialog setInputHintText(String text){
    	mEditText.setHint(text);
        return this;
    }

    public CommonHintDialog setInputBackground(int resid){
        mEditText.setBackgroundResource(resid);
        return this;
    }

    public CommonHintDialog setInputBottomBackground(int resid){
        mEditTextTwo.setBackgroundResource(resid);
        return this;
    }

    public String getInputBottomText(){
        return mEditTextTwo.getText().toString();
    }

    public CommonHintDialog setInputBottomText(String text){
        mEditTextTwo.setText(text);
        return this;
    }

    public CommonHintDialog setInputBottomHintText(String text){
        mEditTextTwo.setHint(text);
        return this;
    }

    public String getStrFlag() {
        return strFlag;
    }

    public CommonHintDialog setStrFlag(String strFlag) {
        this.strFlag = strFlag;
        return this;
    }

    public int getIntFlag() {
        return intFlag;
    }

    public CommonHintDialog setIntFlag(int intFlag) {
        this.intFlag = intFlag;
        return this;
    }

    public interface OnClickHintDialogListener{
        boolean onClickItem(int index);
    }
}
