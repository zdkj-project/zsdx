package com.zd.school.jw.arrangecourse.controller;



import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.scripting.xmltags.ForEachSqlNode;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zd.core.constant.Constant;
import com.zd.core.constant.NodeType;
import com.zd.core.controller.core.FrameWorkController;
import com.zd.core.model.extjs.JSONTreeNode;
import com.zd.core.util.JsonBuilder;
import com.zd.core.util.StringUtils;
import com.zd.school.jw.eduresources.model.JwTGrade;
import com.zd.school.jw.eduresources.service.JwTGradeService;
import com.zd.school.teacher.teacherinfo.model.TeaTeacherbase;
import com.zd.school.teacher.teacherinfo.service.TeaTeacherbaseService;

/**
 * 
 * ClassName: JwTCourseStudyController
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 自习课程表实体Controller.
 * date: 2016-04-22
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Controller
@RequestMapping("/JwTAutoMonitor")
public class JwTAutoMonitor extends FrameWorkController<TeaTeacherbase> implements Constant {

    @Resource
    TeaTeacherbaseService teacherService;
    
    @Resource
    JwTGradeService gradeService;
    
    
    //查询所有老师
    @RequestMapping(value = { "/teacherlist" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
            org.springframework.web.bind.annotation.RequestMethod.POST }, produces = "text/html;charset=UTF-8")
    public @ResponseBody String teacherlist(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        // hql语句
    	String hql="from TeaTeacherbase";
        List<TeaTeacherbase> list = teacherService.getQuery(hql);// 执行查询方法
        StringBuffer result=new StringBuffer();
        for(int i=0;i<list.size();i++){
        	if(i==(list.size()-1)){
        		result.append(list.get(i).getXm());
        	}else
        	result.append(list.get(i).getXm()+",");
        }
        String teacher=result.toString();
        System.out.println(teacher);
        
        return teacher;
    }
    
    //保存数据
    @RequestMapping(value = { "/saveexam" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
            org.springframework.web.bind.annotation.RequestMethod.POST }, produces = "text/html;charset=UTF-8")
    public void saveexam (HttpServletRequest request, HttpServletResponse response){
        // 获得页面所有表单
        Map<String, String[]> paramMap = request.getParameterMap();
        String deletesql="truncate table JW_T_EXAM_SAVE";
        teacherService.doExecuteSql(deletesql);
        for (String key : paramMap.keySet()) {
            String[] value = paramMap.get(key);
            String sql="insert into JW_T_EXAM_SAVE (exam_name,exam_value) values ('"+key+"','"+value[0]+"')";
            teacherService.doExecuteSql(sql);
        }
    }
    
    //读取数据
    @RequestMapping(value = { "/getexam" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
            org.springframework.web.bind.annotation.RequestMethod.POST })
    public @ResponseBody List<Object[]> getexam (HttpServletRequest request, HttpServletResponse response){
    	String sql="select * from JW_T_EXAM_SAVE";
    	List<Object[]> list=teacherService.ObjectQuerySql(sql);

		return list;
    }
    
    
    
    //查询所有年级
    @RequestMapping(value = { "/gradelist" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
            org.springframework.web.bind.annotation.RequestMethod.POST }, produces = "text/html;charset=UTF-8")
    public void gradelist(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        // hql语句
    	String hql="from JwTGrade where isdelete=0";
        List<JwTGrade> list = gradeService.getQuery(hql);// 执行查询方法
        String strData = jsonBuilder.buildList(list,"");// 处理数据
        writeJSON(response, strData);// 返回数据
    }
    
    
    @RequestMapping(value = { "/course" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
            org.springframework.web.bind.annotation.RequestMethod.POST }, produces = "text/html;charset=UTF-8")
    public @ResponseBody String course(String gradeid,HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        // hql语句
    	String hql="select COURSE_NAME,BASECOURSE_ID from JW_T_BASECOURSE where BASECOURSE_ID in (select MAIN_COURSE from TEA_T_TEACHERBASE where user_id in(select TTEAC_ID from JW_T_COURSETEACHER where CLAI_ID in(select CLAI_ID from JW_T_GRADECLASS)))";
        // where GRAI_ID='"+gradeid+"'
    	List<Object[]> list = teacherService.ObjectQuerySql(hql);// 执行查询方法
        
        StringBuffer result=new StringBuffer();
        for(int i=0;i<list.size();i++){
        	if(i==(list.size()-1)){
        		result.append(list.get(i)[0].toString());
        	}else
        	result.append(list.get(i)[0].toString()+",");
        }
        String course=result.toString();
        System.out.println(course);

		return course;

    }
    
    @RequestMapping("/getJwTTeacherTree")
    public void getJwTClassTree(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String node = request.getParameter("node");
        String excludes = request.getParameter("excludes");
        String parentId = request.getParameter("parentId");
        String whereSql = request.getParameter("whereSql");
        StringBuffer sBuffer = new StringBuffer();
        sBuffer.append("select * from V_AUTOMONITOR");
        if (StringUtils.isEmpty(parentId)) {
            parentId = NodeType.ROOT;
        }
        List<JSONTreeNode> jsonList = new ArrayList<JSONTreeNode>();

        if (StringUtils.isNotEmpty(whereSql)) {
            sBuffer.append(whereSql);
        }
        List<?> list = teacherService.getQuerySql(sBuffer.toString());
        //循环添加数据
        for (int i = 0; i < list.size(); i++) {
            Object[] obj = (Object[]) list.get(i);
            JSONTreeNode jsonNode = new JSONTreeNode();
            jsonNode.setText((String) obj[0]);
            jsonNode.setParent((String) obj[1]);
            jsonNode.setId(obj[2].toString());
            jsonNode.setNodeLayer(obj[3].toString());
            jsonList.add(jsonNode);
        }
        // 构建成树形节点对象
        JSONTreeNode roots = buildJSONTreeNode(jsonList, node);
        //获得json格式树结构
        String strData = JsonBuilder.getInstance().buildList(roots.getChildren(), excludes);
        writeJSON(response, strData);
    }
    /**
     * 构建树形对象.
     *   
     * @param list
     *            树形列表对象
     * @param rootId
     *            树形的根节点
     * @return the JSON tree node
     */
    public JSONTreeNode buildJSONTreeNode(List<JSONTreeNode> list, String rootId) {
        JSONTreeNode root = new JSONTreeNode();
        for (JSONTreeNode node : list) {
            if (!(StringUtils.isNotEmpty(node.getParent()) && !node.getId().equals(rootId))) {
                root = node;
                list.remove(node);
                break;
            }
        }
        createTreeChildren(list, root);
        return root;
    }
    
    private void createTreeChildren(List<JSONTreeNode> childrens, JSONTreeNode root) {
        String parentId = root.getId();
        for (int i = 0; i < childrens.size(); i++) {
            JSONTreeNode node = childrens.get(i);
            if (StringUtils.isNotEmpty(node.getParent()) && node.getParent().equals(parentId)) {
                root.getChildren().add(node);
                createTreeChildren(childrens, node);
            }
            if (i == childrens.size() - 1) {
                if (root.getChildren().size() < 1) {
                    root.setLeaf(true);
                }
                return;
            }
        }
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
    public void list(@ModelAttribute TeaTeacherbase entity, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String strData = ""; // 返回给js的数据
        // hql语句
        StringBuffer hql = new StringBuffer("from " + entity.getClass().getSimpleName() + " where 1=1 ");
        
        
    
        
        // 总记录数
        StringBuffer countHql = new StringBuffer(
                "select count(*) from " + entity.getClass().getSimpleName() + " where  1=1");
        String whereSql = entity.getWhereSql();// 查询条件
        String parentSql = entity.getParentSql();// 条件
        String querySql = entity.getQuerySql();// 查询条件
        String orderSql = entity.getOrderSql();// 排序
        int start = super.start(request); // 起始记录数
        int limit = entity.getLimit();// 每页记录数
       // hql.append(whereSql);
        hql.append(parentSql);
        hql.append(querySql);
        hql.append(orderSql);
       // countHql.append(whereSql);
        countHql.append(querySql);
        countHql.append(parentSql);
        if (whereSql!=null &&!whereSql.trim().equals("")) {
        	   String str=" and uuid in(select jwTTeacher.uuid from JwClassteacher where claiId in(?)) "
               		+ "or uuid in(select jwTTeacher.uuid from JwTCourseteacher where jwTGradeclass.claiId in(?)) "
               		+ "or uuid in(select jwTTeacher.uuid from JwGradeteacher where graiId in(select parentGrade.graiId from JwTGradeclass where claiId in(?)))";
             
               str=str.replaceAll("\\?",whereSql);   
               countHql.append(str);
               hql.append(str);
		}
     
        List<TeaTeacherbase> lists = teacherService.getQuery(hql.toString(), start, limit);// 执行查询方法
        List<TeaTeacherbase> newLists=new ArrayList<>();
        for (TeaTeacherbase jwtteacher : lists) {
        	jwtteacher.setUserNumb(jwtteacher.getUserNumb());    //得到的是教师的姓名
        	jwtteacher.setXm(jwtteacher.getXm());
        	jwtteacher.setXbm(jwtteacher.getXbm());
        	jwtteacher.setUuid(jwtteacher.getUuid());
        	//定义的是课程名称中课程表中的响应
        	//String zjkc=jwtCourseteacher.getJwTTeacher().getMainCourse();
            //定义班级名称
        	//String gradeName=jwTGradeclassService.get(jwtCourseteacher.getClaiId()).getClassName();
        
        	//jwtCourseteacher.setBjmc(jwtCourseteacher.getJwTGradeclass().getClassName());
        	//if(zjkc!=null){
        	//	jwtCourseteacher.setMainCourse(jwTBasecourseService.get(zjkc).getCourseName());
        	//}
        	
			newLists.add(jwtteacher);
		}
        Integer count = teacherService.getCount(countHql.toString());// 查询总记录数
        strData = jsonBuilder.buildObjListToJson(new Long(count), lists, true);// 处理数据
        writeJSON(response, strData);// 返回数据
    }



}
