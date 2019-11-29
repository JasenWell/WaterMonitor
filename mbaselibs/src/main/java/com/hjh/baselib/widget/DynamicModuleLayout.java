package com.hjh.baselib.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.hjh.baselib.R;
import com.hjh.baselib.constants.ModuleConfig;
import com.hjh.baselib.listener.ITextDefaultColor;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author hjh
 *
 */
public final class DynamicModuleLayout extends BaseLinearLayout {

	String AndroidNamespace = "http://schemas.android.com/apk/res/android" ;

	private int availableWidth = 0;//可供容器使用的宽
	private int totalWidth = 0;//当前标签总宽
	
	private int marginLeft = 0;//容器距左
	private int marginRight = 0;//容器距右
	private int leftViewWidth = 0;//容器左边视图的宽
	private int rightViewWidth = 0;//容器右边视图的宽
	
	private int rowMinHeight = 0;//容器内每行布局最低高
	private int contentViewHeight = 0;//容器内每行布局内部视图的高
	
	private int contentViewMarginLeft = 0;//容器内每行布局内部视图的左间距
	private int contentViewMarginRight = 0;//容器内每行布局内部视图的右间距
	private int contentViewMarginTop = 0;//容器内每行布局内部视图的顶间距
	private int contentViewMarginBottom = 0;//容器内每行布局内部视图的底间距
	
	private int contentViewPaddingLeft = 0;//容器内每行布局内部视图的内容左间距
	private int contentViewPaddingRight = 0;//容器内每行布局内部视图的内容右间距
	private int contentViewPaddingTop = 0;//容器内每行布局内部视图的内容顶间距
	private int contentViewPaddingBottom = 0;//容器内每行布局内部视图的内容底间距
	private boolean flag = false;
	private boolean checkBackGround = false;//是否改变内容文本背景
	private int defaultId = 0;//内容文本的背景资源
	private int defaultCorlorId = 0;//默认颜色id
	
	private Context mContext;
	private OnclickModule onclickModule;
	private Map<Object, TextView> map = new HashMap<Object, TextView>();
	private boolean changeTextColor = false;
	private ITextDefaultColor defaultColor;
	private boolean multySelect = false;//是否多选
	private boolean select = false;//该模块是否已经被选中
	private List list;
	
	public DynamicModuleLayout(Context context){
		this(context, null);
	}
	
	public DynamicModuleLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		setOrientation(VERTICAL);
		mContext = context;
		TypedArray array = context.obtainStyledAttributes(attrs,R.styleable.DynamicLayout);
		marginLeft = array.getDimensionPixelSize(R.styleable.DynamicLayout_margin_left, 0);
		marginRight = array.getDimensionPixelSize(R.styleable.DynamicLayout_margin_right, 0);
		leftViewWidth = array.getDimensionPixelSize(R.styleable.DynamicLayout_left_view_width, 0);
		rightViewWidth = array.getDimensionPixelSize(R.styleable.DynamicLayout_right_view_width, 0);
		rowMinHeight = array.getDimensionPixelSize(R.styleable.DynamicLayout_row_min_height,0);
		contentViewHeight = array.getDimensionPixelSize(R.styleable.DynamicLayout_content_view_height, 0);
		
		contentViewMarginLeft = array.getDimensionPixelSize(R.styleable.DynamicLayout_content_view_margin_left, 0);
		contentViewMarginRight = array.getDimensionPixelSize(R.styleable.DynamicLayout_content_view_margin_right, 0);
		contentViewMarginTop = array.getDimensionPixelSize(R.styleable.DynamicLayout_content_view_margin_top, 0);
		contentViewMarginBottom = array.getDimensionPixelSize(R.styleable.DynamicLayout_content_view_margin_bottom, 0);
		
		contentViewPaddingLeft = array.getDimensionPixelSize(R.styleable.DynamicLayout_content_view_padding_left, 0);
		contentViewPaddingRight = array.getDimensionPixelSize(R.styleable.DynamicLayout_content_view_padding_right, 0);
		contentViewPaddingTop = array.getDimensionPixelSize(R.styleable.DynamicLayout_content_view_padding_top, 0);
		contentViewPaddingBottom = array.getDimensionPixelSize(R.styleable.DynamicLayout_content_view_padding_bottom,0);
		
