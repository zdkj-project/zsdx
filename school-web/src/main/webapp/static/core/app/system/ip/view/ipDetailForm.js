Ext.define("core.system.ip.view.ipDetailForm", {
    extend: "core.base.view.BaseForm",
    alias: "widget.ip.deailform",
    xtype: 'ipdetailform',
    layout: "form",
    autoHeight: true,
    frame: false,
    fieldDefaults: { // 统一设置表单字段默认属性
        labelSeparator: '：', // 分隔符
        msgTarget: 'qtip',
        labelAlign: "right",
    },
    items: [{
        xtype: "textfield",
        fieldLabel: "主键",
        name: "uuid",
        hidden: true
    }, {
        beforeLabelTextTpl: comm.get("required"),
        allowBlank: false,
        blankText: "ip名称不能为空",        
        xtype: "textfield",
        fieldLabel: "IP名称",
        name: "ipName",
        maxLength: 32,
        emptyText: '请输入IP名称(最大32个字符)'
    }, {
        beforeLabelTextTpl: comm.get("required"),
        allowBlank: false,
        blankText: "ip地址不能为空",            
        xtype: "textfield",
        fieldLabel: "IP地址",
        name: "ipUrl",
        maxLength: 16,
        emptyText: '请输入ip地址(例如192.168.1.1)'        
    }]

});