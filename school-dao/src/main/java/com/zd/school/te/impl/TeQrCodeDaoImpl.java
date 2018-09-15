package com.zd.school.te.impl;

import com.zd.core.dao.BaseDaoImpl;
import com.zd.school.te.TeQrCode;
import com.zd.school.te.TeQrCodeDao;
import org.springframework.stereotype.Repository;

/**
 * Created with IntelliJ IDEA.
 *
 * @description:
 * @author: yz
 * @date: 2018-09-12
 * @time: 16:23
 */
@Repository
public class TeQrCodeDaoImpl extends BaseDaoImpl<TeQrCode> implements TeQrCodeDao {
    public TeQrCodeDaoImpl() {
        super(TeQrCode.class);
        // TODO Auto-generated constructor stub
    }
}
