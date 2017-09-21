package com.zd.core.util;

import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ImportExcelUtil {

	private final static String excel2003L = ".xls"; // 2003- 版本的excel
	private final static String excel2007U = ".xlsx"; // 2007+ 版本的excel

	/**
	 * 描述：获取IO流中的数据，组装成List<List<Object>>对象
	 * 
	 * @param in,fileName
	 * @return
	 * @throws IOException
	 */
	public List<List<Object>> getBankListByExcel(InputStream in, String fileName) throws Exception {
		List<List<Object>> list = null;

		// 创建Excel工作薄
		Workbook work = this.getWorkbook(in, fileName);
		if (null == work) {
			throw new Exception("创建Excel工作薄为空！");
		}
		Sheet sheet = null;
		Row row = null;
		Cell cell = null;

		list = new ArrayList<List<Object>>();
		// 遍历Excel中所有的sheet
		// for (int i = 0; i < work.getNumberOfSheets(); i++) {
		for (int i = 0; i < 1; i++) {
			sheet = work.getSheetAt(i);
			if (sheet == null) {
				continue;
			}

			// 遍历当前sheet中的所有行
			int countCellNum = 0;
			for (int j = sheet.getFirstRowNum(); j <= sheet.getLastRowNum(); j++) {
				row = sheet.getRow(j);
				if (row == null || row.getFirstCellNum() == j) {
					if (row != null)
						countCellNum = row.getLastCellNum();
					continue;
				}

				// 遍历所有的列
				List<Object> li = new ArrayList<Object>();
				for (int y = row.getFirstCellNum(); y <= countCellNum; y++) {
					cell = row.getCell(y);
					li.add(this.getCellValue(cell));
				}

//				if (li.size() < countCellNum) {
//					int num = countCellNum - li.size();
//					for (int z = 0; z < num; z++) {
//						li.add("");
//					}
//				}	
				if(li.size() >= countCellNum)
					list.add(li);
				
			}
		}
		work.close();
		return list;
	}

	/**
	 * 描述：根据文件后缀，自适应上传文件的版本
	 * 
	 * @param inStr,fileName
	 * @return
	 * @throws Exception
	 */
	public Workbook getWorkbook(InputStream inStr, String fileName) throws Exception {
		Workbook wb = null;
		String fileType = fileName.substring(fileName.lastIndexOf("."));
		if (excel2003L.equals(fileType)) {
			wb = new HSSFWorkbook(inStr); // 2003-
		} else if (excel2007U.equals(fileType)) {
			wb = new XSSFWorkbook(inStr); // 2007+
		} else {
			throw new Exception("解析的文件格式有误！");
		}
		return wb;
	}

	/**
	 * 描述：对表格中数值进行格式化
	 * 
	 * @param cell
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public Object getCellValue(Cell cell) {
		Object value = null;

		if (cell == null) {
			value = "";
			return value;
		}

		DecimalFormat df = new DecimalFormat("0"); // 格式化number String字符
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 日期格式化
		DecimalFormat df2 = new DecimalFormat("0.00"); // 格式化数字
		
		Short thisDataFormat =cell.getCellStyle().getDataFormat();
		
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_STRING:
			value = cell.getRichStringCellValue().getString();
			break;
		case Cell.CELL_TYPE_NUMERIC:
			if (HSSFDateUtil.isCellDateFormatted(cell)) {// 处理日期格式、时间格式

				if (thisDataFormat == HSSFDataFormat.getBuiltinFormat("h:mm")
						|| thisDataFormat == 176 || thisDataFormat == 177) {
					sdf = new SimpleDateFormat("HH:mm");
				}
				Date date = cell.getDateCellValue();
				value = sdf.format(date);

			} else if ( thisDataFormat == 58) {
				// 处理自定义日期格式：m月d日(通过判断单元格的格式id解决，id的值是58)
				SimpleDateFormat sdf2 = new SimpleDateFormat("MM-dd");
				double cellValue = cell.getNumericCellValue();
				Date date = org.apache.poi.ss.usermodel.DateUtil.getJavaDate(cellValue);
				value = sdf2.format(date);
			} else if ( thisDataFormat == 183 || thisDataFormat == 177 || thisDataFormat == 176) {
				// 处理自定义日期格式：y年m月d日(通过判断单元格的格式id解决，id的值是183)
				SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
				Date date = cell.getDateCellValue();
				value = sdf2.format(date);
			} else {
				if ("General".equals(cell.getCellStyle().getDataFormatString())) {
					value = df.format(cell.getNumericCellValue());
				} else {
					value = df2.format(cell.getNumericCellValue());
				}
			}

			break;
		case Cell.CELL_TYPE_BOOLEAN:
			value = cell.getBooleanCellValue();
			break;
		case Cell.CELL_TYPE_BLANK:
			value = "";
			break;
		default:
			break;
		}
		return value;
	}

}