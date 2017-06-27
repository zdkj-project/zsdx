Ext.define("core.system.role.view.RoleDetailLayout",{
	extend:"core.base.view.BasePanel",
	alias : 'widget.role.detaillayout',
	funCode:"role_detail",
	
	funData: {
		action: comm.get('baseUrl') + "/sysrole", //请求Action
		whereSql: "", //表格查询条件
		orderSql: " order by orderIndex", //表格排序条件
		pkName: "uuid",
		defaultObj: {
			issystem:"1",
			orderIndex:'1'
		}
	},
	items: [{
		xtype: "role.deailform"
	}]
})