Ext.define("core.train.trainee.view.MainQueryPanel", {
    extend: "core.base.view.BaseQueryForm",
    alias: "widget.trainee.mainquerypanel",
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
            columnWidth: 0.15,
            labelWidth: 50,
            xtype: "basequeryfield",
            name: "xm",
            fieldLabel: "姓名",
            queryType: "textfield",
        }, {
            columnWidth: 0.15,
            labelWidth: 50,
            xtype: "basequeryfield",
            name: "xbm",
            fieldLabel: "性别",
            queryType: "basecombobox",
            config: {
                ddCode: "XBM"
            }
        }, {
            columnWidth: 0.15,
            labelWidth: 50,
            xtype: "basequeryfield",
            name: "position",
            fieldLabel: "职务",
            queryType: "textfield"
        }, {
            columnWidth: 0.15,
            labelWidth: 80,
            xtype: "basequeryfield",
            name: "headshipLevel",
            fieldLabel: "行政级别",
            queryType: "basecombobox",
            config: {
                ddCode: "HEADSHIPLEVEL"
            }
        }, {
            columnWidth: 0.2,
            labelWidth: 80,
            xtype: "basequeryfield",
            name: "workUnit",
            fieldLabel: "工作单位",
            queryType: "textfield"
        }, {
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