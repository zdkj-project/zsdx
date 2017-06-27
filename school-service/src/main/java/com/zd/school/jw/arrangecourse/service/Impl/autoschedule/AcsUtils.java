package com.zd.school.jw.arrangecourse.service.Impl.autoschedule;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class AcsUtils {
	public	static int amnumScore=4;
	public static int weeknumScore=0;
	/**课程固定**/
	public final static int fixedCourseScore=1000;
	/**主课**/
	public final static int mainCourseScore=10;
	public final static int subCourseScore=5;
	public final static int am=10;
	public static  int repetScheduleNumThrowExcetption=5;
	public static Map<String,ClassWrap[]> tempSaveScheduleResult=new  HashMap<String,ClassWrap[]>();
	public static double div(String num1, String num2, int scale) {
	  BigDecimal bd1 = new BigDecimal(num1);
	  BigDecimal bd2 = new BigDecimal(num2);
	  return bd1.divide(bd2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();//除法操作
	}

	public static double add(String num1, String num2) {
		BigDecimal bd1 = new BigDecimal(num1);// 构造一个大数
		BigDecimal bd2 = new BigDecimal(num2);
		return bd1.add(bd2).doubleValue();// 调用add静态方法把bd2加到bd1上，并返回doubleValue()类型数据，也可以是intValue()
	}

	public static double sub(String num1, String num2) {
		BigDecimal bd1 = new BigDecimal(num1);
		BigDecimal bd2 = new BigDecimal(num2);
		return bd1.subtract(bd2).doubleValue();// 减法操作
	}

	public static double mul(String num1, String num2) {
		BigDecimal bd1 = new BigDecimal(num1);
		BigDecimal bd2 = new BigDecimal(num2);
		return bd1.multiply(bd2).doubleValue();// 乘法操作
	}
}
