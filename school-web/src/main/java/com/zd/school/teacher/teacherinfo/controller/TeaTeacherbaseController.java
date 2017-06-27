
package com.zd.school.teacher.teacherinfo.controller;

import com.zd.core.constant.Constant;
import com.zd.core.constant.StatuVeriable;
import com.zd.core.controller.core.FrameWorkController;
import com.zd.core.model.extjs.QueryResult;
import com.zd.core.util.*;
import com.zd.school.jw.controller.util.ExcelUtil;
import com.zd.school.jw.eduresources.model.JwTBasecourse;
import com.zd.school.jw.eduresources.service.JwTBasecourseService;
import com.zd.school.plartform.baseset.model.BaseDicitem;
import com.zd.school.plartform.baseset.model.BaseJob;
import com.zd.school.plartform.baseset.service.BaseDicitemService;
import com.zd.school.plartform.baseset.service.BaseJobService;
import com.zd.school.plartform.baseset.service.BaseOrgService;
import com.zd.school.plartform.system.model.SysRole;
import com.zd.school.plartform.system.model.SysUser;
import com.zd.school.plartform.system.service.SysRoleService;
import com.zd.school.plartform.system.service.SysUserService;
import com.zd.school.teacher.teacherinfo.model.TeaTeacherbase;
import com.zd.school.teacher.teacherinfo.service.TeaTeacherbaseService;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 
 * ClassName: TeaTeacherbaseController Function: TODO ADD FUNCTION. Reason: TODO
 * ADD REASON(可选). Description: 教职工基本数据实体Controller. date: 2016-07-19
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Controller
@RequestMapping("/teacher")
public class TeaTeacherbaseController extends FrameWorkController<TeaTeacherbase> implements Constant {

    @Resource
    TeaTeacherbaseService thisService; // service层接口

    @Resource
    SysUserService userService;

    @Resource
    SysRoleService roleService;

    @Resource
    private JwTBasecourseService bcourseService;

    @Resource
    private BaseOrgService orgService;

    @Resource
    private BaseDicitemService dicItemService;

    @Resource
    private BaseJobService jobservice;

    /**
     * list查询 @Title: list @Description: TODO @param @param entity
     * 实体类 @param @param request @param @param response @param @throws
     * IOException 设定参数 @return void 返回类型 @throws
     */
    @RequestMapping(value = { "/list" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
            org.springframework.web.bind.annotation.RequestMethod.POST })
    public void list(@ModelAttribute TeaTeacherbase entity, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String strData = ""; // 返回给js的数据
        String deptId = request.getParameter("deptId");
        SysUser currentUser = getCurrentSysUser();
        //        QueryResult<TeaTeacherbase> qr = thisService.doPaginationQuery(super.start(request), super.limit(request),
        //                super.sort(request), super.filter(request), true);

        QueryResult<TeaTeacherbase> qr = thisService.getDeptTeacher(super.start(request), super.limit(request),
                super.sort(request), super.filter(request), super.whereSql(request), super.orderSql(request),
                super.querySql(request), true, deptId, currentUser);

        strData = jsonBuilder.buildObjListToJson(qr.getTotalCount(), qr.getResultList(), true);// 处理数据
        writeJSON(response, strData);// 返回数据
    }

