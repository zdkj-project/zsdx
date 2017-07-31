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
    /*关联此视图控制器*/
    controller: 'role.roleController',
    otherController: 'role.OtherController',
	/*设置最小宽度，并且自动滚动*/
    minWidth:1000,
    scrollable:true,
    
	items: [{
		xtype: "role.deailform"
	}]
})