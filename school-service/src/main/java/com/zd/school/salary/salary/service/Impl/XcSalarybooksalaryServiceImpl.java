package com.zd.school.salary.salary.service.Impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zd.core.model.extjs.QueryResult;
import com.zd.core.service.BaseServiceImpl;
import com.zd.core.util.StringUtils;
import com.zd.school.plartform.system.model.SysUser;
import com.zd.school.salary.salary.dao.XcSalarybooksalaryDao;
import com.zd.school.salary.salary.model.XcSalarybook ;
import com.zd.school.salary.salary.model.XcSalarybooksalary;
import com.zd.school.salary.salary.service.XcSalarybooksalaryService;

/**
 * 
 * ClassName: XcSalarybookServiceImpl
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 工资台账表(XC_T_SALARYBOOK)实体Service接口实现类.
 * date: 2016-12-05
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Service
@Transactional
public class XcSalarybooksalaryServiceImpl extends BaseServiceImpl<XcSalarybooksalary> implements XcSalarybooksalaryService{

    @Resource
    public void setXcSalarybooksalaryDao(XcSalarybooksalaryDao dao) {
        this.dao = dao;
    }

}