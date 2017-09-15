Ext.define("core.cashier.mealtime.view.MainLayout", {
    extend: "core.base.view.BasePanel",
    alias: "widget.mealtime.mainlayout",
    /** 引入必须的文件 */
    requires: [ 
        'core.cashier.mealtime.controller.MainController',
        'core.cashier.mealtime.view.MainGrid',
//        'core.train.trainee.view.MainQueryPanel',
//        "core.cashier.mealtime.view.DetailLayout",
    ],    
    /** 关联此视图控制器 */
    controller: 'mealtime.mainController',
    /** 页面代码定义 */
    funCode: "mealtime_main",
    detCode: "mealtime_detail",
    detLayout: "mealtime.detaillayout",
    /*标注这个视图控制器的别名，以此提供给window处使用*/
    otherController:'mealtime.otherController',
    layout:'fit',
    border:false,
    funData: {
        action: comm.get("baseUrl") + "/CashDinnertime", //请求Action
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
        xtype: "mealtime.maingrid",
    }]
})
