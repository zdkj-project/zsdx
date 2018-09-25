package com.zd.core.util;

import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import com.alibaba.druid.pool.vendor.SybaseExceptionSorter;
import com.zd.school.jw.train.model.TrainClassschedule;
import com.zd.school.jw.train.model.TrainClasstrainee;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.Array;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 导出会议信息
 */
public class exportMeetingInfo {

    public final static boolean exportMeetingInfoExcel(HttpServletResponse response, String fileName, String sheetTitle,
                                                       List<Map<String, Object>> listContent) throws IOException {
        HSSFWorkbook workbook = new HSSFWorkbook();
        boolean result = false;
        OutputStream fileOutputStream = null;
        response.reset();// 清空输出流
        response.setHeader("Content-disposition",
                "attachment; filename=" + new String((fileName + ".xls").getBytes("GB2312"), "ISO8859-1"));
        response.setContentType("application/msexcel");
        if (null != listContent && !listContent.isEmpty()) {
            try {
                Map<String, Object> exportInfo = listContent.get(0);
                String[] headArray = (String[]) exportInfo.get("head");
                Integer[] columnWidthArray = (Integer[]) exportInfo.get("columnWidth");
                Integer[] headAlignmentArray = (Integer[]) exportInfo.get("headAlignment");
                Integer[] columnAlignmentArray = (Integer[]) exportInfo.get("columnAlignment");
                int columnCount = headArray.length;

                List<Map<String, Object>> mapMeetingList = (List<Map<String, Object>>) exportInfo.get("data");
                int exportMeetingCount = mapMeetingList.size();
                CellStyle headStyle = getCellStyle(workbook,"", (short) 12,false,HorizontalAlignment.CENTER,VerticalAlignment.CENTER);
                CellStyle titleStyle = getCellStyle(workbook, "", (short) 20, true, HorizontalAlignment.CENTER,VerticalAlignment.CENTER);
                for (int i = 0; i < exportMeetingCount; i++) {
                    //获取会议的信息
                    Map<String, Object> dataList = mapMeetingList.get(i);
                    String meetingTitle = (String) dataList.get("meetingTitle");
                    String meetingCategory = (String) dataList.get("meetingCategory");
                    String roomName = (String) dataList.get("roomName");
                    String beginTime = (String) dataList.get("beginTime");
                    String endTime = (String) dataList.get("endTime");
                    String needChecking = (String) dataList.get("needChecking");

                    //创建sheet 一个会议一个sheet
                    String sheetName = meetingTitle;
                    Row row = null;
                    Cell cell = null;
                    Sheet sheet = workbook.createSheet(sheetName);
                    for (int j = 0; j < 5; j++) {
                        row = sheet.createRow(j);
                        for (int k = 0; k < columnCount; k++) {
                            cell = row.createCell(k);
                            cell.setCellStyle(headStyle);
                        }
                    }
                    //第1行
                    sheet.getRow(0).getCell(0).setCellValue("会议主题");
                    sheet.getRow(0).getCell(1).setCellValue(meetingTitle);
                    sheet.getRow(0).getCell(1).setCellStyle(titleStyle);
                    sheet.getRow(0).setHeight((short) 0x200);
                    sheet.addMergedRegion(new CellRangeAddress(0, 0, 1,8));
                    
                    //第2行
                    sheet.getRow(1).getCell(0).setCellValue("会议类型");
                    sheet.getRow(1).getCell(1).setCellValue(meetingCategory);
                    sheet.addMergedRegion(new CellRangeAddress(1, 1, 1,4));

                    sheet.getRow(1).getCell(5).setCellValue("会议地点");
                    sheet.getRow(1).getCell(6).setCellValue(roomName);
                    sheet.addMergedRegion(new CellRangeAddress(1, 1, 6,8));

                    //第3行
                    sheet.getRow(2).getCell(0).setCellValue("开始时间");
                    sheet.getRow(2).getCell(1).setCellValue(beginTime);
                    sheet.addMergedRegion(new CellRangeAddress(2, 2, 1,4));

                    sheet.getRow(2).getCell(5).setCellValue("结束时间");
                    sheet.getRow(2).getCell(6).setCellValue(endTime);
                    sheet.addMergedRegion(new CellRangeAddress(2, 2, 6,8));

                    sheet.getRow(3).getCell(0).setCellValue("是否需要考勤");
                    sheet.getRow(3).getCell(1).setCellValue(needChecking);
                    sheet.addMergedRegion(new CellRangeAddress(3, 3, 1,8));

                    sheet.getRow(4).getCell(0).setCellValue("参会人员");
                    sheet.addMergedRegion(new CellRangeAddress(4, 4, 0,8));
                    sheet.getRow(4).setHeight((short) 0x200);
                    
                    //参会人员标题
                    row = sheet.createRow(5);
                    for (int j = 0; j < columnCount; j++) {
                        cell = row.createCell(j);
                        cell.setCellStyle(headStyle);
                        cell.setCellValue(headArray[j]);
                        sheet.setColumnWidth(j, columnWidthArray[j] * 256);
                    }
                    //参会人员数据
                    List<Map<String, Object>> mapMeetingUserList = (List<Map<String, Object>>) dataList.get("meetingUser");
                    int meetingUserCount = mapMeetingUserList.size();
                    for (int j = 0; j < meetingUserCount; j++) {
                        int k = 1;  //列
                        row = sheet.createRow(6+j);
                        cell = row.createCell(0);
                        cell.setCellStyle(headStyle);
                        cell.setCellValue(String.valueOf(j+1));
                        Map<String,Object> mapUser = mapMeetingUserList.get(j);
                        for (String s : mapUser.keySet()) {
                            cell = row.createCell(k);
                            cell.setCellStyle(headStyle);
                            Object val = mapUser.get(s);
                            if (val == null)
                                val = "";
                            cell.setCellValue(String.valueOf(val));
                            k++;
                        }
                    }
                }
                fileOutputStream = response.getOutputStream();
                workbook.write(fileOutputStream);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return false;
            }
            result = true;
        }
        return result;
    }

