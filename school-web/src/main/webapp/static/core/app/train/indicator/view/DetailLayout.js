Ext.define("core.train.indicator.view.DetailLayout", {
	extend:"core.base.view.BasePanel",
	alias: "widget.indicator.detaillayout",
	funCode: "indicator_detail",
    detCode: "indicator_detail",
    detLayout: "indicator.detaillayout",
	funData: {
		action: comm.get("baseUrl") + "/TrainEvalIndicator", //请求Action
		whereSql: "", //表格查询条件
		orderSql: "", //表格排序条件
		pkName: "uuid",
		defaultObj: {}
	},
    /*关联此视图控制器*/
	controller: 'indicator.detailController',
	items: [{
		xtype: "indicator.detailform"
	}]
});
