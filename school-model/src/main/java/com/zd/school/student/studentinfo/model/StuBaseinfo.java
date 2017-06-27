package com.zd.school.student.studentinfo.model;

import com.zd.core.annotation.FieldInfo;
import com.zd.school.plartform.system.model.SysUser;
import org.hibernate.annotations.Formula;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 
 * ClassName: StuBaseinfo Function: TODO ADD FUNCTION. Reason: TODO ADD
 * REASON(可选). Description: 学生基本信息实体类. date: 2016-07-19
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */

@Entity
@Table(name = "STU_T_BASEINFO")
//@AttributeOverride(name = "uuid", column = @Column(name = "STU_ID", length = 36, nullable = false))
public class StuBaseinfo extends SysUser implements Serializable {
    private static final long serialVersionUID = 1L;
    /*
     * @FieldInfo(name = "学号")
     * 
     * @Column(name = "XH", length = 20, nullable = false) private String xh;
     * 
     * public void setXh(String xh) { this.xh = xh; }
     * 
     * public String getXh() { return xh; }
     * 
     * @FieldInfo(name = "姓名")
     * 
     * @Column(name = "XM", length = 36, nullable = false) private String xm;
     * 
     * public void setXm(String xm) { this.xm = xm; }
     * 
     * public String getXm() { return xm; }
     */
    @FieldInfo(name = "英文姓名")
    @Column(name = "YWXM", length = 60, nullable = true)
    private String ywxm;

    public void setYwxm(String ywxm) {
        this.ywxm = ywxm;
    }

    public String getYwxm() {
        return ywxm;
    }

    @FieldInfo(name = "姓名拼音")
    @Column(name = "XMPY", length = 60, nullable = true)
    private String xmpy;

    public void setXmpy(String xmpy) {
        this.xmpy = xmpy;
    }

    public String getXmpy() {
        return xmpy;
    }

    @FieldInfo(name = "曾用名")
    @Column(name = "CYM", length = 36, nullable = true)
    private String cym;

    public void setCym(String cym) {
        this.cym = cym;
    }

    public String getCym() {
        return cym;
    }

    //    @FieldInfo(name = "性别码GB/T 2261.1")
    //    @Column(name = "XBM", length = 10, nullable = true)
    //    private String xbm;
    //
    //    public void setXbm(String xbm) {
    //        this.xbm = xbm;
    //    }

    //    public String getXbm() {
    //        return xbm;
    //    }

/*    @FieldInfo(name = "出生日期")
    @Column(name = "CSRQ", length = 24, nullable = true)
    private String csrq;

    public void setCsrq(String csrq) {
        this.csrq = csrq;
    }

    public String getCsrq() {
        return csrq;
    }*/

    @FieldInfo(name = "出生地码")
    @Column(name = "CSDM", length = 10, nullable = true)
    private String csdm;

    public void setCsdm(String csdm) {
        this.csdm = csdm;
    }

    public String getCsdm() {
        return csdm;
    }

    @FieldInfo(name = "籍贯")
    @Column(name = "JG", length = 20, nullable = true)
    private String jg;

    public void setJg(String jg) {
        this.jg = jg;
    }

    public String getJg() {
        return jg;
    }

    @FieldInfo(name = "民族码")
    @Column(name = "MZM", length = 10, nullable = true)
    private String mzm;

    public void setMzm(String mzm) {
        this.mzm = mzm;
    }

    public String getMzm() {
        return mzm;
    }

    @FieldInfo(name = "国籍/地区码")
    @Column(name = "GJDQM", length = 10, nullable = true)
    private String gjdqm;

    public void setGjdqm(String gjdqm) {
        this.gjdqm = gjdqm;
    }

    public String getGjdqm() {
        return gjdqm;
    }

    @FieldInfo(name = "身份证件类型码")
    @Column(name = "SFZJLXM", length = 10, nullable = true)
    private String sfzjlxm;

    public void setSfzjlxm(String sfzjlxm) {
        this.sfzjlxm = sfzjlxm;
    }

    public String getSfzjlxm() {
        return sfzjlxm;
    }

    @FieldInfo(name = "身份证件号")
    @Column(name = "SFZJH", length = 20, nullable = true)
    private String sfzjh;

    public void setSfzjh(String sfzjh) {
        this.sfzjh = sfzjh;
    }

    public String getSfzjh() {
        return sfzjh;
    }

    @FieldInfo(name = "婚姻状况码")
    @Column(name = "HYZKM", length = 10, nullable = true)
    private String hyzkm;

    public void setHyzkm(String hyzkm) {
        this.hyzkm = hyzkm;
    }

    public String getHyzkm() {
        return hyzkm;
    }

    @FieldInfo(name = "港澳台侨外码")
    @Column(name = "GATQWM", length = 10, nullable = true)
    private String gatqwm;

