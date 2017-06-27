package com.zd.school.oa.flow.service;

import java.util.List;
import java.util.Map;

import com.zd.core.service.BaseService;
import com.zd.school.oa.flow.model.LeaveApplay;

public interface LeaveApplayService extends BaseService<LeaveApplay> {

    public boolean startWorkflow(String userId, String key, String businesskey, Map map);
    
    public List<LeaveApplay> getLastApplay(String userid) ;

}
