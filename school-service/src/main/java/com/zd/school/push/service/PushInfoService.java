
package com.zd.school.push.service;

import com.zd.core.service.BaseService;
import com.zd.school.push.model.PushInfo;

public interface PushInfoService extends BaseService<PushInfo> {

	public boolean pushInfo(String empName,String empNo,String eventType,String regStatus, Integer pushWay,String pushUrl);

    public boolean pushInfo(String empName, String mobile, String evenType, String pushInfo, Integer pushWay);
    
    public boolean pushInfo(String userId,String empId,String empName, String mobile, String eventType, String info, Integer pushWay);
}
