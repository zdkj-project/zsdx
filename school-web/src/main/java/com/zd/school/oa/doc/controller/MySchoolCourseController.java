package com.zd.school.oa.doc.controller;

import com.zd.core.constant.Constant;
import com.zd.core.constant.StatuVeriable;
import com.zd.core.controller.core.FrameWorkController;
import com.zd.core.model.extjs.QueryResult;
import com.zd.core.util.BeanUtils;
import com.zd.core.util.StringUtils;
import com.zd.school.jw.eduresources.model.JwTSchoolcourse;
import com.zd.school.jw.eduresources.service.JwTSchoolcourseService;
import com.zd.school.plartform.system.model.SysUser;
import com.zd.school.plartform.system.service.SysUserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;



@Controller
@RequestMapping("/MySchoolCourse")
public class MySchoolCourseController extends FrameWorkController<JwTSchoolcourse> implements Constant {
	
    @Resource
    JwTSchoolcourseService thisService;
    
    @Resource
    SysUserService userService;
    
    //获取列表数据
    @RequestMapping(value = "/list", method = { org.springframework.web.bind.annotation.RequestMethod.GET,
            org.springframework.web.bind.annotation.RequestMethod.POST })
    public void getList(@ModelAttribute JwTSchoolcourse entity, HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        String strData = ""; // 返回给js的数据
        QueryResult<JwTSchoolcourse> qr = thisService.getPaginationQuery(super.start(request), super.limit(request),
                super.sort(request), super.filter(request), true);

        strData = jsonBuilder.buildObjListToJson(qr.getTotalCount(), qr.getResultList(), true);// 处理数据
        writeJSON(response, strData);// 返回数据
    }
    
    @RequestMapping("/doadd")
    public void doAdd(JwTSchoolcourse entity, HttpServletRequest request, HttpServletResponse response)
            throws IOException, IllegalAccessException, InvocationTargetException {
        String courseName = entity.getCourseName();

        if (thisService.IsFieldExist("courseName", courseName, "-1")) {
            writeJSON(response, jsonBuilder.returnFailureJson("'课程名称不能重复！'"));
            return;
        }

        //获取当前操作用户
        String userCh = "超级管理员";
        SysUser currentUser = getCurrentSysUser();
        if (currentUser != null)
            userCh = currentUser.getXm();

        JwTSchoolcourse saveEntity = new JwTSchoolcourse();
        BeanUtils.copyPropertiesExceptNull(entity, saveEntity);

        // 增加时要设置创建人
        entity.setCreateUser(userCh); // 创建人
        entity.setState(1);
        entity.setSchoolId(currentUser.getSchoolId());
        entity.setSchoolName(currentUser.getSchoolName());
        entity.setTeacherName(userCh);
        entity.setTeachID(currentUser.getUuid());
        // 持久化到数据库
        entity = thisService.doMerge(entity);

        //返回实体到前端界面
        writeJSON(response, jsonBuilder.returnSuccessJson(jsonBuilder.toJson(entity)));
    }
    
    
    @RequestMapping("/doDraft")
    public void doDraft(JwTSchoolcourse entity, HttpServletRequest request, HttpServletResponse response)
            throws IOException, IllegalAccessException, InvocationTargetException {
        String courseName = entity.getCourseName();

        if (thisService.IsFieldExist("courseName", courseName, "-1")) {
            writeJSON(response, jsonBuilder.returnFailureJson("'课程名称不能重复！'"));
            return;
        }

        //获取当前操作用户
        String userCh = "超级管理员";
        SysUser currentUser = getCurrentSysUser();
        if (currentUser != null)
            userCh = currentUser.getXm();

        JwTSchoolcourse saveEntity = new JwTSchoolcourse();
        BeanUtils.copyPropertiesExceptNull(entity, saveEntity);

        // 增加时要设置创建人
        entity.setCreateUser(userCh); // 创建人
        entity.setState(0);
        entity.setSchoolId(currentUser.getSchoolId());
        entity.setSchoolName(currentUser.getSchoolName());
        entity.setTeacherName(userCh);
        entity.setTeachID(currentUser.getUuid());
        // 持久化到数据库
        entity = thisService.doMerge(entity);

        //返回实体到前端界面
        writeJSON(response, jsonBuilder.returnSuccessJson(jsonBuilder.toJson(entity)));
    }
    
