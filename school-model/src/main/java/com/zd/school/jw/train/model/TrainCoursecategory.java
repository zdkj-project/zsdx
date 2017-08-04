package com.zd.school.jw.train.model;

import com.zd.core.annotation.FieldInfo;
import com.zd.core.model.TreeNodeEntity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * ClassName: TrainCoursecategory Function: TODO ADD FUNCTION. Reason: TODO ADD
 * REASON(可选). Description: 课程分类信息(TRAIN_T_COURSECATEGORY)实体类. date: 2017-03-07
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */

@Entity
@Table(name = "TRAIN_T_COURSECATEGORY")
@AttributeOverrides({
        @AttributeOverride(name = "uuid", column = @Column(name = "CATEGORY_ID", length = 36, nullable = false)),
        @AttributeOverride(name = "nodeCode", column = @Column(name = "CATEGORY_CODE", length = 32)),
        @AttributeOverride(name = "nodeText", column = @Column(name = "CATEGORY_NAME", length = 36)),
        @AttributeOverride(name = "parentNode", column = @Column(name = "PARENT_CATEGORY", length = 36))
})
public class TrainCoursecategory extends TreeNodeEntity implements Serializable {
    private static final long serialVersionUID = 1L;

/* 	@FieldInfo(name = "分类名称")
    @Column(name = "CATEGORY_NAME", length = 36, nullable = false)
	private String categoryName;

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getCategoryName() {
		return categoryName;
	}

	@FieldInfo(name = "分类编码")
	@Column(name = "CATEGORY_CODE", length = 16, nullable = true)
	private String categoryCode;

	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}

	public String getCategoryCode() {
		return categoryCode;
	}

	@FieldInfo(name = "上级分类")
	@Column(name = "PARENT_CATEGORY", length = 36, nullable = false)
	private String parentCategory;

	public void setParentCategory(String parentCategory) {
		this.parentCategory = parentCategory;
	}

	public String getParentCategory() {
		return parentCategory;
	}*/

    @FieldInfo(name = "分类说明")
    @Column(name = "CATEGORY_DESC", length = 256, nullable = true)
    private String categoryDesc;

    public void setCategoryDesc(String categoryDesc) {
        this.categoryDesc = categoryDesc;
    }

    public String getCategoryDesc() {
        return categoryDesc;
    }

    /**
     * 以下为不需要持久化到数据库中的字段,根据项目的需要手工增加
     *
     * @Transient
     * @FieldInfo(name = "") private String field1;
     */
}