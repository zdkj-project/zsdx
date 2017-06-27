package com.zd.activiti.oa.model.model;

import java.util.Date;

public class ActivitiModel {
	 private String id;  
	    private String name;  
	    private String key;  
	    private String description;  
	    private String createTime;  
	    private String lastUpdateTime;  
	    private int version;  
	    private String metaInfo;
	    private String category;
	  
	    public String getCategory() {
			return category;
		}

		public void setCategory(String category) {
			this.category = category;
		}

		public String getId() {  
	        return id;  
	    }  
	  
	    public void setId(String id) {  
	        this.id = id;  
	    }  
	  
 
	  
	    public String getCreateTime() {
			return createTime;
		}

		public void setCreateTime(String createTime) {
			this.createTime = createTime;
		}

		public String getLastUpdateTime() {
			return lastUpdateTime;
		}

		public void setLastUpdateTime(String lastUpdateTime) {
			this.lastUpdateTime = lastUpdateTime;
		}

		public int getVersion() {  
	        return version;  
	    }  
	  
	    public void setVersion(int version) {  
	        this.version = version;  
	    }  
	  
	    public String getName() {  
	        return name;  
	    }  
	  
	    public void setName(String name) {  
	        this.name = name;  
	    }  
	  
	    public String getKey() {  
	        return key;  
	    }  
	  
	    public void setKey(String key) {  
	        this.key = key;  
	    }  
	  
	    public String getDescription() {  
	        return description;  
	    }  
	  
	    public void setDescription(String description) {  
	        this.description = description;  
	    }  
	    


		public String getMetaInfo() {
			return metaInfo;
		}

		public void setMetaInfo(String metaInfo) {
			this.metaInfo = metaInfo;
		}

		@Override  
	    public String toString() {  
	        return "ActivitiModel [id=" + id + ", name=" + name + ", key=" + key  
	                + ", description=" + description + ", createTime=" + createTime  
	                + ", lastUpdateTime=" + lastUpdateTime + ", version=" + version  
	                + ", metaInfo="+metaInfo+"]";  
	    }  

}
