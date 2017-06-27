package com.zd.school.build.define.service.Impl;

import com.zd.core.service.BaseServiceImpl;
import com.zd.core.util.StringUtils;
import com.zd.school.build.define.dao.BuildRoominfoDao;
import com.zd.school.build.define.model.BuildRoominfo;
import com.zd.school.build.define.service.BuildRoominfoService;
import com.zd.school.plartform.system.model.SysUser;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * ClassName: BuildRoominfoServiceImpl
 * Function: TODO ADD FUNCTION.
 * Reason: TODO ADD REASON(可选).
 * Description: 教室信息实体Service接口实现类.
 * date: 2016-08-23
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Service
@Transactional
public class BuildRoominfoServiceImpl extends BaseServiceImpl<BuildRoominfo> implements BuildRoominfoService {

    private static Logger logger = Logger.getLogger(BuildRoominfoServiceImpl.class);

    @Resource
    public void setBuildRoominfoDao(BuildRoominfoDao dao) {
        this.dao = dao;
    }

    public Boolean batchAddRoom(BuildRoominfo roominfo, SysUser currentUser) {
        String benginChar = roominfo.getRoomName();
        Integer roomCount = roominfo.getRoomCount();
        String areaId = roominfo.getAreaId();
        String roomType = roominfo.getRoomType();
        String createUser = currentUser.getXm();
        BuildRoominfo saveRoom = null;
        String roomName = "";
        try {
            for (int i = 1; i <= roomCount; i++) {
                roomName = benginChar + StringUtils.addString(String.valueOf(i), "0", 2, "L");
                saveRoom = new BuildRoominfo();
                saveRoom.setRoomName(roomName);
                saveRoom.setOrderIndex(i);
                saveRoom.setExtField01(roomName);
                saveRoom.setAreaId(areaId);
                saveRoom.setRoomType(roomType);
                saveRoom.setCreateUser(createUser);

                this.merge(saveRoom);
            }
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return false;
        }
    }
}