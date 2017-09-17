Ext.define("core.cashier.mealtime.view.DetailForm", {
    extend: "core.base.view.BaseForm",
    alias: "widget.mealtime.detailform",
    xtype: 'mealtimedeailform',
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
    },{
        fieldLabel: "快餐类别",
        name: "mealType",
        xtype: "basecombobox",
        ddCode: "MEALTYPE",
        readOnly:true
    },{
        fieldLabel: "开始时间",
        name: "beginTime",
        xtype: "datetimefield",
    },{
        fieldLabel: "结束时间",
        name: "endTime",
        xtype: "datetimefield",
    }]
});
               
