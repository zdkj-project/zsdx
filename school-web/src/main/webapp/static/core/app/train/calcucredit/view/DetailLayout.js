Ext.define("core.train.calcucredit.view.DetailLayout", {
	extend:"core.base.view.BasePanel",
	alias: "widget.calcucredit.detaillayout",
	funCode: "calcucredit_detail",
	funData: {
		action: comm.get("baseUrl") + "/TrainClass", //请求Action
		whereSql: "", //表格查询条件
		orderSql: "", //表格排序条件
		pkName: "uuid",
		defaultObj: {}
	},
    /*关联此视图控制器*/
	controller: 'calcucredit.detailController',
	items: [{
		xtype: "calcucredit.detailform"
	}]
});
