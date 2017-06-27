Ext.define("core.systemset.dictionary.view.itemLayout", {
	extend: "core.base.view.BasePanel",
	alias: 'widget.dic.itemlayout',
	funCode: "dicItem_main",
	border: false,
	funData: {
		action: comm.get('baseUrl') + "/BaseDicitem", //请求Action
		whereSql: "", //表格查询条件
		orderSql: "", //表格排序条件
		pkName: "uuid",
		defaultObj: {
			orderIndex: 1
		}
	},
	items: [{
		xtype: "dic.itemform"
	}]
})