package com.zd.school.oa.notice.dao.Impl;

import org.springframework.stereotype.Repository;

import com.zd.core.dao.BaseDaoImpl;
import com.zd.school.oa.notice.dao.OaNoticerightDao;
import com.zd.school.oa.notice.model.OaNoticeright;


@Repository
public class OaNoticerightImpl extends BaseDaoImpl<OaNoticeright> implements OaNoticerightDao {
    public OaNoticerightImpl() {
        super(OaNoticeright.class);
        // TODO Auto-generated constructor stub
    }
}