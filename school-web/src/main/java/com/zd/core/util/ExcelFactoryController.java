package com.zd.core.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.zd.core.controller.core.FrameWorkController;
import com.zd.core.model.BaseEntity;
import com.zd.core.service.BaseService;
import com.zd.core.util.EntityUtil;
import com.zd.core.util.ModelUtil;
import com.zd.school.excel.ExcelCellField;
import com.zd.school.excel.FastExcel;
import com.zd.school.excel.annotation.MapperCell;
import com.zd.school.plartform.system.model.SysUser;

@Controller
@RequestMapping("/ExcelFactory")
public class ExcelFactoryController extends FrameWorkController<BaseEntity> {

	@Resource
	private BaseService<BaseEntity> baseService;

	/**
	 * 获取导出字段
	 */
	@RequestMapping(value = "/getMapperCellFields", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
	@ResponseBody
	public List<ExcelCellField> getMapperCellFields(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String modelName = request.getParameter("modelName");
		Class<?> c = EntityUtil.getClassByName(modelName);
		Map<Integer, String> titleMap = new TreeMap<Integer, String>();
		Field[] fields = ModelUtil.getClassFields(c, false);
		for (Field field : fields) {
			if (field.isAnnotationPresent(MapperCell.class)) {
				MapperCell mapperCell = field.getAnnotation(MapperCell.class);
				titleMap.put(mapperCell.order(), mapperCell.cellName());
			}
		}
		List<ExcelCellField> list = new ArrayList<ExcelCellField>();
		for (Entry<Integer, String> map : titleMap.entrySet()) {
			ExcelCellField excelFile = new ExcelCellField();
			excelFile.setId(map.getKey());
			excelFile.setName(map.getValue());
			list.add(excelFile);
		}
		return list;
	}

	/**
	 * 导出Excel
	 */
	@RequestMapping("/exportExcel")
	public void exportExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String modelName = request.getParameter("modelName");
		String fileName = request.getParameter("fileName");
		String exportField = request.getParameter("exportField");
		String[] exportFields = exportField.split(",");

		Class<?> c = EntityUtil.getClassByName(modelName);
		Map<Integer, String> titleMap = new TreeMap<Integer, String>();

		for (int i = 0; i < exportFields.length; i++) {
			titleMap.put(i, exportFields[i]);
		}

		List<?> exportList = baseService.doQuery("from " + c.getSimpleName());
		if (StringUtils.isNotEmpty(exportField)) {
			FastExcel.exportExcel(response, fileName, titleMap, exportList);
		} else {
			FastExcel.exportExcel(response, fileName, exportList);
		}

	}
	
	/**
	 * 下载导入模版
	 */
	@RequestMapping("/dlImportModel")
	public void dlImportModel(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String modelName = request.getParameter("modelName");
		String fileName = request.getParameter("fileName");
		Class<?> c = EntityUtil.getClassByName(modelName);
		FastExcel.dlImportModel(response, fileName, c);
	}

	@SuppressWarnings({ "unchecked", "resource", "rawtypes" })
	@RequestMapping("/importExcel")
	public <T extends BaseEntity> void importExcel(@RequestParam MultipartFile file, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// 获取当前的操作用户
		String userCh = "超级管理员";
		SysUser currentUser = getCurrentSysUser();
		if (currentUser != null)
			userCh = currentUser.getXm();
		String modelName = request.getParameter("modelName");
		FastExcel fastExcel = new FastExcel(file);
		Class c = EntityUtil.getClassByName(modelName);
		List<T> importList = fastExcel.importExcel(c);
		for (T obj : importList) {
			// 增加时要设置创建人
			obj.setCreateUser(userCh);
			baseService.merge(obj);
		}
		writeJSON(response, JsonBuilder.getInstance().returnSuccessJson("'上传成功'"));
	}

}
