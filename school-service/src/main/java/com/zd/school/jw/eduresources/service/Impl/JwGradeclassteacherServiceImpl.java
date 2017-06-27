package com.zd.school.jw.eduresources.service.Impl;

import com.zd.core.model.extjs.ExtDataFilter;
import com.zd.core.model.extjs.QueryResult;
import com.zd.core.service.BaseServiceImpl;
import com.zd.core.util.JsonBuilder;
import com.zd.core.util.StringUtils;
import com.zd.school.jw.eduresources.dao.JwGradeclassteacherDao;
import com.zd.school.jw.eduresources.model.JwGradeclassteacher;
import com.zd.school.jw.eduresources.model.JwTGradeclass;
import com.zd.school.jw.eduresources.service.JwGradeclassteacherService;
import com.zd.school.jw.eduresources.service.JwTGradeclassService;
import com.zd.school.plartform.system.model.SysUser;
import com.zd.school.teacher.teacherinfo.model.TeaTeacherbase;
import com.zd.school.teacher.teacherinfo.service.TeaTeacherbaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * ClassName: JwGradeclassteacherServiceImpl Function: TODO ADD FUNCTION.
 * Reason: TODO ADD REASON(可选). Description:
 * 年级组长信息(JW_T_GRADECLASSTEACHER)实体Service接口实现类. date: 2016-09-20
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Service
@Transactional
public class JwGradeclassteacherServiceImpl extends BaseServiceImpl<JwGradeclassteacher>
        implements JwGradeclassteacherService {

    @Resource
    public void setJwGradeclassteacherDao(JwGradeclassteacherDao dao) {
        this.dao = dao;
    }

    @Resource
    private JwTGradeclassService gradeClassService;
    
    @Resource 
    private TeaTeacherbaseService teacherService;

    @SuppressWarnings("unchecked")
    @Override
    public QueryResult<TeaTeacherbase> getGradeClassTeacher(Integer start, Integer limit, String sort,
            String filter, Boolean isDelete, SysUser currentUser, String claiId, Integer claiLevel) {
        String queryFilter = filter;
        String qrClassId = "";
        ExtDataFilter selfFilter = new ExtDataFilter();
        StringBuffer sbClass = new StringBuffer();
        //指定了班级或年级
        if (StringUtils.isNotEmpty(claiId)) {
            switch (claiLevel) {
            case 1:
                //为1级是查询所有的数据
                break;
            case 2:
                //为2级是查询年级及年级下的班级的数据
                List<JwTGradeclass> classLists = gradeClassService.queryByProerties("graiId", claiId);
                for (JwTGradeclass gc : classLists) {
                    sbClass.append(gc.getUuid() + ",");
                }
                sbClass.append(claiId);
                qrClassId = sbClass.toString();
                break;
            case 3:
                //是3级，查询班级的数据
                qrClassId = claiId;
                break;
            default:
                break;
            }
        }
        //如果要根据指定的ID过滤
        if (StringUtils.isNotEmpty(qrClassId)) {
            //组装指定的过滤条件
            selfFilter = (ExtDataFilter) JsonBuilder.getInstance().fromJson(
                    "{\"type\":\"string\",\"comparison\":\"in\",\"value\":\"" + qrClassId + "\",\"field\":\"graiId\"}",
                    ExtDataFilter.class);
            //检查有没有外部传入的过滤条件
            if (StringUtils.isNotEmpty(filter)) {
                //有外部传入的条件，要合并上去
                List<ExtDataFilter> listFilters = (List<ExtDataFilter>) JsonBuilder.getInstance().fromJsonArray(filter,
                        ExtDataFilter.class);
                listFilters.add(selfFilter);

                queryFilter = JsonBuilder.getInstance().buildObjListToJson((long) listFilters.size(), listFilters,
                        false);
            } else {
                queryFilter = "[{\"type\":\"string\",\"comparison\":\"in\",\"value\":\"" + qrClassId
                        + "\",\"field\":\"graiId\"}]";
            }
        }
        QueryResult<JwGradeclassteacher> qr = this.doPaginationQuery(start, limit, sort, queryFilter, true);
        QueryResult<TeaTeacherbase> teacherList = new QueryResult<TeaTeacherbase>();
        List<TeaTeacherbase> newList = new ArrayList<TeaTeacherbase>();
/*        for (JwGradeclassteacher t : qr.getResultList()) {
			TeaTeacherbase teacherbase = teacherService.get(t.getTteacId());
			String jobInfo = teacherService.getTeacherJobs(teacherbase);
			String[] strings = jobInfo.split(",");
			teacherbase.setJobId(strings[0]);
			teacherbase.setJobName(strings[1]);
			
			String deptInfo = teacherService.getTeacherDepts(teacherbase);
			strings = deptInfo.split(",");
			teacherbase.setDeptName(strings[1]);	
			newList.add(teacherbase);			
		}
        teacherList.setResultList(newList);
        teacherList.setTotalCount(qr.getTotalCount());*/
        return teacherList;
    }

}