	public final static boolean exportCourseEvalResultExcel(HttpServletResponse response, String fileName,String sheetTitle, List<Map<String, Object>> listContent) throws IOException {
		HSSFWorkbook workbook = new HSSFWorkbook();
		boolean result = false;
		OutputStream fileOutputStream = null;
		response.reset();// 清空输出流
		response.setHeader("Content-disposition","attachment; filename=" + new String((fileName + ".xls").getBytes("GB2312"), "ISO8859-1"));
		response.setContentType("application/msexcel");
		if (null != listContent && !listContent.isEmpty()) {
			try {
				Map<String, Object> exportInfo = listContent.get(0);
				Integer[] columnWidth= (Integer[]) exportInfo.get("columnWidth");
				Integer headColumnCount = (Integer) exportInfo.get("headColumnCount");
				int columnCount = headColumnCount;

				CellStyle headStyle = getCellStyle(workbook, "", (short) 12, false, HorizontalAlignment.CENTER,VerticalAlignment.CENTER);
				CellStyle titleStyle = getCellStyle(workbook, "", (short) 26, true, HorizontalAlignment.CENTER,VerticalAlignment.CENTER);
					// 课程基本信息
					String className = (String) exportInfo.get("className");
					String courseName = (String) exportInfo.get("courseName");
					String teachTypeName=(String) exportInfo.get("teachTypeName");
					String teacherName = (String) exportInfo.get("teacherName");
					String advise = (String) exportInfo.get("advise");
					String verySatisfaction = (String) exportInfo.get("verySatisfaction");
					String satisfaction = (String) exportInfo.get("satisfaction");
					
					//创建表格
					Row row = null;
					Cell cell = null;
					Sheet sheet = workbook.createSheet(courseName);
					//创建表头
					for (int j = 0; j < 7; j++) {
						row = sheet.createRow(j);
						row.setHeight((short) 0x200);
						for (int k = 0; k < columnCount; k++) {
							cell = row.createCell(k);
							cell.setCellStyle(headStyle);
							sheet.setColumnWidth(k, columnWidth[k] * 256);
						}
					}
					// 标题
					sheet.getRow(0).getCell(0).setCellStyle(titleStyle);
					sheet.getRow(0).getCell(0).setCellValue(className+"班教学评估表");
					sheet.addMergedRegion(new CellRangeAddress(0, 2, 0, 7));
					
					// 第1行
					sheet.getRow(3).getCell(0).setCellValue("课程名称");
					sheet.getRow(3).getCell(1).setCellValue(courseName);
					sheet.addMergedRegion(new CellRangeAddress(3, 3, 1, 7));

					// 第2行
					sheet.getRow(4).getCell(0).setCellValue("教学形式");
					sheet.getRow(4).getCell(1).setCellValue(teachTypeName);
					sheet.addMergedRegion(new CellRangeAddress(4, 4, 1, 7));

					// 第3行
					sheet.getRow(5).getCell(0).setCellValue("授课教师");
                    sheet.getRow(5).getCell(1).setCellValue(teacherName);
                    sheet.addMergedRegion(new CellRangeAddress(5, 5, 1,7));
                    
                    // 第4行
					sheet.getRow(6).getCell(0).setCellValue("评估指标");
					sheet.getRow(6).getCell(1).setCellValue("评估标准");
					sheet.getRow(6).getCell(2).setCellValue("很满意");
					sheet.getRow(6).getCell(3).setCellValue("满意");
					sheet.getRow(6).getCell(4).setCellValue("基本满意");
					sheet.getRow(6).getCell(5).setCellValue("不满意");
					sheet.getRow(6).getCell(6).setCellValue("很满意度");
					sheet.getRow(6).getCell(7).setCellValue("满意度");
					
					//处理评估指标部分数据
					List<List<Map<String, String>>> standList = new ArrayList<>();
					standList=(List<List<Map<String, String>>>)exportInfo.get("standList");
					 int standListCount = standList.size();
					 for(int k=0;k<standListCount;k++){
						 List<Map<String, String>> detail = standList.get(k);
						 Map<String, String> details =detail.get(0);
						 row = sheet.createRow(7+k);
						 row.setHeight((short) 0x500);
						 for (int j = 0; j < 8; j++) {
								cell = row.createCell(j);
								cell.setCellStyle(headStyle);
								if(j==0){
									cell.setCellValue(String.valueOf(details.get("INDICATOR_NAME")));
								}
								if(j==1){
									cell.setCellValue(String.valueOf(details.get("INDICATOR_STAND")));
								}
								if(j==2){
									cell.setCellValue(String.valueOf(details.get("VERY_SATISFACTIONCOUNT")));
								}
								if(j==3){
									cell.setCellValue(String.valueOf(details.get("SATISFACTIONCOUNT")));
								}
								if(j==4){
									cell.setCellValue(String.valueOf(details.get("BAS_SATISFACTIONCOUNT")));
								}
								if(j==5){
									cell.setCellValue(String.valueOf(details.get("NO_SATISFACTIONCOUNT")));
								}
								if(j==6){
									cell.setCellValue(String.valueOf(details.get("VERY_SATISFACTION")));
								}
								if(j==7){
									cell.setCellValue(String.valueOf(details.get("SATISFACTION")));
								}
							}
					 }
					
					 //创建汇总和评价栏
					 for (int j = 0; j < 2; j++) {
							row = sheet.createRow(7+standListCount+j);
							row.setHeight((short) 0x500);
							for (int k = 0; k < columnCount; k++) {
								cell = row.createCell(k);
								cell.setCellStyle(headStyle);
								sheet.setColumnWidth(k, columnWidth[k] * 256);
							}
						}
					 sheet.getRow(7+standListCount).getCell(0).setCellValue("汇总：");
	                 sheet.addMergedRegion(new CellRangeAddress(7+standListCount, 7+standListCount, 0,5));
					 sheet.getRow(7+standListCount).getCell(6).setCellValue(verySatisfaction);
					 sheet.getRow(7+standListCount).getCell(7).setCellValue(satisfaction);
					 
					 sheet.getRow(8+standListCount).getCell(0).setCellValue("意见建议");
					 sheet.getRow(8+standListCount).getCell(1).setCellValue(advise);
	                 sheet.addMergedRegion(new CellRangeAddress(8+standListCount, 8+standListCount, 1,7));
				
	                 fileOutputStream = response.getOutputStream();
	                 workbook.write(fileOutputStream);
			} catch (Exception e) {
				System.out.println(e.getMessage());
				return false;
			}
			result = true;
		}
		return result;
	}
    
