Ext.define("core.oa.meeting.meetinginfo.view.DetailLayout", {
	extend:"core.base.view.BasePanel",
	alias: "widget.meetinginfo.detaillayout",
	funCode: "meetinginfo_detail",
	funData: {
		action: comm.get("baseUrl") + "/OaMeeting", //请求Action
		whereSql: "", //表格查询条件
		orderSql: "", //表格排序条件
		pkName: "uuid",
		defaultObj: {}
	},
    /*关联此视图控制器*/
	controller: 'meetinginfo.detailController',
	items: [{
		xtype: "meetinginfo.detailform"
	}]
});
