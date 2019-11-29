package com.android.zht.waterwatch.util;

import android.text.TextUtils;
import android.util.Log;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2016/9/12.
 */
public class DateUtil {

    private int daysOfMonth;
    private int mDayCount;
    private String mStartDay;
    private String mEndDay;

    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";


    public static String getDayStartTime(long time){
        return toDateString(toDate(time))+" 00:00:00";
    }

    public static String getDayEndTime(long time){
        return toDateString(toDate(time))+" 23:59:59";
    }

    public static Date toDate(long milliseconds) {
        if(milliseconds <= 0L) {
            return null;
        } else {
            try {
                return new Date(milliseconds);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    public static Date toDate(String dateString, String pattern) {
        if(TextUtils.isEmpty(dateString)) {
            return null;
        } else {
            SimpleDateFormat formatter = new SimpleDateFormat(pattern);
            ParsePosition position = new ParsePosition(0);
            Date strtodate = formatter.parse(dateString, position);
            return strtodate;
        }
    }

    public static String toDateString(Date date) {
        return toDateString(date, "yyyy-MM-dd");
    }

    public static String toDateString(Date date, String pattern) {
        if(date == null) {
            return "";
        } else {
            SimpleDateFormat formatter = new SimpleDateFormat(pattern);
            return formatter.format(date);
        }
    }

    public String getStartEndDate(int year, int month) {
        boolean isLeapYear = isLeapYear(year);
        mDayCount = getDaysOfMonth(isLeapYear, month);
        mStartDay = year + "-" + month + "-" + "01";
        mEndDay = year + "-" + month + "-" + mDayCount;

        return null;
    }

    public boolean isLeapYear(int year) {
        if (year % 100 == 0 && year % 400 == 0) {
            return true;
        } else if (year % 100 != 0 && year % 4 == 0) {
            return true;
        }
        return false;
    }

    //得到某月有多少天数
    public int getDaysOfMonth(boolean isLeapyear, int month) {
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                daysOfMonth = 31;
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                daysOfMonth = 30;
                break;
            case 2:
                if (isLeapyear) {
                    daysOfMonth = 29;
                } else {
                    daysOfMonth = 28;
                }

        }
        return daysOfMonth;
    }

    public String getTotalTime(String start, String end) {
        String total = "";
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        try {
            Date d1 = format.parse(start);
            Date d2 = format.parse(end);
            long diff = d1.getTime() - d2.getTime();//这样得到的差值是微秒级别

            long days = diff / (1000 * 60 * 60 * 24);
            long hours = (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
            long minutes = (diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60);

//            System.out.println(""+days+"天"+hours+"小时"+minutes+"分");
            total = days + "天" + hours + "小时" + minutes + "分";
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return total;
    }

    public int isInvalid(String end) {
        int isInvalid = 0;
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Calendar c1 = Calendar.getInstance();
            Calendar c2 = Calendar.getInstance();

            Date d2 = format.parse(end);
            c2.setTime(d2);

            int result = c1.compareTo(c2);
//            Log.e("wangyl", "DateUtil---isInvalid---end  = " + end + "----c2 = " + c2
//                    + "----result = " + result);
            if (result == 0) {
//                Log.e("wangyl", "c1相等c2");
                isInvalid = 0;
            } else if (result < 0) {
//                Log.e("wangyl", "c1小于c2");//结束时间大于现在，未过期
                isInvalid = -1;
            } else {
//                Log.e("wangyl", "c1大于c2"); //结束时间小于现在，已过期
                isInvalid = 1;
            }
        } catch (Exception e) {
            Log.e("wagyl", "isInValid Exception" );
            e.printStackTrace();
        }
        return isInvalid;

    }

    public int getmDayCount() {
        return mDayCount;
    }

    public String getmStartDay() {
        return mStartDay;
    }

    public String getmEndDay() {
        return mEndDay;
    }
}
