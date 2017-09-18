Ext.define("core.system.appupdate.view.MainLayout", {
    extend: "core.base.view.BasePanel",
    alias: 'widget.appupdate.mainlayout',
    funCode: "appupdate_main",
    detCode: 'appupdate_detail',
    detLayout: 'appupdate.detaillayout',
    layout:'fit',
    funData: {
        action: comm.get('baseUrl') + "/SysAppinfo", //请求Action
        whereSql: "", //表格查询条件
        orderSql: " order by orderIndex", //表格排序条件
        pkName: "uuid",
        defaultObj: {},
        width:550,
        height:300,
    },
    
      /*关联此视图控制器*/
    controller: 'appupdate.mainController',
    
    /*标注这个视图控制器的别名，以此提供给window处使用*/
    otherController:'appupdate.otherController',

    items: [{
        xtype: "appupdate.appgrid",        
    }]
  
});