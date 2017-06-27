Ext.define("core.good.activity.view.MainLayout", {
    extend: "core.base.view.BasePanel",
    alias: 'widget.activity.mainlayout',

    requires: [    
        'core.good.activity.controller.MainController',
        'core.good.activity.model.MainGridModel',
        'core.good.activity.store.MainGridStore', 
        'core.good.activity.view.MainGrid',
        'core.good.activity.view.MainQueryPanel',
        "core.good.activity.view.DetailLayout",
    ],

    /*关联此视图控制器*/
    controller: 'activity.mainController',


    funCode: "activity_main",
    detCode: 'activity_detail',
    detLayout: 'activity.detaillayout',

    /*标注这个视图控制器的别名，以此提供给window处使用*/
    otherController:'activity.otherController',

    layout:'border',
    funData: {
        action: comm.get('baseUrl') + "/goodactivity", //请求Action
        whereSql: "", //表格查询条件
        orderSql: " order by orderIndex", //表格排序条件
        pkName: "uuid",
        defaultObj: {},
        width:550,
        height:300,
    },
   
    items: [/*{
        xtype: "activity.mainquerypanel",
        region: "north",
        margin:'0 0 2 0',
    }, */{
        xtype: "activity.maingrid",
        region: "center"
    }]
  
});