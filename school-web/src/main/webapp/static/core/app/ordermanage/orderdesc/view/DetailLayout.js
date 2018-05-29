Ext.define("core.ordermanage.orderdesc.view.DetailLayout", {
	extend:"core.base.view.BasePanel",
	alias: "widget.orderdesc.detaillayout",
	funCode: "orderdesc_detail",
	funData: {
		action: comm.get("baseUrl") + "/TrainClassrealdinner", //请求Action
		whereSql: "", //表格查询条件
		orderSql: "", //表格排序条件
		pkName: "uuid",
		defaultObj: {}
	},
    /*关联此视图控制器*/
	controller: 'orderdesc.detailController',
	items: [{
		xtype: "orderdesc.detailform"
	}],

	/*设置最小宽度*/
    minWidth:1200,   
    scrollable:true, 
});
