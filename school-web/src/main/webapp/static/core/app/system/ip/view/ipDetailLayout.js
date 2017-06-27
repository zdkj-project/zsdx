Ext.define("core.system.ip.view.ipDetailLayout",{
	extend:"core.base.view.BasePanel",
	alias : 'widget.ip.detaillayout',
	funCode:"ip_detail",
	funData: {
		action: comm.get('baseUrl') + "/SysIp", //请求Action
		whereSql: "", //表格查询条件
		orderSql: " order by orderIndex", //表格排序条件
		pkName: "uuid"
		// defaultObj: {
		// 	 actBegin: new Date(),
		// 	 signBeing:new Date()
		// }
	},
	items: [{
		xtype: "ip.deailform"
	}]
})