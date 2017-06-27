Ext.define("core.train.class.view.CourseDetailLayout", {
	extend:"core.base.view.BasePanel",
	alias: "widget.class.coursedetaillayout",
	funCode: "class_coursedetail",
	funData: {
		action: comm.get("baseUrl") + "/TrainClassschedule", //请求Action
		whereSql: "", //表格查询条件
		orderSql: "", //表格排序条件
		pkName: "uuid",
		defaultObj: {}
	},
    /*关联此视图控制器*/
	controller: 'class.detailController',
	items: [{
		xtype: "class.coursedetailform"
	}]
});