	public final static boolean exportCheckResultInfoExcel(HttpServletResponse response, String fileName,String sheetTitle, List<Map<String, Object>> listContent) throws IOException {
		HSSFWorkbook workbook = new HSSFWorkbook();
		boolean result = false;
		OutputStream fileOutputStream = null;
		response.reset();// 清空输出流
		response.setHeader("Content-disposition","attachment; filename=" + new String((fileName + ".xls").getBytes("GB2312"), "ISO8859-1"));
		response.setContentType("application/msexcel");
		if (null != listContent && !listContent.isEmpty()) {
			try {
				Map<String, Object> exportInfo = listContent.get(0);
				Integer columnWidth= (Integer) exportInfo.get("columnWidth");
				Integer headColumnCount = (Integer) exportInfo.get("headColumnCount");
				int columnCount = headColumnCount;

				List<Map<String, Object>> mapMeetingList = (List<Map<String, Object>>) exportInfo.get("data");
				int exportMeetingCount = mapMeetingList.size();
				CellStyle headStyle = getCellStyle(workbook, "", (short) 12, false, HorizontalAlignment.CENTER,VerticalAlignment.CENTER);
				CellStyle titleStyle = getCellStyle(workbook, "", (short) 26, true, HorizontalAlignment.CENTER,VerticalAlignment.CENTER);
				for (int i = 0; i < exportMeetingCount; i++) {
					// 获取会议的信息
					Map<String, Object> dataList = mapMeetingList.get(i);
					String className = (String) dataList.get("className");
					String classCategory = (String) dataList.get("classCategory");
					String traineeNum=(String) dataList.get("traineeNum");
					String beginDate = (String) dataList.get("beginDate");
					String endDate = (String) dataList.get("endDate");
					String needChecking = (String) dataList.get("needChecking");
					// 创建sheet 一个会议一个sheet
					String sheetName = className+"班考勤";
					Row row = null;
					Cell cell = null;
					Sheet sheet = workbook.createSheet(sheetName);
					for (int j = 0; j < 10; j++) {
						row = sheet.createRow(j);
						row.setHeight((short) 0x200);
						for (int k = 0; k < columnCount; k++) {
							cell = row.createCell(k);
							cell.setCellStyle(headStyle);
						}
					}
					// 标题
					sheet.getRow(0).getCell(0).setCellStyle(titleStyle);
					sheet.getRow(0).getCell(0).setCellValue(className+"班考勤详细");
					sheet.addMergedRegion(new CellRangeAddress(0, 2, 0, 9));
					
					// 第1行
					sheet.getRow(3).getCell(0).setCellValue("班级名称");
					sheet.getRow(3).getCell(1).setCellValue(className);
					sheet.addMergedRegion(new CellRangeAddress(3, 3, 1, 9));

					// 第2行
					sheet.getRow(4).getCell(0).setCellValue("班级类型");
					sheet.getRow(4).getCell(1).setCellValue(classCategory);
					sheet.addMergedRegion(new CellRangeAddress(4, 4, 1, 4));

					sheet.getRow(4).getCell(5).setCellValue("人数");
					sheet.getRow(4).getCell(6).setCellValue(traineeNum);
					sheet.addMergedRegion(new CellRangeAddress(4, 4, 6, 9));

					// 第3行
					sheet.getRow(5).getCell(0).setCellValue("开始时间");
                    sheet.getRow(5).getCell(1).setCellValue(beginDate);
                    sheet.addMergedRegion(new CellRangeAddress(5, 5, 1,4));

                    sheet.getRow(5).getCell(5).setCellValue("结束时间");
                    sheet.getRow(5).getCell(6).setCellValue(endDate);
                    sheet.addMergedRegion(new CellRangeAddress(5, 5, 6,9));

                    //第4行
                    sheet.getRow(6).getCell(0).setCellValue("是否需要考勤");
                    sheet.getRow(6).getCell(1).setCellValue(needChecking);
                    sheet.addMergedRegion(new CellRangeAddress(6, 6, 1,9));

					
					sheet.getRow(7).getCell(0).setCellValue("人员考勤详细");
                    sheet.addMergedRegion(new CellRangeAddress(7, 7, 0,9));
					
					// 课程数据
					List<Map<String,Object>> trainClassscheduleList =(List<Map<String, Object>>) dataList.get("checkCourse");
					int trainClassscheduleCount = trainClassscheduleList.size();
					for (int k = 0; k< 3; k++) {
						row = sheet.createRow(8+k);
						row.setHeight((short) 0x250);
						for (int j = 0; j < trainClassscheduleCount*4+2; j++) {
							cell = row.createCell(j);
							cell.setCellStyle(headStyle);
						}
					}
					//创建课程表头
					for (int j = 0; j < trainClassscheduleCount; j++) {
						 Map<String,Object> trainClassschedule = trainClassscheduleList.get(j);
						for (String s : trainClassschedule.keySet()) {
							Object val = trainClassschedule.get(s);
							if(s.equals("COURSE_NAME")){
								sheet.getRow(8).getCell(2+j*4).setCellValue(String.valueOf(val));
			                    sheet.addMergedRegion(new CellRangeAddress(8, 8, 2+j*4,5+j*4));
							}
							if(s.equals("BEGIN_TIME")){
								sheet.getRow(9).getCell(2+j*4).setCellValue("开始时间");
			                    sheet.getRow(9).getCell(3+j*4).setCellValue(String.valueOf(val).substring(0, String.valueOf(val).lastIndexOf(".")));
			                    sheet.addMergedRegion(new CellRangeAddress(9, 9, 3+j*4,5+j*4));
							}
							if(s.equals("END_TIME")){
								sheet.getRow(10).getCell(2+j*4).setCellValue("结束时间");
			                    sheet.getRow(10).getCell(3+j*4).setCellValue(String.valueOf(val).substring(0, String.valueOf(val).lastIndexOf(".")));
			                    sheet.addMergedRegion(new CellRangeAddress(10, 10, 3+j*4,5+j*4));
							}
						}
					}
					row = sheet.createRow(11);
					row.setHeight((short) 0x200);
					for (int j = 0; j < (((trainClassscheduleCount*4+2)<10)?10:trainClassscheduleCount*4+2); j++) {
	                        cell = row.createCell(j);
	                        cell.setCellStyle(headStyle);
	                        if(j==0)
	                        	cell.setCellValue("序号");
	                        else if(j==1)
	                        	cell.setCellValue("姓名");
	                        else if(j%4==2){
	                        	cell.setCellValue("签到日期");
	                        }else if(j%4==3){
	                        	cell.setCellValue("签退日期");
	                        }else if(j%4==0){
	                        	cell.setCellValue("上课时长");
	                        }else{
	                        	cell.setCellValue("考勤结果");
	                        }
	                        sheet.setColumnWidth(j, columnWidth * 256);
	                    }
					 
					// 学员考勤数据
					 Map<String,List<String>> traineeResult =(Map<String,List<String>>) dataList.get("voTrainClassCheck");
					 List<String> keylist = new LinkedList<String>();
					 for (String s : traineeResult.keySet()) {
						 keylist.add(s);
					 }
					 for(int k=0;k<keylist.size();k++){
						 List<String> val = traineeResult.get(keylist.get(k));
						 row = sheet.createRow(12+k);
						 row.setHeight((short) 0x200);
						 for (int j = 0; j < trainClassscheduleCount*4+2; j++) {
								cell = row.createCell(j);
								cell.setCellStyle(headStyle);
								if(j==0){
									cell.setCellValue(val.get(j));
								}else if(j%4==0){
									cell.setCellValue(val.get(j)+" 分钟");
								}else if(val.get(j).equals("null")){
									cell.setCellValue(" ");
								}else if(val.get(j).equals("1")){
									cell.setCellValue("正常");
								}else if(val.get(j).equals("2")){
									cell.setCellValue("迟到");
								}else if(val.get(j).equals("3")){
									cell.setCellValue("早退");
								}else if(val.get(j).equals("4")){
									cell.setCellValue("缺勤");
								}else if(val.get(j).equals("5")){
									cell.setCellValue("迟到早退");
								}else{
									cell.setCellValue(val.get(j));
								}
							}
					 }
				}
				fileOutputStream = response.getOutputStream();
				workbook.write(fileOutputStream);
			} catch (Exception e) {
				System.out.println(e.getMessage());
				return false;
			}
			result = true;
		}
		return result;
	}
    
