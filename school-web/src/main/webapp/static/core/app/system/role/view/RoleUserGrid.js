Ext.define("core.system.role.view.RoleUserGrid", {
	extend: "core.base.view.BaseGrid",
	alias: "widget.role.roleusergrid",
	dataUrl: comm.get('baseUrl') + "/teacher/roleteacherlist",
	al:false,
//	dataUrl: comm.get('baseUrl') + "/sysuser/list",
	model: factory.ModelFactory.getModelByName("com.zd.school.plartform.system.model.SysUser", "checked").modelName,
    selModel:null,	
	noPagging: true,
	extParams: {
		whereSql: "",
		orderSql: ""
	},
	tbar: [],
    panelTopBar:{
        xtype:'toolbar',
        items: [{
            xtype: 'button',
            text: '添加',
            ref: 'gridAdd',
            funCode: 'girdFuntionBtn',
            iconCls: 'x-fa fa-plus-circle'
        },{
            xtype: 'button',
            text: '删除',
            ref: 'gridDelete',
            funCode: 'girdFuntionBtn',
            iconCls: 'x-fa fa-clipboard'
        },'->',{
            xtype: 'tbtext',
            html:'快速搜索：'
        },{
            xtype:'textfield',
            name:'courseName',
            funCode:'girdFastSearchText',
            isNotForm:true,   //由于文本框重写了baseform下面的funcode值，所以使用这个属性，防止重写这里设定的fundcode值。
            emptyText: '请输入姓名'
        },{
            xtype: 'button',
            funCode:'girdSearchBtn',    //指定此类按钮为girdSearchBtn类型
            ref: 'gridFastSearchBtn',
            iconCls: 'x-fa fa-search'
        }]
    },
	panelBottomBar:false,

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
			text: "用户名",
			dataIndex: "userName"
		},{
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