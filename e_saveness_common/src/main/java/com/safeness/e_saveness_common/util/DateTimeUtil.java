package com.safeness.e_saveness_common.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * Created by EISAVISA on 2015/1/24.
 */
public class DateTimeUtil {



    public static String getNowDate(){
        //SimpleDateFormat formatter = (SimpleDateFormat) SimpleDateFormat.getDateTimeInstance();
        SimpleDateFormat formatter   =   new   SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date   curDate   =   new   Date(System.currentTimeMillis());//获取当前时间
        String   str   =   formatter.format(curDate);
        return str;
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
