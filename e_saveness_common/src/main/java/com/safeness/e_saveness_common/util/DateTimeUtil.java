package com.safeness.e_saveness_common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * Created by EISAVISA on 2015/1/24.
 *
 * 日期格式化工具类
 */
public class DateTimeUtil {



    public static final  String NORMAL_PATTERN ="yyyy-MM-dd HH:mm:ss";
    public static String getNowDate(){
        //SimpleDateFormat formatter = (SimpleDateFormat) SimpleDateFormat.getDateTimeInstance();
        SimpleDateFormat formatter   =   new   SimpleDateFormat(NORMAL_PATTERN);
        Date   curDate   =   new   Date(System.currentTimeMillis());//获取当前时间
        String   str   =   formatter.format(curDate);
        return str;
    }

    public static  Calendar getSelectCalendar(String dateStr,String pattern ) throws ParseException {
        SimpleDateFormat   formatter   =   new   SimpleDateFormat(pattern);

        Date date = formatter.parse(dateStr);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;

    }


    /**
     * 获取格式化字符串
     * @param calendar
     * @return
     */
    public static String getSelectedDate(Calendar calendar,String pattern){

        SimpleDateFormat   formatter   =   new   SimpleDateFormat(pattern);
        Date   curDate   =   calendar.getTime();
        String   str   =   formatter.format(curDate);
        return str;
    }







}