    public void setGatqwm(String gatqwm) {
        this.gatqwm = gatqwm;
    }

    public String getGatqwm() {
        return gatqwm;
    }

    @FieldInfo(name = "政治面貌码")
    @Column(name = "ZZMMM", length = 10, nullable = true)
    private String zzmmm;

    public void setZzmmm(String zzmmm) {
        this.zzmmm = zzmmm;
    }

    public String getZzmmm() {
        return zzmmm;
    }

    @FieldInfo(name = "健康状况码")
    @Column(name = "JKZKM", length = 10, nullable = true)
    private String jkzkm;

    public void setJkzkm(String jkzkm) {
        this.jkzkm = jkzkm;
    }

    public String getJkzkm() {
        return jkzkm;
    }

    @FieldInfo(name = "信仰宗教码")
    @Column(name = "XYZJM", length = 10, nullable = true)
    private String xyzjm;

    public void setXyzjm(String xyzjm) {
        this.xyzjm = xyzjm;
    }

    public String getXyzjm() {
        return xyzjm;
    }

    @FieldInfo(name = "血型码")
    @Column(name = "XXM", length = 10, nullable = true)
    private String xxm;

    public void setXxm(String xxm) {
        this.xxm = xxm;
    }

    public String getXxm() {
        return xxm;
    }

    @FieldInfo(name = "照片")
    @Column(name = "ZP", length = 200, nullable = true)
    private String zp;

    public void setZp(String zp) {
        this.zp = zp;
    }

    public String getZp() {
        return zp;
    }

    @FieldInfo(name = "身份证件有效期")
    @Column(name = "SFZJYXQ", length = 17, nullable = true)
    private String sfzjyxq;

    public void setSfzjyxq(String sfzjyxq) {
        this.sfzjyxq = sfzjyxq;
    }

    public String getSfzjyxq() {
        return sfzjyxq;
    }

    @FieldInfo(name = "是否独生子女")
    @Column(name = "SFDSZN", length = 10, nullable = true)
    private String sfdszn;

    public void setSfdszn(String sfdszn) {
        this.sfdszn = sfdszn;
    }

    public String getSfdszn() {
        return sfdszn;
    }

    @FieldInfo(name = "户口所在地行政区划码")
    @Column(name = "HKSZDXZQHM", length = 10, nullable = true)
    private String hkszdxzqhm;

    public void setHkszdxzqhm(String hkszdxzqhm) {
        this.hkszdxzqhm = hkszdxzqhm;
    }

    public String getHkszdxzqhm() {
        return hkszdxzqhm;
    }

    @FieldInfo(name = "户口类别码")
    @Column(name = "HKLBM", length = 10, nullable = true)
    private String hklbm;

    public void setHklbm(String hklbm) {
        this.hklbm = hklbm;
    }

    public String getHklbm() {
        return hklbm;
    }

    @FieldInfo(name = "是否流动人口")
    @Column(name = "SFLDRK", length = 10, nullable = true)
    private String sfldrk;

    public void setSfldrk(String sfldrk) {
        this.sfldrk = sfldrk;
    }

    public String getSfldrk() {
        return sfldrk;
    }

    @FieldInfo(name = "特长")
    @Column(name = "TC", length = 128, nullable = true)
    private String tc;

    public void setTc(String tc) {
        this.tc = tc;
    }

    public String getTc() {
        return tc;
    }

    @FieldInfo(name = "学籍号")
    @Column(name = "XJH", length = 30, nullable = true)
    private String xjh;

    public void setXjh(String xjh) {
        this.xjh = xjh;
    }

    public String getXjh() {
        return xjh;
    }

    //    @FieldInfo(name = "学校主键")
    //    @Column(name = "SCHOOL_ID", length = 36, nullable = false)
    //    private String schoolId;
    //
    //    @Override
    //    public void setSchoolId(String schoolId) {
    //        this.schoolId = schoolId;
    //    }
    //
    //    @Override
    //    public String getSchoolId() {
    //        return schoolId;
    //    }
    //
    //    @FieldInfo(name = "学校代码")
    //    @Column(name = "SCHOOL_CODE", length = 32, nullable = true)
    //    private String schoolCode;
    //
    //    public void setSchoolCode(String schoolCode) {
    //        this.schoolCode = schoolCode;
    //    }
    //
    //    public String getSchoolCode() {
    //        return schoolCode;
    //    }
    //
    //    @FieldInfo(name = "学校名称")
    //    @Column(name = "SCHOOL_NAME", length = 64, nullable = true)
    //    private String schoolName;
    //
    //    @Override
    //    public void setSchoolName(String schoolName) {
    //        this.schoolName = schoolName;
    //    }
    //
    //    @Override
    //    public String getSchoolName() {
    //        return schoolName;
    //    }

