
package com.zd.school.salary.jxsalary.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.zd.core.constant.Constant;
import com.zd.core.controller.core.FrameWorkController;
import com.zd.core.model.extjs.QueryResult;
import com.zd.core.util.BeanUtils;
import com.zd.core.util.JsonBuilder;
import com.zd.core.util.StringUtils;
import com.zd.school.jw.controller.util.ExcelUtil;
import com.zd.school.plartform.comm.model.CommTree;
import com.zd.school.plartform.system.model.SysUser;
import com.zd.school.plartform.system.service.SysUserService;
import com.zd.school.salary.jxsalary.model.XcJxbook ;
import com.zd.school.salary.jxsalary.model.XcJxbookitem;
import com.zd.school.salary.jxsalary.model.XcJxbooksalary;
import com.zd.school.salary.jxsalary.model.XcJxplartitem;
import com.zd.school.salary.jxsalary.service.XcJxbookService ;
import com.zd.school.salary.jxsalary.service.XcJxbookitemService;
import com.zd.school.salary.jxsalary.service.XcJxbooksalaryService;
import com.zd.school.salary.jxsalary.service.XcJxplartitemService;
import com.zd.school.salary.salary.model.XcSalarybook;
import com.zd.school.salary.salary.model.XcSalaryplatitem;

