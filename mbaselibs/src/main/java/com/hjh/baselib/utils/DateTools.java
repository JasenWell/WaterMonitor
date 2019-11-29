package com.hjh.baselib.utils;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public final class DateTools {

		// ��
		public static final String YEAR_FORMAT = "yyyy";
		
		/**
		 *  ���ڸ�ʽyyyy-MM-dd
		 */
		public static final String DATE_FORMAT = "yyyy-MM-dd";
		
		/**
		 *  ���ڸ�ʽMM-dd
		 */
		public static final String DATE_MONTH = "MM-dd";
		
		/**
		 *  ���ڸ�ʽHH:mm
		 */
		public static final String TIME_FORMAT = "HH:mm";
		
		/**
		 *  ����ʱ���ʽyyyy-MM-dd HH:mm:ss
		 */
		public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
		
		/**
		 * ��ȷ������yyyy-MM-dd HH:mm:ss SSS
		 */
		public static final String MS_FORMART = "yyyy-MM-dd HH:mm:ss SSS";
		
		/**
		 *  ��������ʱ���ʽyyyy-MM-dd HH:mm
		 */
	public static final String PART_TIME_FORMAT = "yyyy-MM-dd HH:mm";
	public static final String DATE_PARRERN = "\\d{1,4}\\-\\d{1,2}\\-\\d{1,2}";
	public static final String DATE_TIME_PATTERN = "\\d{1,4}\\-\\d{1,2}\\-\\d{1,2}\\s+\\d{1,2}:\\d{1,2}:\\d{1,2}";
	
	/**
	 * yyyy-MM-dd HH:mm��ʽ
	 */
	public static final SimpleDateFormat ACTIVITY_FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm");
	/**
	 * HH:mm��ʽ
	 */
	public static final SimpleDateFormat SHORT_FORMAT = new SimpleDateFormat("HH:mm");
	
	private DateTools() {

	}

	/**
	 * 时间转换为long
	 * @param date
	 * @return
	 */
	public static String dateFormartToLong(String date){
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sf.format(new Date(Long.valueOf(date)));
	}


	public static Date toDate(String dateString) {

		if (StringTools.matches(dateString, DATE_PARRERN)) {
			return toDate(dateString, DATE_FORMAT);
		}
		if (StringTools.matches(dateString, DATE_TIME_PATTERN)) {
			return toDate(dateString, DATE_TIME_FORMAT);
		}
		return null;
	}

	public static Date toDate(long milliseconds) {

		if (milliseconds <= 0)
			return null;

		try {
			return new Date(milliseconds);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Long getDaysBetween(Date endDate, Date startDate) {

		if (endDate == null || startDate == null) {
			return -1L;
		}

		Calendar fromCalendar = Calendar.getInstance();
		fromCalendar.setTime(startDate);
		fromCalendar.set(Calendar.HOUR_OF_DAY, 0);
		fromCalendar.set(Calendar.MINUTE, 0);
		fromCalendar.set(Calendar.SECOND, 0);
		fromCalendar.set(Calendar.MILLISECOND, 0);

		Calendar toCalendar = Calendar.getInstance();
		toCalendar.setTime(endDate);
		toCalendar.set(Calendar.HOUR_OF_DAY, 0);
		toCalendar.set(Calendar.MINUTE, 0);
		toCalendar.set(Calendar.SECOND, 0);
		toCalendar.set(Calendar.MILLISECOND, 0);

		return (toCalendar.getTime().getTime() - fromCalendar.getTime()
				.getTime()) / (1000 * 60 * 60 * 24);
	}

	/**
	 * 群活动时间转换器
	 * 
	 * @param dateStr
	 * @return
	 */
	public static Date ActivityDateParse(String dateStr) {
		Date date = null;
		try {
			date = ACTIVITY_FORMAT.parse(dateStr);
		} catch (ParseException e) {

		}
		return date;
	}

	public static Date toDate(String dateString, String pattern) {

		if (StringTools.isEmpty(dateString)) {
			return null;
		}

		SimpleDateFormat formatter = new SimpleDateFormat(pattern);
		ParsePosition position = new ParsePosition(0);
		Date strtodate = formatter.parse(dateString, position);
		return strtodate;
	}

	public static String toDateString(Date date) {
		return toDateString(date, DATE_FORMAT);
	}

	public static String toDateString(Date date, String pattern) {

		if (date == null) {
			return "";
		}

		SimpleDateFormat formatter = new SimpleDateFormat(pattern);
		return formatter.format(date);
	}

	public static String pad(int string) {
		if (string >= 10) {
			return String.valueOf(string);
		} else {
			return "0" + String.valueOf(string);
		}
	}

	public static Date getCurrtentTimes() {
		Calendar calendar = Calendar.getInstance(TimeZone
				.getTimeZone("GMT+08:00"));
		Date date = calendar.getTime();
		return date;
	}

	public static int[] getDateTime(Date date) {
		Calendar calendar = Calendar.getInstance(TimeZone
				.getTimeZone("GMT+08:00"));
		int year = 0, month = 0, day = 0, hour = 0, min = 0;
		calendar.setTime(date);
		year = calendar.get(Calendar.YEAR);
		month = calendar.get(Calendar.MONTH) + 1;
		day = calendar.get(Calendar.DAY_OF_MONTH);
		hour = calendar.get(Calendar.HOUR_OF_DAY);
		min = calendar.get(Calendar.MINUTE);
		int[] times = { year, month, day, hour, min };
		return times;
	}

	public static long compare(Date d1, Date d2) {

		if (d1 == null) {
			return -1;
		}
		if (d2 == null) {
			return -1;
		}

		return d1.getTime() - d2.getTime();
	}

	public static Date countDate(Date date, int type, int num) {

		if (num < 1) {
			return date;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(type, num);
		return calendar.getTime();
	}

	public static int getYear(Date date) {

		if (date == null) {
			return getCurrentYear();
		}

		SimpleDateFormat year = new SimpleDateFormat(YEAR_FORMAT);

		try {
			return Integer.valueOf(year.format(date));
		} catch (Exception e) {
			return 2013;
		}
	}

	public static int getMonth(Date date) {
		Calendar cl = Calendar.getInstance();
		cl.setTime(date);
		return cl.get(Calendar.MONTH) + 1;
	}

	public static int getDay(Date date) {
		Calendar cl = Calendar.getInstance();
		cl.setTime(date);
		return cl.get(Calendar.DAY_OF_MONTH);
	}
	
	public static String getShortTime(Date date) {
		return date != null ? SHORT_FORMAT.format(date) : "";
	}

	public static int getCurrentYear() {
		return Calendar.getInstance().get(Calendar.YEAR);
	}

	public static Date addSub(Date date, int num) {
		
		if(date == null){
			date	= getCurrtentTimes() ;
		}
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, num);
		
		return calendar.getTime();
	}
	
	public static String getWeekOfDate(Date dt) {
		
		 String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
       
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        
        if (w < 0)
            w = 0;
        
        return weekDays[w];
    }
	
	public static String getWeekOfDate(Calendar calendar){
		String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
		 int w = calendar.get(Calendar.DAY_OF_WEEK) - 1;
	        
        if (w < 0){
            w = 0;
        }
        
        return weekDays[w];
	}
}
