package com.zd.school.plartform.comm.service;

import java.util.List;

import com.zd.core.model.BaseEntity;
import com.zd.core.service.BaseService;
import com.zd.school.plartform.comm.model.CommTree;
import com.zd.school.plartform.comm.model.FacultyClassTree;
import com.zd.school.plartform.comm.model.UpGradeRule;

/**
 * 
 * ClassName: BaseDicitemService Function: TODO ADD FUNCTION. Reason: TODO ADD
 * REASON(可选). Description: 数据字典项实体Service接口类. date: 2016-07-19
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */

public interface CommTreeService extends BaseService<BaseEntity> {

    /**
     * 
     * getFacultyClassTree:获取系别-专业-班级的树.
     *
     * @author luoyibo
     * @param whereSql
     *            过滤条件
     * @return List<FacultyClassTree>
     * @throws @since
     *             JDK 1.8
     */
    public List<FacultyClassTree> getFacultyClassTree(String whereSql);

    public List<CommTree> getCommTree(String treeView, String whereSql);

    public List<UpGradeRule> getUpGradeRuleList();
}