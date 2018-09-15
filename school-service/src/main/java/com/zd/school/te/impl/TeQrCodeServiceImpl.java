package com.zd.school.te.impl;

import com.alibaba.fastjson.JSON;
import com.zd.core.service.BaseServiceImpl;
import com.zd.school.te.TeQrCode;
import com.zd.school.te.TeQrCodeDao;
import com.zd.school.te.TeQrCodeForApp;
import com.zd.school.te.TeQrCodeService;
import org.apache.log4j.Logger;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 *
 * @description:
 * @author: yz
 * @date: 2018-09-12
 * @time: 16:26
 */
@Service
@Transactional
public class TeQrCodeServiceImpl extends BaseServiceImpl<TeQrCode> implements TeQrCodeService {


    private static Logger logger = Logger.getLogger(TeQrCodeServiceImpl.class);

    private Properties prop;

    public TeQrCodeServiceImpl() throws IOException {
        prop = PropertiesLoaderUtils.loadAllProperties("up.properties");
    }

    @Resource
    public void setTeaTeQrCodeDao(TeQrCodeDao dao) {
        this.dao = dao;
    }

    @Override
    public Boolean findQrCode(String userid, TeQrCode teQrCode) {
        try {
            //查询权限
            String sql = "SELECT * FROM PT_V_USERROOM WHERE USER_ID ='" + userid + "' AND ROOM_ID ='" + teQrCode.getRoomID() + "'";
            List<Object[]> list = this.ObjectQuerySql(sql);
            if (null != list&&0==list.size()) {
                return false;
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    return false;
    }

    @Override
    public TeQrCodeForApp openDoor(Map<String, Object> map) {
        //请求地址
        String urlSite = "taskapi/opendoor?";
        TeQrCodeForApp  teQrCodeForApp=null;
        try {
            // 把字符串转换为URL请求地址
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                urlSite += entry.getKey() + "=" + entry.getValue() + "&";
            }
            urlSite=  urlSite.substring(0, urlSite.length() - 1);
            URL url = new URL(prop.get("url").toString() + urlSite);
            // 打开连接
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();// 连接会话
            // 获取输入流
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            String line;
            StringBuilder sb = new StringBuilder();

            // 循环读取流
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            // 关闭流
            br.close();
            // 断开连接
            connection.disconnect();
            teQrCodeForApp =TeQrCodeForApp.fromJson(sb.toString());
            return teQrCodeForApp;
        } catch (Exception e) {
              teQrCodeForApp = new TeQrCodeForApp(e.getCause().toString(), false, null);
            return teQrCodeForApp;
        }

    }
}
