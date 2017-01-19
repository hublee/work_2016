package com.zeustel.top9.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Context;

import com.zeustel.top9.R;

@SuppressLint("SimpleDateFormat")
public class TimeUtil {
	    
	    public static String getTimeDisplay(Context context,long getTime) {
	        final long currTime = System.currentTimeMillis();
	        final Date formatSysDate = new Date(currTime);
	        // 判断当前总天数
	        final int sysMonth = formatSysDate.getMonth() + 1;
	        final int sysYear = formatSysDate.getYear();

	        // 计算服务器返回时间与当前时间差值
	        final long seconds = (currTime - getTime) / 1000;
	        final long minute = seconds / 60;
	        final long hours = minute / 60;
	        final long day = hours / 24;
	        final long month = day / calculationDaysOfMonth(sysYear, sysMonth);
	        final long year = month / 12;
	        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                    "yyyy-MM-dd");
	        if (year > 0 || month > 0) {
	            return simpleDateFormat.format(getTime);
	        }else if(day > 0){
	        	if (1 == day) {
	        		 return context.getString(R.string.str_yesterday);
				}else if(2 == day){
					 return context.getString(R.string.str_before_yesterday);
				}else{
					return context.getString(R.string.str_dayago,day);
				}
	        }else if (hours > 0) {
	            return context.getString(R.string.str_hoursago, hours);
	        } else if (minute > 0) {
	            return context.getString(R.string.str_minsago, minute);
	        } else if (seconds > 0) {
	            return context.getString(R.string.str_just);
	        } else {
	           return simpleDateFormat.format(getTime);
	        }
	    }
	    /**
	     * 计算月数
	     * 
	     * @return
	     */
	    private static int calculationDaysOfMonth(int year, int month) {
	        int day = 0;
	        switch (month) {
	        // 31天
	        case 1:
	        case 3:
	        case 5:
	        case 7:
	        case 8:
	        case 10:
	        case 12:
	            day = 31;
	            break;
	        // 30天
	        case 4:
	        case 6:
	        case 9:
	        case 11:
	            day = 30;
	            break;
	        // 计算2月天数
	        case 2:
	            day = year % 100 == 0 ? year % 400 == 0 ? 29 : 28
	                    : year % 4 == 0 ? 29 : 28;
	            break;
	        }

	        return day;
	    }
}