    @RequestMapping("/dodelete")
    public void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String delIds = request.getParameter("ids");
        if (StringUtils.isEmpty(delIds)) {
            writeJSON(response, jsonBuilder.returnSuccessJson("'没有传入删除主键'"));
            return;
        } else {
            boolean flag = thisService.doLogicDelOrRestore(delIds, StatuVeriable.ISDELETE);
            if (flag) {
                writeJSON(response, jsonBuilder.returnSuccessJson("'删除成功'"));
            } else {
                writeJSON(response, jsonBuilder.returnFailureJson("'删除失败'"));
            }
        }
    }
    
    @RequestMapping("/doupdate")
    public void doUpdates(JwTSchoolcourse entity, HttpServletRequest request, HttpServletResponse response)
            throws IOException, IllegalAccessException, InvocationTargetException {
        String courseName = entity.getCourseName();
        String courseId = entity.getUuid();

        if (thisService.IsFieldExist("courseName", courseName, courseId)) {
            writeJSON(response, jsonBuilder.returnFailureJson("'课程名称不能重复！'"));
            return;
        }

        //获取当前的操作用户
        String userCh = "超级管理员";
        SysUser currentUser = getCurrentSysUser();
        if (currentUser != null)
            userCh = currentUser.getXm();

        //先拿到已持久化的实体
        JwTSchoolcourse perEntity = thisService.get(entity.getUuid());
        BeanUtils.copyPropertiesExceptNull(perEntity, entity);

        perEntity.setUpdateTime(new Date()); //设置修改时间
        perEntity.setUpdateUser(userCh); //设置修改人的中文名
        entity = thisService.doMerge(perEntity);//执行修改方法

        writeJSON(response, jsonBuilder.returnSuccessJson(jsonBuilder.toJson(perEntity)));

    }
    
    
    @RequestMapping("/approvelist")
    public void approvelist(@ModelAttribute JwTSchoolcourse entity,HttpServletRequest request, HttpServletResponse response)
            throws IOException, IllegalAccessException, InvocationTargetException {
    	SysUser currentUser = getCurrentSysUser();
		String hql1 = "from SysUser as u inner join fetch u.sysRoles as r where r.roleName='课程管理员' and r.isDelete=0 and u.isDelete=0 and u.uuid='"+currentUser.getUuid()+"'";
		List<SysUser> list=userService.getQuery(hql1);
		if(list.size()>0){
	        String strData = ""; // 返回给js的数据
	        StringBuffer hql = new StringBuffer("from " + entity.getClass().getSimpleName() + " o where 1=1 ");
			StringBuffer countHql = new StringBuffer(
					"select count(*) from " + entity.getClass().getSimpleName() + " where   1=1");
	        String whereSql = entity.getWhereSql();// 查询条件
			int start =  super.start(request); // 起始记录数
			int limit = entity.getLimit();// 每页记录数
			hql.append(whereSql);
			countHql.append(whereSql);
			List<JwTSchoolcourse> listapprove=thisService.getQuery(hql.toString(), start, limit);
			Integer count = thisService.getCount(countHql.toString());// 查询总记录数
			strData = jsonBuilder.buildObjListToJson(new Long(count), listapprove, true);// 处理数据
	        writeJSON(response, strData);// 返回数据
		}

    }
    
    @RequestMapping("/approve")
    public void approve(@ModelAttribute JwTSchoolcourse entity,HttpServletRequest request, HttpServletResponse response)
            throws IOException, IllegalAccessException, InvocationTargetException {
    	String Tstate=request.getParameter("distribType");
    	String state="";
    	if(Tstate.equals("true")){
    		state="2";
    	}else{
    		state="3";
    	}
        //获取当前的操作用户
        String userCh = "超级管理员";
        SysUser currentUser = getCurrentSysUser();
        if (currentUser != null)
            userCh = currentUser.getXm();
    	
    	entity.setState(Integer.parseInt(state));
        JwTSchoolcourse perEntity = thisService.get(entity.getUuid());
        BeanUtils.copyPropertiesExceptNull(perEntity, entity);
        
        perEntity.setUpdateTime(new Date()); //设置修改时间
        perEntity.setUpdateUser(userCh); //设置修改人的中文名
        entity = thisService.doMerge(perEntity);//执行修改方法
        
        writeJSON(response, jsonBuilder.returnSuccessJson(jsonBuilder.toJson(perEntity)));

    }
    
    @RequestMapping("/commit")
    public void commit(JwTSchoolcourse entity, HttpServletRequest request, HttpServletResponse response)
            throws IOException, IllegalAccessException, InvocationTargetException {
        //获取当前的操作用户
        String userCh = "超级管理员";
        SysUser currentUser = getCurrentSysUser();
        if (currentUser != null)
            userCh = currentUser.getXm();

        //先拿到已持久化的实体
        JwTSchoolcourse perEntity = thisService.get(entity.getUuid());
        BeanUtils.copyPropertiesExceptNull(perEntity, entity);

        perEntity.setUpdateTime(new Date()); //设置修改时间
        perEntity.setUpdateUser(userCh); //设置修改人的中文名
        perEntity.setState(1);
        entity = thisService.doMerge(perEntity);//执行修改方法

        writeJSON(response, jsonBuilder.returnSuccessJson(jsonBuilder.toJson(perEntity)));

    }
    
    @RequestMapping("/edit")
    public void edit(JwTSchoolcourse entity, HttpServletRequest request, HttpServletResponse response)
            throws IOException, IllegalAccessException, InvocationTargetException {
        String courseName = entity.getCourseName();
        String courseId = entity.getUuid();

        if (thisService.IsFieldExist("courseName", courseName, courseId)) {
            writeJSON(response, jsonBuilder.returnFailureJson("'课程名称不能重复！'"));
            return;
        }

        //获取当前的操作用户
        String userCh = "超级管理员";
        SysUser currentUser = getCurrentSysUser();
        if (currentUser != null)
            userCh = currentUser.getXm();

        //先拿到已持久化的实体
        JwTSchoolcourse perEntity = thisService.get(entity.getUuid());
        BeanUtils.copyPropertiesExceptNull(perEntity, entity);

        perEntity.setUpdateTime(new Date()); //设置修改时间
        perEntity.setUpdateUser(userCh); //设置修改人的中文名
        entity = thisService.doMerge(perEntity);//执行修改方法

        writeJSON(response, jsonBuilder.returnSuccessJson(jsonBuilder.toJson(perEntity)));

    }

}
