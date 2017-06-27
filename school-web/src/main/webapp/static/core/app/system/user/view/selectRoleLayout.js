Ext.define("core.system.user.view.selectRoleLayout", {
	extend: "core.base.view.BasePanel",
	alias: 'widget.user.selectrolelayout',
	funCode: "user_selectrolemain",
	layout: 'border',
	bodyPadding: 5,	
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
	items: [{
		xtype: "user.selectrolegrid",
		region: "west",
		margin:'0 5 0 0',
		flex:2,
		border:true
	},{
		xtype: "user.isselectrolegrid",
		region: "center",
		flex:1,
		border:true
	}]
})