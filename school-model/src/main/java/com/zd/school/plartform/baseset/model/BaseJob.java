package com.zd.school.plartform.baseset.model;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.zd.core.annotation.FieldInfo;
import com.zd.core.model.BaseEntity;

/**
 * 
 * ClassName: BizTJob Function: TODO ADD FUNCTION. Reason: TODO ADD REASON(可选).
 * Description: 岗位信息实体类. date: 2016-05-16
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */

@Entity
@Table(name = "BASE_T_JOB")
@AttributeOverride(name = "uuid", column = @Column(name = "JOB_ID", length = 36, nullable = false))
public class BaseJob extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @FieldInfo(name = "岗位名称")
    @Column(name = "JOB_NAME", length = 32, nullable = false)
    private String jobName;

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobName() {
        return jobName;
    }

    @FieldInfo(name = "岗位编码")
    @Column(name = "JOB_CODE", length = 64, nullable = true)
    private String jobCode;

    public void setJobCode(String jobCode) {
        this.jobCode = jobCode;
    }

    public String getJobCode() {
        return jobCode;
    }

    @FieldInfo(name = "备注")
    @Column(name = "REMARK", length = 128, nullable = true)
    private String remark;

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemark() {
        return remark;
    }
    
    public BaseJob() {

        super();
        // TODO Auto-generated constructor stub

    }

    public BaseJob(String uuid) {

        super(uuid);
        // TODO Auto-generated constructor stub

    }

    //    @ManyToMany(mappedBy = "userJobs", cascade = CascadeType.ALL)
    //    private Set<SysUser> jobUsers = new HashSet<SysUser>();
    //
    //    public Set<SysUser> getJobUsers() {
    //        return jobUsers;
    //    }
    //
    //    public void setJobUsers(Set<SysUser> jobUsers) {
    //        this.jobUsers = jobUsers;
    //    }

    /**
     * 以下为不需要持久化到数据库中的字段,根据项目的需要手工增加
     * 
     * @Transient
     * @FieldInfo(name = "") private String field1;
     */
}