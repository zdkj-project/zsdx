package com.zd.school.plartform.baseset.model;

import java.io.Serializable;

public class BaseOrgToUP implements Serializable {

	private static final long serialVersionUID = 1L;

	private String departmentId;
	private String parentDepartmentId;
	private String departmentName;
	private String layer;
	private String layerorder;
	private String departmentStatus;
	

	public String getDepartmentId() {
		return departmentId;
	}


	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}


	public String getParentDepartmentId() {
		return parentDepartmentId;
	}


	public void setParentDepartmentId(String parentDepartmentId) {
		this.parentDepartmentId = parentDepartmentId;
	}


	public String getDepartmentName() {
		return departmentName;
	}


	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}


	public String getLayer() {
		return layer;
	}


	public void setLayer(String layer) {
		this.layer = layer;
	}


	public String getLayerorder() {
		return layerorder;
	}


	public void setLayerorder(String layerorder) {
		this.layerorder = layerorder;
	}

	

	public String getDepartmentStatus() {
		return departmentStatus;
	}


	public void setDepartmentStatus(String departmentStatus) {
		this.departmentStatus = departmentStatus;
	}


	public BaseOrgToUP() {
		super();
	}


	


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BaseOrgToUP other = (BaseOrgToUP) obj;
		
		if (departmentId == null) {
			if (other.departmentId != null)
				return false;
		} else if (!departmentId.equals(other.departmentId))
			return false;
		if (departmentName == null) {
			if (other.departmentName != null)
				return false;
		} else if (!departmentName.equals(other.departmentName))
			return false;
		if (layer == null) {
			if (other.layer != null)
				return false;
		} else if (!layer.equals(other.layer))
			return false;
		if (layerorder == null) {
			if (other.layerorder != null)
				return false;
		} else if (!layerorder.equals(other.layerorder))
			return false;
		if (parentDepartmentId == null) {
			if (other.parentDepartmentId != null)
				return false;
		} else if (!parentDepartmentId.equals(other.parentDepartmentId))
			return false;
		return true;
	}

	
		

}
