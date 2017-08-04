package com.zd.school.jw.arrangecourse.controller;



import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.zd.core.constant.Constant;
import com.zd.core.constant.StatuVeriable;
import com.zd.core.controller.core.FrameWorkController;
import com.zd.core.util.StringUtils;
import com.zd.core.util.readTxt;
import com.zd.school.jw.arrangecourse.model.JwCourseArrange;
import com.zd.school.jw.arrangecourse.model.JwCourseteacher;
import com.zd.school.jw.arrangecourse.model.JwXyz;
import com.zd.school.jw.arrangecourse.service.JwCourseArrangeService;
import com.zd.school.jw.arrangecourse.service.JwCourseteacherService;
import com.zd.school.jw.eduresources.model.JwTBasecourse;
import com.zd.school.jw.eduresources.model.JwTGradeclass;
import com.zd.school.jw.eduresources.service.JwTBasecourseService;
import com.zd.school.jw.eduresources.service.JwTGradeclassService;
import com.zd.school.push.model.PushInfo;
import com.zd.school.push.service.PushInfoService;
import com.zd.school.plartform.system.model.SysUser;
import com.zd.school.plartform.system.service.SysUserService;
import com.zd.school.teacher.teacherinfo.model.TeaTeacherbase;
import com.zd.school.teacher.teacherinfo.service.TeaTeacherbaseService;

import jxl.Workbook;
import jxl.write.WritableWorkbook;

