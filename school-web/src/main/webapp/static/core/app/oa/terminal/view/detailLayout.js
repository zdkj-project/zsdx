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
	 /*设置最小宽度，并且自动滚动*/
    minWidth:1200,
    scrollable:true,
	items: [{
		xtype: "terminal.detailform"
	}]
});