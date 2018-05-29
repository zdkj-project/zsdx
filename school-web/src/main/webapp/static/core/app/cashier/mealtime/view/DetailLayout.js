Ext.define("core.cashier.mealtime.view.DetailLayout", {
	extend:"core.base.view.BasePanel",
	alias: "widget.mealtime.detaillayout",
	funCode: "mealtime_detail",
    detCode: "mealtime_detail",
    detLayout: "mealtime.detaillayout",
	funData: {
		action: comm.get("baseUrl") + "/CashDinnertime", //请求Action
		whereSql: "", //表格查询条件
		orderSql: "", //表格排序条件
		pkName: "uuid",
		defaultObj: {}
	},
	/*设置最小宽度，并且自动滚动*/
    minWidth: 1200,
    scrollable: true,
    /*关联此视图控制器*/
	controller: 'mealtime.detailController',
	items: [{
		xtype: "mealtime.detailform"
	}]
});
