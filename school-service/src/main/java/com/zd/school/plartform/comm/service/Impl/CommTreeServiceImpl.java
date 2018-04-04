package com.zd.school.plartform.comm.service.Impl;

import com.zd.core.constant.TreeVeriable;
import com.zd.core.model.BaseEntity;
import com.zd.core.service.BaseServiceImpl;
import com.zd.school.plartform.comm.dao.CommTreeDao;
import com.zd.school.plartform.comm.model.*;
import com.zd.school.plartform.comm.service.CommTreeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * ClassName: FacultyClassitemServiceImpl Function: TODO ADD FUNCTION. Reason:
 * TODO ADD REASON(可选). Description: 数据字典项实体Service接口实现类. date: 2016-07-19
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
@Service
@Transactional
public class CommTreeServiceImpl extends BaseServiceImpl<BaseEntity> implements CommTreeService {

    @Resource
    public void setDao(CommTreeDao dao) {
        this.dao = dao;
    }

    @Override
    public List<FacultyClassTree> getFacultyClassTree(String whereSql) {

        String sql = "select id,text,iconCls,leaf,level,parent from EDU_V_FACULTYCLASS_TREE where 1=1 " + whereSql;
        List<FacultyClass> lists = this.getQuerySqlObject(sql, FacultyClass.class);

        List<FacultyClassTree> result = new ArrayList<FacultyClassTree>();

        // 构建Tree数据
        createFacultyClassChild(new FacultyClassTree(TreeVeriable.ROOT, new ArrayList<FacultyClassTree>()), result,
                lists);

        return result;
    }

    private void createFacultyClassChild(FacultyClassTree parentNode, List<FacultyClassTree> result,
            List<FacultyClass> list) {
        List<FacultyClass> childs = new ArrayList<FacultyClass>();
        for (FacultyClass dic : list) {
            if (dic.getParent().equals(parentNode.getId())) {
                childs.add(dic);
            }
        }
        // public FacultyClassTree(String id, String text, String iconCls,
        // Boolean leaf, Integer level, String treeid,
        // List<FacultyClassTree> children, String parent) {
        for (FacultyClass fc : childs) {
            FacultyClassTree child = new FacultyClassTree(fc.getId(), fc.getText(), fc.getIconCls(),
                    Boolean.parseBoolean(fc.getLeaf().toString()), fc.getLevel(), "", fc.getParent(),0,new ArrayList()
                    );

            if (fc.getParent().equals(TreeVeriable.ROOT)) {
                result.add(child);
            } else {
                List<FacultyClassTree> trees = parentNode.getChildren();
                trees.add(child);
                parentNode.setChildren(trees);
            }
            createFacultyClassChild(child, result, list);
        }
    }

    @Override
    public List<CommTree> getCommTree(String treeView, String whereSql) {

        String sql = "select id,text,iconCls,leaf,level,parent from " + treeView + " where 1=1 " + whereSql;

        List<CommBase> lists = this.getQuerySqlObject(sql, CommBase.class);

        List<CommTree> result = new ArrayList<CommTree>();

        // 构建Tree数据
        createTreeChild(new CommTree(TreeVeriable.ROOT, new ArrayList<CommTree>()), result, lists);

        return result;
    }

    public void createTreeChild(CommTree parentNode, List<CommTree> result, List<CommBase> list) {
        List<CommBase> childs = new ArrayList<CommBase>();
        for (CommBase dic : list) {
            if (dic.getParent().equals(parentNode.getId())) {
                childs.add(dic);
            }
        }

        for (CommBase fc : childs) {
            CommTree child = new CommTree(fc.getId(), fc.getText(), fc.getIconCls(), Boolean.parseBoolean(fc.getLeaf()),
                    fc.getLevel(), "", fc.getParent(),0, new ArrayList<CommTree>());

            if (fc.getParent().equals(TreeVeriable.ROOT)) {
                result.add(child);
            } else {
                List<CommTree> trees = parentNode.getChildren();
                trees.add(child);
                parentNode.setChildren(trees);
            }
            createTreeChild(child, result, list);
        }
    }

    @Override
    public List<UpGradeRule> getUpGradeRuleList() {

        String sql = "select beforeName,afterName,upDirect from base_t_upgraderule";
        List<UpGradeRule> lists = this.getQuerySqlObject(sql, UpGradeRule.class);
        return lists;
    }

}