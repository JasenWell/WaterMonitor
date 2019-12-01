package com.android.zht.waterwatch.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.zht.waterwatch.R;
import com.android.zht.waterwatch.ui.AnalyzeChartActivity;
import com.android.zht.waterwatch.ui.MainActivity;
import com.android.zht.waterwatch.util.DateUtil;
import com.android.zht.waterwatch.widget.AppTitleLayout;
import com.android.zht.waterwatch.widget.MonthSelectDialog;
import com.android.zht.waterwatch.widget.MyDatePicker;
import com.android.zht.waterwatch.widget.SelectBottomPopView;
import com.hjh.baselib.base.LBaseFragment;
import com.hjh.baselib.constants.ModuleConfig;
import com.hjh.baselib.widget.PullToRefreshLayout;
import com.hjh.baselib.widget.PullableScrollView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * @author hjh
 * 2016-5-21下午9:26:41
 */
public class ConsumeFragment extends LBaseFragment {
	@BindView(R.id.refresh_view)
	PullToRefreshLayout mRefreshLayout;

	@BindView(R.id.scroll_view)
	PullableScrollView mScrollView;

	@BindView(R.id.title_bar)
	AppTitleLayout mTitleLayout;

	@BindView(R.id.bottom_more_layout)
	RelativeLayout mBottomMoreLayout;

	@BindView(R.id.item_chart)
	TextView chart;

	@BindView(R.id.switch_chart)
	TextView switchChart;

	@BindView(R.id.select_time)
	TextView timeView;

	@BindView(R.id.btn_search)
	TextView searchView;

	@BindView(R.id.report_day)
	TextView dayReport;

	@BindView(R.id.report_month)
	TextView monthReport;

	@BindView(R.id.report_year)
	TextView yearReport;

    @BindView(R.id.container)
    LinearLayout container;//水耗容器

	private SparseArray<TextView> sparseArray;
	private int currentReportType;
	/**
	 * 传入fragment的参数
	 */
	protected Bundle fragmentArgs;
	private MainActivity mActivity;
	private MyDatePicker dateDialog;

	private SelectBottomPopView areaView;
	private SelectBottomPopView yearView;

	protected int mYearId = 0;
	protected final static int[] MONTH = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
	protected ArrayList<Integer> mAllYear = new ArrayList<Integer>();
	protected MonthSelectDialog mMonthPickDialog;
	protected Calendar dateDisplay;
	protected int mMonthId = 0;
	private long startTime,endTime;

