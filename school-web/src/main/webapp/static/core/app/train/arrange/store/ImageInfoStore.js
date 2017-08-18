Ext.define("core.train.arrange.store.ImageInfoStore",{
	extend:"Ext.data.Store",

	alias: 'store.arrange.imageinfoStore',

	model:factory.ModelFactory.getModelByName("com.zd.school.plartform.baseset.model.BaseAttachment","checked").modelName,
	proxy:{
		type:"ajax",
		url:comm.get('baseUrl') + "/BaseAttachment/attachmentList",
		extraParams :{
			//filter :"[{'type':'string','comparison':'=','value':'0','field':'classId'}]"
		},
		reader:{
			type:"json",
			root:"rows",
			totalProperty :'totalCount'
		},
		writer:{
			type:"json"
		}
	},
	autoLoad:false,
	remoteSort:false,
	pageSize:8,
	sorters: [{
        property: 'createTime',
        direction: 'DESC'
    }]
})