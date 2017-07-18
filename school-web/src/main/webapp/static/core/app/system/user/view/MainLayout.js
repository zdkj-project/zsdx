Ext.define("core.system.user.view.MainLayout", {
	extend: "core.base.view.BasePanel",
	alias: 'widget.user.mainlayout',

	requires: [
		"core.system.user.controller.DeptUserController",
        "core.system.user.view.MainLayout",
        "core.system.user.view.DeptTree",
        "core.system.user.view.UserLayout",
        "core.system.user.view.UserGrid",
        "core.system.user.view.UserForm",
        "core.system.user.view.userRoleGrid",
        "core.system.user.view.selectRoleGrid",
        "core.system.user.view.selectRoleLayout",
        "core.system.user.view.isSelectRoleGrid",
        "core.system.user.view.selectUserLayout",
        "core.system.user.view.selectUserGrid",
        "core.system.user.view.isSelectUserGrid"
    ],
    controller:'user.deptUserController',
    
	funCode: "user_main", //主窗体标识
	detCode: 'user_detail', //详细窗口标识
	detLayout: 'user.userlayout', //详细窗口别名
	border: false,

	/*标注这个视图控制器的别名，以此提供给window处使用*/
    otherController:'user.otherController',
    
	//funData用来定义一些常规的参数
	funData: {
		action: comm.get('baseUrl') + "/sysuser", //请求controller
		whereSql: "", //表格查询条件
		orderSql: "", //表格排序条件
		filter: "", //表格过滤条件
		pkName: "uuid", //主键
		//默认的初始值设置
		defaultObj: {
			sex: '1',
			category: '1',
			state: '1',
			orderIndex: 1,
			userPwd: '123456',
			issystem: '1'
		},
		tabConfig: {         //zzk：2017-6-1加入，用于对tab操作提供基本配置数据
            addTitle: '添加用户',
            editTitle: '编辑用户',
            detailTitle: '用户详情'
        }
	},
	layout: 'border',

	/*设置最小宽度，并且自动滚动*/
    minWidth: 1300,
    scrollable: true,


	bodyPadding: '2 0 0 0',
	items: [{
		xtype: "user.depttree",
		region: "west",
    	//border:true,
		width:250,
		split : true,
        collapsible:true,    
        // style:{
        //     border: '1px solid #ddd'
        // },    
	}, 
	{
		xtype: "user.usergrid",
		region: "center",
		//flex:1,
		title:null,
		// style:{
  //           border: '1px solid #ddd'
  //       },
    	//margin:'0 5 0 5',
	}/*,{
		xtype: "user.userrolegrid",
		region: "east",
    	//border:true,
		width: 300,
		split : true,
        collapsible:true,
        //style:{
        //    border: '1px solid #ddd'
        //},
	}*/]
})