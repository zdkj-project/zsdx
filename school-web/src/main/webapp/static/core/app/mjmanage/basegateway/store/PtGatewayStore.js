Ext.define("core.mjmanage.basegateway.store.PtGatewayStore", {
	extend: "Ext.data.Store",
	alias: 'store.mjmanage.basegateway.ptgatewaystore',
	model: factory.ModelFactory.getModelByName("com.zd.school.build.define.model.SysFrontServer", "checked").modelName,
	proxy: {
		type: "ajax",
		url: comm.get('baseUrl') + "/SysFrontServer/list", //对应后台controller路径or方法
		extParams: {
		},
		reader: {
			type: "json",
			rootProperty: "rows",
			totalProperty: 'totalCount'
		},
		writer: {
			type: "json"
		}
	},
	autoLoad: true
})