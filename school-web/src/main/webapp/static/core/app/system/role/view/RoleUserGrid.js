Ext.define("core.system.role.view.RoleUserGrid", {
	extend: "core.base.view.BaseGrid",
	alias: "widget.role.roleusergrid",
	dataUrl: comm.get('baseUrl') + "/sysrole/getRoleUser",
	model: factory.ModelFactory.getModelByName("com.zd.school.plartform.system.model.SysUser", "checked").modelName,
    al:false,
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
            ref: 'gridAddUser',
            funCode: 'girdFuntionBtn',
            iconCls: 'x-fa fa-plus-circle'
        },{
            xtype: 'button',
            text: '删除',
            ref: 'gridDelUser',
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
            align:'center',
            titleAlign:"center"
        },
        items:[{
            text: "主键",
            dataIndex: "uuid",
            hidden: true
        },{
            xtype: "rownumberer",
            flex:0,
            width: 50,
            text: '序号',
            align: 'center'
        }, {
			width:120,
            text: "用户名",
			dataIndex: "userName"
		},{
            width:120,
            text: "姓名",
			dataIndex: "xm"
		}, {
            width:80,
            text: "性别",
			dataIndex: "xbm",
			columnType: "basecombobox",
			ddCode: "XBM"
		}, {
            minWidth:160,
            flex:1,
			text: "部门",
			dataIndex: "deptName"
		}, {
            minWidth:160,
            flex:1,
            text: "岗位",
            dataIndex: "jobName"
        }, {
            text: "账户状态",
            dataIndex: "state",
            width:100,
            align:'left',
            renderer: function(value) {
                return (value == '0') ? '<font color=green>正常</font>' : '<font color=red>锁定</font>';
            }
        }]
	}
});