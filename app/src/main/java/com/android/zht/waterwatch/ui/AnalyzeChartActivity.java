package com.android.zht.waterwatch.ui;


import android.graphics.Color;
import android.os.Bundle;

import com.android.zht.waterwatch.R;
import com.android.zht.waterwatch.widget.AppTitleLayout;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.hjh.baselib.base.LBaseActivity;

import java.util.ArrayList;

import butterknife.BindView;

public class AnalyzeChartActivity extends LBaseActivity {

    @BindView(R.id.title_bar)
    AppTitleLayout mTitleLayout;

    @BindView(R.id.barchart)
    BarChart mChart;//https://www.jianshu.com/p/c6e8ea5e9ba0

    @Override
    public int getContentLayout() {
        return R.layout.activity_analyze_chart;
    }

    @Override
    public void onAfterCreate(Bundle savedInstanceState) {
        super.onAfterCreate(savedInstanceState);
        mTitleLayout.setTitleText("用水分析");
        mTitleLayout.enableLeftButton();
        mTitleLayout.setTitleClickListener(this);

        BarData mBarData = getBarData(12, 100);
        showBarChart(mChart, mBarData);
    }

    private BarData getBarData(int count, float range) {
        ArrayList<BarEntry> yValues = new ArrayList<>();
        for (int i = 0; i< count; i++) {
            float value = (float) (Math.random() * range/*100以内的随机数*/) + 3;
            yValues.add(new BarEntry(i, value));
        }
        // y轴的数据集合
        BarDataSet barDataSet = new BarDataSet(yValues, "测试饼状图");
        barDataSet.setColor(Color.rgb(114, 188, 223));
        BarData barData = new BarData(barDataSet);
        return barData;
    }

//    private void ss(){//https://github.com/zhuanghongji/mp-android-chart/blob/master/MPChartExample/app/src/main/java/com/zhuanghongji/mpchartexample/BarChartActivity.java
//        mChart.setDrawBarShadow(false);
//        mChart.setDrawValueAboveBar(true);
//
//        mChart.getDescription().setEnabled(false);
//
//        // if more than 60 entries are displayed in the chart, no values will be
//        // drawn
//        mChart.setMaxVisibleValueCount(60);
//
//        // scaling can now only be done on x- and y-axis separately
//        mChart.setPinchZoom(false);
//
//        mChart.setDrawGridBackground(false);
//        // mChart.setDrawYLabels(false);
//
//        IAxisValueFormatter xAxisFormatter = new DayAxisValueFormatter(mChart);
//
//        XAxis xAxis = mChart.getXAxis();
//        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//        xAxis.setTypeface(mTfLight);
//        xAxis.setDrawGridLines(false);
//        xAxis.setGranularity(1f); // only intervals of 1 day
//        xAxis.setLabelCount(7);
//        xAxis.setValueFormatter(xAxisFormatter);
//
//        IAxisValueFormatter custom = new MyAxisValueFormatter();
//
//        YAxis leftAxis = mChart.getAxisLeft();
//        leftAxis.setTypeface(mTfLight);
//        leftAxis.setLabelCount(8, false);
//        leftAxis.setValueFormatter(custom);
//        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
//        leftAxis.setSpaceTop(15f);
//        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
//
//        YAxis rightAxis = mChart.getAxisRight();
//        rightAxis.setDrawGridLines(false);
//        rightAxis.setTypeface(mTfLight);
//        rightAxis.setLabelCount(8, false);
//        rightAxis.setValueFormatter(custom);
//        rightAxis.setSpaceTop(15f);
//        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
//
//        Legend l = mChart.getLegend();
//        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
//        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
//        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
//        l.setDrawInside(false);
//        l.setForm(Legend.LegendForm.SQUARE);
//        l.setFormSize(9f);
//        l.setTextSize(11f);
//        l.setXEntrySpace(4f);
//        // l.setExtra(ColorTemplate.VORDIPLOM_COLORS, new String[] { "abc",
//        // "def", "ghj", "ikl", "mno" });
//        // l.setCustom(ColorTemplate.VORDIPLOM_COLORS, new String[] { "abc",
//        // "def", "ghj", "ikl", "mno" });
//
//        XYMarkerView mv = new XYMarkerView(this, xAxisFormatter);
//        mv.setChartView(mChart); // For bounds control
//        mChart.setMarker(mv); // Set the marker to the chart
//    }

    private void showBarChart(BarChart barChart, BarData barData) {//https://blog.csdn.net/weixin_40595516/article/details/82860790
        // 设置 条形图 简介
        Description description = new Description();
        // 可以自定义 位置
        // description.setPosition(200, 200);
        // 也可以 根据 X 轴，Y 轴进行偏移量设置
        description.setXOffset(50f);
        description.setYOffset(10f);
        description.setText("我是 description");
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
        // 设置 x 坐标轴 颜色
        xAxis.setAxisLineColor(Color.RED);
        // 设置 x 坐标轴 宽度
        xAxis.setAxisLineWidth(2f);
        // 设置 x轴 的刻度数量
        xAxis.setLabelCount(10);

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
        mLAxis.setLabelCount(10);

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

        barChart.setData(barData); // 设置数据
        barChart.animateX(2500); // 立即执行的动画,x轴
    }
}
