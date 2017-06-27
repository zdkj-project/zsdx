Ext.define("core.system.menu.store.MenuStore",{
	extend:"Ext.data.TreeStore",
	defaultRootId:"ROOT",
	model:factory.ModelFactory.getModelByName("com.zd.core.model.extjs.JSONTreeNode","checked").modelName,
	proxy:{
		type:"ajax",
		url:comm.get('baseUrl') + "/sysmenu/getMenuTree",
		extraParams :{excludes: 'checked'},
		reader:{
			type:"json"
		},
		writer:{
			type:"json"
		}
	},
	autoLoad:true
 });