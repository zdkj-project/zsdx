Ext.define("core.oa.terminal.view.detailLayout", {
    extend: "core.base.view.BasePanel",
	alias: "widget.terminal.detaillayout",
	funCode: "terminal_detail",
	funData: {
		action: comm.get("baseUrl") + "/OaInfoterm", //请求Action
		whereSql: "", //表格查询条件
		orderSql: "", //表格排序条件
		pkName: "uuid",
		defaultObj: {}
	},
	items: [{
		xtype: "terminal.detailform"
	}]
});