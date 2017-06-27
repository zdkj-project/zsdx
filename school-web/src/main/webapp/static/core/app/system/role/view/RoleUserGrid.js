Ext.define("core.system.role.view.RoleUserGrid", {
	extend: "core.base.view.BaseGrid",
	alias: "widget.role.roleusergrid",
	dataUrl: comm.get('baseUrl') + "/teacher/roleteacherlist",
	model: factory.ModelFactory.getModelByName("com.zd.school.plartform.system.model.SysUser", "checked").modelName,
	al: false,
    selModel:null,	
	noPagging: true,
	extParams: {
		whereSql: "",
		orderSql: ""
	},
	tbar: null,
	panelTopBar:null,
	panelBottomBar:null,

	columns: {        
        defaults:{
            flex:1,    	//【若使用了 selType: "checkboxmodel"；则不要在这设定此属性了，否则多选框的宽度也会变大 】
            align:'center',
            titleAlign:"center"
        },
        items:[{
            xtype: "rownumberer",
            flex:0,
            width: 60,
            text: '序号',
            align: 'center'
        },{
			text: "主键",
			dataIndex: "uuid",
			hidden: true
		}, {
			text: "编号",
			dataIndex: "userNumb"
		}, {
			text: "姓名",
			dataIndex: "xm"
		}, {
			text: "性别",
			dataIndex: "xbm",
			columnType: "basecombobox",
			ddCode: "XBM"
		}/*, {
			text: "岗位",
			dataIndex: "jobName"
		}*/]
	}
});