package com.zd.school.student.studentclass.service.Impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zd.core.model.extjs.ExtDataFilter;
import com.zd.core.model.extjs.QueryResult;
import com.zd.core.service.BaseServiceImpl;
import com.zd.core.util.JsonBuilder;
import com.zd.core.util.StringUtils;
import com.zd.school.jw.eduresources.model.JwTGradeclass;
import com.zd.school.jw.eduresources.service.JwTGradeclassService;
import com.zd.school.plartform.baseset.service.BaseOrgService;
import com.zd.school.plartform.system.model.SysUser;
import com.zd.school.student.studentclass.dao.JwClassstudentDao;
import com.zd.school.student.studentclass.model.JwClassstudent;
import com.zd.school.student.studentclass.service.JwClassstudentService;

/**
 * 
 * ClassName: JwClassstudentServiceImpl Function: TODO ADD FUNCTION. Reason:
 * TODO ADD REASON(可选). Description: 学生分班信息(JW_T_CLASSSTUDENT)实体Service接口实现类.
 * date: 2016-08-25
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Service
@Transactional
public class JwClassstudentServiceImpl extends BaseServiceImpl<JwClassstudent> implements JwClassstudentService {

    @Resource
    public void setJwClassstudentDao(JwClassstudentDao dao) {
        this.dao = dao;
    }

    @Resource
    private BaseOrgService orgService;
    @Resource
    private JwTGradeclassService classService;

    @SuppressWarnings("unchecked")
    @Override
    public QueryResult<JwClassstudent> getclassStudent(Integer start, Integer limit, String sort, String filter,
            Boolean isDelete, String claiId, SysUser currentUser) {
        String queryFilter = filter;
        String qrClassId = claiId;
        //当前用户有权限的班级列表
        QueryResult<JwTGradeclass> qr = classService.getGradeClassList(0, 0, "", "", true, currentUser);
        List<JwTGradeclass> jgClass = qr.getResultList();
        StringBuffer sb = new StringBuffer();
        if (jgClass.size() > 0) {
            for (JwTGradeclass jwTGrade : jgClass) {
                sb.append(jwTGrade.getUuid() + ",");
            }
            sb.deleteCharAt(sb.length() - 1);
        }
        if (StringUtils.isEmpty(claiId) || claiId.equals("2851655E-3390-4B80-B00C-52C7CA62CB39")) {
            //选择没有选择年级，使用有权限的所有年级
            qrClassId = sb.toString();
        }
        ExtDataFilter selfFilter = (ExtDataFilter) JsonBuilder.getInstance().fromJson(
                "{\"type\":\"string\",\"comparison\":\"in\",\"value\":\"" + qrClassId + "\",\"field\":\"claiId\"}",
                ExtDataFilter.class);

        if (StringUtils.isNotEmpty(filter)) {
            List<ExtDataFilter> listFilters = (List<ExtDataFilter>) JsonBuilder.getInstance().fromJsonArray(filter,
                    ExtDataFilter.class);
            listFilters.add(selfFilter);

            queryFilter = JsonBuilder.getInstance().buildObjListToJson((long) listFilters.size(), listFilters, false);
        } else {
            queryFilter = "[{\"type\":\"string\",\"comparison\":\"in\",\"value\":\"" + qrClassId
                    + "\",\"field\":\"claiId\"}]";
        }
        QueryResult<JwClassstudent> qrReturn = this.doPaginationQuery(start, limit, sort, queryFilter, true);
        return qrReturn;
    }
}