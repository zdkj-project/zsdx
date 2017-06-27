Ext.define("core.train.coursecategory.view.DetailLayout", {
	extend:"core.base.view.BasePanel",
	alias: "widget.coursecategory.detaillayout",
	funCode: "coursecategory_detail",
	funData: {
		action: comm.get("baseUrl") + "/TrainCourseinfo", //请求Action
		whereSql: "", //表格查询条件
		orderSql: "", //表格排序条件
		pkName: "uuid",
		defaultObj: {}
	},
    /*关联此视图控制器*/
	controller: 'coursecategory.detailController',
	items: [{
		xtype: "coursecategory.detailform"
	}]
});
