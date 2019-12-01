package com.android.zht.waterwatch.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.zht.waterwatch.R;
import com.android.zht.waterwatch.bean.ClassifyInfo;
import com.android.zht.waterwatch.bean.SchoolInfo;
import com.android.zht.waterwatch.bean.UserInfo;
import com.android.zht.waterwatch.bean.WaterEffectInfo;
import com.android.zht.waterwatch.ui.MainActivity;
import com.android.zht.waterwatch.util.DateUtil;
import com.android.zht.waterwatch.widget.AppTitleLayout;
import com.android.zht.waterwatch.widget.SelectTopPopView;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.hjh.baselib.base.LBaseFragment;
import com.hjh.baselib.constants.ModuleConfig;
import com.hjh.baselib.utils.DateTools;
import com.hjh.baselib.widget.PullToRefreshLayout;
import com.hjh.baselib.widget.PullableScrollView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

//import com.robinhood.ticker.TickerUtils;
//import com.robinhood.ticker.TickerView;


/**
 * @author hjh
 * 2016-5-21下午9:26:41
 */
public class HomeReplaceFragment extends LBaseFragment {
	@BindView(R.id.refresh_view)
	PullToRefreshLayout mRefreshLayout;

	@BindView(R.id.scroll_view)
	PullableScrollView mScrollView;

	@BindView(R.id.title_bar)
	AppTitleLayout mTitleLayout;

	@BindView(R.id.bottom_more_layout)
	RelativeLayout mBottomMoreLayout;

    @BindView(R.id.organization_name)
    TextView organizationName;

    @BindView(R.id.school_name)
    TextView schoolName;

	@BindView(R.id.total_water)
	TextView mTotalWater;

	@BindView(R.id.select_time)
	TextView selectTimeView;

	@BindView(R.id.chart1)
	PieChart mChart;

	@BindView(R.id.barchart)
	BarChart mBarChart;

	private Handler handler = new Handler();
	private MainActivity mActivity;
	private boolean resumed;
	private long delayTime = 500;
	private long startTime,endTime,firstTime;
	private UserInfo userInfo;
	private int currentIndex = 0;
	private SelectTopPopView popView;
	private List<WaterEffectInfo> effectInfoList = new ArrayList<>();

	private Runnable runnable = new Runnable() {
		@Override
		public void run() {
			if (resumed) {
				handler.postDelayed(this,delayTime == 0 ? 200 : delayTime);
			}
		}
	};

