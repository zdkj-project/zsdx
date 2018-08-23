package com.zd.school.jw.train.dao.Impl;

import com.zd.core.dao.BaseDaoImpl;
import com.zd.school.jw.train.dao.ClassReservationNumberDao;
import com.zd.school.jw.train.model.ClassReservationNumber;
import com.zd.school.jw.train.model.vo.DClassVo;
import org.springframework.stereotype.Repository;

@Repository
public class ClassReservationNumberDaoImpl extends BaseDaoImpl<ClassReservationNumber>  implements ClassReservationNumberDao {
    public ClassReservationNumberDaoImpl(){
        super(ClassReservationNumber.class);
        
    }
}
