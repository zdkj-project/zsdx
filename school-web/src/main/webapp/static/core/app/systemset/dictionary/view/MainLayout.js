Ext.define("core.systemset.dictionary.view.MainLayout", {
	extend: "core.base.view.BasePanel",
	alias: 'widget.dictionary.mainlayout',
    requires: [    
		"core.systemset.dictionary.view.MainLayout",
		"core.systemset.dictionary.view.dicDetailLayout",
		"core.systemset.dictionary.view.dicGrid",
		"core.systemset.dictionary.view.dicForm",
		"core.systemset.dictionary.view.itemLayout",
		"core.systemset.dictionary.view.itemGrid",
		"core.systemset.dictionary.view.itemForm"
   
    ],

    controller: 'dictionary.dictionaryController',
	funCode: "dic_main",
	detCode: 'dic_detail',
	detLayout: 'dic.detaillayout',
	border: false,
	funData: {
		action: comm.get('baseUrl') + "/BaseDic", //请求Action
		whereSql: "", //表格查询条件
		orderSql: "", //表格排序条件
		filter:"",
		pkName: "id",
		defaultObj: {
			orderIndex: 1,
			dicType:"LIST"
		},
		width:500,
		height:400
	},
	layout: 'border',
	//bodyPadding: 2,
	items: [{
		xtype: "dic.dicgrid",
		region: "west",
		width: comm.get("clientWidth") * 0.31,
		margin:'0 5 0 0',
		border: false,
		style:{
            border: '1px solid #ddd'
        },
		frame:false
	}, {
		xtype: "dic.itemgrid",
		region: "center",
		border: false,
		style:{
            border: '1px solid #ddd'
        },
		frame:false
	}]
})