    /**
     * 公用的教师查询（用于办公室分配使用）
     * 
     * @param entity
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = { "/officeAllot" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
            org.springframework.web.bind.annotation.RequestMethod.POST })
    public void officeAllot(@ModelAttribute TeaTeacherbase entity, HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        String strData = ""; // 返回给js的数据
        // hql语句
        StringBuffer hql = new StringBuffer("from " + entity.getClass().getSimpleName() + "");
        // 总记录数
        StringBuffer countHql = new StringBuffer("select count(*) from " + entity.getClass().getSimpleName() + "");
        String whereSql = entity.getWhereSql();// 查询条件
        String parentSql = entity.getParentSql();// 条件
        String querySql = entity.getQuerySql();// 查询条件
        String orderSql = entity.getOrderSql();// 排序
        hql.append(whereSql);
        hql.append(parentSql);
        hql.append(querySql);
        hql.append(orderSql);
        countHql.append(whereSql);
        countHql.append(querySql);
        countHql.append(parentSql);
        List<TeaTeacherbase> lists = thisService.doQuery(hql.toString(), super.start(request), entity.getLimit());// 执行查询方法
        Integer count = thisService.getCount(countHql.toString());// 查询总记录数
        strData = jsonBuilder.buildObjListToJson(new Long(count), lists, true);// 处理数据
        writeJSON(response, strData);// 返回数据
    }

    /**
     * 
     * @throws BadHanyuPinyinOutputFormatCombination
     * @Title: 增加新实体信息至数据库 @Description: TODO @param @param TeaTeacherbase
     *         实体类 @param @param request @param @param response @param @throws
     *         IOException 设定参数 @return void 返回类型 @throws
     */
    @RequestMapping("/doadd")
    public void doAdd(@RequestParam MultipartFile file, TeaTeacherbase entity, HttpServletRequest request,
            HttpServletResponse response) throws IOException, IllegalAccessException, InvocationTargetException,
            BadHanyuPinyinOutputFormatCombination {
        if (!file.isEmpty()) {
            // 重命名上传后的文件名
            String myFileName = file.getOriginalFilename();
            String type = myFileName.substring(myFileName.lastIndexOf("."));
            String fileName = entity.getUserNumb() + "_" + String.valueOf(System.currentTimeMillis()) + type;

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            String url = "/static/upload/zp/" + sdf.format(System.currentTimeMillis()) + "/";

            String rootPath = request.getSession().getServletContext().getRealPath("/");
            rootPath = rootPath.replace("\\", "/");

            // 定义上传路径
            String path = rootPath + url + fileName;
            File localFile = new File(path);

            if (!localFile.exists()) { // 判断文件夹是否存在
                localFile.mkdirs(); // 不存在则创建
            }

            file.transferTo(localFile);
            entity.setZp(url + fileName);
        }
        //此处为放在入库前的一些检查的代码，如唯一校验等
        String xm = entity.getXm();
        String userName = PinyinUtil.toPinYin(xm);
        //获取当前操作用户
        String userCh = "超级管理员";
        SysUser currentUser = getCurrentSysUser();
        if (currentUser != null)
            userCh = currentUser.getXm();

        TeaTeacherbase perEntity = new TeaTeacherbase();
        BeanUtils.copyPropertiesExceptNull(entity, perEntity);

        //默认的角色为教师
        Set<SysRole> theUserRole = entity.getSysRoles();
        SysRole defaultRole = roleService.get("5D667F9F-C20B-486B-A456-73F62D9885ED");
        theUserRole.add(defaultRole);

        Integer orderIndex = thisService.getDefaultOrderIndex(entity);
        entity.setOrderIndex(orderIndex);//排序
        entity.setCategory("1");
        //entity.setJobId("662f266f-6c5b-420d-ac1b-0f4e5ce44cd6");
        //entity.setJobName("教师");
        entity.setState("1");
        entity.setIsDelete(0);
        entity.setIsHidden("0");
        entity.setIssystem(1);
        entity.setUserPwd(new Sha256Hash("123456").toHex());
        //entity.setDeptId("058b21fe-b37f-41c9-ad71-091f97201ff8");
        //entity.setDeptName("临时部门");
        entity.setSchoolId("2851655E-3390-4B80-B00C-52C7CA62CB39");
        entity.setSchoolName("深大附中");
        entity.setUserName(userName);
        entity.setSysRoles(theUserRole);
        //增加时要设置创建人
        entity.setCreateUser(userCh); //创建人

        //持久化到数据库
        entity = thisService.merge(entity);

        //返回实体到前端界面
        writeJSON(response, jsonBuilder.returnSuccessJson(jsonBuilder.toJson(entity)));
    }

