package com.zd.school.jw.train.service.Impl;

import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zd.core.constant.CharConvertType;
import com.zd.core.constant.TreeVeriable;
import com.zd.core.model.extjs.QueryResult;
import com.zd.core.service.BaseServiceImpl;
import com.zd.core.util.BeanUtils;
import com.zd.core.util.EntityUtil;
import com.zd.core.util.StringUtils;
import com.zd.school.jw.train.dao.TrainCoursecategoryDao;
import com.zd.school.jw.train.model.TrainCoursecategory;
import com.zd.school.jw.train.model.TrainCoursecategoryTree;
import com.zd.school.jw.train.service.TrainCoursecategoryService;
import com.zd.school.jw.train.service.TrainCourseinfoService;
import com.zd.school.plartform.baseset.model.BaseDicitem;
import com.zd.school.plartform.baseset.service.BaseDicitemService;
import com.zd.school.plartform.system.model.SysUser;

/**
 * ClassName: TrainCoursecategoryServiceImpl
 * Function:  ADD FUNCTION.
 * Reason:  ADD REASON(可选).
 * Description: 课程分类信息(TRAIN_T_COURSECATEGORY)实体Service接口实现类.
 * date: 2017-03-07
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Service
@Transactional
public class TrainCoursecategoryServiceImpl extends BaseServiceImpl<TrainCoursecategory> implements TrainCoursecategoryService {

    @Resource
    public void setTrainCoursecategoryDao(TrainCoursecategoryDao dao) {
        this.dao = dao;
    }

    @Resource
    private BaseDicitemService dicitemService;
    
    @Resource
    private TrainCourseinfoService courseinfoService;

    private static Logger logger = Logger.getLogger(TrainCoursecategoryServiceImpl.class);

    @Override
    public QueryResult<TrainCoursecategory> list(Integer start, Integer limit, String sort, String filter, Boolean isDelete) {
        QueryResult<TrainCoursecategory> qResult = this.doPaginationQuery(start, limit, sort, filter, isDelete);
        return qResult;
    }

    /**
     * 根据主键逻辑删除数据
     *
     * @param ids         要删除数据的主键
     * @param currentUser 当前操作的用户
     * @return 操作成功返回true，否则返回false
     */
    @Override
    public Boolean doLogicDeleteByIds(String ids, SysUser currentUser) {
        Boolean delResult = false;
        try {
            Object[] conditionValue = ids.split(",");
            String[] propertyName = {"isDelete", "updateUser", "updateTime"};
            Object[] propertyValue = {1, currentUser.getXm(), new Date()};
            this.updateByProperties("uuid", conditionValue, propertyName, propertyValue);

            //把课程分类下的课程也逻辑删除
            String hql = MessageFormat.format("update TrainCourseinfo set isDelete=1 where categoryId in (''{0}'')", ids.replace(",", "','"));
            courseinfoService.executeHql(hql);
/*            for (int i = 0; i < conditionValue.length; i++) {
                String hql = "update TrainCourseinfo set isDelete=1 where categoryId='" + conditionValue[i].toString() + "' ";
                courseinfoService.executeHql(hql);
            }*/

            delResult = true;
        } catch (Exception e) {
            logger.error(e.getMessage());
            delResult = false;
        }
        return delResult;
    }

    /**
     * 根据传入的实体对象更新数据库中相应的数据
     *
     * @param entity      传入的要更新的实体对象
     * @param currentUser 当前操作用户
     * @return
     */
    @Override
    public TrainCoursecategory doUpdateEntity(TrainCoursecategory entity, SysUser currentUser) {
        // 先拿到已持久化的实体
        TrainCoursecategory saveEntity = this.get(entity.getUuid());
        String oldParentNode = saveEntity.getParentNode();
        String oldNodeText = saveEntity.getNodeText();
        String newParentNode = entity.getParentNode();
        String newNodeText = entity.getNodeText();
        String newNodeCode = saveEntity.getNodeCode();
        String oldNodeCode = saveEntity.getNodeCode();
        Integer orderIndex = saveEntity.getOrderIndex();
/*        Integer parentLevel = Integer.valueOf(1);
        String parentCode = "";*/

        try {
            if (!oldNodeText.equals(newNodeText) || !oldParentNode.equals(newParentNode)) {
                orderIndex = this.getOrderIndex(newParentNode);
                //如果修改了分类名称或上级分类，需要重新生成当前分类的编码并更新分类下的课程编码
                newNodeCode = BuildNode(saveEntity, newNodeText, orderIndex, oldNodeCode, newParentNode);
 /*               if (!newParentNode.equals(TreeVeriable.ROOT)) {
                    TrainCoursecategory parEntity = this.get(newParentNode);
                    parentLevel = parEntity.getNodeLevel();
                    parentCode = parEntity.getNodeCode();
                    parEntity.setLeaf(false);
                    this.merge(parEntity);
                    //根据上级节点的编码来生成当前的编码
                    if (parentLevel == 0) {
                        //上级节点层次为0时，编码为拼音首字母大写
                        newNodeCode = this.createNodeCode(newNodeText);
                    } else {
                        newNodeCode = parentCode + StringUtils.addString(orderIndex.toString(), "0", 2, "L");
                    }
                    saveEntity.BuildNode(parEntity);
                } else {
                    saveEntity.BuildNode(null);
                }*/
            }
            saveEntity.setOrderIndex(orderIndex);
            saveEntity.setParentNode(newParentNode);
            saveEntity.setNodeCode(newNodeCode);
            saveEntity.setNodeText(entity.getNodeText());
            saveEntity.setCategoryDesc(entity.getCategoryDesc());
            saveEntity.setUpdateTime(new Date()); // 设置修改时间
            saveEntity.setUpdateUser(currentUser.getXm()); // 设置修改人的中文名

            entity = this.merge(saveEntity);// 执行修改方法
            if (!oldNodeText.equals(newNodeText) || !oldParentNode.equals(newParentNode)) {
                //更新所有子分类及分类下的课程的编码
                UpdateCode(newNodeCode, oldNodeCode);
            }
            return entity;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    @Override
    public void doChangeOrder(String ids, String orders, SysUser currentUser) {
        String[] categoryId = ids.split(",");
        String[] orderIndex = orders.split(",");
        String newNodeCode;
        String oldNodeCode;
        Integer newOrder;
        String sql;
        TrainCoursecategory saveEntity = null;
        Map<String, TrainCoursecategory> mapCategory = new HashMap<>();
        List<TrainCoursecategory> list = this.queryByProerties("uuid", categoryId);
        for (TrainCoursecategory category : list) {
            mapCategory.put(category.getUuid(), category);
        }

        for (int i = 0; i < orderIndex.length; i++) {
            if ((Integer.parseInt(orderIndex[i]) == mapCategory.get(categoryId[i]).getOrderIndex()))
                continue;
            else {
                newOrder = Integer.parseInt(orderIndex[i]);
                saveEntity = mapCategory.get(categoryId[i]);
                oldNodeCode = saveEntity.getNodeCode();
                if (saveEntity.getNodeLevel() == 1) {
                    //一级分类，不需要更新编码
                    saveEntity.setOrderIndex(newOrder);
                    saveEntity.setUpdateTime(new Date());
                    saveEntity.setUpdateUser(currentUser.getXm());
                    this.merge(saveEntity);
                } else {
                    oldNodeCode = saveEntity.getNodeCode();
                    newNodeCode = oldNodeCode.substring(0, oldNodeCode.length() - 2) + StringUtils.addString(newOrder.toString(), "0", 2, "L");
                    saveEntity.setNodeCode(newNodeCode);
                    saveEntity.setOrderIndex(newOrder);
                    saveEntity.setUpdateTime(new Date());
                    saveEntity.setUpdateUser(currentUser.getXm());
                    this.merge(saveEntity);

                    sql = "EXECUTE TRAIN_P_CHANGECOURSECATEGORYORDER ''{0}'',''{1}'',''{2}''";
                    sql = MessageFormat.format(sql, saveEntity.getUuid(), oldNodeCode, newNodeCode);
                    this.doQuerySql(sql);
                    //this.UpdateCode(oldNodeCode, newNodeCode);
                }
            }
        }
    }

    /**
     * 更新课程分类与课程的编码
     *
     * @param newNodeCode 瓣的课程分类编码
     * @param oldNodeCode 原来的课程分类编码
     */
    private void UpdateCode(String newNodeCode, String oldNodeCode) {
        String sql = " update TRAIN_T_COURSECATEGORY set CATEGORY_CODE = replace(CATEGORY_CODE,''{0}'',''{1}'') where CATEGORY_CODE like ''{2}'%'' ";
        sql = MessageFormat.format(sql, oldNodeCode, newNodeCode, oldNodeCode);

        String sql1 = " update TRAIN_T_COURSEINFO set COURSE_CODE = replace(COURSE_CODE,''{0}'',''{1}'') where COURSE_CODE like ''{2}'%'' ";
        sql1 = MessageFormat.format(sql1, oldNodeCode, newNodeCode, oldNodeCode);
        this.executeSql(sql + sql1);
    }

    /**
     * @param entity      传入的要更新的实体对象
     * @param currentUser 当前操作用户
     * @return
     */
    @Override
    public TrainCoursecategory doAddEntity(TrainCoursecategory entity, SysUser currentUser) {
        TrainCoursecategory saveEntity = new TrainCoursecategory();
        String nodeText = entity.getNodeText();
        Integer orderIndex = Integer.valueOf(1);
        String nodeCode = "";  //当前节点编码
        String parentNode = entity.getParentNode();//上级节点
/*        Integer parentLevel = Integer.valueOf(1); //上级层级
        String parentCode = ""; //上级编码*/
        try {
            orderIndex = this.getOrderIndex(parentNode);
            List<String> excludedProp = new ArrayList<>();
            excludedProp.add("uuid");
            BeanUtils.copyProperties(saveEntity, entity, excludedProp);
            saveEntity.setCreateUser(currentUser.getXm()); // 设置修改人的中文名
            saveEntity.setLeaf(true);
            saveEntity.setOrderIndex(orderIndex);
            nodeCode = BuildNode(saveEntity, nodeText, orderIndex, nodeCode, parentNode);
            saveEntity.setNodeCode(nodeCode);
            entity = this.merge(saveEntity);// 执行修改方法

            return entity;
        } catch (IllegalAccessException e) {
            logger.error(e.getMessage());
            return null;
        } catch (InvocationTargetException e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    /**
     * 构建树节点数据并生成nodeCode
     *
     * @param saveEntity 要构建节点的实体
     * @param nodeText   节点文本
     * @param orderIndex 节点排序号
     * @param nodeCode   节点编码
     * @param parentNode 父节点
     * @return
     */
    private String BuildNode(TrainCoursecategory saveEntity, String nodeText, Integer orderIndex, String nodeCode, String parentNode) {
        Integer parentLevel;
        String parentCode;
        if (!parentNode.equals(TreeVeriable.ROOT)) {
            TrainCoursecategory parEntity = this.get(parentNode);
            parentLevel = parEntity.getNodeLevel();
            parentCode = parEntity.getNodeCode();
            parEntity.setLeaf(false);
            this.merge(parEntity);
            //根据上级节点的编码来生成当前的编码
            if (parentLevel == 0) {
                //上级节点层次为0时，编码为拼音首字母大写
                nodeCode = this.createNodeCode(nodeText);
            } else {
                nodeCode = parentCode + StringUtils.addString(orderIndex.toString(), "0", 2, "L");
            }
            saveEntity.BuildNode(parEntity);
        } else {
            saveEntity.BuildNode(null);
        }
        return nodeCode;
    }


    /**
     * listTree:获取系统菜单的树形列表
     *
     * @param whereSql :查询条件
     * @param orderSql :排序条件11
     * @return List<SysMenuTree>
     * @throws @since JDK 1.8
     * @author luoyibo
     */

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public List<TrainCoursecategoryTree> getTreeList(String whereSql, String orderSql) {

        StringBuffer hql = new StringBuffer("from TrainCoursecategory where 1=1");
        hql.append(whereSql);
        hql.append(orderSql);

        // 总记录数
        StringBuffer countHql = new StringBuffer("select count(*) from TrainCoursecategory where 1=1");
        countHql.append(whereSql);

        List<TrainCoursecategory> typeList = super.doQuery(hql.toString());
        List<TrainCoursecategoryTree> result = new ArrayList<TrainCoursecategoryTree>();
        // 构建Tree数据
        recursion(new TrainCoursecategoryTree(TreeVeriable.ROOT, new ArrayList<TrainCoursecategoryTree>()), result, typeList);

        return result;
    }

    private void recursion(TrainCoursecategoryTree parentNode, List<TrainCoursecategoryTree> result, List<TrainCoursecategory> list) {
        List<TrainCoursecategory> childs = new ArrayList<TrainCoursecategory>();
        for (TrainCoursecategory trainCoursecategory : list) {
            if (trainCoursecategory.getParentNode().equals(parentNode.getId())) {
                childs.add(trainCoursecategory);
            }
        }
        for (TrainCoursecategory trainCoursecategory : childs) {
            TrainCoursecategoryTree child = new TrainCoursecategoryTree(trainCoursecategory.getUuid(), trainCoursecategory.getNodeText(), "", trainCoursecategory.getLeaf(),
                    trainCoursecategory.getNodeLevel(), trainCoursecategory.getTreeIds(), trainCoursecategory.getParentNode(), trainCoursecategory.getOrderIndex(), new ArrayList<TrainCoursecategoryTree>(),
                    trainCoursecategory.getCategoryDesc(), true, trainCoursecategory.getNodeCode());

            if (trainCoursecategory.getParentNode().equals(TreeVeriable.ROOT)) {
                result.add(child);
            } else {
                List<TrainCoursecategoryTree> trees = parentNode.getChildren();
                trees.add(child);
                parentNode.setChildren(trees);
            }
            recursion(child, result, list);
        }
    }

    /**
     * 生成同一分类下的排序号
     *
     * @param parentNode 要生成排序号的上级分类
     * @return
     */
    private Integer getOrderIndex(String parentNode) {
        Integer orderIndex = Integer.valueOf(1);

        String hql = " from  TrainCoursecategory  where orderIndex=(select max(orderIndex) from TrainCoursecategory where parentNode=''{0}'')";
        hql = MessageFormat.format(hql, parentNode);
        List<TrainCoursecategory> list = this.doQuery(hql);
        if (list.size() > 0) {
            orderIndex = (Integer) EntityUtil.getPropertyValue(list.get(0), "orderIndex") + 1;
        } else
            orderIndex = 1;

        return orderIndex;
    }

    /**
     * 分类名称转换成只有首字母大写的拼音形式
     * 如果出现重复，在原有的基础上变成 _+重名个数
     *
     * @param nodeText
     * @return
     */
    private String createNodeCode(String nodeText) {
        //先生成一个编码，只取每个汉字的大写首字母
        String nodeCode = StringUtils.ConvertWenziToPingYing(nodeText, CharConvertType.FIRSTUPPER, CharConvertType.RETURNFIRST);
        String hql = " from TrainCoursecategory where nodeCode = ''{0}'' ";
        hql = MessageFormat.format(hql, nodeCode);
        List<TrainCoursecategory> list = this.doQuery(hql);
        if (list.size() > 1) {
            nodeCode += "_" + StringUtils.addString(String.valueOf(list.size()), "0", 2, "L");
            return nodeCode;
        } else {
            return nodeCode;
        }
    }
    
    @Override
    public List<TrainCoursecategory> listExport(String ids,String orderSql) {
        String hql = " from TrainCoursecategory where isDelete=0 ";
        if (StringUtils.isNotEmpty(ids)) {
            hql += " and uuid in ('" + ids.replace(",", "','") + "')";
        }
        if("".equals(orderSql))
            hql += " order by parentNode,orderIndex asc";
        else
            hql += orderSql;
        List<TrainCoursecategory> list = this.doQuery(hql);
        List<TrainCoursecategory> exportList = new ArrayList<>();

        //导出时要转换字典项
        String mapKey = null;
        String[] propValue = {"HEADSHIPLEVEL", "XBM", "TRAINEECATEGORY", "XWM", "XLM", "ZZMMM", "MZM"};
        Map<String, String> mapDicItem = new HashMap<>();
        List<BaseDicitem> listDicItem = dicitemService.queryByProerties("dicCode", propValue);
//        for (BaseDicitem baseDicitem : listDicItem) {
//            mapKey = baseDicitem.getItemCode() + baseDicitem.getDicCode();
//            mapDicItem.put(mapKey, baseDicitem.getItemName());
//        }
//        for (TrainCoursecategory trainee : list) {
//            trainee.setHeadshipLevelName(mapDicItem.get(trainee.getHeadshipLevel() + "HEADSHIPLEVEL"));
//            trainee.setXbmName(mapDicItem.get(trainee.getXbm() + "XBM"));
//            trainee.setTraineeCategoryName(mapDicItem.get(trainee.getTraineeCategory() + "TRAINEECATEGORY"));
//            trainee.setXwmName(mapDicItem.get(trainee.getXwm() + "XWM"));
//            trainee.setXlmName(mapDicItem.get(trainee.getXlm() + "XLM"));
//            trainee.setZzmmmName(mapDicItem.get(trainee.getZzmmm() + "ZZMMM"));
//            trainee.setMzmName(mapDicItem.get(trainee.getMzm() + "MZM"));
//        }
        exportList.addAll(list);

        return exportList;
    }
}