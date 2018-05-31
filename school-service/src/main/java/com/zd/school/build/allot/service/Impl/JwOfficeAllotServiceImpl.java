package com.zd.school.build.allot.service.Impl;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zd.core.service.BaseServiceImpl;
import com.zd.core.util.BeanUtils;
import com.zd.school.build.allot.dao.BaseOfficeAllotDao;
import com.zd.school.build.allot.model.DormStudentDorm;
import com.zd.school.build.allot.model.JwOfficeAllot;
import com.zd.school.build.allot.service.JwOfficeAllotService;
import com.zd.school.build.allot.service.JwClassDormAllotService;
import com.zd.school.build.define.model.BuildOfficeDefine;
import com.zd.school.build.define.model.BuildRoominfo;
import com.zd.school.build.define.service.BuildDormDefineService;
import com.zd.school.build.define.service.BuildOfficeDefineService;
import com.zd.school.build.define.service.BuildRoominfoService;
import com.zd.school.control.device.model.MjUserright;
import com.zd.school.control.device.model.PtTerm;
import com.zd.school.control.device.service.MjUserrightService;
import com.zd.school.control.device.service.PtTermService;
import com.zd.school.plartform.system.model.SysUser;
import com.zd.school.push.model.PushInfo;
import com.zd.school.push.service.PushInfoService;
import com.zd.school.student.studentclass.model.JwClassstudent;

