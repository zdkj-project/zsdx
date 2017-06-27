Ext.define("core.train.courseeval.view.DetailLayout", {
	extend:"core.base.view.BasePanel",
	alias: "widget.courseeval.detaillayout",
	funCode: "courseeval_detail",
    detCode: "courseeval_detail",
    detLayout: "courseeval.detaillayout",
	funData: {
		action: comm.get("baseUrl") + "/TrainEvalcourseeval", //请求Action
		whereSql: "", //表格查询条件
		orderSql: "", //表格排序条件
		pkName: "uuid",
		defaultObj: {}
	},
    /*关联此视图控制器*/
	controller: 'courseeval.detailController',
	items: [{
		xtype: "courseeval.detailform"
	}]
});