	/**
	 * 班级课程考勤信息
	 * @param response
	 * @param fileName
	 * @param sheetTitle
	 * @param listContent
	 * @return
	 * @throws IOException
	 */
	public final static boolean exportCouseCheckResultInfoExcel(HttpServletResponse response, String fileName,String sheetTitle, List<Map<String, Object>> listContent) throws IOException {
		HSSFWorkbook workbook = new HSSFWorkbook();
		boolean result = false;
		OutputStream fileOutputStream = null;
		response.reset();// 清空输出流
		response.setHeader("Content-disposition","attachment; filename=" + new String((fileName + ".xls").getBytes("GB2312"), "ISO8859-1"));
		response.setContentType("application/msexcel");
		if (null != listContent && !listContent.isEmpty()) {
			try {
				Map<String, Object> exportInfo = listContent.get(0);
				Integer columnWidth= (Integer) exportInfo.get("columnWidth");
				Integer headColumnCount = (Integer) exportInfo.get("headColumnCount");
				int columnCount = headColumnCount;

				List<Map<String, Object>> mapMeetingList = (List<Map<String, Object>>) exportInfo.get("data");
				int exportMeetingCount = mapMeetingList.size();
				CellStyle headStyle = getCellStyle(workbook, "", (short) 12, false, HorizontalAlignment.CENTER,VerticalAlignment.CENTER);
				CellStyle titleStyle = getCellStyle(workbook, "", (short) 26, true, HorizontalAlignment.CENTER,VerticalAlignment.CENTER);
				for (int i = 0; i < exportMeetingCount; i++) {
					// 获取班级的信息
					Map<String, Object> dataList = mapMeetingList.get(i);
					String className = (String) dataList.get("className");
					String classCategory = (String) dataList.get("classCategory");
					String traineeNum=(String) dataList.get("traineeNum");
					String beginDate = (String) dataList.get("beginDate");
					String endDate = (String) dataList.get("endDate");
					String needChecking = (String) dataList.get("needChecking");
					// 创建sheet 一个会议一个sheet
					String sheetName = className+"班课程考勤";
					Row row = null;
					Cell cell = null;
					Sheet sheet = workbook.createSheet(sheetName);
					for (int j = 0; j < 9; j++) {
						row = sheet.createRow(j);
						row.setHeight((short) 0x200);
						for (int k = 0; k < columnCount; k++) {
							cell = row.createCell(k);
							cell.setCellStyle(headStyle);
						}
					}
					// 标题
					sheet.getRow(0).getCell(0).setCellStyle(titleStyle);
					sheet.getRow(0).getCell(0).setCellValue(className+"-班级课程考勤详细");
					sheet.addMergedRegion(new CellRangeAddress(0, 2, 0, 8));
					
					// 第1行
					sheet.getRow(3).getCell(0).setCellValue("班级名称");
					sheet.getRow(3).getCell(1).setCellValue(className);
					sheet.addMergedRegion(new CellRangeAddress(3, 3, 1, 8));

					// 第2行
					sheet.getRow(4).getCell(0).setCellValue("班级类型");
					sheet.getRow(4).getCell(1).setCellValue(classCategory);
					sheet.addMergedRegion(new CellRangeAddress(4, 4, 1, 3));

					sheet.getRow(4).getCell(4).setCellValue("总人数");
					sheet.getRow(4).getCell(5).setCellValue(traineeNum);
					sheet.addMergedRegion(new CellRangeAddress(4, 4, 5, 8));

					// 第3行
					sheet.getRow(5).getCell(0).setCellValue("开始时间");
                    sheet.getRow(5).getCell(1).setCellValue(beginDate);
                    sheet.addMergedRegion(new CellRangeAddress(5, 5, 1,3));

                    sheet.getRow(5).getCell(4).setCellValue("结束时间");
                    sheet.getRow(5).getCell(5).setCellValue(endDate);
                    sheet.addMergedRegion(new CellRangeAddress(5, 5, 5,8));

                    //第4行
                    sheet.getRow(6).getCell(0).setCellValue("是否需要考勤");
                    sheet.getRow(6).getCell(1).setCellValue(needChecking);
                    sheet.addMergedRegion(new CellRangeAddress(6, 6, 1,8));

					
					sheet.getRow(7).getCell(0).setCellValue("人员考勤详细");
                    sheet.addMergedRegion(new CellRangeAddress(7, 7, 0,8));
					
					// 课程数据
					List<Map<String,Object>> trainClassscheduleList =(List<Map<String, Object>>) dataList.get("checkCourse");
					int trainClassscheduleCount = trainClassscheduleList.size();
					for (int k = 0; k< 3; k++) {
						row = sheet.createRow(8+k);
						row.setHeight((short) 0x250);
						for (int j = 0; j < trainClassscheduleCount*6+3; j++) {
							cell = row.createCell(j);
							cell.setCellStyle(headStyle);
						}
					}
					//创建课程表头
					for (int j = 0; j < trainClassscheduleCount; j++) {
						 Map<String,Object> trainClassschedule = trainClassscheduleList.get(j);
						for (String s : trainClassschedule.keySet()) {
							Object val = trainClassschedule.get(s);
							if(s.equals("COURSE_NAME")){
								sheet.getRow(8).getCell(3+j*6).setCellValue(String.valueOf(val));
			                    sheet.addMergedRegion(new CellRangeAddress(8, 8, 3+j*6,8+j*6));
							}
							if(s.equals("BEGIN_TIME")){
								sheet.getRow(9).getCell(3+j*6).setCellValue("开始时间");
			                    sheet.getRow(9).getCell(4+j*6).setCellValue(String.valueOf(val).substring(0, String.valueOf(val).lastIndexOf(".")));
			                    sheet.addMergedRegion(new CellRangeAddress(9, 9, 4+j*6,5+j*6));
							}
							if(s.equals("END_TIME")){
								sheet.getRow(9).getCell(6+j*6).setCellValue("结束时间");
			                    sheet.getRow(9).getCell(7+j*6).setCellValue(String.valueOf(val).substring(0, String.valueOf(val).lastIndexOf(".")));
			                    sheet.addMergedRegion(new CellRangeAddress(9, 9, 7+j*6,8+j*6));
							}							
							if(s.equals("lateNum")){
								sheet.getRow(10).getCell(3+j*6).setCellValue("迟到人数");
								sheet.getRow(10).getCell(4+j*6).setCellValue(String.valueOf(val));	
							}
							if(s.equals("absentNum")){
								sheet.getRow(10).getCell(5+j*6).setCellValue("缺勤/未考勤人数");
								sheet.getRow(10).getCell(6+j*6).setCellValue(String.valueOf(val));	
							}
							if(s.equals("percent")){
								sheet.getRow(10).getCell(7+j*6).setCellValue("到课率");
								sheet.getRow(10).getCell(8+j*6).setCellValue(String.valueOf(val));	
							}
						}
					}
					row = sheet.createRow(11);
					row.setHeight((short) 0x200);
					for (int j = 0; j < (((trainClassscheduleCount*6+3)<9)?9:trainClassscheduleCount*6+3); j++) {
							sheet.setColumnWidth(j, columnWidth * 256);
							cell = row.createCell(j);
	                        cell.setCellStyle(headStyle);
	                        if(j==0)
	                        	cell.setCellValue("序号");
	                        else if(j==1)
	                        	cell.setCellValue("姓名");
	                        else if(j==2){
	                        	cell.setCellValue("学号");
	                        	sheet.setColumnWidth(j, 17 * 256);
	                        }else if(j%6==3){
	                        	cell.setCellValue("签到时间");
	                        }else if(j%6==4){
	                        	cell.setCellValue("签退时间");
	                        }else if(j%6==5){
	                        	cell.setCellValue("上课时长");
	                        }else if(j%6==0){
	                        	cell.setCellValue("考勤结果");
 	                        }else if((j-1)%6==0){			//0 1 2 3 4 5 6 7 8    9 10 11 12 13 14
	                        	cell.setCellValue("是否请假");
	                        }else{
	                        	cell.setCellValue("备注信息");
	                        }
	                        
	                    }
					 
					// 学员考勤数据
					 Map<String,List<String>> traineeResult =(Map<String,List<String>>) dataList.get("voTrainClassCheck");
					 List<String> keylist = new LinkedList<String>();
					 for (String s : traineeResult.keySet()) {
						 keylist.add(s);
					 }
					 for(int k=0;k<keylist.size();k++){
						 List<String> val = traineeResult.get(keylist.get(k));
						 row = sheet.createRow(12+k);
						 row.setHeight((short) 0x200);
						 for (int j = 0; j < trainClassscheduleCount*6+3; j++) {
								cell = row.createCell(j);
								cell.setCellStyle(headStyle);
								if(j==0){
									cell.setCellValue(val.get(j));
								}else if((j+1)%6==0){	//5+1  11+1 15+1
									cell.setCellValue(val.get(j)+" 分钟");
								}else if(j%6==0){
									if(val.get(j).equals("1")){
										cell.setCellValue("正常");
									}else if(val.get(j).equals("2")){
										cell.setCellValue("迟到");
									}else if(val.get(j).equals("3")){
										cell.setCellValue("早退");
									}else if(val.get(j).equals("4")){
										cell.setCellValue("缺勤");
									}else if(val.get(j).equals("5")){
										cell.setCellValue("迟到早退");
									}else{
										cell.setCellValue("未考勤");
									}
								}else if(val.get(j).equals("null")){
									cell.setCellValue(" ");
								}else{
									cell.setCellValue(val.get(j));
								}
							}
					 }
				}
				fileOutputStream = response.getOutputStream();
				workbook.write(fileOutputStream);
			} catch (Exception e) {
				System.out.println(e.getMessage());
				return false;
			}
			result = true;
		}
		return result;
	}
	