    /**
     * doDelete @Title: 逻辑删除指定的数据 @Description: TODO @param @param
     * request @param @param response @param @throws IOException 设定参数 @return
     * void 返回类型 @throws
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

    /**
     * doUpdate编辑记录 @Title: doUpdate @Description: TODO @param @param
     * TeaTeacherbase @param @param request @param @param
     * response @param @throws IOException 设定参数 @return void 返回类型 @throws
     */
    @RequestMapping("/doupdate")
    public void doUpdates(@RequestParam MultipartFile file, TeaTeacherbase entity, HttpServletRequest request,
            HttpServletResponse response) throws IOException, IllegalAccessException, InvocationTargetException {

        if (!file.isEmpty()) {
            // 重命名上传后的文件名
            String myFileName = file.getOriginalFilename();
            String type = myFileName.substring(myFileName.lastIndexOf("."));
            String fileName = entity.getUserNumb() + "_" + String.valueOf(System.currentTimeMillis()) + type;

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            String url = "/static/upload/zp/" + sdf.format(System.currentTimeMillis()) + "/";

            String rootPath = request.getSession().getServletContext().getRealPath("/");
            rootPath = rootPath.replace("\\", "/");

            // 定义上传路径
            String path = rootPath + url + fileName;
            File localFile = new File(path);

            if (!localFile.exists()) { // 判断文件夹是否存在
                localFile.mkdirs(); // 不存在则创建
            }

            file.transferTo(localFile);
            entity.setZp(url + fileName);
        }

        //获取当前的操作用户
        String userCh = "超级管理员";
        SysUser currentUser = getCurrentSysUser();
        if (currentUser != null)
            userCh = currentUser.getXm();

        //先拿到已持久化的实体
        //entity.getSchoolId()要自己修改成对应的获取主键的方法
        TeaTeacherbase perEntity = thisService.get(entity.getUuid());

        //将entity中不为空的字段动态加入到perEntity中去。
        BeanUtils.copyPropertiesExceptNull(perEntity, entity);

        perEntity.setUpdateTime(new Date()); //设置修改时间
        perEntity.setUpdateUser(userCh); //设置修改人的中文名
        entity = thisService.merge(perEntity);//执行修改方法

        writeJSON(response, jsonBuilder.returnSuccessJson(jsonBuilder.toJson(perEntity)));

    }

    //上传excel
    @RequestMapping("/addExcel")
    public ModelAndView addExcel(@RequestParam MultipartFile file, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        if (!file.isEmpty()) {
            // 重命名上传后的文件名
            String myFileName = file.getOriginalFilename();
            String type = myFileName.substring(myFileName.lastIndexOf("."));
            String fileName = String.valueOf(System.currentTimeMillis()) + type;

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            String url = "/static/upload/file/" + sdf.format(System.currentTimeMillis()) + "/";

            String rootPath = request.getSession().getServletContext().getRealPath("/");
            rootPath = rootPath.replace("\\", "/");

            // 定义上传路径
            String path = rootPath + url + fileName;
            File localFile = new File(path);

            if (!localFile.exists()) { // 判断文件夹是否存在
                localFile.mkdirs(); // 不存在则创建
            }

            file.transferTo(localFile);

            /*
             * ModelMap model = new ModelMap(); model.put("filePath",path);
             * return new ModelAndView("redirect:/teacher/doimport",model);
             */
            excelImport(path, request, response);
        }
        return null;
    }

