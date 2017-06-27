package com.zd.school.salary.salary.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.zd.core.annotation.FieldInfo;

/**
 * 
 * ClassName: XcSalarybook Function: TODO ADD FUNCTION. Reason: TODO ADD
 * REASON(可选). Description: 工资台账表(XC_T_SALARYBOOK)实体类. date: 2016-12-05
 *
 * @author luoyibo 创建文件
 * @version 0.1
 * @since JDK 1.8
 */


@Entity
@Table(name = "aduoge")
public class aduoge {
	private static final long serialVersionUID = 1L;
	
	@Id
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @GeneratedValue(generator = "uuid")
    @FieldInfo(name = "学生ID", type = "ID")
    @Column(name = "zhujian", length = 50, nullable = false)
	 private String zhujian;

	public String getXm() {
		return xm;
	}


	public void setXm(String xm) {
		this.xm = xm;
	}


	public String getMoney() {
		return money;
	}


	public void setMoney(String money) {
		this.money = money;
	}


	public String getZhujian() {
		return zhujian;
	}


	public void setZhujian(String zhujian) {
		this.zhujian = zhujian;
	}


	@FieldInfo(name = "工资套账ID")
	@Column(name = "xm", length = 36, nullable = true)
	private String xm;

	
	@FieldInfo(name = "工资套账ID")
	@Column(name = "money", length = 36, nullable = true)
	private String money;
	
	@FieldInfo(name = "工资套账ID")
	@Column(name = "yt", length = 36, nullable = true)
	private String yt;

	public String getYt() {
		return yt;
	}


	public void setYt(String yt) {
		this.yt = yt;
	}
	
	private String xj;

	public String getXj() {
		return xj;
	}


	public void setXj(String xj) {
		this.xj = xj;
	}
	
	
	



	


}