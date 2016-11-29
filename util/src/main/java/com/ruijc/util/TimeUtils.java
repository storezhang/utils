/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ruijc.util;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间常量
 *
 * @author storezhang
 */
public class TimeUtils {

    public static final long SEC_MILLIS = 1 * 1000;
    public static final long MIN_MILLIS = 60 * SEC_MILLIS;
    public static final long HOUR_MILLIS = 60 * MIN_MILLIS;
    public static final long DAY_MILLIS = 24 * HOUR_MILLIS;
    public static final long WEEK_MILLIS = 7 * DAY_MILLIS;
    public static final long MONTH_MILLIS = 30 * DAY_MILLIS;
    public static final long YEAR_MILLIS = 365 * DAY_MILLIS;

    public static final int SEC = 1;
    public static final int MIN_SEC = 60 * SEC;
    public static final int HOUR_SEC = 60 * MIN_SEC;
    public static final int DAY_SEC = 24 * HOUR_SEC;
    public static final int WEEK_SE = 7 * DAY_SEC;
    public static final int MONTH_SEC = 30 * DAY_SEC;
    public static final int YEAR_SEC = 365 * DAY_SEC;

    public static int days(Date time) {
        long startTim = time.getTime();
        long endTim = System.currentTimeMillis();
        long diff = endTim - startTim;
        int days = (int) (diff / 1000 / 3600 / 24);
        return days;
    }

    public static String time(Integer time) {
        return time((long) time);
    }

    public static String time(Long time) {
        long hour = time / HOUR_MILLIS;
        time %= HOUR_MILLIS;
        long min = time / MIN_MILLIS;
        time %= MIN_MILLIS;
        long sec = time / SEC_MILLIS;
        time %= SEC_MILLIS;
        return String.format("%02d:%02d:%02d", hour, min, sec);
    }

    // 获得当天0点时间
    public static Date todayStart() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    // 获得当天24点时间
    public static Date todayEnd() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 24);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    //昨天开始时间
    public static Date yesterdayStart() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    //昨天结束时间
    public static Date yesterdayEnd() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    // 获得本周一0点时间
    public static Date thisWeekStart() {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return cal.getTime();
    }

    // 获得本周日24点时间
    public static Date thisWeekEnd() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(thisWeekStart());
        cal.add(Calendar.DAY_OF_WEEK, 7);
        return cal.getTime();
    }

    // 获得本月第一天0点时间
    public static Date thisMonthStart() {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY),
                cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        return cal.getTime();
    }

    // 获得本月最后一天24点时间
    public static Date thisMonthEnd() {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY),
                cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, 24);
        return cal.getTime();
    }

    //获得今年最后一天的日期
    public static Date thisYearEnd() {
        Calendar lastDate = Calendar.getInstance();
        int year = lastDate.get(Calendar.YEAR);
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return format.parse(year + "-12-31 23:59:59");
        } catch (ParseException ex) {
            return null;
        }
    }

    //获得今年第一天的日期
    public static Date thisYearStart() {
        Calendar lastDate = Calendar.getInstance();
        int year = lastDate.get(Calendar.YEAR);
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return format.parse(year + "-01-01 00:00:00");
        } catch (ParseException ex) {
            return null;
        }
    }

    //获得去年最后一天的日期
    public static Date lastYearEnd() {
        Calendar lastDate = Calendar.getInstance();
        int year = lastDate.get(Calendar.YEAR) - 1;
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return format.parse(year + "-12-31 23:59:59");
        } catch (ParseException ex) {
            return null;
        }
    }

    //获得去年第一天的日期
    public static Date lastYearStart() {
        Calendar lastDate = Calendar.getInstance();
        int year = lastDate.get(Calendar.YEAR) - 1;
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return format.parse(year + "-01-01 00:00:00");
        } catch (ParseException ex) {
            return null;
        }
    }

    /**
     * 修改系统时间
     *
     * @param date 时间
     * @return 是否修改成功
     */
    public static boolean setSystemTime(Date date) {
        String osName = System.getProperty("os.name");
        String cmd;
        SimpleDateFormat dateFormat;
        SimpleDateFormat timeFormat;
        try {
            if (osName.matches("^(?i)Windows.*$")) {// Window系统
                dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                timeFormat = new SimpleDateFormat("HH:mm:ss");
                cmd = String.format("cmd /c time %s", timeFormat.format(date));
                Runtime.getRuntime().exec(cmd);
                cmd = String.format("cmd /c date %s", dateFormat.format(date));
                Runtime.getRuntime().exec(cmd);
            } else {// Linux系统
                dateFormat = new SimpleDateFormat("yyyyMMdd");
                timeFormat = new SimpleDateFormat("HH:mm:ss");
                cmd = String.format("date -s %s", dateFormat.format(date));
                Runtime.getRuntime().exec(cmd);
                cmd = String.format("date -s %s", timeFormat.format(date));
                Runtime.getRuntime().exec(cmd);
            }
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        System.out.println("---->" + time(753000));
    }
}