    //导入数据库
    //@RequestMapping("/doimport")
    public void excelImport(String filePath, HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        try {
            //获取当前的操作用户
            String userCh = "超级管理员";
            SysUser currentUser = getCurrentSysUser();
            if (currentUser != null)
                userCh = currentUser.getXm();
            String schoolId = currentUser.getSchoolId();
            String hql = " o.isDelete='0' and o.schoolId='" + schoolId + "'";
            // 1、创建excel导入工具
            ExcelUtil excelUtil = new ExcelUtil();
            // 2、创建时间格式
            //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            // 3、通过工具获取教师数据
            List<List<String>> teaTeacherbaseData = excelUtil.importExcel(filePath);
            // 4、创建教师集合
            List<String> teaTeacherbaseList = null;
            // 5、创建教师对象
            TeaTeacherbase entity = null;

            // 6、通过循环将数据放入教师集合中，从集合中获取教师对象，一一设置属性
            for (int i = 0; i < teaTeacherbaseData.size(); i++) {
                teaTeacherbaseList = teaTeacherbaseData.get(i);
                entity = new TeaTeacherbase();
                entity.setUserNumb(teaTeacherbaseList.get(0));
                entity.setXm(teaTeacherbaseList.get(1));
                String sex = null;
                if (teaTeacherbaseList.get(2).equals("男")) {
                    sex = "1";
                } else if (teaTeacherbaseList.get(2).equals("女")) {
                    sex = "2";
                }
                entity.setXbm(sex);
                String idCard = teaTeacherbaseList.get(3);
                entity.setSfzjh(idCard);
                String tmpStr;
                if (StringUtils.isEmpty(idCard)) {
                    tmpStr = "";
                } else {
                    tmpStr = idCard.length() == 15 ? "19" + idCard.substring(6, 12) : idCard.substring(6, 14);
                    tmpStr = tmpStr.substring(0, 4) + "-" + tmpStr.substring(4, 6) + "-" + tmpStr.substring(6);
                }
                entity.setCsrq(DateUtil.getDate(tmpStr));

                //处理主讲课程
                String courseName = teaTeacherbaseList.get(4);
                JwTBasecourse basecourse = bcourseService.getByProerties("courseName", courseName);
                if (ModelUtil.isNotNull(basecourse)) {
                    entity.setMainCourse(basecourse.getUuid());
                } else {
                	basecourse = bcourseService.getByProerties("courseName", "自习");
                	if (ModelUtil.isNotNull(basecourse)) {
                        entity.setMainCourse(basecourse.getUuid());
                    }
                }

                //处理人员编制类型
                String bzName = teaTeacherbaseList.get(5);
                String[] propName = new String[] { "dicId", "itemName" };
                Object[] propValue = new Object[] { "513279d3-18ab-492d-b7aa-e0ea33b5ecc6", bzName };
                BaseDicitem dicItem = dicItemService.getByProerties(propName, propValue);
                if (ModelUtil.isNotNull(dicItem)) {
                    entity.setZxxbzlb(dicItem.getItemCode());
                }
                Integer orderIndex = thisService.getDefaultOrderIndex(entity);
                entity.setOrderIndex(orderIndex);//排序
                //增加时要设置创建人
                entity.setCreateUser(userCh); //创建人

                String userName = PinyinUtil.toPinYin(teaTeacherbaseList.get(1));
                //默认的角色为教师
                String[] param={"roleName","schoolId","isDelete"};
                Object[] values={"教师",schoolId,0};
                Set<SysRole> theUserRole = entity.getSysRoles();
                SysRole defaultRole = roleService.getByProerties(param, values);
                theUserRole.add(defaultRole);
                entity.setSysRoles(theUserRole);

                param=new String[]{"jobName","isDelete"};
                values=new Object[]{"教师",0};
                //默认的岗位为教师
  /*              Set<BaseJob> teaJobs = entity.getUserJobs();
                BaseJob defaultJob = jobservice.getByProerties(param, values);
                teaJobs.add(defaultJob);
                entity.setUserJobs(teaJobs);
*/
                //默认部门为临时部门
/*                Set<BaseOrg> teaOrgs = entity.getUserDepts();
                BaseOrg defaultOrg = orgService.getByProerties("nodeText", "临时部门");
                teaOrgs.add(defaultOrg);*/
                //entity.setUserDepts(teaOrgs);

                entity.setCategory("1");
                entity.setState("1");
                entity.setIsDelete(0);
                entity.setIsHidden("0");
                entity.setIssystem(1);
                entity.setUserPwd(new Sha256Hash("123456").toHex());
                entity.setSchoolId(currentUser.getSchoolId());
                entity.setSchoolName(currentUser.getSchoolName());
                entity.setUserName(userName);

                // 7、存入数据库
                this.thisService.persist(entity);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        writeJSON(response, JsonBuilder.getInstance().returnSuccessJson("'上传成功'"));
        //return new ModelAndView("redirect:/static/core/coreApp/arrangecourse/Invigilate/iframe/uploadzp.jsp");
    }

    @RequestMapping(value = { "/courseteacher" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
            org.springframework.web.bind.annotation.RequestMethod.POST })
    public void getCourseTeacherlist(@ModelAttribute TeaTeacherbase entity, HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        String strData = ""; // 返回给js的数据
        QueryResult<TeaTeacherbase> qr = thisService.getCourseTeacherlist(super.start(request), super.limit(request),
                super.sort(request), super.filter(request),super.whereSql(request),super.orderSql(request),super.querySql(request), true);

        strData = jsonBuilder.buildObjListToJson(qr.getTotalCount(), qr.getResultList(), true);// 处理数据
        writeJSON(response, strData);// 返回数据
    }

    @RequestMapping("/batchSetDept")
    public void batchSetDept(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String delIds = request.getParameter("ids");
        String deptId = request.getParameter("deptId");
        if (StringUtils.isEmpty(delIds) || StringUtils.isEmpty(deptId)) {
            writeJSON(response, jsonBuilder.returnSuccessJson("'没有传入要绑定的人员'"));
            return;
        } else {
            SysUser cuurentUser = getCurrentSysUser();
            boolean flag = thisService.batchSetDept(deptId, delIds, cuurentUser);
            if (flag)
                writeJSON(response, jsonBuilder.returnSuccessJson("'绑定成功'"));
            else
                writeJSON(response, jsonBuilder.returnSuccessJson("'绑定失败'"));
        }
    }

    @RequestMapping("/dodeldept")
    public void delTeaFromDept(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String delIds = request.getParameter("ids");
        String deptId = request.getParameter("deptId");
        if (StringUtils.isEmpty(delIds) || StringUtils.isEmpty(deptId)) {
            writeJSON(response, jsonBuilder.returnSuccessJson("'没有传入要解除绑定的人员'"));
            return;
        } else {
            SysUser cuurentUser = getCurrentSysUser();
            boolean flag = thisService.delTeaFromDept(deptId, delIds, cuurentUser);
            if (flag)
                writeJSON(response, jsonBuilder.returnSuccessJson("'解除绑定成功'"));
            else
                writeJSON(response, jsonBuilder.returnSuccessJson("'解除绑定失败'"));
        }
    }

    @RequestMapping("/setteatojob")
    public void setTeaToJob(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String delIds = request.getParameter("ids");
        String deptId = request.getParameter("jobId");
        if (StringUtils.isEmpty(delIds) || StringUtils.isEmpty(deptId)) {
            writeJSON(response, jsonBuilder.returnSuccessJson("'没有传入要绑定的人员'"));
            return;
        } else {
            SysUser cuurentUser = getCurrentSysUser();
            boolean flag = thisService.setTeaToJob(deptId, delIds, cuurentUser);
            if (flag)
                writeJSON(response, jsonBuilder.returnSuccessJson("'绑定成功'"));
            else
                writeJSON(response, jsonBuilder.returnSuccessJson("'绑定失败'"));
        }
    }

    @RequestMapping("/delteafromjob")
    public void delTeaFromJob(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String delIds = request.getParameter("ids");
        String deptId = request.getParameter("jobId");
        if (StringUtils.isEmpty(delIds) || StringUtils.isEmpty(deptId)) {
            writeJSON(response, jsonBuilder.returnSuccessJson("'没有传入要解除绑定的人员'"));
            return;
        } else {
            SysUser cuurentUser = getCurrentSysUser();
            boolean flag = thisService.delTeaFromJob(deptId, delIds, cuurentUser);
            if (flag)
                writeJSON(response, jsonBuilder.returnSuccessJson("'解除绑定成功'"));
            else
                writeJSON(response, jsonBuilder.returnSuccessJson("'解除绑定失败'"));
        }
    }

    @RequestMapping(value = { "/teacherjoblist" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
            org.springframework.web.bind.annotation.RequestMethod.POST })
    public void getTeahcerJobList(@ModelAttribute TeaTeacherbase entity, HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        String strData = ""; // 返回给js的数据
        String deptId = request.getParameter("deptId");
        SysUser currentUser = getCurrentSysUser();
        //        QueryResult<TeaTeacherbase> qr = thisService.doPaginationQuery(super.start(request), super.limit(request),
        //                super.sort(request), super.filter(request), true);

        QueryResult<BaseJob> qr = thisService.getTeahcerJobList(entity, currentUser);

        strData = jsonBuilder.buildObjListToJson(qr.getTotalCount(), qr.getResultList(), true);// 处理数据
        writeJSON(response, strData);// 返回数据
    }

    @RequestMapping(value = { "/roleteacherlist" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET,
            org.springframework.web.bind.annotation.RequestMethod.POST })
    public void roleTeacherList(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String strData = ""; // 返回给js的数据
        String roleId = request.getParameter("roleId");
        SysUser currentUser = getCurrentSysUser();
        //        QueryResult<TeaTeacherbase> qr = thisService.doPaginationQuery(super.start(request), super.limit(request),
        //                super.sort(request), super.filter(request), true);
        QueryResult<SysUser> qr = userService.getUserByRoleId(roleId);
        if (ModelUtil.isNotNull(qr))
        	strData = jsonBuilder.buildObjListToJson(qr.getTotalCount(), qr.getResultList(), true);// 处理数据
        writeJSON(response, strData);// 返回数据
    }
}
