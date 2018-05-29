Ext.define("core.good.activity.view.MainQueryPanel", {
    extend: 'core.base.view.BaseQueryForm',

    alias: 'widget.activity.mainquerypanel',
 
    items: [{   
        margin:"2 10 0 0",
        xtype: "basequeryfield",
        queryType: "textfield",
        fieldLabel: "姓名1",
        name: "name",
       // width:300,
    },{
        margin:"2 10 0 0",
        xtype: "basequeryfield",
        queryType: "combobox",
        fieldLabel: "性别1",
        name: "sex",
        store: Ext.create('Ext.data.Store', {
            fields: ['name', 'value'],
            data : [
                {"name":"男", "value":true},    
                {"name":"女", "value":false}
            ]
        }),
        queryMode: 'local',
        displayField: 'name',
        valueField: 'value',
        editable:false,
       // width:300,
    },{            
        xtype:'button',
        text:'搜 索',
        ref:'gridSearchFormOk',
        iconCls:'x-fa fa-search',
    },{            
        xtype:'button',
        text:'重 置',
        ref:'gridSearchFormReset',
        iconCls:'x-fa fa-undo',
    }]
    
    // buttonAlign: "center",
    // buttons: [{
    //  text: '查询',
    //  ref: 'queryBtn',
    //  iconCls: 'tree_ok'
    // }, {
    //  text: '重置',
    //  ref: 'resetBtn',
    //  iconCls: 'tree_delete'
    // }]
});