package com.zd.school.oa.meeting.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.math.BigDecimal;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.zd.core.annotation.FieldInfo;
import com.zd.core.model.BaseEntity;
import com.zd.core.util.DateTimeSerializer;
import com.zd.school.excel.annotation.MapperCell;

/**
 * 
 * ClassName: OaMeeting 
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 会议信息(OA_T_MEETING)实体类.
 * date: 2017-03-07
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
 
@Entity
@Table(name = "OA_T_MEETING")
@AttributeOverride(name = "uuid", column = @Column(name = "MEETING_ID", length = 36, nullable = false))
public class OaMeeting extends BaseEntity implements Serializable{
    private static final long serialVersionUID = 1L;
    
    @MapperCell(cellName = "会议主题", order = 1)
    @FieldInfo(name = "会议主题")
    @Column(name = "MEETING_TITLE", length = 1024, nullable = false)
    private String meetingTitle;
    public void setMeetingTitle(String meetingTitle) {
        this.meetingTitle = meetingTitle;
    }
    public String getMeetingTitle() {
        return meetingTitle;
    }
        
    @MapperCell(cellName = "会议名称", order = 2)
    @FieldInfo(name = "会议名称")
    @Column(name = "MEETING_NAME", length = 1024, nullable = false)
    private String meetingName;
    public void setMeetingName(String meetingName) {
        this.meetingName = meetingName;
    }
    public String getMeetingName() {
        return meetingName;
    }
        
    @FieldInfo(name = "会议类型 MEETINGCATEGORY数据字典")
    @Column(name = "MEETING_CATEGORY", length = 16, nullable = false)
    private String meetingCategory;
    public void setMeetingCategory(String meetingCategory) {
        this.meetingCategory = meetingCategory;
    }
    public String getMeetingCategory() {
        return meetingCategory;
    }
        
    @FieldInfo(name = "主持人")
    @Column(name = "EMCEE", length = 16, nullable = true)
    private String emcee;
    public void setEmcee(String emcee) {
        this.emcee = emcee;
    }
    public String getEmcee() {
        return emcee;
    }
        
    @FieldInfo(name = "是否考勤 0-不需要，1-需要")
    @Column(name = "NEED_CHECKING", length = 5, nullable = false)
    private Short needChecking;
    public void setNeedChecking(Short needChecking) {
        this.needChecking = needChecking;
    }
    public Short getNeedChecking() {
        return needChecking;
    }
        
    @MapperCell(cellName = "会议内容", order = 3)
    @FieldInfo(name = "会议内容")
    @Column(name = "MEETING_CONTENT", length = 2048, nullable = true)
    private String meetingContent;
    public void setMeetingContent(String meetingContent) {
        this.meetingContent = meetingContent;
    }
    public String getMeetingContent() {
        return meetingContent;
    }
        
    @FieldInfo(name = "房间ID")
    @Column(name = "ROOM_ID", length = 36, nullable = true)
    private String roomId;
    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }
    public String getRoomId() {
        return roomId;
    }
        
    @MapperCell(cellName = "房间名称", order = 5)
    @FieldInfo(name = "房间名称")
    @Column(name = "ROOM_NAME", length = 32, nullable = false)
    private String roomName;
    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }
    public String getRoomName() {
        return roomName;
    }
        
    @FieldInfo(name = " 会议人员ID,多人用英文逗号隔开")
    @Column(name = "METTING_EMPID", length = 2048, nullable = true)
    private String mettingEmpid;
    public void setMettingEmpid(String mettingEmpid) {
        this.mettingEmpid = mettingEmpid;
    }
    public String getMettingEmpid() {
        return mettingEmpid;
    }
        
    @FieldInfo(name = "会议人员姓名,多人用英文逗号隔开，和ID顺序对应")
    @Column(name = "MEETING_EMPNMAE", length = 1024, nullable = true)
    private String mettingEmpname;
        
    public String getMettingEmpname() {
		return mettingEmpname;
	}
	public void setMettingEmpname(String mettingEmpname) {
		this.mettingEmpname = mettingEmpname;
	}

	@FieldInfo(name = "会议状态0：未开始 1:进行中 2:已完成")
    @Column(name = "MEETING_STATE", length = 5, nullable = true)
    private Short meetingState=0;	//默认为0
    public void setMeetingState(Short meetingState) {
        this.meetingState = meetingState;
    }
    public Short getMeetingState() {
        return meetingState;
    }
        
    @FieldInfo(name = "考勤结果")
    @Column(name = "ATTEND_RESULT", length = 16, nullable = true)
    private String attendResult;
    public void setAttendResult(String attendResult) {
        this.attendResult = attendResult;
    }
    public String getAttendResult() {
        return attendResult;
    }
        
    @MapperCell(cellName = "开始时间", order = 6)
    @FieldInfo(name = "开始时间")
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
        
    @MapperCell(cellName = "结束时间", order = 7)
    @FieldInfo(name = "结束时间")
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
    

	public OaMeeting() {
		super();
		// TODO Auto-generated constructor stub
	}

	public OaMeeting(String uuid) {
		super(uuid);
		// TODO Auto-generated constructor stub
	}
        
	
	@Transient
	private String begin;
	@Transient
	private String end;
	public String getBegin() {
		return begin;
	}
	public void setBegin(String begin) {
		this.begin = begin;
	}
	public String getEnd() {
		return end;
	}
	public void setEnd(String end) {
		this.end = end;
	}
	
	
	//封装app数据需要
	@Transient
    private List<OaMeetingemp> meetingemp;
    
    
	public List<OaMeetingemp> getMeetingemp() {
		return meetingemp;
	}
	public void setMeetingemp(List<OaMeetingemp> meetingemp) {
		this.meetingemp = meetingemp;
	}
	
    @MapperCell(cellName = "会议类型", order = 4)
    @Transient
    @FieldInfo(name = "会议类型")
    private String meetingCategoryName;
	public String getMeetingCategoryName() {
		return meetingCategoryName;
	}
	public void setMeetingCategoryName(String meetingCategoryName) {
		this.meetingCategoryName = meetingCategoryName;
	}


    /** 以下为不需要持久化到数据库中的字段,根据项目的需要手工增加 
    *@Transient
    *@FieldInfo(name = "")
    *private String field1;
    */
}