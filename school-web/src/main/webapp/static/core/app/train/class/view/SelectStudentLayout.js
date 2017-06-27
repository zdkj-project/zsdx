Ext.define("core.train.class.view.SelectStudentLayout", {
	extend: "core.base.view.BasePanel",
	alias: 'widget.class.selectstudent.mainlayout',
	funCode: "selectstudent_detail", 
	
	border: false,
	//funData用来定义一些常规的参数
	funData: {
		action: comm.get('baseUrl') + "/sysuser", //请求controller
		whereSql: "", //表格查询条件
		orderSql: "", //表格排序条件
		filter: "", //表格过滤条件
		pkName: "uuid", //主键
		//默认的初始值设置
		defaultObj: {			
		}
	},
	layout: 'border',
	items: [{
		xtype:'class.selectedstudentgrid',
	    width: 300,
		region: "east",
		margin:'5',		
	}, {
		xtype: "class.studentgrid",
		region: "center",
		margin:'5',		
	}]
})