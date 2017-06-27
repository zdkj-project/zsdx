Ext.define("core.oa.terminal.view.detailForm", {
    extend: "core.base.view.BaseForm",
    alias: "widget.terminal.detailform",
    xtype: 'terminaldeailform',
    layout: "form", //从上往下布局
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
        xtype: "textfield",
        hidden: true
    }, {
        beforeLabelTextTpl: comm.get("required"),
        allowBlank: false,
        blankText: "终端起始号不能为空",
        fieldLabel: "终端起始号",
        name: "beforeNumber",
        xtype: "numberfield",
        emptyText: "请输入终端起始号",
        maxLength: 6,
        maxLengthText: "最多6个字符,汉字占2个字符"
    }, {
        beforeLabelTextTpl: comm.get("required"),
        allowBlank: false,
        blankText: "终端个数不能为空",
        fieldLabel: "终端个数",
        name: "termCount",
        xtype: "numberfield",
        emptyText: "请输入终端个数",
        maxLength: 6,
        maxLengthText: "最多6个字符,汉字占2个字符"
    }, {
        beforeLabelTextTpl: comm.get("required"),
        allowBlank: false,
        blankText: "终端类型不能为空",
        fieldLabel: "终端类型",
        name: "termType",
        xtype: "basecombobox",
        ddCode: "INFOTERTYPE",
        emptyText: "请选择终端类型",
        maxLength: 16,
        maxLengthText: "最多16个字符,汉字占2个字符"
    }, {
        fieldLabel: "规格",
        name: "termSpec",
        xtype: "textfield",
        emptyText: "请输入规格",
        maxLength: 32,
        maxLengthText: "最多32个字符,汉字占2个字符"
    }]
});