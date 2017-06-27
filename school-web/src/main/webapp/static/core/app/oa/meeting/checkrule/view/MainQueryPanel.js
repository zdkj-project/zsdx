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
            columnWidth:0.3,
            labelAlign: "right",
            xtype: "basequeryfield",
            name: "ruleName",
            fieldLabel: "规则名称",
            queryType: "textfield",
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