package com.zd.school.jw.schoolcourse.service.Impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zd.core.model.extjs.QueryResult;
import com.zd.core.service.BaseServiceImpl;
import com.zd.core.util.StringUtils;
import com.zd.school.jw.schoolcourse.dao.JwCoursepublishDao;
import com.zd.school.jw.schoolcourse.model.JwCoursepublish;
import com.zd.school.jw.schoolcourse.service.JwCoursepublishService;
import com.zd.school.plartform.system.model.SysUser;

/**
 * 
 * ClassName: JwCoursepublishServiceImpl Function: TODO ADD FUNCTION. Reason:
 * TODO ADD REASON(可选). Description: 校本课程发布信息(JW_T_COURSEPUBLISH)实体Service接口实现类.
 * date: 2016-11-21
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Service
@Transactional
public class JwCoursepublishServiceImpl extends BaseServiceImpl<JwCoursepublish> implements JwCoursepublishService {

    @Resource
    public void setJwCoursepublishDao(JwCoursepublishDao dao) {
        this.dao = dao;
    }

    @Override
    public QueryResult<JwCoursepublish> list(Integer start, Integer limit, String sort, String filter, String whereSql,
            String orderSql, SysUser currentUser) {
        String sortSql = StringUtils.convertSortToSql(sort);
        String filterSql = StringUtils.convertFilterToSql(filter);

        StringBuffer hql = new StringBuffer("from JwCoursepublish o where 1=1");
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

        QueryResult<JwCoursepublish> qResult = this.getQueryResult(hql.toString(), start, limit);
        return qResult;
    }
}