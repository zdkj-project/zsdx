Ext.define("core.cashier.dishes.view.MainLayout", {
    extend: "core.base.view.BasePanel",
    alias: "widget.dishes.mainlayout",
    /** 引入必须的文件 */
    requires: [ 
        'core.cashier.dishes.controller.MainController',
        'core.cashier.dishes.view.MainGrid',
//        'core.train.trainee.view.MainQueryPanel',
        "core.cashier.dishes.view.DetailLayout",
    ],    
    /** 关联此视图控制器 */
    controller: 'dishes.mainController',
    /** 页面代码定义 */
    funCode: "dishes_main",
    detCode: "dishes_detail",
    detLayout: "dishes.detaillayout",
    /*标注这个视图控制器的别名，以此提供给window处使用*/
    otherController:'dishes.otherController',
    layout:'fit',
    border:false,
    funData: {
        action: comm.get("baseUrl") + "/CashDishes", //请求Action
        whereSql: "", //表格查询条件
        orderSql: "", //表格排序条件
        filter: "",
        pkName: "uuid",
        defaultObj: {} ,
        tabConfig: {
            addTitle: '添加菜品',
            editTitle: '编辑菜品',
            detailTitle: '菜品详情'
        }
    },

    /*设置最小宽度，并且自动滚动*/
    minWidth:1200,
    scrollable:true,
    
    items: [{
        xtype: "dishes.maingrid",
    }]
})