    private static CellStyle getCellStyle(HSSFWorkbook workbook, String fontName, Short fontSize, Boolean isBold, HorizontalAlignment horizontalAlignment, VerticalAlignment verticalAlignment) {
        HSSFFont font = workbook.createFont();
        if (StringUtils.isEmpty(fontName))
            font.setFontName("方正黑体简体");
        else
            font.setFontName(fontName);
        font.setFontHeightInPoints(fontSize);// 字体大小
        font.setBold(isBold);  //是否加粗
        CellStyle style = workbook.createCellStyle();
        style.setFont(font);
        style.setAlignment(horizontalAlignment);// 左右对齐
        style.setVerticalAlignment(verticalAlignment);// 上下对齐
        style.setBorderBottom(BorderStyle.THIN);  //底边框
        style.setBorderLeft(BorderStyle.THIN); //左边框
        style.setBorderTop(BorderStyle.THIN); //上边框
        style.setBorderRight(BorderStyle.THIN); //右边框
        style.setWrapText(true); //是否自动换行

        return style;
    }
    
    /*
     * 学分汇总的格式 
     */
     public final static boolean exportCourseCreditExcel(HttpServletResponse response, String fileName,String sheetTitle, List<Map<String, Object>> listContent) throws IOException {
 		HSSFWorkbook workbook = new HSSFWorkbook();
 		boolean result = false;
 		OutputStream fileOutputStream = null;
 		response.reset();// 清空输出流
 		response.setHeader("Content-disposition","attachment; filename=" + new String((fileName + ".xls").getBytes("GB2312"), "ISO8859-1"));
 		response.setContentType("application/msexcel");
 		if (null != listContent && !listContent.isEmpty()) {
 			try {
 				Map<String, Object> exportInfo = listContent.get(0);
 				//获取表头详细
 				List<Map<String,Object>> classTraineeinfo =(List<Map<String,Object>>)exportInfo.get("headinfo");
 				//获取学分详细数据
 				List<Map<String,Object>> traineeList =(List<Map<String,Object>>)exportInfo.get("data");
 				int traineeListCount = traineeList.size();
 				//获取列宽
 				Integer[] columnWidth= (Integer[]) exportInfo.get("columnWidth");
 				//获取字段
 				String[] head =(String[])exportInfo.get("head");
 				int columnCount = columnWidth.length;

 				CellStyle headStyle = getCellStyle(workbook, "", (short) 12, false, HorizontalAlignment.CENTER,VerticalAlignment.CENTER);
 				CellStyle titleStyle = getCellStyle(workbook, "", (short) 26, true, HorizontalAlignment.CENTER,VerticalAlignment.CENTER);
 					// 课程基本信息
 				String name = String.valueOf(classTraineeinfo.get(0).get("XM"));
 				String xbm = String.valueOf(classTraineeinfo.get(0).get("XBM"));
 				String mobile_phone = String.valueOf(classTraineeinfo.get(0).get("MOBILE_PHONE"));
 				String position = String.valueOf(classTraineeinfo.get(0).get("POSITION"));
 				String work_unit = String.valueOf(classTraineeinfo.get(0).get("WORK_UNIT"));
 					
 					// 创建sheet 一课程一个sheet
 				String sheetName =sheetTitle;
 					
 					//创建表格
 					Row row = null;
 					Cell cell = null;
 					Sheet sheet = workbook.createSheet(sheetName);
 					//创建表头
 					for (int j = 0; j < 7; j++) {
 						row = sheet.createRow(j);
 						row.setHeight((short) 0x200);
 						for (int k = 0; k < columnCount; k++) {
 							cell = row.createCell(k);
 							cell.setCellStyle(headStyle);
 							sheet.setColumnWidth(k, columnWidth[k] * 256);
 						}
 					}
 					// 标题
 					sheet.getRow(0).getCell(0).setCellStyle(titleStyle);
 					sheet.getRow(0).getCell(0).setCellValue(sheetName);
 					sheet.addMergedRegion(new CellRangeAddress(0, 2, 0, 6));
 					
 					// 第1行
 					sheet.getRow(3).getCell(0).setCellValue("性别");
 					sheet.getRow(3).getCell(1).setCellValue((xbm.equals("1"))?"男":"女");
 					sheet.addMergedRegion(new CellRangeAddress(3, 3, 1, 6));

 					// 第2行
 					sheet.getRow(4).getCell(0).setCellValue("移动电话");
 					sheet.getRow(4).getCell(1).setCellValue(mobile_phone);
 					sheet.addMergedRegion(new CellRangeAddress(4, 4, 1, 6));

 					// 第3行
 					sheet.getRow(5).getCell(0).setCellValue("职务");
                    sheet.getRow(5).getCell(1).setCellValue(position);
                    sheet.addMergedRegion(new CellRangeAddress(5, 5, 1,6));
                     
                  // 第4行
  					sheet.getRow(6).getCell(0).setCellValue("所在岗位");
                    sheet.getRow(6).getCell(1).setCellValue(work_unit);
                    sheet.addMergedRegion(new CellRangeAddress(6, 6, 1,6));
                    
                    //处理字段行
                    row = sheet.createRow(7);
					row.setHeight((short) 0x200);
					 for (int j = 0; j <columnCount ; j++) {
	                        cell = row.createCell(j);
	                        cell.setCellStyle(headStyle);
	                        cell.setCellValue(head[j]);
	                    } 
					 
					 //填入数据
					 Map<String, Object> traineeMap = null;
                    for(int j = 0; j <traineeListCount ; j++){
                    	row = sheet.createRow(8+j);
                    	row.setHeight((short) 0x250);
                    	traineeMap=traineeList.get(j);
                    	List<String> keylist = new LinkedList<String>();
                    	for (String s : traineeMap.keySet()) {
   						 	keylist.add(s);
   					 	}
                    	for(int k = 0; k <columnCount ; k++){
                    		cell = row.createCell(k);
	                        cell.setCellStyle(headStyle);
	                        cell.setCellValue(String.valueOf(traineeMap.get(keylist.get(k))));
                    	}
                    }
 					
 					//处理评估指标部分数据
 	                 fileOutputStream = response.getOutputStream();
 	                 workbook.write(fileOutputStream);
 			} catch (Exception e) {
 				System.out.println(e.getMessage());
 				return false;
 			}
 			result = true;
 		}
 		return result;
 	}
     
