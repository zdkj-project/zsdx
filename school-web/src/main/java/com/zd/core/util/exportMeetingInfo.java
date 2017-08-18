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
				//String[] headArray = (String[]) exportInfo.get("head");
				Integer columnWidth= (Integer) exportInfo.get("columnWidth");
				Integer headColumnCount = (Integer) exportInfo.get("headColumnCount");
				//Integer[] headAlignmentArray = (Integer[]) exportInfo.get("headAlignment");
				//Integer[] columnAlignmentArray = (Integer[]) exportInfo.get("columnAlignment");
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
					for (int j = 0; j < 8; j++) {
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
					sheet.addMergedRegion(new CellRangeAddress(0, 2, 0, 7));
					
					
					// 第1行
					sheet.getRow(3).getCell(0).setCellValue("班级名称");
					sheet.getRow(3).getCell(1).setCellValue(className);
					sheet.addMergedRegion(new CellRangeAddress(3, 3, 1, 7));

					// 第2行
					sheet.getRow(4).getCell(0).setCellValue("班级类型");
					sheet.getRow(4).getCell(1).setCellValue(classCategory);
					sheet.addMergedRegion(new CellRangeAddress(4, 4, 1, 3));

					sheet.getRow(4).getCell(4).setCellValue("人数");
					sheet.getRow(4).getCell(5).setCellValue(traineeNum);
					sheet.addMergedRegion(new CellRangeAddress(4, 4, 5, 7));

					// 第3行
					sheet.getRow(5).getCell(0).setCellValue("开始时间");
                    sheet.getRow(5).getCell(1).setCellValue(beginDate);
                    sheet.addMergedRegion(new CellRangeAddress(5, 5, 1,3));

                    sheet.getRow(5).getCell(4).setCellValue("结束时间");
                    sheet.getRow(5).getCell(5).setCellValue(endDate);
                    sheet.addMergedRegion(new CellRangeAddress(5, 5, 5,7));

                    //第4行
                    sheet.getRow(6).getCell(0).setCellValue("是否需要考勤");
                    sheet.getRow(6).getCell(1).setCellValue(needChecking);
                    sheet.addMergedRegion(new CellRangeAddress(6, 6, 1,7));

					
					sheet.getRow(7).getCell(0).setCellValue("人员考勤详细");
                    sheet.addMergedRegion(new CellRangeAddress(7, 7, 0,7));
					
					// 课程数据
					List<Map<String,Object>> trainClassscheduleList =(List<Map<String, Object>>) dataList.get("checkCourse");
					int trainClassscheduleCount = trainClassscheduleList.size();
					for (int k = 0; k< 3; k++) {
						row = sheet.createRow(8+k);
						row.setHeight((short) 0x200);
						for (int j = 0; j < trainClassscheduleCount*3+2; j++) {
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
								sheet.getRow(8).getCell(2+j*3).setCellValue(String.valueOf(val));
			                    sheet.addMergedRegion(new CellRangeAddress(8, 8, 2+j*3,4+j*3));
							}
							if(s.equals("BEGIN_TIME")){
								sheet.getRow(9).getCell(2+j*3).setCellValue("开始时间");
			                    sheet.getRow(9).getCell(3+j*3).setCellValue(String.valueOf(val).substring(0, String.valueOf(val).lastIndexOf(".")));
			                    sheet.addMergedRegion(new CellRangeAddress(9, 9, 3+j*3,4+j*3));
							}
							if(s.equals("END_TIME")){
								sheet.getRow(10).getCell(2+j*3).setCellValue("结束时间");
			                    sheet.getRow(10).getCell(3+j*3).setCellValue(String.valueOf(val).substring(0, String.valueOf(val).lastIndexOf(".")));
			                    sheet.addMergedRegion(new CellRangeAddress(10, 10, 3+j*3,4+j*3));
							}
						}
					}
					row = sheet.createRow(11);
					row.setHeight((short) 0x200);
					 for (int j = 0; j < (((trainClassscheduleCount*3+2)<8)?8:trainClassscheduleCount*3+2); j++) {
	                        cell = row.createCell(j);
	                        cell.setCellStyle(headStyle);
	                        if(j==0)
	                        	cell.setCellValue("序号");
	                        else if(j==1)
	                        	cell.setCellValue("姓名");
	                        else if(j%3==2){
	                        	cell.setCellValue("签到日期");
	                        }else if(j%3==0){
	                        	cell.setCellValue("签退日期");
	                        }else{
	                        	cell.setCellValue("考情结果");
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
						 for (int j = 0; j < trainClassscheduleCount*3+2; j++) {
								cell = row.createCell(j);
								cell.setCellStyle(headStyle);
								cell.setCellValue(val.get(j));
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
    
}
