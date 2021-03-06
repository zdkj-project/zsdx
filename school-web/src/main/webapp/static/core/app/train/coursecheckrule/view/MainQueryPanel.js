Ext.define("core.train.coursecheckrule.view.MainQueryPanel", {
	extend:"core.base.view.BaseQueryForm",
	alias: "widget.coursecheckrule.mainquerypanel",
    layout: "form",
    frame: false,
	height: 140,

    items: [{
        xtype: "container",
        layout: "column",
        items: [{
            columnWidth: 0.25,
            xtype: "basequeryfield",
            queryType: "textfield",
            name: "ruleName",
            fieldLabel: "规则名称",
        }, {
            columnWidth: 0.25,
            xtype: "basequeryfield",
            queryType: "basecombobox",
            dataType:'short',
            name: "checkMode",
            fieldLabel: "考勤模式",
            config: {
                ddCode: "CHECKMODE"
            }
        },{
            columnWidth: 0.25,
            labelAlign: "right",
            xtype: "basequeryfield",
            queryType: "combobox",
            dataType:'short',
            name: "needCheckout",
            fieldLabel: "是否启用签退",
            config: {
                store:Ext.create('Ext.data.Store', {
                    fields: ['val', 'name'],
                    data : [                
                        {"val":0, "name":"未启用"},
                        {"val":1, "name":"启用"},                      
                    ]  
                }),
                queryMode: 'local',
                displayField: 'name',
                valueField: 'val',
                editable:false,
            }
        },{
            columnWidth: 0.25,
            labelAlign: "right",
            xtype: "basequeryfield",
            queryType: "combobox",
            dataType:'short',
            name: "startUsing",
            fieldLabel: "是否启用规则",
            config: {
                store:Ext.create('Ext.data.Store', {
                    fields: ['val', 'name'],
                    data : [                
                        {"val":0, "name":"未启用"},
                        {"val":1, "name":"启用"},                      
                    ]  
                }),
                queryMode: 'local',
                displayField: 'name',
                valueField: 'val',
                editable:false,
            }
        }]
    },{
        xtype: "container",
        layout: "column",
        items: [{
            columnWidth: 0.25,
            xtype: "basequeryfield",
            name: "inBefore",
            fieldLabel: "签到提前分钟",
            queryType: "numberfield",
            dataType:'short',
            value:''
        },{
            columnWidth: 0.25,
            xtype: "basequeryfield",
            name: "beLate",
            fieldLabel: "迟到分钟",
            queryType: "numberfield",
            dataType:'short',
            value:''
        },{
            columnWidth: 0.25,
            xtype: "basequeryfield",
            name: "absenteeism",
            fieldLabel: "缺勤分钟",
            queryType: "numberfield",
            dataType:'short',
            value:''
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