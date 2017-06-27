/**
 * Project Name:jw-service
 * File Name:WorkFlowService.java
 * Package Name:com.zd.school.oa.service.flow
 * Date:2016年4月18日下午2:41:30
 * Copyright (c) 2016 ZDKJ All Rights Reserved.
 *
*/

package com.zd.school.oa.flow.service;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.web.multipart.MultipartFile;

/**
 * ClassName:WorkFlowService Function: TODO ADD FUNCTION. Reason: TODO ADD
 * REASON. Date: 2016年4月18日 下午2:41:30
 * 
 * @author luoyibo
 * @version
 * @since JDK 1.8
 * @see
 */
public interface WorkFlowService {
    public ProcessInstance startProcess(String userId, String key, String businesskey, Map map);

    public ProcessInstance createProcess(String key);

    public long getPdCount(String userId);

    public long getPdCount(String whereSql, String userId);

    public List<?> findPdByPage(Integer start, Integer limit, String userId);

    public List<?> findPdByPage(Integer start, Integer limit, String whereSql, String userId);

    /**
     * 
     * deleteDeployment:根据多个id参数删除定义的流程对象.
     *
     * @author luoyibo
     * @param id
     *            多个id，以英文逗号隔开
     * @return 返回true或者false
     * @throws @since
     *             JDK 1.8
     */

    public boolean delteProcessDefinitionById(String id);

    /**
     * 
     * deployment:部署指定的流程.
     * 
     * @author luoyibo
     * @param file
     *            要部署的流程文件
     * @param deployDir
     *            流程部署的路径
     * @return 返回true或者false
     * @throws FileNotFoundException
     * @throws @since
     *             JDK 1.8
     */
    public boolean deployment(MultipartFile file, String deployDir) throws FileNotFoundException;
}
