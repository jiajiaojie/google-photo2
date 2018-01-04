package com.example.jiaojiejia.googlephoto.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtil {

    public static final String DATE_FORMATER_FULL_SPECIAL_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String DATE_FORMATER_FULL_SPECIAL_PATTERN_2 = "yyyy-MM-dd'T'00:00:00";
    public static final String SIGNATURE_FORMAT_TIME = "EEE, dd MMM y HH:mm:ss 'GMT'";
    private static String[] WEEKS = {"星期日","星期一","星期二","星期三","星期四","星期五","星期六"};
    public static final String DATE_FORMATER_SIMPLE_PATTERN_LINE = "yyyy年MM月dd日";
    public static final String  DATE_FORMATE_STANDER = "yyyy-MM-dd HH:mm:ss";


    /**
     * 转换时间
     */
    public static String formatUtcTime(long timeInMillis) {
        Date date = new Date(timeInMillis);
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMATER_FULL_SPECIAL_PATTERN);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf.format(date);
    }

    /**
     * 转换时间
     */
    public static Calendar parseUTCTime(String utcDateStr) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMATER_FULL_SPECIAL_PATTERN);
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = sdf.parse(utcDateStr);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            return calendar;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 获得当前的日期
     */
    public static String getGMTString() {
        SimpleDateFormat sdf = new SimpleDateFormat(SIGNATURE_FORMAT_TIME, Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        return sdf.format(new Date());
    }

    /** 获取指定日期为星期几 */
    public static String getWeek(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int week_index = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if(week_index<0){
            week_index = 0;
        }
        return WEEKS[week_index];
    }

    /**
     * 根据日期计算距今天数
     * @param source
     * @return
     */
    public static int daysAsYet(String source) {

        Calendar calst = parseUTCTime(source);
        Calendar caled = Calendar.getInstance();
        if (calst != null && caled != null) {
            calst.set(Calendar.HOUR_OF_DAY, 0);
            calst.set(Calendar.MINUTE, 0);
            calst.set(Calendar.SECOND, 0);
            caled.set(Calendar.HOUR_OF_DAY, 0);
            caled.set(Calendar.MINUTE, 0);
            caled.set(Calendar.SECOND, 0);
            //得到两个日期相差的天数
            return ((int) (caled.getTime().getTime() / 1000) - (int) (calst
                    .getTime().getTime() / 1000)) / 3600 / 24 + 1;
        } else {
            return 0;
        }
    }

    public static String formateMillisecond(long second) {
        SimpleDateFormat mDataFormatOfDay = new SimpleDateFormat(DATE_FORMATER_FULL_SPECIAL_PATTERN);
        mDataFormatOfDay.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = new Date(second);
        return mDataFormatOfDay.format(date);
    }

    public static String formateDate(Date date) {
        SimpleDateFormat mDataFormatOfDay = new SimpleDateFormat(DATE_FORMATER_FULL_SPECIAL_PATTERN_2);
        mDataFormatOfDay.setTimeZone(TimeZone.getTimeZone("UTC"));
        return mDataFormatOfDay.format(date);
    }

}
