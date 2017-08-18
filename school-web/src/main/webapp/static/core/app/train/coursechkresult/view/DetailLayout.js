Ext.define("core.train.coursechkresult.view.DetailLayout", {
	extend:"core.base.view.BasePanel",
	alias: "widget.coursechkresult.detaillayout",
	funCode: "coursechkresult_detail",
    layout:'border',
    border:false,
	funData: {
		action: comm.get("baseUrl") + "/TrainClass", //请求Action
		whereSql: "", //表格查询条件
		orderSql: "", //表格排序条件
		pkName: "uuid",
		defaultObj: {}
	},
    /*关联此视图控制器*/
	controller: 'coursechkresult.detailController',
    minWidth:1200,
    scrollable:'x',
	items: [{
		xtype: "coursechkresult.classcoursegrid",
        region: "west",
        width: 700,
        split: true,
        style: {
            border: '1px solid #ddd'
        },
        frame: false
	},{
        xtype: "coursechkresult.classstudentgrid",
        region:"center"
    }]
});
