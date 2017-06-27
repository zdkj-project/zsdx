Ext.define("core.system.user.store.DeptStore",{
	extend:"Ext.data.TreeStore",
	defaultRootId:"ROOT",
	model:factory.ModelFactory.getModelByName("com.zd.core.model.extjs.JSONTreeNode","checked").modelName,
	proxy:{
		type:"ajax",
		url: comm.get('baseUrl') + "/sysdept/getDeptTree",
		extraParams :{excludes: 'checked',whereSql:"  and isDelete='0'"},
		reader:{
			type:"json"
		},
		writer:{
			type:"json"
		}
	},
	autoLoad:true
 });