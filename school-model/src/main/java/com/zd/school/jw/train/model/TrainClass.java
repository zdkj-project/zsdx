package com.zd.school.jw.train.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.zd.core.annotation.FieldInfo;
import com.zd.core.model.BaseEntity;
import com.zd.core.util.DateTimeSerializer;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * ClassName: TrainClass
 * Function: TODO ADD FUNCTION.
 * Reason: TODO ADD REASON(可选).
 * Description: 培训开班信息(TRAIN_T_CLASS)实体类.
 * date: 2017-03-07
 * 备用字段5：用来存放班级的结算单据编号（每次导出都要重新变化：例 20170830001，前缀年月日为导出时的日期，后缀三位数为这一天当前票据流水号）
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */

@Entity
@Table(name = "TRAIN_T_CLASS")
@AttributeOverride(name = "uuid", column = @Column(name = "CLASS_ID", length = 36, nullable = false))
public class TrainClass extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @FieldInfo(name = "班级名称")
    @Column(name = "CLASS_NAME", length = 64, nullable = false)
    private String className;

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassName() {
        return className;
    }

    @FieldInfo(name = "班级类型 ZXXBJLX数据字典")
    @Column(name = "CLASS_CATEGORY", length = 16, nullable = false)
    private String classCategory;

    public void setClassCategory(String classCategory) {
        this.classCategory = classCategory;
    }

    public String getClassCategory() {
        return classCategory;
    }

    @FieldInfo(name = "班级编号，原则上根据开班的年份+班级类型编码+3位顺序号")
    @Column(name = "CLASS_NUMB", length = 10, nullable = false)
    private String classNumb;

    public void setClassNumb(String classNumb) {
        this.classNumb = classNumb;
    }

    public String getClassNumb() {
        return classNumb;
    }

    @FieldInfo(name = "是否考勤 0-不需要，1-需要")
    @Column(name = "NEED_CHECKING", length = 5, nullable = false)
    private Short needChecking = 0;

    public void setNeedChecking(Short needChecking) {
        this.needChecking = needChecking;
    }

    public Short getNeedChecking() {
        return needChecking;
    }

    @FieldInfo(name = "是否同步学员0:I不同步,1:同步")
    @Column(name = "NEED_SYNCTRAINEE", length = 5, nullable = false)
    private Short needSynctrainee = 0;

    public void setNeedSynctrainee(Short needSynctrainee) {
        this.needSynctrainee = needSynctrainee;
    }

    public Short getNeedSynctrainee() {
        return needSynctrainee;
    }

    @FieldInfo(name = "班级简介")
    @Column(name = "CLASS_DESC", length = 256, nullable = true)
    private String classDesc;

    public void setClassDesc(String classDesc) {
        this.classDesc = classDesc;
    }

    public String getClassDesc() {
        return classDesc;
    }

    @FieldInfo(name = "开始日期")
    @Column(name = "BEGIN_DATE", length = 23, nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using = DateTimeSerializer.class)
    private Date beginDate;

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    @FieldInfo(name = "结束日期")
    @Column(name = "END_DATE", length = 23, nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using = DateTimeSerializer.class)
    private Date endDate;

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    @FieldInfo(name = "班主任ID")
    @Column(name = "BZR_ID", length = 1024, nullable = true)
    private String bzrId;

    public void setBzrId(String bzrId) {
        this.bzrId = bzrId;
    }

    public String getBzrId() {
        return bzrId;
    }

    @FieldInfo(name = "班主任姓名")
    @Column(name = "BZR_NAME", length = 1024, nullable = true)
    private String bzrName;

    public void setBzrName(String bzrName) {
        this.bzrName = bzrName;
    }

    public String getBzrName() {
        return bzrName;
    }

    @FieldInfo(name = "主办单位")
    @Column(name = "HOLD_UNIT", length = 64, nullable = true)
    private String holdUnit;

    public String getHoldUnit() {
        return holdUnit;
    }

    public void setHoldUnit(String holdUnit) {
        this.holdUnit = holdUnit;
    }

    @FieldInfo(name = "承办单位")
    @Column(name = "UNDERTAKER", length = 64, nullable = true)
    private String undertaker;

    public String getUndertaker() {
        return undertaker;
    }

    public void setUndertaker(String undertaker) {
        this.undertaker = undertaker;
    }

    /**
     * 1-围餐 2-自助餐 3-快餐 4-点餐 默认为3
     */
    @FieldInfo(name = "用餐类型")
    @Column(name = "DINNER_TYPE", nullable = true)
    private Integer dinnerType = Integer.valueOf(3);

    public Integer getDinnerType() {
        return dinnerType;
    }

    public void setDinnerType(Integer dinnerType) {
        this.dinnerType = dinnerType;
    }

    /*    @FieldInfo(name = "用餐围数")
        @Column(name = "EAT_NUMBER",nullable = true)
        private Integer eatNumber;

        public Integer getEatNumber() {
            return eatNumber;
        }

        public void setEatNumber(Integer eatNumber) {
            this.eatNumber = eatNumber;
        }*/
    @FieldInfo(name = "每围人数")
    @Column(name = "AVG_NUMBER", nullable = true)
    private Integer avgNumber = Integer.valueOf(10);

    public Integer getAvgNumber() {
        return avgNumber;
    }

    public void setAvgNumber(Integer avgNumber) {
        this.avgNumber = avgNumber;
    }

    @FieldInfo(name = "早餐餐标")
    @Column(name = "BREKFAST_STAND", precision = 8, scale = 2, nullable = true)
    private BigDecimal breakfastStand = BigDecimal.valueOf(20);

    public BigDecimal getBreakfastStand() {
        return breakfastStand;
    }

    public void setBreakfastStand(BigDecimal breakfastStand) {
        this.breakfastStand = breakfastStand;
    }

    @FieldInfo(name = "早餐围/位数")
    @Column(name = "BREAKFAST_COUNT")
    private Integer breakfastCount = 0;

    public Integer getBreakfastCount() {
        return breakfastCount;
    }

    public void setBreakfastCount(Integer breakfastCount) {
        this.breakfastCount = breakfastCount;
    }

    @FieldInfo(name = "午餐餐标")
    @Column(name = "LUNCH_STAND", precision = 8, scale = 2, nullable = true)
    private BigDecimal lunchStand = BigDecimal.valueOf(22);

    public BigDecimal getLunchStand() {
        return lunchStand;
    }

    public void setLunchStand(BigDecimal lunchStand) {
        this.lunchStand = lunchStand;
    }

    @FieldInfo(name = "午餐围/位数")
    @Column(name = "LUNCH_COUNT")
    private Integer lunchCount = 0;

    public Integer getLunchCount() {
        return lunchCount;
    }

    public void setLunchCount(Integer lunchCount) {
        this.lunchCount = lunchCount;
    }

    @FieldInfo(name = "晚餐餐标")
    @Column(name = "DINNER_STAND", precision = 8, scale = 2, nullable = true)
    private BigDecimal dinnerStand = BigDecimal.valueOf(22);

    public BigDecimal getDinnerStand() {
        return dinnerStand;
    }

    public void setDinnerStand(BigDecimal dinnerStand) {
        this.dinnerStand = dinnerStand;
    }

    @FieldInfo(name = "晚餐围/位数")
    @Column(name = "DINNER_COUNT")
    private Integer dinnerCount = 0;

    public Integer getDinnerCount() {
        return dinnerCount;
    }

    public void setDinnerCount(Integer dinnerCount) {
        this.dinnerCount = dinnerCount;
    }

    /*1：启用 ， 默认为0，未提交；1：已提交；2：修改未提交；3：已提交*/
    @FieldInfo(name = "是否提交")
    @Column(name = "ISUSE", length = 10, nullable = false, columnDefinition = "int default(0)")
    private Integer isuse = 0;

    public void setIsuse(Integer isuse) {
        this.isuse = isuse;
    }

    public Integer getIsuse() {
        return isuse;
    }

    /*1：安排 ， 默认为0，未安排；1：已安排（当isuse为3的时候，可以再次安排，安排之后设置isuse为1）*/
    @FieldInfo(name = "是否安排")
    @Column(name = "ISARRANGE", length = 10, nullable = false, columnDefinition = "int default(0)")
    private Integer isarrange = 0;

    public void setIsarrange(Integer isarrange) {
        this.isarrange = isarrange;
    }

    public Integer getIsarrange() {
        return isarrange;
    }

    @FieldInfo(name = "是否需要评价")
    @Column(name = "IS_EVAL", nullable = false)
    private Integer isEval = 0;

    public Integer getIsEval() {
        return isEval;
    }

    public void setIsEval(Integer isEval) {
        this.isEval = isEval;
    }

    @FieldInfo(name = "很满意度")
    @Column(name = "VERY_SATISFACTION", columnDefinition = "numeric(10,2) default 0", nullable = true)
    private BigDecimal verySatisfaction;

    public BigDecimal getVerySatisfaction() {
        return verySatisfaction;
    }

    public void setVerySatisfaction(BigDecimal verySatisfaction) {
        this.verySatisfaction = verySatisfaction;
    }

    @FieldInfo(name = "满意度")
    @Column(name = "SATISFACTION", columnDefinition = "numeric(10,2) default 0", nullable = true)
    private BigDecimal satisfaction;

    public BigDecimal getSatisfaction() {
        return satisfaction;
    }

    public void setSatisfaction(BigDecimal satisfaction) {
        this.satisfaction = satisfaction;
    }

    @FieldInfo(name = "学员人数")
    @Column(name = "TRAINEE_COUNT", columnDefinition = "int default 0", nullable = false)
    private Integer traineeCount = 0;

    public Integer getTraineeCount() {
        return traineeCount;
    }

    @FieldInfo(name = "提交评价人数")
    @Column(name = "EVAL_COUNT", columnDefinition = "int default 0", nullable = false)
    private Integer evalCount=0;

    public Integer getEvalCount() {
        return evalCount;
    }

    public void setEvalCount(Integer evalCount) {
        this.evalCount = evalCount;
    }

    public void setTraineeCount(Integer traineeCount) {
        this.traineeCount = traineeCount;
    }

    @FieldInfo(name = "评价结束日期")
    @Column(name = "EVAL_ENDTIME", columnDefinition = "datetime", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date evalEndtime;

    public Date getEvalEndtime() {
        return evalEndtime;
    }

    public void setEvalEndtime(Date evalEndtime) {
        this.evalEndtime = evalEndtime;
    }

    @FieldInfo(name = "考勤规则Id")
    @Column(name = "CHECKRULE_ID",length = 36,nullable = true)
    private String checkruleId;

    public String getCheckruleId() {
        return checkruleId;
    }

    public void setCheckruleId(String checkruleId) {
        this.checkruleId = checkruleId;
    }

    @FieldInfo(name = "考勤规则名称")
    @Column(name = "CHECKRULE_NAME", length = 128, nullable = true)
    private String checkruleName;

    public String getCheckruleName() {
        return checkruleName;
    }

    public void setCheckruleName(String checkruleName) {
        this.checkruleName = checkruleName;
    }
    
    @FieldInfo(name = "学分规则Id")
    @Column(name = "CREDITSRULE_ID",length = 36,nullable = true)
    private String creditsruleId;

    public String getCreditsruleId() {
        return creditsruleId;
    }

    public void setCreditsruleId(String creditsruleId) {
        this.creditsruleId = creditsruleId;
    }

    @FieldInfo(name = "学分规则名称")
    @Column(name = "CREDITSRULE_NAME", length = 128, nullable = true)
    private String creditsruleName;

    public String getCreditsruleName() {
        return creditsruleName;
    }

    public void setCreditsruleName(String creditsruleName) {
        this.creditsruleName = creditsruleName;
    }
    /*
    @FieldInfo(name = "学员ID")
    @Column(name = "TRINEE_ID", length = 8000, nullable = true)
    private String traineeId;
    public void setTraineeId(String traineeId) {
        this.traineeId = traineeId;
    }
    public String getTraineeId() {
        return bzrId;
    }
    
    @FieldInfo(name = "学员Name")
    @Column(name = "TRINEE_NAME", length = 8000, nullable = true)
    private String traineeName;
    public void setTraineeName(String traineeName) {
        this.traineeName = traineeName;
    }
    public String getTraineeName() {
        return traineeName;
    }
    
    @FieldInfo(name = "课程ID")
    @Column(name = "BZR_ID", length = 8000, nullable = true)
    private String courseId;
    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }
    public String getCourseId() {
        return courseId;
    }
    
    @FieldInfo(name = "课程Name")
    @Column(name = "BZR_NAME", length = 8000, nullable = true)
    private String courseName;
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
    public String getCourseName() {
        return courseName;
    }
    */

    public TrainClass() {
        super();
        // TODO Auto-generated constructor stub
    }

    public TrainClass(String uuid) {
        super(uuid);
        // TODO Auto-generated constructor stub
    }

    /** 以下为不需要持久化到数据库中的字段,根据项目的需要手工增加 
     *@Transient
     *@FieldInfo(name = "")
     *private String field1;
     */
    @FieldInfo(name = "联系人")
    @Formula("(SELECT a.XM FROM dbo.SYS_T_USER a WHERE a.USER_ID=CREATE_USER)")
    private String contactPerson;

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    @FieldInfo(name = "联系电话")
    @Formula("(SELECT a.MOBILE FROM dbo.SYS_T_USER a WHERE a.USER_ID=CREATE_USER)")
    private String contactPhone;

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    @FieldInfo(name = "班级类型名称")
    @Formula("(SELECT a.ITEM_NAME FROM dbo.BASE_T_DICITEM a WHERE a.isDelete=0 and a.DIC_ID=(SELECT b.DIC_ID FROM BASE_T_DIC b WHERE b.DIC_CODE='ZXXBJLX') AND a.ITEM_CODE=(select top 1 c.CLASS_CATEGORY from TRAIN_T_CLASS c where c.CLASS_ID=CLASS_ID))")
    private String classCategoryName;

    public String getClassCategoryName() {
        return classCategoryName;
    }

    public void setClassCategoryName(String classCategoryName) {
        this.classCategoryName = classCategoryName;
    }
}