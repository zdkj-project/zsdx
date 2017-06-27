Ext.define("core.train.creditrule.view.MainQueryPanel", {
    extend: "core.base.view.BaseQueryForm",
    alias: "widget.creditrule.mainquerypanel",
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

        },{
            columnWidth: 0.45,
            labelAlign: "right",
            xtype: "basequeryfield",
            name: "startUsing",
            fieldLabel: "是否启用",
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
    },{
        xtype: "container",
        layout: "column",
        items: [{
            columnWidth: 0.45,
            xtype: "basequeryfield",
            name: "normalCredits",
            fieldLabel: "正常出勤学分",
            queryType: "numberfield",
            dataType:'short'
        },{
            columnWidth: 0.45,
            xtype: "basequeryfield",
            name: "lateCredits",
            fieldLabel: "迟到扣除学分",
            queryType: "numberfield",
            dataType:'short'
        }]
    },{
        xtype: "container",
        layout: "column",
        items: [{
            columnWidth: 0.45,
            xtype: "basequeryfield",
            name: "earlyCredits",
            fieldLabel: "早退扣除学分",
            queryType: "numberfield",
            dataType:'short'
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