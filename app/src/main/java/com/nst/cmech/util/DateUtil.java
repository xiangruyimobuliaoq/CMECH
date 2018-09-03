package com.nst.cmech.util;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 日期时间工具类
 *
 * @description : 对日期时间的操作封装
 */
public class DateUtil {
    public static final String YMD = "yyyy-MM-dd";
    public static final String YMD_HMS = "yyyy-MM-dd HH:mm:ss";

    private static ThreadLocal<SimpleDateFormat> sdfThreadLocal = new ThreadLocal<SimpleDateFormat>();

    private static ThreadLocal<SimpleDateFormat> hmSdfThreadLocal = new ThreadLocal<SimpleDateFormat>();

    private final static long ratio = 24 * 60 * 60 * 1000;

    public static String clanderTodatetime(Calendar calendar, String style) {
        SimpleDateFormat formatter = new SimpleDateFormat(style);
        return formatter.format(calendar.getTime());
    }


    private static SimpleDateFormat getSdf() {
        SimpleDateFormat sdf = sdfThreadLocal.get();
        if (sdf == null) {
            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            sdfThreadLocal.set(sdf);
        }
        return sdf;
    }

    private static SimpleDateFormat getSdf5() {
        SimpleDateFormat sdf = sdfThreadLocal.get();
        if (sdf == null) {
            sdf = new SimpleDateFormat("yyyy-MM", Locale.getDefault());
            sdfThreadLocal.set(sdf);
        }
        return sdf;
    }

    private static SimpleDateFormat getSdf1() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return sdf;
    }

    private static SimpleDateFormat getSdf2() {
        SimpleDateFormat sdf = hmSdfThreadLocal.get();
        if (sdf == null) {
            sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            hmSdfThreadLocal.set(sdf);
        }
        return sdf;
    }

    private static SimpleDateFormat getSdf3() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        return sdf;
    }

    private static SimpleDateFormat getSdf4() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
        return sdf;
    }

    // 时间差
    private static long span = 0;

    public static long getSpan() {
        return span;
    }

    public static void setSpan(long span) {
        DateUtil.span = span;
    }

    // 获取一个日期对象
    public static Date getDate() {
        return new Date();
    }

    // 获取一个日历对象
    public static Calendar getCalendar() {
        return Calendar.getInstance();
    }

    // 获取系统时间
    public static Date getSystemDateTime() {
        return new Date(getDate().getTime() + span);
    }

    // 获取格式化后的系统时间
    public static String getSystemALLTimeString() {
        return getSdf().format(getSystemDateTime());
    }

    public static String getSystemYYYYMMString() {
        return getSdf5().format(getSystemDateTime());
    }

    public static String getSystemDateTimeString() {
        return getSdf2().format(getSystemDateTime());
    }


    public static String getSystemDateTimeHHMMString() {
        return getSdf1().format(getSystemDateTime());
    }

    public static String getSystemDateTimeYYMMDDString() {
        return getSdf3().format(getSystemDateTime());
    }

    public static Calendar getSystemCalendar() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getSystemDateTime());
        return cal;
    }

    public static Calendar getSystemCalendar(Calendar calendar) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getSystemDateTime(calendar.getTime()));
        return cal;
    }

    public static Date getSystemDateTime(Date date) {
        return new Date(date.getTime() - span);
    }

    public static String getDateString(long dateTime, String formatStr) {
        return getSimpleDateFormat(formatStr).format(dateTime);
    }

    public static String getDateString(Date dateTime, String formatStr) {
        return getSimpleDateFormat(formatStr).format(dateTime);
    }

    public static String getDateString(Calendar dateTime, String formatStr) {
        return getSimpleDateFormat(formatStr).format(dateTime.getTime());
    }

    public static SimpleDateFormat getSimpleDateFormat(String formatStr) {
        return new SimpleDateFormat(formatStr, Locale.getDefault());
    }

    public static String getCalendarStringByOffset(String oldTime, int Offset) {
        Date oldDate = getSimpleDateByParse(oldTime);
        Date value = new Date(oldDate.getTime() - Offset * ratio);
        return getDateString(value.getTime(), "yyyy-MM-dd HH:MM:SS");
    }

    public static String getDateStringByOffset(String oldTime, int Offset) {
        Date oldDate = getDateByParse(oldTime);
        Date value = new Date(oldDate.getTime() - Offset * ratio);
        return getDateString(value.getTime(), "yyyy-MM-dd");
    }

