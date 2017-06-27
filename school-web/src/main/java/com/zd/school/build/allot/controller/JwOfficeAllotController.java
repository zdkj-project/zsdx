
package com.zd.school.build.allot.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zd.core.constant.Constant;
import com.zd.core.constant.StatuVeriable;
import com.zd.core.controller.core.FrameWorkController;
import com.zd.core.model.extjs.QueryResult;
import com.zd.core.util.BeanUtils;
import com.zd.core.util.JsonBuilder;
import com.zd.core.util.StringUtils;
import com.zd.school.build.allot.model.JwOfficeAllot ;
import com.zd.school.build.allot.service.JwOfficeAllotService ;
import com.zd.school.build.define.model.BuildRoominfo;
import com.zd.school.build.define.service.BuildRoominfoService;
import com.zd.school.jw.push.model.PushInfo;
import com.zd.school.jw.push.service.PushInfoService;
import com.zd.school.plartform.comm.model.CommTree;
import com.zd.school.plartform.comm.service.CommTreeService;
import com.zd.school.plartform.system.model.SysUser;
import com.zd.school.teacher.teacherinfo.service.TeaTeacherbaseService;

/**
 * 
 * ClassName: JwOfficeallotController
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: JW_T_OFFICEALLOT实体Controller.
 * date: 2016-08-23
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Controller
@RequestMapping("/JwOfficeAllot")
public class JwOfficeAllotController extends FrameWorkController<JwOfficeAllot> implements Constant {

    @Resource
    JwOfficeAllotService thisService; // service层接口

    @Resource
    TeaTeacherbaseService teacherService;//教师 
    
    @Resource
    CommTreeService treeService;	//生成树

    @Resource
    PushInfoService pushService;	//推送
    
    @Resource
    BuildRoominfoService infoService;//房间
    
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
    public void list(@ModelAttribute JwOfficeAllot entity, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String strData = ""; // 返回给js的数据
        QueryResult<JwOfficeAllot> qr = thisService.doPaginationQuery(super.start(request), super.limit(request),
                super.sort(request), super.filter(request), true);

        strData = jsonBuilder.buildObjListToJson(qr.getTotalCount(), qr.getResultList(), true);// 处理数据
        writeJSON(response, strData);// 返回数据
    }

    /**
     * 生成树
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping("/treelist")
	public void getGradeTreeList(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String strData = "";
		String whereSql = request.getParameter("whereSql");
		List<CommTree> lists = treeService.getCommTree("JW_OFFICEALLOTTREE", whereSql);
		strData = JsonBuilder.getInstance().buildList(lists, "");// 处理数据
		writeJSON(response, strData);// 返回数据
	}

    /**
     * 
      * @Title: 增加新实体信息至数据库
      * @Description: TODO
      * @param @param JwOfficeallot 实体类
      * @param @param request
      * @param @param response
      * @param @throws IOException    设定参数
      * @return void    返回类型
      * @throws
     */
    @RequestMapping("/doadd")
    public void doAdd(JwOfficeAllot entity, HttpServletRequest request, HttpServletResponse response)
            throws IOException, IllegalAccessException, InvocationTargetException {
        
    	 String userCh = "超级管理员";//中文名

         String[] strId = null;//多个老师id
         strId = entity.getTteacId().split(",");//多个老师id

         SysUser currentUser = getCurrentSysUser();
         if (currentUser != null)
             userCh = currentUser.getXm();

         for (int i = 0; i < strId.length; i++) {
             boolean cz = thisService.IsFieldExist("tteacId", strId[i], "-1", "isdelete=0");
             if (cz) {
                 writeJSON(response, jsonBuilder.returnFailureJson("'请勿重复添加。'"));
                 return;
             }
             // 生成默认的orderindex
             Integer orderIndex = thisService.getDefaultOrderIndex(entity);
             JwOfficeAllot perEntity = new JwOfficeAllot();
             BeanUtils.copyPropertiesExceptNull(entity, perEntity);
             entity.setCreateUser(userCh); //创建人

             entity.setTteacId(strId[i]);
             
             entity.setOrderIndex(orderIndex);//排序

             thisService.merge(entity); // 执行添加方法
         }
		//返回实体到前端界面
        writeJSON(response, jsonBuilder.returnSuccessJson("'成功'"));
    }
    /**
     * 办公室推送
     * @param id
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping("/officeTs")
    public void officeTs(String id, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String[] str = { "roomId", "isDelete" };
        Object[] str2 = { id, 0 };
        List<JwOfficeAllot> newLists = thisService.queryByProerties(str, str2);
        PushInfo pushInfo = null;
        BuildRoominfo roominfo=null;
        for (JwOfficeAllot jwTOfficeAllot : newLists) {
            pushInfo = new PushInfo();
            pushInfo.setEmplName(jwTOfficeAllot.getXm());//姓名
            pushInfo.setEmplNo(jwTOfficeAllot.getGh());//学号
            pushInfo.setRegTime(new Date());
            pushInfo.setEventType("办公室分配");
            pushInfo.setPushStatus(0);
            pushInfo.setPushWay(1);
            roominfo = infoService.get(jwTOfficeAllot.getRoomId());
            pushInfo.setRegStatus(pushInfo.getEmplName() + "您好，你的办公室分配在" + roominfo.getAreaUpName() + "，" + roominfo.getAreaName() + "，"
                    + jwTOfficeAllot.getRoomName() + "房");
            pushService.merge(pushInfo);
        }
        writeJSON(response, jsonBuilder.returnSuccessJson("'推送信息成功。'"));
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
            boolean flag = thisService.logicDelOrRestore(delIds, StatuVeriable.ISDELETE);
            if (flag) {
                writeJSON(response, jsonBuilder.returnSuccessJson("'删除成功'"));
            } else {
                writeJSON(response, jsonBuilder.returnFailureJson("'删除失败'"));
            }
        }
    }
}
