Ext.define("core.system.appupdate.view.DetailLayout",{
	extend:"core.base.view.BasePanel",
	alias : 'widget.appupdate.detaillayout',
	funCode:"appupdate_detail",
	funData: {
		action: comm.get('baseUrl') + "/SysAppinfo", //请求Action
		whereSql: "", //表格查询条件
		orderSql: " order by orderIndex", //表格排序条件
		pkName: "uuid"
		// defaultObj: {
		// 	 actBegin: new Date(),
		// 	 signBeing:new Date()
		// }
	},
	items: [{
		xtype: "appupdate.deailform"
	}]
})