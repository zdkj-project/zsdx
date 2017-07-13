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
		tabConfig:{         //zzk：2017-6-1加入，用于对tab操作提供基本配置数据
        	addTitle:'添加字典',
        	editTitle:'编辑字典',
        	detailTitle:'字典详细',
        }
    },    
    layout: 'border',
    
    /*设置最小宽度，并且自动滚动*/
    minWidth:1200,
    scrollable:true,
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