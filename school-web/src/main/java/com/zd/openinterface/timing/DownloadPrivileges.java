package com.zd.openinterface.timing;

import com.zd.core.util.DBContextHolder;
import com.zd.school.control.device.model.PtTerm;
import com.zd.school.control.device.model.Ptcard;
import com.zd.school.control.device.service.PtTermService;
import com.zd.school.jw.model.app.ForApp;
import com.zd.school.jw.train.model.TrainClasstrainee;
import com.zd.school.jw.train.service.TrainClassService;
import com.zd.school.jw.train.service.TrainClasstraineeService;
import com.zd.school.plartform.system.model.CardUserInfo;
import com.zd.school.plartform.system.model.SysUser;
import com.zd.school.plartform.system.service.CardUserInfoService;
import com.zd.school.plartform.system.service.SysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.method.P;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @description: 定时下载权限
 * @author: yz
 * @date: 2018-10-17
 * @time: 15:24
 */


public class DownloadPrivileges {

    private static final Logger LOGGER = LoggerFactory.getLogger(DownloadPrivileges.class);

    @Resource
    private CardUserInfoService cardUserInfoService;

    @Resource
    private PtTermService ptTermService;

    @Resource
    private TrainClasstraineeService classtraineeService;

    @Resource
    private SysUserService sysUserService;

    /**
     * @param
     * @return void
     * @description 定时下载权限
     * @author yz
     * @date 2018/10/17 17:19
     * @method timingDownloadPrivileges
     */
    public void timingDownloadPrivileges() {
        PtTerm ptTerm;
        ForApp forApp;
        SysUser sysUser;
        TrainClasstrainee trainClasstrainee = null;
        try {
            //切换数据库
            DBContextHolder.setDBType(DBContextHolder.DATA_SOURCE_UP6);

            String sql = "SELECT CardID FROM YCSCMDB_UP6.DBO.TC_CARD " +
                    "WHERE STATUSCHANGETIMEXF BETWEEN  DATEADD(MINUTE,-10,GETDATE()) AND GETDATE() OR" +
                    " STATUSCHANGETIMEJS BETWEEN  DATEADD(MINUTE,-10,GETDATE()) AND GETDATE() OR" +
                    " UPDATEDATEBALANCEXF BETWEEN  DATEADD(MINUTE,-10,GETDATE()) AND GETDATE() OR" +
                    " UPDATEDATEBALANCEJS BETWEEN  DATEADD(MINUTE,-10,GETDATE()) AND GETDATE()";

            List<Map<String, Object>> list = cardUserInfoService.getForValuesToSql(sql);
            //切换Q1数据库
            DBContextHolder.clearDBType();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (null == list || 0 == list.size()) {
                LOGGER.info("自动更新权限--没有查询到要更新的" + formatter.format(new Date()));
                return;
            }

            String where = "";
            for (int i = 0; i < list.size(); i++) {
                where += " UP_CARD_ID = " + list.get(i).get("CardID");
                if (i != list.size() - 1) {
                    where += " or ";
                }
            }

            String sqlinfo = "SELECT * FROM CARD_T_USEINFO WHERE " + where;
            String hql = "from PtTerm where roomId = ( select uuid from BuildRoominfo where roomName =(select roomName from TrainClasstrainee where uuid = ? and isDelete <> 1 )) and isDelete =0";

            String listhql = "from PtTerm where uuid = ( select termId from MjUserright where stuId = ? and isDelete <> 1 )  and isDelete <> 1 ";

            List<Map<String, Object>> listInfo = cardUserInfoService.getForValuesToSql(sqlinfo);
            for (int i = 0; i < listInfo.size(); i++) {
                if (null == listInfo.get(i).get("USER_ID") || "".equals(listInfo.get(i).get("USER_ID"))) {
                    continue;
                }
                ptTerm = ptTermService.getForValue(hql, listInfo.get(i).get("USER_ID"));
                sysUser = sysUserService.getForValue("from SysUser where isDelete <> 1 and uuid = ? ", listInfo.get(i).get("USER_ID"));
                if (null == sysUser) {
                    trainClasstrainee = classtraineeService.getForValue("from TrainClasstrainee where isDelete <> 1 and uuid = ? ", listInfo.get(i).get("USER_ID"));
                }
                if (null == ptTerm) {
                    List<PtTerm> ptTermList = ptTermService.getForValues(listhql, listInfo.get(i).get("USER_ID"));
                    if (0 == ptTermList.size()) {
                        continue;
                    }
                    for (PtTerm l : ptTermList) {
                        forApp = classtraineeService.downloadPrivileges(listInfo.get(i).get("USER_ID").toString(), l);
                        if (null == forApp) {
                            LOGGER.error(l.getRoomName() + "房间"+ (null == sysUser?trainClasstrainee.getXm():sysUser.getXm())+"权限下载权限失败:无法连接前置接口");
                            continue;
                        } else if (!forApp.isResult()) {
                            LOGGER.error(l.getRoomName() + "房间"+ (null == sysUser?trainClasstrainee.getXm():sysUser.getXm())+"权限下载权限失败:" + forApp.getMessage());
                            continue;
                        }
                    }

                }


                forApp = classtraineeService.downloadPrivileges(listInfo.get(i).get("USER_ID").toString(), ptTerm);
                if (null == forApp) {
                    LOGGER.error(ptTerm.getRoomName() + "房间"+ (null == sysUser?trainClasstrainee.getXm():sysUser.getXm())+"权限下载权限失败:无法连接前置接口");
                    continue;
                } else if (!forApp.isResult()) {
                    LOGGER.error(ptTerm.getRoomName() + "房间"+ (null == sysUser?trainClasstrainee.getXm():sysUser.getXm())+"权限下载权限失败:" + forApp.getMessage());
                    continue;
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }

    }
}
