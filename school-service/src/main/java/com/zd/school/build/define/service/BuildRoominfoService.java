package com.zd.school.build.define.service;

import com.zd.core.service.BaseService;
import com.zd.school.build.define.model.BuildRoominfo ;
import com.zd.school.plartform.system.model.SysUser;


/**
 * 
 * ClassName: BuildRoominfoService
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 教室信息实体Service接口类.
 * date: 2016-08-23
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
 
public interface BuildRoominfoService extends BaseService<BuildRoominfo> {


   public Boolean batchAddRoom(BuildRoominfo roominfo, SysUser currentUser);
}