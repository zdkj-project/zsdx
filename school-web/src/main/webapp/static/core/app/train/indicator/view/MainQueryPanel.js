Ext.define("core.train.indicator.view.MainQueryPanel", {
    extend: "core.base.view.BaseQueryForm",
    alias: "widget.indicator.mainquerypanel",
    layout: "form",
    frame: false,
    height: 50,

    fieldDefaults: { // 统一设置表单字段默认属性
        labelSeparator: '：', // 分隔符
        labelAlign: "right",
        width: '100%'
    },

    items: [{
        xtype: "container",
        layout: "column",
        items: [{
            columnWidth: 0.3,
            labelWidth: 100,
            xtype: "basequeryfield",
            name: "indicatorName",
            fieldLabel: "指标名称",
            queryType: "textfield",
        }, {
            columnWidth: 0.3,
            labelWidth: 100,
            xtype: "basequeryfield",
            name: "indicatorStand",
            fieldLabel: "评价标准",
            queryType: "textfield"
        },  {
            //buttonAlign: "left",
            xtype: "container",
            layout: "column",
            items:[{
                xtype: "button",
                align: "right",
                text: '搜 索',
                ref: 'gridSearchFormOk',
                iconCls: 'x-fa fa-search',
                margin:'0 0 0 10'
            }, {
                xtype: "button",
                align: "right",
                text: '重 置',
                ref: 'gridSearchFormReset',
                iconCls: 'x-fa fa-undo',
                margin:'0 0 0 10'
            }]
        }]
    }]
});