/**
 * 
 * ClassName: XcJxbookController
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 绩效工资台账表(XC_T_JXBOOK)实体Controller.
 * date: 2016-11-29
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Controller
@RequestMapping("/Xcjxbook")
public class XcJxbookController extends FrameWorkController<XcJxbook> implements Constant {

    @Resource
    private XcJxbookService thisService; // service层接口
    @Resource
	private XcJxbooksalaryService booksalaryService;
    @Resource
	private SysUserService userService;
    @Resource
	private XcJxbookitemService bookitemService;
    @Resource
	private XcJxplartitemService plartitemService;

    
    @RequestMapping("/getSalaryItems")
	public @ResponseBody List<XcJxbooksalary> getSalaryItems(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String jxbookId = request.getParameter("jxbookId");
		// XcSalarybook book = thisService.get(jxplartId);
		String hql = "from XcJxbooksalary where jxbookId='" + jxbookId + "' order by orderIndex";
		List<XcJxbooksalary> list = booksalaryService.doQuery(hql);
		List<XcJxbooksalary> list2 = new ArrayList<XcJxbooksalary>();
		for (XcJxbooksalary temp : list) {
			XcJxbooksalary entity = new XcJxbooksalary();
			entity.setExtField01(temp.getSalaryitemName());
			entity.setExtField02("gz" + (temp.getOrderIndex() - 1));
			list2.add(entity);
		}
		return list2;
	}
    
    @RequestMapping("/importSalary")
	public void importSalary(@RequestParam MultipartFile file, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		if (!file.isEmpty()) {
			String jxbookId = request.getParameter("jxbookId");
			XcJxbook book = thisService.get(jxbookId);
			String hql = "from XcJxbooksalary where jxbookId='" + jxbookId + "' order by orderIndex";
			List<XcJxbooksalary> list = booksalaryService.doQuery(hql);
			Map<String, Integer> map = new LinkedHashMap<String, Integer>();
			// 储存每一项工资的序号 例 职务工资 1
			for (XcJxbooksalary temp : list) {
				map.put(temp.getSalaryitemName(), temp.getOrderIndex());
			}
			Integer xcYear = book.getXcYear();
			Integer xcMonth = book.getXcMonth();
			SysUser currentUser = getCurrentSysUser();
			String userCh = currentUser.getXm();
			ExcelUtil excelUtil = new ExcelUtil();
			List<List<String>> salaryData = excelUtil.importExcel(file);
			List<String> salaryList = null;
			XcJxbookitem entity = null;
			for (int i = 0; i < salaryData.size(); i++) {
				salaryList = salaryData.get(i);
				entity = new XcJxbookitem();

				// 设置基本属性
				String xm = salaryList.get(0);
				entity.setXm(xm);
				hql = "from SysUser where xm='" + xm + "' and isDelete=0";	
				List<SysUser> users = userService.doQuery(hql);
				String userId;
				if (users != null && users.size() > 0) {
					userId = users.get(0).getUuid();
				} else {
					userId = "";
				}
				entity.setUserId(userId);
				entity.setXcYear(xcYear);
				entity.setXcMonth(xcMonth);
				entity.setRemark(salaryList.get(salaryList.size() - 2));
				entity.setBankNumb(salaryList.get(salaryList.size() - 1));
				entity.setJxbookId(jxbookId);
				entity.setCreateUser(userCh);

				/**
				 * 反射 设置工资 因为职务工资在gz0 所以取得下标减1
				 */
				Class clazz = entity.getClass();
				for (String key : map.keySet()) {
					Integer index = map.get(key);
					String methodName = "setGz" + (index - 1);
					Method method = clazz.getDeclaredMethod(methodName, BigDecimal.class);
					BigDecimal gz;
					try {
						gz = new BigDecimal(salaryList.get(index));
					} catch (Exception e) {
						gz = null;
					}
					method.invoke(entity, gz);
				}
				bookitemService.merge(entity);
			}
			writeJSON(response, jsonBuilder.returnSuccessJson(jsonBuilder.toJson("'工资数据导入成功'")));
		} else {
			writeJSON(response, jsonBuilder.returnFailureJson(jsonBuilder.toJson("'工资数据导入失败'")));
		}
	}
    
    @RequestMapping(value = { "/getByjxbookId" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public @ResponseBody XcJxbook getByjxbookId(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String jxbookId = request.getParameter("jxbookId");
		XcJxbook entity = thisService.get(jxbookId);
		return entity;
	}

    /**
      * list查询
      * @Title: list
      * @Description: TODO
      * @param @param entity 实体类
      * @param @param request
      * @param @param response
      * @param @throws IOException    设定参数
      * @return void    返回类型
      * @throws
     */
    @RequestMapping(value = { "/list" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
            org.springframework.web.bind.annotation.RequestMethod.POST })
    public void list(@ModelAttribute XcJxbook entity, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String strData = ""; // 返回给js的数据
        SysUser currentUser = getCurrentSysUser();
		String whereSql = super.whereSql(request);
		String orderSql = super.orderSql(request);
		Integer start = super.start(request);
		Integer limit = super.limit(request);
		String sort = super.sort(request);
		String filter = super.filter(request);
        QueryResult<XcJxbook> qResult = thisService.list(start, limit, sort, filter, whereSql, orderSql, currentUser);
        strData = jsonBuilder.buildObjListToJson(qResult.getTotalCount(), qResult.getResultList(), true);// 处理数据
        writeJSON(response, strData);// 返回数据
    }

    /**
     * 
      * @Title: 增加新实体信息至数据库
      * @Description: TODO
      * @param @param XcJxbook 实体类
      * @param @param request
      * @param @param response
      * @param @throws IOException    设定参数
      * @return void    返回类型
      * @throws
     */
    @RequestMapping("/doadd")
    public void doAdd(XcJxbook entity, HttpServletRequest request, HttpServletResponse response)
            throws IOException, IllegalAccessException, InvocationTargetException {
        
		//此处为放在入库前的一些检查的代码，如唯一校验等
		
		//获取当前操作用户
		String userCh = "超级管理员";
        SysUser currentUser = getCurrentSysUser();
        if (currentUser != null)
            userCh = currentUser.getXm();

        XcJxbook perEntity = new XcJxbook();
		BeanUtils.copyPropertiesExceptNull(entity, perEntity);
		// 生成默认的orderindex
		//如果界面有了排序号的输入，则不需要取默认的了
        //Integer orderIndex = thisService.getDefaultOrderIndex(entity);
		//entity.setOrderIndex(orderIndex);//排序
		
		//增加时要设置创建人
        entity.setCreateUser(userCh); //创建人
        		
		//持久化到数据库
		entity = thisService.merge(entity);
		
		List<XcJxplartitem> list = plartitemService.queryByProerties("jxplartId", entity.getJxplartId());
		for (XcJxplartitem xcSalaryplatitem : list) {
			XcJxbooksalary temp = new XcJxbooksalary();
			temp.setJxbookId(entity.getUuid());
			temp.setJxplartId(entity.getJxplartId());
			temp.setSalaryitemId(xcSalaryplatitem.getUuid());
			temp.setSalaryitemName(xcSalaryplatitem.getSalaryitemName());
			temp.setOrderIndex(xcSalaryplatitem.getOrderIndex());
			booksalaryService.merge(temp);
		}
		
		//返回实体到前端界面
        writeJSON(response, jsonBuilder.returnSuccessJson(jsonBuilder.toJson(entity)));
    }

    /**
      * doDelete
      * @Title: 逻辑删除指定的数据
      * @Description: TODO
      * @param @param request
      * @param @param response
      * @param @throws IOException    设定参数
      * @return void    返回类型
      * @throws
     */
    @RequestMapping("/dodelete")
    public void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String delIds = request.getParameter("ids");
        if (StringUtils.isEmpty(delIds)) {
            writeJSON(response, jsonBuilder.returnSuccessJson("'没有传入删除主键'"));
            return;
        } else {
        	XcJxbook entity = thisService.get(delIds);
			if (entity.getIsPushed()==1) {
				writeJSON(response, jsonBuilder.returnFailureJson("'当前台帐已有数据不能删除'"));
			} else {
				booksalaryService.deleteByProperties("jxbookId", delIds);
				bookitemService.deleteByProperties("jxbookId", delIds);
				boolean flag = thisService.deleteByPK(delIds);
				if (flag) {
					writeJSON(response, jsonBuilder.returnSuccessJson("'删除成功'"));
				} else {
					writeJSON(response, jsonBuilder.returnFailureJson("'删除失败'"));
				}
			}
        }
    }

    /**
     * doUpdate编辑记录
     * @Title: doUpdate
     * @Description: TODO
     * @param @param XcJxbook
     * @param @param request
     * @param @param response
     * @param @throws IOException    设定参数
     * @return void    返回类型
     * @throws
    */
    @RequestMapping("/doupdate")
    public void doUpdates(XcJxbook entity, HttpServletRequest request, HttpServletResponse response)
            throws IOException, IllegalAccessException, InvocationTargetException {
		
		//入库前检查代码
		
		//获取当前的操作用户
        String userCh = "超级管理员";
        SysUser currentUser = getCurrentSysUser();
        if (currentUser != null)
            userCh = currentUser.getXm();
			
			
        //先拿到已持久化的实体
		//entity.getSchoolId()要自己修改成对应的获取主键的方法
        XcJxbook perEntity = thisService.get(entity.getUuid());

        //将entity中不为空的字段动态加入到perEntity中去。
        BeanUtils.copyPropertiesExceptNull(perEntity,entity);
       
        perEntity.setUpdateTime(new Date()); //设置修改时间
        perEntity.setUpdateUser(userCh); //设置修改人的中文名
        entity = thisService.merge(perEntity);//执行修改方法

        writeJSON(response, jsonBuilder.returnSuccessJson(jsonBuilder.toJson(perEntity)));

    }
    
	@RequestMapping("/doPush")
	public void doPush( HttpServletRequest request, HttpServletResponse response)
			throws IOException, IllegalAccessException, InvocationTargetException {
		String delIds = request.getParameter("ids");
		
		// 入库前检查代码

		// 获取当前的操作用户
		String userCh = "超级管理员";
		SysUser currentUser = getCurrentSysUser();
		if (currentUser != null)
			userCh = currentUser.getXm();

		// 先拿到已持久化的实体
		// entity.getSchoolId()要自己修改成对应的获取主键的方法
		XcJxbook perEntity = thisService.get(delIds);

	
		perEntity.setIsPushed(1);

		perEntity.setUpdateTime(new Date()); // 设置修改时间
		perEntity.setUpdateUser(userCh); // 设置修改人的中文名
		perEntity = thisService.merge(perEntity);// 执行修改方法
		writeJSON(response, jsonBuilder.returnSuccessJson("'发布成功'"));
		

	}
    
    /**
     * 获取绩效工资台账树
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping("/getBookTree")
    public void getBookTree(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String strData = "";
        String whereSql = request.getParameter("whereSql");
        SysUser currentUser = getCurrentSysUser();

        //List<CommTree> lists = treeSerice.getCommTree("JW_V_GRADECLASSTREE", whereSql);
        List<CommTree> lists = thisService.getBookTree("XC_V_JXBOOKTREE", whereSql, currentUser);

        strData = JsonBuilder.getInstance().buildList(lists, "");// 处理数据
        writeJSON(response, strData);// 返回数据
    }
}
