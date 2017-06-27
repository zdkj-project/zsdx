package com.zd.core.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

/**
 * 用于课程表解析
 * @author huangzc
 *
 */
public class readTxt {
	
	public static Map<String,Map<String,List<String>>> importCourseTxt(MultipartFile file){
		List<String> strList = new ArrayList<String>(); 
		BufferedReader br = null;
		InputStream fis = null;
		InputStreamReader isr = null;
		try {
			fis = file.getInputStream();
			isr = new InputStreamReader(fis,"GBK");
			br = new BufferedReader(isr);
			String rl = "";
			while((rl = br.readLine())!=null){
				String temp = rl.replaceAll("(?i)[^a-zA-Z0-9\u4E00-\u9FA5]", "");
				if(temp!=null && !temp.trim().equals("") && temp.length() > 5)
					strList.add(temp);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				if(fis!=null)
					fis.close();
				if(isr != null)
					isr.close();
				if(br!=null)
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		if(strList == null || strList.size() <= 3)
			return null;
//		String titleStr = strList.get(0);//txt标题
		String weekStr[] = strList.get(1).replaceAll("(.{3})", "$1,").split(",");//星期数以‘星期一’为一个开始
		String courseStr[] = strList.get(2).replaceAll("一", ",一").split(",");//课程数以1为一个开始。
		StringBuffer sbCourse = new StringBuffer();
		for (int i = 0; i < courseStr.length; i++) {
			if(courseStr[i] != null && !courseStr[i].trim().equals("")){
				if(i == courseStr.length-1)
					sbCourse.append(courseStr[i]);
				else
					sbCourse.append(courseStr[i]).append(",");
			}
		}
		courseStr = sbCourse.toString().split(",");
		Map<String,Map<String,List<String>>> classMap = new HashMap<String,Map<String,List<String>>>();
		for(int i=3;i<strList.size();i++){
			String className = strList.get(i).substring(0, 2);
			int tempInt = 3;
			if(StringUtils.isInteger(strList.get(i).substring(2, 4)))
				tempInt = 4;
			className = className + "（" + strList.get(i).substring(2, tempInt) + "）班";
			String courseOfWeek[] = strList.get(i).substring(tempInt, strList.get(i).length()).replaceAll("(.{2})",  "$1,").split(",");
			int k = 0;
			Map<String,List<String>> weekCourse = new HashMap<String,List<String>>();
			for(int j=0;j<courseStr.length;j++){
				List<String> courseFoDayList = new ArrayList<String>(); 
				for (int j2 = 0; j2 < courseStr[j].length(); j2++) {
					if(k < courseOfWeek.length)
						courseFoDayList.add(courseOfWeek[k]);
					++k;
				}
				weekCourse.put(weekStr[j], courseFoDayList);
			}
			classMap.put(className, weekCourse);
		}
		return classMap;
	}
}
