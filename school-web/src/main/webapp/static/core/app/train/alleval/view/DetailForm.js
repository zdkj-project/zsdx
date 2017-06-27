Ext.define("core.train.alleval.view.DetailForm", {
    extend: "core.base.view.BaseForm",
    alias: "widget.alleval.detailform",
    xtype: 'allevaldeailform',
    layout: "form", //从上往下布局
    autoHeight: true,
    frame: false,
    //bodyPadding: '10 15 10 5',
    fieldDefaults: { // 统一设置表单字段默认属性
        labelSeparator: "：", // 分隔符
        msgTarget: "qtip",
        labelWidth: 120,
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
            beforeLabelTextTpl: comm.get("required"),
            allowBlank: false,
            blankText: "指标名称不能为空",
            fieldLabel: "指标名称",
            columnWidth: 1,
            name: "allevalName",
            xtype: "textfield",
            emptyText: "请输入指标名称",
            maxLength: 64,
            maxLengthText: "最多64个字符,汉字占2个字符"
        }]
    }, {
        xtype: "container",
        layout: "column",
        labelAlign: "right",
        items: [{
            beforeLabelTextTpl: comm.get("required"),
            allowBlank: false,
            fieldLabel: "评价标准",
            columnWidth: 1,
            name: "allevalStand",
            xtype: "textfield",
            emptyText: "请输入评价标准",
            maxLength: 1024,
            maxLengthText: "最多36个字符,汉字占2个字符",
        }]
    }, {
        xtype: "container",
        layout: "column",
        labelAlign: "right",
        items: [{
            fieldLabel: "评估对象",
            columnWidth: 1,
            name: "position",
            xtype: "textfield",
            emptyText: "请输入评估对象",
            maxLength: 128,
            maxLengthText: "最多128个字符,汉字占2个字符"
        }]
    }]
});
               
