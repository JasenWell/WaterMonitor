package com.android.zht.waterwatch.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.Spanned;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;



/**
 * 展示选项栏
 * @author hjh
 * 2016-5-20下午11:30:59
 */
import com.android.zht.waterwatch.R;
import com.hjh.baselib.constants.ModuleConfig;
import com.hjh.baselib.widget.BaseLinearLayout;
import com.hjh.baselib.widget.ShaderImageView;

import butterknife.BindView;
import butterknife.OnClick;

public class ShowItemLayout extends BaseLinearLayout {

	@BindView(R.id.bg_view)
	ImageView mBgView;//背景
	
	@BindView(R.id.item_icon)
	ImageView mIconView;

	@BindView(R.id.item_title)
	TextView mTitleView;
	
	@BindView(R.id.item_content)
	TextView mContentView;
	
	@BindView(R.id.item_next)
	ImageView mNextView;
	
	@BindView(R.id.item_close)
	ImageView mCloseView;
	
	@BindView(R.id.right_icon)
	ShaderImageView mRightIcon;
	
	@BindView(R.id.item_layout)
	RelativeLayout mLayout;//点击事件及背景改变,onClick="this"
	
	@BindView(R.id.item_line1)
    View mLine1;
    
    @BindView(R.id.item_line2)
    View mLine2;
	
	private OnClickItemListener listener;
	private int flag;//点击事件宿主的标记

	@Override
	public int getContentLayout() {
		return R.layout.item_public_show_layout ;
	}

	public ShowItemLayout(Context context){
		this(context, null);
	}
	
	public ShowItemLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.Item_style);
		Drawable icon= array.getDrawable(R.styleable.Item_style_item_icon);
		Drawable rightIcon= array.getDrawable(R.styleable.Item_style_item_right_icon);
		if(icon != null){
			mIconView.setImageDrawable(icon);
			mIconView.setVisibility(View.VISIBLE);
		}else{
			mIconView.setVisibility(View.GONE);
		}
		
		if(rightIcon != null){
			mNextView.setImageDrawable(rightIcon);
		}
		
		int mode = array.getInt(R.styleable.Item_style_item_show_line_mode,1);
        if(mode == 2){
        	mLine1.setVisibility(View.VISIBLE);
        	mLine2.setVisibility(View.GONE);
        }else if(mode == 1){
        	mLine1.setVisibility(View.GONE);
        	mLine2.setVisibility(View.VISIBLE);
		}else if(mode == 0){
        	mLine1.setVisibility(View.GONE);
        	mLine2.setVisibility(View.GONE);
		}
        
        int color = array.getColor(R.styleable.Item_style_item_content_color, 0);
        if(color != 0){
        	mContentView.setTextColor(color);
        }
        
		setTitle(array.getString(R.styleable.Item_style_item_title));
		setContent(array.getString(R.styleable.Item_style_item_hint));
		setNextVisiable(array.getBoolean(R.styleable.Item_style_item_show_next,true) ? View.VISIBLE : View.GONE);
		array.recycle();
	}

	public ShowItemLayout setLine2Margin(int margin){
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mLine2.getLayoutParams();
		params.leftMargin = params.rightMargin = margin;
		return this;
	}
	
	public ShowItemLayout setTitle(String text){
		mTitleView.setText(text);
		return this;
	}
	
	public ShowItemLayout setTitle(Spanned text){
		mTitleView.setText(text);
		return this;
	}
	
	public ShowItemLayout setTitleColor(int color){
		mTitleView.setTextColor(getAppColor(color));
		return this;
	}
	
	public String getTitle(){
		return mTitleView.getText().toString();
	}

	public TextView getTitleView(){
		return mTitleView;
	}

	public ShowItemLayout setContentColor(int color){
		mContentView.setTextColor(getAppColor(color));
		return this;
		
	}
	public ShowItemLayout setContent(String text){
		mContentView.setText(text);
		return this;
	}

	public String getContent(){
		return mContentView.getText().toString();
	}
	
	public ShowItemLayout setTextGravity(){
		mContentView.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
		return this;
	}

	public ShowItemLayout setContentMargin(int margin){
		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mContentView.getLayoutParams();
		params.leftMargin = margin;
		return this;
	}
	
	public ShowItemLayout setTextWidth(int width){
		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mTitleView.getLayoutParams();
		params.width = width;
		return this;
	}
	
	public void setNextVisiable(int visibility){
		mNextView.setVisibility(visibility);
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag,OnClickItemListener listener) {
		this.flag = flag;
		this.listener = listener;
	}
	
	public ShaderImageView getmRightIcon() {
		return mRightIcon;
	}

	public ShowItemLayout resetLayoutPadding(int left,int top,int right,int bottom){
		mLayout.setPadding(left,top,right,bottom);
		return this;
	}
	
	public ShowItemLayout resetBgViewHeight(){
		mLayout.measure(ModuleConfig.SCREEN_WIDTH,ModuleConfig.SCREEN_HEIGHT);
		int height = mLayout.getMeasuredHeight();
		mBgView.getLayoutParams().height = height;
		return this;
	}
	
	public ShowItemLayout setCloseOnClickListener(int resId,View.OnClickListener listener){
		mCloseView.setImageResource(resId);
		mCloseView.setOnClickListener(listener);
		return this;
	}
	
	public ShowItemLayout setCloseViewVisibility(int visibility){
		mCloseView.setVisibility(visibility);
		return this;
	}
	
	public ImageView getBgView(){
		return mBgView;
	}
	
	public ShowItemLayout setNullBg(){
		mLayout.setBackgroundDrawable(null);
		return this;
	}

	@OnClick({R.id.item_layout})
	public void onViewClicked(View view) {
		if(view.getId() == R.id.item_layout){
			if(listener != null){
				listener.onClickItem(flag);
			}
		}
	}
	
	public interface OnClickItemListener{
		void onClickItem(int flag);
	}
	
	
}