     /*
      * 学分汇总的格式 
      */
      public final static boolean exportTraineeCreditExcel(HttpServletResponse response, String fileName,String sheetTitle, List<Map<String, Object>> listContent) throws IOException {
  		HSSFWorkbook workbook = new HSSFWorkbook();
  		boolean result = false;
  		OutputStream fileOutputStream = null;
  		response.reset();// 清空输出流
  		response.setHeader("Content-disposition","attachment; filename=" + new String((fileName + ".xls").getBytes("GB2312"), "ISO8859-1"));
  		response.setContentType("application/msexcel");
  		if (null != listContent && !listContent.isEmpty()) {
  			try {
  				//设置格式
  				CellStyle headStyle = getCellStyle(workbook, "", (short) 12, false, HorizontalAlignment.CENTER,VerticalAlignment.CENTER);
				CellStyle titleStyle = getCellStyle(workbook, "", (short) 26, true, HorizontalAlignment.CENTER,VerticalAlignment.CENTER);
  				
  				//获取数据
  				Map<String, Object> exportInfo = listContent.get(0);
  				
  				//获取第一页的数据
  				List<Map<String,Object>> classTraineetList =(List<Map<String,Object>>)exportInfo.get("headinfo");
  				int classTraineetListCount = classTraineetList.size();
  				
  				//获取学分详细列宽
				Integer[] headColumnWidth= (Integer[]) exportInfo.get("headColumnWidth");
				int headColumnCount = headColumnWidth.length;
  				
  				//获取学分详细列宽
				Integer[] columnWidth= (Integer[]) exportInfo.get("columnWidth");
				int columnCount = columnWidth.length;
				
				//创建sheet
  				String sheetName =sheetTitle;
				Row row = null;
				Cell cell = null;
				Sheet sheet = workbook.createSheet(sheetName);
				for (int j = 0; j <4; j++) {
					row = sheet.createRow(j);
					row.setHeight((short) 0x200);
					for (int k = 0; k < headColumnCount; k++) {
						cell = row.createCell(k);
						cell.setCellStyle(headStyle);
						sheet.setColumnWidth(k, headColumnWidth[k] * 256);
					}
				}
				// 标题
				sheet.getRow(0).getCell(0).setCellStyle(titleStyle);
				sheet.getRow(0).getCell(0).setCellValue(sheetName);
				sheet.addMergedRegion(new CellRangeAddress(0, 2, 0, 8));
				
				// 第1行
				sheet.getRow(3).getCell(0).setCellValue("序号");
				sheet.getRow(3).getCell(1).setCellValue("姓名");
				sheet.getRow(3).getCell(2).setCellValue("性别");
				sheet.getRow(3).getCell(3).setCellValue("学号");
				sheet.getRow(3).getCell(4).setCellValue("学分");
				sheet.getRow(3).getCell(5).setCellValue("移动电话");
				sheet.getRow(3).getCell(6).setCellValue("职务");
				sheet.getRow(3).getCell(7).setCellValue("职级");
				sheet.getRow(3).getCell(8).setCellValue("所在单位");
				
				//导出第一页数据
				Map<String, Object> traineeMap = null;
				for (int j = 0; j <classTraineetListCount; j++) {
					row = sheet.createRow(4+j);
					row.setHeight((short) 0x200);
					traineeMap=classTraineetList.get(j);
                 	List<String> keylist = new LinkedList<String>();
                 	for (String s : traineeMap.keySet()) {
						 	keylist.add(s);
					 	}
					for (int k = 0; k < headColumnCount; k++) {
						cell = row.createCell(k);
						cell.setCellStyle(headStyle);
						cell.setCellValue(String.valueOf(traineeMap.get(keylist.get(k))));
						sheet.setColumnWidth(k, headColumnWidth[k] * 256);
					}
				}
				
  				for(int i=0;i<classTraineetListCount;i++) {
  				
  				//获取表头详细
  				Map<String,Object> classTraineeinfo =classTraineetList.get(i);
  				// 课程基本信息
  				String name = String.valueOf(classTraineeinfo.get("xm"));
  				String xbm = String.valueOf(classTraineeinfo.get("xb"));
  				String mobile_phone = String.valueOf(classTraineeinfo.get("phone"));
  				String position = String.valueOf(classTraineeinfo.get("position"));
  				String work_unit = String.valueOf(classTraineeinfo.get("workUnit"));
  				String classTraineeId = String.valueOf(classTraineeinfo.get("classTraineeId"));	
  				
  				//获取学分详细数据
  				Map<String,List<Map<String, String>>> traineeCreditMaps =(Map<String,List<Map<String, String>>>)exportInfo.get("data");
  				List<Map<String, String>> traineeCreditList = traineeCreditMaps.get(classTraineeId);
  				int traineeCreditListCount =traineeCreditList.size();
  				
  				//获取字段
  				String[] head =(String[])exportInfo.get("head");
  					// 创建sheet 一课程一个sheet
  					 sheetName =name+"学分详细信息";
  					//创建表格
  					 row = null;
  					 cell = null;
  					 sheet = workbook.createSheet(sheetName);
  					//创建表头
  					for (int j = 0; j < 7; j++) {
  						row = sheet.createRow(j);
  						row.setHeight((short) 0x200);
  						for (int k = 0; k < columnCount; k++) {
  							cell = row.createCell(k);
  							cell.setCellStyle(headStyle);
  							sheet.setColumnWidth(k, columnWidth[k] * 256);
  						}
  					}
  					// 标题
  					sheet.getRow(0).getCell(0).setCellStyle(titleStyle);
  					sheet.getRow(0).getCell(0).setCellValue(sheetName);
  					sheet.addMergedRegion(new CellRangeAddress(0, 2, 0, 6));
  					
  					// 第1行
  					sheet.getRow(3).getCell(0).setCellValue("性别");
  					sheet.getRow(3).getCell(1).setCellValue(xbm);
  					sheet.addMergedRegion(new CellRangeAddress(3, 3, 1, 6));

  					// 第2行
  					sheet.getRow(4).getCell(0).setCellValue("移动电话");
  					sheet.getRow(4).getCell(1).setCellValue(mobile_phone);
  					sheet.addMergedRegion(new CellRangeAddress(4, 4, 1, 6));

  					// 第3行
  					sheet.getRow(5).getCell(0).setCellValue("职务");
                     sheet.getRow(5).getCell(1).setCellValue(position);
                     sheet.addMergedRegion(new CellRangeAddress(5, 5, 1,6));
                      
                   // 第4行
   					sheet.getRow(6).getCell(0).setCellValue("所在岗位");
                     sheet.getRow(6).getCell(1).setCellValue(work_unit);
                     sheet.addMergedRegion(new CellRangeAddress(6, 6, 1,6));
                     
                     //处理字段行
                     row = sheet.createRow(7);
 					row.setHeight((short) 0x200);
 					 for (int j = 0; j <columnCount ; j++) {
 	                        cell = row.createCell(j);
 	                        cell.setCellStyle(headStyle);
 	                        cell.setCellValue(head[j]);
 	                    } 
 					 
 					 //填入数据
 					 Map<String, String> traineeCreditMap = null;
                     for(int j = 0; j <traineeCreditListCount ; j++){
                     	row = sheet.createRow(8+j);
                     	row.setHeight((short) 0x250);
                     	traineeCreditMap=traineeCreditList.get(j);
                     	List<String> keylist = new LinkedList<String>();
                     	for (String s : traineeCreditMap.keySet()) {
    						 	keylist.add(s);
    					 	}
                     	for(int k = 0; k <columnCount ; k++){
                     		cell = row.createCell(k);
 	                        cell.setCellStyle(headStyle);
 	                        cell.setCellValue(traineeCreditMap.get(keylist.get(k)));
                     	}
                     }
  			}
  					//处理评估指标部分数据
  	                 fileOutputStream = response.getOutputStream();
  	                 workbook.write(fileOutputStream);
  			} catch (Exception e) {
  				System.out.println(e.getMessage());
  				return false;
  			}
  			result = true;
  		}
  		return result;
  	} 
      
