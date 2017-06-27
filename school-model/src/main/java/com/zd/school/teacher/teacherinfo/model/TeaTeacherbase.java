package com.zd.school.teacher.teacherinfo.model;

import com.zd.core.annotation.FieldInfo;
import com.zd.school.plartform.system.model.SysUser;
import org.hibernate.annotations.Formula;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;

/**
 * 
 * ClassName: TeaTeacherbase Function: TODO ADD FUNCTION. Reason: TODO ADD
 * REASON(可选). Description: 教职工基本数据实体类. date: 2016-07-19
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */

@Entity
@Table(name = "TEA_T_TEACHERBASE")
//@AttributeOverride(name = "uuid", column = @Column(name = "TEACH_ID", length = 36, nullable = false))
public class TeaTeacherbase extends SysUser implements Serializable {
    private static final long serialVersionUID = 1L;

//    @FieldInfo(name = "学校主键")
//    @Column(name = "SCHOOL_ID", length = 36, nullable = true)
//    private String schoolId;
//
//    public void setSchoolId(String schoolId) {
//        this.schoolId = schoolId;
//    }
//
//    public String getSchoolId() {
//        return schoolId;
//    }
//
//    @FieldInfo(name = "工号")
//    @Column(name = "GH", length = 20, nullable = false)
//    private String gh;
//
//    public void setGh(String gh) {
//        this.gh = gh;
//    }
//
//    public String getUserNumb() {
//        return gh;
//    }

//    @FieldInfo(name = "姓名")
//    @Column(name = "XM", length = 36, nullable = false)
//    private String xm;
//
//    public void setXm(String xm) {
//        this.xm = xm;
//    }
//
//    public String getXm() {
//        return xm;
//    }

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

//    @FieldInfo(name = "性别码")
//    @Column(name = "XBM", length = 10, nullable = true)
//    private String xbm;
//
//    public void setXbm(String xbm) {
//        this.xbm = xbm;
//    }
//
//    public String getXbm() {
//        return xbm;
//    }

    @FieldInfo(name = "出生地码")
    @Column(name = "CSDM", length = 10, nullable = true)
    private String csdm;

    public void setCsdm(String csdm) {
        this.csdm = csdm;
    }

