package com.zd.core.util;

import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PoiExportExcel {

	/**
	 * 导出EXCEL文件（单个sheet，可以多个表格）
	 * @param response	
	 * @param fileName	文件名称
	 * @param sheetTitle  sheet中的大标题（第一行）
	 * @param listContent	sheet中的数据集（list中每个map数据中，存放一个表格的数据；在每个map中又细分为多个不同的Object数据）
	 *  如：（详见导出班级代码）
	 *  List<Map<String, Object>> listContent = new ArrayList<>();	//数据集
	 *  
	 *  Map<String, Object> roomAllMap = new LinkedHashMap<>();		//一个map中，代表一个表格
		roomAllMap.put("data", roomList);				//此表格中的具体遍历数据
		roomAllMap.put("title", "班级学员宿舍信息表");		//表格标题，若为null，且数据集中存在其他map，则下一个map不会空出3行（空出3行，用于划分各个表格）。
		roomAllMap.put("head", new String[] { "姓名", "性别", "是否午休", "是否晚宿" }); 	//若存在名字相同的，则名字相同且相邻的head合并（当head名字相同，并且某行中的对应的列值也相同，则合并它们）
		roomAllMap.put("columnWidth",  new Integer[] { 15, 15, 15, 20 }); // 20代表20个字节，10个字符（整个sheet中，只能存在一个columnWidth,因为列宽是针对整个sheet的）
		roomAllMap.put("columnAlignment", new Integer[] { 0, 0, 0, 0 }); // 0代表居中，1代表居左，2代表居右
		roomAllMap.put("mergeCondition", null); // 跨行合行列需要的条件，条件优先级按顺序决定，NULL表示不合并,空数组表示无条件
	 	
	 	listContent.add(roomAllMap);	//加入此map到数据集中
	 * 
	 * @return
	 * @throws IOException
	 */
	public final static boolean exportExcel(HttpServletResponse response, String fileName, String sheetTitle,
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
				Sheet sheet = workbook.createSheet(fileName);

				// 大标题栏样式
				HSSFFont titleFont = workbook.createFont();
				titleFont.setFontName("方正黑体简体");
				titleFont.setFontHeightInPoints((short) 22);// 字体大小
				titleFont.setBold(true);
				CellStyle titleStyle = workbook.createCellStyle();
				titleStyle.setFont(titleFont);
				titleStyle.setAlignment(HorizontalAlignment.CENTER);// 左右居中
				titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);// 上下居中

				// 表头字体样式
				HSSFFont headFont = workbook.createFont();
				headFont.setFontName("方正黑体简体");
				headFont.setFontHeightInPoints((short) 11);// 字体大小
				headFont.setBold(true);
				// 表头Cell样式
				CellStyle headStyle = workbook.createCellStyle();
				headStyle.setAlignment(HorizontalAlignment.CENTER);
				headStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				headStyle.setFont(headFont);
				headStyle.setWrapText(true);
				headStyle.setBorderLeft(BorderStyle.THIN);
				headStyle.setBorderRight(BorderStyle.THIN);
				headStyle.setBorderTop(BorderStyle.THIN);
				headStyle.setBorderBottom(BorderStyle.THIN);

				// 内容字体样式
				HSSFFont textFont = workbook.createFont();
				textFont.setFontName("方正黑体简体");
				// textFont.setFontHeightInPoints((short) 11);// 字体大小
				// 内容Cell样式，内容居中对齐
				CellStyle textStyleCenter = workbook.createCellStyle();
				textStyleCenter.setAlignment(HorizontalAlignment.CENTER);
				textStyleCenter.setVerticalAlignment(VerticalAlignment.CENTER);
				textStyleCenter.setBorderLeft(BorderStyle.THIN);
				textStyleCenter.setBorderRight(BorderStyle.THIN);
				textStyleCenter.setBorderTop(BorderStyle.THIN);
				textStyleCenter.setBorderBottom(BorderStyle.THIN);
				textStyleCenter.setFont(textFont);
				textStyleCenter.setWrapText(true);

				// 内容Cell样式2,内容居左对齐
				CellStyle textStyleLeft = workbook.createCellStyle();
				textStyleLeft.setAlignment(HorizontalAlignment.LEFT);
				textStyleLeft.setVerticalAlignment(VerticalAlignment.CENTER);
				textStyleLeft.setBorderLeft(BorderStyle.THIN);
				textStyleLeft.setBorderRight(BorderStyle.THIN);
				textStyleLeft.setBorderTop(BorderStyle.THIN);
				textStyleLeft.setBorderBottom(BorderStyle.THIN);
				textStyleLeft.setFont(textFont);
				textStyleLeft.setWrapText(true);

				// 内容Cell样式3,内容居右对齐
				CellStyle textStyleRight = workbook.createCellStyle();
				textStyleRight.setAlignment(HorizontalAlignment.RIGHT);
				textStyleRight.setVerticalAlignment(VerticalAlignment.CENTER);
				textStyleRight.setBorderLeft(BorderStyle.THIN);
				textStyleRight.setBorderRight(BorderStyle.THIN);
				textStyleRight.setBorderTop(BorderStyle.THIN);
				textStyleRight.setBorderBottom(BorderStyle.THIN);
				textStyleRight.setFont(textFont);
				textStyleRight.setWrapText(true);

				int rowNum = 0;
				int colCount = ((String[]) listContent.get(0).get("head")).length;
				// 第一行先创建一个大标题(当不为null的时候，设置这一行)
				if (sheetTitle != null) {
					Row sheetTitleRow = sheet.createRow(rowNum);
					sheetTitleRow.setHeight((short) 0x300);
					Cell sheetTitleCell = sheetTitleRow.createCell(0);
					sheetTitleCell.setCellStyle(titleStyle);
					sheetTitleCell.setCellValue(sheetTitle);
					sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 0, colCount - 1));
					rowNum++;
				}

				for (Map<String, Object> dataList : listContent) {
					// 获取数据
					List<Map<String, String>> currentData = (List<Map<String, String>>) dataList.get("data");
					String title = (String) dataList.get("title");
					String[] headArray = (String[]) dataList.get("head");
					Integer[] columnWidthArray = (Integer[]) dataList.get("columnWidth");
					Integer[] columnWidthAlignment = (Integer[]) dataList.get("columnAlignment");
					String[] columnMergeCondition = (String[]) dataList.get("mergeCondition");

					// 设置标题栏内容(当不为null的时候，设置这一行)
					if (title != null) {
						if (rowNum > 1) // 除了第一个表格的时候
							rowNum += 3; // 空出三行

						Row titleRow = sheet.createRow(rowNum);
						titleRow.setHeight((short) 0x248);
						for (int i = 0; i < headArray.length; i++) {
							Cell titleCell = titleRow.createCell(i);
							titleCell.setCellStyle(headStyle);
							titleCell.setCellValue(title);
						}
						sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 0, headArray.length - 1));
						rowNum++;
					}

					// 设置表头内容
					Row headRow = sheet.createRow(rowNum);
					headRow.setHeight((short) 0x200);
					Object[] headMergeObj = null; // （因为2个行或列合并了之后，就必须要先移除合并，才能重新合并更多的行）
					for (int i = 0; i < headArray.length; i++) {
						Cell cell = headRow.createCell(i);
						cell.setCellValue(headArray[i]);
						cell.setCellStyle(headStyle);
						// 指定列的宽度
						sheet.setColumnWidth(i, columnWidthArray[i] * 256);

						// 如果当前列名和上一列的名字一致，则合并
						if (i > 0 && headArray[i - 1].equals(headArray[i])) {
							if (headMergeObj == null) {
								int index = sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, i - 1, i));
								headMergeObj = new Object[] { index, headArray[i], i - 1 }; //// 合并的index值，表头值，行号。。
							} else {
								if (headMergeObj[1].equals(headArray[i])) {
									sheet.removeMergedRegion((int) headMergeObj[0]);
									int index = sheet.addMergedRegion(
											new CellRangeAddress(rowNum, rowNum, (int) headMergeObj[2], i));
									headMergeObj[0] = index;
								} else {
									int index = sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, i - 1, i));
									headMergeObj = new Object[] { index, headArray[i], i - 1 }; // 合并的index值，表头值，行号。
								}
							}
						}

					}
					rowNum++;

					// 保存上一个map
					Map<String, String> preMap = null;
					// 保存某一列上一个合并的数据，并用来判断是否再合并（因为2个列合并了之后，就必须要先移除合并，才能合并）
					Map<Integer, Object[]> recordMap = new HashMap<>();
					// 保存某一行的上一个合并的数据，并用来判断是否再合并
					Object[] rowMergeObj = null;
					for (int i = 0; i < currentData.size(); i++) {
						Row textRow = sheet.createRow(rowNum);
						Map<String, String> map = currentData.get(i);

						int j = 0, maxTextHeight = (short) 0X250; // 默认行高，可以放2行数据
						String preVal = null; // 保存某一行中，上一列的值
						for (String s : map.keySet()) {

							Object val = map.get(s);
							if (val == null)
								val = "";

							Cell cell = textRow.createCell(j);
							cell.setCellValue(String.valueOf(val));

							if (columnWidthAlignment[j] == 0) {
								cell.setCellStyle(textStyleCenter);
							} else if (columnWidthAlignment[j] == 1) {
								cell.setCellStyle(textStyleLeft);
							} else if (columnWidthAlignment[j] == 2) {
								cell.setCellStyle(textStyleRight);
							} else {
								cell.setCellStyle(textStyleCenter);
							}

							// 计算最大的高度值
							int len = String.valueOf(val).getBytes().length;
							if (len > columnWidthArray[j] + 1) {
								int tempHeight = (len / (columnWidthArray[j] - 1) + 1) * 0X125; // 加入了边框，所以一行放入的字节数会少一个
								if (tempHeight > maxTextHeight)
									maxTextHeight = tempHeight;
							}

							// 判断是否需要进行列合并
							// 如果当前列名和上一列的名字一致，并且行的值也一致，则合并
							if (j > 0 && headArray[j - 1].equals(headArray[j]) && preVal != null
									&& preVal.equals(val)) {
								if (rowMergeObj == null) {
									int index = sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, j - 1, j));
									rowMergeObj = new Object[] { index, headArray[j - 1], preVal, i, j - 1 }; // 合并的index值，表头值，数据值，行号,列号。
								} else {
									// 当上一个合并的数据值、表头值、行号 与
									// 当前处理的单元格的数据一致时，才继续合并，否则单独合并两列
									if (rowMergeObj[2].equals(val) && rowMergeObj[1].equals(headArray[j])
											&& i == (int) rowMergeObj[3]) {
										sheet.removeMergedRegion((int) rowMergeObj[0]);
										int index = sheet.addMergedRegion(
												new CellRangeAddress(rowNum, rowNum, (int) rowMergeObj[4], j));
										rowMergeObj[0] = index;
									} else {
										int index = sheet
												.addMergedRegion(new CellRangeAddress(rowNum, rowNum, j - 1, j));
										rowMergeObj = new Object[] { index, headArray[j - 1], preVal, i, j - 1 }; // 合并的index值，表头值，数据值，行号,列号。
									}
								}
							}

							// 判断是否需要进行行合并
							if (i > 0 && preMap.get(s) != null && !preMap.get(s).equals("") && preMap.get(s).equals(val)
									&& columnMergeCondition != null) { // 当前后的值都一致，才能满足最基本的合并条件

								boolean isMerge = true;
								String tempStr = "";
								// 当需要合并的列，满足必要的合并条件后，才允许合并
								for (int k = 0; k < columnMergeCondition.length; k++) {
									tempStr = columnMergeCondition[k];

									// 当前判断的列为条件中的列时，可以直接合并（因为列是有顺序的，所以当判断到当前列的时候，表明前面的列条件都判断完毕）
									if (s.equals(tempStr)) {
										isMerge = true;
										break;
									} else if (!preMap.get(tempStr).equals(map.get(tempStr))) { // 当其中一个条件不满足，则不允许合并
										isMerge = false;
										break;
									}
								}

								if (isMerge == true) {
									// 若不存在值，表明还未合并
									Object[] recordObj = recordMap.get(j);
									if (recordObj == null) {
										int index = sheet
												.addMergedRegion(new CellRangeAddress(rowNum - 1, rowNum, j, j));
										recordObj = new Object[] { index, currentData.get(i), rowNum - 1 }; // 合并的index值，MAP，行号。
										recordMap.put(j, recordObj);
									}
									// 若存在值，则判断，值是否一致，一致则合并，否则重新保存新的数据
									else {
										boolean isTempMerger = false;
										Map<String, String> tempMap = (Map) recordObj[1];
										// 当需要合并的列，满足必要的合并条件后，才允许合并
										for (int k = 0; k < columnMergeCondition.length; k++) {
											tempStr = columnMergeCondition[k];

											// 当前判断的列为条件中的列时，可以直接合并（因为列是有顺序的，所以当判断到当前列的时候，表明前面的列条件都判断完毕）
											if (s.equals(tempStr) && tempMap.get(s).equals(val)) {
												isTempMerger = true;
												break;
											} else if (!tempMap.get(tempStr).equals(map.get(tempStr))) { // 当其中一个条件不满足，则不允许合并
												isTempMerger = false;
												break;
											}
										}
										if (isTempMerger == true) {
											sheet.removeMergedRegion((int) recordObj[0]); // 先移除
											int index = sheet.addMergedRegion(
													new CellRangeAddress((int) recordObj[2], rowNum, j, j)); // 再合并
											recordObj[0] = index;
											recordMap.put(j, recordObj);
										} else { // 否则，重新保存此列的合并数据
											int index = sheet
													.addMergedRegion(new CellRangeAddress(rowNum - 1, rowNum, j, j));
											recordObj = new Object[] { index, currentData.get(i), rowNum - 1 }; // 保存新的合并的index值，列值，行号。
											recordMap.put(j, recordObj);
										}

									}
								}
							}
							preVal = (String) val;
							j++;
						}
						// 设置行高
						textRow.setHeight((short) maxTextHeight);
						// 保存上一个map
						preMap = currentData.get(i);
						rowNum++;
					}

				}

				fileOutputStream = response.getOutputStream();
				workbook.write(fileOutputStream);
			} catch (IOException e) {
				System.out.println(e.getMessage());

				return false;
				// LOG.error("流异常", e);
			} /*
				 * catch (IllegalAccessException e) { // LOG.error("反射异常", e); }
				 */
			catch (Exception e) {

				System.out.println(e.getMessage());
				return false;
				// LOG.error("其他异常", e);
			} finally {
				if (null != fileOutputStream) {
					try {
						fileOutputStream.close();
					} catch (IOException e) {
						System.out.println(e.getMessage());
						// LOG.error("关闭流异常", e);
					}
				}
			}
			result = true;
		}
		return result;
	}


    public final static boolean exportAllEvalExcel(HttpServletResponse response, String fileName, String sheetTitle,
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
                // 第一行，评估表表头样式
                CellStyle titleStyle = getCellStyle(workbook, "", (short) 22, false, HorizontalAlignment.CENTER, VerticalAlignment.CENTER);

                //班级或课程基本信息标题样式
                CellStyle baseTitleStyle = getCellStyle(workbook, "", (short) 14, false, HorizontalAlignment.CENTER, VerticalAlignment.CENTER);

                //班级或课程基本信息内容样式
                CellStyle baseContentStyle = getCellStyle(workbook, "", (short) 14, false, HorizontalAlignment.LEFT, VerticalAlignment.CENTER);

                //生成排名数
                workbook = createCourseRankingSheet("排名", listContent, workbook);
                workbook = createCourseEvalSheet("排名", listContent, workbook);
                workbook = createClassEvalSheet("排名", listContent, workbook, titleStyle, baseTitleStyle, baseContentStyle);
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

    public static CellStyle getCellStyle(HSSFWorkbook workbook, String fontName, Short fontSize, Boolean isBold, HorizontalAlignment horizontalAlignment, VerticalAlignment verticalAlignment) {
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
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);

        return style;
    }

    /**
     * 生成排名的sheet
     *
     * @param fileName
     * @param listContent
     * @param workbook
     */
    private static HSSFWorkbook createCourseRankingSheet(String fileName, List<Map<String, Object>> listContent, HSSFWorkbook workbook) {
        Sheet sheet = workbook.createSheet(fileName);
        Map<String, Object> dataList = listContent.get(0);
        List<Map<String, String>> rankingData = (List<Map<String, String>>) dataList.get("data");
        String title = (String) dataList.get("title");
        String[] headArray = (String[]) dataList.get("head");
        Integer[] columnWidthArray = (Integer[]) dataList.get("columnWidth");
        Integer[] columnWidthAlignment = (Integer[]) dataList.get("columnAlignment");
        String[] columnMergeCondition = (String[]) dataList.get("mergeCondition");
        // 第一行，评估表表头样式
        CellStyle titleStyle = getCellStyle(workbook, "", (short) 22, false, HorizontalAlignment.CENTER, VerticalAlignment.CENTER);

        //班级或课程基本信息标题样式
        CellStyle baseTitleStyle = getCellStyle(workbook, "", (short) 14, false, HorizontalAlignment.CENTER, VerticalAlignment.CENTER);

        //班级或课程基本信息内容样式
        CellStyle baseContentStyle = getCellStyle(workbook, "", (short) 14, false, HorizontalAlignment.LEFT, VerticalAlignment.CENTER);
        //表头
        Row headRow = sheet.createRow(0);
        for (int i = 0; i < headArray.length; i++) {
            Cell cell = headRow.createCell(i);
            cell.setCellValue(headArray[i]);
            cell.setCellStyle(baseTitleStyle);
            sheet.setColumnWidth(i, columnWidthArray[i] * 256);
        }
        for (int i = 0; i < rankingData.size(); i++) {
            Row textRow = sheet.createRow(i + 1);
            Map<String, String> map = rankingData.get(i);

            int j = 0;//列
            for (String s : map.keySet()) {
                Object val = map.get(s);
                if (val == null)
                    val = "";

                Cell cell = textRow.createCell(j);
                cell.setCellValue(String.valueOf(val));
                cell.setCellStyle(baseContentStyle);
                j++;
            }
        }
        return workbook;
    }

    private static HSSFWorkbook createCourseEvalSheet(String fileName, List<Map<String, Object>> listContent, HSSFWorkbook workbook) {
        int couseCount = listContent.size() - 2;
        int sheetNameCount = 0;
        Map<String, String> mapSheetName = new HashMap<>();
        CellStyle titleStyle = getCellStyle(workbook, "", (short) 22, true, HorizontalAlignment.CENTER, VerticalAlignment.CENTER);
        //班级或课程基本信息标题样式
        CellStyle baseTitleStyle = getCellStyle(workbook, "", (short) 14, false, HorizontalAlignment.CENTER, VerticalAlignment.CENTER);
        //班级或课程基本信息内容样式
        CellStyle baseContentStyle = getCellStyle(workbook, "", (short) 14, false, HorizontalAlignment.LEFT, VerticalAlignment.CENTER);
        //int i=0;
        for (int i = 0; i < couseCount; i++) {
            Map<String, Object> dataList = listContent.get(i + 1);
            String classScheduleId = (String) dataList.get("classScheduleId");
            String className = (String) dataList.get("className");
            String courseName = (String) dataList.get("courseName");
            String teachTypeName = (String) dataList.get("teachTypeName");
            String teacherName = (String) dataList.get("teacherName");
            String verySatisfaction = (String) dataList.get("verySatisfaction");
            String satisfaction = (String) dataList.get("satisfaction");
            String advise = (String) dataList.get("advise");
            String[] headArray = (String[]) dataList.get("head");
            Integer[] columnWidth = (Integer[]) dataList.get("columnWidth");
            String sheetName = teacherName;
            if (teacherName.indexOf(",") > 0)
                sheetName = "教学组";

            for (String isSheet : mapSheetName.values()) {
                if (sheetName == isSheet)
                    sheetNameCount++;
            }
            if (sheetNameCount > 0) {
                sheetName += String.valueOf(sheetNameCount);
                sheetNameCount = 0;
            }
            mapSheetName.put(classScheduleId, sheetName);
            int iColumn = headArray.length;
            Sheet sheet = workbook.createSheet(sheetName);

            Row headRow = sheet.createRow(0);
            headRow.setHeight((short) 0x600);
            Cell cell = headRow.createCell(0);
            cell.setCellValue(className);
            for (int j = 0; j < iColumn; j++) {
                cell = headRow.createCell(j);
                cell.setCellValue(className + "\n教学评估表");
                sheet.setColumnWidth(j, columnWidth[j] * 256);
                cell.setCellStyle(titleStyle);
            }
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, iColumn - 1));

            for (int k = 1; k < 4; k++) {
                headRow = sheet.createRow(k);
                for (int j = 0; j < iColumn; j++) {
                    cell = headRow.createCell(j);
                    if (j == 0) {
                        cell.setCellStyle(baseTitleStyle);
                        switch (k) {
                            case 1:
                                cell.setCellValue("课程名称");
                                break;
                            case 2:
                                cell.setCellValue("教学形式");
                                break;
                            case 3:
                                cell.setCellValue("上课教师");
                                break;
                        }
                    } else {
                        cell.setCellStyle(baseContentStyle);
                        switch (k) {
                            case 1:
                                cell.setCellValue(courseName);
                                break;
                            case 2:
                                cell.setCellValue(teachTypeName);
                                break;
                            case 3:
                                cell.setCellValue(teacherName);
                                break;
                        }
                    }
                }
                sheet.addMergedRegion(new CellRangeAddress(k , k, 1, iColumn-1));
            }

            headRow = sheet.createRow(4);
            for (int j = 0; j < iColumn; j++) {
                cell = headRow.createCell(j);
                cell.setCellStyle(baseTitleStyle);
                cell.setCellValue(headArray[j]);
            }
            //评价标准
            int standCount = 1;
            int rowBegin = 5;
            int rowEnd = 1;
            Map<String, List<Map<String, Object>>> standData = (Map<String, List<Map<String, Object>>>) dataList.get("standList");
            //循环每个指标
            for (String s : standData.keySet()) {
                List<Map<String, Object>> value = standData.get(s);  //指标下的标准
                standCount = value.size();
                rowEnd = rowBegin + standCount;
                for (int j = 0; j < standCount; j++) {
                    Row textRow = sheet.createRow(j + rowBegin);
                    Map<String, Object> map = value.get(j);
                    map.remove("CLASS_SCHEDULE_ID");
                    map.remove("INDICATOR_ID");
                    int k = 0;//列
                    for (String sss : map.keySet()) {
                        Object val = map.get(sss);
                        if (val == null)
                            val = "";

                        cell = textRow.createCell(k);
                        cell.setCellStyle(baseContentStyle);
                        cell.setCellValue(String.valueOf(val));
                        k++;
                    }
                }
                rowBegin += standCount;
            }

            //满意度合计
            int IndexColumn = headArray.length;
            headRow = sheet.createRow(rowBegin);
            for (int j = 0; j < iColumn-2; j++) {
                cell = headRow.createCell(j);
                cell.setCellStyle(baseTitleStyle);
                cell.setCellValue("汇总：");
            }
            sheet.addMergedRegion(new CellRangeAddress(rowBegin, rowBegin, 0, IndexColumn - 3));

            cell = headRow.createCell(IndexColumn - 2);
            cell.setCellStyle(baseTitleStyle);
            cell.setCellValue(verySatisfaction);

            cell = headRow.createCell(IndexColumn - 1);
            cell.setCellStyle(baseTitleStyle);
            cell.setCellValue(satisfaction);
            rowBegin += 1;

            //意见与建议
            headRow = sheet.createRow(rowBegin);
            headRow.setHeight((short) 0x1200);
            for (int ii = 0; ii < headArray.length; ii++) {
                cell = headRow.createCell(ii);
                cell.setCellValue(advise.replace("|","\n"));
                cell.setCellStyle(baseContentStyle);
            }
            headRow.getCell(0).setCellValue("意见与建议");
            headRow.getCell(0).setCellStyle(baseTitleStyle);
            sheet.addMergedRegion(new CellRangeAddress(rowBegin, rowBegin, 1, IndexColumn - 1));
        }

        return workbook;
    }

    private static HSSFWorkbook createClassEvalSheet(String fileName, List<Map<String, Object>> listContent, HSSFWorkbook workbook, CellStyle titleStyle, CellStyle baseTitleStyle, CellStyle baseContentStyle) {
        Sheet sheet = workbook.createSheet("管理评价");
        Map<String, Object> dataList = listContent.get(listContent.size() - 1);
        String className = (String) dataList.get("className");
        String holdUnit = (String) dataList.get("holdUnit");
        String undertaker = (String) dataList.get("undertaker");
        String traineeCount = (String) dataList.get("traineeCount");
        String evalCount = (String) dataList.get("evalCount");
        String trainTime = (String) dataList.get("trainTime");
        String verySatisfaction = (String) dataList.get("verySatisfaction");
        String satisfaction = (String) dataList.get("satisfaction");
        String advise = (String) dataList.get("advise");
        String[] headArray1 = (String[]) dataList.get("head1");
        String[] headArray = (String[]) dataList.get("head");
        Integer[] columnWidth = (Integer[]) dataList.get("columnWidth");
        int iColumn = headArray.length;
        Row headRow = sheet.createRow(0);
        headRow.setHeight((short) 0x600);
        Cell cell = headRow.createCell(0);
        cell.setCellValue(className);
        for (int i = 0; i < iColumn; i++) {
            cell = headRow.createCell(i);
            cell.setCellValue(className + "\n教学评估表");
            sheet.setColumnWidth(i, columnWidth[i] * 256);
            cell.setCellStyle(titleStyle);
        }
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, iColumn - 1));


        for (int i = 1; i < 5; i++) {
            headRow = sheet.createRow(i);
            for (int j = 0; j < iColumn; j++) {
                cell = headRow.createCell(j);
                if (j == 0) {
                    cell.setCellStyle(baseTitleStyle);
                    switch (i) {
                        case 1:
                            cell.setCellValue("主办单位");
                            break;
                        case 2:
                            cell.setCellValue("承办单位");
                            break;
                        case 3:
                            cell.setCellValue("培训时间");
                            break;
                        case 4:
                            cell.setCellValue("学员人数");
                            break;
                    }
                } else {
                    cell.setCellStyle(baseContentStyle);
                    switch (i) {
                        case 1:
                            cell.setCellValue(holdUnit);
                            break;
                        case 2:
                            cell.setCellValue(undertaker);
                            break;
                        case 3:
                            cell.setCellValue(trainTime);
                            break;
                        case 4:
                            cell.setCellValue(traineeCount);
                            break;
                    }
                }
            }
            sheet.addMergedRegion(new CellRangeAddress(i, i, 1, iColumn - 1));
        }

        headRow = sheet.createRow(5);
        for (int j = 0; j < 2; j++) {
            cell = headRow.createCell(j);
            cell.setCellStyle(baseTitleStyle);
            cell.setCellValue(headArray1[0]);
        }
        for (int k = 0; k < 6; k++) {
            cell = headRow.createCell(k + 2);
            cell.setCellStyle(baseTitleStyle);
            cell.setCellValue(headArray1[1]);
        }
        sheet.addMergedRegion(new CellRangeAddress(5, 5, 0, 1));
        sheet.addMergedRegion(new CellRangeAddress(5, 5, 2, 7));

        headRow = sheet.createRow(6);
        for (int m = 0; m < iColumn; m++) {
            cell = headRow.createCell(m);
            cell.setCellValue(headArray[m]);
            cell.setCellStyle(baseTitleStyle);
        }
        //评价标准
        int standCount = 1;
        int rowBegin = 7;
        int rowEnd = 1;
        Map<String, List<Map<String, Object>>> standData = (Map<String, List<Map<String, Object>>>) dataList.get("standList");
        //循环每个指标
        for (String s : standData.keySet()) {
            List<Map<String, Object>> value = standData.get(s);  //指标下的标准
            standCount = value.size();
            rowEnd = rowBegin + standCount;
            for (int j = 0; j < standCount; j++) {
                Row textRow = sheet.createRow(j + rowBegin);
                Map<String, Object> map = value.get(j);
                map.remove("INDICATOR_ID");
                int k = 0;//列
                for (String sss : map.keySet()) {
                    Object val = map.get(sss);
                    if (val == null)
                        val = "";

                    cell = textRow.createCell(k);
                    cell.setCellValue(String.valueOf(val));
                    if (k == 0)
                        cell.setCellStyle(baseTitleStyle);
                    else
                        cell.setCellStyle(baseContentStyle);
                    k++;
                }
            }
            sheet.addMergedRegion(new CellRangeAddress(rowBegin, rowEnd - 1, 0, 0));
            rowBegin += standCount;
        }
        //满意度合计
        int IndexColumn = headArray.length;
        headRow = sheet.createRow(rowBegin);
        for (int ii = 0; ii < IndexColumn - 2; ii++) {
            cell = headRow.createCell(ii);
            cell.setCellValue("汇总：");
            cell.setCellStyle(baseTitleStyle);
        }
        sheet.addMergedRegion(new CellRangeAddress(rowBegin, rowBegin, 0, IndexColumn - 3));

        cell = headRow.createCell(IndexColumn - 2);
        cell.setCellStyle(baseContentStyle);
        cell.setCellValue(verySatisfaction);

        cell = headRow.createCell(IndexColumn - 1);
        cell.setCellStyle(baseContentStyle);

        cell.setCellValue(satisfaction);

        rowBegin += 1;
        //意见与建议
        headRow = sheet.createRow(rowBegin);
        headRow.setHeight((short) 0x1200);
        for (int ii = 0; ii < headArray.length; ii++) {
            cell = headRow.createCell(ii);
            cell.setCellValue(advise.replace("|","\n"));
            cell.setCellStyle(baseContentStyle);
        }
        headRow.getCell(0).setCellValue("意见与建议");
        headRow.getCell(0).setCellStyle(baseTitleStyle);
        sheet.addMergedRegion(new CellRangeAddress(rowBegin, rowBegin, 1, IndexColumn - 1));

        return workbook;
    }
}
