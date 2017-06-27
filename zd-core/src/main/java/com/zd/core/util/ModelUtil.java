package com.zd.core.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import com.zd.core.annotation.FieldInfo;

/**
 * 实体的工具类
 * 
 * @author zhangshuaipeng
 *
 */
public class ModelUtil {
    public static Map<String, Field[]> modelFields = new HashMap<String, Field[]>();

    public static Map<String, String> modelJson = new HashMap<String, String>();

    /**
     * 判断实体不为空
     * 
     * @param obj
     * @return
     */
    public static Boolean isNotNull(Object obj) {
        if (obj != null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取实体对应的数据表名
     * 
     * @param clazz
     *            要获取表名的实体类
     * @param equalEntity
     *            为true时返回实体的名称，否则返回注解的表名
     * @return
     */
    public static String getTableName(Class<?> clazz, boolean equalEntity) {
        String tableName = clazz.getSimpleName();

        if (!equalEntity) {
            Table annotation = (Table)clazz.getAnnotation(Table.class);
            if (!StringUtils.isEmpty(annotation.name())) {
                tableName = annotation.name();
            }
        }
        return tableName;
    }

    /**
     * 获取指定字段映射的数据表的列名
     * 
     * @param f
     *            指定的字段
     * @param isAnnotation
     *            为true表示取字段使用@Column进行注解的名称，当没有注解时取字段名
     * @return
     */
    public static String getColumnName(Field f, boolean isAnnotation) {
        String columnName = f.getName();
        if (isAnnotation) {
            Annotation[] annotations = f.getAnnotations();
            for (int i = 0; i < annotations.length; i++) {
                if (annotations[i] instanceof Column) {
                    // 数据列
                    Column column = (Column) annotations[i];
                    if (StringUtils.isNotEmpty(column.name())) {
                        columnName = column.name();
                    }
                } else if (annotations[i] instanceof JoinColumn) {
                    // 关联列
                    JoinColumn joinColumn = (JoinColumn) annotations[i];
                    if (StringUtils.isNotEmpty(joinColumn.name())) {
                        columnName = joinColumn.name();
                    }
                }

            }
        }

        return columnName;
    }

    /**
     * 得到类的属性集合
     * 
     * @param c
     * @param itself
     *            是否是自身的字段
     * @return
     */
    public static Field[] getClassFields(Class<?> c, boolean itself) {
        if (itself) {
            if (modelFields.get(c.getName()) != null) {
                return modelFields.get(c.getName());
            } else {
                Field[] fields = c.getDeclaredFields();
                modelFields.put(c.getName(), fields);
                return fields;
            }
        } else {
            if (modelFields.get(c.getName()) != null) {
                return modelFields.get(c.getName());
            } else {
                List<Field> fields = new ArrayList<Field>();
                getAllDeclaredFields(c, fields);
                Field[] fies = new Field[fields.size()];
                fields.toArray(fies);
                modelFields.put(c.getName(), fies);
                return fies;
            }
        }
    }

    /**
     * 从c类中取得全部字段,包括父类
     * 
     * @param c
     * @param fields
     */
    public static void getAllDeclaredFields(Class<?> c, List<Field> fields) {
        Field[] fies = c.getDeclaredFields();
        Collections.addAll(fields, fies);
        Class<?> parent = c.getSuperclass();
        if (parent != Object.class) {
            getAllDeclaredFields(parent, fields);
        } else {
            return;
        }
    }

    /**
     * 得到类的主键字段
     * 
     * @param clazz
     * @return
     */
    public static String getClassPkName(Class<?> clazz) {
        Field[] fields = getClassFields(clazz, false);
        String pkName = "";
        for (Field f : fields) {
            FieldInfo fieldInfo = f.getAnnotation(FieldInfo.class);
            if (isNotNull(fieldInfo)) {
                if ("ID".equals(fieldInfo.type())) {
                    pkName = f.getName();
                    break;
                }
            }
        }
        return pkName;
    }

    /**
     * 得到树形字段的模版类
     * 
     * @param c
     * @return
     */
    // public static JSONTreeNode getJSONTreeNodeTemplate(Class<?> c) {
    // Field[] fields = getClassFields(c, false);
    // JSONTreeNode template = new JSONTreeNode();
    // for (Field f : fields) {
    // NodeType nodeType = f.getAnnotation(NodeType.class);
    // if (nodeType == null) {
    // continue;
    // }
    //
    // if (TreeNodeType.ID.equalsType(nodeType.type())) {
    // template.setId(getColumnName(f, true));
    // }
    // if (TreeNodeType.TEXT.equalsType(nodeType.type())) {
    // template.setText(getColumnName(f, true));
    // }
    // if (TreeNodeType.CODE.equalsType(nodeType.type())) {
    // template.setCode(getColumnName(f, true));
    // }
    // if (TreeNodeType.ICON.equals(nodeType.type())) {
    // template.setIcon(getColumnName(f, true));
    // }
    // if (TreeNodeType.NODEINFO.equals(nodeType.type())) {
    // template.setNodeInfo(getColumnName(f, true));
    // }
    // if (TreeNodeType.NODEINFOTYPE.equals(nodeType.type())) {
    // template.setNodeInfoType(getColumnName(f, true));
    // }
    // if (TreeNodeType.CLS.equals(nodeType.type())) {
    // template.setCls(getColumnName(f, true));
    // }
    // if (TreeNodeType.LEAF.equals(nodeType.type())) {
    // // 根据NODETYPE
    // template.setNodeType(getColumnName(f, true));
    // }
    // if (TreeNodeType.PARENT.equals(nodeType.type())) {
    // template.setParent(getColumnName(f, true));
    // }
    // if (TreeNodeType.DISABLED.equals(nodeType.type())) {
    // template.setHref(getColumnName(f, true));
    // }
    // if (TreeNodeType.BIGICON.equals(nodeType.type())) {
    // template.setBigIcon(getColumnName(f, true));
    // }
    // }
    // return template;
    // }
}
