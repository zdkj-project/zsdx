Ext.define("core.train.class.view.DetailLayout", {
	extend:"core.base.view.BasePanel",
	alias: "widget.class.detaillayout",
	funCode: "class_detail",
	funData: {
		action: comm.get("baseUrl") + "/TrainClass", //请求Action
		whereSql: "", //表格查询条件
		orderSql: "", //表格排序条件
		pkName: "uuid",
		defaultObj: {}
	},
	/*设置最小宽度*/
    minWidth:1200,
    scrollable:true,

    /*关联此视图控制器*/
	controller: 'class.detailController',
	items: [{
		xtype: "class.detailform"
	}]
});