	public HomeReplaceFragment(){

	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onViewStateRestored( Bundle savedInstanceState) {
		super.onViewStateRestored(savedInstanceState);
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
		initPieChart(mChart);
		initBarChart(mBarChart);
		showBottomMore(false);
		organizationName.setVisibility(View.GONE);
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
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mChart.getLayoutParams();
		params.height = ModuleConfig.SCREEN_HEIGHT - ModuleConfig.STATUS_BAR_HEIGHT
				- dp2px(getContext(),48) -dp2px(getContext(),190) - ModuleConfig.TAB_HEIGHT ;
		setTime();
		loadView();
	}

	@Override
	public int getContentLayout() {
		return R.layout.fragment_home_replace;
	}

	@OnClick({R.id.select_time})
	public void onViewClicked(View view){
		switch (view.getId()){
			case R.id.select_time:
				selectTime();
				break;
		}
	}

	private int getOneHeight(){
		View view = LayoutInflater.from(mContext).inflate(R.layout.item_area,null);
		TextView textView = view.findViewById(R.id.item_name);
		textView.setGravity(Gravity.CENTER);
		textView.setText("单行");
		textView.measure(ModuleConfig.SCREEN_WIDTH,ModuleConfig.SCREEN_HEIGHT);
		int height =  textView.getMeasuredHeight();
		return height;
	}

	public void selectTime(){
		if(popView == null){
			getScreenSize();
			popView = new SelectTopPopView(mActivity,dp2px(mContext,100),getOneHeight()*effectInfoList.size(), new SelectTopPopView.OnClickPopView() {
				@Override
				public void onClickPop(int index) {
					currentIndex = index;
					List<ClassifyInfo> dataList = effectInfoList.get(currentIndex).getClassifyInfoList();
					if(dataList == null || dataList.size() == 0)return;
					refreshPieChat(dataList);
				}

				@Override
				public String getShowText(int index) {
					return DateTools.formatTimestamp(effectInfoList.get(index).getDate());
				}
			});
		}

		popView.addChild(effectInfoList.size());
		popView.show(selectTimeView);
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

	public HomeReplaceFragment setOwner(MainActivity mainActivity){
		mActivity = mainActivity;
		return this;
	}

	public HomeReplaceFragment setUserInfo(UserInfo userInfo){
	    this.userInfo = userInfo;
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

	private void loadView(){
		if(userInfo.getDepartmentInfo() != null && userInfo.getDepartmentInfo().getSchoolInfo() != null) {
			SchoolInfo schoolInfo = userInfo.getDepartmentInfo().getSchoolInfo();
			schoolName.setText(schoolInfo.getName());
			mTotalWater.setText(schoolInfo.getTotal());
			if(schoolInfo.getEffectInfoList() != null && schoolInfo.getEffectInfoList().size() > 0) {
				selectTimeView.setText(DateTools.formatTimestamp(schoolInfo.getEffectInfoList().get(0).getDate()));
				int size = schoolInfo.getEffectInfoList().size();
				for(int i = 0; i< size-1;i++){
					effectInfoList.add(schoolInfo.getEffectInfoList().get(i));
				}
				List<ClassifyInfo> dataList = effectInfoList.get(currentIndex).getClassifyInfoList();
				if(dataList == null || dataList.size() == 0)return;
				refreshPieChat(dataList);

				List<ClassifyInfo> hourList = schoolInfo.getEffectInfoList().get(size-1).getClassifyInfoList();
				if(hourList == null || hourList.size() == 0)return;
				refreshBarChart(hourList);
			}
		}
	}

	private void refreshPieChat(List<ClassifyInfo> dataList){
		ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
		for(int i = 0; i < dataList.size();i++){
			entries.add(new PieEntry(dataList.get(i).getTypeProportion(),dataList.get(i).getTypeName()));
		}

		if (mChart.getData() != null && mChart.getData().getDataSetCount() > 0) {
			PieDataSet dataSet = (PieDataSet) mChart.getData().getDataSet();
			dataSet.setValues(entries);
			mChart.getData().notifyDataChanged();
			mChart.notifyDataSetChanged();
		}else {
			firstPieData(mChart,entries);
		}
	}

	public void initPieChart( PieChart pieChart){
		//饼状图
		pieChart.setNoDataText("暂无数据");
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
	private void firstPieData( PieChart pieChart,ArrayList<PieEntry> entries) {
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
		pieChart.highlightValues(null);//在给定的数据集中突出显示给定索引的值
		pieChart.setData(data);
	}

	private void refreshBarChart(List<ClassifyInfo> dataList){
		ArrayList<BarEntry> valuesToday = new ArrayList<BarEntry>();
		ArrayList<BarEntry> valuesLastDay = new ArrayList<BarEntry>();
		for (int i = 0; i < dataList.size(); i++) {
//			valuesToday.add(new BarEntry(i,(int) (Math.random() * 30)+10));
			valuesToday.add(new BarEntry(i,dataList.get(i).getTodaydata()));
			valuesLastDay.add(new BarEntry(i,dataList.get(i).getLastdata()));
		}
		mBarChart.getXAxis().setLabelCount(dataList.size());
		BarDataSet set1, set2;
		if (mBarChart.getData() != null && mBarChart.getData().getDataSetCount() > 0) {
			set1 = (BarDataSet) mBarChart.getData().getDataSetByIndex(0);
			set2 = (BarDataSet) mBarChart.getData().getDataSetByIndex(1);
			set1.setValues(valuesToday);
			set2.setValues(valuesLastDay);
			mBarChart.getData().notifyDataChanged();
			mBarChart.notifyDataSetChanged();
		}else {
			firstBarData(dataList.size(),valuesToday,valuesLastDay);
		}
	}
	private BarData firstBarData(int cnt,ArrayList<BarEntry> valuesToday,ArrayList<BarEntry> valuesLastDay) {//https://blog.csdn.net/ww897532167/article/details/74171294
		BarDataSet set1 = new BarDataSet(valuesToday, "今天");
		BarDataSet set2 = new BarDataSet(valuesLastDay, "昨天");
		set1.setColor(Color.rgb(255, 51, 0));
		set2.setColor(Color.rgb(247, 150, 70));

		ArrayList<IBarDataSet> barDataSets = new ArrayList<IBarDataSet>();
		barDataSets.add(set1);
		barDataSets.add(set2);

		BarData barData = new BarData(barDataSets);
//		barData.setValueFormatter(new MyValueFormatter());

		int barAmount = 2; //需要显示柱状图的类别 数量
//设置组间距占比30% 每条柱状图宽度占比 70% /barAmount  柱状图间距占比 0%
		float groupSpace = 0.3f; //柱状图组之间的间距
		float barWidth = (1f - groupSpace) / barAmount;
		float barSpace = 0f;

//		groupSpace = 0.08f;
//		barSpace = 0.03f; // x4 DataSet
//		barWidth = 0.2f; // x4 DataSet
//设置柱状图宽度
		barData.setBarWidth(barWidth);
//(起始点、柱状图组间距、柱状图之间间距)
		barData.groupBars(0f, groupSpace, barSpace);

		mBarChart.getXAxis().setAxisMinimum(0);

		// barData.getGroupWith(...) is a helper that calculates the width each group needs based on the provided parameters
		mBarChart.getXAxis().setAxisMaximum(barData.getGroupWidth(groupSpace, barSpace) * cnt);
		mBarChart.setData(barData);
		return barData;
	}

	private void initBarChart(BarChart barChart) {//https://blog.csdn.net/weixin_40595516/article/details/82860790
		// 设置 条形图 简介
		barChart.setNoDataText("暂无数据");
		Description description = new Description();
		// 可以自定义 位置
		// description.setPosition(200, 200);
		// 也可以 根据 X 轴，Y 轴进行偏移量设置
		description.setXOffset(50f);
		description.setYOffset(10f);
		description.setText("两日对比");
		barChart.setDescription(description);// 数据描述
		barChart.setScaleEnabled(false);// 是否可以缩放
		// 设置 柱子的宽度
		// mBarData.setBarWidth(2f);
		// 获取 x 轴
		XAxis xAxis = barChart.getXAxis();
		// 设置 x 轴显示位置
		xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
		// 取消 垂直 网格线
		xAxis.setDrawGridLines(false);
		// 设置 x 轴 坐标旋转角度
		xAxis.setLabelRotationAngle(10f);
		// 设置 x 轴 坐标字体大小
		xAxis.setTextSize(10f);
		xAxis.setCenterAxisLabels(true);
		// 设置 x 坐标轴 颜色
		xAxis.setAxisLineColor(Color.RED);
		// 设置 x 坐标轴 宽度
		xAxis.setAxisLineWidth(1f);
		// 设置 x轴 的刻度数量
		xAxis.setValueFormatter(new IAxisValueFormatter() {
			@Override
			public String getFormattedValue(float value, AxisBase axis) {
				if(value == 24.0){
					return "";
				}
				return String.valueOf((int)(value+1));
			}
		});

		// 获取 右边 y 轴
		YAxis mRAxis = barChart.getAxisRight();
		// 隐藏 右边 Y 轴
		mRAxis.setEnabled(false);
		// 获取 左边 Y轴
		YAxis mLAxis = barChart.getAxisLeft();
		// 取消 左边 Y轴 坐标线
		mLAxis.setDrawAxisLine(false);
		// 取消 横向 网格线
		mLAxis.setDrawGridLines(false);
		// 设置 Y轴 的刻度数量
		mLAxis.setLabelCount(8);

//        barChart.setDrawBorders(false); ////是否在折线图上添加边框
//        // 如果没有数据的时候，会显示这个，类似ListView的EmptyView
//        barChart.setNoDataText("You need to provide data for the chart.");
//        barChart.setDrawGridBackground(false); // 是否显示表格颜色
//        barChart.setGridBackgroundColor(Color.WHITE& 0x70FFFFFF); // 表格的的颜色，在这里是是给颜色设置一个透明度
//        barChart.setTouchEnabled(true); // 设置是否可以触摸
//        barChart.setDragEnabled(true);// 是否可以拖拽
//
//        barChart.setPinchZoom(false);//
//        // barChart.setBackgroundColor();// 设置背景
//        barChart.setDrawBarShadow(true);
//        Legend mLegend = barChart.getLegend(); // 设置比例图标示
//        mLegend.setForm(Legend.LegendForm.CIRCLE);// 样式
//        mLegend.setFormSize(6f);// 字体
//        mLegend.setTextColor(Color.BLACK);// 颜色
		barChart.animateX(2500); // 立即执行的动画,x轴
	}


}
