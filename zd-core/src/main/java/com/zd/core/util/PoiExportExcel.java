package com.zd.core.util;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;

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
				// 第一行先创建一个大标题
				Row sheetTitleRow = sheet.createRow(rowNum);
				sheetTitleRow.setHeight((short) 0x300);
				Cell sheetTitleCell = sheetTitleRow.createCell(0);
				sheetTitleCell.setCellStyle(titleStyle);
				sheetTitleCell.setCellValue(sheetTitle);
				sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 0, colCount - 1));
				rowNum++;

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
}
