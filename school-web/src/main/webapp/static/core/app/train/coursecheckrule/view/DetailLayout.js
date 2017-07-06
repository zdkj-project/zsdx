Ext.define("core.train.coursecheckrule.view.DetailLayout", {
	extend:"core.base.view.BasePanel",
	alias: "widget.coursecheckrule.detaillayout",
	funCode: "coursecheckrule_detail",
	funData: {
		action: comm.get("baseUrl") + "/TrainCheckrule", //请求Action
		whereSql: "", //表格查询条件
		orderSql: "", //表格排序条件
		pkName: "uuid",
		defaultObj: {}
	},
    /*关联此视图控制器*/
	controller: 'coursecheckrule.detailController',
	 /*设置最小宽度，并且自动滚动*/
    minWidth:1200,
    scrollable:true,
	items: [{
		xtype: "coursecheckrule.detailform"
	}]
});
