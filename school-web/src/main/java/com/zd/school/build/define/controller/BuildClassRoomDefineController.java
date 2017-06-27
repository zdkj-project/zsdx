
package com.zd.school.build.define.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zd.core.constant.Constant;
import com.zd.core.controller.core.FrameWorkController;
import com.zd.core.model.extjs.QueryResult;
import com.zd.core.util.BeanUtils;
import com.zd.core.util.StringUtils;
import com.zd.school.build.allot.service.JwClassRoomAllotService;
import com.zd.school.build.define.model.BuildClassRoomDefine;
import com.zd.school.build.define.model.BuildRoominfo;
import com.zd.school.build.define.service.BuildClassRoomDefineService ;
import com.zd.school.build.define.service.BuildRoominfoService;
import com.zd.school.plartform.system.model.SysUser;

/**
 * 
 * ClassName: BuildClassroomController
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 教室定义Controller
 * date: 2016-08-23
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Controller
@RequestMapping("/BuildClassRoomDefine")
public class BuildClassRoomDefineController extends FrameWorkController<BuildClassRoomDefine> implements Constant {

    @Resource
    BuildClassRoomDefineService thisService; // service层接口
    
    @Resource
    JwClassRoomAllotService classRoomAllotService;//教室分配
    
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
    public void list(@ModelAttribute BuildClassRoomDefine entity, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String strData = ""; // 返回给js的数据
        QueryResult<BuildClassRoomDefine> qr = thisService.doPaginationQuery(super.start(request), super.limit(request),
                super.sort(request), super.filter(request), true);

        strData = jsonBuilder.buildObjListToJson(qr.getTotalCount(), qr.getResultList(), true);// 处理数据
        writeJSON(response, strData);// 返回数据
    }

    /**
     * 
      * @Title: 增加新实体信息至数据库
      * @Description: TODO
      * @param @param BuildClassroom 实体类
      * @param @param request
      * @param @param response
      * @param @throws IOException    设定参数
      * @return void    返回类型
      * @throws
     */
    @RequestMapping("/doadd")
    public void doAdd(BuildClassRoomDefine entity, HttpServletRequest request, HttpServletResponse response)
            throws IOException, IllegalAccessException, InvocationTargetException {
    	String userCh = "超级管理员";//中文名
        SysUser currentUser = getCurrentSysUser();
        if (currentUser != null)
            userCh = currentUser.getXm();
        String[] ids = entity.getRoomId().split(",");
        if (ids != null)
            for (int i = 0; i < ids.length; i++) {
                boolean cz = thisService.IsFieldExist("roomId", ids[i], "-1", "isdelete=0");
                if (cz) {
                    writeJSON(response, jsonBuilder.returnFailureJson("'请勿重复添加。'"));
                    return;
                }
                BuildClassRoomDefine perEntity = new BuildClassRoomDefine();
        		BeanUtils.copyPropertiesExceptNull(entity, perEntity);
                //生成默认的orderindex
                Integer orderIndex = thisService.getDefaultOrderIndex(entity);
                entity.setRoomId(ids[i]);//设置房间id
                entity.setCreateUser(userCh); //创建人
                entity.setUpdateUser(userCh); //创建人的中文名
                entity.setOrderIndex(orderIndex);//排序
                thisService.merge(entity); // 执行添加方法
                
                BuildRoominfo info = infoService.get(ids[i]);
                info.setUpdateTime(new Date());
                info.setUpdateUser(userCh);
                info.setRoomType("3");//设置房间类型为教室--1.宿舍，2.办公室，3.教室，4、实验室，5、功能室，9、其它，0、未分配
                info.setAreaStatu(1);//设置为已分配
                //执行更新方法
                infoService.merge(info);
            }
        writeJSON(response, jsonBuilder.returnSuccessJson("'设置成功。'"));
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
        int count=0;
        int fs=0;
        if (StringUtils.isEmpty(delIds)) {
            writeJSON(response, jsonBuilder.returnSuccessJson("'没有传入删除主键'"));
            return;
        } else {
            String[] ids = delIds.split(",");
            BuildRoominfo roominfo = null;
            BuildClassRoomDefine jwTClassroom = null;
            for (int j = 0; j < ids.length; j++) {
                jwTClassroom = thisService.get(ids[j]);
                boolean cz = classRoomAllotService.IsFieldExist("roomId", jwTClassroom.getRoomId(), "-1", "isdelete=0");
                if (cz) {
                    ++count;
                }
                if(count==0){
                    roominfo = infoService.get(jwTClassroom.getRoomId());
                    roominfo.setUpdateTime(new Date());
                    roominfo.setRoomType("0");//设置房间类型为空
                    roominfo.setAreaStatu(0);//设置房间状态为未分配
                    //执行更新方法
                    infoService.merge(roominfo);
                    jwTClassroom.setIsDelete(1);
                    thisService.merge(jwTClassroom);
                    ++fs;
                }
                count=0;
            }
            if (fs>0) {
                writeJSON(response, jsonBuilder.returnSuccessJson("'教室已分配班级的未删除，教室未分配的已删除。'"));
            } else {
                writeJSON(response, jsonBuilder.returnFailureJson("'教室都已分配给各班级，不允许删除。'"));
            }

        }
    }
}
