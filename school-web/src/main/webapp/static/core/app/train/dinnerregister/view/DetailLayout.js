Ext.define("core.train.dinnerregister.view.DetailLayout", {
	extend:"core.base.view.BasePanel",
	alias: "widget.dinnerregister.detaillayout",
	funCode: "dinnerregister_detail",
	funData: {
		action: comm.get("baseUrl") + "/TrainClassrealdinner", //请求Action
		whereSql: "", //表格查询条件
		orderSql: "", //表格排序条件
		pkName: "uuid",
		defaultObj: {}
	},
    /*关联此视图控制器*/
	controller: 'dinnerregister.detailController',
	items: [{
		xtype: "dinnerregister.detailform"
	}],

	/*设置最小宽度*/
    minWidth:1200,   
    scrollable:true, 
});
