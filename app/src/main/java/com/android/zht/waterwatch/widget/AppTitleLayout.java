package com.android.zht.waterwatch.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.android.zht.waterwatch.R;
import com.hjh.baselib.listener.ITitleClickListener;
import com.hjh.baselib.widget.BaseLinearLayout;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * 标题栏
 * @author hjh
 * 2016-5-20下午11:14:06
 */
public class AppTitleLayout extends BaseLinearLayout {
	@BindView(R.id.back_layout)
	LinearLayout leftLayout;

	@BindView(R.id.back_view)
	TextView leftView;
	
	@BindView(R.id.head_top_bar_right)
	FrameLayout rightLayout;

	@BindView(R.id.right_text)
	TextView rightTextView;

	@BindView(R.id.right_view)
	ImageView rightImageView;
	
	@BindView(R.id.head_top_bar_title)
	TextView titleView;

	@BindView(R.id.flag_view)
	ImageView flagImageView;

	@BindView(R.id.title_layout)
	LinearLayout titleLayout;
	
	@BindView(R.id.head_top_bar_layout)
	RelativeLayout layout;
	
	@BindView(R.id.head_top_bar_nearright)
	TextView nearrightView;

	private ITitleClickListener titleClickListener;
	
	public AppTitleLayout(Context context){
		this(context, null);
	}
	
	public AppTitleLayout(Context context, AttributeSet attrs) {
		super(context, attrs);

	}

	@Override
	public int getContentLayout() {
		return R.layout.item_title_layout;
	}

	@OnClick({R.id.back_layout,R.id.head_top_bar_right,R.id.head_top_bar_nearright})
    public void onViewClicked(View view) {
        if(null == titleClickListener){
            return;
        }

        if(view.getId() == R.id.back_layout){
            titleClickListener.onClickLeftView();
        }else if (view.getId() == R.id.head_top_bar_right) {
            titleClickListener.onClickRightView();
        }else if (view.getId() == R.id.head_top_bar_nearright) {
            titleClickListener.onClickOtherView();
        }
    }

	public final void setOnClickFlagListener(View.OnClickListener listener){
		if(titleLayout != null){
			titleLayout.setOnClickListener(listener);
		}
	}

	public void showFlagView(int visibility){
		flagImageView.setVisibility(visibility);
	}
	
	/**
	 * 设置标题背景
	 */
	public void setBackground(int resid){
		layout.setBackgroundResource(resid);
	}
	
	/**
	 * 设置标题
	 * @param text
	 */
	public void setTitleText(String text){
		
		if(titleView != null){
			titleView.setText(text);
		}
	}

	public String getTitleText(){

		if(titleView != null){
			return titleView.getText().toString();
		}

		return null;
	}
	
	public void setTitleText(int id){
		if(titleView != null){
			titleView.setText(getResources().getString(id));
		}
	}
	
	/**
	 * 设置标题颜色
	 * @param color
	 */
	public void setTitleTextColor(int color){
		
		if(titleView != null){
			titleView.setTextColor(getAppColor(color));
		}
	}
	
	/**
	 * 左按钮不可见
	 */
	public final void disableLeftButton(){
		
		if(leftLayout != null){
			leftLayout.setVisibility(INVISIBLE) ;
		}
	}
	
	public final void enableLeftButton(){
		
		if(leftView != null){
			leftView.setText(null) ;
			leftLayout.setVisibility(VISIBLE) ;
		}
	}

	public TextView getLeftView(){

		return leftView;
	}

	public final void enableLeftButtonImage(int rid){
	
		if(leftView != null){
			leftView.setVisibility(VISIBLE);
			leftView.setBackgroundResource(rid) ;
		}
	}
	
	/**
	 * 左按钮仅为文本
	 * @param text
	 */
	@SuppressWarnings("deprecation")
	public final void enableLeftButtonText(String text){
		
		if(leftView != null){
			leftView.setVisibility(VISIBLE);
			leftView.setBackgroundDrawable(null);
			leftView.setText(text) ;
		}
		
	}
	
	public final void setLeftButtonPaading(int left, int top, int right, int bottom){
		
		if(leftView != null){
			leftView.setPadding(left, top, right, bottom);
		}
	}
	
