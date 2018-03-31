package com.zd.school.app.salary;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zd.school.salary.jxsalary.model.XcJxbook;
import com.zd.school.salary.jxsalary.model.XcJxbookitem;
import com.zd.school.salary.jxsalary.model.XcJxplartitem;
import com.zd.school.salary.jxsalary.service.XcJxbookService;
import com.zd.school.salary.jxsalary.service.XcJxbookitemService;
import com.zd.school.salary.jxsalary.service.XcJxplartitemService;

@Controller
@RequestMapping("/app/XcJxbookitem")
public class XcJxbookitemAppController {

	@Resource
	private XcJxbookitemService thisService;
	@Resource
	private XcJxbookService bookService;
	@Resource
	private XcJxplartitemService plartitemService;

	@RequestMapping(value = { "/jxDetail" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public @ResponseBody Map<String, Object> list(HttpServletRequest request, HttpServletResponse response) {
		String userId = request.getParameter("userId");
		String xcYear = request.getParameter("xcYear");
		String xcMonth = request.getParameter("xcMonth");
		String jxbookId = request.getParameter("jxbookId");
		String hql = "from XcJxbookitem where userId='" + userId + "' and xcYear=" + xcYear + " and xcMonth=" + xcMonth
				+ " and isDelete=0 and jxbookId='" + jxbookId + "'";
		XcJxbookitem item = thisService.getQuery(hql).get(0);
		XcJxbook book = bookService.getByProerties("uuid", item.getJxbookId());
		hql = "from XcJxplartitem where jxplartId='" + book.getJxplartId() + "' order by orderIndex";
		List<XcJxplartitem> list = plartitemService.getQuery(hql);
		Map<String, Integer> map = new LinkedHashMap<String, Integer>();
		for (XcJxplartitem temp : list) {
			map.put(temp.getSalaryitemName(), temp.getOrderIndex() - 1);
		}

		StringBuffer sql = new StringBuffer();
		sql.append("select ");
		for (String key : map.keySet()) {
			Integer val = map.get(key);
			sql.append(" gz" + val + ",");
		}
		sql = new StringBuffer(sql.substring(0, sql.length() - 1));
		sql.append(" from XC_T_JXBOOKITEM where JXSALARY_ID='" + item.getUuid() + "'");
		List<Object[]> objects = thisService.ObjectQuerySql(sql.toString());
		Object[] obj = objects.get(0);
		int index = 0;
		Map<String, Object> returnMap = new LinkedHashMap<String, Object>();
		for (String key : map.keySet()) {
			returnMap.put(key, obj[index++]);
		}
		return returnMap;
	}

}
