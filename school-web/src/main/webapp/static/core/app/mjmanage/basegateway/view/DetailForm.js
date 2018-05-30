var combostore = new Ext.data.ArrayStore({
    fields: ['id', 'gatewayStatus'],
    data: [
        [0, '禁用'],
        [1, '启用']
    ]
});
Ext.define("core.mjmanage.basegateway.view.DetailForm", {
    extend: "core.base.view.BaseForm",
    alias: "widget.mjmanage.basegateway.detailform",
    autoHeight: true,
    fieldDefaults: { // 统一设置表单字段默认属性
        labelSeparator: "：", // 分隔符
        msgTarget: "qtip",
        labelWidth: 120,
        labelAlign : 'right',
    },
    items: [{
        xtype: "textfield",
        fieldLabel: "主键",
        name: "uuid",
        hidden: true
    }, /*{
        xtype: "textfield",
        fieldLabel: "前置主键",
        name: "frontserverId",
        hidden: true
    }, */{
        xtype: "container",
        layout: "column", // 从左往右的布局
        items: [{
            columnWidth:0.5,
            xtype: 'numberfield',
            fieldLabel: "机号",
            name: "gatewayNo",
           // readOnly: true
        }, {
            beforeLabelTextTpl: comm.get('required'),
            xtype: "textfield",
            allowDecimals: true,
            fieldLabel: "网关名称",
            name: "gatewayName",
            allowBlank: false,
            blankText: "网关名称为空",
            columnWidth:0.5,
        }]
    }, {
        xtype: "container",
        layout: "column", // 从左往右的布局
        items: [{
            columnWidth:0.5,
            beforeLabelTextTpl: comm.get("required"),
            xtype: "combobox",
            editable:false,
            store: {
                type: 'mjmanage.basegateway.ptgatewaystore',
                //.......这里可以写传入这个store的其他参数
                //model:'core.good.signup.model.SignupGridModel',
            },
            fieldLabel: "综合前置",
            name: "frontserverId",
            displayField: 'frontServerName',
            valueField: 'uuid',
            allowBlank: false,
            blankText: "综合前置为空",
           
        }, {
            columnWidth:0.5,
            xtype: "combobox",
            store: combostore,
            fieldLabel: "状态",
            displayField: 'gatewayStatus',
            valueField: 'id',
            name: "gatewayStatus",
            value: "0",
            triggerAction: 'all',
            emptyText: '请选择...',
            blankText: '请选择状态',
            editable: false,
            mode: 'local'
        }]
    }, {
            fieldLabel: "备注",
            name: "notes",
            xtype: "textarea",
            maxLength: 500,
        }]
});