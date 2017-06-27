package com.zd.school.app.salary;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zd.core.constant.Constant;
import com.zd.core.controller.core.FrameWorkController;
import com.zd.school.salary.salary.model.XcSalarybook;
import com.zd.school.salary.salary.model.XcSalarybookitem;
import com.zd.school.salary.salary.model.XcSalarybooksalary;
import com.zd.school.salary.salary.model.XcSalaryitem;
import com.zd.school.salary.salary.model.XcSalaryplatitem;
import com.zd.school.salary.salary.service.XcSalarybookService;
import com.zd.school.salary.salary.service.XcSalarybookitemService;
import com.zd.school.salary.salary.service.XcSalarybooksalaryService;
import com.zd.school.salary.salary.service.XcSalaryitemService;
import com.zd.school.salary.salary.service.XcSalaryplatitemService;

@Controller
@RequestMapping("/app/salary")
public class SalaryBookAppController extends FrameWorkController<XcSalarybook> implements Constant {

	@Resource
	private XcSalarybookitemService thisService;
	@Resource
	private XcSalarybookService bookService;
	@Resource
	private XcSalaryplatitemService plartitemService;
	@Resource
	private XcSalaryitemService salaryitemService;
	@Resource
	private XcSalarybooksalaryService booksalaryService;

	// 获取指定教师的工资明细
	@RequestMapping(value = { "/getTeacherSalaryBook" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public @ResponseBody List<XcSalaryplatitem> getTeacherSalaryBook(HttpServletRequest request,
			HttpServletResponse response, @RequestParam("uuid") String uuid) {
		XcSalarybookitem item = thisService.get(uuid);
		XcSalarybook book = bookService.getByProerties("uuid", item.getSalarybookId());
		String hql = "from XcSalarybooksalary where salarybookId='" + book.getUuid() + "' order by orderIndex";
		List<XcSalarybooksalary> list = booksalaryService.doQuery(hql);
		Map<String, Integer> map = new LinkedHashMap<String, Integer>();
		for (XcSalarybooksalary temp : list) {
			map.put(temp.getSalaryitemName(), temp.getOrderIndex() - 1);
		}

		StringBuffer sql = new StringBuffer();
		sql.append("select ");
		for (String key : map.keySet()) {
			Integer val = map.get(key);
			sql.append(" gz" + val + ",");
		}
		sql = new StringBuffer(sql.substring(0, sql.length() - 1));
		sql.append(" ,'' from XC_T_SALARYBOOKITEM where SALARYBOOKITEM_ID='" + item.getUuid() + "'");
		List<Object[]> objects = thisService.ObjectQuerySql(sql.toString());
		Object[] obj = objects.get(0);
		int index = 0;
		List<XcSalaryplatitem> returnList = new LinkedList<XcSalaryplatitem>();
		for (String key : map.keySet()) {
			XcSalaryplatitem temp = new XcSalaryplatitem();
			temp.setExtField01(key);
			temp.setExtField02(obj[index++] + "");
			String salaryitemType = getSalaryitemType(key);
			temp.setExtField03(salaryitemType);
			returnList.add(temp);
		}
		return returnList;
	}

	@RequestMapping(value = { "/getNameAndBankNum" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public @ResponseBody XcSalarybookitem getNameAndBankNum(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("userId") String userId, @RequestParam("xcYear") Integer xcYear,
			@RequestParam("xcMonth") Integer xcMonth) {
		String[] param = { "userId", "xcYear", "xcMonth" };
		Object[] values = { userId, xcYear, xcMonth };
		XcSalarybookitem item = thisService.getByProerties(param, values);
		return item;
	}

	@RequestMapping(value = { "/getXcSalarybookitemList" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public @ResponseBody List<XcSalarybookitem> getXcSalarybookitemList(HttpServletRequest request,
			HttpServletResponse response, @RequestParam("userId") String userId, @RequestParam("xcYear") Integer xcYear,
			@RequestParam("xcMonth") Integer xcMonth) {
		String hql;
		hql = "from XcSalarybook where xcYear=" + xcYear + " and xcMonth=" + xcMonth + " and isDelete=0 and isPushed=1";
		List<XcSalarybook> list = bookService.doQuery(hql);
		String bookIds = "";
		for (XcSalarybook xcSalarybook : list) {
			bookIds += "'" + xcSalarybook.getUuid() + "',";
		}
		bookIds = bookIds.substring(0, bookIds.length() - 1);
		hql = "from XcSalarybookitem where userId='" + userId + "' and xcYear=" + xcYear + " and xcMonth=" + xcMonth
				+ " and isDelete=0 and salarybookId in("+bookIds+")";
		List<XcSalarybookitem> items = thisService.doQuery(hql);
		return items;
	}

	private String getSalaryitemType(String salaryitemName) {
		XcSalaryitem item = salaryitemService.getByProerties("salaryitemName", salaryitemName);
		return item.getSalaryitemTypeName();
	}

}
