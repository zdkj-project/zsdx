Ext.define("core.good.signup.store.MainGridStore",{
	extend:"Ext.data.Store",
	
	alias: 'store.signup.maingridstore',

 	model:'core.good.signup.model.MainGridModel',

	data:{ 
		items:  [
	        { uuid: 'Lisa', actTitle: 'lisa@simpsons.com', actDate: '2017/2/21 12:10' },
	        { uuid: 'Bart', actTitle: 'bart@simpsons.com', actDate: '2017/2/21 10:10' },
	        { uuid: 'Homer', actTitle: 'homer@simpsons.com', actDate: '2017/2/21 11:10' },
	        { uuid: 'Marge', actTitle: 'marge@simpsons.com', actDate: '2017/2/21 18:10' }
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
