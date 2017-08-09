Ext.define("core.train.dinnertotal.view.DetailLayout", {
	extend:"core.base.view.BasePanel",
	alias: "widget.dinnertotal.detaillayout",
	funCode: "dinnertotal_detail",
	funData: {
		action: comm.get("baseUrl") + "/TrainClassrealdinner", //请求Action
		whereSql: "", //表格查询条件
		orderSql: "", //表格排序条件
		pkName: "uuid",
		defaultObj: {}
	},
	layout: 'fit',
    /*关联此视图控制器*/
	controller: 'dinnertotal.detailController',
	items: [{
		xtype: "dinnertotal.classdinnergrid"
	}],

	/*设置最小宽度*/
    minWidth:1200,   
    scrollable:true, 
});
