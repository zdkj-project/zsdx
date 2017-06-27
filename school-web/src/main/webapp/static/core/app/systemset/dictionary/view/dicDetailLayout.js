Ext.define("core.systemset.dictionary.view.dicDetailLayout", {
	extend: "core.base.view.BasePanel",
	alias: 'widget.dic.detaillayout',
	funCode: "dic_detail",
	funData: {
		action: comm.get('baseUrl') + "/BaseDic", //请求Action
		whereSql: "", //表格查询条件
		orderSql: "", //表格排序条件
		pkName: "uuid",
		defaultObj: {
			orderIndex: 1,
			dicType:"LIST"
		}
	},
	layout: 'fit',
	bodyPadding: 2,
	items: [{
		xtype: "dic.dicform"
	}]
})