Ext.define("core.good.activity.view.DetailLayout",{
	extend:"core.base.view.BasePanel",
	alias : 'widget.activity.detaillayout',
	funCode:"activity_detail",
	funData: {
		action: comm.get('baseUrl') + "/activity", //请求Action
		whereSql: "", //表格查询条件
		orderSql: " order by orderIndex", //表格排序条件
		pkName: "uuid"
		// defaultObj: {
		// 	 actBegin: new Date(),
		// 	 signBeing:new Date()
		// }
	},

	/*关联此视图控制器*/
	controller: 'activity.detailController',

	items: [{
		xtype: "activity.deailform"
	}]
})