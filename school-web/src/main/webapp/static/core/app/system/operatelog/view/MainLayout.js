Ext.define("core.system.operatelog.view.MainLayout", {
    extend: "core.base.view.BasePanel",
    alias: 'widget.operatelog.mainlayout',
    
    requires: [    
        "core.system.operatelog.controller.MainController",
    	"core.system.operatelog.view.MainLayout",
        "core.system.operatelog.view.MainGrid",
   
    ],
    /*关联此视图控制器*/
    controller: 'operatelog.mainController',
    /*控制器中获取组件*/
    funCode: "operatelog_main",
    detCode: 'operatelog_detail',
    detLayout: 'operatelog.detaillayout',
    funData: {
        action: comm.get('baseUrl') + "/OperateLog", //请求Action
        whereSql: "", //表格查询条件
        orderSql: " order by orderIndex", //表格排序条件
        pkName: "uuid",
        defaultObj: {
        },
        
        /*7.10 lhy*/
        tabConfig:{
            addTitle:'添加',
            reactTitle:'编辑',
            deleteTitle:'删除'
        }
    },


    items: [{
        xtype: "operatelog.maingrid",
        region: "center"
        
    }]
})