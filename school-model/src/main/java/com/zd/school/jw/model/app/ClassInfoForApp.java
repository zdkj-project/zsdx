package com.zd.school.jw.model.app;

import java.util.List;

import com.zd.school.jw.ecc.model.EccClassredflag;
import com.zd.school.jw.ecc.model.EccClassstar;
import com.zd.school.jw.eduresources.model.JwTGradeclass;
import com.zd.school.teacher.teacherinfo.model.TeaTeacherbase;


public class ClassInfoForApp {
	private boolean message;//调用结果 ture&false
	private String messageInfo;//返回状态信息
	private JwTGradeclass ClassInfo;//班级信息
	private TeaTeacherbase teacherInfo;//班主任信息
	private EccClassstar classstarInfo; //班级评星信息
	private List<EccClassredflag> redflagList; //班级流动红旗
	public boolean getMessage() {
		return message;
	}
	public void setMessage(boolean message) {
		this.message = message;
	}
	public String getMessageInfo() {
		return messageInfo;
	}
	public void setMessageInfo(String messageInfo) {
		this.messageInfo = messageInfo;
	}
	public JwTGradeclass getClassInfo() {
		return ClassInfo;
	}
	public void setClassInfo(JwTGradeclass classInfo) {
		ClassInfo = classInfo;
	}
	public TeaTeacherbase getTeacherInfo() {
		return teacherInfo;
	}
	public void setTeacherInfo(TeaTeacherbase teacherInfo) {
		this.teacherInfo = teacherInfo;
	}
	public EccClassstar getClassstarInfo() {
		return classstarInfo;
	}
	public void setClassstarInfo(EccClassstar classstarInfo) {
		this.classstarInfo = classstarInfo;
	}
	public List<EccClassredflag> getRedflagList() {
		return redflagList;
	}
	public void setRedflagList(List<EccClassredflag> redflagList) {
		this.redflagList = redflagList;
	}
	
}
