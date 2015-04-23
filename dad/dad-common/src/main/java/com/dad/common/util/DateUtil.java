package com.dad.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
	public static String simpleDatefmt = "yyyyMMddHHmmss";
	
	public static String commonDatefmt = "yyyy-MM-dd HH:mm:ss";

	public static String hourFmt = "yyyyMMddHH";

	public static String hourEnd = "5959";

	public static String dayFmt = "yyyyMMdd";

	public static String dayEnd = "235959";

	public static String mouthFmt = "yyyyMM";

	public static Date getDateByString(String date, String fmt)
			throws ParseException {
		return new SimpleDateFormat(fmt).parse(date);
	}
	
	public static String getStringByDate(Date date, String fmt) {
		return new SimpleDateFormat(fmt).format(date);
	}

	public static Date getLastTimeOfMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		return calendar.getTime();
	}
	
	public static Date getLastTimeOfDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		return calendar.getTime();
	}
	
	public static Date getLastTimeOfHour(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		return calendar.getTime();
	}
	
}
