package com.android.zht.waterwatch.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;


import com.android.zht.waterwatch.R;

import java.util.Calendar;


/**
 * Created by hjh on 2015/8/31.
 */
public class MyDatePicker extends AlertDialog implements OnDateChangedListener {


    private final DatePicker mDatePicker;
    private final OnDateSetListener mCallBack;
    private View view;
    private boolean minToday;

    public MyDatePicker(Context context, OnDateSetListener callBack, int year,
                        int monthOfYear, int dayOfMonth) {
        this(context, 0, callBack, year, monthOfYear, dayOfMonth);
    }

    public MyDatePicker(Context context, int theme, OnDateSetListener callBack,
                        int year, int monthOfYear, int dayOfMonth) {
        super(context, theme);
        mCallBack = callBack;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.datedialog, null);
        mDatePicker = (DatePicker) view.findViewById(R.id.datePicker);
        mDatePicker.init(year, monthOfYear, dayOfMonth, this);
//        mDatePicker.setMaxDate(Calendar.getInstance().getTimeInMillis());

        clickButton();
    }

    public MyDatePicker setMaxDate(long time){
        mDatePicker.setMaxDate(time);
        return this;
    }

    public MyDatePicker setMinDate(long time){
        mDatePicker.setMinDate(time);
        return this;
    }

    public void clickButton() {
        view.findViewById(R.id.okBtn).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mCallBack != null) {
                            mDatePicker.clearFocus();
                            mCallBack.onDateSet(mDatePicker,
                                    mDatePicker.getYear(),
                                    mDatePicker.getMonth(),
                                    mDatePicker.getDayOfMonth());
                        }
                        dismiss();
                    }
                });

        view.findViewById(R.id.cancelBtn).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismiss();
                    }
                });
    }

    public void myShow() {
        show();
        setContentView(view);
    }

    //判断选择的日期是否大于今天
    private boolean isDateAfter(DatePicker tempView) {
        Calendar mCalendar = Calendar.getInstance();
        Calendar tempCalendar = Calendar.getInstance();
        tempCalendar.set(tempView.getYear(), tempView.getMonth(), tempView.getDayOfMonth(), 0, 0, 0);
        if(tempCalendar.after(mCalendar))
            return true;
        else
            return false;
    }

    @Override
    public void onDateChanged(DatePicker view, int year, int month, int day) {
        mDatePicker.init(year, month, day, this);
        if(isMinToday() && !isDateAfter(view)){//如果小于今天，设置成今天
            Calendar mCalendar = Calendar.getInstance();
            view.init(mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH), this);
        }
    }

    public interface OnDateSetListener {
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth);
    }

    public boolean isMinToday() {
        return minToday;
    }

    public void setMinToday(boolean minToday) {
        this.minToday = minToday;
    }
}