      /*
       * 就餐汇总的格式 
       */
       public final static boolean exportClassTotalsExcel(HttpServletResponse response, String fileName,String sheetTitle, List<Map<String, Object>> listContent) throws IOException {
   		HSSFWorkbook workbook = new HSSFWorkbook();
   		boolean result = false;
   		OutputStream fileOutputStream = null;
   		response.reset();// 清空输出流
   		response.setHeader("Content-disposition","attachment; filename=" + new String((fileName + ".xls").getBytes("GB2312"), "ISO8859-1"));
   		response.setContentType("application/msexcel");
   		if (null != listContent && !listContent.isEmpty()) {
   			try {
   				//设置格式
   				CellStyle headsStyle = getCellNoBoderStyle(workbook, "", (short) 12, false, HorizontalAlignment.CENTER,VerticalAlignment.CENTER);
   				CellStyle NBLeftStyle = getCellNBLeftStyle(workbook, "", (short) 12, false, HorizontalAlignment.CENTER,VerticalAlignment.CENTER);
   				CellStyle headStyle = getCellStyle(workbook, "", (short) 12, false, HorizontalAlignment.CENTER,VerticalAlignment.CENTER);
 				CellStyle titleStyle = getCellStyle(workbook, "", (short) 26, true, HorizontalAlignment.CENTER,VerticalAlignment.CENTER);
   				
   				//获取数据
   				Map<String, Object> exportInfo = listContent.get(0);
   				List<List<Map<String, String>>> exportLists = (List<List<Map<String, String>>>)exportInfo.get("data");
   				int exportListsCount = exportLists.size();
   				String dinnerType = (String)exportInfo.get("dinnerType");
   				List<String> djbhs = (List<String>)exportInfo.get("djbhs");
   				
   				//获取总合计
   				List<String> totalTexts = (List<String>)exportInfo.get("totalTexts");
   				List<BigDecimal> totals = (List<BigDecimal>)exportInfo.get("totals");
   				
   				//获取列宽
   				Integer[] columnWidth=(Integer[])exportInfo.get("columnWidth");
   				int columnWidthCount = columnWidth.length;
   				
   				//获取字段 
   				String[] head = (String[])exportInfo.get("head");
   				for(int s=0;s<exportListsCount;s++) {
   					List<Map<String, String>>  exportList=new ArrayList<>();
   					exportList=exportLists.get(s);
   					String sheetName =sheetTitle+(s+1);
   	 				Row row = null;
   	 				Cell cell = null;
   	 				Sheet sheet = workbook.createSheet(sheetName);
   	 				for (int j = 0; j <5; j++) {
   						row = sheet.createRow(j);
   						row.setHeight((short) 0x200);
   						for (int k = 0; k < columnWidthCount; k++) {
   							cell = row.createCell(k);
   							cell.setCellStyle(headsStyle);
   							sheet.setColumnWidth(k, columnWidth[k] * 256);
   						}
   					}
   	 				// 标题
   	 				sheet.getRow(0).getCell(0).setCellStyle(titleStyle);
   	 				sheet.getRow(0).getCell(0).setCellValue("中共中山市委党校餐饮部校内结算账单");
   	 				sheet.addMergedRegion(new CellRangeAddress(0, 2, 0, 12));
   	 				
   	 				// 第4行
   	 				sheet.getRow(3).getCell(0).setCellValue("用餐类型：");
   	 				sheet.addMergedRegion(new CellRangeAddress(3, 3, 0, 1));
   	 				sheet.getRow(3).getCell(2).setCellStyle(NBLeftStyle);
   	 				sheet.getRow(3).getCell(2).setCellValue(dinnerType);
   	 				sheet.addMergedRegion(new CellRangeAddress(3, 3, 2, 12));
   	 				
   	 				// 第5行
   	 				sheet.getRow(4).getCell(0).setCellValue("培训班：");
   	 				sheet.addMergedRegion(new CellRangeAddress(4, 4, 0, 1));
   	 				sheet.getRow(4).getCell(2).setCellStyle(NBLeftStyle);
   	 				sheet.getRow(4).getCell(2).setCellValue(sheetTitle);
   	 				sheet.addMergedRegion(new CellRangeAddress(4, 4, 2, 8));
   	 				
   	 				sheet.getRow(4).getCell(9).setCellValue("单据编号：");
   	 				sheet.addMergedRegion(new CellRangeAddress(4, 4, 9, 10));
   	 				sheet.getRow(4).getCell(11).setCellStyle(NBLeftStyle);
   	 				sheet.getRow(4).getCell(11).setCellValue(djbhs.get(s));
   	 				sheet.addMergedRegion(new CellRangeAddress(4, 4, 11, 12));
   	 				
   	 				//创建表头
   	 				for (int j = 0; j <2; j++) {
   						row = sheet.createRow(5+j);
   						row.setHeight((short) 0x200);
   						for (int k = 0; k < columnWidthCount; k++) {
   							cell = row.createCell(k);
   							cell.setCellStyle(headStyle);
   	   						sheet.setColumnWidth(k, columnWidth[k] * 256);
   							if(j==1) {
   								cell.setCellValue(head[k]);
   							}
   						}
   					}
   	 				//表格表头
   	 				sheet.getRow(5).getCell(0).setCellValue("日期");
   	 				sheet.addMergedRegion(new CellRangeAddress(5, 5, 0, 1));
   	 				
   	 				sheet.getRow(5).getCell(2).setCellValue("早餐");
   	 				sheet.addMergedRegion(new CellRangeAddress(5, 5, 2, 4));
   	 				
   	 				sheet.getRow(5).getCell(5).setCellValue("午餐");
   	 				sheet.addMergedRegion(new CellRangeAddress(5, 5, 5, 7));
   	 				
   	 				sheet.getRow(5).getCell(8).setCellValue("晚餐");
   	 				sheet.addMergedRegion(new CellRangeAddress(5, 5, 8, 10));
   	 				
   	 				sheet.getRow(5).getCell(11).setCellValue("其他");
   	 				sheet.addMergedRegion(new CellRangeAddress(5, 6, 11, 11));
   	 				
   	 				sheet.getRow(5).getCell(12).setCellValue("金额合计");
   	 				sheet.addMergedRegion(new CellRangeAddress(5, 6, 12, 12));
   	 				
   	 				//处理数据
   	 				 Map<String, String> traineeDinnerMap = null;
   	                 for(int j = 0; j <6 ; j++){
   	                 	row = sheet.createRow(7+j);
   	                 	row.setHeight((short) 0x380);
   	                 	if(j<exportList.size()) {
   	                 		traineeDinnerMap=exportList.get(j);
   	                 		List<String> keylist = new LinkedList<String>();
   	                 		for (String key : traineeDinnerMap.keySet()) {
   							 	keylist.add(key);
   	                 		}
   	                 		for(int k = 0; k <columnWidthCount ; k++){
   	                 			cell = row.createCell(k);
   		                        cell.setCellStyle(headStyle);
   		                        if(k<columnWidthCount){
   		                         cell.setCellValue(traineeDinnerMap.get(keylist.get(k)));
   		                        }
   	                 		}
   	                 	}else {
   	                 	for(int k = 0; k <columnWidthCount ; k++){
	                 			cell = row.createCell(k);
		                        cell.setCellStyle(headStyle);
	                 		}
   	                 	}
   	                 }
   	                 	//创建合计行
   	                 	row = sheet.createRow(7+6);
   						row.setHeight((short) 0x200);
   						for (int k = 0; k < columnWidthCount; k++) {
   							cell = row.createCell(k);
   							cell.setCellStyle(headStyle);
   						}
   						sheet.getRow(7+6).getCell(0).setCellValue("总合计：大写："+totalTexts.get(s)+"   ￥ "+totals.get(s));
   		 				sheet.addMergedRegion(new CellRangeAddress(7+6, 7+6, 0, 12));
   	                 
   		 				//创建签字行
   		 				row = sheet.createRow(8+6);
   						row.setHeight((short) 0x200);
   						for (int k = 0; k < columnWidthCount; k++) {
   							cell = row.createCell(k);
   							cell.setCellStyle(headStyle);
   						}
   						sheet.getRow(8+6).getCell(0).setCellValue("用餐申请人：                          餐饮部经办人：                                                               ");
   		 				sheet.addMergedRegion(new CellRangeAddress(8+6, 8+6, 0, 12));
   		 				
   				}
   					//处理评估指标部分数据
   	                 fileOutputStream = response.getOutputStream();
   	                 workbook.write(fileOutputStream);
   			} catch (Exception e) {
   				System.out.println(e.getMessage());
   				return false;
   			}
   			result = true;
   		}
   		return result;
   	}   
    
