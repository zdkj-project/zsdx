Ext.define("core.eduresources.room.view.areaForm", {
    extend: "core.base.view.BaseForm",
	alias: "widget.room.areaform",
	xtype: 'areadetailfrom',
	layout: "form",
	autoHeight: true,
	frame: false,
	fieldDefaults: { // 统一设置表单字段默认属性
		labelSeparator: '：', // 分隔符
		msgTarget: 'qtip',
		labelAlign: "right",
	},
	items: [{
		fieldLabel: '主键',
		name: "uuid",
		hidden: true
	}, {
		fieldLabel: '区域类型',
		name: "areaType",
		hidden: true
	}, {
		fieldLabel: '上级区域类型',
		name: "parentType",
		hidden: true
	},{
        xtype: "container",
        layout: "column", // 从左往右的布局
        items: [{
            columnWidth: 0.6,
            beforeLabelTextTpl: comm.get("required"),
            fieldLabel: "顺序号",
            xtype: "numberfield",
            name: "orderIndex",
            allowBlank: false,
            emptyText: "同级别的区域的显示顺序",
            blankText: "顺序号不能为空"
        }, {
            columnWidth: 0.4,
            xtype: "label",
            html: "  (<font color=red,size=12>同级别的区域的显示顺序,不能重复)</font>",
        }]
    },{
        xtype: "container",
        layout: "column", // 从左往右的布局
        items: [{
            xtype: "textfield",
            fieldLabel: "上级区域ID",
            name: "parentNode",
            hidden: true
        }, {
            columnWidth: 1,
            beforeLabelTextTpl: comm.get("required"),
            xtype: "textfield",
            fieldLabel: "上级区域",
            name: "parentName",
            readOnly: true
        }]
    }, {
        xtype: "container",
        layout: "column", // 从左往右的布局
        items: [{
            columnWidth: 1,
            beforeLabelTextTpl: comm.get("required"),
            xtype: "textfield",
            fieldLabel: "区域名称",
            name: "nodeText",
            allowBlank: false,
            emptyText: '区域名称',
            blankText: "区域名称不能为空"
        }]
    }, {
        xtype: "container",
        layout: "column", // 从左往右的布局
        items: [{
            columnWidth: 1,
            beforeLabelTextTpl: "",
            xtype: "textfield",
            fieldLabel: "区域编号",
            name: "areaCode",
            allowBlank: true,
            emptyText: '区域编号',
            blankText: "区域编号不能为空"
        }]
    }, {
        xtype: "container",
        layout: "column", // 从左往右的布局
        items: [{
            columnWidth: 01,
            beforeLabelTextTpl: "",
            xtype: "textarea",
            fieldLabel: "区域说明",
            name: "areaDesc",
            height: 100,
            allowBlank: true,
            emptyText: '区域说明',
            blankText: "区域说明不能为空"
        }]
    }]
});