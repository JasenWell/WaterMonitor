package com.android.zht.waterwatch.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.zht.waterwatch.R;
import com.hjh.baselib.constants.ModuleConfig;
import com.hjh.baselib.widget.BaseLinearLayout;

import butterknife.BindView;


/**
 * tab底部选项卡布局
 * @author hjh
 * 2016-5-21下午11:05:59
 */
public class TabBottomLayout extends BaseLinearLayout {
	
	@BindView(R.id.root_layout)
	LinearLayout mRootLayout;
	
	@BindView(R.id.layout)
	RelativeLayout mLayout;
	
	@BindView( R.id.tab_icon)
	ImageView mImageView;
	
	@BindView(R.id.tab_point)
	ImageView mPointView;
	
	@BindView(R.id.tab_number)
	TextView mNumberView;
	
	@BindView(R.id.tab_text)
	TextView mTextView;
	
	private int mPosition;
	private int normalId;
	private int selectId;
	private int total = 4;
	
	public TabBottomLayout(Context context, int position, int...length){
		this(context, null,length);
		this.mPosition = position;
		loadResource();
	}

	public TabBottomLayout(Context context, AttributeSet attrs, int...length) {
		super(context, attrs);
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mRootLayout.getLayoutParams();
		if(length.length != 0){
			total = length[0];
		}
		params.width = ModuleConfig.SCREEN_WIDTH / total;
	}

	@Override
	public int getContentLayout() {
		return R.layout.item_bottom_tab_layout;
	}
	
	private void loadResource(){
		if(mPosition >= getResources().getStringArray(R.array.tab_bottom_array).length){
			return;
		}
		TypedArray normalTypedArray = getResources().obtainTypedArray(R.array.tab_normal_image);
		normalId = normalTypedArray.getResourceId(mPosition, 0);
		TypedArray selectTypedArray = getResources().obtainTypedArray(R.array.tab_select_image);
		selectId = selectTypedArray.getResourceId(mPosition, 0);
		mImageView.setImageResource(normalId);
		mTextView.setText(getResources().getStringArray(R.array.tab_bottom_array)[mPosition]);
		mTextView.setVisibility(View.VISIBLE);
		mTextView.setTextColor(getAppColor(R.color.gray));
		normalTypedArray.recycle();
		selectTypedArray.recycle();
	}
	
	public TabBottomLayout setTextSize(int size){
		mTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,size);
		return this;
	}
	
	public TabBottomLayout setTextColor(int id){
		mTextView.setTextColor(id);
		return this;
	}
	
	public TabBottomLayout reloadResource(int iconID,int textID){
		TypedArray normalTypedArray = getResources().obtainTypedArray(iconID);
		normalId = normalTypedArray.getResourceId(mPosition, 0);
		TypedArray selectTypedArray = getResources().obtainTypedArray(iconID);
		selectId = selectTypedArray.getResourceId(mPosition, 0);
		mImageView.setImageResource(normalId);
		mTextView.setText(getResources().getStringArray(textID)[mPosition]);
		mTextView.setVisibility(View.VISIBLE);
		mTextView.setTextColor(getAppColor(R.color.text_second_color));
		normalTypedArray.recycle();
		selectTypedArray.recycle();
		return this;
	}
	
	public void resetIconSize(int width,int height){
		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mImageView.getLayoutParams();
//		params.width = width;
		params.height = height;
	}
	
	public void setCheck(boolean check){
		mImageView.setImageResource(check ? selectId : normalId);
		mTextView.setTextColor(getAppColor(check ? R.color.red : R.color.gray));
	}
	
	public void resetResId(int selectId,int normalId){
		this.selectId = selectId;
		this.normalId = normalId;
	}
	
	public void receivedMsg(boolean newMsg){
		mPointView.setVisibility(newMsg ? View.VISIBLE : View.INVISIBLE);
	}
	
	public void setNumber(int number){
		if(number == 0){
			mNumberView.setVisibility(View.INVISIBLE);
		}else {

			mNumberView.setVisibility(View.VISIBLE);
			if (number > 99) {
				mNumberView.setText("...");
			} else {
				mNumberView.setText(number + "");
			}
		}
	}

	public void showNumber(boolean show){
		mNumberView.setVisibility(show ? View.VISIBLE : INVISIBLE);
	}
	
	public void setRootPadding(int left,int top,int right,int bottom){
		mLayout.setPadding(left, top, right, bottom);
	}
	
	public RelativeLayout getLayout(){
		return mLayout;
	}
	
	public void setTextMargin(int topMargin){
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mTextView.getLayoutParams();
		params.topMargin = topMargin;
	}

}
