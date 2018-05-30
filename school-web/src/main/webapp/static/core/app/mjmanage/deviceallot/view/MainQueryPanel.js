Ext.define("core.mjmanage.deviceallot.view.MainQueryPanel", {
    extend: "core.base.view.BaseQueryForm",
    alias: "widget.mjmanage.deviceallot.mainquerypanel",
    layout: "form",
    frame: false,
    height: 100,

    fieldDefaults: { // 统一设置表单字段默认属性
        labelSeparator: '：', // 分隔符
        labelAlign: "right",
        width: '100%'
    },

    items: [{
        xtype: "container",
        layout: "column",
        items: [{
            columnWidth: 0.33,
            xtype: "basequeryfield",
            name: "termSN",
            fieldLabel: "序列号",
            queryType: "textfield",
        }, {
            columnWidth: 0.33,
            xtype: "basequeryfield",
            name: "termNo",
            queryType: "numberfield",
            dataType:'numberfield',
            operationType:"other",
            fieldLabel: "机号",
            value:''
        }, {
            columnWidth: 0.33,
            xtype: "basequeryfield",
            name: "termName",
            fieldLabel: "设备名称",
            queryType: "textfield",
        }]
    }],
    buttonAlign: "center",
    buttons: [{
        text: '查询',
        ref: 'gridSearchFormOk',
        iconCls: 'x-fa fa-search',
        formBind: true, //只要表单数据正常的时候，才会允许查询
    }, {
        text: '重置',
        ref: 'gridSearchFormReset',
        iconCls: 'x-fa fa-undo'
    }]
});