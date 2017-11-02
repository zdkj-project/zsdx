Ext.define("core.ordermanage.ordertotal.view.DetailLayout", {
	extend:"core.base.view.BasePanel",
	alias: "widget.ordertotal.detaillayout",
	funCode: "cashreport_detail",
	funData: {
		action: comm.get("baseUrl") + "/TrainTeacherOrder", //请求Action
		whereSql: "", //表格查询条件
		orderSql: "", //表格排序条件
		pkName: "uuid",
		defaultObj: {}
	},
	layout: 'fit',
    /*关联此视图控制器*/
	controller: 'ordertotal.detailController',
	items: [{
		xtype: "ordertotal.orderdetailgrid"
	}],

	/*设置最小宽度*/
    minWidth:1200,   
    scrollable:true, 
});
