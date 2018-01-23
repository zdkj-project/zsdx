package com.zd.school.jw.train.service.Impl;

import com.zd.core.model.extjs.QueryResult;
import com.zd.core.service.BaseServiceImpl;
import com.zd.core.util.BeanUtils;
import com.zd.core.util.EntityUtil;
import com.zd.core.util.StringUtils;
import com.zd.school.jw.train.dao.TrainCourseinfoDao;
import com.zd.school.jw.train.model.TrainCourseEval;
import com.zd.school.jw.train.model.TrainCoursecategory;
import com.zd.school.jw.train.model.TrainCourseinfo;
import com.zd.school.jw.train.model.TrainTeacher;
import com.zd.school.jw.train.model.TrainTrainee;
import com.zd.school.jw.train.service.TrainCoursecategoryService;
import com.zd.school.jw.train.service.TrainCourseinfoService;
import com.zd.school.jw.train.service.TrainTeacherService;
import com.zd.school.plartform.baseset.model.BaseDicitem;
import com.zd.school.plartform.baseset.service.BaseDicitemService;
import com.zd.school.plartform.system.model.SysUser;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.*;

/**
 * ClassName: TrainCourseinfoServiceImpl
 * Function:  ADD FUNCTION.
 * Reason:  ADD REASON(可选).
 * Description: 课程信息(TRAIN_T_COURSEINFO)实体Service接口实现类.
 * date: 2017-03-07
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Service
@Transactional
public class TrainCourseinfoServiceImpl extends BaseServiceImpl<TrainCourseinfo> implements TrainCourseinfoService {

    @Resource
    public void setTrainCourseinfoDao(TrainCourseinfoDao dao) {
        this.dao = dao;
    }

    @Resource
    private TrainTeacherService teacherService;

    @Resource
    private TrainCoursecategoryService coursecategoryService;

    @Resource
    private BaseDicitemService dicitemService;


    private static Logger logger = Logger.getLogger(TrainCourseinfoServiceImpl.class);

    @Override
    public QueryResult<TrainCourseinfo> list(Integer start, Integer limit, String sort, String filter, Boolean isDelete) {
        QueryResult<TrainCourseinfo> qResult = this.doPaginationQuery(start, limit, sort, filter, isDelete);
        return qResult;
    }

    /**
     * 根据主键逻辑删除数据
     *
     * @param ids         要删除数据的主键
     * @param currentUser 当前操作的用户
     * @return 操作成功返回true，否则返回false
     */
    @Override
    public Boolean doLogicDeleteByIds(String ids, SysUser currentUser) {
        Boolean delResult = false;
        try {
            Object[] conditionValue = ids.split(",");
            String[] propertyName = {"isDelete", "updateUser", "updateTime"};
            Object[] propertyValue = {1, currentUser.getXm(), new Date()};
            this.updateByProperties("uuid", conditionValue, propertyName, propertyValue);
            delResult = true;
        } catch (Exception e) {
            logger.error(e.getMessage());
            delResult = false;
        }
        return delResult;
    }

    /**
     * 根据传入的实体对象更新数据库中相应的数据
     *
     * @param entity      传入的要更新的实体对象
     * @param currentUser 当前操作用户
     * @return
     */
    @Override
    public TrainCourseinfo doUpdateEntity(TrainCourseinfo entity, SysUser currentUser) {
        // 先拿到已持久化的实体
        TrainCourseinfo saveEntity = this.get(entity.getUuid());
        String oldCategoryId =saveEntity.getCategoryId();
        try {
            List<String> excludedProp = new ArrayList<>();
            excludedProp.add("uuid");
            excludedProp.add("courseCode");
            excludedProp.add("orderIndex");
            if(oldCategoryId!=entity.getCategoryId()){
                //如果修改了分类，重新生成课程的编码
                String codeAndOrder = this.getCourseCodeAndOrderIndex(entity.getCategoryId(), entity.getCategoryCode(), entity.getCourseName());
                String[] temp = codeAndOrder.split(",");
                saveEntity.setCourseCode(temp[0]);
                saveEntity.setOrderIndex(Integer.valueOf(temp[1]));
            }
            BeanUtils.copyPropertiesExceptNull(saveEntity, entity, excludedProp);
            saveEntity.setUpdateTime(new Date()); // 设置修改时间
            saveEntity.setUpdateUser(currentUser.getXm()); // 设置修改人的中文名
            entity = this.merge(saveEntity);// 执行修改方法

            return entity;
        } catch (IllegalAccessException e) {
            logger.error(e.getMessage());
            return null;
        } catch (InvocationTargetException e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    /**
     * 将传入的实体对象持久化到数据
     *
     * @param entity      传入的要更新的实体对象
     * @param currentUser 当前操作用户
     * @return
     */
    @Override
    public TrainCourseinfo doAddEntity(TrainCourseinfo entity, SysUser currentUser) {
        String categoryCode = entity.getCategoryCode(); //所属分类编码
        String categoryId = entity.getCategoryId(); //所属分类的ID
        String courseName = entity.getCourseName();
        Integer courseMode = entity.getCourseMode(); //授课模式 1-单一授课 2-团队授课
        TrainCourseinfo saveEntity = null;
        String[] mainTeacherId = new String[0];
        String[] mainTeacherName = new String[0];
        List<String> excludedProp = new ArrayList<>();
        excludedProp.add("uuid");
        try {
            if (courseMode == 2) {
                //如果是团队授课，是不拆分多个教师的
                saveEntity = new TrainCourseinfo();
                BeanUtils.copyPropertiesExceptNull(saveEntity, entity, excludedProp);

                String codeAndOrder = this.getCourseCodeAndOrderIndex(categoryId, categoryCode, courseName);
                String[] temp = codeAndOrder.split(",");
                saveEntity.setCourseCode(temp[0]);
                saveEntity.setOrderIndex(Integer.valueOf(temp[1]));
                entity = this.merge(saveEntity);// 执行修改方法

                return entity;
            } else {
                //如果是单一授课,则多个教师要进行拆分
                mainTeacherId = entity.getMainTeacherId().split(",");
                mainTeacherName = entity.getMainTeacherName().split(",");
                for (Integer i = 0; i < mainTeacherName.length; i++) {
                    saveEntity = new TrainCourseinfo();
                    BeanUtils.copyPropertiesExceptNull(saveEntity, entity, excludedProp);
                    if (mainTeacherId != null || mainTeacherId.length > 0) {
                        saveEntity.setMainTeacherId(mainTeacherId[i]);
                    }
                    saveEntity.setMainTeacherName(mainTeacherName[i]);
                    saveEntity.setCreateUser(currentUser.getXm()); // 设置修改人的中文名

                    String codeAndOrder = this.getCourseCodeAndOrderIndex(categoryId, categoryCode, courseName);
                    String[] temp = codeAndOrder.split(",");
                    saveEntity.setCourseCode(temp[0]);
                    saveEntity.setOrderIndex(Integer.valueOf(temp[1]));
                    entity = this.merge(saveEntity);// 执行修改方法
                }
                return entity;
            }
        } catch (IllegalAccessException e) {
            logger.error(e.getMessage());
            return null;
        } catch (InvocationTargetException e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    @Override
    public List<TrainTeacher> getCourseTeacherList(String courseId) {
        TrainCourseinfo courseinfo = this.get(courseId);
        Object[] propValue = courseinfo.getMainTeacherId().split(",");
        List<TrainTeacher> list = teacherService.queryByProerties("uuid", propValue);

        return list;

    }

    @Override
    public void doImportCourse(List<List<Object>> listCourse, SysUser currentUser) {
        String categoryId = null;
        String categoryCode = null;
        String teachType = null;
        String teacherId = null;
        String teacherName = null;
        String courseName=null;
        //所有的课程分类
        Map<String, TrainCoursecategory> mapCoursecategory = new HashMap<>();
        List<TrainCoursecategory> lisCourSecategory = coursecategoryService.doQueryAll();
        for (TrainCoursecategory trainCoursecategory : lisCourSecategory) {
            mapCoursecategory.put(trainCoursecategory.getNodeText(), trainCoursecategory);
        }
        //所有的教学形式字典项
        Map<String, String> mapTeachType = new HashMap<>();
        String hql = " from BaseDicitem where dicCode='TEACHTYPE'";
        List<BaseDicitem> listTeachType = dicitemService.doQuery(hql);
        for (BaseDicitem baseDicitem : listTeachType) {
            mapTeachType.put(baseDicitem.getItemName(), baseDicitem.getItemCode());
        }

        //所有的教师
        Map<String, String> mapTeacher = new HashMap<>();
        hql = " from TrainTeacher where isDelete=0";
        List<TrainTeacher> listTeacher = teacherService.doQuery(hql);
        for (TrainTeacher trainTeacher : listTeacher) {
            mapTeacher.put(trainTeacher.getXm(), trainTeacher.getUuid());
        }

        //导入数据
        TrainCourseinfo course=null;
        boolean isExist=true;
        for (int i = 0; i < listCourse.size(); i++) {
            List<Object> lo = listCourse.get(i);
            
            //分类找不到，则不导入
            TrainCoursecategory  tcc=mapCoursecategory.get(lo.get(1));
            if(tcc==null)
            	continue;
            
            categoryId = mapCoursecategory.get(lo.get(1)).getUuid();
            categoryCode = mapCoursecategory.get(lo.get(1)).getNodeCode();
            teachType = mapTeachType.get(lo.get(2));
            //teacherId = mapTeacher.get(lo.get(6));
            teacherName = lo.get(5).toString();
            teacherId = this.getMainTeacherid(mapTeacher, teacherName);
            courseName= String.valueOf(lo.get(0));
            
            //没有对应的教师，该课程不导入
            if (StringUtils.isEmpty(teacherId))
            	continue;
            	
           
        	// 查询课程表中是否存在 课程名、教师名一致的课程；若存在，则替换
			course = this.getByProerties(
					new String[] { "courseName", "mainTeacherId", "isDelete" },
					new Object[] { courseName, teacherId, 0 });
			
			// 如果存在，则替换；否则新建
			if (course == null) {
				isExist = false;
				course = new TrainCourseinfo();
				course.setCreateUser(currentUser.getXm()); // 设置修改人的中文名
				course.setCourseName(courseName);
				course.setMainTeacherName(String.valueOf(teacherName));
	            course.setMainTeacherId(teacherId);
	            //导入时不做多老师的拆分
	            course.setCourseMode(2);
	            course.setPeriod(1);
	            
			}else{
				isExist=true;
			}
			
            course.setCategoryId(categoryId);
            course.setCategoryCode(categoryCode);
            course.setTeachType(teachType);
            //course.setCourseCode(String.valueOf(lo.get(1)));          
            course.setPeriodTime(Integer.parseInt(lo.get(3).toString()));
            course.setCredits(Integer.parseInt(lo.get(4).toString()));
            course.setCourseDesc(String.valueOf(lo.get(6)));
          
            course.setUpdateTime(new Date());
            course.setUpdateUser(currentUser.getXm());
            
            if(isExist==true)
            	this.merge(course);
            else
                this.doAddEntity(course, currentUser);
            	
        }
    }

    @Override
    public List<TrainCourseinfo> listExport(String ids, String orderSql) {
        List<TrainCourseinfo> list = null;
        List<TrainCourseinfo> exportList = new ArrayList<>();
        String hql = " from TrainCourseinfo where isDelete=0 ";
        if (StringUtils.isNotEmpty(ids)) {
            hql += " and uuid in ('" + ids.replace(",", "','") + "')";
        }
        if ("".equals(orderSql))
            hql += " order by categoryOrderindex,courseCode";
        else
            hql += orderSql;

        list = this.doQuery(hql);
        for (TrainCourseinfo trainCourseinfo : list) {
            trainCourseinfo.setCourseDesc(StringUtils.HtmlToText(trainCourseinfo.getCourseDesc()));
        }
        exportList.addAll(list);
        return list;
    }

    @Override
    public List<TrainCourseEval> getCouseEvalList(Integer start, Integer limit, String sort, String filter, Boolean isDelete, String courseId) {
        return null;
    }

    @Override
    public String parseCouserseDesc(String htmlStr) {
        return StringUtils.HtmlToText(htmlStr);
    }

    private String getCourseCodeAndOrderIndex(String categoryId, String categoryCode, String courseName) {
        Integer orderIndex = Integer.valueOf(1);
        String courseCode = "";
        //得到排序号
        String hql = " from  TrainCourseinfo  where orderIndex=(select max(orderIndex) from TrainCourseinfo where categoryId=''{0}'')";
        hql = MessageFormat.format(hql, categoryId);
        List<TrainCourseinfo> list = this.doQuery(hql);
        if (list.size() > 0) {
            orderIndex = (Integer) EntityUtil.getPropertyValue(list.get(0), "orderIndex") + 1;
        } else
            orderIndex = 1;

        //先检查同一分类下是否已有此课程名，如有，则使用此编码
        hql = " from TrainCourseinfo where categoryId=''{0}'' and courseName=''{1}'' ";
        hql = MessageFormat.format(hql, categoryId, courseName);
        List<TrainCourseinfo> listCourse = this.doQuery(hql);
        if (listCourse.size() > 0) {
            //有此课程了,直接使用此编号
            courseCode = listCourse.get(0).getCourseCode();
        } else {
            //要重新生成编号
            courseCode = categoryCode + StringUtils.addString(orderIndex.toString(), "0", 3, "L");
        }
        return courseCode + "," + orderIndex.toString();
    }

    private String getMainTeacherid(Map<String, String> mapTeacher, String mainTeacherName) {
        String[] listTeacherName = mainTeacherName.split(",");
        StringBuilder sb = new StringBuilder();
        Integer iLenth = listTeacherName.length;
        String teacherId = "";
        if (iLenth > 1) {
            for (int i = 0; i < listTeacherName.length; i++) {
                sb.append(mapTeacher.get(listTeacherName[i]));
                sb.append(",");
            }
            sb = sb.deleteCharAt(sb.length() - 1);
            teacherId = sb.toString();
        } else {
            teacherId = mapTeacher.get(mainTeacherName);
        }
        return  teacherId;
    }

}