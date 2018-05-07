package com.zd.school.oa.meeting.service.Impl;

import com.zd.core.model.extjs.QueryResult;
import com.zd.core.service.BaseServiceImpl;
import com.zd.core.util.BeanUtils;
import com.zd.core.util.StringUtils;
import com.zd.school.build.define.model.BuildRoominfo;
import com.zd.school.build.define.service.BuildRoominfoService;
import com.zd.school.oa.meeting.dao.OaMeetingDao;
import com.zd.school.oa.meeting.model.OaMeeting;
import com.zd.school.oa.meeting.model.OaMeetingcheckrule;
import com.zd.school.oa.meeting.model.OaMeetingemp;
import com.zd.school.oa.meeting.service.OaMeetingService;
import com.zd.school.oa.meeting.service.OaMeetingcheckruleService;
import com.zd.school.oa.meeting.service.OaMeetingempService;
import com.zd.school.plartform.baseset.model.BaseDicitem;
import com.zd.school.plartform.baseset.service.BaseDicitemService;
import com.zd.school.plartform.system.model.SysUser;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * ClassName: OaMeetingServiceImpl
 * Function:  ADD FUNCTION. 
 * Reason:  ADD REASON(可选). 
 * Description: 会议信息(OA_T_MEETING)实体Service接口实现类.
 * date: 2017-03-07
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Service
@Transactional
public class OaMeetingServiceImpl extends BaseServiceImpl<OaMeeting> implements OaMeetingService{

    @Resource
    public void setOaMeetingDao(OaMeetingDao dao) {
        this.dao = dao;
    }
	private static Logger logger = Logger.getLogger(OaMeetingServiceImpl.class);

    @Resource
    private OaMeetingempService meetingempService;

    @Resource
    private BaseDicitemService dicitemService;
    
    @Resource
    private BuildRoominfoService buildService;
    
    @Resource
    private OaMeetingcheckruleService oaMeetingcheckruleService;
    
	@Override
	public QueryResult<OaMeeting> list(Integer start, Integer limit, String sort, String filter, Boolean isDelete) {
        QueryResult<OaMeeting> qResult = this.getPaginationQuery(start, limit, sort, filter, isDelete);
		return qResult;
	}
	/**
	 * 根据主键逻辑删除数据
	 * 
	 * @param ids
	 *            要删除数据的主键
	 * @param currentUser
	 *            当前操作的用户
	 * @return 操作成功返回true，否则返回false
	 */
	@Override
	public Boolean doLogicDeleteByIds(String ids, SysUser currentUser) {
		Boolean delResult = false;
		try {
			Object[] conditionValue = ids.split(",");
			String[] propertyName = { "isDelete", "updateUser", "updateTime" };
			Object[] propertyValue = { 1, currentUser.getXm(), new Date() };
			this.doUpdateByProperties("uuid", conditionValue, propertyName, propertyValue);
			delResult = true;
		} catch (Exception e) {
			logger.error(e.getMessage());
			delResult = false;
		}
		return delResult;
	}

    @Override
    public Boolean doDeleteMeetingUser(String ids, String userId, SysUser currentUser) {
	    String[] userIds = userId.split(",");
        meetingempService.doDeleteByPK(userIds);
        return true;
    }

    @Override
    public Boolean doAddMeetingUser(String ids, String userId, String userName,SysUser currentUser) {
        String[] userIds = userId.split(",");
        String[] userNames = userName.split(",");
        OaMeeting meeting = this.get(ids);
        OaMeetingemp meetingemp = null;
        for (int i = 0; i < userIds.length; i++) {
        	meetingemp = meetingempService.get(ids+userIds[i]);
            if (meetingemp == null) {
            	meetingemp = new OaMeetingemp(ids+userIds[i]);	//注：主键id为会议id+人员id
            	meetingemp.setEmployeeId(userIds[i]);
            	meetingemp.setMeetingId(ids);
            }
            meetingemp.setBeginTime(meeting.getBeginTime());
            meetingemp.setEndTime(meeting.getEndTime());
            meetingemp.setXm(userNames[i]);
            meetingemp.setCreateUser(currentUser.getUuid());
            meetingemp.setUpdateUser(currentUser.getUuid());
            meetingemp.setIsDelete(0);
            
            meetingempService.getBaseDao().merge(meetingemp);
            //meetingempService.doMerge(meetingemp);
        }
        return true;
    }

