
package com.zd.school.build.allot.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
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
import com.zd.school.build.allot.model.DormStudentDorm;
import com.zd.school.build.allot.model.JwClassRoomAllot ;
import com.zd.school.build.allot.service.JwClassRoomAllotService ;
import com.zd.school.plartform.comm.model.CommTree;
import com.zd.school.plartform.comm.service.CommTreeService;
import com.zd.school.plartform.system.model.SysUser;

/**
 * 
 * ClassName: JwClassroomallotController
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: JW_T_CLASSROOMALLOT实体Controller.
 * date: 2016-08-23
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Controller
@RequestMapping("/JwClassRoomAllot")
public class JwClassRoomAllotController extends FrameWorkController<JwClassRoomAllot> implements Constant {

    @Resource
    JwClassRoomAllotService thisService; // service层接口

    
    @Resource
    CommTreeService treeService;	//生成树

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
    public void list(@ModelAttribute JwClassRoomAllot entity, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String strData = ""; // 返回给js的数据
        QueryResult<JwClassRoomAllot> qr = thisService.getPaginationQuery(super.start(request), super.limit(request),
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
	public void treelist(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String strData = "";
		String whereSql = request.getParameter("whereSql");
		List<CommTree> lists = treeService.getCommTree("JW_CLASSROOMALLOTTREE", whereSql);
		strData = JsonBuilder.getInstance().buildList(lists, "");// 处理数据
		writeJSON(response, strData);// 返回数据
	}
    
    /**
     * 
      * @Title: 增加新实体信息至数据库
      * @Description: TODO
      * @param @param JwClassroomallot 实体类
      * @param @param request
      * @param @param response
      * @param @throws IOException    设定参数
      * @return void    返回类型
      * @throws
     */
    @RequestMapping("/doadd")
    public void doAdd(JwClassRoomAllot entity, HttpServletRequest request, HttpServletResponse response)
            throws IOException, IllegalAccessException, InvocationTargetException {
    	  if(entity.getRoomId()==null || entity.getRoomId().equals("")){
              writeJSON(response, JsonBuilder.getInstance().returnFailureJson(
                      "'房间主键不能为空。'"));
              return;
          }
          boolean cz = thisService.IsFieldExist("roomId",entity.getRoomId(),"-1", "isDelete=0");
          if (cz) {
              writeJSON(response, jsonBuilder.returnFailureJson("'请勿重复添加。'"));
              return;
          }
          String userCh = "超级管理员";//中文名
          SysUser currentUser = getCurrentSysUser();
          if (currentUser != null)
              userCh = currentUser.getXm();
          //生成默认的orderindex
          Integer orderIndex = thisService.getDefaultOrderIndex(entity);
          DormStudentDorm perEntity = new DormStudentDorm();
          BeanUtils.copyPropertiesExceptNull(entity, perEntity);
          entity.setCreateUser(userCh); //创建人的中文名
          entity.setOrderIndex(orderIndex);//排序
          thisService.doMerge(entity); // 执行添加方法
          writeJSON(response, jsonBuilder.returnSuccessJson("'成功'"));// 返回数据到js
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
            boolean flag = thisService.doLogicDelOrRestore(delIds, StatuVeriable.ISDELETE);
            if (flag) {
                writeJSON(response, jsonBuilder.returnSuccessJson("'删除成功'"));
            } else {
                writeJSON(response, jsonBuilder.returnFailureJson("'删除失败'"));
            }
        }
    }
}
