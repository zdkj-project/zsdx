Ext.define("core.cashier.dishes.view.DetailForm", {
    extend: "core.base.view.BaseForm",
    alias: "widget.dishes.detailform",
    xtype: 'dishesdeailform',
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
        beforeLabelTextTpl: comm.get("required"),
        allowBlank: false,
        blankText: "菜品名称不能为空",
        fieldLabel: "菜品名称",
        columnWidth: 1,
        name: "dishesName",
        xtype: "textfield",
        emptyText: "请输入菜品名称",
        maxLength: 64,
        maxLengthText: "最多64个字符,汉字占2个字符"
    }, {
        beforeLabelTextTpl: comm.get("required"),
        allowBlank: false,
        blankText: "菜品类别不能为空",
        fieldLabel: "菜品类别",
        columnWidth: 1,
        name: "dishesType",
        xtype: "basecombobox",
        ddCode: "DISHESTYPE",
        emptyText: "请选择菜品类别",
        maxLength: 4,
        maxLengthText: "最多1个字符,汉字占2个字符",
    },{
        beforeLabelTextTpl: comm.get("required"),
        allowBlank: false,
        blankText: "菜品单价不能为空",
        fieldLabel: "菜品单价",
        columnWidth:1,
        name: "dishesPrice",
        xtype: "numberfield",
        emptyText: "请输入菜品单价",
        
    }, {
        beforeLabelTextTpl: comm.get("required"),
        allowBlank: true,
        fieldLabel: "身份证件号",
        columnWidth: 1,
        name: "dishesExplain",
        xtype: "textarea",
        maxLength: 64,
        maxLengthText: "最多64个字符,汉字占2个字符",
    }]
});
               