    @FieldInfo(name = "开户银行")
    @Column(name = "BANK_NAME", length = 128, nullable = true)
    private String bankName;

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankName() {
        return bankName;
    }

    @FieldInfo(name = "银行账号")
    @Column(name = "BANK_NUMBER", length = 32, nullable = true)
    private String bankNumber;

    public void setBankNumber(String bankNumber) {
        this.bankNumber = bankNumber;
    }

    public String getBankNumber() {
        return bankNumber;
    }

    @FieldInfo(name = "学生状态")
    @Column(name = "STUDENT_STATE", length = 4, nullable = true)
    private String studentState;

    public void setStudentState(String studentState) {
        this.studentState = studentState;
    }

    public String getStudentState() {
        return studentState;
    }

    /**
     * 广州城建导入数据需要，增加如下字段 luoyibo 2016-07-19
     */
    @FieldInfo(name = "年级ID")
    @Column(name = "GRADE_ID", length = 36, nullable = true)
    private String gradeId;

    public String getGradeId() {
        return gradeId;
    }

    public void setGradeId(String gradeId) {
        this.gradeId = gradeId;
    }

    @FieldInfo(name = "班级ID")
    @Column(name = "CLASS_ID", length = 36, nullable = true)
    private String classId;

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    /**
     * 00-收费 10-免费 11-全免
     */
    @FieldInfo(name = "学生的缴费类型")
    @Column(name = "STUPAY_TYPE", length = 16, nullable = true)
    private String stupayType;

    public String getStupayType() {
        return stupayType;
    }

    public void setStupayType(String stupayType) {
        this.stupayType = stupayType;
    }

    @FieldInfo(name = "专业ID")
    @Column(name = "MAJOR_ID", length = 36, nullable = true)
    private String majorId;

    public String getMajorId() {
        return majorId;
    }

    public void setMajorId(String majorId) {
        this.majorId = majorId;
    }

    @FieldInfo(name = "借读方式ID")
    @Column(name = "JDTYPE_ID", length = 36, nullable = true)
    private String jdtypeId;

    public String getJdtypeId() {
        return jdtypeId;
    }

    public void setJdtypeId(String jdtypeId) {
        this.jdtypeId = jdtypeId;
    }

    /**
     * 以下为不需要持久化到数据库中的字段,根据项目的需要手工增加
     * 
     * @Transient
     * @FieldInfo(name = "") private String field1;
     */
    //    @FieldInfo(name = "系别")
    //    @Formula("(SELECT c.FACULTY_NAME FROM EDU_T_MAJOR a,dbo.EDU_T_FACULTY c WHERE a.MAJOR_ID=MAJOR_ID AND a.FACULTY_ID=c.FACULTY_ID)")
    //    private String facultyName;
    //
    //    public String getFacultyName() {
    //        return facultyName;
    //    }
    //
    //    public void setFacultyName(String facultyName) {
    //        this.facultyName = facultyName;
    //    }
    //
    //    @FieldInfo(name = "专业名称")
    //    @Formula("(SELECT a.MAJOR_NAME FROM EDU_T_MAJOR a WHERE a.MAJOR_ID=MAJOR_ID )")
    //    private String majorName;
    //
    //    public String getMajorName() {
    //        return majorName;
    //    }
    //
    //    public void setMajorName(String majorName) {
    //        this.majorName = majorName;
    //    }
    //
    //    @FieldInfo(name = "专业层次名称")
    //    @Formula("(SELECT c.LEVEL_NAME FROM EDU_T_MAJOR a,dbo.EDU_T_MAJORLEVEL c WHERE a.MAJOR_ID=MAJOR_ID AND a.LEVEL_ID=c.LEVEL_ID)")
    //    private String levelName;
    //
    //    public String getLevelName() {
    //        return levelName;
    //    }
    //
    //    public void setLevelName(String levelName) {
    //        this.levelName = levelName;
    //    }
    //
    //    @FieldInfo(name = "年级名称")
    //    @Formula("(SELECT a.GRADE_NAME FROM EDU_T_UNIGRADE a WHERE a.GRADE_ID=GRADE_ID)")
    //    private String gradeName;
    //
    //    public String getGradeName() {
    //        return gradeName;
    //    }
    //
    //    public void setGradeName(String gradeName) {
    //        this.gradeName = gradeName;
    //    }

    @FieldInfo(name = "班级名称")
    @Formula("(SELECT a.CLASS_NAME FROM JW_T_GRADECLASS a where a.CLAI_ID=CLASS_ID)")
    private String className;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
    
    public StuBaseinfo() {
		super();
	}

	public StuBaseinfo(String uuid) {
		super(uuid);
	}
}