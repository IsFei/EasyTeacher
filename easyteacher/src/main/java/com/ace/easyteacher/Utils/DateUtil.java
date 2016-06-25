package com.ace.easyteacher.Utils;

import android.text.TextUtils;
import android.text.format.DateFormat;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    /**
     * Function 判断 yyyy-MM-dd HH-mm-ss 格式的两个时间是否相等，忽略秒
     */
    public static boolean equalsIgnoredSecond(String date1, String date2) {

        String year1 = date1.split(" ")[0];
        String time1 = date1.split(" ")[1].split(":")[0] + ":" + date1.split(" ")[1].split(":")[1];

        String year2 = date2.split(" ")[0];
        String time2 = date2.split(" ")[1].split(":")[0] + ":" + date2.split(" ")[1].split(":")[1];

        if (year1.equals(year2) && time1.equals(time2)) {
            return true;
        } else {
            return false;
        }
    }

    public static String getMonPreviousOrNext(int position) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, position);
        Date now = c.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String str = dateFormat.format(now);
        return str;
    }

    /**
     * 获取以当天开�?前一天或者后�?��
     * 以十二点为分割
     *
     * @param postion
     * @return
     */
    public static String getDatePerviousAndNext(int postion) {

        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, postion);
        Date now = c.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String str = dateFormat.format(now);
        String temp = str + " 12:00:00";
        return temp;
    }

    public static String DateToString(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    public static String DateToString(long date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    public static String formatDate(String date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String str = "";
        try {
            Date date2 = sdf.parse(date);
            str = sdf.format(date2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static String formatDate(String dateStr, boolean isFlag) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("MM.dd");
        String str = null;
        try {
            Date date2 = dateFormat.parse(dateStr);
            if (!isFlag) {
                str = dateFormat1.format(date2);
            } else {
                str = dateFormat.format(date2);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static String formathhmm(String dateStr, boolean isFlag) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("HH:mm");
        // SimpleDateFormat dateFormat2 = new SimpleDateFormat("MM-dd HH:mm");
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("MM-dd");
        String str = null;
        try {
            Date date2 = dateFormat.parse(dateStr);
            if (!isFlag) {
                str = dateFormat1.format(date2);
            } else {
                str = dateFormat2.format(date2);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static String addMinute(String dateStr, int minute) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date begin = null;
        try {
            begin = formatter.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar c = Calendar.getInstance();
        c.setTime(begin);
        c.add(Calendar.MINUTE, minute);

        String next = formatter.format(c.getTime());
        return next;
    }

    /**
     * 返回x的长度
     *
     * @param date
     * @param date1
     * @return
     * @throws ParseException
     */
    public static long getMinuteDiff(String date, String date1) {

        SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date begin = null;
        Date end = null;
        try {
            begin = dfs.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
        try {
            end = dfs.parse(date1);
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
        long between = (end.getTime() - begin.getTime()) / 1000;//
        long minute1 = between / 60;
        return minute1;
    }

    public static String getTime(String number) {
        int num = (int) Float.parseFloat(number);
        int hh = num / 60;
        int mm = num - hh * 60;
        if (hh == 0) {
            return mm + "'";
        }
        return hh + "h" + mm + "'";
    }


    /**
     * 返回x的长度
     *
     * @param date
     * @param date1
     * @return
     * @throws ParseException
     */
    public static long getTimeDiff(String date, String date1) throws ParseException {

        SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date begin = dfs.parse(date);
        Date end = dfs.parse(date1);
        long between = (end.getTime() - begin.getTime()) / 1000;// 除以1000是为了转换成�?

        long day1 = between / (24 * 3600);
        long hour1 = between % (24 * 3600) / 3600;

        // long minute1 = between % 3600 / 60;
        // long second1 = between % 60 / 60;
        return day1 * 24 + hour1;
    }

    public static boolean panduan(String date, String date1) throws ParseException {

        boolean isflag = false;
        SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH");
        Date begin = dfs.parse(date);
        Date end = dfs.parse(date1);

        String str = dfs.format(begin);
        String str1 = dfs.format(end);

        if (str.equals(str1)) {
            isflag = true;
        }

        return isflag;
    }

    public static boolean isEqual(String date, String date1) throws ParseException {
        boolean isflag = false;
        if (TextUtils.isEmpty(date) || TextUtils.isEmpty(date1)) {
            return isflag;
        }
        SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd");
        Date begin = dfs.parse(date);
        Date end = dfs.parse(date1);
        String str = dfs.format(begin);
        String str1 = dfs.format(end);
        if (str.equals(str1)) {
            isflag = true;
        }

        return isflag;
    }

    /**
     * 补齐
     *
     * @return
     * @throws ParseException
     */
    public static String getDatePerviousAndNext(String date, int posion) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date begin = dateFormat.parse(date);
        Calendar c = Calendar.getInstance();
        c.set(begin.getYear() + 1900, begin.getMonth(), begin.getDate());
        c.add(Calendar.DATE, posion);
        Date now = c.getTime();
        String str = dateFormat.format(now);
        String temp = str + " 12:00:00";
        return temp;
    }

    public static int getDayOfMonth(String time) {
        int year = Integer.valueOf(time.split(" ")[0].split("-")[0]);
        int month = Integer.valueOf(time.split(" ")[0].split("-")[1]);
        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month - 1);
        a.set(Calendar.DATE, 1);// 把日期设置为当月第一天
        a.roll(Calendar.DATE, -1);// 日期回滚一天，也就是最后一天
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }

    public static String getDatePerviousAndNextHours(String date, int posion) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH");
        Date begin = dateFormat.parse(date);
        Calendar c = Calendar.getInstance();
        c.set(begin.getYear() + 1900, begin.getMonth(), begin.getDate(), begin.getHours(), 0, 0);
        c.add(Calendar.HOUR, posion);
        Date now = c.getTime();
        String str = dateFormat.format(now);
        String temp = str + ":00";
        return temp;
    }

    public static String formathhmm(String dateStr) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH");
        String str = null;
        try {
            Date date = dateFormat.parse(dateStr);

            str = dateFormat.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str + ":00";
    }

    public static String formathh(String dateStr) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String str = null;
        try {
            Date date = dateFormat.parse(dateStr);

            str = dateFormat.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static long StringTolong(String str_date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long time = 0;
        try {
            Date date = sdf.parse(str_date);
            time = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }

    /**
     * Function 将 yyyy-MM-dd HH:mm:ss 的字串转换为毫秒，注意在转换的时候统一把秒数位当做全零处理
     */
    public static long StringTolongNoSecond(String str_date) {

        String dateStr = str_date.split(" ")[0];
        String timeStr = str_date.split(" ")[1];
        String hour = timeStr.split(":")[0];
        String min = timeStr.split(":")[1];
        str_date = dateStr + " " + hour + ":" + min + ":00";

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long time = 0;
        try {
            Date date = sdf.parse(str_date);
            time = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }


    public static long getMinutes(String srcDate, String desDate) {

        return (StringTolong(desDate) - StringTolong(srcDate)) / (60 * 1000);
    }

    /**
     * Function 获取当前时间，24小时制
     */
    public static String getCurrentTime() {
        return DateFormat.format("yyyy-MM-dd HH:mm:ss", new Date()).toString();
    }

    /**
     * 秒数转换为时间
     */
    public static String ssToTime(String time) {
        long timelong = Long.valueOf(time);
        long allMM = timelong / 60;
        int hour = (int) (allMM / 60);
        int min = (int) (allMM - hour * 60);
        DecimalFormat decimalFormat = new DecimalFormat("00");// 取两位

        String h = decimalFormat.format(hour);// format 返回的是字符串
        String m = decimalFormat.format(min);
        return h + ":" + m;
    }

    /**
     * 获取月和天总共多少天
     */
    public static int getDays(String collect_time) {
        String dates[] = collect_time.split(" ")[0].split("-");
        return Integer.valueOf(dates[1]) * 30 + Integer.valueOf(dates[2]);
    }
}