/**
 * 
 * ClassName: JwOfficeallotServiceImpl Function: TODO ADD FUNCTION. Reason: TODO
 * ADD REASON(可选). Description: JW_T_OFFICEALLOT实体Service接口实现类. date: 2016-08-23
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Service
@Transactional
public class JwOfficeAllotServiceImpl extends BaseServiceImpl<JwOfficeAllot> implements JwOfficeAllotService {
	@Resource
	BuildOfficeDefineService offRoomService; // 办公室service层接口
	
	@Resource
	BuildRoominfoService infoService;// 房间
	
	@Resource
	PushInfoService pushService; // 推送
	
	@Resource
	MjUserrightService mjService; // 门禁权限
	
	@Resource
	PtTermService ptTermService; // 设备表接口
	
	/* @Resource
	 JwClassRoomAllotService classservice;*/
	
	 @Resource
	 BuildDormDefineService dormDefine;
	
	 @Resource
	 JwClassDormAllotService classDormService;

	 @Resource
	 public void setJwOfficeallotDao(BaseOfficeAllotDao dao) {
		this.dao = dao;
	 }

	/**
	 * uuid：需要进行设置门禁权限的学生ID或教师ID；
	 * roomId：需要设置门禁的房间id；
	 * userId：需要取消门禁权限的学生ID或教师ID；
	 * dorm：在学生宿舍分配门禁使用，通过它来找到roomId；
	 * classStu：班级学生，暂时不设置，已经取消了班级的方式。
	 */
	@Override
	public boolean mjUserRight(String uuid, String roomId, String userId, DormStudentDorm dorm,
			JwClassstudent classStu) {
		try {
			if (dorm != null) {//学生宿舍门禁分配
				String dormId = classDormService.get(dorm.getCdormId()).getDormId(); //班级宿舍id
				roomId = dormDefine.get(dormId).getRoomId();
			} else if (classStu != null) { //班级的教师分配门禁 #目前还未增加教师分配该模块
				//String[] propName = { "claiId", "isDelete" };
				//Object[] propValue = { classStu.getClaiId(), 0 };
				//roomId = classservice.getByProerties(propName, propValue).getRoomId();
			}
			String[] propName = { "termTypeID", "isDelete", "roomId" };
			Object[] propValue = { "4", 0, roomId };
			MjUserright userRight = null;
			List<PtTerm> list = ptTermService.queryByProerties(propName, propValue);//该房间是否有设备
			if (uuid == null || uuid.equals("")) {
				if (list.size() > 0) {//解除门禁权限
					String[] uId = userId.split(","); //房间分配解除门禁设置
					for (int i = 0; i < list.size(); i++) {
						for (int j = 0; j < uId.length; j++) {
							String[] name = { "termId", "stuId" };
							String[] value = { list.get(i).getUuid(), uId[j] };
							userRight = mjService.getByProerties(name, value);
							if (userRight != null) {
								userRight.setIsDelete(1);
								userRight.setControlsegId(0);
								userRight.setCardstatusId(0);
								userRight.setUpdateTime(new Date());
								mjService.doMerge(userRight);
							}
						}
					}
				}
			} else {//增加门禁权限
				if (list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						String[] name = { "termId", "stuId" };
						String[] value = { list.get(i).getUuid(), uuid };
						userRight = mjService.getByProerties(name, value);//该学生或教师是否已经被分配了该房间的设备
						if (userRight != null) {
							userRight.setIsDelete(0);
							userRight.setUpdateTime(new Date());
							mjService.doMerge(userRight);
						} else {
							userRight = new MjUserright();
							userRight.setTermId(list.get(i).getUuid());
							userRight.setCreateUser("超级管理员");
							userRight.setStuId(uuid);
							mjService.doMerge(userRight);
						}
					}
				}
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	
	@Override
	public Boolean doAddRoom(JwOfficeAllot entity, Map hashMap, SysUser currentUser)
			throws IllegalAccessException, InvocationTargetException {
		Boolean flag = false;
		Boolean qxflag = false;
		Integer orderIndex = 0;
		JwOfficeAllot perEntity = null;
		JwOfficeAllot valioff = null;
		String[] strId = null;// 多个老师id
		StringBuffer xm =new StringBuffer();
		StringBuffer roomName =new StringBuffer();
		strId = entity.getTteacId().split(",");// 多个老师id
		for (int i = 0; i < strId.length; i++) {
			Object[] objValue = { entity.getRoomId(), strId[i], 0 };
			String[] objName = { "roomId", "tteacId", "isDelete" };
			valioff = this.getByProerties(objName, objValue);
			if (valioff != null) {
				xm.append(valioff.getXm()+',');
				roomName.append(valioff.getRoomName()+',');
				flag = false;
				hashMap.put("flag", flag);
				continue;
			}
			// 保存房间分配信息
			orderIndex = this.getDefaultOrderIndex(entity);
			perEntity = new JwOfficeAllot();
			BeanUtils.copyPropertiesExceptNull(entity, perEntity);
			entity.setCreateUser(currentUser.getXm()); // 创建人
			entity.setTteacId(strId[i]);
			entity.setOrderIndex(orderIndex);// 排序
			this.doMerge(entity); // 执行添加方法
			
			//2018-5-30分配房间、宿舍时，不再手动添加权限数据了。
			//qxflag=this.mjUserRight(strId[i], entity.getRoomId(), entity.getUuid(), null, null);
			/*if(!qxflag){ 
				flag = false;
				hashMap.put("flag", flag);
				hashMap.put("qx", "qx");
				continue;
			}*/
			//将办公室设置为已分配
			String hql=" from BuildOfficeDefine a where a.roomId='"+entity.getRoomId()+"' ";
			BuildOfficeDefine office=this.getEntityByHql(hql);
			if(office!=null){
				office.setRoomStatus("1");
				offRoomService.doMerge(office); 
			}
			flag = true;
		}
		hashMap.put("xm", xm);
		hashMap.put("roomName", roomName);
		return flag;
	}

	@Override
	public Boolean doPushMessage(String roomId) {
		Boolean flag=false;
		List<JwOfficeAllot> offTeas = null;
		PushInfo pushInfo = null;
		BuildRoominfo roominfo = null;
		String[] str = { "roomId", "isDelete" };
		Object[] str2 = { roomId, 0 };
		offTeas = this.queryByProerties(str, str2);//该办公室下的老师
	    for (JwOfficeAllot jwTOfficeAllot : offTeas) {
			pushInfo = new PushInfo();
			pushInfo.setEmplName(jwTOfficeAllot.getXm());// 姓名
			pushInfo.setEmplNo(jwTOfficeAllot.getGh());// 学号
			pushInfo.setRegTime(new Date());
			pushInfo.setEventType("办公室分配");
			pushInfo.setPushStatus(0);
			pushInfo.setPushWay(1);
			roominfo = infoService.get(jwTOfficeAllot.getRoomId());
			pushInfo.setRegStatus(pushInfo.getEmplName() + "您好，你的办公室分配在" + roominfo.getAreaUpName() + "，"
					+ roominfo.getAreaName() + "，" + jwTOfficeAllot.getRoomName() + "房");
			pushService.doMerge(pushInfo);
		}
		flag=true;
		return flag;
	}

	@Override
	public Boolean doDeleteOff(String delIds,String roomId,String tteacId) {
		JwOfficeAllot offAllot = null ;
		Boolean flag =false;
		String offRoomId = "";
		String[] delId = delIds.split(",");
		for (String id : delId) {
			offAllot = this.get(id);
			offRoomId += offAllot.getRoomId()+',';
			this.mjUserRight(null, offAllot.getRoomId(), offAllot.getTteacId(), null, null);
			flag = this.deleteByPK(id);	
	    }
		return flag;
  }
	@Override
	public void doOffSetOff(String roomIds) {
		String[] roomId = roomIds.split(",");
		BuildOfficeDefine office =null;
		String sql="";
		//List list =new ArrayList<>();
	    for (String officeRoomId : roomId) {
	    	sql = "select count(*) from JW_T_OFFICEALLOT a join BUILD_T_OFFICEDEFINE b  "
					+ " on  a.ROOM_ID = b.ROOM_ID "
					+ " where a.ISDELETE=0 and b.ISDELETE=0 and b.ROOM_ID='"
					+ officeRoomId + "'";
			
			Integer count = this.getQueryCountBySql(sql);
			if(count==0){	
				office = offRoomService.get(officeRoomId);
				if (office.getRoomStatus().equals("1")) {
					office.setRoomStatus("0");
					office.setUpdateTime(new Date());
					offRoomService.doMerge(office);			
				}
			}
			/*
			sql="select a.ROOM_ID ,b.OFFICE_ID from JW_T_OFFICEALLOT a right join BUILD_T_OFFICEDEFINE b  on  a.ROOM_ID = b.ROOM_ID where b.ROOM_ID='"+officeRoomId+"'";
			list = this.querySql(sql);
			for(int j=0; j<list.size();j++){
				Object[] object= (Object[]) list.get(j);
				if(object[0]==null){
					String dormId= (String) object[1];
					office = offRoomService.get(dormId);
					if(office.getRoomStatus().equals("1")){
						office.setRoomStatus("0");
						office.setUpdateTime(new Date());
						offRoomService.merge(office);
					}
					
				}
			}
			*/
	    }
	}
}