Ext.define("core.train.class.view.CourseMainLayout", {
	extend:"core.base.view.BasePanel",
	alias: "widget.class.coursemainlayout",
	funCode: "class_coursemain",
	funData: {
		action: comm.get("baseUrl") + "/TrainClassschedule", //请求Action
		whereSql: "", //表格查询条件
		orderSql: "", //表格排序条件
		pkName: "uuid",
		defaultObj: {}
	},

    /*关联此视图控制器*/
	controller: 'class.detailController',

	detCode: "class_coursedetail",
    detLayout: "class.coursedetaillayout",
    /*标注这个视图控制器的别名，以此提供给window处使用*/
    otherController:'class.otherController',

	items: [{
		xtype: "class.coursegrid"
	}]
});
