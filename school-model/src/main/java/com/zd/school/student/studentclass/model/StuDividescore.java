package com.zd.school.student.studentclass.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.zd.core.annotation.FieldInfo;
import com.zd.core.model.BaseEntity;

/**
 * 
 * ClassName: StuDividescore 
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * Description: 学生分班成绩实体类.
 * date: 2016-08-23
 *
 * @author  luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
 
@Entity
@Table(name = "STU_T_DIVIDESCORE")
@AttributeOverride(name = "uuid", column = @Column(name = "SCORE_ID", length = 36, nullable = false))
public class StuDividescore extends BaseEntity implements Serializable{
    private static final long serialVersionUID = 1L;
    
    @FieldInfo(name = "记录ID")
    @Column(name = "DIVIDE_ID", length = 36, nullable = true)
    private String divideId;

    @FieldInfo(name = "学号")
    @Column(name = "XH", length = 20, nullable = false)
    private String xh;
        
    @FieldInfo(name = "姓名")
    @Column(name = "XM", length = 36, nullable = false)
    private String xm;
        
    @FieldInfo(name = "性别码")
    @Column(name = "XBM", length = 10, nullable = true)
    private String xbm;
        
    @FieldInfo(name = "成绩")
    @Column(name = "SCORE", length = 8, nullable = false)
    private BigDecimal score;

    @FieldInfo(name = "考号")
    @Column(name = "EXAM_NUMB", length = 20, nullable = false)
    private String  examNumb;
    
    @FieldInfo(name = "文理类型")
    @Column(name = "ARTS_SCIENCES", length = 16, nullable = true)
    private String artsSciences;

	public String getDivideId() {
		return divideId;
	}

	public void setDivideId(String divideId) {
		this.divideId = divideId;
	}

	public String getXh() {
		return xh;
	}

	public void setXh(String xh) {
		this.xh = xh;
	}

	public String getXm() {
		return xm;
	}

	public void setXm(String xm) {
		this.xm = xm;
	}

	public String getXbm() {
		return xbm;
	}

	public void setXbm(String xbm) {
		this.xbm = xbm;
	}

	public BigDecimal getScore() {
		return score;
	}

	public void setScore(BigDecimal score) {
		this.score = score;
	}

	public String getExamNumb() {
		return examNumb;
	}

	public void setExamNumb(String examNumb) {
		this.examNumb = examNumb;
	}

	public String getArtsSciences() {
		return artsSciences;
	}

	public void setArtsSciences(String artsSciences) {
		this.artsSciences = artsSciences;
	}

    /** 以下为不需要持久化到数据库中的字段,根据项目的需要手工增加 
    *@Transient
    *@FieldInfo(name = "")
    *private String field1;
    */
}