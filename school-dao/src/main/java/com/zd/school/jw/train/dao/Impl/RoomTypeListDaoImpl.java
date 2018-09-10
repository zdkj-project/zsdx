package com.zd.school.jw.train.dao.Impl;

import com.zd.core.dao.BaseDaoImpl;
import com.zd.school.jw.train.dao.RoomTypeListDao;
import com.zd.school.jw.train.model.RoomTypeList;
import org.springframework.stereotype.Repository;

/**
 * Created with IntelliJ IDEA.
 *
 * @description: 房型列表
 * @author: yz
 * @date: 2018-09-06
 * @time: 16:48
 */
@Repository
public class RoomTypeListDaoImpl extends BaseDaoImpl<RoomTypeList> implements RoomTypeListDao {
    public RoomTypeListDaoImpl() {
        super(RoomTypeList.class);
        // TODO Auto-generated constructor stub
    }
}