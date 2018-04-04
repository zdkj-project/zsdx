package com.zd.school.plartform.system.service;



import com.zd.core.service.BaseService;
import com.zd.school.plartform.system.model.SysOperateLog;

public interface SysOperateLogService extends BaseService<SysOperateLog> {
	/**
	 * 防止这个日志记录时，再去调用do开头的service方法，造成死循环日志记录。
	 * @param entity
	 */
	public void addLog(SysOperateLog entity);

}
