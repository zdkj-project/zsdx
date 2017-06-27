package com.zd.school.jw.eduresources.dao.Impl;

import org.springframework.stereotype.Repository;

import com.zd.core.dao.BaseDaoImpl;
import com.zd.school.jw.eduresources.dao.JwTGradeDao;
import com.zd.school.jw.eduresources.model.JwTGrade;

/**
 * 
 * ClassName: JwTGradeDaoImpl Function: TODO ADD FUNCTION. Reason: TODO ADD
 * REASON(可选). Description: 学校年级信息实体Dao接口实现类. date: 2016-03-13
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Repository
public class JwTGradeDaoImpl extends BaseDaoImpl<JwTGrade> implements JwTGradeDao {
    public JwTGradeDaoImpl() {
        super(JwTGrade.class);
        // TODO Auto-generated constructor stub
    }
}