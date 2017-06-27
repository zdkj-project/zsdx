Ext.define("core.system.menu.view.MenuForm", {
	extend: "core.base.view.BaseForm",
	alias: "widget.menu.menuform",
	xtype: 'menudetailform',
	layout: "form", //从上往下布局
	autoHeight: true,
	frame: false,
	bodyPadding: '0 10 10 0',

	fieldDefaults: { // 统一设置表单字段默认属性
		labelSeparator: "：", // 分隔符
		msgTarget: "qtip",
		labelWidth: 100,
		labelAlign: "right"
	},
	items: [{
		fieldLabel: "主键",
		name: "uuid",
		hidden: true
	}, {
		xtype: "container",
		layout: "column", // 从左往右的布局
		items: [{
			columnWidth: 0.6,
			beforeLabelTextTpl: comm.get("required"),
			fieldLabel: "顺序号",
			xtype: "numberfield",
			name: "orderIndex",
			allowBlank: false,
			emptyText: "同级别的菜单的显示顺序",
			blankText: "顺序号不能为空"
		}, {
			columnWidth: 0.4,
			xtype: "label",
			margin:'5 0 0 5 ',
			html: "  (<font color=red,size=12>同级别的菜单的显示顺序,不能重复)</font>",
		}]
	}, {
		xtype: "container",
		layout: "column", // 从左往右的布局
		items: [{
			columnWidth: 0.5,
			beforeLabelTextTpl: comm.get("required"),
			xtype: "textfield",
			fieldLabel: "菜单名称",
			name: "nodeText",
			allowBlank: true,
			emptyText: '菜单名称',
			blankText: "菜单名称不能为空"
		}, {
			columnWidth: 0.5,
			beforeLabelTextTpl: comm.get("required"),
			xtype: "textfield",
			fieldLabel: "菜单编码",
			name: "menuCode",
			allowBlank: true,
			emptyText: '菜单编码',
			blankText: "菜单编码不能为空"
		}]
	}, {
		xtype: "container",
		layout: "column", // 从左往右的布局
		items: [{
			xtype: "textfield",
			fieldLabel: "上级菜单ID",
			name: "parentNode",
			hidden: true
		}, {
			columnWidth: 0.5,
			beforeLabelTextTpl: comm.get("required"),
			xtype: "basecombobox",

			fieldLabel: "菜单类型",
			name: "menuType",
			ddCode: "MENUTYPE",
			emptyText: '菜单类型',
			blankText: "菜单类型不能为空"
		}, {
			// columnWidth: 0.5,
			// beforeLabelTextTpl: comm.get("required"),
			// xtype: "textfield",
			// fieldLabel: "上级菜单",
			// name: "parentName",
			// readOnly: true
            columnWidth: .5,
            beforeLabelTextTpl: comm.get("required"),
            fieldLabel: "上级菜单",
            name: "parentName",
            allowBlank: false,
            xtype: "basetreefield",
            ddCode: "MENUTREE",
            rootId: "ROOT",
            configInfo: {
            	width:400,
            	height:500,
                multiSelect: false,
                fieldInfo: "parentName~parentNode,text~id",
                whereSql: " and isDelete='0' ",
                orderSql: " order by parentNode,orderIndex asc",
                url:comm.get('baseUrl') + "/BaseMenu/treelist",
            } //// 			 
		}]
	},{
		xtype: "container",
		layout: "column", // 从左往右的布局
		items: [{
			columnWidth: 1,
			beforeLabelTextTpl: "",
			//xtype: "filefield",
			xtype:"textfield",
			fieldLabel: "小图标",
			name: "smallIcon",
			allowBlank: true,
			emptyText: '小图标',
			blankText: ""
		}]
	}, {
		xtype: "container",
		layout: "column", // 从左往右的布局
		items: [{
			columnWidth: 1,
			beforeLabelTextTpl: "",
			//xtype: "filefield",
			xtype:"textfield",
			fieldLabel: "大图标",
			name: "bigIcon",
			allowBlank: true,
			emptyText: '大图标',
			blankText: ""
		}]
	}, {
		xtype: "container",
		layout: "column", // 从左往右的布局
		items: [{
			columnWidth: 1,
			beforeLabelTextTpl: "",
			xtype: "textfield",
			fieldLabel: "配置信息",
			name: "menuTarget",
			allowBlank: true,
			emptyText: '配置信息',
			blankText: ""
		}]
	}]
});