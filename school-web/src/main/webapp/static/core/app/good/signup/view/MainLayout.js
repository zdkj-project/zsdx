Ext.define("core.good.signup.view.MainLayout", {
    extend: "Ext.container.Container",
    alias: 'widget.signup.mainlayout',

    requires: [    
        'core.good.signup.controller.MainController',
        'core.good.signup.model.MainGridModel',
        'core.good.signup.store.MainGridStore', 
        'core.good.signup.view.MainGrid',
        'core.good.signup.view.MainQueryPanel',
        "core.good.signup.view.DetailLayout",
   
    ],

    controller: 'signup.mainController',

    funCode: "signup_main",
    detCode: 'signup_detail',
    detLayout: 'signup.detaillayout',
  

    funData: {
        //action: comm.get('baseUrl') + "/signup", //请求Action
        action: "/signup",
        whereSql: "", //表格查询条件
        orderSql: " order by orderIndex", //表格排序条件
        pkName: "uuid",
        defaultObj: {
          
        }
    },  

    layout: 'border',
    items: [{
        xtype: "signup.mainquerypanel",
        region: 'north',
        height:150
    },{
        xtype: "signup.maingrid",
        margin:'2 0 0 0',
        region: 'center'
    }]
})