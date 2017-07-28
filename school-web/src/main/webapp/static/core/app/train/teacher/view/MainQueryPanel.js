Ext.define("core.train.teacher.view.MainQueryPanel", {
    extend: "core.base.view.BaseQueryForm",
    alias: "widget.teacher.mainquerypanel",
    layout: "form",
    frame: false,
    height: 90,

    fieldDefaults: { // 统一设置表单字段默认属性
        labelSeparator: '：', // 分隔符
        labelWidth:100,
        labelAlign: "right",
        width:'100%'
    },   


    items: [{
        xtype: "container",
        layout: "column",
        items: [{
            columnWidth: 0.25,
            xtype: "basequeryfield",
            name: "xm",
            fieldLabel: "姓名",
            queryType: "textfield",
        }, {
            columnWidth: 0.25,
            xtype: "basequeryfield",
            name: "xbm",
            fieldLabel: "性别",
            queryType: "basecombobox",
            config: {
                ddCode: "XBM"
            }
        }, {
            columnWidth: 0.25,
            xtype: "basequeryfield",
            name: "position",
            fieldLabel: "职务",
            queryType: "textfield"
        },{
            columnWidth: 0.25,
            xtype: "basequeryfield",
            name: "technical",
            fieldLabel: "职称",
            queryType: "basecombobox",
            config: {
                ddCode: "TECHNICAL"
            }
        }]
    },{
        xtype: "container",
        layout: "column",
        items: [{
            columnWidth: 0.25,
            xtype: "basequeryfield",
            name: "inout",
            fieldLabel: "校内/校外",
            queryType: "basecombobox",
            config: {
                ddCode: "INOUT"
            }
        },  {
            columnWidth: 0.25,
            xtype: "basequeryfield",
            name: "workUnits",
            fieldLabel: "工作单位",
            queryType: "textfield"
        }, {
            columnWidth: 0.25,
            xtype: "container",
            layout: "column",
            margin:'0 0 0 50',
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