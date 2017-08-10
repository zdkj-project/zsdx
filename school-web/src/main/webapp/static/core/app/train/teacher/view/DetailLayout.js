Ext.define("core.train.teacher.view.DetailLayout", {
	extend:"core.base.view.BasePanel",
	alias: "widget.teacher.detaillayout",
	funCode: "teacher_detail",
	funData: {
		action: comm.get("baseUrl") + "/TrainTeacher", //请求Action
		whereSql: "", //表格查询条件
		orderSql: "", //表格排序条件
		pkName: "uuid",
		defaultObj: {}
	},
	/*设置最小宽度，并且自动滚动*/
    minWidth:1000,
    scrollable:true,

    /*关联此视图控制器*/
	controller: 'teacher.detailController',
	items: [{
		xtype: "teacher.detailform"
	}]
});
