Ext.define("core.oa.meeting.checkrule.view.MainQueryPanel", {
    extend: "core.base.view.BaseQueryForm",
    alias: "widget.checkrule.mainquerypanel",
    layout: "form",
    frame: false,
    height: 100,
    items: [{
        xtype: "container",
        layout: "column",
        items: [{
            columnWidth:0.25,
            labelAlign: "right",
            xtype: "basequeryfield",
            name: "ruleName",
            fieldLabel: "规则名称",
            queryType: "textfield",
        },{
            columnWidth: 0.25,
            xtype: "basequeryfield",
            queryType: "combobox",
            dataType:"short",     //指定filter的数据类型为 short
            name: "needCheckout",
            fieldLabel: "是否需要签退",
            store: Ext.create('Ext.data.Store', {
                fields: ['name', 'value'],
                data : [
                    {"name":"不需要", "value":0},
                    {"name":"需要", "value":1}             
                ]
            }),
            queryMode: 'local',
            displayField: 'name',
            valueField: 'value',
            editable:false
        },{
            columnWidth: 0.25,
            xtype: "basequeryfield",
            queryType: "combobox",
            dataType:"short",     //指定filter的数据类型为 short
            name: "startUsing",
            fieldLabel: "是否启用",
            store: Ext.create('Ext.data.Store', {
                fields: ['name', 'value'],
                data : [
                    {"name":"未启用", "value":0},
                    {"name":"已启用", "value":1}                                 
                ]
            }),
            queryMode: 'local',
            displayField: 'name',
            valueField: 'value',
            editable:false
        }]
    }],
    buttonAlign: "center",
    buttons: [{
        xtype: 'button',
        text: '搜 索',
        ref: 'gridSearchFormOk',
        iconCls: 'x-fa fa-search',
    }, {
        xtype: 'button',
        text: '重 置',
        ref: 'gridSearchFormReset',
        iconCls: 'x-fa fa-undo',
    }]
});