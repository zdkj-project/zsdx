Ext.define("core.system.user.view.UserGrid", {
	extend: "core.base.view.BaseGrid",
	alias: "widget.user.usergrid",
	dataUrl: comm.get('baseUrl') + "/sysuser/list",
	model: factory.ModelFactory.getModelByName("com.zd.school.plartform.system.model.SysUser", "checked").modelName,
	al: false,
	//排序字段及模式定义
	defSort: [{
		property: 'userNumb',
		direction: 'ASC'
	}, {
		property: 'state',
		direction: 'DESC'
	}],
	extParams: {
		whereSql: "",
		orderSql: "",
		//filter: "[{'type':'numeric','comparison':'=','value':0,'field':'isDelete'},{'type':'string','comparison':'=','value':'0','field':'deptId'}]"
	},
	title: "部门人员账户",

	panelTopBar:{
        xtype:'toolbar',
        items: [{
            xtype: 'button',
            text: '编辑',
            ref: 'gridEdit',
            funCode:'girdFuntionBtn',
            disabled:true,
            iconCls: 'x-fa fa-pencil-square'
        },{
            xtype: 'button',
            text: '锁定账户',
            ref: 'gridLock',
            funCode:'girdFuntionBtn',    
            iconCls: 'x-fa fa-lock'
        },{
            xtype: 'button',
            text: '解锁账户',
            ref: 'gridUnLock',
            funCode:'girdFuntionBtn',       
            iconCls: 'x-fa fa-unlock'
        },{
            xtype: 'button',
            text: '重置密码',
            ref: 'gridSetPwd',
            funCode:'girdFuntionBtn',         
            iconCls: 'x-fa fa-key'
        },{
            xtype: 'button',
            text: '同步数据',
            ref: 'sync',
            funCode:'girdFuntionBtn',         
            iconCls: 'x-fa fa-rss'
        },{
            xtype: 'button',
            text: '同步人员数据到UP',
            ref: 'syncToUP',
            funCode:'girdFuntionBtn',
            iconCls: 'x-fa fa-rss'
        },{
            xtype: 'button',
            text: '从UP同步发卡信息',
            ref: 'syncCardInfoFromUP',
            funCode:'girdFuntionBtn',
            iconCls: 'x-fa fa-rss'
        },'->',{
            xtype: 'tbtext', 
            html:'快速搜索：'
        },{
            xtype:'textfield',
            name:'userName',
            funCode:'girdFastSearchText', 
            emptyText: '用户名'
        },{
            xtype:'textfield',
            name:'xm',
            funCode:'girdFastSearchText', 
            emptyText: '姓名'
        },{
            xtype: 'button',
            funCode:'girdSearchBtn',    //指定此类按钮为girdSearchBtn类型
            ref: 'gridFastSearchBtn',   
            iconCls: 'x-fa fa-search',  
        }],
    },

    panelButtomBar:null,

	columns: { 
        defaults:{
            //flex:1,     //【若使用了 selType: "checkboxmodel"；则不要在这设定此属性了，否则多选框的宽度也会变大 】
            align:'center',
            titleAlign:"center"
        },
        items:[{
			text: "主键",
			dataIndex: "uuid",
			hidden: true
		}, {
			text: "用户名",
			dataIndex: "userName",
			flex:1,
		}, {
			text: "姓名",
			dataIndex: "xm",
			flex:1,
		}, {
			text: "性别",
			dataIndex: "xbm",
			columnType: "basecombobox",
			ddCode: "XBM",
			flex:1,
		}, {
			text: "编制",
			dataIndex: "zxxbzlb",
			ddCode: "ZXXBZLB",
			columnType: "basecombobox",
			flex:1,
		}/*, {
			text: "岗位",
			dataIndex: "jobName"
		}*/, {
			text: "账户状态",
			dataIndex: "state",
			flex:1,
			renderer: function(value) {
				return (value == '0') ? '<font color=green>正常</font>' : '<font color=red>锁定</font>';
			}
		}]
	}
});