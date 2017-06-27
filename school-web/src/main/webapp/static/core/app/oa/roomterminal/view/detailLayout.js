Ext.define("core.oa.roomterminal.view.detailLayout", {
    extend: "core.base.view.BasePanel",
	alias: "widget.roomterminal.detaillayout",
	funCode: "roomterminal_detail",
	funData: {
		action: comm.get("baseUrl") + "/OaInfoterm", //请求Action
		whereSql: "", //表格查询条件
		orderSql: "", //表格排序条件
		pkName: "uuid",
		defaultObj: {}
	},
	items: [{
        xtype: "roomterminal.selectGrid",
    }]
});