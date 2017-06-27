package com.zd.school.jw.schoolcourse.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zd.core.constant.Constant;
import com.zd.core.controller.core.FrameWorkController;
import com.zd.core.model.extjs.QueryResult;
import com.zd.core.util.DateUtil;
import com.zd.school.jw.schoolcourse.model.JwCoursepublish;
import com.zd.school.jw.schoolcourse.model.JwStuEnteredCourse;
import com.zd.school.jw.schoolcourse.service.JwCoursepublishService;
import com.zd.school.jw.schoolcourse.service.JwStuEnteredCourseService;

@Controller
@RequestMapping("/JwStuEnteredCourse")
public class JwStuEnteredCourseController extends FrameWorkController<JwStuEnteredCourse> implements Constant {

	@Resource
	JwStuEnteredCourseService thisService; // service层接口

	@Resource
	JwCoursepublishService publishService;

	@RequestMapping(value = { "/list" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public void list(@ModelAttribute JwStuEnteredCourse entity, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String strData = ""; // 返回给js的数据
		Integer start = super.start(request);
		Integer limit = super.limit(request);
		String hql = "from JwStuEnteredCourse where 1=1 ";
		String whereSql = super.whereSql(request);
		hql += whereSql;
		QueryResult<JwStuEnteredCourse> qResult = thisService.doQueryResult(hql, start, limit);
		strData = jsonBuilder.buildObjListToJson(qResult.getTotalCount(), qResult.getResultList(), true);// 处理数据
		writeJSON(response, strData);// 返回数据
	}

	@RequestMapping(value = { "/exportToExcel" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public void exportToExcel(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String publishId = request.getParameter("publishId");
		String sql = "SELECT a.COURSE_NAME FROM JW_T_SCHOOLCOURSE a WHERE a.SCHOOLCOURSE_ID in(SELECT b.COURSE_ID FROM JW_T_PUBLISHCOURSE b WHERE b.PUBLISH_ID='"
				+ publishId + "')";
		sql += " group by a.COURSE_NAME";
		List<Object[]> courseNames = thisService.ObjectQuerySql(sql);
		JwCoursepublish puhlish = publishService.get(publishId);
		String fileName = puhlish.getStudyYeahname() + puhlish.getSemester() + "报名列表";

		OutputStream fileOutputStream = null;
		response.reset();// 清空输出流
		response.setHeader("Content-disposition",
				"attachment; filename=" + new String((fileName + ".xls").getBytes("GB2312"), "ISO8859-1"));
		response.setContentType("application/msexcel");
		HSSFWorkbook workbook = new HSSFWorkbook();
		Sheet sheet = workbook.createSheet(fileName);

		int rowIndex = 0;

		CellStyle style = workbook.createCellStyle();
		HSSFFont font = workbook.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 粗体显示
		style.setFont(font);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);

		for (int i = 0; i < courseNames.size(); i++) {
			String courseName = courseNames.get(i) + "";
			String hql = "from JwStuEnteredCourse  where 1=1 and publishId='" + publishId + "' and courseName='"
					+ courseName + "'";

			List<JwStuEnteredCourse> list = thisService.doQuery(hql);
			if (list.size() > 0) {
				Row titleRow = sheet.createRow(rowIndex);
				Cell titleCell = titleRow.createCell(0);
				CellRangeAddress cra = new CellRangeAddress(rowIndex, rowIndex, 0, 1);
				sheet.addMergedRegion(cra);
				titleCell.setCellStyle(style);
				titleCell.setCellValue("课程名称:"+courseName);
				
				rowIndex++;
				titleRow = sheet.createRow(rowIndex);
				titleCell = titleRow.createCell(0);
				titleCell.setCellValue("姓名");
				titleCell = titleRow.createCell(1);
				titleCell.setCellValue("报名时间");
				
				for (JwStuEnteredCourse jwStuEnteredCourse : list) {
					rowIndex++;
					Row valueRow = sheet.createRow(rowIndex);
					Cell valueCell = valueRow.createCell(0);
					valueCell.setCellValue(jwStuEnteredCourse.getStudentName());
					valueCell = valueRow.createCell(1);
					valueCell.setCellValue(DateUtil.formatDateTime(jwStuEnteredCourse.getCreateTime()));
				}
				rowIndex++;
			}

		}

		sheet.autoSizeColumn(0, false);
		sheet.autoSizeColumn(1, false);
		fileOutputStream = response.getOutputStream();
		workbook.write(fileOutputStream);

	}

}
