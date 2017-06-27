Ext.define("core.train.creditrule.view.DetailLayout", {
	extend:"core.base.view.BasePanel",
	alias: "widget.creditrule.detaillayout",
	funCode: "creditrule_detail",
	funData: {
		action: comm.get("baseUrl") + "/TrainCreditsrule", //请求Action
		whereSql: "", //表格查询条件
		orderSql: "", //表格排序条件
		pkName: "uuid",
		defaultObj: {}
	},
    /*关联此视图控制器*/
	controller: 'creditrule.detailController',
	items: [{
		xtype: "creditrule.detailform"
	}]
});
