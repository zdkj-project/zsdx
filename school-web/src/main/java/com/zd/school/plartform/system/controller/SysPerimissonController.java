
package com.zd.school.plartform.system.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zd.core.constant.Constant;
import com.zd.core.controller.core.FrameWorkController;
import com.zd.school.plartform.system.model.SysPermission;
import com.zd.school.plartform.system.service.SysPerimissonService;

/**
 * 
 * ClassName: BaseTPerimissonController Function: TODO ADD FUNCTION. Reason:
 * TODO ADD REASON(可选). Description: 权限表实体Controller. date: 2016-07-17
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Controller
@RequestMapping("/BasePerimisson")
public class SysPerimissonController extends FrameWorkController<SysPermission> implements Constant {

    @Resource
    SysPerimissonService thisService; // service层接口

}
