/**
 * Project Name:jw-service
 * File Name:WorkFlowServiceImpl.java
 * Package Name:com.zd.school.oa.service.flow.Impl
 * Date:2016年4月18日下午2:52:08
 * Copyright (c) 2016 ZDKJ All Rights Reserved.
 *
*/

package com.zd.school.oa.flow.service.Impl;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

import javax.annotation.Resource;

import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.zd.school.oa.flow.service.WorkFlowService;

/**
 * ClassName:WorkFlowServiceImpl Function: TODO ADD FUNCTION. Reason: TODO ADD
 * REASON. Date: 2016年4月18日 下午2:52:08
 * 
 * @author luoyibo
 * @version
 * @since JDK 1.8
 * @see
 */
@Service
@Transactional
public class WorkFlowServiceImpl implements WorkFlowService {
	@Resource
	private ProcessEngine processEngine;

	@Resource
	private RuntimeService runtimeService;

	@Resource
	private IdentityService identityService;

	@Resource
	private RepositoryService repositoryService;

	public ProcessInstance startProcess(String userId, String key, String businesskey, Map map) {

		// TODO Auto-generated method stub
		identityService.setAuthenticatedUserId(userId);
		return runtimeService.startProcessInstanceByKey(key, businesskey, map);
	}

	public ProcessInstance createProcess(String key) {

		// TODO Auto-generated method stub
		return runtimeService.startProcessInstanceByKey(key);
	}

	public long getPdCount(String userId) {

		// TODO Auto-generated method stub
		return repositoryService.createProcessDefinitionQuery().latestVersion().startableByUser(userId).count();
	}

	public long getPdCount(String whereSql, String userId) {

		return repositoryService.createProcessDefinitionQuery().processDefinitionCategory(whereSql).latestVersion()
				.startableByUser(userId).count();
	}

	public List<?> findPdByPage(Integer start, Integer limit, String userId) {

		// TODO Auto-generated method stub
		return repositoryService.createProcessDefinitionQuery().latestVersion().startableByUser(userId).listPage(start,
				limit);
	}

	public List<?> findPdByPage(Integer start, Integer limit, String whereSql, String userId) {

		return repositoryService.createProcessDefinitionQuery().processDefinitionCategory(whereSql).latestVersion()
				.startableByUser(userId).listPage(start, limit);
	}

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

	public boolean delteProcessDefinitionById(String id) {
		boolean result = false;
		String[] del = id.split(",");
		for (String string : del) {
			ProcessDefinition pDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(string)
					.singleResult();
			if (pDefinition != null) {
				String deploymentId = pDefinition.getDeploymentId();
				repositoryService.deleteDeployment(deploymentId, true);
				result = true;
			}
		}

		return result;
	}

	public boolean deployment(MultipartFile file, String deployDir) throws FileNotFoundException {

		// TODO Auto-generated method stub
		boolean result = false;
		try {
			DeploymentBuilder deploymentBuilder = null;
			// 部署流程
			Deployment deployment = null;
			InputStream fileInputStream = file.getInputStream();
			String uploadFileName = file.getOriginalFilename();
			String extension = uploadFileName.substring(uploadFileName.lastIndexOf(".") + 1);
			if (extension.equals("zip") || extension.equals("bar")) {
				ZipInputStream zip = new ZipInputStream(fileInputStream);
				deploymentBuilder = repositoryService.createDeployment();
				deploymentBuilder.enableDuplicateFiltering();
				deployment = deploymentBuilder.name(uploadFileName).addZipInputStream(zip).deploy();
			} else {
				deploymentBuilder = repositoryService.createDeployment();
				deploymentBuilder.name(uploadFileName);
				deploymentBuilder.enableDuplicateFiltering();
				deployment = deploymentBuilder.addInputStream(uploadFileName, fileInputStream).deploy();
			}

			// 部署的流程列表
			List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery()
					.deploymentId(deployment.getId()).list();

			// 生成图片文件
			for (ProcessDefinition processDefinition : list) {
				WorkflowUtils.exportDiagramToFile(repositoryService, processDefinition, deployDir);
			}
			result = true;
		} catch (Exception e) {

		}
		return result;
	}
}
