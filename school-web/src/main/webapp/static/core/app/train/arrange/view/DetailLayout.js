Ext.define("core.train.arrange.view.DetailLayout", {
	extend:"core.base.view.BasePanel",
	alias: "widget.arrange.detaillayout",
	funCode: "arrange_detail",
	funData: {
		action: comm.get("baseUrl") + "/TrainClass", //请求Action
		whereSql: "", //表格查询条件
		orderSql: "", //表格排序条件
		pkName: "uuid",
		defaultObj: {}
	},
    /*关联此视图控制器*/
	controller: 'arrange.detailController',
	items: [{
		xtype: "arrange.detailform"
	}],

	/*设置最小宽度*/
    minWidth:1200,   
    scrollable:true, 
});