//    public static String getCalendarStringByOffset(int calendarFieldValue, int Offset) {
//        Calendar calendar = getCalendar();
//        calendar.addGoods(calendarFieldValue, Offset);
//        if (calendarFieldValue == Calendar.WEEK_OF_YEAR) {
//            calendar.addGoods(Calendar.DAY_OF_YEAR, -(calendar.get(Calendar.DAY_OF_WEEK) == 1 ? 8 : calendar.get(Calendar.DAY_OF_WEEK)) + 2);
//        }
//        return getDateString(calendar.getTime(), "yyyy-MM-dd 00:00:00");
//    }

    public static String getSimpleDateString(Date dateTime) {
        return getSdf().format(dateTime);
    }

    public static String getSimpleDateString(String dateTime) {
        return getSdf().format(getSimpleDateByParse(dateTime));
    }

    public static String getSdf4DateString(String dateTime) {
        return getSdf().format(getSdf4Date(dateTime));
    }

    public static Date getSdf4Date(String dateTime) {
        Date date = null;
        try {
            date = getSdf4().parse(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static Date getSimpleDateByParse(String dateTime) {
        Date date = null;
        try {
            date = getSdf().parse(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static Date getDateByParse(String dateTime) {
        Date date = null;
        try {
            date = getSdf2().parse(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static Date getSimpleDateByParse(String dateTime, String formatStr) {
        Date date = null;
        try {
            date = getSimpleDateFormat(formatStr).parse(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 从yyyy-MM-dd HH:mm:ss 格式的时间里面取出 ddHHmmss
     */
    public static String getHHmmssByDateTime(String dateTime) {
        String[] datesplite = dateTime.split(" ");
        String[] before = datesplite[0].split("-");
        String[] after = datesplite[1].split(":");

        StringBuilder builder = new StringBuilder();
        builder.append(before[2]).append(after[0]).append(after[1]).append(after[2]);
        return builder.toString();
    }

    public static int getAge(String birthday) throws Exception {
        return getAge(getDateByParse(birthday));


    }

    public static int getAge(Date birthDay) throws Exception {
        Calendar cal = Calendar.getInstance();

        if (cal.before(birthDay)) {
            throw new IllegalArgumentException(
                    "The birthDay is before Now.It's unbelievable!");
        }

        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH) + 1;
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);

        cal.setTime(birthDay);
        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH);
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

        int age = yearNow - yearBirth;

        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                //monthNow==monthBirth 
                if (dayOfMonthNow < dayOfMonthBirth) {
                    age--;
                }
            } else {
                //monthNow>monthBirth 
                age--;
            }
        }

        return age;
    }

    public static long getMinDate(long maxDate) {
        Date date = new Date(maxDate);
        Calendar calendar = Calendar.getInstance();//日历对象
        calendar.setTime(date);//设置当前日期
        calendar.add(Calendar.MONTH, -1);//月份减一
        date = calendar.getTime();
        return date.getTime();
    }

    public static long getMaxDate(long minDate) {
        Date date = new Date(minDate);
        Calendar calendar = Calendar.getInstance();//日历对象
        calendar.setTime(date);//设置当前日期
        calendar.add(Calendar.MONTH, 1);//月份减一
        date = calendar.getTime();
        return date.getTime();
    }

    public static long getLastSevenDay() {
        Date date = new Date(System.currentTimeMillis());
        Calendar calendar = Calendar.getInstance();//日历对象
        calendar.setTime(date);//设置当前日期
        calendar.add(Calendar.DATE, -6);
        calendar.set(Calendar.HOUR_OF_DAY, calendar.getMinimum(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE, calendar.getMinimum(Calendar.MINUTE));
        calendar.set(Calendar.SECOND, calendar.getMinimum(Calendar.SECOND));
        calendar.set(Calendar.MILLISECOND, calendar.getMinimum(Calendar.MILLISECOND));
        //date=calendar.getTime();
        return calendar.getTimeInMillis();
    }

    public static long getNextSevenDay() {
        Date date = new Date(System.currentTimeMillis());
        Calendar calendar = Calendar.getInstance();//日历对象
        calendar.setTime(date);//设置当前日期
        calendar.add(Calendar.DATE, 6);
        date = calendar.getTime();
        return date.getTime();
    }

    public static long getFrontMonthDay() {
        Date date = new Date(System.currentTimeMillis());
        Calendar calendar = Calendar.getInstance();//日历对象
        calendar.setTime(date);//设置当前日期
        calendar.add(Calendar.DATE, 66);
        date = calendar.getTime();
        return date.getTime();
    }

    public static long getNowDay() {
        Date date = new Date(System.currentTimeMillis());
        Calendar calendar = Calendar.getInstance();//日历对象
        calendar.setTime(date);//设置当前日期
        calendar.set(Calendar.HOUR_OF_DAY, calendar.getMaximum(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE, calendar.getMaximum(Calendar.MINUTE));
        calendar.set(Calendar.SECOND, calendar.getMaximum(Calendar.SECOND));
        calendar.set(Calendar.MILLISECOND, calendar.getMaximum(Calendar.MILLISECOND));
        //date=calendar.getTime();
        return calendar.getTimeInMillis();
    }

    public static String getSimpleDate2String(Date dateTime) {
        return getSdf2().format(dateTime);
    }

    public static String getSimpleDate3String(Date dateTime) {
        return getSdf3().format(dateTime);
    }

    public static String getSaleTime() {
        return getSdf4().format(getDate());
    }

    public static String getSaleTime(Date date) {
        return getSdf4().format(getDate());
    }

    public static Date getDateTimeByParse(String dateTime) {
        Date date = null;
        try {
            date = getSdf().parse(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /*
  * 将时间戳转换为时间
  */
    public static String stampToDate(String s) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    public static void setDate(final Context context, final String format, final Callback callback) {
        Calendar d = Calendar.getInstance(Locale.CHINA);
        Date myDate = new Date();
        d.setTime(myDate);
        final int year = d.get(Calendar.YEAR);
        final int month = d.get(Calendar.MONTH);
        final int day = d.get(Calendar.DAY_OF_MONTH);
        final DatePickerDialog dlg = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, final int year, final int month, final int dayOfMonth) {
                Calendar c = Calendar.getInstance();
                c.set(year, month, dayOfMonth);
                String result = new SimpleDateFormat(format).format(c.getTime());
                callback.callback(result);
            }
        }, year, month, day);
        dlg.show();
    }

    public static void setTime(final Context context, final String format, final Callback callback) {
        Calendar d = Calendar.getInstance(Locale.CHINA);
        Date myDate = new Date();
        d.setTime(myDate);
        int year = d.get(Calendar.YEAR);
        int month = d.get(Calendar.MONTH);
        int day = d.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dlg = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, final int year, final int month, final int dayOfMonth) {

                TimePickerDialog timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Calendar c = Calendar.getInstance();
                        c.set(year, month, dayOfMonth, hourOfDay, minute);
                        String result = new SimpleDateFormat(format).format(c.getTime());
                        callback.callback(result);

                    }
                }, 0, 0, true);
                timePickerDialog.show();
            }
        }, year, month, day);
        dlg.show();
    }

    public interface Callback {
        void callback(String result);
    }


}
