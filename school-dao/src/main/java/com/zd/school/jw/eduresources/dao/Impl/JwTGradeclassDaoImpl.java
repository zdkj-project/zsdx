package com.zd.school.jw.eduresources.dao.Impl;

import org.springframework.stereotype.Repository;

import com.zd.core.dao.BaseDaoImpl;
import com.zd.school.jw.eduresources.dao.JwTGradeclassDao;
import com.zd.school.jw.eduresources.model.JwTGradeclass;

/**
 * 
 * ClassName: JwTGradeclassDaoImpl Function: TODO ADD FUNCTION. Reason: TODO ADD
 * REASON(可选). Description: 学校班级信息实体Dao接口实现类. date: 2016-03-13
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Repository
public class JwTGradeclassDaoImpl extends BaseDaoImpl<JwTGradeclass> implements JwTGradeclassDao {
    public JwTGradeclassDaoImpl() {
        super(JwTGradeclass.class);
        // TODO Auto-generated constructor stub
    }
}