/**
 * Project Name:school-model
 * File Name:FacultyClass.java
 * Package Name:com.zd.school.plartform.comm.model
 * Date:2016年7月21日上午11:23:11
 * Copyright (c) 2016 ZDKJ All Rights Reserved.
 *
*/

package com.zd.school.plartform.comm.model;

import com.zd.core.model.extjs.ExtTreeNode;

import java.util.List;

/**
 * ClassName:FacultyClass Function: TODO ADD FUNCTION. Reason: 系别-专业-班级数据视图实体.
 * Date: 2016年7月21日 上午11:23:11
 * 
 * @author luoyibo
 * @version
 * @since JDK 1.8
 * @see
 */
public class FacultyClassTree extends ExtTreeNode<FacultyClassTree> {


    public FacultyClassTree() {

        super();
        // TODO Auto-generated constructor stub

    }

    public FacultyClassTree(String id, List<FacultyClassTree> children) {

        super(id, children);
        // TODO Auto-generated constructor stub

    }

    public FacultyClassTree(String id, String text, String iconCls, Boolean leaf, Integer level, String treeid,String parent,Integer orderIndex,
            List<FacultyClassTree> children) {

        super(id, text, iconCls, leaf, level, treeid,parent,orderIndex, children);

    }

}
