package com.zd.school.plartform.system.model;

import com.zd.core.annotation.FieldInfo;
import com.zd.core.model.BaseEntity;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 
 * ClassName: SysAppinfo 
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: (SYS_T_APPINFO)实体类.
 * date: 2017-05-15
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
 
@Entity
@Table(name = "SYS_T_APPINFO")
@AttributeOverride(name = "uuid", column = @Column(name = "APP_ID", length = 36, nullable = false))
public class SysAppinfo extends BaseEntity implements Serializable{
    private static final long serialVersionUID = 1L;
    
    @FieldInfo(name = "appIntro")
    @Column(name = "APP_INTRO", length = 256, nullable = true)
    private String appIntro;
    public void setAppIntro(String appIntro) {
        this.appIntro = appIntro;
    }
    public String getAppIntro() {
        return appIntro;
    }
        
    @FieldInfo(name = "appIsuse")
    @Column(name = "APP_ISUSE", length = 10, nullable = true)
    private Integer appIsuse;
    public void setAppIsuse(Integer appIsuse) {
        this.appIsuse = appIsuse;
    }
    public Integer getAppIsuse() {
        return appIsuse;
    }
        
    @FieldInfo(name = "appTitle")
    @Column(name = "APP_TITLE", length = 128, nullable = true)
    private String appTitle;
    public void setAppTitle(String appTitle) {
        this.appTitle = appTitle;
    }
    public String getAppTitle() {
        return appTitle;
    }
        
    @FieldInfo(name = "appType 1=会议考勤 2-课程考勤")
    @Column(name = "APP_TYPE", length = 4, nullable = true)
    private String appType;
    public void setAppType(String appType) {
        this.appType = appType;
    }
    public String getAppType() {
        return appType;
    }
        
    @FieldInfo(name = "下载地址")
    @Column(name = "APP_URL", length = 256, nullable = true)
    private String appUrl;
    public void setAppUrl(String appUrl) {
        this.appUrl = appUrl;
    }
    public String getAppUrl() {
        return appUrl;
    }
        
    @FieldInfo(name = "APP版本")
    @Column(name = "APP_VERSION", length = 10, nullable = true)
    private Integer appVersion;
    public void setAppVersion(Integer appVersion) {
        this.appVersion = appVersion;
    }
    public Integer getAppVersion() {
        return appVersion;
    }
        

    /** 以下为不需要持久化到数据库中的字段,根据项目的需要手工增加 
    *@Transient
    *@FieldInfo(name = "")
    *private String field1;
    */
}