package com.zd.school.jw.train.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Formula;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.zd.core.annotation.FieldInfo;
import com.zd.core.model.BaseEntity;
import com.zd.core.util.DateTimeSerializer;
import com.zd.school.excel.annotation.MapperCell;
import com.zd.school.jw.train.model.vo.VoTrainClasstrainee;
import com.zd.school.plartform.comm.model.CommAttachment;

/**
 * 
 * ClassName: TrainClassschedule Function: TODO ADD FUNCTION. Reason: TODO ADD
 * REASON(可选). Description: 班级课程日历(TRAIN_T_CLASSSCHEDULE)实体类. date: 2017-03-07
 * 分班原则：若班级课程是分班的，则学员必须是对应分班的才能上课
 * 			若班级课程是不分班的，则所有学员可以来上课
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */

@Entity
@Table(name = "TRAIN_T_CLASSSCHEDULE")
@AttributeOverride(name = "uuid", column = @Column(name = "CLASS_SCHEDULE_ID", length = 36, nullable = false) )
public class TrainClassschedule extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@FieldInfo(name = "班级ID")
	@Column(name = "CLASS_ID", length = 36, nullable = false)
	private String classId;

	public void setClassId(String classId) {
		this.classId = classId;
	}

	public String getClassId() {
		return classId;
	}

	@FieldInfo(name = "课程ID")
	@Column(name = "COURSE_ID", length = 36, nullable = true)
	private String courseId;

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	public String getCourseId() {
		return courseId;
	}

	@FieldInfo(name = "课程名称")
	@Column(name = "COURSE_NAME", length = 128, nullable = false)
	private String courseName;

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getCourseName() {
		return courseName;
	}

	@FieldInfo(name = "教室ID")
	@Column(name = "ROOM_ID", length = 1024, nullable = true)
	private String roomId;

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	@FieldInfo(name = "授课地点")
	@Column(name = "SCHEDULE_ADDRESS", length = 1024, nullable = true)
	private String scheduleAddress;

	public void setScheduleAddress(String scheduleAddress) {
		this.scheduleAddress = scheduleAddress;
	}

	public String getScheduleAddress() {
		return scheduleAddress;
	}

	@FieldInfo(name = "授课模式,主要是针对有些课多个讲师分别主讲,有的要多个讲师一起上课 1-单一模式 2-群组模式")
	@Column(name = "COURSE_MODE", length = 5, nullable = false)
	private Integer courseMode = 1;

	public Integer getCourseMode() {
		return courseMode;
	}

	public void setCourseMode(Integer courseMode) {
		this.courseMode = courseMode;
	}

	@FieldInfo(name = "开始时间")
	@Column(name = "BEGIN_TIME", nullable = false, columnDefinition = "datetime")
	@Temporal(TemporalType.TIMESTAMP)
	@JsonSerialize(using = DateTimeSerializer.class)
	private Date beginTime;

	public void setBeginTime(Date beginTime) {
		if (beginTime == null)
			this.beginTime = null;
		else
			this.beginTime = (Date) beginTime.clone();
	}

	public Date getBeginTime() {
		if (beginTime == null)
			return null;

		return (Date) beginTime.clone();
	}

	@FieldInfo(name = "结束时间")
	@Column(name = "END_TIME", nullable = false, columnDefinition = "datetime")
	@Temporal(TemporalType.TIMESTAMP)
	@JsonSerialize(using = DateTimeSerializer.class)
	private Date endTime;

	public void setEndTime(Date endTime) {
		if (endTime == null)
			this.endTime = null;
		else
			this.endTime = (Date) endTime.clone();
	}

	public Date getEndTime() {
		if (endTime == null)
			return null;

		return (Date) endTime.clone();
	}

	@FieldInfo(name = "主讲老师ID")
	@Column(name = "MAIN_TEACHER_ID", length = 1024, nullable = true)
	private String mainTeacherId;

	public void setMainTeacherId(String mainTeacherId) {
		this.mainTeacherId = mainTeacherId;
	}

	public String getMainTeacherId() {
		return mainTeacherId;
	}

	@FieldInfo(name = "主讲老师Name")
	@Column(name = "MAIN_TEACHER_NAME", length = 1024, nullable = true)
	private String mainTeacherName;

	public void setMainTeacherName(String mainTeacherName) {
		this.mainTeacherName = mainTeacherName;
	}

	public String getMainTeacherName() {
		return mainTeacherName;
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

	@FieldInfo(name = "排名")
	@Column(name = "RANKING")
	private Integer ranking;

	public Integer getRanking() {
		return ranking;
	}

	public void setRanking(Integer ranking) {
		this.ranking = ranking;
	}

	@FieldInfo(name = "是否需要评价")
	@Column(name = "IS_EVAL", nullable = true)
	private Integer isEval;

	public Integer getIsEval() {
		return isEval;
	}

	public void setIsEval(Integer isEval) {
		this.isEval = isEval;
	}

	@FieldInfo(name = "评价状态")
	@Column(name = "EVAL_STATE", columnDefinition = "int default 0", nullable = false)
	private Integer evalState = 0;

	public Integer getEvalState() {
		return evalState;
	}

	public void setEvalState(Integer evalState) {
		this.evalState = evalState;
	}

	@MapperCell(cellName = "课程学分", order = 6)
	@FieldInfo(name = "学分")
	@Column(name = "CREDITS", length = 5, nullable = false)
	private Integer credits = 4;

	public Integer getCredits() {
		return credits;
	}

	public void setCredits(Integer credits) {
		this.credits = credits;
	}


	// 自选课程，则学员没有刷卡，则不会算做缺勤
	@FieldInfo(name = "是否自选课程")
	@Column(name = "IS_OPTIONAL", columnDefinition = "int default 0", nullable = true)
	private Integer isOptional = 0;

	public Integer getIsOptional() {
		return isOptional;
	}

	public void setIsOptional(Integer isOptional) {
		this.isOptional = isOptional;
	}
	
	//课程分班：不分班、A班、B班 （0、1、2）
    @FieldInfo(name = "分组班级名称 CLASSGROUP字典")
    @Column(name = "CLASSGROUP", length = 8, nullable = true)
    private String classGroup;

    public String getClassGroup() {
        return classGroup;
    }

    public void setClassGroup(String classGroup) {
        this.classGroup = classGroup;
    }
	/**
	 * 以下为不需要持久化到数据库中的字段,根据项目的需要手工增加
	 * 
	 * @Transient
	 * @FieldInfo(name = "") private String field1;
	 */

	@FieldInfo(name = "班级名称")
	@Formula("(SELECT a.CLASS_NAME FROM TRAIN_T_CLASS a where a.CLASS_ID=CLASS_ID)")
	private String className;

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	@FieldInfo(name = "教学形式名称")
	@Formula("(SELECT a.ITEM_NAME FROM dbo.BASE_T_DICITEM a WHERE a.isDelete=0 and a.DIC_ID=(SELECT b.DIC_ID FROM BASE_T_DIC b WHERE b.DIC_CODE='TEACHTYPE') AND a.ITEM_CODE=(select top 1 c.TEACH_TYPE from TRAIN_T_COURSEINFO c where c.COURSE_ID=COURSE_ID))")
	private String teachTypeName;

	public String getTeachTypeName() {
		return teachTypeName;
	}

	public void setTeachTypeName(String teachTypeName) {
		this.teachTypeName = teachTypeName;
	}

	//学员数据
	@Transient
	private List<VoTrainClasstrainee> list;

	public List<VoTrainClasstrainee> getList() {
		return list;
	}

	public void setList(List<VoTrainClasstrainee> list) {
		this.list = list;
	}

	//考勤规则
	@Transient
	private TrainCheckrule checkRule;

	public TrainCheckrule getCheckRule() {
		return checkRule;
	}

	public void setCheckRule(TrainCheckrule checkRule) {
		this.checkRule = checkRule;
	}

	//学员数据
	@Transient
	private List<CommAttachment> attachmentlist;

	public List<CommAttachment> getAttachmentlist() {
		return attachmentlist;
	}

	public void setAttachmentlist(List<CommAttachment> attachmentlist) {
		this.attachmentlist = attachmentlist;
	}

}