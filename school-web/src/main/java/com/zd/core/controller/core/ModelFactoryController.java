package com.zd.core.controller.core;

import java.lang.reflect.Field;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zd.core.model.BaseEntity;
import com.zd.core.util.EntityUtil;
import com.zd.core.util.JsonBuilder;
import com.zd.core.util.ModelUtil;
import com.zd.core.util.StringUtils;

@Controller
@RequestMapping("/ModelFactory")
public class ModelFactoryController extends BaseController<BaseEntity> {

    @RequestMapping("/getModelFields")
    public void getModelFields(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String modelName = request.getParameter("modelName");
        String excludes = request.getParameter("excludes");
        String strData = null;
        if (!StringUtils.isNotEmpty(ModelUtil.modelJson.get(modelName))) {
            Class<?> c = EntityUtil.getClassByName(modelName);
            Field[] fields = ModelUtil.getClassFields(c, false);
            strData = JsonBuilder.getInstance().getModelFileds(modelName, fields, excludes);
        } else {
            strData = ModelUtil.modelJson.get(modelName);
        }
        writeJSON(response, strData);
    }

}
