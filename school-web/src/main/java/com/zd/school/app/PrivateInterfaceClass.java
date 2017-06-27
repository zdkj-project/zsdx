
package com.zd.school.app;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zd.core.constant.Constant;
import com.zd.core.controller.core.FrameWorkController;
import com.zd.core.util.DBContextHolder;
import com.zd.school.build.allot.model.DormStudentDorm;
import com.zd.school.build.allot.service.DormStudentdormService;

/**
 * 自定义私有接口类
 * 
 * @author hucy_
 *
 */
@Controller
@RequestMapping("/app/PrivateInterfaceClass")
public class PrivateInterfaceClass extends FrameWorkController<DormStudentDorm> implements Constant {

	@Resource
	DormStudentdormService thisService; // service层接口

	/**
	 * 已入住宿舍学生列表 @Title: list @Description: TODO @param @param entity
	 * 实体类 @param @param request @param @param response @param @throws
	 * IOException 设定参数 @return void 返回类型 @throws
	 */
	@RequestMapping(value = { "/getStuIdbyfactoryFixID" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public void getStuIdbyfactoryFixID(String factoryFixID, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		StringBuffer returnData = new StringBuffer();
		String url = "http://ykt.sdsyfz.edu.cn/static/core/coreApp/miniweb/indexPad.jsp";
		String status = "0"; // 状态：0有效，1无效
		Integer fixID = Integer.parseUnsignedInt(factoryFixID);
		try {
			//DBContextHolder.setDBType(DBContextHolder.DATA_SOURCE_Three);
			List list = thisService
					.doQuerySql("SELECT EmployeeID FROM TC_Card " + "WHERE FactoryFixID='" + fixID + "' AND CardStatusIDXF=1 Order By CreateDate DESC");
			if (list.size() <= 0) {
				status = "1";
				returnData.append("{\"status\" : " + "\""+status+"\", \"msg\":" + "\"卡无效\"" + "}");
				writeAppJSON(response, returnData.toString());
			}else{
				String employeeID = list.get(0).toString();
				List stuId = thisService
						.doQuerySql("SELECT Student_id FROM TC_Employee " + "WHERE EmployeeID='" + employeeID + "'");
				if (stuId.size() <= 0) {
					if (stuId.get(0) == null)
						status = "1";
					returnData.append("{\"status\" : " + "\""+status+"\", \"msg\":" + "\"无此人员信息\"" + "}");
					writeAppJSON(response, returnData.toString());
				}
				returnData.append("{\"msg\":\"成功。\",\"status\" : " + "\""+status+"\", \"stuid\":" + "\""+stuId.get(0)+"\",\"url\":" + "\""+url+"\"}");
			}
		} finally {
			DBContextHolder.clearDBType();
		}
		writeAppJSON(response, returnData.toString());
	}
}
