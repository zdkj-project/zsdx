package com.zd.school.oa.meeting.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.zd.core.annotation.FieldInfo;
import com.zd.core.model.BaseEntity;
import com.zd.core.util.DateTimeSerializer;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 
 * ClassName: OaMeetingemp 
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 会议人员信息(OA_T_MEETINGEMP)实体类.
 * date: 2017-03-07
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
 
@Entity
@Table(name = "OA_T_MEETINGEMP")
@AttributeOverride(name = "uuid", column = @Column(name = "MEETINGEMP_ID", length = 255, nullable = false))
public class OaMeetingemp extends BaseEntity implements Serializable{
    private static final long serialVersionUID = 1L;
    
    @FieldInfo(name = "会议ID")
    @Column(name = "MEETING_ID", length = 36, nullable = false)
    private String meetingId;
    public void setMeetingId(String meetingId) {
        this.meetingId = meetingId;
    }
    public String getMeetingId() {
        return meetingId;
    }
        
    @FieldInfo(name = "教职工ID")
    @Column(name = "EMPLOYEE_ID", length = 36, nullable = true)
    private String employeeId;
    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }
    public String getEmployeeId() {
        return employeeId;
    }
    
    @FieldInfo(name = "姓名")
    @Column(name = "XM", length = 36, nullable = true)
    private String xm;
        
    public String getXm() {
		return xm;
	}
	public void setXm(String xm) {
		this.xm = xm;
	}
        
    @FieldInfo(name = "签到状态,考勤结果数据字典")
    @Column(name = "IN_RESULT", length = 16, nullable = true)
    private String inResult;
    public void setInResult(String inResult) {
        this.inResult = inResult;
    }
    public String getInResult() {
        return inResult;
    }
        
    @FieldInfo(name = "签退状态")
    @Column(name = "OUT_RESULT", length = 16, nullable = true)
    private String outResult;
    public void setOutResult(String outResult) {
        this.outResult = outResult;
    }
    public String getOutResult() {
        return outResult;
    }
        
    @FieldInfo(name = "考勤结果 1-正常 2-迟到 3-早退 4-缺勤5-迟到早退 ")
    @Column(name = "ATTEND_RESULT", length = 16, nullable = true)
    private String attendResult;
    public void setAttendResult(String attendResult) {
        this.attendResult = attendResult;
    }
    public String getAttendResult() {
        return attendResult;
    }
        
    @FieldInfo(name = "结果说明")
    @Column(name = "RESULT_DESC", length = 64, nullable = true)
    private String resultDesc;
    public void setResultDesc(String resultDesc) {
        this.resultDesc = resultDesc;
    }
    public String getResultDesc() {
        return resultDesc;
    }
    

	@FieldInfo(name = "会议开始时间")
    @Column(name = "BEGIN_TIME", length = 23, nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using=DateTimeSerializer.class)
    private Date beginTime;
    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }
    public Date getBeginTime() {
        return beginTime;
    }
        
    @FieldInfo(name = "签到刷卡时间")
    @Column(name = "INCARD_TIME", length = 23, nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using=DateTimeSerializer.class)
    private Date incardTime;
    public void setIncardTime(Date incardTime) {
        this.incardTime = incardTime;
    }
    public Date getIncardTime() {
        return incardTime;
    }
        
    @FieldInfo(name = "会议结束时间")
    @Column(name = "END_TIME", length = 23, nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using=DateTimeSerializer.class)
    private Date endTime;
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
    public Date getEndTime() {
        return endTime;
    }
        
    @FieldInfo(name = "签退刷卡时间")
    @Column(name = "OUTCARD_TIME", length = 23, nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using=DateTimeSerializer.class)
    private Date outcardTime;
    public void setOutcardTime(Date outcardTime) {
        this.outcardTime = outcardTime;
    }
    public Date getOutcardTime() {
        return outcardTime;
    }
    
	public OaMeetingemp() {
		super();
		// TODO Auto-generated constructor stub
	}

	public OaMeetingemp(String uuid) {
		super(uuid);
		// TODO Auto-generated constructor stub
	}
     
	@Transient
	private String cardNo;
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	
	@Transient
	private String factoryfixId;
	public String getFactoryfixId() {
		return factoryfixId;
	}
	public void setFactoryfixId(String factoryfixId) {
		this.factoryfixId = factoryfixId;
	}
	

    /** 以下为不需要持久化到数据库中的字段,根据项目的需要手工增加 
    *@Transient
    *@FieldInfo(name = "")
    *private String field1;
    */
}