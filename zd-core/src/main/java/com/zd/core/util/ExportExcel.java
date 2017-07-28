package com.zd.core.util;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.*;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExportExcel {

	public final static <T> String exportToExcel(HttpServletResponse response, String fileName, String[] Title,
			List<T> listContent, List<String> excludeContent, Integer[] coulumnWidth, Integer[] coulumnDirection) {
		String result = "系统提示：Excel文件导出成功！";
		// 以下开始输出到EXCEL
		try {
			// 定义输出流，以便打开保存对话框______________________begin
			// HttpServletResponse response=HttpServletSessi .getResponse();
			OutputStream os = response.getOutputStream();// 取得输出流
			response.reset();// 清空输出流
			response.setHeader("Content-disposition",
					"attachment; filename=" + new String((fileName + ".xls").getBytes("GB2312"), "ISO8859-1"));
			// 设定输出文件头
			response.setContentType("application/msexcel");// 定义输出类型
			// 定义输出流，以便打开保存对话框_______________________end

			/** **********创建工作簿************ */
			WritableWorkbook workbook = Workbook.createWorkbook(os);

			/** **********创建工作表************ */

			WritableSheet sheet = workbook.createSheet("Sheet1", 0);

			/** **********设置纵横打印（默认为纵打）、打印纸***************** */
			jxl.SheetSettings sheetset = sheet.getSettings();
			sheetset.setProtected(false);

			/** ************设置单元格字体************** */
			WritableFont NormalFont = new WritableFont(WritableFont.ARIAL, 10);
			WritableFont BoldFont = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);

			/** ************以下设置三种单元格样式，灵活备用************ */
			// 用于标题居中
			WritableCellFormat wcf_centerTitle = new WritableCellFormat(BoldFont);
			wcf_centerTitle.setBorder(Border.NONE, BorderLineStyle.THIN); // 线条
			wcf_centerTitle.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐
			wcf_centerTitle.setAlignment(Alignment.CENTRE); // 文字水平对齐
			wcf_centerTitle.setWrap(false); // 文字是否换行

			// 用于标题居左
			WritableCellFormat wcf_leftTitle = new WritableCellFormat(BoldFont);
			wcf_leftTitle.setBorder(Border.NONE, BorderLineStyle.THIN); // 线条
			wcf_leftTitle.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐
			wcf_leftTitle.setAlignment(Alignment.LEFT); // 文字水平对齐
			wcf_leftTitle.setWrap(false); // 文字是否换行

			// 用于标题居右
			WritableCellFormat wcf_rightTitle = new WritableCellFormat(BoldFont);
			wcf_rightTitle.setBorder(Border.NONE, BorderLineStyle.THIN); // 线条
			wcf_rightTitle.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐
			wcf_rightTitle.setAlignment(Alignment.RIGHT); // 文字水平对齐
			wcf_rightTitle.setWrap(false); // 文字是否换行

			// 用于正文居左
			WritableCellFormat wcf_left = new WritableCellFormat(NormalFont);
			wcf_left.setBorder(Border.NONE, BorderLineStyle.THIN); // 线条
			wcf_left.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐
			wcf_left.setAlignment(Alignment.LEFT); // 文字水平对齐
			wcf_left.setWrap(false); // 文字是否换行

			// 用于正文居右
			WritableCellFormat wcf_right = new WritableCellFormat(NormalFont);
			wcf_right.setBorder(Border.NONE, BorderLineStyle.THIN); // 线条
			wcf_right.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐
			wcf_right.setAlignment(Alignment.RIGHT); // 文字水平对齐
			wcf_right.setWrap(true); // 文字是否换行

			/** ***************以下是EXCEL开头大标题********************* */
			sheet.mergeCells(0, 0, Title.length, 0);
			sheet.addCell(new Label(0, 0, fileName, wcf_centerTitle));
			sheet.setRowView(0, 500);
			/** ***************以下是EXCEL第一行列标题********************* */
			for (int i = 0; i < Title.length; i++) {
				if (coulumnDirection[i] == 1) // 左
					sheet.addCell(new Label(i, 1, Title[i], wcf_leftTitle));
				else if (coulumnDirection[i] == 2) // 右
					sheet.addCell(new Label(i, 1, Title[i], wcf_rightTitle));
				else // 中
					sheet.addCell(new Label(i, 1, Title[i], wcf_centerTitle));

				sheet.setColumnView(i, coulumnWidth[i]); // 设置列宽
				//sheet.setRowGroup(arg0, arg1, arg2);
			}

			/** ***************以下是EXCEL正文数据********************* */
			// CellView cellView = new CellView();
			// cellView.setAutosize(true); //设置自动大小

			Pattern p = Pattern.compile("((^[+-]?[1-9]{1}[0-9]{0,7})|(^[0]{1}))(0?\\.\\d{1,2})?$");

			// 2 位小数
			jxl.write.NumberFormat nf = new jxl.write.NumberFormat("0.00"); // 设置数字格式
			// 左对齐
			jxl.write.WritableCellFormat wcfN_Left = new jxl.write.WritableCellFormat(nf); // 设置表单格式
			wcfN_Left.setBorder(Border.NONE, BorderLineStyle.THIN); // 线条
			wcfN_Left.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐
			wcfN_Left.setAlignment(Alignment.LEFT);
			wcfN_Left.setWrap(false); // 文字是否换行

			// 右对齐
			jxl.write.WritableCellFormat wcfN_Right = new jxl.write.WritableCellFormat(nf); // 设置表单格式
			wcfN_Right.setBorder(Border.NONE, BorderLineStyle.THIN); // 线条
			wcfN_Right.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐
			wcfN_Right.setAlignment(Alignment.RIGHT);
			wcfN_Right.setWrap(false); // 文字是否换行

			// 整数
			Pattern p2 = Pattern.compile("^[+-]?\\d{1,}$");
			// 左对齐
			jxl.write.WritableCellFormat wcfN_Integer_Left = new jxl.write.WritableCellFormat(NumberFormats.INTEGER); // 设置表单格式
			wcfN_Integer_Left.setBorder(Border.NONE, BorderLineStyle.THIN); // 线条
			wcfN_Integer_Left.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐
			wcfN_Integer_Left.setAlignment(Alignment.LEFT);
			wcfN_Integer_Left.setWrap(false); // 文字是否换行

			// 右对齐
			jxl.write.WritableCellFormat wcfN_Integer_Right = new jxl.write.WritableCellFormat(NumberFormats.INTEGER); // 设置表单格式
			wcfN_Integer_Right.setBorder(Border.NONE, BorderLineStyle.THIN); // 线条
			wcfN_Integer_Right.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐
			wcfN_Integer_Right.setAlignment(Alignment.RIGHT);
			wcfN_Integer_Right.setWrap(false); // 文字是否换行

			/** ***************以下是EXCEL正文数据********************* */
			Field[] fields = null;
			int i = 2;
			for (Object obj : listContent) {
				fields = obj.getClass().getDeclaredFields();
				int j = 0;
				for (Field v : fields) {
					if (excludeContent.contains(v.getName()))
						continue;

					v.setAccessible(true);
					Object val = v.get(obj);

					if (val == null) {
						val = "";
					}
					if (coulumnDirection[j] == 1) // 左
					{
						Matcher m1 = p2.matcher(val.toString().trim());
						Matcher m2 = p.matcher(val.toString().trim());
						if (m1.matches()) {
							jxl.write.Number labelNF = new jxl.write.Number(j, i, Double.parseDouble(val.toString()),
									wcfN_Integer_Left); // 格式化数值
							sheet.addCell(labelNF);
						} else if (m2.matches()) {
							jxl.write.Number labelNF = new jxl.write.Number(j, i, Double.parseDouble(val.toString()),
									wcfN_Left); // 格式化数值
							sheet.addCell(labelNF);
						} else
							sheet.addCell(new Label(j, i, val.toString(), wcf_left));

					} else if (coulumnDirection[j] == 2) // 右
					{
						if (val instanceof String) {
							sheet.addCell(new Label(j, i, val.toString(), wcf_right));
						} else {
							jxl.write.Number labelNF = new jxl.write.Number(j, i, Double.parseDouble(val.toString()),
									wcfN_Integer_Left); // 格式化数值
							sheet.addCell(labelNF);
						}
					} else {

						sheet.addCell(new Label(j, i, val.toString(), wcf_left));
					}

					j++;
				}
				i++;
			}

			/** **********将以上缓存中的内容写到EXCEL文件中******** */
			workbook.write();
			/** *********关闭文件************* */
			workbook.close();

		} catch (Exception e) {
			result = "系统提示：Excel文件导出失败，原因：" + e.toString();
			System.out.println(result);
			e.printStackTrace();
		}
		return result;

	}

	public final static <T> String exportExcel(HttpServletResponse response, String fileName, String[] Title,
			List<T> listContent, List<String> excludeContent, Integer[] coulumnWidth, Integer[] coulumnDirection) {
		String result = "系统提示：Excel文件导出成功！";
		// 以下开始输出到EXCEL
		try {
			// 定义输出流，以便打开保存对话框______________________begin
			// HttpServletResponse response=HttpServletSessi .getResponse();
			OutputStream os = response.getOutputStream();// 取得输出流
			response.reset();// 清空输出流
			response.setHeader("Content-disposition",
					"attachment; filename=" + new String((fileName + ".xls").getBytes("GB2312"), "ISO8859-1"));
			// 设定输出文件头
			response.setContentType("application/msexcel");// 定义输出类型
			// 定义输出流，以便打开保存对话框_______________________end

			/** **********创建工作簿************ */
			WritableWorkbook workbook = Workbook.createWorkbook(os);

			/** **********创建工作表************ */

			WritableSheet sheet = workbook.createSheet("Sheet1", 0);

			/** **********设置纵横打印（默认为纵打）、打印纸***************** */
			jxl.SheetSettings sheetset = sheet.getSettings();
			sheetset.setProtected(false);

			/** ************设置单元格字体************** */
			WritableFont NormalFont = new WritableFont(WritableFont.ARIAL, 10);
			WritableFont BoldFont = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);

			/** ************以下设置三种单元格样式，灵活备用************ */
			// 用于标题居中
			WritableCellFormat wcf_centerTitle = new WritableCellFormat(BoldFont);
			wcf_centerTitle.setBorder(Border.NONE, BorderLineStyle.THIN); // 线条
			wcf_centerTitle.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐
			wcf_centerTitle.setAlignment(Alignment.CENTRE); // 文字水平对齐
			wcf_centerTitle.setWrap(false); // 文字是否换行

			// 用于标题居左
			WritableCellFormat wcf_leftTitle = new WritableCellFormat(BoldFont);
			wcf_leftTitle.setBorder(Border.NONE, BorderLineStyle.THIN); // 线条
			wcf_leftTitle.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐
			wcf_leftTitle.setAlignment(Alignment.LEFT); // 文字水平对齐
			wcf_leftTitle.setWrap(false); // 文字是否换行

			// 用于标题居右
			WritableCellFormat wcf_rightTitle = new WritableCellFormat(BoldFont);
			wcf_rightTitle.setBorder(Border.NONE, BorderLineStyle.THIN); // 线条
			wcf_rightTitle.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐
			wcf_rightTitle.setAlignment(Alignment.RIGHT); // 文字水平对齐
			wcf_rightTitle.setWrap(false); // 文字是否换行

			// 用于正文居左
			WritableCellFormat wcf_left = new WritableCellFormat(NormalFont);
			wcf_left.setBorder(Border.NONE, BorderLineStyle.THIN); // 线条
			wcf_left.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐
			wcf_left.setAlignment(Alignment.LEFT); // 文字水平对齐
			wcf_left.setWrap(false); // 文字是否换行

			// 用于正文居右
			WritableCellFormat wcf_right = new WritableCellFormat(NormalFont);
			wcf_right.setBorder(Border.NONE, BorderLineStyle.THIN); // 线条
			wcf_right.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐
			wcf_right.setAlignment(Alignment.RIGHT); // 文字水平对齐
			wcf_right.setWrap(false); // 文字是否换行

			/** ***************以下是EXCEL开头大标题********************* */
			sheet.mergeCells(0, 0, Title.length, 0);
			sheet.addCell(new Label(0, 0, fileName, wcf_centerTitle));
			sheet.setRowView(0, 500);
			/** ***************以下是EXCEL第一行列标题********************* */
			for (int i = 0; i < Title.length; i++) {
				if (coulumnDirection[i] == 1) // 左
					sheet.addCell(new Label(i, 1, Title[i], wcf_leftTitle));
				else if (coulumnDirection[i] == 2) // 右
					sheet.addCell(new Label(i, 1, Title[i], wcf_rightTitle));
				else // 中
					sheet.addCell(new Label(i, 1, Title[i], wcf_centerTitle));

				sheet.setColumnView(i, coulumnWidth[i]); // 设置列宽
			}

			/** ***************以下是EXCEL正文数据********************* */
			// CellView cellView = new CellView();
			// cellView.setAutosize(true); //设置自动大小

			Pattern p = Pattern.compile("((^[+-]?[1-9]{1}[0-9]{0,7})|(^[0]{1}))(0?\\.\\d{1,2})?$");

			// 2 位小数
			jxl.write.NumberFormat nf = new jxl.write.NumberFormat("0.00"); // 设置数字格式
			// 左对齐
			jxl.write.WritableCellFormat wcfN_Left = new jxl.write.WritableCellFormat(nf); // 设置表单格式
			wcfN_Left.setBorder(Border.NONE, BorderLineStyle.THIN); // 线条
			wcfN_Left.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐
			wcfN_Left.setAlignment(Alignment.LEFT);
			wcfN_Left.setWrap(false); // 文字是否换行

			// 右对齐
			jxl.write.WritableCellFormat wcfN_Right = new jxl.write.WritableCellFormat(nf); // 设置表单格式
			wcfN_Right.setBorder(Border.NONE, BorderLineStyle.THIN); // 线条
			wcfN_Right.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐
			wcfN_Right.setAlignment(Alignment.RIGHT);
			wcfN_Right.setWrap(false); // 文字是否换行

			// 整数
			Pattern p2 = Pattern.compile("^[+-]?\\d{1,}$");
			// 左对齐
			jxl.write.WritableCellFormat wcfN_Integer_Left = new jxl.write.WritableCellFormat(NumberFormats.INTEGER); // 设置表单格式
			wcfN_Integer_Left.setBorder(Border.NONE, BorderLineStyle.THIN); // 线条
			wcfN_Integer_Left.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐
			wcfN_Integer_Left.setAlignment(Alignment.LEFT);
			wcfN_Integer_Left.setWrap(false); // 文字是否换行

			// 右对齐
			jxl.write.WritableCellFormat wcfN_Integer_Right = new jxl.write.WritableCellFormat(NumberFormats.INTEGER); // 设置表单格式
			wcfN_Integer_Right.setBorder(Border.NONE, BorderLineStyle.THIN); // 线条
			wcfN_Integer_Right.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐
			wcfN_Integer_Right.setAlignment(Alignment.RIGHT);
			wcfN_Integer_Right.setWrap(false); // 文字是否换行

			/** ***************以下是EXCEL正文数据********************* */
			Field[] fields = null;
			int i = 2;
			for (Object obj : listContent) {
				fields = obj.getClass().getDeclaredFields();
				int j = 0;
				for (Field v : fields) {
					if (excludeContent.contains(v.getName()))
						continue;

					v.setAccessible(true);
					Object val = v.get(obj);
					if (val == null) {
						val = "";
					}
					if (coulumnDirection[j] == 1) // 左
					{
						Matcher m1 = p2.matcher(val.toString().trim());
						Matcher m2 = p.matcher(val.toString().trim());
						if (m1.matches()) {
							jxl.write.Number labelNF = new jxl.write.Number(j, i, Double.parseDouble(val.toString()),
									wcfN_Integer_Left); // 格式化数值
							sheet.addCell(labelNF);
						} else if (m2.matches()) {
							jxl.write.Number labelNF = new jxl.write.Number(j, i, Double.parseDouble(val.toString()),
									wcfN_Left); // 格式化数值
							sheet.addCell(labelNF);
						} else
							sheet.addCell(new Label(j, i, val.toString(), wcf_left));

					} else if (coulumnDirection[j] == 2) // 右
					{
						Matcher m1 = p2.matcher(val.toString().trim());
						Matcher m2 = p.matcher(val.toString().trim());
						if (m1.matches()) {
							jxl.write.Number labelNF = new jxl.write.Number(j, i, Double.parseDouble(val.toString()),
									wcfN_Integer_Right); // 格式化数值
							sheet.addCell(labelNF);
						} else if (m2.matches()) {
							jxl.write.Number labelNF = new jxl.write.Number(j, i, Double.parseDouble(val.toString()),
									wcfN_Right); // 格式化数值
							sheet.addCell(labelNF);
						} else
							sheet.addCell(new Label(j, i, val.toString(), wcf_right));
					} else {

						Matcher m1 = p2.matcher(val.toString().trim());
						Matcher m2 = p.matcher(val.toString().trim());
						if (m1.matches()) {
							jxl.write.Number labelNF = new jxl.write.Number(j, i, Double.parseDouble(val.toString()),
									wcfN_Integer_Left); // 格式化数值
							sheet.addCell(labelNF);
						} else if (m2.matches()) {
							jxl.write.Number labelNF = new jxl.write.Number(j, i, Double.parseDouble(val.toString()),
									wcfN_Left); // 格式化数值
							sheet.addCell(labelNF);
						} else
							sheet.addCell(new Label(j, i, val.toString(), wcf_left));
					}

					j++;
				}
				i++;
			}

			/** **********将以上缓存中的内容写到EXCEL文件中******** */
			workbook.write();
			/** *********关闭文件************* */
			workbook.close();

		} catch (Exception e) {
			result = "系统提示：Excel文件导出失败，原因：" + e.toString();
			System.out.println(result);
			e.printStackTrace();
		}
		return result;

	}

	public final static String exportExcelInHashMap(HttpServletResponse response, String fileName, String[] Title,
			List<Map<String, Object>> listContent, List<String> excludeContent, Integer[] coulumnWidth,
			Integer[] coulumnDirection) {
		String result = "系统提示：Excel文件导出成功！";
		// 以下开始输出到EXCEL
		try {
			// 定义输出流，以便打开保存对话框______________________begin
			// HttpServletResponse response=HttpServletSessi .getResponse();
			OutputStream os = response.getOutputStream();// 取得输出流
			response.reset();// 清空输出流
			response.setHeader("Content-disposition",
					"attachment; filename=" + new String((fileName + ".xls").getBytes("GB2312"), "ISO8859-1"));
			// 设定输出文件头
			response.setContentType("application/msexcel");// 定义输出类型
			// 定义输出流，以便打开保存对话框_______________________end

			/** **********创建工作簿************ */
			WritableWorkbook workbook = Workbook.createWorkbook(os);

			/** **********创建工作表************ */

			WritableSheet sheet = workbook.createSheet("Sheet1", 0);

			/** **********设置纵横打印（默认为纵打）、打印纸***************** */
			jxl.SheetSettings sheetset = sheet.getSettings();
			sheetset.setProtected(false);

			/** ************设置单元格字体************** */
			WritableFont NormalFont = new WritableFont(WritableFont.ARIAL, 10);
			WritableFont BoldFont = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);

			/** ************以下设置三种单元格样式，灵活备用************ */
			// 用于标题居中
			WritableCellFormat wcf_centerTitle = new WritableCellFormat(BoldFont);
			wcf_centerTitle.setBorder(Border.NONE, BorderLineStyle.THIN); // 线条
			wcf_centerTitle.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐
			wcf_centerTitle.setAlignment(Alignment.CENTRE); // 文字水平对齐
			wcf_centerTitle.setWrap(false); // 文字是否换行

			// 用于标题居左
			WritableCellFormat wcf_leftTitle = new WritableCellFormat(BoldFont);
			wcf_leftTitle.setBorder(Border.NONE, BorderLineStyle.THIN); // 线条
			wcf_leftTitle.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐
			wcf_leftTitle.setAlignment(Alignment.LEFT); // 文字水平对齐
			wcf_leftTitle.setWrap(false); // 文字是否换行

			// 用于标题居右
			WritableCellFormat wcf_rightTitle = new WritableCellFormat(BoldFont);
			wcf_rightTitle.setBorder(Border.NONE, BorderLineStyle.THIN); // 线条
			wcf_rightTitle.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐
			wcf_rightTitle.setAlignment(Alignment.RIGHT); // 文字水平对齐
			wcf_rightTitle.setWrap(false); // 文字是否换行

			// 用于正文居左
			WritableCellFormat wcf_left = new WritableCellFormat(NormalFont);
			wcf_left.setBorder(Border.NONE, BorderLineStyle.THIN); // 线条
			wcf_left.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐
			wcf_left.setAlignment(Alignment.LEFT); // 文字水平对齐
			wcf_left.setWrap(false); // 文字是否换行

			// 用于正文居右
			WritableCellFormat wcf_right = new WritableCellFormat(NormalFont);
			wcf_right.setBorder(Border.NONE, BorderLineStyle.THIN); // 线条
			wcf_right.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐
			wcf_right.setAlignment(Alignment.RIGHT); // 文字水平对齐
			wcf_right.setWrap(false); // 文字是否换行
			
			// 用于正文居中
			WritableCellFormat wcf_center = new WritableCellFormat(NormalFont);
			wcf_center.setBorder(Border.NONE, BorderLineStyle.THIN); // 线条
			wcf_center.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐
			wcf_center.setAlignment(Alignment.CENTRE); // 文字水平对齐
			wcf_center.setWrap(false); // 文字是否换行

			/** ***************以下是EXCEL开头大标题********************* */
			sheet.mergeCells(0, 0, Title.length, 0);
			sheet.addCell(new Label(0, 0, fileName, wcf_centerTitle));
			sheet.setRowView(0, 500);
			/** ***************以下是EXCEL第一行列标题********************* */
			for (int i = 0; i < Title.length; i++) {
				if (coulumnDirection[i] == 1) // 左
					sheet.addCell(new Label(i, 1, Title[i], wcf_leftTitle));
				else if (coulumnDirection[i] == 2) // 右
					sheet.addCell(new Label(i, 1, Title[i], wcf_rightTitle));
				else // 中
					sheet.addCell(new Label(i, 1, Title[i], wcf_centerTitle));

				sheet.setColumnView(i, coulumnWidth[i]); // 设置列宽
			}

			/** ***************以下是EXCEL正文数据********************* */
			// CellView cellView = new CellView();
			// cellView.setAutosize(true); //设置自动大小

			Pattern p = Pattern.compile("((^[+-]?[1-9]{1}[0-9]{0,7})|(^[0]{1}))(0?\\.\\d{1,2})?$");

			// 2 位小数
			jxl.write.NumberFormat nf = new jxl.write.NumberFormat("0.00"); // 设置数字格式
			// 左对齐
			jxl.write.WritableCellFormat wcfN_Left = new jxl.write.WritableCellFormat(nf); // 设置表单格式
			wcfN_Left.setBorder(Border.NONE, BorderLineStyle.THIN); // 线条
			wcfN_Left.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐
			wcfN_Left.setAlignment(Alignment.LEFT);
			wcfN_Left.setWrap(false); // 文字是否换行

			// 右对齐
			jxl.write.WritableCellFormat wcfN_Right = new jxl.write.WritableCellFormat(nf); // 设置表单格式
			wcfN_Right.setBorder(Border.NONE, BorderLineStyle.THIN); // 线条
			wcfN_Right.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐
			wcfN_Right.setAlignment(Alignment.RIGHT);
			wcfN_Right.setWrap(false); // 文字是否换行
			
			// 中对齐
			jxl.write.WritableCellFormat wcfN_Center = new jxl.write.WritableCellFormat(nf); // 设置表单格式
			wcfN_Center.setBorder(Border.NONE, BorderLineStyle.THIN); // 线条
			wcfN_Center.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐
			wcfN_Center.setAlignment(Alignment.CENTRE);
			wcfN_Center.setWrap(false); // 文字是否换行

			// 整数
			Pattern p2 = Pattern.compile("^[+-]?\\d{1,}$");
			// 左对齐
			jxl.write.WritableCellFormat wcfN_Integer_Left = new jxl.write.WritableCellFormat(NumberFormats.INTEGER); // 设置表单格式
			wcfN_Integer_Left.setBorder(Border.NONE, BorderLineStyle.THIN); // 线条
			wcfN_Integer_Left.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐
			wcfN_Integer_Left.setAlignment(Alignment.LEFT);
			wcfN_Integer_Left.setWrap(false); // 文字是否换行

			// 右对齐
			jxl.write.WritableCellFormat wcfN_Integer_Right = new jxl.write.WritableCellFormat(NumberFormats.INTEGER); // 设置表单格式
			wcfN_Integer_Right.setBorder(Border.NONE, BorderLineStyle.THIN); // 线条
			wcfN_Integer_Right.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐
			wcfN_Integer_Right.setAlignment(Alignment.RIGHT);
			wcfN_Integer_Right.setWrap(false); // 文字是否换行
			
			// 中对齐
			jxl.write.WritableCellFormat wcfN_Integer_Center = new jxl.write.WritableCellFormat(NumberFormats.INTEGER); // 设置表单格式
			wcfN_Integer_Center.setBorder(Border.NONE, BorderLineStyle.THIN); // 线条
			wcfN_Integer_Center.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐
			wcfN_Integer_Center.setAlignment(Alignment.CENTRE);
			wcfN_Integer_Center.setWrap(false); // 文字是否换行

			int i = 2;
			for (Map<String, Object> map : listContent) {

				int j = 0;
				for (String s : map.keySet()) {
					if (excludeContent.contains(s))
						continue;
					Object val = map.get(s);
					if (val == null)
						val = "";
					/*
					 * if(i==2){ sheet.setColumnView(j, coulumnWidth.get(j));
					 * //设置列宽 }
					 */

					if (coulumnDirection[j] == 1) // 左
					{
						Matcher m1 = p2.matcher(val.toString().trim());
						Matcher m2 = p.matcher(val.toString().trim());
						if (m1.matches()) {
							jxl.write.Number labelNF = new jxl.write.Number(j, i, Double.parseDouble(val.toString()),
									wcfN_Integer_Left); // 格式化数值
							sheet.addCell(labelNF);
						} else if (m2.matches()) {
							jxl.write.Number labelNF = new jxl.write.Number(j, i, Double.parseDouble(val.toString()),
									wcfN_Left); // 格式化数值
							sheet.addCell(labelNF);
						} else
							sheet.addCell(new Label(j, i, val.toString(), wcf_left));

					} else if (coulumnDirection[j] == 2) // 右
					{
						Matcher m1 = p2.matcher(val.toString().trim());
						Matcher m2 = p.matcher(val.toString().trim());
						if (m1.matches()) {
							jxl.write.Number labelNF = new jxl.write.Number(j, i, Double.parseDouble(val.toString()),
									wcfN_Integer_Right); // 格式化数值
							sheet.addCell(labelNF);
						} else if (m2.matches()) {
							jxl.write.Number labelNF = new jxl.write.Number(j, i, Double.parseDouble(val.toString()),
									wcfN_Right); // 格式化数值
							sheet.addCell(labelNF);
						} else
							sheet.addCell(new Label(j, i, val.toString(), wcf_right));
					} else {
						Matcher m1 = p2.matcher(val.toString().trim());
						Matcher m2 = p.matcher(val.toString().trim());
						if (m1.matches()) {
							jxl.write.Number labelNF = new jxl.write.Number(j, i, Double.parseDouble(val.toString()),
									wcfN_Integer_Center); // 格式化数值
							sheet.addCell(labelNF);
						} else if (m2.matches()) {
							jxl.write.Number labelNF = new jxl.write.Number(j, i, Double.parseDouble(val.toString()),
									wcfN_Center); // 格式化数值
							sheet.addCell(labelNF);
						} else
							sheet.addCell(new Label(j, i, val.toString(), wcf_center));
					}

					j++;
				}
				i++;
			}

			/** **********将以上缓存中的内容写到EXCEL文件中******** */
			workbook.write();
			/** *********关闭文件************* */
			workbook.close();

		} catch (Exception e) {
			result = "系统提示：Excel文件导出失败，原因：" + e.toString();
			System.out.println(result);
			e.printStackTrace();
		}
		return result;

	}
}
