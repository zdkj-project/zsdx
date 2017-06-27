package com.zd.school.jw.ecc.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Formula;

import com.zd.core.annotation.FieldInfo;
import com.zd.core.model.BaseEntity;
import com.zd.school.plartform.baseset.model.BaseAttachment;

@Entity
@Table(name = "JW_T_CLASSELEGANT")
@AttributeOverride(name = "uuid", column = @Column(name = "ELEGANT_ID", length = 36, nullable = false))
public class EccClasselegant extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@FieldInfo(name = "班级ID")
	@Column(name = "CLAI_ID", length = 36, nullable = true)
	private String claiId;

	public void setClaiId(String claiId) {
		this.claiId = claiId;
	}

	public String getClaiId() {
		return claiId;
	}
	
	@Formula("(SELECT a.CLASS_NAME FROM JW_T_GRADECLASS a WHERE a.CLAI_ID=CLAI_ID )")
    private String className;

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	@FieldInfo(name = "标题")
	@Column(name = "TITLE", length = 36, nullable = true)
	private String title;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	@Transient
    @FieldInfo(name = "文件列表")
	private List<BaseAttachment> fileList;

	public List<BaseAttachment> getFileList() {
		return fileList;
	}

	public void setFileList(List<BaseAttachment> fileList) {
		this.fileList = fileList;
	}
}
