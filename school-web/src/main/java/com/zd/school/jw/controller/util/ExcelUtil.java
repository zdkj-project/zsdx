package com.zd.school.jw.controller.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.POIXMLDocument;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

/**
 * Excel导入导出工具类
 * 
 * @author WHS
 * 
 */
public class ExcelUtil {

	/**
	 * Excel导入数据
	 * 
	 * @param inputUrl
	 * @return
	 * @throws Exception
	 */
	public List<List<String>> importExcel(String inputUrl) throws Exception {

		int columns = 0, rows = 0;
		List<String> dataRow = null;
		List<List<String>> newDataList = new ArrayList<List<String>>();
		// 区分xls与xlsx
		if (inputUrl.substring(inputUrl.lastIndexOf(".")).trim().equals(".xls")) {
			// 创建POI
			POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(inputUrl));

			// 创建工作表
			HSSFWorkbook templatewb = (HSSFWorkbook) create(new FileInputStream(inputUrl));
			// 直接取第一个sheet对象
			HSSFSheet templateSheet = templatewb.getSheetAt(0);

			// 取得Excel的总列数,总行数
			columns = templateSheet.getRow((short) 0).getPhysicalNumberOfCells();
			rows = templateSheet.getPhysicalNumberOfRows();

			HSSFRow newRow = null;
			HSSFCell newCell = null;
			for (int i = 1; i < rows; i++) {
				dataRow = new ArrayList<String>();
				for (int j = 0; j < columns; j++) {
					// 获取行
					newRow = templateSheet.getRow(i);
					// 获取某行某列的某一个单元格
					newCell = newRow.getCell(j);
					// 往dataRow存值
					dataRow.add(getCellValue(newCell));
				}
				newDataList.add(dataRow);
			}
		} else {
			// 创建文件流
			FileInputStream fs = new FileInputStream(inputUrl);
			// 创建工作表
			XSSFWorkbook templatewb = (XSSFWorkbook) create(new FileInputStream(inputUrl));
			// 直接取第一个sheet对象
			XSSFSheet templateSheet = templatewb.getSheetAt(0);

			// 取得Excel的总列数,总行数
			columns = templateSheet.getRow((short) 0).getPhysicalNumberOfCells();
			rows = templateSheet.getPhysicalNumberOfRows();

			XSSFRow newRow = null;
			XSSFCell newCell = null;
			for (int i = 1; i < rows; i++) {
				dataRow = new ArrayList<String>();
				for (int j = 0; j < columns; j++) {
					// 获取行
					newRow = templateSheet.getRow(i);
					// 获取某行某列的某一个单元格
					newCell = newRow.getCell(j);
					// 往dataRow存值
					dataRow.add(getCellValue(newCell));
				}
				newDataList.add(dataRow);
			}
		}
		return newDataList;
	}

	/**
	 * Excel导入数据
	 * 
	 * @param inputUrl
	 * @return
	 * @throws Exception
	 */
	public List<List<String>> importExcel(MultipartFile file) throws Exception {
		String inputUrl = file.getOriginalFilename();
		System.out.println(inputUrl);
		int columns = 0, rows = 0;
		List<String> dataRow = null;
		List<List<String>> newDataList = new ArrayList<List<String>>();
		// 区分xls与xlsx
		if (inputUrl.substring(inputUrl.lastIndexOf(".")).trim().equals(".xls")) {
			
			// 创建工作表
			HSSFWorkbook templatewb = (HSSFWorkbook) create(file.getInputStream());
			// 直接取第一个sheet对象
			HSSFSheet templateSheet = templatewb.getSheetAt(0);

			// 取得Excel的总列数,总行数
			columns = templateSheet.getRow((short) 0).getPhysicalNumberOfCells();
			rows = templateSheet.getPhysicalNumberOfRows();

			HSSFRow newRow = null;
			HSSFCell newCell = null;
			for (int i = 1; i < rows; i++) {
				dataRow = new ArrayList<String>();
				for (int j = 0; j < columns; j++) {
					// 获取行
					newRow = templateSheet.getRow(i);
					// 获取某行某列的某一个单元格
					newCell = newRow.getCell(j);
					// 往dataRow存值
					dataRow.add(getCellValue(newCell));
				}
				newDataList.add(dataRow);
			}
		} else {
		
		
			// 创建工作表
			XSSFWorkbook templatewb = (XSSFWorkbook) create(file.getInputStream());
			// 直接取第一个sheet对象
			XSSFSheet templateSheet = templatewb.getSheetAt(0);

			// 取得Excel的总列数,总行数
			columns = templateSheet.getRow((short) 0).getPhysicalNumberOfCells();
			rows = templateSheet.getPhysicalNumberOfRows();

			XSSFRow newRow = null;
			XSSFCell newCell = null;
			for (int i = 1; i < rows; i++) {
				dataRow = new ArrayList<String>();
				for (int j = 0; j < columns; j++) {
					// 获取行
					newRow = templateSheet.getRow(i);
					// 获取某行某列的某一个单元格
					newCell = newRow.getCell(j);
					// 往dataRow存值
					dataRow.add(getCellValue(newCell));
				}
				newDataList.add(dataRow);
			}
		}
		return newDataList;
	}

	private Workbook create(InputStream in) throws IOException, InvalidFormatException {
		if (!in.markSupported()) {
			in = new PushbackInputStream(in, 8);
		}
		if (POIFSFileSystem.hasPOIFSHeader(in)) {
			return new HSSFWorkbook(in);
		}
		if (POIXMLDocument.hasOOXMLHeader(in)) {
			return new XSSFWorkbook(OPCPackage.open(in));
		}
		throw new IllegalArgumentException("你的excel版本目前poi解析不了");

	}

	/**
	 * Excel导出数据
	 * 
	 * @param modelUrl
	 * @param outputUrl
	 * @param newDataList
	 * @throws Exception
	 */
	public void exportExcel(String modelUrl, String outputUrl, List<List<String>> newDataList) throws Exception {
		// 用模板文件创建POI
		POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(modelUrl));
		// 创建模板工作表
		HSSFWorkbook templatewb = new HSSFWorkbook(fs);
		// 直接取模板第一个sheet对象
		HSSFSheet templateSheet = templatewb.getSheetAt(0);
		// 得到模板的第一个sheet的第一行，为得到模板样式
		HSSFRow templateRow = templateSheet.getRow(0);

		// 取得模板Excel的总列数
		int columns = templateSheet.getRow((short) 0).getPhysicalNumberOfCells();
		// 创建样式数组
		HSSFCellStyle styleArray[] = new HSSFCellStyle[columns];
		for (int s = 0; s < columns; s++) {
			// 给样式数组赋值
			styleArray[s] = templatewb.createCellStyle();
		}

		// 双循环，为每个cell赋值
		List<String> dataRow = null;
		HSSFRow newRow = null;
		HSSFCellStyle style = null;
		HSSFCell templateCell = null;
		HSSFCell newCell = null;
		for (int i = 1; i <= newDataList.size(); i++) { // 行
			// 获取每行的数据集合
			dataRow = newDataList.get(i - 1);
			// 创建新的row
			newRow = templateSheet.createRow(i);

			for (int j = 0; j < columns; j++) { // 列
				// Excel中某一列的样式
				style = styleArray[j];
				// 第一行的某一列的单元格对象
				templateCell = templateRow.getCell(j);

				// 如果对应的模板单元格 样式为非锁定
				if (templateCell.getCellStyle().getLocked() == false) {
					style.setLocked(false);
				} else {
					style.setLocked(true);
				}

				// 创建新的cell
				newCell = newRow.createCell(j);
				newCell.setCellStyle(style);

				// 为Cell赋值，默认都为String
				newCell.setCellValue(dataRow.get(j));
			}
		}

		// 设置输入流
		FileOutputStream fout = new FileOutputStream(outputUrl);
		// 输出文件
		templatewb.write(fout);
		fout.flush();
		fout.close();
	}

	/**
	 * CSV寫入数据
	 * 
	 * @author WHS
	 * @throws Exception
	 * 
	 *             public void writeCSV (String outputUrl, List<String[]>
	 *             newDataList) throws Exception{ try { File file = new
	 *             File(outputUrl); Writer writer = new FileWriter(file);
	 *             CSVWriter csvWriter = new CSVWriter(writer, ','); //String[]
	 *             strs = {"abc" , "abc" , "abc"};
	 *             csvWriter.writeAll(newDataList); csvWriter.close(); } catch
	 *             (Exception e) { e.printStackTrace(); } }
	 */

	private static String getCellValue(Cell cell) {
		String cellValue = "";
		// DataFormatter formatter = new DataFormatter();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (cell != null) {
			switch (cell.getCellType()) {
			case Cell.CELL_TYPE_NUMERIC:
				if (DateUtil.isCellDateFormatted(cell)) {
					// cellValue = formatter.formatCellValue(cell);
					// cellValue = String.valueOf(cell.getDateCellValue());
					cellValue = formatter.format(cell.getDateCellValue());
				} else {
					double value = cell.getNumericCellValue();
					long intValue = (long) value;
					cellValue = value - intValue == 0 ? String.valueOf(intValue) : String.valueOf(value);
				}
				break;
			case Cell.CELL_TYPE_STRING:
				cellValue = cell.getStringCellValue();
				break;
			case Cell.CELL_TYPE_BOOLEAN:
				cellValue = String.valueOf(cell.getBooleanCellValue());
				break;
			case Cell.CELL_TYPE_FORMULA:
				cellValue = String.valueOf(cell.getCellFormula());
				break;
			case Cell.CELL_TYPE_BLANK:
				cellValue = "";
				break;
			case Cell.CELL_TYPE_ERROR:
				cellValue = "";
				break;
			default:
				cellValue = cell.toString().trim();
				break;
			}
		}
		return cellValue.trim();
	}

}
