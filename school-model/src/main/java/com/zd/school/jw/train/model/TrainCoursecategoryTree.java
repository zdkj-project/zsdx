package com.zd.school.jw.train.model;

import com.zd.core.annotation.FieldInfo;
import com.zd.core.model.extjs.ExtTreeNode;

import java.util.ArrayList;
import java.util.List;

public class TrainCoursecategoryTree extends ExtTreeNode<TrainCoursecategoryTree> {
	
/*	@FieldInfo(name = "课程分类名称")
	private String categoryName;

	 
    @FieldInfo(name = "课程分类编码")
    private String categoryCode;
    
    @FieldInfo(name = "上级课程分类")
    private String parentCategory;*/
    
    @FieldInfo(name = "课程分类说明")
	private String categoryDesc;

    @FieldInfo(name = "是否展开")
	private Boolean expanded;


	public void setCategoryDesc(String categoryDesc) {
		this.categoryDesc = categoryDesc;
	}

	public String getCategoryDesc() {
		return categoryDesc;
	}

	@FieldInfo(name = "分类编码")
	private  String nodeCode;

	public String getNodeCode() {
		return nodeCode;
	}

	public void setNodeCode(String nodeCode) {
		this.nodeCode = nodeCode;
	}
	/*    public String getCategoryName() {
		return categoryName;
	}


	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}


	public String getCategoryCode() {
		return categoryCode;
	}


	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}


	public String getParentCategory() {
		return parentCategory;
	}


	public void setParentCategory(String parentCategory) {
		this.parentCategory = parentCategory;
	}*/

	public Boolean getExpanded() {
		return expanded;
	}

	public void setExpanded(Boolean expanded) {
		this.expanded = expanded;
	}

//	public TrainCoursecategoryTree(String id, String text, String iconCls, Boolean leaf, Integer level, String treeid,
//								   List<TrainCoursecategoryTree> children, String categoryName, String categoryCode, String parentCategory, String categoryDesc, Integer orderIndex,Boolean expanded) {
//
//        super(id, text, iconCls, leaf, level, treeid, children);
//        this.categoryName = categoryName;
//        this.categoryCode = categoryCode;
//        this.parentCategory = parentCategory;
//        this.categoryDesc=categoryDesc;
//        this.orderIndex = orderIndex;
//        this.expanded = expanded;
//
//    }
    public TrainCoursecategoryTree(String id, String text, String iconCls, Boolean leaf, Integer level, String treeid,String parent,Integer orderIndex,
								   List<TrainCoursecategoryTree> children,  String categoryDesc, Boolean expanded,String nodeCode) {

        super(id, text, iconCls, leaf, level, treeid,parent,orderIndex,children);
/*        this.categoryName = categoryName;
        this.categoryCode = categoryCode;
        this.parentCategory = parentCategory;*/
		//super.setParent(parent);
        this.categoryDesc=categoryDesc;
        //this.orderIndex = orderIndex;
        this.expanded = expanded;
        this.nodeCode = nodeCode;

    }


	public TrainCoursecategoryTree(String root, ArrayList<TrainCoursecategoryTree> arrayList) {
		super(root,arrayList);
		
	}
}
