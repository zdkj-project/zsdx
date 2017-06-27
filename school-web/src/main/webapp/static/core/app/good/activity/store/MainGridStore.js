Ext.define("core.good.activity.store.MainGridStore",{
	extend:"Ext.data.Store",
	
	alias: 'store.activity.maingridstore',

 	model:'core.good.activity.model.MainGridModel',

	data:{ 
		items:  [
	        { uuid: '13120', name: '张三', sex:true,age:20,birthday: '2002/2/25' },
	      	{ uuid: '13121', name: '李四', sex:false,age:30,birthday: '2004/2/22' },
	      	{ uuid: '13122', name: '王五', sex:true,age:20,birthday: '2011/2/21' },
	      	{ uuid: '13123', name: '赵六', sex:true,age:27,birthday: '2013/2/27' },
	      	{ uuid: '13124', name: '刘七', sex:false,age:10,birthday: '2014/2/20' },
	      	{ uuid: '13125', name: '朱八', sex:true,age:10,birthday: '2001/2/28' },
	    ]
	},  
	proxy: {
        type: 'memory',
        reader: {
            type: 'json',
            rootProperty: 'items'
        }
    },

    /*
	proxy:{
		type:"ajax",
		url:"/singnup/list",
		extraParams :{},
		reader:{
			type:"json",
			root:"rows",
			totalProperty :'totalCount'
		},
		writer:{
			type:"json"
		}
	},*/

})
