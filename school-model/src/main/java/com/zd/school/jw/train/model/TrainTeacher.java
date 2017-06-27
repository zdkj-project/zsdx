package com.zd.school.jw.train.model;

import com.zd.core.annotation.FieldInfo;
import com.zd.core.model.BaseEntity;
import com.zd.school.excel.annotation.MapperCell;

import javax.persistence.*;
import java.io.Serializable;

/**
 * ClassName: TrainTeacher
 * Function: TODO ADD FUNCTION.
 * Reason: TODO ADD REASON(可选).
 * Description: 师资信息(TRAIN_T_TEACHER)实体类.
 * date: 2017-03-07
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Entity
@Table(name = "TRAIN_T_TEACHER")
@AttributeOverride(name = "uuid", column = @Column(name = "TEA_ID", length = 36, nullable = false))
public class TrainTeacher extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @MapperCell(cellName = "姓名", order = 1)
    @FieldInfo(name = "姓名")
    @Column(name = "XM", length = 64, nullable = false)
    private String xm;

    public void setXm(String xm) {
        this.xm = xm;
    }

    public String getXm() {
        return xm;
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

    @FieldInfo(name = "性别")
    @Column(name = "XBM", length = 1, nullable = false)
    private String xbm;

    public void setXbm(String xbm) {
        this.xbm = xbm;
    }

    public String getXbm() {
        return xbm;
    }

    @MapperCell(cellName = "手机号码", order = 6)
    @FieldInfo(name = "移动电话")
    @Column(name = "MOBILE_PHONE", length = 36, nullable = false)
    private String mobilePhone;

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    @FieldInfo(name = "校内/校外 1-校内 2-校外 21-本市 22-市外")
    @Column(name = "INOUT", length = 6, nullable = false)
    private String inout;

    public void setInout(String inout) {
        this.inout = inout;
    }

    public String getInout() {
        return inout;
    }

    @MapperCell(cellName = "职务", order = 3)
    @FieldInfo(name = "职务")
    @Column(name = "POSITION", length = 128, nullable = true)
    private String position;

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPosition() {
        return position;
    }

    //@MapperCell(cellName = "学历", order =)
    @FieldInfo(name = "行政级别 HEADSHIPLEVEL字典")
    @Column(name = "HEADSHIP_LEVEL", length = 16, nullable = true)
    private String headshipLevel;

    public void setHeadshipLevel(String headshipLevel) {
        this.headshipLevel = headshipLevel;
    }

    public String getHeadshipLevel() {
        return headshipLevel;
    }

    @MapperCell(cellName = "工作单位", order = 8)
    @FieldInfo(name = "工作单位")
    @Column(name = "WOKE_UNITS", length = 128, nullable = false)
    private String workUnits;

    public void setWorkUnits(String workUnits) {
        this.workUnits = workUnits;
    }

    public String getWorkUnits() {
        return workUnits;
    }

    /*   @FieldInfo(name = "教学类型,TEACHTYPE字典")
       @Column(name = "TEACH_TYPE", length = 16, nullable = true)
       private String teachType;
       public void setTeachType(String teachType) {
           this.teachType = teachType;
       }
       public String getTeachType() {
           return teachType;
       }*/
    //@MapperCell(cellName = "学历", order =)
    @FieldInfo(name = "身份证件号")
    @Column(name = "SFZJH", length = 20, nullable = false)
    private String sfzjh;

    public void setSfzjh(String sfzjh) {
        this.sfzjh = sfzjh;
    }

    public String getSfzjh() {
        return sfzjh;
    }

    @FieldInfo(name = "照片")
    @Column(name = "ZP", length = 128, nullable = true)
    private String zp;

    public void setZp(String zp) {
        this.zp = zp;
    }

    public String getZp() {
        return zp;
    }

    @FieldInfo(name = "学历")
    @Column(name = "XLM", length = 16, nullable = true)
    private String xlm;

    public void setXlm(String xlm) {
        this.xlm = xlm;
    }

    public String getXlm() {
        return xlm;
    }

    @MapperCell(cellName = "专业", order = 9)
    @FieldInfo(name = "专业")
    @Column(name = "ZYM", length = 16, nullable = true)
    private String zym;

    public void setZym(String zym) {
        this.zym = zym;
    }

    public String getZym() {
        return zym;
    }

    @MapperCell(cellName = "电子邮件", order = 10)
    @FieldInfo(name = "电子邮件")
    @Column(name = "DZXX", length = 32, nullable = true)
    private String dzxx;

    public void setDzxx(String dzxx) {
        this.dzxx = dzxx;
    }

    public String getDzxx() {
        return dzxx;
    }

    @MapperCell(cellName = "通讯地址", order = 11)
    @FieldInfo(name = "通讯地址")
    @Column(name = "ADDRESS", length = 64, nullable = true)
    private String address;

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    @MapperCell(cellName = "教师简介", order = 12)
    @FieldInfo(name = "教师简介")
    @Column(name = "TEA_DESC", length = 256, nullable = true)
    private String teaDesc;

    public void setTeaDesc(String teaDesc) {
        this.teaDesc = teaDesc;
    }

    public String getTeaDesc() {
        return teaDesc;
    }

    @MapperCell(cellName = "主要研究方向", order = 13)
    @FieldInfo(name = "主要研究方向")
    @Column(name = "RESEARCH_AREA", length = 1024, nullable = true)
    private String researchArea;

    public String getResearchArea() {
        return researchArea;
    }

    public void setResearchArea(String researchArea) {
        this.researchArea = researchArea;
    }

    @MapperCell(cellName = "主要研究成果", order = 14)
    @FieldInfo(name = "主要研究成果")
    @Column(name = "RESEARCH_RESULT", length = 1024, nullable = true)
    private String researchResult;

    public String getResearchResult() {
        return researchResult;
    }

    public void setResearchResult(String researchResult) {
        this.researchResult = researchResult;
    }

    @MapperCell(cellName = "主要讲授专题", order = 15)
    @FieldInfo(name = "主要讲授专题")
    @Column(name = "TEACHING_PROJECT", length = 1024, nullable = true)
    private String teachingProject;

    public String getTeachingProject() {
        return teachingProject;
    }

    public void setTeachingProject(String teachingProject) {
        this.teachingProject = teachingProject;
    }


    /**
     * 以下为不需要持久化到数据库中的字段,根据项目的需要手工增加
     *
     * @Transient
     * @FieldInfo(name = "")
     * private String field1;
     */
    @MapperCell(cellName = "职称", order = 4)
    @Transient
    @FieldInfo(name = "技术职称名称")
    private String technicalName;

    public String getTechnicalName() {
        return technicalName;
    }

    public void setTechnicalName(String technicalName) {
        this.technicalName = technicalName;
    }

    @MapperCell(cellName = "性别", order = 2)
    @Transient
    @FieldInfo(name = "性别")
    private String xbmName;

    public String getXbmName() {
        return xbmName;
    }

    public void setXbmName(String xbmName) {
        this.xbmName = xbmName;
    }

    @MapperCell(cellName = "学历", order = 5)
    @Transient
    @FieldInfo(name = "学历")
    private String xlmName;

    public String getXlmName() {
        return xlmName;
    }

    public void setXlmName(String xlmName) {
        this.xlmName = xlmName;
    }
    @MapperCell(cellName = "校内/外", order = 7)
    @Transient
    @FieldInfo(name = "校内/校外")
    private String inoutName;

    public String getInoutName() {
        return inoutName;
    }

    public void setInoutName(String inoutName) {
        this.inoutName = inoutName;
    }
}