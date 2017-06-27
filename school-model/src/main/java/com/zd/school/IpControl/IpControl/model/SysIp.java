package com.zd.school.IpControl.IpControl.model;

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
 * ClassName: SysIp 
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: (SYS_T_IP)实体类.
 * date: 2017-05-10
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
 
@Entity
@Table(name = "SYS_T_IP")
@AttributeOverride(name = "uuid", column = @Column(name = "IP_ID", length = 50, nullable = false))
public class SysIp extends BaseEntity implements Serializable{
    private static final long serialVersionUID = 1L;
    
    @FieldInfo(name = "ipName")
    @Column(name = "IP_NAME", length = 50, nullable = true)
    private String ipName;
    public void setIpName(String ipName) {
        this.ipName = ipName;
    }
    public String getIpName() {
        return ipName;
    }
        
    @FieldInfo(name = "ipUrl")
    @Column(name = "IP_URL", length = 50, nullable = true)
    private String ipUrl;
    public void setIpUrl(String ipUrl) {
        this.ipUrl = ipUrl;
    }
    public String getIpUrl() {
        return ipUrl;
    }
        

    /** 以下为不需要持久化到数据库中的字段,根据项目的需要手工增加 
    *@Transient
    *@FieldInfo(name = "")
    *private String field1;
    */
}