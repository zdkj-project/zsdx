package com.zd.school.plartform.system.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.zd.core.annotation.FieldInfo;
import com.zd.core.model.BaseEntity;
import com.zd.core.util.DateTimeSerializer;



/**
 * 
 * ClassName: SysOperateLog 
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 用户操作日志信息(SYS_OPERATE_LOG)实体类.
 * date: 2017-09-15
 *
 * @author  zzk 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
 
@Entity
@Table(name = "SYS_T_OPERATE_LOG")
@AttributeOverride(name = "uuid", column = @Column(name = "OperateLog_ID", length = 36, nullable = false))
public class SysOperateLog extends BaseEntity implements Serializable{
    private static final long serialVersionUID = 1L;
    
    @FieldInfo(name = "用户ID")
    @Column(name = "USER_ID", length = 36, nullable = true)
    private String userId;
   
    @FieldInfo(name = "IP地址")
    @Column(name = "IP_HOST", length = 64, nullable = true)
    private String ipHost;
   
    
    @FieldInfo(name = "方法名")
    @Column(name = "METHOD_NAME", length = 500, nullable = true)
    private String methodName;
    

    @FieldInfo(name = "参数")
    @Column(name = "METHOD_PARAMS", columnDefinition = "varchar(MAX)", nullable = true)
    private String methodParams;
    
    @FieldInfo(name = "返回结果")
    @Column(name = "METHOD_RESULT", columnDefinition = "varchar(MAX)", nullable = true)
    private String methodResult;
    
    @FieldInfo(name = "异常类型")
    @Column(name = "EXCEPTION_CLASS", length = 100, nullable = true)
    private String exceptionClass;
    
    @FieldInfo(name = "异常信息")
    @Column(name = "EXCEPTION_Detail", length = 500, nullable = true)
    private String exceptionDetail;
    
    @FieldInfo(name = "操作时间")
    @Column(name = "OPERATE_DATE", length = 23, nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using=DateTimeSerializer.class)
    private Date operateDate;
    
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getIpHost() {
		return ipHost;
	}

	public void setIpHost(String ipHost) {
		this.ipHost = ipHost;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getMethodParams() {
		return methodParams;
	}

	public void setMethodParams(String methodParams) {
		this.methodParams = methodParams;
	}

	public String getMethodResult() {
		return methodResult;
	}

	public void setMethodResult(String methodResult) {
		this.methodResult = methodResult;
	}

	public String getExceptionClass() {
		return exceptionClass;
	}

	public void setExceptionClass(String exceptionClass) {
		this.exceptionClass = exceptionClass;
	}

	public String getExceptionDetail() {
		return exceptionDetail;
	}

	public void setExceptionDetail(String exceptionDetail) {
		this.exceptionDetail = exceptionDetail;
	}

	public Date getOperateDate() {
		return operateDate;
	}

	public void setOperateDate(Date operateDate) {
		this.operateDate = operateDate;
	}
	
	
}
    
    
    
    
    
    
    
    