Ext.define("core.ordermanage.teacherorder.view.DetailLayout", {
	extend:"core.base.view.BasePanel",
	alias: "widget.teacherorder.detaillayout",
	funCode: "orderdesc_detail",
	funData: {
		action: comm.get("baseUrl") + "/TrainClassrealdinner", //请求Action
		whereSql: "", //表格查询条件
		orderSql: "", //表格排序条件
		pkName: "uuid",
		defaultObj: {}
	},
    /*关联此视图控制器*/
	controller: 'teacherorder.detailController',
	items: [{
		xtype: "teacherorder.mainform"
	}],

	/*设置最小宽度*/
    minWidth:1200,   
    scrollable:true, 
});
