Ext.define("core.good.news.view.DetailLayout",{
	extend:"core.base.view.BasePanel",
	alias : 'widget.news.detaillayout',
	funCode:"news_detail",
	funData: {
		action: comm.get('baseUrl') + "/news", //请求Action
		whereSql: "", //表格查询条件
		orderSql: " order by orderIndex", //表格排序条件
		pkName: "uuid"
		// defaultObj: {
		// 	 actBegin: new Date(),
		// 	 signBeing:new Date()
		// }
	},
	items: [{
		xtype: "news.deailform"
	}]
})