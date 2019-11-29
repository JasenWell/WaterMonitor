package com.android.zht.waterwatch.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewCompat;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.zht.waterwatch.R;
import com.android.zht.waterwatch.net.HttpHelper;
import com.android.zht.waterwatch.net.HttpType;
import com.android.zht.waterwatch.ui.MainActivity;
import com.android.zht.waterwatch.ui.WarningDetailActivity;
import com.android.zht.waterwatch.ui.WarningGatherActivity;
import com.android.zht.waterwatch.util.DateUtil;
import com.android.zht.waterwatch.widget.AppTitleLayout;
import com.github.mikephil.charting.charts.LineChart;
import com.hjh.baselib.base.LBaseFragment;
import com.hjh.baselib.constants.ModuleConfig;
import com.hjh.baselib.utils.AppPresences;
import com.hjh.baselib.widget.PullToRefreshLayout;
import com.hjh.baselib.widget.PullableScrollView;
import com.robinhood.ticker.TickerUtils;
import com.robinhood.ticker.TickerView;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * @author hjh
 * 2016-5-21下午9:26:41
 */
public class WarningFragment extends LBaseFragment {
	@BindView(R.id.refresh_view)
	PullToRefreshLayout mRefreshLayout;

	@BindView(R.id.scroll_view)
	PullableScrollView mScrollView;

	@BindView(R.id.title_bar)
	AppTitleLayout mTitleLayout;

    @BindView(R.id.layout)
    LinearLayout layout;

	@BindView(R.id.bottom_layout)
	LinearLayout bottomLayout;

	@BindView(R.id.container)
	LinearLayout container;//告警内容容器

	@BindView(R.id.btn_up)
	TextView btnUp;

	@BindView(R.id.btn_down)
	TextView btnDown;

	@BindView(R.id.btn_statistics)
	TextView btnStatistics;

	@BindView(R.id.item_number)
	TextView number;

	@BindView(R.id.item_point)
	TextView point;

	@BindView(R.id.item_type)
	TextView type;

	@BindView(R.id.item_time)
	TextView time;

	@BindView(R.id.bottom_more_layout)
	RelativeLayout mBottomMoreLayout;


	private SparseArray<TextView> sparseArray;
	private Handler handler = new Handler();
	/**
	 * 传入fragment的参数
	 */
	protected Bundle fragmentArgs;
	private MainActivity mActivity;


	public WarningFragment(){

	}
	

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
    }

	@Override
	public void onAfterCreate(Bundle savedInstanceState) {
		super.onAfterCreate(savedInstanceState);
		mTitleLayout.setTitleText("告警");
		mTitleLayout.disableLeftButton();
		mScrollView.setCanLoadMore(false);
		mScrollView.setCanRefresh(false);
		showBottomMore(false);
		mRefreshLayout.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {

			@Override
			public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
				onRequest();
			}

			@Override
			public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {

			}
		});
	}

	@Override
	public void onLoadDefaultData() {
		super.onLoadDefaultData();
		sparseArray = new SparseArray<>();
		sparseArray.append(R.id.item_number,number);
		sparseArray.append(R.id.item_point,point);
		sparseArray.append(R.id.item_type,type);
		sparseArray.append(R.id.item_time,time);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		fragmentArgs = getArguments();
//		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) container.getLayoutParams();
//		layout.measure(ModuleConfig.SCREEN_WIDTH,ModuleConfig.SCREEN_HEIGHT);
//		bottomLayout.measure(ModuleConfig.SCREEN_WIDTH,ModuleConfig.SCREEN_HEIGHT);
//		int height = layout.getMeasuredHeight()+bottomLayout.getMeasuredHeight();
//		params.height = ModuleConfig.SCREEN_HEIGHT - ModuleConfig.STATUS_BAR_HEIGHT
//				- mActivity.getResources().getDimensionPixelSize(R.dimen.title_bar_height) - ModuleConfig.TAB_HEIGHT
//				- height;
//		container.setLayoutParams(params);
		loadData();
		switchItem(R.id.item_number);
	}

	@Override
	public int getContentLayout() {
		return R.layout.fragment_warning;
	}

	@OnClick({R.id.btn_up,R.id.btn_down,R.id.btn_statistics,R.id.item_number,R.id.item_point,R.id.item_type,R.id.item_time})
	public void onViewClicked(View view){
		switch (view.getId()){
			case R.id.item_number:
			case R.id.item_point:
			case R.id.item_type:
			case R.id.item_time:
				switchItem(view.getId());
				break;
			case R.id.btn_statistics:
				mActivity.startActivityWithAnim(mActivity,new Intent(mContext,WarningGatherActivity.class));
				break;
		}
	}

	public void switchItem(int id){
		for(int i = 0;i < sparseArray.size();i++){
			int key = sparseArray.keyAt(i);
			if(key == id){
				scale(sparseArray.get(key),false);
				sparseArray.get(key).setTextColor(getResources().getColor(R.color.ok_btn_color));
			}else {
				scale(sparseArray.get(key),true);
				sparseArray.get(key).setTextColor(getResources().getColor(R.color.black));
			}
		}
	}

	public void scale(View view,boolean normal){//https://blog.csdn.net/hacker_crazy/article/details/78487742
		ViewCompat.animate(view)
				.setDuration(200)
				.scaleX(normal ? 1f :1.3f)
				.scaleY(normal ? 1f :1.3f)
				.start();
	}

	private void loadData(){
		addLayoutAnimation(container,R.anim.tran_left);
		String[] datas = {"","","","","","","","","",""};
		for(int i = 0; i < datas.length;i++){
			View view = mInflater.inflate(R.layout.item_warning,null, false);
			TextView no = view.findViewById(R.id.item_no);
			TextView position = view.findViewById(R.id.item_position);
			TextView type = view.findViewById(R.id.item_warning_type);
			TextView time = view.findViewById(R.id.item_warning_time);
			TextView detail = view.findViewById(R.id.item_detail);
			detail.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					mActivity.startActivityWithAnim(mActivity,new Intent(mContext,WarningDetailActivity.class));
				}
			});
			no.setText((i+1+"."));
			position.setText("告警节点位置"+(i+1));
			time.setText(DateUtil.toDateString(new Date(),DateUtil.DATE_TIME_FORMAT));
			container.addView(view);
		}
	}

	public void showBottomMore(boolean show){
		if(mBottomMoreLayout == null)return;
		mBottomMoreLayout.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
	}


	private void onRequest(){

	}

	public WarningFragment setOwner(MainActivity mainActivity){
		mActivity = mainActivity;
		return this;
	}

    @Override
    public void onPause() {
    	super.onPause();
    }

    @Override
    public void onResume() {
    	super.onResume();
		onRequest();
    }

	@Override
    public void onRefreshView(){
    }

	@Override
	public void onDestroy() {
		super.onDestroy();
		sparseArray.clear();
		sparseArray = null;
	}

}