	public final void setLeftButtonMargin(int marginLeft){
		
		if(leftView != null){
			RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) leftView.getLayoutParams();
			params.leftMargin = marginLeft;
			leftView.setLayoutParams(params);
		}
	}
	
	public final void setRightButtonPadding(int left, int top, int right, int bottom) {
		
		if(rightTextView != null){
			rightTextView.setPadding(left, top, right, bottom);
		}
	}
	
	public final void setRightButtonMargin(int marginRight){
		
		if(rightLayout != null){
			RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) rightLayout.getLayoutParams();
			params.rightMargin = marginRight;
			rightLayout.setLayoutParams(params);
		}
	}
	
	
	public final void disableRightButton(){
		endableRightButton(INVISIBLE) ;
	}
	
	public final void enableRightButton(){
		endableRightButton(VISIBLE) ;
	}
	
	private final void endableRightButton(int show){
		
		if(rightLayout != null){
			rightLayout.setVisibility(show) ;
		}
		
	}
	
	public final void enableRightButtonText(int rid){
		enableRightButtonText(mContext.getString(rid)) ;
	}
	
	/**
	 * 右按钮仅为文本
	 * @param text
	 */
	@SuppressWarnings("deprecation")
	public final void enableRightButtonText(String text){
		
		if(rightTextView != null && rightLayout != null){
			rightLayout.setVisibility(VISIBLE);
			rightTextView.setBackgroundDrawable(null);
			rightTextView.setText(text) ;
			rightImageView.setVisibility(INVISIBLE);
		}
		
	}
	
	public final void setRightImageViewBg(int resid){
		
		if(rightImageView != null && rightLayout != null){
			rightLayout.setVisibility(VISIBLE);
			rightImageView.setVisibility(VISIBLE);
			rightTextView.setVisibility(INVISIBLE);
			rightImageView.setImageResource(resid);
		}
		
	}

	public final void setRightTextViewBg(int resid){

		if(rightLayout != null){
			rightLayout.setVisibility(VISIBLE);
			rightImageView.setVisibility(INVISIBLE);
			rightTextView.setVisibility(VISIBLE);
			rightTextView.setBackgroundResource(resid);
		}

	}
	
	/**
	 * 设置右按钮字体颜色
	 * @param color
	 */
	public final void setRightButtonTextColor(int color){
		if(rightTextView != null){
			rightTextView.setTextColor(getAppColor(color));
		}
	}
	
	/**
	 * 右按钮为图片
	 * @param rid
	 */
	public final void enableRightButtonImage(int rid){
		
		if(rightTextView != null && rightLayout != null){
			rightLayout.setVisibility(VISIBLE);
			rightTextView.setBackgroundResource(rid) ;
			rightTextView.setText("");
		}
	}

	public final void disableNearRightButton(){
		endableNearRightButton(INVISIBLE) ;
	}
	
	public final void enableNearRightButton(){
		endableNearRightButton(VISIBLE) ;
	}
	
	private final void endableNearRightButton(int show){
		
		if(nearrightView != null){
			nearrightView.setVisibility(show) ;
		}
		
	}
	
	public final void enableNearRightButtonText(int rid){
		enableRightButtonText(mContext.getString(rid)) ;
	}
	
	/**
	 * 靠右按钮仅为文本
	 * @param text
	 */
	@SuppressWarnings("deprecation")
	public final void enableNearRightButtonText(String text){
		
		if(nearrightView != null){
			nearrightView.setVisibility(VISIBLE);
			nearrightView.setBackgroundDrawable(null);
			nearrightView.setText(text) ;
		}
		
	}
	
	/**
	 * 设置靠右按钮字体颜色
	 * @param color
	 */
	public final void setNearRightButtonTextColor(int color){
		if(nearrightView != null){
			nearrightView.setTextColor(color);
		}
	}
	
	/**
	 * 靠右按钮为图片
	 * @param rid
	 */
	public final void enableNearRightButtonImage(int rid){
		
		if(nearrightView != null){
			nearrightView.setVisibility(VISIBLE);
			nearrightView.setBackgroundResource(rid) ;
			nearrightView.setText("");
		}
	}
	
	
	@Override
	public void onClick(View view) {

	}

	public ITitleClickListener getTitleClickListener() {
		return titleClickListener;
	}

	public void setTitleClickListener(ITitleClickListener titleClickListener) {
		this.titleClickListener = titleClickListener;
	}

}
