package com.android.zht.waterwatch.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.android.zht.waterwatch.R;
import com.android.zht.waterwatch.net.HttpHelper;
import com.android.zht.waterwatch.net.HttpType;
import com.android.zht.waterwatch.ui.MainActivity;
import com.android.zht.waterwatch.util.DateUtil;
import com.android.zht.waterwatch.widget.AppTitleLayout;
import com.android.zht.waterwatch.widget.SeekAttentionView;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.hjh.baselib.base.LBaseFragment;
import com.hjh.baselib.constants.ModuleConfig;
import com.hjh.baselib.utils.AppPresences;
import com.hjh.baselib.widget.PullToRefreshLayout;
import com.hjh.baselib.widget.PullableScrollView;
//import com.robinhood.ticker.TickerUtils;
//import com.robinhood.ticker.TickerView;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


import butterknife.BindView;


/**
 * @author hjh
 * 2016-5-21下午9:26:41
 */
public class HomeFragment extends LBaseFragment {
	@BindView(R.id.refresh_view)
	PullToRefreshLayout mRefreshLayout;

	@BindView(R.id.scroll_view)
	PullableScrollView mScrollView;
	
	@BindView(R.id.title_bar)
	AppTitleLayout mTitleLayout;

	@BindView(R.id.bottom_more_layout)
	RelativeLayout mBottomMoreLayout;

	@BindView(R.id.left_money)
	TextView mLeftMoney;

	@BindView(R.id.chart1)
	PieChart mChart;

	@BindView(R.id.container)
	LinearLayout container;

	@BindView(R.id.warning_light)
	ImageView light;

	private Handler handler = new Handler();
	/**
	 * 传入fragment的参数
	 */
	protected Bundle fragmentArgs;
	private MainActivity mActivity;
	private boolean resumed;
	private long delayTime = 500;
	private long startTime,endTime,firstTime;

	private Runnable runnable = new Runnable() {
		@Override
		public void run() {
			if (resumed) {
				handler.postDelayed(this,delayTime == 0 ? 200 : delayTime);
			}
		}
	};

	public HomeFragment(){

	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
    }

	@Override
	public void onAfterCreate(Bundle savedInstanceState) {
		super.onAfterCreate(savedInstanceState);
		mTitleLayout.setTitleText("总览");
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
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		fragmentArgs = getArguments();
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mChart.getLayoutParams();
		params.height = ModuleConfig.SCREEN_HEIGHT - ModuleConfig.STATUS_BAR_HEIGHT
				- dp2px(mActivity,48) -dp2px(mActivity,190) - ModuleConfig.TAB_HEIGHT ;
		setTime();
		loadData();
	}

	@Override
	public int getContentLayout() {
		return R.layout.fragment_home;
	}


	public void showBottomMore(boolean show){
		if(mBottomMoreLayout == null)return;
		mBottomMoreLayout.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
	}

	private void setTime(){
		Calendar dateDisplay = Calendar.getInstance();
		Calendar start = (Calendar) dateDisplay.clone();
		start.setTime(DateUtil.toDate(DateUtil.getDayStartTime(start.getTime().getTime()),DateUtil.DATE_TIME_FORMAT));
		endTime = start.getTime().getTime();

		start.add(Calendar.MONTH,-1);//上月的今天
		startTime = start.getTime().getTime()+24*60*60*1000-1000;

		start = (Calendar) dateDisplay.clone();
		start.set(Calendar.DAY_OF_MONTH, 1);//月初1号
		start.setTime(DateUtil.toDate(DateUtil.getDayStartTime(start.getTime().getTime()),DateUtil.DATE_TIME_FORMAT));
		firstTime = start.getTime().getTime();
	}

	private void onRequest(){
	}

	public HomeFragment setOwner(MainActivity mainActivity){
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
		resumed = true;
		onRequest();
    }

	@Override
    public void onRefreshView(){
    }

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	private void loadData(){
		ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
		entries.add(new PieEntry(60, "学生宿舍"));
		entries.add(new PieEntry(20, "教师宿舍"));
		entries.add(new PieEntry(20, "教学楼"));
		initPieChat(mChart, entries);
		String [] datas = {"10","23","35"};
		addLayoutAnimation(container,R.anim.tran_left);
		for(int i = 0;i < datas.length;i++){
			View view = mInflater.inflate(R.layout.item_ammeter,null, false);
			container.addView(view);
		}

		SeekAttentionView.flickerText(light);
	}

