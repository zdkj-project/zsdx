Ext.define("core.system.user.view.selectUserLayout", {
	extend: "core.base.view.BasePanel",
	alias: 'widget.selectsysuser.mainlayout',
	funCode: "selectsysuser_main", //主窗体标识
	detCode: 'selectsysuser_detail', //详细窗口标识
	detLayout: 'selectsysuser.userlayout', //详细窗口别名
	border: false,
	//funData用来定义一些常规的参数
	funData: {
		action: comm.get('baseUrl') + "/sysuser", //请求controller
		whereSql: "", //表格查询条件
		orderSql: "", //表格排序条件
		filter: "[{'type':'numeric','comparison':'=','value':0,'field':'isDelete'},	  {'type':'string','comparison':'=','value':'1','field':'category'}, {'type': 'numeric','comparison': '=','value': '1','field': 'issystem'}]",
		pkName: "uuid", //主键
		//默认的初始值设置
		defaultObj: {
			sex: '1',
			category: '1',
			state: '1',
			orderIndex: 1,
			userPwd: '123456',
			issystem: '1'
		}
	},
	layout: 'border',
	//bodyPadding: 2,
	items: [{
			xtype: "selectsysuser.selectusergrid",
	        //width: 800,
	        region: "west",
	        width: comm.get("clientWidth") * 0.35,
	        //margin:'5',
		
	}, {
		xtype: "selectsysuser.isselectusergrid",
        region: "center",
        //margin:'5',
	}]
})