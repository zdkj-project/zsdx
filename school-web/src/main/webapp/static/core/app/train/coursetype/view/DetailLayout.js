Ext.define("core.train.coursetype.view.DetailLayout", {
	extend:"core.base.view.BasePanel",
	alias: "widget.coursetype.detaillayout",
	funCode: "coursetype_detail",
	funData: {
		action: comm.get("baseUrl") + "/TrainCoursecategory", //请求Action
		whereSql: "", //表格查询条件
		orderSql: "", //表格排序条件
		pkName: "uuid",
		defaultObj: {}
	},
    /*关联此视图控制器*/
	controller: 'coursetype.detailController',
	items: [{
		xtype: "coursetype.coursecategoryform"
	}]
});
