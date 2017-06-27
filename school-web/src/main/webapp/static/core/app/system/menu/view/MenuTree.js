Ext.define("core.system.menu.view.MenuTree", {
	extend: "core.base.view.BaseTreeGrid",
	alias: "widget.menu.menutree",
	dataUrl: comm.get('baseUrl') + "/BaseMenu/treelist",
	model: factory.ModelFactory.getModelByName("com.zd.school.plartform.system.model.SysMenuTree", "checked").modelName,
	al: true,
	extParams: {
		whereSql: " and isDelete='0' ",
		orderSql: " order by parentNode,isHidden,orderIndex asc"
	},
	tbar: [{
		xtype: 'button',
		text: '添加下级',
		ref: 'gridAdd',
		iconCls: 'x-fa fa-plus-square',
		disabled: false
	}, {
		xtype: 'button',
		text: '添加同级',
		ref: 'gridAddBrother',
		iconCls: 'x-fa fa-plus-square-o',
		disabled: false
	}, {
		xtype: 'button',
		text: '修改',
		ref: 'gridEdit',
		iconCls: 'x-fa fa-pencil-square',
		disabled: false  
	}, {
		xtype: 'button',
		text: '解锁',
		ref: 'gridUnLock',
		iconCls: 'x-fa fa-unlock',
		disabled: false
	}, {
		xtype: 'button',
		text: '锁定',
		ref: 'gridLock',
		iconCls: 'x-fa fa-lock',
		disabled: false
	}, {
		xtype: 'button',
		text: '刷新',
		ref: 'gridRefresh',
		iconCls: 'x-fa fa-refresh'
	}],
	columns:   {
        defaults:{
            //flex:1,     //不设定此属性了，否则多选框的宽度也会变大
            align:'center',
            titleAlign:"center"
        },
	    items:[{
			header: '菜单名称',
			dataIndex: 'text',
			xtype: 'treecolumn',	
			flex:3,	
		}, {
			header: '排序号',
			dataIndex: 'orderIndex',	
			width: 70	
		}, {
			header: '菜单类型',
			dataIndex: 'menuType',
			columnType: "basecombobox", //列类型
			ddCode: "MENUTYPE", //字典代码
			flex:1,		

		}, {
			header: '菜单编码',
			dataIndex: 'menuCode',	
			flex:1,
		}, {
			header: '图标',
			dataIndex: 'bigIcon',
			renderer: function(value) {
				return "<img src=\"" + value + "\" width=16 hight=16/>";
			},
			width: 70
		}, {
			header: '状态',
			dataIndex: 'isHidden',
			renderer: function(value) {
				return value == 0 ? "<font color='green'>正常</font>" : "<font color='red'>锁定</font>";
			},
			width: 70
		}, {
			header: '菜单URL',
			dataIndex: 'menuTarget',
			flex:3,
		}]
	}
})