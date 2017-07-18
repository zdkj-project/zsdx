Ext.define("core.system.user.view.UserLayout", {
	extend: "core.base.view.BasePanel",
	alias: 'widget.user.userlayout',
	funCode: "user_detail",
	funData: {
		action: comm.get('baseUrl') + "/sysuser", //请求Action
		whereSql: "", //表格查询条件
		orderSql: " order by orderIndex", //表格排序条件
		pkName: "uuid",
		//默认的初始值设置
		defaultObj: {
			sex: '1',
			category: '1',
			state: '1',
			userPwd: '123456',
			orderIndex: 1,
			issystem: '1'
		}
	},
	/*设置最小宽度，并且自动滚动*/
    minWidth: 1200,
    scrollable: true,

	items: [{
		xtype: "user.userform"
	}]
})