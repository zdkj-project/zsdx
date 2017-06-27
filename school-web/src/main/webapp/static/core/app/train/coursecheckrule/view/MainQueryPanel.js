Ext.define("core.train.coursecheckrule.view.MainQueryPanel", {
	extend:"core.base.view.BaseQueryForm",
	alias: "widget.coursecheckrule.mainquerypanel",
    layout: "form",
    frame: false,
	height: 180,

    items: [{
        xtype: "container",
        layout: "column",
        items: [{
            columnWidth: 0.45,
            xtype: "basequeryfield",
            name: "ruleName",
            fieldLabel: "规则名称",
            queryType: "textfield",

        }, {
            columnWidth: 0.45,
            xtype: "basequeryfield",
            name: "checkMode",
            fieldLabel: "考勤模式",
            queryType: "basecombobox",
            config: {
                ddCode: "CHECKMODE",
                dataType:'short',
            },

        }]
    },{
        xtype: "container",
        layout: "column",
        items: [{
            columnWidth: 0.45,
            xtype: "basequeryfield",
            name: "inBefore",
            fieldLabel: "签到提前分钟",
            queryType: "numberfield",
            dataType:'short'
        },{
            columnWidth: 0.45,
            xtype: "basequeryfield",
            name: "beLate",
            fieldLabel: "迟到分钟",
            queryType: "numberfield",
            dataType:'short'
        }]
    },{
        xtype: "container",
        layout: "column",
        items: [{
            columnWidth: 0.45,
            xtype: "basequeryfield",
            name: "absenteeism",
            fieldLabel: "缺勤分钟",
            queryType: "numberfield",
            dataType:'short'
        },,{
            columnWidth: 0.25,
            labelAlign: "right",
            xtype: "basequeryfield",
            name: "needCheckout",
            fieldLabel: "是否启用签退",
            queryType: "combo",
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
                dataType:'short',     //ZZK 2017-4-12 加入，用于在SqlUtil获取值的时候直接判断此值是什么类型
            }
        },{
            columnWidth: 0.25,
            labelAlign: "right",
            xtype: "basequeryfield",
            name: "startUsing",
            fieldLabel: "是否启用规则",
            queryType: "combo",
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
                dataType:'short',     //ZZK 2017-4-12 加入，用于在SqlUtil获取值的时候直接判断此值是什么类型
            },
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