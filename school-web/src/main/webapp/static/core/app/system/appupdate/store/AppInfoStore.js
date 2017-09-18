Ext.define("core.system.appupdate.store.AppInfoStore",{
	extend:"Ext.data.Store",
	fields: ['uuid','appTitle', 'appUrl','appIntro','appVersionId','appIsuse',"createTime","appType"],
	proxy:{
		type:"ajax",
		url:comm.get('baseUrl') + "/GoodAppInfo/list",
		extraParams :{
			isLoad:0
		},
		reader:{
			type:"json",
			rootProperty :"rows",
			totalProperty :'totalCount'
		},
		writer:{
			type:"json"
		}
	},
	sorters:[{
        property: 'createTime',
        direction: 'DESC'
    }],
	autoLoad:true,
	remoteSort:false,
	pageSize:20
})
