package com.yidong.util;
import java.util.Calendar;

public class Time {
    public static String getTime(){
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        if(month>=8)year++;
        return String.valueOf(year);
    }
}
