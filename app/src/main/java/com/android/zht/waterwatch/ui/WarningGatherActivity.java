package com.android.zht.waterwatch.ui;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.zht.waterwatch.R;
import com.android.zht.waterwatch.util.DateUtil;
import com.android.zht.waterwatch.widget.AppTitleLayout;
import com.android.zht.waterwatch.widget.MonthSelectDialog;
import com.hjh.baselib.base.LBaseActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;

public class WarningGatherActivity extends LBaseActivity {

    @BindView(R.id.title_bar)
    AppTitleLayout mTitleLayout;

    @BindView(R.id.text_date_today)
    TextView monthView;

    @BindView(R.id.btn_search)
    TextView search;

    @BindView(R.id.item_number)
    TextView number;

    @BindView(R.id.item_type)
    TextView type;

    @BindView(R.id.item_count)
    TextView count;

    @BindView(R.id.item_dispose)
    TextView dispose;

    @BindView(R.id.item_undispose)
    TextView undispose;

    @BindView(R.id.container)
    LinearLayout container;//告警内容容器

    @BindView(R.id.btn_up)
    TextView btnUp;

    @BindView(R.id.btn_down)
    TextView btnDown;

    private SparseArray<TextView> sparseArray;
    protected int mYearId = 0;
    protected final static int[] MONTH = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
    protected ArrayList<Integer> mAllYear = new ArrayList<Integer>();
    protected MonthSelectDialog mMonthPickDialog;
    protected Calendar dateDisplay;
    protected int mMonthId = 0;
    private long startTime,endTime;

    @Override
    public int getContentLayout() {
        return R.layout.activity_warning_gather;
    }

    @Override
    public void onAfterCreate(Bundle savedInstanceState) {
        super.onAfterCreate(savedInstanceState);
        mTitleLayout.setTitleText("告警统计");
        mTitleLayout.enableLeftButton();
        mTitleLayout.setTitleClickListener(this);
    }

    @Override
    public void onLoadDefaultData() {
        super.onLoadDefaultData();
        sparseArray = new SparseArray<>();
        sparseArray.append(R.id.item_number,number);
        sparseArray.append(R.id.item_type,type);
        sparseArray.append(R.id.item_count,count);
        sparseArray.append(R.id.item_dispose,dispose);
        sparseArray.append(R.id.item_undispose,undispose);
    }

    @Override
    public void onLoadDefaultView(Bundle savedInstanceState) {
        super.onLoadDefaultView(savedInstanceState);
        setMonth(null);
        loadData();
        switchItem(R.id.item_number);
    }

    @OnClick({R.id.btn_up,R.id.btn_down,R.id.item_number,R.id.item_dispose,R.id.item_type,
            R.id.text_date_today,R.id.btn_search,R.id.item_undispose,R.id.item_count})
    public void onViewClicked(View view){
        switch (view.getId()){
            case R.id.item_number:
            case R.id.item_count:
            case R.id.item_type:
            case R.id.item_dispose:
            case R.id.item_undispose:
                switchItem(view.getId());
                break;
            case R.id.btn_up:

                break;
            case R.id.btn_down:

                break;
            case R.id.text_date_today:
                selectMonth();
                break;
            case R.id.btn_search:

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

    private void setMonth(Date date){

        if (dateDisplay == null){
            dateDisplay = Calendar.getInstance();
            if(date != null){
                dateDisplay.setTime(date);
            }
        }

        mAllYear = getAllYear(dateDisplay);
        mMonthId = dateDisplay.get(Calendar.MONTH);
        monthView.setText(mAllYear.get(mYearId) + "年"+MONTH[mMonthId] + "月");
        setTime();
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
                monthView.setText(year + "年"+MONTH[mMonthId] + "月");
                dateDisplay.set(Calendar.YEAR,year);
                dateDisplay.set(Calendar.MONTH,mMonthId);
                setTime();
            }
        });
        mMonthPickDialog.show();
    }

    private void loadData(){
        addLayoutAnimation(container,R.anim.tran_left);
        String[] datas = {"","","","","","","","","",""};
        for(int i = 0; i < datas.length;i++){
            View view = mInflater.inflate(R.layout.item_warning_statistic,null, false);
            TextView no = view.findViewById(R.id.item_no);
            TextView position = view.findViewById(R.id.item_position);
            TextView type = view.findViewById(R.id.item_warning_type);
            TextView time = view.findViewById(R.id.item_warning_time);
            TextView count = view.findViewById(R.id.item_warning_count);
            ImageView status = view.findViewById(R.id.item_status);
            if(i % 2 == 0){
                status.setImageResource(R.mipmap.warning_finished);
            }else {
                status.setImageResource(R.mipmap.warning_unfinish2);
            }
            count.setText("告警"+(i+1)+"次");
            no.setText((i+1+"."));
            position.setText("告警节点位置"+(i+1));
            time.setText(DateUtil.toDateString(new Date(),DateUtil.DATE_TIME_FORMAT));
            container.addView(view);
        }
    }
}
