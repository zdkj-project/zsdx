package com.zd.school.build.define.service.Impl;

import com.zd.core.constant.TreeVeriable;
import com.zd.core.service.BaseServiceImpl;
import com.zd.core.util.StringUtils;
import com.zd.school.build.define.dao.BuildRoomareaDao;
import com.zd.school.build.define.model.BuildRoomAreaTree;
import com.zd.school.build.define.model.BuildRoomarea;
import com.zd.school.build.define.service.BuildRoomareaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * ClassName: BuildRoomareaServiceImpl Function: TODO ADD FUNCTION. Reason: TODO
 * ADD REASON(可选). Description: 教室区域实体Service接口实现类. date: 2016-08-23
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Service
@Transactional
public class BuildRoomareaServiceImpl extends BaseServiceImpl<BuildRoomarea> implements BuildRoomareaService {

    @Resource
    public void setBuildRoomareaDao(BuildRoomareaDao dao) {
        this.dao = dao;
    }

    @Override
    public List<BuildRoomAreaTree> getBuildAreaList(String whereSql) {

        String hql = "from BuildRoomarea where 1=1 ";
        if (StringUtils.isNotEmpty(whereSql))
            hql += whereSql;
        hql += " order by orderIndex asc ";
        List<BuildRoomarea> lists = this.getQuery(hql);// 执行查询方法
        List<BuildRoomAreaTree> result = new ArrayList<BuildRoomAreaTree>();

        // 构建Tree数据
        createChild(new BuildRoomAreaTree(TreeVeriable.ROOT, new ArrayList<BuildRoomAreaTree>()), result, lists);

        return result;
    }

    private void createChild(BuildRoomAreaTree parentNode, List<BuildRoomAreaTree> result, List<BuildRoomarea> list) {
        List<BuildRoomarea> childs = new ArrayList<BuildRoomarea>();
        for (BuildRoomarea dic : list) {
            if (dic.getParentNode().equals(parentNode.getId())) {
                childs.add(dic);
            }
        }
        //        public BuildRoomAreaTree(String id, String text, String iconCls, Boolean leaf, Integer level, String treeid,
        //                List<BuildRoomAreaTree> children, String areaCode, String areaType, Integer areaStatu, String areaDesc, String areaAddr,
        //                String parentArea, Integer orderIndex, Integer roomCount) {

        for (BuildRoomarea dic : childs) {
            BuildRoomAreaTree child = new BuildRoomAreaTree(dic.getUuid(), dic.getNodeText(), "", dic.getLeaf(),
                    dic.getNodeLevel(), dic.getTreeIds(),dic.getParentNode(),dic.getOrderIndex(), new ArrayList<BuildRoomAreaTree>(), dic.getAreaCode(),
                    dic.getAreaType(), dic.getAreaStatu(), dic.getAreaDesc(), dic.getAreaAddr(), dic.getRoomCount());

            if (dic.getParentNode().equals(TreeVeriable.ROOT)) {
                result.add(child);
            } else {
                List<BuildRoomAreaTree> trees = parentNode.getChildren();
                trees.add(child);
                parentNode.setChildren(trees);
            }
            createChild(child, result, list);
        }
    }

    @Override
    public Integer getChildCount(String areaId) {

        String hql = " select count(*) from BuildRoomarea where isDelete=0 and parentNode='" + areaId + "'";
        Integer childCount = this.getCount(hql);
        // TODO Auto-generated method stub
        return childCount;
    }
}
