package com.zd.school.build.allot.model;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.zd.core.annotation.FieldInfo;
import com.zd.core.model.BaseEntity;

/**
 * 
 * ClassName: JwLaboratoryallot 
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: JW_T_LABORATORYALLOT实体类.
 * date: 2016-08-23
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
 
@Entity
@Table(name = "JW_T_LABORATORYALLOT")
@AttributeOverride(name = "uuid", column = @Column(name = "LABORATORYALLOT_ID", length = 50, nullable = false))
public class JwLaboratoryAllot extends BaseEntity implements Serializable{
    private static final long serialVersionUID = 1L;
    
        
    @FieldInfo(name = "roomId")
    @Column(name = "ROOM_ID", length = 36, nullable = true)
    private String roomId;
    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }
    public String getRoomId() {
        return roomId;
    }


    /** 以下为不需要持久化到数据库中的字段,根据项目的需要手工增加 
    *@Transient
    *@FieldInfo(name = "")
    *private String field1;
    */
    
//	@Formula("(SELECT A.ROOM_NAME FROM dbo.BUILD_T_ROOMINFO A WHERE A.ROOM_ID=ROOM_ID)")
//	@FieldInfo(name = "房间名称")
//	private String roomName;
//
//	@Formula("(SELECT C.AREA_NAME FROM dbo.JW_T_CLASSROOMALLOT A JOIN dbo.JW_T_ROOMINFO B"
//			+ " ON A.ROOM_ID=B.ROOM_ID JOIN dbo.JW_T_ROOMAREA C ON B.AREA_ID=C.AREA_ID WHERE A.ISDELETE=0)")
//	@FieldInfo(name = "楼层名称")
//	private String areaName;

}