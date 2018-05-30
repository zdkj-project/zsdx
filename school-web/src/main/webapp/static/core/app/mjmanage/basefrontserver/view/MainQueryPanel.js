Ext.define("core.mjmanage.basefrontserver.view.MainQueryPanel", {
    extend: "core.base.view.BaseQueryForm",
    alias: "widget.mjmanage.basefrontserver.mainquerypanel",
    layout: "form",
    frame: false,
    height: 100,
  
    items: [{
        xtype: "container",
        layout: "column",
        items: [{
            columnWidth: 0.25,
            xtype: "basequeryfield",
            name: "frontServerName",
            fieldLabel: "服务器名称",
            queryType: "textfield",
        }, {
            columnWidth: 0.25,
            xtype: "basequeryfield",
            name: "frontServerPort",
            fieldLabel: "服务器端口",
            queryType: "numberfield",
            dataType:'numberfield',
            value:''
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