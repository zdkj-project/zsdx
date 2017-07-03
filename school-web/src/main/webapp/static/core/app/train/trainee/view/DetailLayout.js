Ext.define("core.train.trainee.view.DetailLayout", {
	extend:"core.base.view.BasePanel",
	alias: "widget.trainee.detaillayout",
	funCode: "trainee_detail",
    detCode: "trainee_detail",
    detLayout: "trainee.detaillayout",
	funData: {
		action: comm.get("baseUrl") + "/TrainTrainee", //请求Action
		whereSql: "", //表格查询条件
		orderSql: "", //表格排序条件
		pkName: "uuid",
		defaultObj: {}
	},
	/*设置最小宽度，并且自动滚动*/
    minWidth: 1200,
    scrollable: true,
    /*关联此视图控制器*/
	controller: 'trainee.detailController',
	items: [{
		xtype: "trainee.detailform"
	}]
});
