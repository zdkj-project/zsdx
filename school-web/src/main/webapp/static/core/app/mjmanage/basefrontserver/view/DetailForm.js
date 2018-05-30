var combostore = new Ext.data.ArrayStore({
    fields: ['id', 'frontServerStatus'],
    data: [
        [0, '禁用'],
        [1, '启用']
    ]
});
Ext.define("core.mjmanage.basefrontserver.view.DetailForm", {
    extend: "core.base.view.BaseForm",
    alias: "widget.mjmanage.basefrontserver.detailform",
    fieldDefaults: { // 统一设置表单字段默认属性
        labelSeparator: "：", // 分隔符
        msgTarget: "qtip",
        labelAlign : 'right',
        labelWidth: 120,
    },
    items: [{
        xtype: "textfield",
        fieldLabel: "主键",
        name: "uuid",
        hidden: true
    }, {
        xtype: "container",
        layout: "column", // 从左往右的布局
        items: [{
            beforeLabelTextTpl: comm.get('required'),
            xtype: "textfield",
            fieldLabel: "名称",
            name: "frontServerName",
            allowBlank: false,
            emptyText: '名称',
            blankText: "名称不能为空",
            maxLength: 200,
            columnWidth:0.5
        }, {
            beforeLabelTextTpl: comm.get('required'),
            xtype: "textfield",
            fieldLabel: "服务IP",
            name: "frontServerIp",
            allowBlank: false,
            emptyText: '服务IP',
            blankText: "服务IP不能为空",
            allowBlank: false,
            regex: /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/,
            regexText: 'IP合法值为：0.0.0.0~255.255.255.255',
            columnWidth:0.5
        }]
    }, { 
        xtype: "container",
        layout: "column", // 从左往右的布局
        items: [{
            beforeLabelTextTpl: comm.get('required'),
            xtype: "numberfield",
            fieldLabel: "服务端口",
            name: "frontServerPort",
            allowBlank: false,
            emptyText: '服务端口',
            blankText: "服务端口不能为空",
            columnWidth:0.5,
            maxValue:2147483647
        }, {
            fieldLabel: "请求的URL",
            name: "frontServerUrl",
            xtype: "textfield",
            maxLength: 100,
            columnWidth:0.5
        }]
    }, {
        xtype: "container",
        layout: "column", // 从左往右的布局
        items: [{
            xtype: "combobox",
            store: combostore,
            fieldLabel: "状态",
            displayField: 'frontServerStatus',
            valueField: 'id',
            name: "frontServerStatus",
            value: 0,
            triggerAction: 'all',
            emptyText: '请选择...',
            blankText: '请选择状态',
            editable: false,
            mode: 'local',
            columnWidth:0.5
        }]
    }, {
            xtype: "textarea",
            fieldLabel: "备注",
            maxLength: 500,
            name: "notes",
            
     }]
});