		availableWidth = ModuleConfig.SCREEN_WIDTH - marginLeft - marginRight - leftViewWidth - rightViewWidth;
		array.recycle();
	}

	@Override
	public int getContentLayout() {
		return 0;
	}

	/**
	 * 加载数据(非定宽,列数动态)
	 * @param list
	 * @param resIds 每个标签的背景,可根据服务器返回的数据动态设置 为null则判断defaultId
	 * @param defaultId 默认背景 为0则默认白色
	 * @param style    为0，则无风格
	 */
	public void loadData(List list, int [] resIds, int defaultId, int defaultCorlorId, int style, IFilterData filterData){
		removeAllViews();
		totalWidth = 0;
		LinearLayout layout = initLayout();
		this.defaultCorlorId = defaultCorlorId;
		for(int index = 0; index < list.size();index ++){
			TextView view = new TextView(mContext);
			if(filterData != null) {
				view.setText(filterData.callBack(index));
			}
			map.put(list.get(index), view);
			
			configTextview(view, resIds, defaultId, style, index);
			if(defaultColor != null){
				defaultColor.setTextViewDefaultColor(view, index);
			}
			view.setTag(list.get(index));
			view.setOnClickListener(this);
			view.measure(availableWidth,contentViewHeight);
			int width = view.getMeasuredWidth();
			layout = fillView(layout, view, width, index == list.size() - 1);
		}
		
		if(!flag){//并未加最后一个视图
			addView(layout);
		}
	}
	
	/**
	 * 加载数据(定宽，定列数)
	 * @param list
	 * @param defaultId
	 * @param filterData
	 */
	public void loadDataSource(List list, int defaultId, IFilterData filterData, int columnNumber){
		map.clear();
		this.list = list;
		int textviewWidth = (availableWidth - (columnNumber-1)*contentViewMarginLeft) / columnNumber;
		int totalNumber = list.size();
		int rowNumber  = totalNumber % columnNumber == 0 ? totalNumber / columnNumber : (totalNumber / columnNumber +1);
		for(int index = 0; index < rowNumber;index ++){
			LinearLayout layout = new LinearLayout(mContext);
			layout.setOrientation(HORIZONTAL);
			
			int num = 0;
			if(totalNumber % columnNumber == 0){
				num = columnNumber;
			}else{
				num = index != (rowNumber - 1) ?  columnNumber : (totalNumber % columnNumber);
			}
			
			for(int i = 0; i < num; i++){
				TextView textView = new TextView(mContext);
				textView.setText(filterData.callBack(index*columnNumber+i));
				
				map.put(list.get(index*columnNumber+i),textView);
				textView.setGravity(Gravity.CENTER);
				textView.setSingleLine(true);
				textView.setEllipsize(TruncateAt.END);
				textView.setTag(list.get(index*columnNumber+i));
				textView.setOnClickListener(this);
				LayoutParams params = new LayoutParams(
						textviewWidth,contentViewHeight);
				if(i != 0){
					params.leftMargin = contentViewMarginLeft;
				}
				textView.setTextColor(getAppColor(defaultCorlorId));	
				if(defaultId != 0){
					this.defaultId = defaultId;
					textView.setBackgroundResource(defaultId);	
				}
				layout.addView(textView, params);
				if(defaultColor != null){
					defaultColor.setTextViewDefaultColor(textView, index*columnNumber+i);
				}
			}
			
			LayoutParams params = new LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			params.topMargin = getPixel(R.dimen.margin_1_5normal_size);//20
			if(index == rowNumber - 1){
				params.bottomMargin = getPixel(R.dimen.margin_1_5normal_size);//20
			}
			
			layout.setLayoutParams(params);
			addView(layout);
		}
		
	}
	
	private LinearLayout initLayout(){
		LinearLayout layout = new LinearLayout(mContext);
		layout.setOrientation(LinearLayout.HORIZONTAL);
		layout.setGravity(Gravity.CENTER_VERTICAL);
		if(rowMinHeight == 0){
			rowMinHeight = LayoutParams.WRAP_CONTENT;
		}
		
		layout.setLayoutParams(new LayoutParams(
					availableWidth,rowMinHeight));
		return layout;
	}

	/**
	 *
	 * @param view
	 * @param resIds 标签背景
	 * @param defaultId 默认标签背景
	 * @param style 展现风格
	 * @param index
	 */
	private void configTextview(TextView view, int[] resIds, int defaultId, int style, int index){
		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
				contentViewHeight);
		params.setMargins(contentViewMarginLeft,contentViewMarginTop,contentViewMarginRight,
				contentViewMarginBottom);
		view.setLayoutParams(params);
		view.setTextColor(getAppColor(defaultCorlorId));
		if(null == resIds){
			if(defaultId != 0){
				this.defaultId = defaultId;
				view.setBackgroundResource(defaultId);	
			}else{
				view.setBackgroundColor(getResources().getColor(R.color.white));
			}
		}else{
			view.setBackgroundDrawable(getResources().getDrawable(resIds[index]));
		
		}
		
		if(style != 0){
			view.setTextAppearance(mContext, style);
		}
		
		view.setGravity(Gravity.CENTER);
		view.setSingleLine(true);
		view.setEllipsize(TruncateAt.END);
		view.setPadding(contentViewPaddingLeft, contentViewPaddingTop, contentViewPaddingRight,
				contentViewPaddingBottom);//需要在setbackgroundresource后才能生效
	}
	
	private LinearLayout fillView(LinearLayout layout, TextView view, int viewWidth, boolean  last){
		if((totalWidth + viewWidth + contentViewMarginLeft + contentViewMarginRight) <= availableWidth){
			layout.addView(view);
			totalWidth += viewWidth + contentViewMarginLeft + contentViewMarginRight;
			if(last){
				addView(layout);
				flag = true;
			}
		}else {
			totalWidth = viewWidth +contentViewMarginLeft + contentViewMarginRight;
			addView(layout);//加的只是上一次的视图
			layout = new LinearLayout(mContext);
			layout.setOrientation(LinearLayout.HORIZONTAL);
			layout.setLayoutParams(new LayoutParams(
					availableWidth,rowMinHeight));
			layout.setGravity(Gravity.CENTER_VERTICAL);
			layout.addView(view);
		}
		
		return layout;
	}
	
	/**
	 * 动态获取可用宽(在容器左右视图设置weight情况下)
	 * @param leftWidth 左边视图宽
	 * @param rightWidth 右边视图宽
	 */
	public void setAvailableWith(int leftWidth,int rightWidth){
		availableWidth = ModuleConfig.SCREEN_WIDTH - marginLeft - marginRight - leftWidth - rightWidth;
	}
	
	public boolean isCheckBackGround() {
		return checkBackGround;
	}

	public void setCheckBackGround(boolean checkBackGround) {
		this.checkBackGround = checkBackGround;
	}


	public OnclickModule getOnclickModule() {
		return onclickModule;
	}

	public void setOnclickModule(OnclickModule onclickModule) {
		this.onclickModule = onclickModule;
	}


	public interface OnclickModule{
		void callBack(View view);
		void reset(Object object, View view);
	}

	@SuppressLint("ResourceAsColor") @Override
	public void onClick(View v) {
		if(isCheckBackGround()){
			if(this.defaultId != 0){
				Iterator<Object> iterator =map.keySet().iterator();
				while(iterator.hasNext()){
					Object object = iterator.next();
					if(changeTextColor){
						map.get(object).setTextColor(getResources().getColor(R.color.white));
						((View) map.get(object).getParent().getParent()).setBackgroundResource(this.defaultId);//父视图背景
					}else{
						map.get(object).setBackgroundResource(this.defaultId);
						if(defaultCorlorId != 0) {
							map.get(object).setTextColor(getAppColor(defaultCorlorId));
						}

						if(onclickModule != null){
							onclickModule.reset(object,v);
						}
					}
				}
			}
		}	
		
		if(onclickModule != null){
			onclickModule.callBack(v);
		}
	}
	
	public interface IFilterData{
		String callBack(int position);
	}
	
	public int getDefaultCorlorId() {
		return defaultCorlorId;
	}

	public void setDefaultCorlorId(int defaultCorlorId) {
		this.defaultCorlorId = defaultCorlorId;
	}

	public int getTotalWidth() {
		return totalWidth;
	}

	public void setTotalWidth(int totalWidth) {
		this.totalWidth = totalWidth;
	}

	public int getMarginLeft() {
		return marginLeft;
	}

	public void setMarginLeft(int marginLeft) {
		this.marginLeft = marginLeft;
	}

	public int getMarginRight() {
		return marginRight;
	}

	public void setMarginRight(int marginRight) {
		this.marginRight = marginRight;
	}

	public int getLeftViewWidth() {
		return leftViewWidth;
	}

	public void setLeftViewWidth(int leftViewWidth) {
		this.leftViewWidth = leftViewWidth;
	}

	public int getRightViewWidth() {
		return rightViewWidth;
	}

	public void setRightViewWidth(int rightViewWidth) {
		this.rightViewWidth = rightViewWidth;
	}

	public int getRowMinHeight() {
		return rowMinHeight;
	}

	public void setRowMinHeight(int rowMinHeight) {
		this.rowMinHeight = rowMinHeight;
	}

	public int getContentViewHeight() {
		return contentViewHeight;
	}

	public void setContentViewHeight(int contentViewHeight) {
		this.contentViewHeight = contentViewHeight;
	}

	public int getContentViewMarginLeft() {
		return contentViewMarginLeft;
	}

	public void setContentViewMarginLeft(int contentViewMarginLeft) {
		this.contentViewMarginLeft = contentViewMarginLeft;
	}

	public int getContentViewMarginRight() {
		return contentViewMarginRight;
	}

	public void setContentViewMarginRight(int contentViewMarginRight) {
		this.contentViewMarginRight = contentViewMarginRight;
	}

	public int getContentViewMarginTop() {
		return contentViewMarginTop;
	}

	public void setContentViewMarginTop(int contentViewMarginTop) {
		this.contentViewMarginTop = contentViewMarginTop;
	}

	public int getContentViewMarginBottom() {
		return contentViewMarginBottom;
	}

	public void setContentViewMarginBottom(int contentViewMarginBottom) {
		this.contentViewMarginBottom = contentViewMarginBottom;
	}

	public int getContentViewPaddingLeft() {
		return contentViewPaddingLeft;
	}

	public void setContentViewPaddingLeft(int contentViewPaddingLeft) {
		this.contentViewPaddingLeft = contentViewPaddingLeft;
	}

	public int getContentViewPaddingRight() {
		return contentViewPaddingRight;
	}

	public void setContentViewPaddingRight(int contentViewPaddingRight) {
		this.contentViewPaddingRight = contentViewPaddingRight;
	}

	public int getContentViewPaddingTop() {
		return contentViewPaddingTop;
	}

	public void setContentViewPaddingTop(int contentViewPaddingTop) {
		this.contentViewPaddingTop = contentViewPaddingTop;
	}

	public int getContentViewPaddingBottom() {
		return contentViewPaddingBottom;
	}

	public void setContentViewPaddingBottom(int contentViewPaddingBottom) {
		this.contentViewPaddingBottom = contentViewPaddingBottom;
	}

	public ITextDefaultColor getDefaultColor() {
		return defaultColor;
	}

	public void setDefaultColor(ITextDefaultColor defaultColor) {
		this.defaultColor = defaultColor;
	}

	public boolean isMultySelect() {
		return multySelect;
	}

	public void setMultySelect(boolean multySelect) {
		this.multySelect = multySelect;
	}

	public boolean isSelect() {
		return select;
	}

	public void setSelect(boolean select) {
		this.select = select;
	}

	public List getList() {
		return list;
	}

	public void setList(List list) {
		this.list = list;
	}
	
	
}
