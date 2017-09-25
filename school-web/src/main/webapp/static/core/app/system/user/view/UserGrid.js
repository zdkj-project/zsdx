Ext.define("core.system.user.view.UserGrid", {
	extend: "core.base.view.BaseGrid",
	alias: "widget.user.usergrid",
	dataUrl: comm.get('baseUrl') + "/sysuser/list",
	model: factory.ModelFactory.getModelByName("com.zd.school.plartform.system.model.SysUser", "checked").modelName,
	al: true,
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
        items: [/*{
            xtype: 'button',
            text: '编辑',
            ref: 'gridEdit',
            funCode:'girdFuntionBtn',
            disabled:true,
            iconCls: 'x-fa fa-pencil-square'
        },*/{
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
            text: '同步发卡信息',
            ref: 'syncCardInfoFromUP',
            funCode:'girdFuntionBtn',
            iconCls: 'x-fa fa-rss'
        },{
            xtype: 'button',
            text: '导出',
            ref: 'gridExport',
            funCode:'girdFuntionBtn',
            disabled:false,
            iconCls: 'x-fa fa-file'
        },'->',{
            xtype: 'tbtext', 
            html:'快速搜索：'
        },{
            xtype:'textfield',
            width:100,
            name:'userName',
            funCode:'girdFastSearchText', 
            emptyText: '用户名'
        },{
            xtype:'textfield',
            width:100,
            name:'xm',
            funCode:'girdFastSearchText', 
            emptyText: '姓名'
        },{
            xtype: 'button',
            funCode:'girdSearchBtn',    //指定此类按钮为girdSearchBtn类型
            ref: 'gridFastSearchBtn',   
            iconCls: 'x-fa fa-search'
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
			width:150,
            align:'left'
		}, {
			text: "姓名",
			dataIndex: "xm",
			width:100,
            align:'left'
		}, {
			text: "性别",
			dataIndex: "xbm",
			columnType: "basecombobox",
			ddCode: "XBM",
			width:80,
            align:'left'
		}, {
			text: "编制",
			dataIndex: "zxxbzlb",
			ddCode: "ZXXBZLB",
			columnType: "basecombobox",
			minWidth:100,
            flex:1,
            align:'left'
		}/*, {
			text: "岗位",
			dataIndex: "jobName"
		}*/, {
			text: "账户状态",
			dataIndex: "state",
			width:100,
            align:'left',
			renderer: function(value) {
				return (value == '0') ? '<font color=green>正常</font>' : '<font color=red>锁定</font>';
			}
		},{
            width:120,
            text: "卡片编号",
            dataIndex: "upCardId",
            align:'left'       
        },{
            width:120,
            text: "卡印刷编号",
            dataIndex: "cardPrintId",
            align:'left'       
        },{
            width:100,
            text: "发卡状态",
            dataIndex: "useState",
            align:'left',
            renderer: function(value, metaData) {          
                if(value==0||value==3||value==null)
                    return "<span style='color:red'>未发卡</span>";
                else if(value==1)
                    return "<span style='color:green'>已发卡</span>";            
                else if(value==2){
                	return "<span style='color:black'>挂失</span>";
                }else {
                	return "<span style='color:#FFAC00'>退卡</span>"; 
                }
            }        
        },{
            xtype:'actiontextcolumn',
            text: "操作",
            width:150,
            fixed:true,
            items: [{
                text:'部门岗位',
                style:'font-size:12px;',
                tooltip: '部门岗位',
                ref: 'gridDeptJob',
                handler: function(view, rowIndex, colIndex, item) {
                    var rec = view.getStore().getAt(rowIndex);
                    this.fireEvent('gridDeptJobClick', {
                        view:view.grid,
                        record: rec,
                        cmd:"deptJob"
                    });
                }
            },{
                text:'角色管理',
                style:'font-size:12px;',
                tooltip: '角色管理',
                ref: 'gridRole',
                handler: function(view, rowIndex, colIndex, item) {
                    var rec = view.getStore().getAt(rowIndex);
                    this.fireEvent('gridUserRoleClick', {
                        view:view.grid,
                        record: rec,
                        cmd:"userRole"
                    });
                }
            },{
                text:'编辑',  
                style:'font-size:12px;',         
                tooltip: '编辑',
                ref: 'gridEdit',
                handler: function(view, rowIndex, colIndex, item) {                 
                    var rec = view.getStore().getAt(rowIndex);
                    this.fireEvent('editClick_Tab', {
                        view:view.grid,
                        record: rec,
                        cmd:"edit"
                    });
                }
            },{
                text:'详情',  
                style:'font-size:12px;',
                tooltip: '详情',
                ref: 'gridDetail',
                handler: function(view, rowIndex, colIndex, item) {
                    var rec = view.getStore().getAt(rowIndex);
                    this.fireEvent('detailClick_Tab', {
                        view:view.grid,
                        record: rec
                    });
                }
            }]
        }]
	}
});