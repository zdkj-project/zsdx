Ext.define("core.train.alleval.view.DetailLayout", {
	extend:"core.base.view.BasePanel",
	alias: "widget.alleval.detaillayout",
	funCode: "alleval_detail",
    detCode: "alleval_detail",
    detLayout: "alleval.detaillayout",
	funData: {
		action: comm.get("baseUrl") + "/TrainEvalalleval", //请求Action
		whereSql: "", //表格查询条件
		orderSql: "", //表格排序条件
		pkName: "uuid",
		defaultObj: {}
	},
    /*关联此视图控制器*/
	controller: 'alleval.detailController',
	items: [{
		xtype: "alleval.detailform"
	}]
});
