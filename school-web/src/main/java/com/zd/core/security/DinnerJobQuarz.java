package com.zd.core.security;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.zd.core.util.DBContextHolder;
import com.zd.school.plartform.system.service.SysUserService;

/**
 * 处理未预定早餐的人，无法刷卡的功能
 * 废弃
 * 改为在执行刷卡的存储过程中进行判断
 * @author Administrator
 *
 */
public class DinnerJobQuarz {
	private static Logger logger = Logger.getLogger(DinnerJobQuarz.class);
	
	@Resource
	private SysUserService thisService; // service层接
	
	/**
	 * 禁用未订餐的卡片（凌晨执行）
	 * @throws InterruptedException 
	 */
	protected void  cardDisable() throws InterruptedException {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		Date currentDate=new Date();	//当天
		String sql1="SELECT user_id"
				+ " FROM TRAIN_T_TEACHERORDER "
				+ " where dinner_date='"+sdf.format(currentDate)+"'";
		List<Map<String,Object>> userList=thisService.getForValuesToSql(sql1);
		
		String str=userList.stream()
			.map((e)->String.valueOf(e.get("user_id")))
			.collect(Collectors.joining(","));
		
		//切换数据库
		DBContextHolder.setDBType(DBContextHolder.DATA_SOURCE_UP6);
		
		String sql2="update b set b.cardStatusIDXF='4' from TC_EMPLOYEE a "
				+ "join TC_card b on (a.cardid=b.cardid and a.employeeID=b.employeeID) "
				+ "where a.userid not in ('"
				+  str.replace(",", "','")
				+ "')"
				+ "and a.departmentId not like 'Train%' "
				+ "and a.employeeStatusId='24' "
				+ "and a.employeeName not like '%测%试%' "
				+ "and a.employeeName not like '餐饮部卡' "
				+ "and b.cardStatusIDXF='1' ";
		
		//最多重试执行3次
		for(int i=0;i<3;i++){
			try{			
				thisService.executeSql(sql2);			
				break;
				
			}catch(Exception e){
				logger.error("禁用未订餐的卡片，执行失败; "+e.getMessage());
				logger.error(Arrays.toString( e.getStackTrace()));
				
				Thread.sleep(1000*30);	//30秒后再重试			
			}
		}
			
		DBContextHolder.clearDBType();
	}
	
	/**
	 * 恢复未订餐的卡片（10:10执行）
	 * @throws InterruptedException 
	 */
	protected void  cardAble() throws InterruptedException {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		Date currentDate=new Date();	//当天
		String sql1="SELECT user_id"
				+ " FROM TRAIN_T_TEACHERORDER "
				+ " where dinner_date='"+sdf.format(currentDate)+"'";
		List<Map<String,Object>> userList=thisService.getForValuesToSql(sql1);
		
		String str=userList.stream()
			.map((e)->String.valueOf(e.get("user_id")))
			.collect(Collectors.joining(","));
		
		//切换数据库
		DBContextHolder.setDBType(DBContextHolder.DATA_SOURCE_UP6);
		
		String sql2="update b set b.cardStatusIDXF='1' from TC_EMPLOYEE a "
				+ "join TC_card b on (a.cardid=b.cardid and a.employeeID=b.employeeID) "
				+ "where a.userid not in ('"
				+  str.replace(",", "','")
				+ "')"
				+ "and a.departmentId not like 'Train%' "
				+ "and a.employeeStatusId='24' "
				+ "and a.employeeName not like '%测%试%' "
				+ "and a.employeeName not like '餐饮部卡' "
				+ "and b.cardStatusIDXF='4' ";
		
		//最多重试执行3次
		for(int i=0;i<3;i++){
			try{			
				thisService.executeSql(sql2);			
				break;
				
			}catch(Exception e){
				logger.error("恢复未订餐的卡片，执行失败; "+e.getMessage());
				logger.error(Arrays.toString( e.getStackTrace()));
				
				Thread.sleep(1000*30);	//30秒后再重试			
			}
		}
		DBContextHolder.clearDBType();
		
		
	}
	
	
}
