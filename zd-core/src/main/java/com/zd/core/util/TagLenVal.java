package com.zd.core.util;
/**
 * @author  zenglj 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
public class TagLenVal {
	String fieldName;
	int tag=0;
	int valInt=0;
	String valStr="";
	int len=0;
	/**1：表示数字 存valInt 
	 * 2：表示时间存valStr中 
	 * 3：表示字符串存valStr中 
	 * 4：卡种类存valStr中  
	 * 5：扣费费率以|分割存valStr中
	 * 6:表示ip地址存valStr中 
	 * 7：表示mac地址存valStr中 
	 * 8:表示定时开关时间valStr中一路开关包括：1字节通道+1字节设备类型+4组时间点（4*2字节）和开关标记（4*1字节）规定格式为:
	 * 	1#3#23:11|23:11|23:11|23:11#0101
	 * 9 四路开关 规定格式为:
	 * 	1#3#23:11|23:11|23:11|23:11#0101&1#3#23:11|23:11|23:11|23:11#0101&1#3#23:11|23:11|23:11|23:11#0101&1#3#23:11|23:11|23:11|23:11#0101
	 * 
	 * **/
	int type=0;
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public int getTag() {
		return tag;
	}
	public void setTag(int tag) {
		this.tag = tag;
	}
	public int getLen() {
		return len;
	}
	public void setLen(int len) {
		this.len = len;
	}
	public int getValInt() {
		return valInt;
	}
	public void setValInt(int valInt) {
		this.valInt = valInt;
	}
	public String getValStr() {
		return valStr;
	}
	public void setValStr(String valStr) {
		this.valStr = valStr;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}

	
}
