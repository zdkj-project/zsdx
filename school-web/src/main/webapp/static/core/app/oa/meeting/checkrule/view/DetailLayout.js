Ext.define("core.oa.meeting.checkrule.view.DetailLayout", {
	extend:"core.base.view.BasePanel",
	alias: "widget.checkrule.detaillayout",
	funCode: "checkrule_detail",
	funData: {
		action: comm.get("baseUrl") + "/OaMeetingcheckrule", //请求Action
		whereSql: "", //表格查询条件
		orderSql: "", //表格排序条件
		pkName: "uuid",
		defaultObj: {
		}
	},
    /*关联此视图控制器*/
	controller: 'checkrule.detailController',
	items: [{
		xtype: "checkrule.detailform"
	}]
});
