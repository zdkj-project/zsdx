package com.zd.core.util;

import org.springframework.stereotype.Component;

/**
 * 
 * DataSource操作类（设置DataSource）
 * @author huangzc
 *
 */
@Component
public class DBContextHolder{
	 	public static final String DATA_SOURCE_Q1 = "dataSourceQ1";  
	    public static final String DATA_SOURCE_OA = "dataSourceSyncOA"; 
	    public static final String DATA_SOURCE_UP6 = "dataSourceUP6"; 
	    //public static final String DATA_SOURCE_MYSQL = "dataSourceMysql"; 
	    
	    
	    private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();  
	      
	    public static void setDBType(String dbType) {  
	        contextHolder.set(dbType);  
	    }  
	      
	    public static String getDBType() {  
	        return contextHolder.get();  
	    }  
	      
	    public static void clearDBType() {  
	        contextHolder.remove();  
	        contextHolder.set(DBContextHolder.DATA_SOURCE_Q1);  //重置后，强行设置为q1库
	    }

}