/**
 * 
 * ClassName: JwTCourseArrangeController
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 排课课程表实体Controller.
 * date: 2016-03-24
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Controller
@RequestMapping("/JwTCourseArrange")
public class JwTCourseArrangeController extends FrameWorkController<JwCourseArrange> implements Constant {

	 	@Resource
	    JwCourseArrangeService thisService; // service层接口。。。
	    
	    @Resource
	    JwTGradeclassService classService;
	    
	    @Resource
	    PushInfoService pushService;
	    
	    @Resource
	    private JwTGradeclassService jwClassService;
	    
	    @Resource
	    private JwTBasecourseService jtbService;
	    
	    @Resource
	    private JwCourseteacherService courseTeacherService;

	    @Resource
	    private TeaTeacherbaseService teacherService;
	    
	    @Resource
	    private SysUserService userService;
    
    


	/**
     * @return 
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
    public @ResponseBody List<JwCourseArrange> list(@ModelAttribute JwCourseArrange entity,String cname,String isdelete, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
    	String strData = ""; // 返回给js的数据
        // hql语句
        StringBuffer hql = new StringBuffer("from " + entity.getClass().getSimpleName() + " where className='"+cname+"' and isDelete="+Integer.parseInt(isdelete)+" order by className,teachTime asc");
        List<JwCourseArrange> lists = thisService.doQuery(hql.toString());// 执行查询方法
        List<JwCourseArrange> newlists=new ArrayList<JwCourseArrange>();
        for(JwCourseArrange jca:lists){
        	jca.setWeekOne(jca.getCourseName01()+"("+jca.getTeacherName01()+")");
        	jca.setWeekTwo(jca.getCourseName02()+"("+jca.getTeacherName02()+")");
        	jca.setWeekThree(jca.getCourseName03()+"("+jca.getTeacherName03()+")");
        	jca.setWeekFour(jca.getCourseName04()+"("+jca.getTeacherName04()+")");
        	jca.setWeekFive(jca.getCourseName05()+"("+jca.getTeacherName05()+")");
        	jca.setWeekSix(jca.getCourseName06()+"("+jca.getTeacherName06()+")");
        	jca.setWeekSeven(jca.getCourseName07()+"("+jca.getTeacherName07()+")");
        	newlists.add(jca);
        }
        /*Integer count = thisService.getCount(countHql.toString());// 查询总记录数
        strData = jsonBuilder.buildObjListToJson(new Long(count), newlists, true);// 处理数据
        writeJSON(response, strData);// 返回数据
*/        return newlists;
    }
    
    @RequestMapping(value = { "/classlist" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
            org.springframework.web.bind.annotation.RequestMethod.POST })
    public @ResponseBody List<JwTGradeclass> classlist(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        // hql语句
    	String hql="from JwTGradeclass";
        List<JwTGradeclass> list = classService.doQuery(hql);// 执行查询方法
        return list;
    }


    
    /**
     * 
      * doAdd
      * @Title: doAdd
      * @Description: TODO
      * @param @param JwTCourseArrange 实体类
      * @param @param request
      * @param @param response
      * @param @throws IOException    设定参数
      * @return void    返回类型
      * @throws
     */
    @RequestMapping("/doadd")
    public void doAdd(JwCourseArrange entity, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        
		//此处为放在入库前的一些检查的代码，如唯一校验等
		
		//获取当前操作用户
		String userCh = "超级管理员";
        SysUser currentUser = getCurrentSysUser();
        if (currentUser != null)
            userCh = currentUser.getXm();
        
		// 生成默认的orderindex
		//如果界面有了排序号的输入，则不需要取默认的了
        Integer orderIndex = thisService.getDefaultOrderIndex(entity);
		entity.setOrderIndex(orderIndex);//排序
		
		//增加时要设置创建人
        entity.setCreateUser(userCh); //创建人
        		
		//持久化到数据库
		entity = thisService.merge(entity);
		
		//返回实体到前端界面
        writeJSON(response, jsonBuilder.returnSuccessJson(jsonBuilder.toJson(entity)));
    }

    /**
      * doDelete
      * @Title: doDelete
      * @Description: TODO
      * @param @param request
      * @param @param response
      * @param @throws IOException    设定参数
      * @return void    返回类型
      * @throws
     */
    @RequestMapping("/delete")
    public void Delete(String cname,String isdelete,HttpServletRequest request, HttpServletResponse response) throws IOException {
        String sql="delete JW_T_COURSE_ARRANGE where ISDELETE="+isdelete+" and CLASS_NAME='"+cname+"'";
        thisService.executeSql(sql);

    }

    /**
      * doRestore还原删除的记录
      * @Title: doRestore
      * @Description: TODO
      * @param @param request
      * @param @param response
      * @param @throws IOException    设定参数
      * @return void    返回类型
      * @throws
     */
    @RequestMapping("/dorestore")
    public void doRestore(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String delIds = request.getParameter("ids");
        if (StringUtils.isEmpty(delIds)) {
            writeJSON(response, jsonBuilder.returnSuccessJson("'没有传入还原主键'"));
            return;
        } else {
            boolean flag = thisService.logicDelOrRestore(delIds, StatuVeriable.ISNOTDELETE);
            if (flag) {
                writeJSON(response, jsonBuilder.returnSuccessJson("'还原成功'"));
            } else {
                writeJSON(response, jsonBuilder.returnFailureJson("'还原失败'"));
            }
        }
    }
    
    @RequestMapping(value = { "/doPayRecordReport" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public void doPayRecordReport( HttpServletRequest request, HttpServletResponse response)
			throws IOException, ParseException {
    	//创建HSSFWorkbook对象(excel的文档对象)
        HSSFWorkbook wb = new HSSFWorkbook();
  //建立新的sheet对象（excel的表单）
  HSSFSheet sheet=wb.createSheet("成绩表");
  //在sheet里创建第一行，参数为行索引(excel的行)，可以是0～65535之间的任何一个
  HSSFRow row1=sheet.createRow(0);
  //创建单元格（excel的单元格，参数为列索引，可以是0～255之间的任何一个
  HSSFCell cell=row1.createCell(0);
        //设置单元格内容
  cell.setCellValue("学员考试成绩一览表");
  //合并单元格CellRangeAddress构造参数依次表示起始行，截至行，起始列， 截至列
  sheet.addMergedRegion(new CellRangeAddress(0,0,0,3));
  //在sheet里创建第二行
  HSSFRow row2=sheet.createRow(1);    
        //创建单元格并设置单元格内容
        row2.createCell(0).setCellValue("姓名");
        row2.createCell(1).setCellValue("班级");    
        row2.createCell(2).setCellValue("笔试成绩");
  row2.createCell(3).setCellValue("机试成绩");    
        //在sheet里创建第三行
        HSSFRow row3=sheet.createRow(2);
        row3.createCell(0).setCellValue("李明");
        row3.createCell(1).setCellValue("As178");
        row3.createCell(2).setCellValue(87);    
        row3.createCell(3).setCellValue(78);    
    //.....省略部分代码


  //输出Excel文件
      OutputStream output=response.getOutputStream();
      response.reset();
      response.setHeader("Content-disposition", "attachment; filename=details.xls");
      response.setContentType("application/msexcel");        
      File file=new File("D://temp.xls");
      WritableWorkbook wwb = Workbook.createWorkbook(file); 
	}
    
    

    /**
     * @throws ParseException 
     * doUpdate编辑记录
     * @Title: doUpdate
     * @Description: TODO
     * @param @param JwTCourseArrange
     * @param @param request
     * @param @param response
     * @param @throws IOException    设定参数
     * @return void    返回类型
     * @throws
    */
    @SuppressWarnings("restriction")
	@RequestMapping(value={"/doupdate"},method=RequestMethod.POST,produces="text/html;charset=UTF-8")
    public @ResponseBody String doUpdates(String classname,String startime,String endtime,String update1, String update2,String isdelete,HttpServletRequest request, HttpServletResponse response)
            throws IOException, IllegalAccessException, InvocationTargetException, ParseException {
    	
    	
    	//拆分第一条更新数据
    	String[] updatestr1=update1.split(",");
    	String id1=updatestr1[0];
    	String place1=updatestr1[1];
    	String cid1=updatestr1[2];
    	String cname1=updatestr1[3];
    	String tid1=updatestr1[4];
    	String tgh1=updatestr1[5];
    	String tname1=updatestr1[6];
    	String time1=updatestr1[7];

    	//拆分第二条更新数据
    	String[] updatestr2=update2.split(",");
    	String id2=updatestr2[0];
    	String place2=updatestr2[1];
    	String cid2=updatestr2[2];
    	String cname2=updatestr2[3];
    	String tid2=updatestr2[4];
    	String tgh2=updatestr2[5];
    	String tname2=updatestr2[6];
    	String time2=updatestr2[7];
    	
    	//设置查询是否可以调课的条件
    	String hql3="select count(*)  from  JwCourseArrange where (tteacId" + place1 + "='" + tid1+"' and teachTime='" + time1+"' and isDelete="+Integer.parseInt(isdelete)+")";
    	String hql4="select count(*)  from  JwCourseArrange where (tteacId" + place2 + "='" + tid2+"' and teachTime='" + time2+"' and isDelete="+Integer.parseInt(isdelete)+")";
    	
    	//更新交换记录HQL语句
    	String hql1="update JwCourseArrange set courseId" + place1 + "='" + cid1+"',courseName"+ place1 + "='" + cname1+"',tteacId"+ place1 + "='" + tid1+"',teacherGh"+ place1 + "='" + tgh1+"',teacherName"+ place1 + "='" + tname1+"' where uuid='"+id1+"'";
      	String hql2="update JwCourseArrange set courseId" + place2 + "='" + cid2+"',courseName"+ place2 + "='" + cname2+"',tteacId"+ place2 + "='" + tid2+"',teacherGh"+ place2 + "='" + tgh2+"',teacherName"+ place2 + "='" + tname2+"' where uuid='"+id2+"'";

    	//查询一个班级下节次是否有相同，有相同则更新，没有则可以新增
/*    	String hql9="select count(*)  from  JwTCourseArrange where  teachTime='" + time1+"'";
    	String hql10="select count(*)  from  JwTCourseArrange where teachTime='" + time2+"'";
    	Integer count3=thisService.getCount(hql9);
    	Integer count4=thisService.getCount(hql10);*/
    	

    	Integer count1=thisService.getCount(hql3);
    	Integer count2=thisService.getCount(hql4);
    	if(count1==0&&count2==0){
/*          thisService.executeHql(hql1);
        	thisService.executeHql(hql2);*/
        	//更新设置使调课前的不显示
/*    		if(count3<2&&count4<2){
	        	if(Integer.parseInt(startime2)<Integer.parseInt(systime2)){
	    	    	String hql1="update JwTCourseArrange set isDelete=1 where uuid='"+id1+"'";
	    	    	String hql2="update JwTCourseArrange set isDelete=1 where uuid='"+id2+"'";
	    	    	thisService.executeHql(hql1);
	    	    	thisService.executeHql(hql2);
	    	    	
	    	    	//根据修改课表的id重新插入两条记录
	    	    	String uuid1=UUID.randomUUID().toString();
	    	    	String uuid2=UUID.randomUUID().toString();
	    	    	String sql1="insert into  dbo.JW_T_COURSE_ARRANGE select '"+uuid1+"',SCHOOL_ID, SCHOOL_NAME, SCHOOL_YEAR, SCHOOL_TERM, CLAI_ID, CLASS_CODE, CLASS_NAME, TEACH_TIME, COURSE_ID01, COURSE_NAME01, TTEAC_ID01, TEACHER_GH01, TEACHER_NAME01, COURSE_ID02, COURSE_NAME02, TTEAC_ID02, TEACHER_GH02, TEACHER_NAME02, COURSE_ID03, COURSE_NAME03, TTEAC_ID03, TEACHER_GH03, TEACHER_NAME03, COURSE_ID04, COURSE_NAME04, TTEAC_ID04, TEACHER_GH04, TEACHER_NAME04, COURSE_ID05, COURSE_NAME05, TTEAC_ID05, TEACHER_GH05, TEACHER_NAME05, COURSE_ID06, COURSE_NAME06, TTEAC_ID06, TEACHER_GH06, TEACHER_NAME06, COURSE_ID07, COURSE_NAME07, TTEAC_ID07, TEACHER_GH07, TEACHER_NAME07, EXT_FIELD01, EXT_FIELD02, EXT_FIELD03, EXT_FIELD04, EXT_FIELD05, ORDER_INDEX, CREATE_TIME, CREATE_USER, UPDATE_TIME, UPDATE_USER, "+0+", VERSION, '"+startime+"', '"+endtime+"' from dbo.JW_T_COURSE_ARRANGE where uuid='"+id1+"'";
	    	    	String sql2="insert into  dbo.JW_T_COURSE_ARRANGE select '"+uuid2+"',SCHOOL_ID, SCHOOL_NAME, SCHOOL_YEAR, SCHOOL_TERM, CLAI_ID, CLASS_CODE, CLASS_NAME, TEACH_TIME, COURSE_ID01, COURSE_NAME01, TTEAC_ID01, TEACHER_GH01, TEACHER_NAME01, COURSE_ID02, COURSE_NAME02, TTEAC_ID02, TEACHER_GH02, TEACHER_NAME02, COURSE_ID03, COURSE_NAME03, TTEAC_ID03, TEACHER_GH03, TEACHER_NAME03, COURSE_ID04, COURSE_NAME04, TTEAC_ID04, TEACHER_GH04, TEACHER_NAME04, COURSE_ID05, COURSE_NAME05, TTEAC_ID05, TEACHER_GH05, TEACHER_NAME05, COURSE_ID06, COURSE_NAME06, TTEAC_ID06, TEACHER_GH06, TEACHER_NAME06, COURSE_ID07, COURSE_NAME07, TTEAC_ID07, TEACHER_GH07, TEACHER_NAME07, EXT_FIELD01, EXT_FIELD02, EXT_FIELD03, EXT_FIELD04, EXT_FIELD05, ORDER_INDEX, CREATE_TIME, CREATE_USER, UPDATE_TIME, UPDATE_USER, "+0+", VERSION, '"+startime+"', '"+endtime+"' from dbo.JW_T_COURSE_ARRANGE where uuid='"+id2+"'";
	    	    	thisService.executeSql(sql1);
	    	    	thisService.executeSql(sql2);
	    	    	//更新之后完成新的课表
	    	    	String hql7="update JwTCourseArrange set courseId" + place1 + "='" + cid1+"',courseName"+ place1 + "='" + cname1+"',tteacId"+ place1 + "='" + tid1+"',teacherGh"+ place1 + "='" + tgh1+"',teacherName"+ place1 + "='" + tname1+"' where uuid='"+uuid1+"'";
	    	      	String hql8="update JwTCourseArrange set courseId" + place2 + "='" + cid2+"',courseName"+ place2 + "='" + cname2+"',tteacId"+ place2 + "='" + tid2+"',teacherGh"+ place2 + "='" + tgh2+"',teacherName"+ place2 + "='" + tname2+"' where uuid='"+uuid2+"'";
	    	      	thisService.executeHql(hql7);
	    	    	thisService.executeHql(hql8);
	        	}else{
	            	String hql1="update JwTCourseArrange set isDelete=0 where uuid='"+id1+"'";
	            	String hql2="update JwTCourseArrange set isDelete=0 where uuid='"+id2+"'";
	            	thisService.executeHql(hql1);
	            	thisService.executeHql(hql2);
	            	
	            	//根据修改课表的id重新插入两条记录
	            	String uuid1=UUID.randomUUID().toString();
	            	String uuid2=UUID.randomUUID().toString();
	            	String sql1="insert into  dbo.JW_T_COURSE_ARRANGE select '"+uuid1+"',SCHOOL_ID, SCHOOL_NAME, SCHOOL_YEAR, SCHOOL_TERM, CLAI_ID, CLASS_CODE, CLASS_NAME, TEACH_TIME, COURSE_ID01, COURSE_NAME01, TTEAC_ID01, TEACHER_GH01, TEACHER_NAME01, COURSE_ID02, COURSE_NAME02, TTEAC_ID02, TEACHER_GH02, TEACHER_NAME02, COURSE_ID03, COURSE_NAME03, TTEAC_ID03, TEACHER_GH03, TEACHER_NAME03, COURSE_ID04, COURSE_NAME04, TTEAC_ID04, TEACHER_GH04, TEACHER_NAME04, COURSE_ID05, COURSE_NAME05, TTEAC_ID05, TEACHER_GH05, TEACHER_NAME05, COURSE_ID06, COURSE_NAME06, TTEAC_ID06, TEACHER_GH06, TEACHER_NAME06, COURSE_ID07, COURSE_NAME07, TTEAC_ID07, TEACHER_GH07, TEACHER_NAME07, EXT_FIELD01, EXT_FIELD02, EXT_FIELD03, EXT_FIELD04, EXT_FIELD05, ORDER_INDEX, CREATE_TIME, CREATE_USER, UPDATE_TIME, UPDATE_USER, "+1+", VERSION, '"+startime+"', '"+endtime+"' from dbo.JW_T_COURSE_ARRANGE where uuid='"+id1+"'";
	            	String sql2="insert into  dbo.JW_T_COURSE_ARRANGE select '"+uuid2+"',SCHOOL_ID, SCHOOL_NAME, SCHOOL_YEAR, SCHOOL_TERM, CLAI_ID, CLASS_CODE, CLASS_NAME, TEACH_TIME, COURSE_ID01, COURSE_NAME01, TTEAC_ID01, TEACHER_GH01, TEACHER_NAME01, COURSE_ID02, COURSE_NAME02, TTEAC_ID02, TEACHER_GH02, TEACHER_NAME02, COURSE_ID03, COURSE_NAME03, TTEAC_ID03, TEACHER_GH03, TEACHER_NAME03, COURSE_ID04, COURSE_NAME04, TTEAC_ID04, TEACHER_GH04, TEACHER_NAME04, COURSE_ID05, COURSE_NAME05, TTEAC_ID05, TEACHER_GH05, TEACHER_NAME05, COURSE_ID06, COURSE_NAME06, TTEAC_ID06, TEACHER_GH06, TEACHER_NAME06, COURSE_ID07, COURSE_NAME07, TTEAC_ID07, TEACHER_GH07, TEACHER_NAME07, EXT_FIELD01, EXT_FIELD02, EXT_FIELD03, EXT_FIELD04, EXT_FIELD05, ORDER_INDEX, CREATE_TIME, CREATE_USER, UPDATE_TIME, UPDATE_USER, "+1+", VERSION, '"+startime+"', '"+endtime+"' from dbo.JW_T_COURSE_ARRANGE where uuid='"+id2+"'";
	            	thisService.executeSql(sql1);
	            	thisService.executeSql(sql2);
	            	//更新之后完成新的课表
	            	String hql7="update JwTCourseArrange set courseId" + place1 + "='" + cid1+"',courseName"+ place1 + "='" + cname1+"',tteacId"+ place1 + "='" + tid1+"',teacherGh"+ place1 + "='" + tgh1+"',teacherName"+ place1 + "='" + tname1+"' where uuid='"+uuid1+"'";
	              	String hql8="update JwTCourseArrange set courseId" + place2 + "='" + cid2+"',courseName"+ place2 + "='" + cname2+"',tteacId"+ place2 + "='" + tid2+"',teacherGh"+ place2 + "='" + tgh2+"',teacherName"+ place2 + "='" + tname2+"' where uuid='"+uuid2+"'";
	              	thisService.executeHql(hql7);
	            	thisService.executeHql(hql8);	
	        	}
    		}else{
    			
    		}*/
    		String week1=zh(place1);
    		String week2=zh(place2);
    		
    		thisService.executeHql(hql1);
    		thisService.executeHql(hql2);
    		String push1=tname1+",由于课程临时调整，你在"+classname+""+week2+"第"+time2+"节的"+cname1+"课,调换到"+week1+"第"+time1+"节。特此通知！";
    		String push2=tname2+",由于课程临时调整，你在"+classname+""+week1+"第"+time1+"节的"+cname2+"课,调换到"+week2+"第"+time2+"节。特此通知！";
    		PushInfo push=new PushInfo();
    		PushInfo pushtwo=new PushInfo();
    		push.setEmplNo(tgh1);
    		push.setEmplName(tname1);
    		push.setPushStatus(0);
    		push.setRegStatus(push1);
    		push.setPushWay(1);
    		push.setRegTime(new Date());
    		push.setEventType("调课通知");
    		pushService.merge(push);
    		
    		pushtwo.setEmplNo(tgh2);
    		pushtwo.setEmplName(tname2);
    		pushtwo.setPushStatus(0);
    		pushtwo.setRegStatus(push2);
    		pushtwo.setPushWay(1);
    		pushtwo.setRegTime(new Date());
    		pushtwo.setEventType("调课通知");
    		pushService.merge(pushtwo);	
        	return "true";
    	}else if(count1>0){
    		return "false1";
    	}else{
    		return "false2";
    	}
      	

    }
    
    @RequestMapping("/previewlist")
    public @ResponseBody List<Object[]> previewlist(String cname,HttpServletRequest request, HttpServletResponse response) throws IOException {
		//String sql="select COUNT(*) from(select  ISDELETE from dbo.JW_T_COURSE_ARRANGE where CLASS_NAME='"+cname+"' group by ISDELETE) a";
		String sql="select distinct isdelete,class_name from JW_T_COURSE_ARRANGE where CLASS_NAME='"+cname+"'";
    	List<Object[]> list=thisService.ObjectQuerySql(sql);
    	return list;
 	
    }
    
    @RequestMapping("/copylist")
    public @ResponseBody String copylist(String startime,String endtime,String cname,String isdelete,HttpServletRequest request, HttpServletResponse response) throws IOException {
    	String copysql="select max(distinct isdelete) from JW_T_COURSE_ARRANGE where CLASS_NAME='"+cname+"'";
    	Integer copycount=thisService.getCountSql(copysql);
    	String sql="insert into  dbo.JW_T_COURSE_ARRANGE select NEWID(),SCHOOL_ID, SCHOOL_NAME, SCHOOL_YEAR, SCHOOL_TERM, CLAI_ID, CLASS_CODE, CLASS_NAME, TEACH_TIME, COURSE_ID01, COURSE_NAME01, TTEAC_ID01, TEACHER_GH01, TEACHER_NAME01, COURSE_ID02, COURSE_NAME02, TTEAC_ID02, TEACHER_GH02, TEACHER_NAME02, COURSE_ID03, COURSE_NAME03, TTEAC_ID03, TEACHER_GH03, TEACHER_NAME03, COURSE_ID04, COURSE_NAME04, TTEAC_ID04, TEACHER_GH04, TEACHER_NAME04, COURSE_ID05, COURSE_NAME05, TTEAC_ID05, TEACHER_GH05, TEACHER_NAME05, COURSE_ID06, COURSE_NAME06, TTEAC_ID06, TEACHER_GH06, TEACHER_NAME06, COURSE_ID07, COURSE_NAME07, TTEAC_ID07, TEACHER_GH07, TEACHER_NAME07, EXT_FIELD01, EXT_FIELD02, EXT_FIELD03, EXT_FIELD04, 0, ORDER_INDEX, CREATE_TIME, CREATE_USER, UPDATE_TIME, UPDATE_USER, "+(copycount+1)+", VERSION, '"+startime+"', '"+endtime+"' from dbo.JW_T_COURSE_ARRANGE where isdelete="+Integer.parseInt(isdelete)+" and CLASS_NAME='"+cname+"';";
		Integer count=thisService.executeSql(sql);
    	if(count>0){
    		return "true";
    	}else
    		return "false";
    }
    
    public String zh(String day){
		if(day.equals("01")){
			return "星期一";
		}else if(day.equals("02")){
			return "星期二";
		}else if(day.equals("03")){
			return "星期三";
		}else if(day.equals("04")){
			return "星期四";
		}else if(day.equals("05")){
			return "星期五";
		}else if(day.equals("06")){
			return "星期六";
		}else{
			return "星期日";
		}
    }
    
    
    @RequestMapping("/start")
    public @ResponseBody String start(String cname,String isdelete,HttpServletRequest request, HttpServletResponse response) throws IOException {		
    	String sql="update dbo.JW_T_COURSE_ARRANGE set EXT_FIELD05=case when ISDELETE='"+isdelete+"' then 1 else 0 end where CLASS_NAME='"+cname+"'";
    	Integer count=thisService.executeSql(sql);
    	if(count>0){
    		return "true";
    	}else
    		return "false";
    }
   
    
    
    @RequestMapping(value = { "/importTxt" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
            org.springframework.web.bind.annotation.RequestMethod.POST })
	public void importCourseFotTxt(@RequestParam("txtFile") MultipartFile fileUrl,HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		Map<String, Map<String, List<String>>> map = readTxt
				.importCourseTxt(fileUrl);//解析数据
		if(map == null){
			writeJSON(response, jsonBuilder.returnFailureJson(jsonBuilder.toJson("'课表解析失败'")));
			return ;
		}
		//封装数据
		try{
		List<List<JwCourseArrange>> allJac = new ArrayList<List<JwCourseArrange>>();
		Set<String> classSet = map.keySet();
		Iterator<String> calssIt = classSet.iterator();
		while(calssIt.hasNext()){//班级
			int entityNum = 0;
			List<JwCourseArrange> jcaList = new ArrayList<JwCourseArrange>();// 班级课程
			allJac.add(jcaList);
			String classCode =  calssIt.next();
			Map<String, List<String>> weekMap = map.get(classCode);
			Set<String> weekSet = weekMap.keySet();
			Iterator<String> getNumIt = weekSet.iterator();
			while(getNumIt.hasNext()){//星期几？
				String weekCode = getNumIt.next();
				List<String> courseList = weekMap.get(weekCode);
				entityNum = courseList.size() > entityNum ? courseList.size() : entityNum;
			}
			for(int i = 1;i<=entityNum;i++){
				JwCourseArrange jca = new JwCourseArrange();
				jca.setClassName(classCode);
				jca.setTeachTime(String.valueOf(i));
				JwTGradeclass tempClass = jwClassService.getByProerties("className", classCode);
				if(tempClass == null){
					writeJSON(response, jsonBuilder.returnFailureJson(jsonBuilder.toJson("'未找到该班级：'"+classCode)));
					return ;
				}
				jca.setClaiId(tempClass.getUuid());
				jca.setClassCode(tempClass.getClassCode());
				jcaList.add(jca);
			}
			Iterator<String> weekIt = weekSet.iterator();
			while(weekIt.hasNext()){//星期几
				String weekCode = weekIt.next();
				String teacherGh = "";
				String teacherName = "";
				String teacherId = "";
				List<String> courseList = weekMap.get(weekCode);
				for (int i = 0; i < courseList.size(); i++) {
					teacherGh = "";
					teacherName = "";
					teacherId = "";					
					JwCourseArrange tempJac = jcaList.get(i);
					JwTBasecourse tempJtb = jtbService.getByProerties(
							"courseName", courseList.get(i));
					String courseId = "";
					if(tempJtb != null){
						//courseId = tempJtb.getCourseCode();
					    courseId = tempJtb.getUuid();
						StringBuilder courseTeacherHQL = new StringBuilder(
								" from JwCourseteacher where claiId='");
						courseTeacherHQL.append(jcaList.get(i).getClaiId())
								.append("' and courseId ='")
								.append(tempJtb.getUuid()).append("'");
						List<JwCourseteacher> courseTeacherList = null;
						try{
							courseTeacherList = courseTeacherService
							.doQuery(courseTeacherHQL.toString());
						}catch(Exception e){
							e.printStackTrace();
						}
						if(courseTeacherList !=null && courseTeacherList.size() > 0){
							teacherId = courseTeacherList.get(0).getTteacId();
							//String teacherId = courseTeacherList.get(0).getTteacId();
							if(teacherId != null && !teacherId.trim().equals("")){
								TeaTeacherbase tempTeacher = teacherService
										.getByProerties("uuid", teacherId);
								if(tempTeacher!=null){
									teacherGh = tempTeacher.getUserNumb();
									teacherName = tempTeacher.getXm();
								}
							}
						}
					}
					switch (weekCode) {
					case "星期一":
						tempJac.setCourseId01(courseId);
						tempJac.setCourseName01(courseList.get(i));
						tempJac.setTteacId01(teacherId);
						tempJac.setTeacherGh01(teacherGh);
						tempJac.setTeacherName01(teacherName);
						break;
					case "星期二":
						tempJac.setCourseId02(courseId);
						tempJac.setCourseName02(courseList.get(i));
						tempJac.setTteacId02(teacherId);
						tempJac.setTeacherGh02(teacherGh);
						tempJac.setTeacherName02(teacherName);
						break;
					case "星期三":
						tempJac.setCourseId03(courseId);
						tempJac.setCourseName03(courseList.get(i));
						tempJac.setTteacId03(teacherId);
						tempJac.setTeacherGh03(teacherGh);
						tempJac.setTeacherName03(teacherName);
						break;
					case "星期四":
						tempJac.setCourseId04(courseId);
						tempJac.setCourseName04(courseList.get(i));
						tempJac.setTteacId04(teacherId);
						tempJac.setTeacherGh04(teacherGh);
						tempJac.setTeacherName04(teacherName);
						break;
					case "星期五":
						tempJac.setCourseId05(courseId);
						tempJac.setCourseName05(courseList.get(i));
						tempJac.setTteacId05(teacherId);
						tempJac.setTeacherGh05(teacherGh);
						tempJac.setTeacherName05(teacherName);
						break;
					case "星期六":
						tempJac.setCourseId06(courseId);
						tempJac.setCourseName06(courseList.get(i));
						tempJac.setTteacId06(teacherId);
						tempJac.setTeacherGh06(teacherGh);
						tempJac.setTeacherName06(teacherName);
						break;
					case "星期天":
						tempJac.setCourseId07(courseId);
						tempJac.setCourseName07(courseList.get(i));
						tempJac.setTteacId07(teacherId);
						tempJac.setTeacherGh07(teacherGh);
						tempJac.setTeacherName07(teacherName);
						break;
					default:
						break;
					}
				}
			}
			
		}
		//添加集合中的对象
			for(List<JwCourseArrange> jacList : allJac){
				for(JwCourseArrange entity:jacList){
					entity.setSchoolId("2851655E-3390-4B80-B00C-52C7CA62CB39");
					entity.setSchoolName("深大附中");
					entity.setExtField05("1");
					thisService.merge(entity);
				}
			}
		}catch (Exception e){
			e.printStackTrace();
			writeJSON(response, jsonBuilder.returnFailureJson(jsonBuilder.toJson("'课表导入失败'")));
			return ;
		}
		writeJSON(response, jsonBuilder.returnSuccessJson(jsonBuilder.toJson("'课表导入成功'")));
		return ;
    }
    
    
    @RequestMapping(value = { "/gettname" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
            org.springframework.web.bind.annotation.RequestMethod.POST })
	public  @ResponseBody List<JwXyz> getTname(String cname,String tname,String isdelete,String weekX,String timeY,HttpServletRequest request,
			HttpServletResponse response) throws IOException {
    	String hql="from JwTGradeclass where className='"+cname+"'";
    	List<JwTGradeclass> list=jwClassService.doQuery(hql);
    	String sql=" select user_id,xm from SYS_T_USER where USER_ID in (select tteac_id from JW_T_COURSETEACHER where CLAI_ID='"+list.get(0).getUuid()+"')";
    	List<Object[]> listo=userService.ObjectQuerySql(sql);
    	for (Object[] objects : listo) {
			System.out.println(objects[1]);
		}
    	for (int i = 0; i < listo.size(); i++) {
    		Object[] objects=listo.get(i);
		    if (objects[1].toString().equals(tname)) {					
						listo.remove(i);
					}
		}    	
    	List<JwXyz> listxyz=new ArrayList<JwXyz>();
    	JwXyz xyz=null;
    	for(int i=1;i<=7;i++){
    		String hql1="from JwCourseArrange where teacherName0"+i+"='"+tname+"' and isDelete="+isdelete+"";
    		List<JwCourseArrange> list1=thisService.doQuery(hql1);
    		for(int j=0;j<list1.size();j++){
    			xyz=new JwXyz();
    			list1.get(j).getTeachTime();
    			xyz.setY(Integer.parseInt(list1.get(j).getTeachTime())-1);
    			xyz.setX(i);
    			listxyz.add(xyz);
    			
    		}
    	}
		return listxyz;
    	
    }
    
    @RequestMapping(value = { "/gettnametwo" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
            org.springframework.web.bind.annotation.RequestMethod.POST })
	public @ResponseBody List<String> getTnametwo(String cname,String tname,String isdelete,String weekX,String timeY,HttpServletRequest request,
			HttpServletResponse response) throws IOException {
    	String hql="from JwTGradeclass where className='"+cname+"'";
    	List<JwTGradeclass> list=jwClassService.doQuery(hql);
    	String sql=" select user_id,xm from SYS_T_USER where USER_ID in (select tteac_id from JW_T_COURSETEACHER where CLAI_ID='"+list.get(0).getUuid()+"')";
    	List<Object[]> listo=userService.ObjectQuerySql(sql);
    	for (Object[] objects : listo) {
			System.out.println(objects[1]);
		}
    	for (int i = 0; i < listo.size(); i++) {
    		Object[] objects=listo.get(i);
		    if (objects[1].toString().equals(tname)) {					
						listo.remove(i);
					}
		}
    	List<String> list1=new ArrayList<String>();
    	for(int j=0;j<listo.size();j++){
    		String sql1="select COUNT(*) from dbo.JW_T_COURSE_ARRANGE where TEACHER_NAME"+weekX+"='"+listo.get(j)[1]+"' and TEACH_TIME="+(Integer.parseInt(timeY)+1)+" and ISDELETE="+isdelete+"";
    		Integer result=thisService.getCountSql(sql1);
    		if(result>0){
    			list1.add(listo.get(j)[1].toString());
    		}
    	}
		return list1;

    	
    }
    
}
