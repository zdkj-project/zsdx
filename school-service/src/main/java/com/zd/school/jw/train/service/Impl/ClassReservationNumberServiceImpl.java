package com.zd.school.jw.train.service.Impl;

import com.zd.core.service.BaseServiceImpl;
import com.zd.school.jw.train.dao.ClassReservationNumberDao;
import com.zd.school.jw.train.model.ClassReservationNumber;
import com.zd.school.jw.train.service.ClassReservationNumberService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ClassReservationNumberServiceImpl extends BaseServiceImpl<ClassReservationNumber> implements ClassReservationNumberService{
    @Resource
    public void setDao(ClassReservationNumberDao dao) {
        this.dao = dao;
    }

    @Override
    public List<Map<String,Object>> getClassRvNumber(String classid){
        String sql="select numid ,reservationid RESERVATIONID from CLASS_RESERVATION_NUMBER where  classid='"+classid+"'";
        List<Map<String,Object>> list=this.queryMapBySql(sql);
        return  list;
    }
}