	public void initPieChat( PieChart pieChart,ArrayList<PieEntry> entries ){
		//饼状图
		pieChart.setUsePercentValues(true);//设置为TRUE的话，图标中的数据自动变为percent
		pieChart.getDescription().setEnabled(false);
		pieChart.setExtraOffsets(5, 10, 5, 5);//设置额外的偏移量(在图表视图周围)

		pieChart.setDragDecelerationFrictionCoef(0.95f);//设置滑动减速摩擦系数，在0~1之间
		//设置中间文件
		// pieChart.setCenterText(generateCenterSpannableText());
		pieChart.setDrawSliceText(false);//设置隐藏饼图上文字，只显示百分比
		pieChart.setDrawHoleEnabled(false);//设置为TRUE时，饼中心透明
//        pieChart.setHoleColor(Color.WHITE);//设置饼中心颜色

//        pieChart.setTransparentCircleColor(Color.WHITE);//透明的圆
//        pieChart.setTransparentCircleAlpha(110);//透明度

		//pieChart.setHoleRadius(58f);//中间圆的半径占总半径的百分数
		pieChart.setHoleRadius(0);//实心圆
		//pieChart.setTransparentCircleRadius(61f);//// 半透明圈

		pieChart.setDrawCenterText(true);//绘制显示在饼图中心的文本

		pieChart.setRotationAngle(0);//设置一个抵消RadarChart的旋转度
		// 触摸旋转
		pieChart.setRotationEnabled(true);//通过触摸使图表旋转
		pieChart.setHighlightPerTapEnabled(true);//通过点击手势突出显示的值


		//变化监听
		// pieChart.setOnChartValueSelectedListener(this);
		//模拟数据

		//设置数据
		setData(pieChart,entries);


		pieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);

		Legend l = pieChart.getLegend();
		l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
		l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
		l.setOrientation(Legend.LegendOrientation.VERTICAL);
		l.setDrawInside(false);
		l.setXEntrySpace(7f);
		l.setYEntrySpace(0f);
		l.setYOffset(0f);

		// 输入标签样式
		pieChart.setEntryLabelColor(Color.WHITE);
		pieChart.setEntryLabelTextSize(12f);
		/**
		 * 设置比例图
		 */
		Legend mLegend = pieChart.getLegend();
		mLegend.setPosition(Legend.LegendPosition.RIGHT_OF_CHART_CENTER);  //在左边中间显示比例图
		mLegend.setFormSize(14f);//比例块字体大小
		mLegend.setXEntrySpace(4f);//设置距离饼图的距离，防止与饼图重合
		mLegend.setYEntrySpace(4f);
		//设置比例块换行...
		mLegend.setWordWrapEnabled(true);
		mLegend.setDirection(Legend.LegendDirection.LEFT_TO_RIGHT);//设置字跟图表的左右顺序

		//mLegend.setTextColor(getResources().getColor(R.color.alpha_80));
		mLegend.setForm(Legend.LegendForm.SQUARE);//设置比例块形状，默认为方块
//        mLegend.setEnabled(false);//设置禁用比例块
	}

	//设置数据
	private void setData( PieChart pieChart,ArrayList<PieEntry> entries) {
		PieDataSet dataSet = new PieDataSet(entries, "");
		dataSet.setSliceSpace(2f);//饼图区块之间的距离
		dataSet.setSelectionShift(5f);//

		//数据和颜色
		Integer[] colors=new Integer[]{Color.parseColor("#d87a80"), Color.parseColor("#2ec7c9"), Color.parseColor("#b6a2de"),
				Color.parseColor("#5ab1ef"), Color.parseColor("#ffb980"), Color.parseColor("#8d98b3")};
		//添加对应的颜色值
		List<Integer> colorSum = new ArrayList<>();
		for (Integer color : colors) {
			colorSum.add(color);
		}
		dataSet.setColors(colorSum);
		PieData data = new PieData(dataSet);
		data.setValueFormatter(new PercentFormatter());
		data.setValueTextSize(11f);
		data.setValueTextColor(Color.BLACK);
		pieChart.setData(data);
		pieChart.highlightValues(null);//在给定的数据集中突出显示给定索引的值
		//刷新
		pieChart.invalidate();
	}

}