    /**
	 * 根据传入的实体对象更新数据库中相应的数据
	 * 
	 * @param entity
	 *            传入的要更新的实体对象
	 * @param currentUser
	 *            当前操作用户
	 * @return
	 */
	@Override
	public OaMeeting doUpdateEntity(OaMeeting entity, SysUser currentUser) {
		// 先拿到已持久化的实体
		OaMeeting saveEntity = this.get(entity.getUuid());
		try {
			List<String> excludedProp = new ArrayList<>();
			excludedProp.add("meetingState");
			BeanUtils.copyPropertiesExceptNull(saveEntity, entity,excludedProp);
			
			if(StringUtils.isEmpty(entity.getRoomId())){
				saveEntity.setRoomId(null);
			}
			
			saveEntity.setUpdateTime(new Date()); // 设置修改时间
			saveEntity.setUpdateUser(currentUser.getXm()); // 设置修改人的中文名
			entity = this.doMerge(saveEntity);// 执行修改方法

			return entity;
		} catch (IllegalAccessException e) {
			logger.error(e.getMessage());
			return null;
		} catch (InvocationTargetException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	/**
	 * 将传入的实体对象持久化到数据
	 * 
	 * @param entity
	 *            传入的要更新的实体对象
	 * @param currentUser
	 *            当前操作用户
	 * @return
	 */
	@Override
	public OaMeeting doAddEntity(OaMeeting entity, SysUser currentUser) {
		OaMeeting saveEntity = new OaMeeting();
		try {
			List<String> excludedProp = new ArrayList<>();
			excludedProp.add("uuid");
			BeanUtils.copyProperties(saveEntity, entity,excludedProp);
			saveEntity.setCreateUser(currentUser.getXm()); // 设置修改人的中文名
			entity = this.doMerge(saveEntity);// 执行修改方法

			return entity;
		} catch (IllegalAccessException e) {
			logger.error(e.getMessage());
			return null;
		} catch (InvocationTargetException e) {
			logger.error(e.getMessage());
			return null;
		}
	}
	@Override
	public Integer syncMetting(List<Object[]> meetingList,List<Object[]> empList) {
		// TODO Auto-generated method stub
		int row = 1;
        OaMeeting m = null;
        OaMeetingemp emp = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 小写的mm表示的是分钟
    	try {
	        //1: 会议类型数据字典
	        String mapKey = null;
	        String[] propValue = {"MEETINGCATEGORY"};
	        Map<String, String> mapDicItem = new HashMap<>();
	        List<BaseDicitem> listDicItem = dicitemService.queryByProerties("dicCode", propValue);
	        for (BaseDicitem baseDicitem : listDicItem) {
	            mapKey = baseDicitem.getItemName() + baseDicitem.getDicCode();
	            mapDicItem.put(mapKey, baseDicitem.getItemCode());
	        }
	        
	        //2: 获取房间实体，拿到roomid
	        BuildRoominfo build = null;
	        Map<String, String> mapRoomInfo = new HashMap<>();
	        List<BuildRoominfo> roominfoList = buildService.getQueryAll();
	        for (BuildRoominfo buildRoominfo : roominfoList) {
	            mapRoomInfo.put(buildRoominfo.getRoomName(), buildRoominfo.getUuid());
	        }
	        //3: 获取默认的会议考勤规则
	        String[] rulePropertyName = {"isDelete","startUsing"};
	        Object[] rulpropValue = {0,(short)1};
	        Map<String, String> sortedCondition = new HashMap<>();
	        sortedCondition.put("createTime", "desc");
	        List<OaMeetingcheckrule> ruleList = oaMeetingcheckruleService.queryByProerties(rulePropertyName, rulpropValue, sortedCondition, 1);

	        OaMeetingcheckrule checkRule =ruleList.size()>0?ruleList.get(0):null;
	        
	        //4：遍历会议列表
	        for (Object[] o : meetingList) {
	            m = this.get(o[0].toString());
	            if (m == null) {
	                m = new OaMeeting(o[0].toString());
	                //考勤规则，现在放在第一次同步时来设定，因为后面可能会修改规则，所以之后不再同步
	                m.setNeedChecking((short) 1);
	                if(checkRule!=null){
	                	m.setCheckruleId(checkRule.getUuid());
	                	m.setCheckruleName(checkRule.getRuleName());
	                }		           
	            }
	            m.setMeetingTitle(o[1].toString());
	            m.setMeetingName(o[1].toString());
	            m.setMeetingContent(o[2].toString());
	            //会议类型数据字典转换
	            if (o[3] != null){
	            	String categoryItem=mapDicItem.get(o[3].toString() + "MEETINGCATEGORY");
	            	if(categoryItem==null)
	            		categoryItem="7";	//若没有找到类型，则指定为其他
	                m.setMeetingCategory(categoryItem);
	            }else{
	            	m.setMeetingCategory(mapDicItem.get("其他MEETINGCATEGORY"));	//若没有类型，则指定为其他
	            }
	          
	            m.setBeginTime(sdf.parse(o[4].toString()));
	            m.setEndTime(sdf.parse(o[5].toString()));
	            
	            m.setRoomName(o[6].toString());
	            if (mapRoomInfo.get(o[6]) != null)
	                m.setRoomId(mapRoomInfo.get(o[6]));
	            
	            //创建人
	            m.setCreateUser(o[7].toString());
	            
	            this.getBaseDao().merge(m);
	            //this.doMerge(m);	
	            
	            
	            //5. 过时：处理会议人员（从MEETING_USER_IDS、MEETING_USER_NAMES字段获取，逗号隔开）
//	            String meetingUserIds=String.valueOf(o[8]);
//	            String meetingUserNames=String.valueOf(o[9]);
//	            //当人员id不为空的时候就处理
//	            if(StringUtils.isNotEmpty(meetingUserIds)){
//	            	String[] userIds=meetingUserIds.split(",");
//	            	String[] userNames=meetingUserNames.split(",");
//	            	for(int i=0;i<userIds.length;i++){	            
//	            		emp = meetingempService.get(m.getUuid()+ userIds[i]);
//						if (emp == null) {
//							emp = new OaMeetingemp(m.getUuid() + userIds[i]); // 注：主键id为会议id+人员id
//							emp.setAttendResult("0"); // 默认为0，未考勤
//							emp.setMeetingId(m.getUuid());
//							emp.setEmployeeId(userIds[i]);
//							emp.setIsDelete(0);
//						}
//
//						emp.setXm(userNames[i]);				
//						emp.setBeginTime(m.getBeginTime());
//						emp.setEndTime(m.getEndTime());
//
//						meetingempService.doMerge(emp);
//	            	}
//	            }	           
	        }
	        
	        //5：处理会议人员
            for (Object[] o : empList) {
                emp = meetingempService.get(o[0].toString() + o[1].toString());
                if (emp == null) {
                	emp = new OaMeetingemp(o[0].toString() + o[1].toString());	//注：主键id为会议id+人员id
                	emp.setAttendResult("0");	//默认为0，未考勤
                	emp.setMeetingId(o[0].toString());
                    emp.setEmployeeId(o[1].toString());
                    emp.setIsDelete(0);
                }
                
                emp.setXm(o[2].toString());
                            
                emp.setBeginTime(sdf.parse(o[3].toString()));
                emp.setEndTime(sdf.parse(o[4].toString()));
                
                meetingempService.getBaseDao().merge(emp);
                //meetingempService.doMerge(emp);
            }
	        
    	} catch (Exception e) {
			// 捕获了异常后，要手动进行回滚；
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();

            logger.error(e.getMessage());
			logger.error(e.getStackTrace());
			row = -1;
		}

		return row;
	}

}