package com.zd.school.control.device.dao.Impl;

import org.springframework.stereotype.Repository;

import com.zd.core.dao.BaseDaoImpl;
import com.zd.school.control.device.dao.PtMjOpenDoorDao;
import com.zd.school.control.device.model.PtMjOpenDoor;


@Repository
public class PtMjOpenDoorDaoImpl extends BaseDaoImpl<PtMjOpenDoor> implements PtMjOpenDoorDao {
    public PtMjOpenDoorDaoImpl() {
        super(PtMjOpenDoor.class);
        // TODO Auto-generated constructor stub
    }
}