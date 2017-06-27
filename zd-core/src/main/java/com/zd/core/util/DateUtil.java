package com.zd.core.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 
 * @ClassName: DateUtil
 * @Description: 日期处理工具类
 * @author: luoyibo
 * @date: 2016年3月7日 下午10:03:49
 *
 */
public class DateUtil {
    public static String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    public static String DEFAULT_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 格式化日期类型
     * 
     * @param d
     * @return
     */
    public static String formatDate(Date d) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
        return simpleDateFormat.format(d);
    }
    
    public static String formatDate(Date d,String formatType) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatType);
        return simpleDateFormat.format(d);
    }

    /**
     * 格式化日期时间
     * 
     * @param d
     * @return
     */
    public static String formatDateTime(Date d) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DEFAULT_TIME_FORMAT);
        return simpleDateFormat.format(d);
    }

    /**
     * 得到日期对象
     * 
     * @param str
     * @return
     */
    public static Date getDate(String str) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
        try {
            return simpleDateFormat.parse(str);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            return null;
        }
    }

    /**
     * 得到日期时间对象
     * 
     * @param str
     * @return
     */
    public static Date getTime(String str) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DEFAULT_TIME_FORMAT);
        try {
            return simpleDateFormat.parse(str);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            return null;
        }
    }

    /**
     * 根据格式化信息得到日期对象
     * 
     * @param str
     * @param parse
     * @return
     */
    public static Date getDate(String str, String parse) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(parse);
        try {
            return simpleDateFormat.parse(str);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            return null;
        }
    }
    
    /**
     * 根据Date得到该Date所属的星期日期
     * @param date
     * @return
     * @author huangzc
     */
    public static int mathWeekDay(Date date){
    	if(date == null)
    		return -1;
	    Calendar cl = Calendar.getInstance();//获得一个日历
	    cl.setTime(date);//设置当前时间
	    int number = cl.get(Calendar.DAY_OF_WEEK);//星期表示1-7，是从星期日开始，   
	    int [] weekNum = {-1,7,1,2,3,4,5,6};
	    return weekNum[number];
    }
}