       //无边框
       private static CellStyle getCellNoBoderStyle(HSSFWorkbook workbook, String fontName, Short fontSize, Boolean isBold, HorizontalAlignment horizontalAlignment, VerticalAlignment verticalAlignment) {
           HSSFFont font = workbook.createFont();
           if (StringUtils.isEmpty(fontName))
               font.setFontName("方正黑体简体");
           else
               font.setFontName(fontName);
           font.setFontHeightInPoints(fontSize);// 字体大小
           font.setBold(isBold);  //是否加粗
           CellStyle style = workbook.createCellStyle();
           style.setFont(font);
           style.setAlignment(horizontalAlignment);// 左右对齐
           style.setVerticalAlignment(verticalAlignment);// 上下对齐
           style.setWrapText(true); //是否自动换行
           return style;
       }
       //无边框左对齐
       private static CellStyle getCellNBLeftStyle(HSSFWorkbook workbook, String fontName, Short fontSize, Boolean isBold, HorizontalAlignment horizontalAlignment, VerticalAlignment verticalAlignment) {
           HSSFFont font = workbook.createFont();
           if (StringUtils.isEmpty(fontName))
               font.setFontName("方正黑体简体");
           else
               font.setFontName(fontName);
           font.setFontHeightInPoints(fontSize);// 字体大小
           font.setBold(isBold);  //是否加粗
           CellStyle style = workbook.createCellStyle();
           style.setFont(font);
           style.setAlignment(horizontalAlignment.LEFT);// 左右对齐
           style.setVerticalAlignment(verticalAlignment);// 上下对齐
           style.setWrapText(true); //是否自动换行
           return style;
       }
       
}
