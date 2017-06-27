/**
 * Project Name:zd-core
 * File Name:PinyinUtil.java
 * Package Name:com.zd.core.util
 * Date:2016年9月9日上午10:38:59
 * Copyright (c) 2016 ZDKJ All Rights Reserved.
 *
*/

package com.zd.core.util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * ClassName:PinyinUtil 
 * Function: TODO ADD FUNCTION. 
 * Reason:	 TODO ADD REASON. 
 * Date:     2016年9月9日 上午10:38:59 
 * @author   luoyibo
 * @version  
 * @since    JDK 1.8
 * @see 	 
 */
public class PinyinUtil {
//    static HanyuPinyinOutputFormat format = null;  
    public static enum Type {  
        UPPERCASE,              //全部大写  
        LOWERCASE,              //全部小写  
        FIRSTUPPER              //首字母大写  
    }  
//  
//    public  PinyinUtil(){  
//        format = new HanyuPinyinOutputFormat();  
//        format.setCaseType(HanyuPinyinCaseType.UPPERCASE);  
//        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);  
//    }  
//  
    public static String toPinYin(String str) throws BadHanyuPinyinOutputFormatCombination{  
        return toPinYin(str, "", Type.LOWERCASE);  
    }  
  
    public static String toPinYin(String str,String spera) throws BadHanyuPinyinOutputFormatCombination{  
        return toPinYin(str, spera, Type.LOWERCASE);  
    }
    
    public static String toPinYin(String str, String spera, Type type) throws BadHanyuPinyinOutputFormatCombination {  
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat(); 
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);  
        if(str == null || str.trim().length()==0)  
            return "";  
        if(type == Type.UPPERCASE)  
            format.setCaseType(HanyuPinyinCaseType.UPPERCASE);  
        else  
            format.setCaseType(HanyuPinyinCaseType.LOWERCASE);  
  
        String py = "";  
        String temp = "";  
        String[] t;  
        for(int i=0;i<str.length();i++){  
            char c = str.charAt(i);  
            if((int)c <= 128)  
                py += c;  
            else{  
                t = PinyinHelper.toHanyuPinyinStringArray(c, format);  
                if(t == null)  
                    py += c;  
                else{  
                    temp = t[0];  
                    if(type == Type.FIRSTUPPER)  
                        temp = t[0].toUpperCase().charAt(0)+temp.substring(1);  
                    py += temp+(i==str.length()-1?"":spera);  
                }  
            }  
        }  
        return py.trim();  
    }      
}

