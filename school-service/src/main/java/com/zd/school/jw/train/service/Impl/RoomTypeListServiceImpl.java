
package com.zd.school.jw.train.service.Impl;

import com.alibaba.druid.support.json.JSONUtils;
import com.zd.core.constant.InfoPushWay;
import com.zd.core.model.extjs.QueryResult;
import com.zd.core.service.BaseServiceImpl;
import com.zd.core.util.*;
import com.zd.school.jw.model.app.ClassEvalApp;
import com.zd.school.jw.train.dao.RoomTypeListDao;
import com.zd.school.jw.train.dao.TrainClassDao;
import com.zd.school.jw.train.model.*;
import com.zd.school.jw.train.model.vo.TrainClassEval;
import com.zd.school.jw.train.service.*;
import com.zd.school.opu.CreateOrderPerson;
import com.zd.school.opu.CreateOrderResponse;
import com.zd.school.opu.OpuService;
import com.zd.school.plartform.baseset.model.BaseOrgToUP;
import com.zd.school.plartform.baseset.service.BaseOrgService;
import com.zd.school.plartform.system.model.SysUser;
import com.zd.school.plartform.system.model.SysUserToUP;
import com.zd.school.plartform.system.service.SysUserService;
import com.zd.school.push.service.PushInfoService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.text.MessageFormat;
import java.util.*;

/**
 * ClassName: TrainClassServiceImpl Function: ADD FUNCTION. Reason: ADD
 * REASON(可选). Description: 培训开班信息(TRAIN_T_CLASS)实体Service接口实现类. date:
 * 2017-03-07
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Service
@Transactional
public class RoomTypeListServiceImpl extends BaseServiceImpl<RoomTypeList> implements RoomTypeListService {

    @Resource
    public void setRoomTypeListDao(RoomTypeListDao dao) {
        this.dao = dao;
    }


}