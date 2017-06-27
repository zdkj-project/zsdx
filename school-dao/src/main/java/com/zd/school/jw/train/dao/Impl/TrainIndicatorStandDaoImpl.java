package com.zd.school.jw.train.dao.Impl;

import com.zd.core.dao.BaseDaoImpl;
import com.zd.school.jw.train.dao.TrainIndicatorStandDao;
import com.zd.school.jw.train.model.TrainIndicatorStand;
import org.springframework.stereotype.Repository;

/**
 * Created by luoyibo on 2017-06-18.
 */
@Repository
public class TrainIndicatorStandDaoImpl extends BaseDaoImpl<TrainIndicatorStand> implements TrainIndicatorStandDao {
    public TrainIndicatorStandDaoImpl() {
        super(TrainIndicatorStand.class);
    }
}