	public ConsumeFragment(){

	}

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
    }

	@Override
	public void onAfterCreate(Bundle savedInstanceState) {
		super.onAfterCreate(savedInstanceState);
		mTitleLayout.setTitleText("水耗情况");
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
	public void onLoadDefaultView(Bundle savedInstanceState) {
		super.onLoadDefaultView(savedInstanceState);
		timeView.setText(DateUtil.toDateString(new Date()));
        loadData();
	}

	@Override
	public void onLoadDefaultData() {
		super.onLoadDefaultData();
		sparseArray = new SparseArray<>();
		sparseArray.append(R.id.report_day,dayReport);
		sparseArray.append(R.id.report_month,monthReport);
		sparseArray.append(R.id.report_year,yearReport);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		fragmentArgs = getArguments();
		switchItem(R.id.report_day);
		scale(chart,false);
		chart.setTextColor(getResources().getColor(R.color.ok_btn_color));
	}

	@Override
	public int getContentLayout() {
		return R.layout.fragment_consume;
	}

	@OnClick({R.id.switch_chart,R.id.select_time,R.id.btn_search,R.id.select_area,R.id.btn_up,R.id.btn_down,
			R.id.report_day,R.id.report_month,R.id.report_year})
	public void onViewClicked(View view){
		switch (view.getId()){
			case R.id.switch_chart:
				mActivity.startActivityWithAnim(mActivity,new Intent(mContext,AnalyzeChartActivity.class));
				break;
			case R.id.select_time:
				if(currentReportType == R.id.report_day) {
					showDateDialog();
				}else if(currentReportType == R.id.report_month){
					selectMonth();
				}else if(currentReportType == R.id.report_year){
					selectYear();
				}
				break;
			case R.id.btn_search:

				break;
			case R.id.select_area:
				selectArea();
				break;
			case R.id.btn_up:

				break;
			case R.id.btn_down:

				break;
			case R.id.report_day:
				timeView.setText(DateUtil.toDateString(new Date()));
				switchItem(view.getId());
				break;
			case R.id.report_month:
				setMonth(null);
				switchItem(view.getId());
				break;
			case R.id.report_year:
				timeView.setText(DateUtil.toDateString(new Date(),"yyyy年"));
				switchItem(view.getId());
				break;
		}
	}

	private void selectArea(){
		if(areaView == null){
			getScreenSize();
			areaView = new SelectBottomPopView(mActivity, ModuleConfig.SCREEN_WIDTH, ModuleConfig.SCREEN_HEIGHT, new SelectBottomPopView.OnClickPopView() {
				@Override
				public void onClickPop(int index) {

				}
			});
		}
		areaView.addChild(true);
		areaView.show();
	}

	private void selectYear(){
		if(yearView == null){
			getScreenSize();
			yearView = new SelectBottomPopView(mActivity, ModuleConfig.SCREEN_WIDTH, ModuleConfig.SCREEN_HEIGHT, new SelectBottomPopView.OnClickPopView() {
				@Override
				public void onClickPop(int index) {

				}
			});
		}
		yearView.addChild(false);
		yearView.show();
	}

	private void setMonth(Date date){

		if (dateDisplay == null){
			dateDisplay = Calendar.getInstance();
			if(date != null){
				dateDisplay.setTime(date);
			}
		}

		mAllYear = getAllYear(dateDisplay);
		mMonthId = dateDisplay.get(Calendar.MONTH);
		timeView.setText(mAllYear.get(mYearId) + "年"+MONTH[mMonthId] + "月");
		setTime();
	}

	public ArrayList<Integer> getAllYear(Calendar calendar) {
		int currentYear = calendar.get(Calendar.YEAR);
		ArrayList<Integer> year = new ArrayList<Integer>();
		int initYear = currentYear - 2;

		if (currentYear <= 2017) { //2017年以前的不作处理
			year.add(2017);
		} else {
			int size = currentYear - initYear;
			for (int i = 0; i <= size; i++) {
				int temp = initYear + i;
				year.add(temp);
				if (temp == currentYear) {
					mYearId = i;
				}
			}
		}
		return year;
	}

	//选择月份
	private void selectMonth(){
		if(mMonthPickDialog == null){
			mMonthPickDialog = new MonthSelectDialog(mActivity, R.style.Dialog, mAllYear.get(mYearId), mMonthId);
		}

		mMonthPickDialog.setMonthSelectDialogListener(new MonthSelectDialog.MonthSelectDialogListener() {
			@Override
			public void MonthSelectDialogListener(int year, int checkedId) {
				mMonthId = checkedId;
				timeView.setText(year + "年"+MONTH[mMonthId] + "月");
				dateDisplay.set(Calendar.YEAR,year);
				dateDisplay.set(Calendar.MONTH,mMonthId);
				setTime();
			}
		});
		mMonthPickDialog.show();
	}

	private void setTime(){
		Calendar start = (Calendar) dateDisplay.clone();
		start.set(Calendar.DAY_OF_MONTH, 1);//月初1号
		start.setTime(DateUtil.toDate(DateUtil.getDayStartTime(start.getTime().getTime()),DateUtil.DATE_TIME_FORMAT));
		startTime = start.getTime().getTime();

		start.add(Calendar.MONTH,1);//下月1号
		start.add(Calendar.DAY_OF_MONTH,-1);//本月底
		endTime = start.getTime().getTime()+24*60*60*1000-1000;
	}

	public void switchItem(int id){
		currentReportType = id;
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


	private void showDateDialog(){
		dateDialog = new MyDatePicker(mActivity,
				new MyDatePicker.OnDateSetListener() {
					@Override
					public void onDateSet(DatePicker view,
										  int year, int monthOfYear,
										  int dayOfMonth) {
						timeView.setText(year + "-"
								+ String.valueOf((monthOfYear + 1) < 10 ? "0"
								+ (monthOfYear + 1) : (monthOfYear + 1)) + "-"
								+ String.valueOf((dayOfMonth < 10) ? "0"
								+ dayOfMonth : dayOfMonth));
					}
				}, getDate()[0], getDate()[1], getDate()[2]);
		dateDialog.setMaxDate(Calendar.getInstance().getTimeInMillis());
		dateDialog.myShow();
	}

	/**
	 * 获取存放年、月、日的数组
	 * [0]-年;[1]-月;[2]-日;
	 * @return
	 */
	public int[] getDate(){
		int[] date = new int[]{1990, 0, 1};
		String bir = timeView.getText().toString().replace(" ", "");
		if ((date==null) || (date.equals("")) || (date.equals("出生年月"))) {

		} else if((bir.contains("-")) && (bir.length()==10)) {
			String[] datas = bir.split("-");
			date[0] = Integer.parseInt(datas[0]);
			date[1] = Integer.parseInt(datas[1])-1;
			date[2] = Integer.parseInt(datas[2]);
		}
		return date;
	}

	public void showBottomMore(boolean show){
		if(mBottomMoreLayout == null)return;
		mBottomMoreLayout.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
	}

	public void scale(View view,boolean normal){//https://blog.csdn.net/hacker_crazy/article/details/78487742
		ViewCompat.animate(view)
				.setDuration(200)
				.scaleX(normal ? 1f :1.3f)
				.scaleY(normal ? 1f :1.3f)
				.start();
	}


	private void onRequest(){

	}

	public ConsumeFragment setOwner(MainActivity mainActivity){
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
	}

    private void loadData(){
        addLayoutAnimation(container,R.anim.tran_left);
        container.removeAllViews();
        String[] datas = {"","","","","","","","","",""};
        for(int i = 0; i < datas.length;i++){
            View view = mInflater.inflate(R.layout.item_consume,null, false);
            TextView no = view.findViewById(R.id.item_no);
            TextView number = view.findViewById(R.id.item_number);
            no.setText((i+1+"."));
            number.setText("消耗"+(i+1));
            container.addView(view);
        }
    }
}
