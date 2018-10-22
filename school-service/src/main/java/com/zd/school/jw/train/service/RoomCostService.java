package com.zd.school.jw.train.service;

import com.zd.core.service.BaseService;
import com.zd.school.jw.train.model.RoomCost;


/**
 *  学员住宿信息
 */
 
public interface RoomCostService extends BaseService<RoomCost> {

    public void timingUpdate();
}