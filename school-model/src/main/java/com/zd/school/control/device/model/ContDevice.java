package com.zd.school.control.device.model;

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
 * ClassName: ContDevice 
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: CONT_T_DEVICE实体类.
 * date: 2016-08-23
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
 
@Entity
@Table(name = "CONT_T_DEVICE")
@AttributeOverride(name = "uuid", column = @Column(name = "DEVICE_ID", length = 36, nullable = false))
public class ContDevice extends BaseEntity implements Serializable{
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
        
    @FieldInfo(name = "deviceNo")
    @Column(name = "DEVICE_NO", length = 36, nullable = false)
    private String deviceNo;
    public void setDeviceNo(String deviceNo) {
        this.deviceNo = deviceNo;
    }
    public String getDeviceNo() {
        return deviceNo;
    }
        
    @FieldInfo(name = "deviceName")
    @Column(name = "DEVICE_NAME", length = 200, nullable = false)
    private String deviceName;
    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }
    public String getDeviceName() {
        return deviceName;
    }
        
    @FieldInfo(name = "deviceSn")
    @Column(name = "DEVICE_SN", length = 200, nullable = false)
    private String deviceSn;
    public void setDeviceSn(String deviceSn) {
        this.deviceSn = deviceSn;
    }
    public String getDeviceSn() {
        return deviceSn;
    }
        
    @FieldInfo(name = "deviceTypeid")
    @Column(name = "DEVICE_TYPEID", length = 10, nullable = false)
    private Integer deviceTypeid;
    public void setDeviceTypeid(Integer deviceTypeid) {
        this.deviceTypeid = deviceTypeid;
    }
    public Integer getDeviceTypeid() {
        return deviceTypeid;
    }
        
    @FieldInfo(name = "gatewayId")
    @Column(name = "GATEWAY_ID", length = 36, nullable = false)
    private String gatewayId;
    public void setGatewayId(String gatewayId) {
        this.gatewayId = gatewayId;
    }
    public String getGatewayId() {
        return gatewayId;
    }
        
    @FieldInfo(name = "deviceStatus")
    @Column(name = "DEVICE_STATUS", length = 10, nullable = true)
    private Integer deviceStatus;
    public void setDeviceStatus(Integer deviceStatus) {
        this.deviceStatus = deviceStatus;
    }
    public Integer getDeviceStatus() {
        return deviceStatus;
    }
        
    @FieldInfo(name = "advparam")
    @Column(name = "ADVPARAM", length = 36, nullable = true)
    private String advparam;
    public void setAdvparam(String advparam) {
        this.advparam = advparam;
    }
    public String getAdvparam() {
        return advparam;
    }
        
    @FieldInfo(name = "baseparam")
    @Column(name = "BASEPARAM", length = 36, nullable = true)
    private String baseparam;
    public void setBaseparam(String baseparam) {
        this.baseparam = baseparam;
    }
    public String getBaseparam() {
        return baseparam;
    }
        
    @FieldInfo(name = "netparam")
    @Column(name = "NETPARAM", length = 36, nullable = true)
    private String netparam;
    public void setNetparam(String netparam) {
        this.netparam = netparam;
    }
    public String getNetparam() {
        return netparam;
    }
        
    @FieldInfo(name = "notes")
    @Column(name = "NOTES", length = 1000, nullable = true)
    private String notes;
    public void setNotes(String notes) {
        this.notes = notes;
    }
    public String getNotes() {
        return notes;
    }
        
    @FieldInfo(name = "rateparam")
    @Column(name = "RATEPARAM", length = 36, nullable = true)
    private String rateparam;
    public void setRateparam(String rateparam) {
        this.rateparam = rateparam;
    }
    public String getRateparam() {
        return rateparam;
    }
        
    @FieldInfo(name = "offlineuse")
    @Column(name = "OFFLINEUSE", length = 10, nullable = true)
    private Integer offlineuse;
    public void setOfflineuse(Integer offlineuse) {
        this.offlineuse = offlineuse;
    }
    public Integer getOfflineuse() {
        return offlineuse;
    }
        

    /** 以下为不需要持久化到数据库中的字段,根据项目的需要手工增加 
    *@Transient
    *@FieldInfo(name = "")
    *private String field1;
    */
}