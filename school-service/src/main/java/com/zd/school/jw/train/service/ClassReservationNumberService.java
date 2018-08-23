package com.zd.school.jw.train.service;

import com.zd.core.service.BaseService;
import com.zd.school.jw.train.model.ClassReservationNumber;

import java.util.List;
import java.util.Map;

public interface ClassReservationNumberService extends BaseService<ClassReservationNumber> {
    public List<Map<String,Object>>  getClassRvNumber(String classid);
}

