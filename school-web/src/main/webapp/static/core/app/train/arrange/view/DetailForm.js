Ext.define("core.train.arrange.view.DetailForm", {
    extend: "core.base.view.BaseForm",
    alias: "widget.arrange.detailform",
    layout: "form", //从上往下布局
    autoHeight: true,
    frame: false,
    bodyPadding: '10 15 10 5',
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
    },{
        xtype: "container",
        layout: "column",
        labelAlign: "right",
        items:[{
            beforeLabelTextTpl: comm.get("required"),
            allowBlank: false,
            blankText: "姓名不能为空",        
            fieldLabel: "姓名",
            columnWidth: 0.5,
            name: "xm",
            xtype: "textfield",

            emptyText: "请输入姓名",
            maxLength:64,
            maxLengthText:"最多64个字符,汉字占2个字符",    
        },{ 
            beforeLabelTextTpl: comm.get("required"),
            allowBlank: false,
            blankText: "性别不能为空",        
            fieldLabel: "性别",
            columnWidth: 0.5,
            name: "xbm",
            xtype: "basecombobox",
            ddCode: "XBM",

            emptyText: "请输入性别",
            maxLength:1,
            maxLengthText:"最多1个字符,汉字占2个字符",
        }]
    }]
});