    public String getCsdm() {
        return csdm;
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

    @FieldInfo(name = "籍贯")
    @Column(name = "JG", length = 20, nullable = true)
    private String jg;

    public void setJg(String jg) {
        this.jg = jg;
    }

    public String getJg() {
        return jg;
    }

    @FieldInfo(name = "户口所在地")
    @Column(name = "HKSZD", length = 180, nullable = true)
    private String hkszd;

    public void setHkszd(String hkszd) {
        this.hkszd = hkszd;
    }

    public String getHkszd() {
        return hkszd;
    }

    @FieldInfo(name = "户口性质码")
    @Column(name = "HKXZM", length = 10, nullable = true)
    private String hkxzm;

    public void setHkxzm(String hkxzm) {
        this.hkxzm = hkxzm;
    }

    public String getHkxzm() {
        return hkxzm;
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

    @FieldInfo(name = "身份证件类型码")
    @Column(name = "SFZJLXM", length = 10, nullable = true)
    private String sfzjlxm;

    public void setSfzjlxm(String sfzjlxm) {
        this.sfzjlxm = sfzjlxm;
    }

    public String getSfzjlxm() {
        return sfzjlxm;
    }

/*    @FieldInfo(name = "身份证件号")
    @Column(name = "SFZJH", length = 20, nullable = true)
    private String sfzjh;

    public void setSfzjh(String sfzjh) {
        this.sfzjh = sfzjh;
    }

    public String getSfzjh() {
        return sfzjh;
    }*/

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

/*    @FieldInfo(name = "政治面貌码GB/T 4762")
    @Column(name = "ZZMMM", length = 10, nullable = true)
    private String zzmmm;

    public void setZzmmm(String zzmmm) {
        this.zzmmm = zzmmm;
    }

    public String getZzmmm() {
        return zzmmm;
    }*/

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

    @FieldInfo(name = "家庭住址")
    @Column(name = "JTZZ", length = 180, nullable = true)
    private String jtzz;

    public void setJtzz(String jtzz) {
        this.jtzz = jtzz;
    }

    public String getJtzz() {
        return jtzz;
    }

    @FieldInfo(name = "现住址")
    @Column(name = "XZZ", length = 180, nullable = true)
    private String xzz;

    public void setXzz(String xzz) {
        this.xzz = xzz;
    }

    public String getXzz() {
        return xzz;
    }

    @FieldInfo(name = "学历码")
    @Column(name = "XLM", length = 10, nullable = true)
    private String xlm;

    public void setXlm(String xlm) {
        this.xlm = xlm;
    }

    public String getXlm() {
        return xlm;
    }

    @FieldInfo(name = "参加工作年月")
    @Column(name = "GZNY", length = 16, nullable = true)
    private String gzny;

    public void setGzny(String gzny) {
        this.gzny = gzny;
    }

    public String getGzny() {
        return gzny;
    }

    @FieldInfo(name = "来校年月")
    @Column(name = "LXNY", length = 16, nullable = true)
    private String lxny;

    public void setLxny(String lxny) {
        this.lxny = lxny;
    }

    public String getLxny() {
        return lxny;
    }

    @FieldInfo(name = "从教年月")
    @Column(name = "CJNY", length = 16, nullable = true)
    private String cjny;

    public void setCjny(String cjny) {
        this.cjny = cjny;
    }

    public String getCjny() {
        return cjny;
    }

    @FieldInfo(name = "编制类别码")
    @Column(name = "BZLBM", length = 10, nullable = true)
    private String bzlbm;

    public void setBzlbm(String bzlbm) {
        this.bzlbm = bzlbm;
    }

    public String getBzlbm() {
        return bzlbm;
    }

    @FieldInfo(name = "档案编号")
    @Column(name = "DABH", length = 10, nullable = true)
    private String dabh;

    public void setDabh(String dabh) {
        this.dabh = dabh;
    }

    public String getDabh() {
        return dabh;
    }

    @FieldInfo(name = "档案文本")
    @Column(name = "DAWB", length = 128, nullable = true)
    private String dawb;

    public void setDawb(String dawb) {
        this.dawb = dawb;
    }

    public String getDawb() {
        return dawb;
    }

    @FieldInfo(name = "通信地址")
    @Column(name = "TXDZ", length = 180, nullable = true)
    private String txdz;

    public void setTxdz(String txdz) {
        this.txdz = txdz;
    }

    public String getTxdz() {
        return txdz;
    }

    @FieldInfo(name = "联系电话")
    @Column(name = "LXDH", length = 30, nullable = true)
    private String lxdh;

    public void setLxdh(String lxdh) {
        this.lxdh = lxdh;
    }

    public String getLxdh() {
        return lxdh;
    }

    @FieldInfo(name = "邮政编码")
    @Column(name = "YZBM", length = 6, nullable = true)
    private String yzbm;

    public void setYzbm(String yzbm) {
        this.yzbm = yzbm;
    }

    public String getYzbm() {
        return yzbm;
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

    @FieldInfo(name = "主页地址")
    @Column(name = "ZYDZ", length = 60, nullable = true)
    private String zydz;

    public void setZydz(String zydz) {
        this.zydz = zydz;
    }

    public String getZydz() {
        return zydz;
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

    @FieldInfo(name = "岗位职业码")
    @Column(name = "GWZYM", length = 10, nullable = true)
    private String gwzym;

    public void setGwzym(String gwzym) {
        this.gwzym = gwzym;
    }

    public String getGwzym() {
        return gwzym;
    }

    @FieldInfo(name = "主要任课学段")
    @Column(name = "ZYRKXD", length = 10, nullable = true)
    private String zyrkxd;

    public void setZyrkxd(String zyrkxd) {
        this.zyrkxd = zyrkxd;
    }

    public String getZyrkxd() {
        return zyrkxd;
    }

    @FieldInfo(name = "主讲课程")
    @Column(name = "MAIN_COURSE", length = 36, nullable = true)
    private String mainCourse;

    public void setMainCourse(String mainCourse) {
        this.mainCourse = mainCourse;
    }

    public String getMainCourse() {
        return mainCourse;
    }

/*    @FieldInfo(name = "出生日期")
    @Column(name = "CSRQ", length = 23, nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using = DateTimeSerializer.class)
    private Date csrq;

    public void setCsrq(Date csrq) {
        this.csrq = csrq;
    }

    public Date getCsrq() {
        return csrq;
    }*/

	/**
     * 以下为不需要持久化到数据库中的字段,根据项目的需要手工增加
     * 
     * @Transient
     * @FieldInfo(name = "") private String field1;
     */
    @FieldInfo(name = "老师工号")
    @Formula("(SELECT a.USER_NUMB FROM SYS_T_USER a WHERE a.USER_ID=USER_ID )")
    private String gh;

    public String getGh() {
        return gh;
    }

    public void setGh(String gh) {
        this.gh = gh;
    }
    @Transient
    @FieldInfo(name = "老师姓名")
    //@Formula("(SELECT a.xm FROM SYS_T_USER a WHERE a.USER_ID=USER_ID )")
    private String xm;

    public String getXm() {
        return xm;
    }

    public void setXm(String xm) {
        this.xm = xm;
    }
//
//    @FieldInfo(name = "老师性别")
//    @Formula("(SELECT a.xbm FROM SYS_T_USER a WHERE a.USER_ID=USER_ID )")
//    private String xbm;
//
//    public String getXbm() {
//        return xbm;
//    }
//
//    public void setXbm(String xbm) {
//        this.xbm = xbm;
//    }
}