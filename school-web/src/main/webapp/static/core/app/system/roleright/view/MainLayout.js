Ext.define("core.system.roleright.view.MainLayout", {
	extend: "core.base.view.BasePanel",
	alias: 'widget.roleright.mainlayout',

	requires: [    
        'core.system.roleright.controller.RoleRightController',
        "core.system.roleright.view.RoleGrid",
        'core.system.roleright.view.RoleRightGrid', 
        "core.system.roleright.view.SelectMenuGrid",
        'core.system.roleright.view.SelectMenuLayout',
    ],
    controller: 'roleright.rolerightController',


	funCode: "roleright_main",    //主窗体标识
	detCode: 'roleright_selectmenu',  //详细窗口标识
	detLayout: 'roleright.detaillayout', //详细窗口别名

	/*标注这个视图控制器的别名，以此提供给window处使用*/
    otherController:'roleright.otherController',

	border: false,
	//funData用来定义一些常规的参数
	funData: {
		action: comm.get('baseUrl') + "/Baseroleright", //请求controller
		whereSql: "", //表格查询条件
		orderSql: "", //表格排序条件
		filter:"",    //表格过滤条件
		pkName: "uuid", //主键
		//默认的初始值设置
		defaultObj: {
			orderIndex: 1,
			standYear: 2016,
			standPc:1,
			isuse:0
		}
	},
	layout: 'border',
	//bodyPadding: 2,
	items: [{
		xtype: "roleright.rolegrid",
		region: "west",
		//border: true,
		style:{
            border: '1px solid #ddd'
        },
		margin:'0 5 0 0',
		width:460
		//width: comm.get("clientWidth") * 0.35
	}, {
		xtype: "roleright.rolgerightgrid",
		region: "center",
		//border: true,
		style:{
            border: '1px solid #ddd'
        },
		flex:1
	}]
})