package com.zd.school.control.device.model;


import java.util.List;

import com.zd.core.util.TagLenVal;

/**
 * @author  zenglj 创建文件
 * @version 0.1
 * @since JDK 1.8
 */
public class TLVModel {
	 private List<TagLenVal> tlvs;
	 private String uuid;
	 private String notes;
	public List<TagLenVal> getTlvs() {
		return tlvs;
	}

	public void setTlvs(List<TagLenVal> tlvs) {
		for(int i=0;i<tlvs.size();i++){
			if(tlvs.get(i)==null){
				tlvs.remove(i);
				i--;
			}
		}
		this.tlvs = tlvs;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}
	 

}
