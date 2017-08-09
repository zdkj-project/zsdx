package com.zd.core.util;

import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

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
