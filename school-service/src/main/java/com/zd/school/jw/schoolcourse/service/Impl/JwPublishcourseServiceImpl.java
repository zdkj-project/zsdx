package com.zd.school.jw.schoolcourse.service.Impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zd.core.model.extjs.QueryResult;
import com.zd.core.service.BaseServiceImpl;
import com.zd.core.util.StringUtils;
import com.zd.school.jw.eduresources.model.JwTGrade;
import com.zd.school.jw.eduresources.service.JwTGradeService;
import com.zd.school.jw.schoolcourse.dao.JwPublishcourseDao;
import com.zd.school.jw.schoolcourse.model.JwPublishcourse;
import com.zd.school.jw.schoolcourse.service.JwPublishcourseService;
import com.zd.school.plartform.system.model.SysUser;

/**
 * 
 * ClassName: JwPublishcourseServiceImpl Function: TODO ADD FUNCTION. Reason:
 * TODO ADD REASON(可选). Description:
 * 校本课程发布课程信息(JW_T_PUBLISHCOURSE)实体Service接口实现类. date: 2016-11-21
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Service
@Transactional
public class JwPublishcourseServiceImpl extends BaseServiceImpl<JwPublishcourse> implements JwPublishcourseService {

    @Resource
    public void setJwPublishcourseDao(JwPublishcourseDao dao) {
        this.dao = dao;
    }

    @Resource
    private JwTGradeService gradeService;

    @Override
    public QueryResult<JwPublishcourse> list(Integer start, Integer limit, String sort, String filter, String whereSql,
            String orderSql, SysUser currentUser) {
        String sortSql = StringUtils.convertSortToSql(sort);
        String filterSql = StringUtils.convertFilterToSql(filter);

        StringBuffer hql = new StringBuffer("from JwPublishcourse where 1=1 ");
        hql.append(whereSql);
        hql.append(filterSql);
        if (orderSql.length() > 0) {
            if (sortSql.length() > 0)
                hql.append(orderSql + " , " + sortSql);
            else
                hql.append(orderSql);
        } else {
            if (sortSql.length() > 0)
                hql.append(" order by  " + sortSql);
        }

        QueryResult<JwPublishcourse> qResult = this.getQueryResult(hql.toString(), start, limit);
        return qResult;
    }

    @Override
    public Boolean doAdd(JwPublishcourse course, SysUser currentUser) {
    	Boolean result = false;
        String[] courseIds = course.getCourseId().split(",");
        String[] graiId = course.getGraiId().split(",");
        List<JwTGrade> grades = gradeService.queryByProerties("uuid", graiId);
        try {
            for (String ids : courseIds) {
                JwPublishcourse saveEntity = new JwPublishcourse();
                saveEntity.setPublishId(course.getPublishId());
                saveEntity.setCourseId(ids);
                saveEntity.setWeek(course.getWeek());
                saveEntity.setJc(course.getJc());
                saveEntity.setSignCount(course.getSignCount());
                saveEntity.setBeginDate(course.getBeginDate());
                saveEntity.setEndDate(course.getEndDate());
                saveEntity.setCreateUser(currentUser.getXm());
                
                Set<JwTGrade> hasGrade = new HashSet<JwTGrade>();
                hasGrade.addAll(grades);
                saveEntity.setPcourseGrade(hasGrade);
                
                this.doMerge(saveEntity);
            }
            result = true;
		} catch (Exception e) {
			// TODO
			result = false ;
		}
        
        return result;
    }
    @Override
    public Boolean doUpdate(JwPublishcourse course, SysUser currentUser) {
    	Boolean result = false;
        String[] courseIds = course.getCourseId().split(",");
        String[] graiId = course.getGraiId().split(",");
        List<JwTGrade> grades = gradeService.queryByProerties("uuid", graiId);
        try {
            for (String ids : courseIds) {
            	course.setCreateUser(currentUser.getXm());
            	course.setCourseId(ids);
                Set<JwTGrade> hasGrade = new HashSet<JwTGrade>();
                hasGrade.addAll(grades);
                course.setPcourseGrade(hasGrade);
                
                this.doMerge(course);
            }
            result = true;
		} catch (Exception e) {
			// TODO
			result = false ;
		}
        
        return result;
    }
}