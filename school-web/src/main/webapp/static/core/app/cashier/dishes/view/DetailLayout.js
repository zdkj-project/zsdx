Ext.define("core.cashier.dishes.view.DetailLayout", {
	extend:"core.base.view.BasePanel",
	alias: "widget.dishes.detaillayout",
	funCode: "dishes_detail",
    detCode: "dishes_detail",
    detLayout: "dishes.detaillayout",
	funData: {
		action: comm.get("baseUrl") + "/CashDishes", //请求Action
		whereSql: "", //表格查询条件
		orderSql: "", //表格排序条件
		pkName: "uuid",
		defaultObj: {}
	},
	/*设置最小宽度，并且自动滚动*/
    minWidth: 1200,
    scrollable: true,
    /*关联此视图控制器*/
	controller: 'dishes.detailController',
	items: [{
		xtype: "dishes.detailform"
	}]
});
