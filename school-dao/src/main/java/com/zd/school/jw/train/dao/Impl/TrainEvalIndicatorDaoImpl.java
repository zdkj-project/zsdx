package com.zd.school.jw.train.dao.Impl;

import com.zd.core.dao.BaseDaoImpl;
import com.zd.school.jw.train.dao.TrainEvalIndicatorDao;
import com.zd.school.jw.train.model.TrainEvalIndicator;
import org.springframework.stereotype.Repository;

/**
 * Created by luoyibo on 2017-06-18.
 */
@Repository
public class TrainEvalIndicatorDaoImpl extends BaseDaoImpl<TrainEvalIndicator> implements TrainEvalIndicatorDao {
    public TrainEvalIndicatorDaoImpl() {
        super(TrainEvalIndicator.class);
    }
}
