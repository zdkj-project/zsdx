Ext.define("core.train.courseeval.view.EvalUrlForm", {
    extend: "core.base.view.BaseForm",
    alias: "widget.courseeval.evalurlform",
    xtype: 'evalurlform',
    layout: "form", //从上往下布局
    autoHeight: true,
    frame: false,
    //bodyPadding: '10 15 10 5',
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
        xtype: "container",
        layout: "column",
        labelAlign: "right",
        items: [{
            beforeLabelTextTpl: '',
            allowBlank: false,
            blankText: "评价地址",
            fieldLabel: "评价地址",
            columnWidth: 1,
            height:50,
            name: "evalUrl",
            xtype: "textfield",
            readOnly:true
        }]
    }]
});

