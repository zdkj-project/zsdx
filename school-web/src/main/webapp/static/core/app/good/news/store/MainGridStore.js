Ext.define("core.good.news.store.MainGridStore",{
	extend:"Ext.data.Store",
	
	alias: 'store.news.maingridstore',

 	model:'core.good.news.model.MainGridModel',

	data:{ 
		items:  [
	        { uuid: 'Lisa', newTitle: 'lisa@simpsons.com', newIntro:'阿斯达岁的阿萨德阿斯',newIntro:'阿斯达岁的阿萨德阿斯阿斯达岁的阿萨德阿斯阿斯达岁的阿萨德阿斯',createUser:'ZZK',createTime: '2017/2/21 12:10' },
	        { uuid: 'Bart', newTitle: 'bart@simpsons.com', newIntro:'阿斯达岁的阿萨德阿斯',newIntro:'阿斯达岁的阿萨德阿斯阿斯达岁的阿萨德阿斯阿斯达岁的阿萨德阿斯',createUser:'ZZK',createTime: '2017/2/21 10:10' },
	        { uuid: 'Homer', newTitle: 'homer@simpsons.com',newIntro:'阿斯达岁的阿萨德阿斯',newIntro:'阿斯达岁的阿萨德阿斯阿斯达岁的阿萨德阿斯阿斯达岁的阿萨德阿斯',createUser:'ZZK', createTime: '2017/2/21 11:10' },
	        { uuid: 'Marge', newTitle: 'marge@simpsons.com', newIntro:'阿斯达岁的阿萨德阿斯',newIntro:'阿斯达岁的阿萨德阿斯阿斯达岁的阿萨德阿斯阿斯达岁的阿萨德阿斯',createUser:'ZZK',createTime: '2017/2/21 18:10' }
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
