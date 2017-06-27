package com.zd.school.oa.terminal.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
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

/**
 * 
 * ClassName: OaInfotermuse 
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 终端使用历史(OA_T_INFOTERMUSE)实体类.
 * date: 2017-01-14
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
 
@Entity
@Table(name = "OA_T_INFOTERMUSE")
@AttributeOverride(name = "uuid", column = @Column(name = "USE_ID", length = 36, nullable = false))
public class OaInfotermuse extends BaseEntity implements Serializable{
    private static final long serialVersionUID = 1L;
    
    @FieldInfo(name = "终端主键ID")
    @Column(name = "TERM_ID", length = 36, nullable = false)
    private String termId;
    public void setTermId(String termId) {
        this.termId = termId;
    }
    public String getTermId() {
        return termId;
    }
        
    @FieldInfo(name = "终端号")
    @Column(name = "TERM_CODE", length = 6, nullable = false)
    private String termCode;
    public void setTermCode(String termCode) {
        this.termCode = termCode;
    }
    public String getTermCode() {
        return termCode;
    }
        
    @FieldInfo(name = "使用房间ID")
    @Column(name = "ROOM_ID", length = 36, nullable = true)
    private String roomId;
    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }
    public String getRoomId() {
        return roomId;
    }
        
    @FieldInfo(name = "使用房间名称")
    @Column(name = "ROOM_NAME", length = 64, nullable = true)
    private String roomName;
    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }
    public String getRoomName() {
        return roomName;
    }
        

    /** 以下为不需要持久化到数据库中的字段,根据项目的需要手工增加 
    *@Transient
    *@FieldInfo(name = "")
    *private String field1;
    */
}