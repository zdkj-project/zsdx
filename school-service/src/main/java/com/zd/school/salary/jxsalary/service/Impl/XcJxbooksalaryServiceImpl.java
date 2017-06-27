package com.zd.school.salary.jxsalary.service.Impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zd.core.service.BaseServiceImpl;
import com.zd.school.salary.jxsalary.dao.XcJxbooksalaryDao;
import com.zd.school.salary.jxsalary.model.XcJxbooksalary;
import com.zd.school.salary.jxsalary.service.XcJxbooksalaryService;

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
public class XcJxbooksalaryServiceImpl extends BaseServiceImpl<XcJxbooksalary> implements XcJxbooksalaryService{

    @Resource
    public void setXcJxbooksalaryDao(XcJxbooksalaryDao dao) {
        this.dao = dao;
    }

}