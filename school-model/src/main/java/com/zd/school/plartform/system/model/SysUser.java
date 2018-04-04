package com.zd.school.plartform.system.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.zd.core.annotation.FieldInfo;
import com.zd.core.model.BaseEntity;
import com.zd.core.util.DateTimeSerializer;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * ClassName: BaseTUser Function: TODO ADD FUNCTION. Reason: TODO ADD
 * REASON(可选). Description: 用户管理实体类. date: 2016-07-17
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */

@Entity
@Table(name = "SYS_T_USER")
@AttributeOverride(name = "uuid", column = @Column(name = "USER_ID", length = 36, nullable = false))
@Inheritance(strategy = InheritanceType.JOINED)
public class SysUser extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
/*    @FieldInfo(name = "部门ID")
    @Column(name = "DEPT_ID", length = 36, nullable = true)
    private String deptId;

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getDeptId() {
        return deptId;
    }*/

    @FieldInfo(name = "账号")
    @Column(name = "USER_NAME", length = 16, nullable = false)
    private String userName;

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    @FieldInfo(name = "密码")
    @Column(name = "USER_PWD", length = 128, nullable = false)
    private String userPwd;

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    public String getUserPwd() {
        return userPwd;
    }


    @FieldInfo(name = "状态")
    @Column(name = "STATE", length = 4, nullable = true)
    private String state;

    public void setState(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }

    @FieldInfo(name = "是否系统 1=非内置 0=内置")
    @Column(name = "ISSYSTEM", length = 10, nullable = true)
    private Integer issystem;

    public void setIssystem(Integer issystem) {
        this.issystem = issystem;
    }

    public Integer getIssystem() {
        return issystem;
    }

    @FieldInfo(name = "身份 0=内部用户 1=老师 2=学生 3=家长 ")
    @Column(name = "CATEGORY", length = 10, nullable = true)
    private String category;

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    @FieldInfo(name = "最后登录时间")
    @Column(name = "LOGIN_TIME", length = 23, nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using = DateTimeSerializer.class)
    private Date loginTime;

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    @JsonIgnore
    @FieldInfo(name = "用户隶属角色")
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "SYS_T_ROLEUSER", joinColumns = {@JoinColumn(name = "USER_ID")}, inverseJoinColumns = {
            @JoinColumn(name = "ROLE_ID")})
    @Cache(region = "all", usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<SysRole> sysRoles = new HashSet<SysRole>();

    public Set<SysRole> getSysRoles() {
        return sysRoles;
    }

    public void setSysRoles(Set<SysRole> sysRoles) {
        this.sysRoles = sysRoles;
    }

/*	@JsonIgnore
    @FieldInfo(name = "用户所属部门")
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "SYS_T_USERDEPT", joinColumns = { @JoinColumn(name = "USER_ID") }, inverseJoinColumns = {
			@JoinColumn(name = "DEPT_ID") })
	@Cache(region = "all", usage = CacheConcurrencyStrategy.READ_WRITE)
	private Set<BaseOrg> userDepts = new HashSet<BaseOrg>();

	public Set<BaseOrg> getUserDepts() {
		return userDepts;
	}

	public void setUserDepts(Set<BaseOrg> userDepts) {
		this.userDepts = userDepts;
	}

	@JsonIgnore
	@FieldInfo(name = "用户所在岗位")
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "SYS_T_USERJOB", joinColumns = { @JoinColumn(name = "USER_ID") }, inverseJoinColumns = {
			@JoinColumn(name = "JOB_ID") })
	@Cache(region = "all", usage = CacheConcurrencyStrategy.READ_WRITE)
	private Set<BaseJob> userJobs = new HashSet<BaseJob>();

	public Set<BaseJob> getUserJobs() {
		return userJobs;
	}

	public void setUserJobs(Set<BaseJob> userJobs) {
		this.userJobs = userJobs;
	}*/

    @FieldInfo(name = "学校ID")
    @Column(name = "SCHOOL_ID", length = 36, nullable = true)
    private String schoolId;

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    @FieldInfo(name = "是否隐藏,0-不隐藏 1-隐藏")
    @Column(name = "ISHIDDEN", length = 10, nullable = true)
    private String isHidden;

    public String getIsHidden() {
        return isHidden;
    }

    public void setIsHidden(String isHidden) {
        this.isHidden = isHidden;
    }

    /**
     * CATEGORY=1 对应老师的工号(gh) CATEGORY=2 对应学生的学号(xh)
     */
    @FieldInfo(name = "用户编号")
    @Column(name = "USER_NUMB", length = 16, nullable = true)
    private String userNumb;

    public String getUserNumb() {
        return userNumb;
    }

    public void setUserNumb(String userNumb) {
        this.userNumb = userNumb;
    }

    @FieldInfo(name = "姓名")
    @Column(name = "XM", length = 36, nullable = false)
    private String xm;

    public void setXm(String xm) {
        this.xm = xm;
    }

    public String getXm() {
        return xm;
    }

    @FieldInfo(name = "性别码")
    @Column(name = "XBM", length = 10, nullable = true)
    private String xbm;

    public void setXbm(String xbm) {
        this.xbm = xbm;
    }

    public String getXbm() {
        return xbm;
    }


    @FieldInfo(name = "出生日期")
    @Column(name = "CSRQ", length = 23, nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using = DateTimeSerializer.class)
    private Date csrq;

    public void setCsrq(Date csrq) {
        this.csrq = csrq;
    }

    public Date getCsrq() {
        return csrq;
    }

    @FieldInfo(name = "身份证件号")
    @Column(name = "SFZJH", length = 255, nullable = true)
    private String sfzjh;

    public void setSfzjh(String sfzjh) {
        this.sfzjh = sfzjh;
    }

    public String getSfzjh() {
        return sfzjh;
    }

    @FieldInfo(name = "政治面貌")
    @Column(name = "ZZMMM", length = 10, nullable = true)
    private String zzmmm;

    public void setZzmmm(String zzmmm) {
        this.zzmmm = zzmmm;
    }

    public String getZzmmm() {
        return zzmmm;
    }

    @FieldInfo(name = "移动电话")
    @Column(name = "MOBILE", length = 50, nullable = true)
    private String mobile;

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getMobile() {
        return mobile;
    }

    @FieldInfo(name = "电子信箱")
    @Column(name = "DZXX", length = 40, nullable = true)
    private String dzxx;

    public void setDzxx(String dzxx) {
        this.dzxx = dzxx;
    }

    public String getDzxx() {
        return dzxx;
    }

    @FieldInfo(name = "人员编制类型")
    @Column(name = "ZXXBZLB", length = 40, nullable = true)
    private String zxxbzlb;

    public String getZxxbzlb() {
        return zxxbzlb;
    }

    public void setZxxbzlb(String zxxbzlb) {
        this.zxxbzlb = zxxbzlb;
    }

    @FieldInfo(name = "专业技术职称")
    @Column(name = "TECHNICAL", length = 64, nullable = true)
    private String technical;

    public void setTechnical(String technical) {
        this.technical = technical;
    }

    public String getTechnical() {
        return technical;
    }
/*	@FieldInfo(name = "主岗位ID")
    @Column(name = "JOB_ID", length = 36, nullable = true)
	private String jobId;

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	@FieldInfo(name = "主岗位编码")
	@Column(name = "JOB_CODE", length = 16, nullable = true)
	private String jobCode;

	public String getJobCode() {
		return jobCode;
	}

	public void setJobCode(String jobCode) {
		this.jobCode = jobCode;
	}

	@FieldInfo(name = "主岗位名称")
	@Column(name = "JOB_NAME", length = 36, nullable = true)
	private String jobName;

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}*/

    /**
     * 以下为不需要持久化到数据库中的字段,根据项目的需要手工增加
     *
     * @Transient
     * @FieldInfo(name = "") private String field1;
     */
    @FieldInfo(name = "下次自动登录")
    @Transient
    private boolean rememberMe;

    public boolean isRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(boolean rememberMe) {
        this.rememberMe = rememberMe;
    }
    @FieldInfo(name = "主部门ID")
    @Formula("(SELECT ISNULL(a.DEPT_ID,'') FROM dbo.BASE_T_USERDEPTJOB a WHERE a.MASTER_DEPT=1 AND a.ISDELETE=0 AND a.USER_ID=USER_ID)")
    private String deptId;

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getDeptId() {
        return deptId;
    }

    @FieldInfo(name = "主部门名称")
    @Formula("(SELECT ISNULL(a.NODE_TEXT,'') FROM dbo.BASE_T_ORG a WHERE a.DEPT_ID=(SELECT b.DEPT_ID FROM BASE_T_USERDEPTJOB b WHERE b.MASTER_DEPT=1 AND  b.USER_ID=USER_ID AND b.ISDELETE=0))")
    private String deptName;

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    @FieldInfo(name = "主岗位ID")
    @Formula("(SELECT ISNULL(a.JOB_ID,'') FROM dbo.BASE_T_USERDEPTJOB a WHERE a.MASTER_DEPT=1 AND a.ISDELETE=0 AND a.USER_ID=USER_ID)")
    private String jobId;

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }
    @FieldInfo(name = "主岗位名称")
    @Formula("(SELECT ISNULL(a.JOB_NAME,'') FROM dbo.BASE_T_JOB a WHERE a.JOB_ID=(SELECT b.JOB_ID FROM BASE_T_USERDEPTJOB b WHERE b.MASTER_DEPT=1 AND  b.USER_ID=USER_ID AND b.ISDELETE=0))")
    private String jobName;

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }
    @FieldInfo(name = "学校名称")
    @Formula("(SELECT a.NODE_TEXT from BASE_T_ORG a where a.DEPT_ID=SCHOOL_ID)")
    private String schoolName;

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    @FieldInfo(name = "当前学年")
    @Transient
    private Integer studyYear;

    public Integer getStudyYear() {
        return studyYear;
    }

    public void setStudyYear(Integer studyYear) {
        this.studyYear = studyYear;
    }

    @FieldInfo(name = "当前学期")
    @Transient
    private String semester;

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    @FieldInfo(name = "当前学年名称")
    @Transient
    private String studyYearname;

    public String getStudyYearname() {
        return studyYearname;
    }

    public void setStudyYearname(String studyYearname) {
        this.studyYearname = studyYearname;
    }
    
    @FieldInfo(name = "UP卡流水号")
    @Formula("(SELECT top 1 a.UP_CARD_ID FROM CARD_T_USEINFO a where a.USER_ID=USER_ID order by a.CREATE_TIME desc)")
    private Long upCardId;

    public Long getUpCardId() {
        return upCardId;
    }

    public void setUpCardId(Long upCardId) {
        this.upCardId = upCardId;
    }
    
    @FieldInfo(name = "UP卡印刷卡号")
    @Formula("(SELECT top 1 a.CARD_PRINT_ID FROM CARD_T_USEINFO a where a.USER_ID=USER_ID order by a.CREATE_TIME desc)")
    private String cardPrintId;

   	public String getCardPrintId() {
   		return cardPrintId;
   	}

   	public void setCardPrintId(String cardPrintId) {
   		this.cardPrintId = cardPrintId;
   	}
    
    @FieldInfo(name = "UP卡状态")
    @Formula("(SELECT top 1 a.USE_STATE FROM CARD_T_USEINFO a where a.USER_ID=USER_ID order by a.CREATE_TIME desc)")
    private Integer useState;

    public Integer getUseState() {
        return useState;
    }

    public void setUseState(Integer useState) {
        this.useState = useState;
    }
    
    public SysUser() {
        super();
    }

    public SysUser(String uuid) {
        super(uuid);
    }
/*
	@FieldInfo(name = "所有岗位ID")
	@Transient
	private String allJobId;

	public String getAllJobId() {
		return allJobId;
	}

	public void setAllJobId(String allJobId) {
		this.allJobId = allJobId;
	}

	@FieldInfo(name = "所有岗位名称")
	// @Transient
	@Formula("(SELECT dbo.fn_GetUserJobNames(user_id))")
	private String allJobName;

	public String getAllJobName() {
		return allJobName;
	}

	public void setAllJobName(String allJobName) {
		this.allJobName = allJobName;
	}

	@FieldInfo(name = "所有部门名称")
	@Formula("(SELECT dbo.fn_GetUserDeptNames(user_id))")
	private String allDeptName;

	public String getAllDeptName() {
		return allDeptName;
	}

	public void setAllDeptName(String allDeptName) {
		this.allDeptName = allDeptName;
	}*/
}