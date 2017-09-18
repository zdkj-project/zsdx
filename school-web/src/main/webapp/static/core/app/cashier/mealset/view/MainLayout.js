Ext.define("core.cashier.mealset.view.MainLayout", {
    extend: "core.base.view.BasePanel",
    alias: "widget.mealset.mainlayout",
    /** 引入必须的文件 */
    requires: [ 
        'core.cashier.mealset.controller.MainController',
        'core.cashier.mealset.view.MainGrid',
//        'core.train.trainee.view.MainQueryPanel',
        "core.cashier.mealset.view.DetailLayout",
    ],    
    /** 关联此视图控制器 */
    controller: 'mealset.mainController',
    /** 页面代码定义 */
    funCode: "mealset_main",
    detCode: "mealset_detail",
    detLayout: "mealset.detaillayout",
    /*标注这个视图控制器的别名，以此提供给window处使用*/
    otherController:'mealset.otherController',
    layout:'fit',
    border:false,
    funData: {
        action: comm.get("baseUrl") + "/CashDinneritem", //请求Action
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
        xtype: "mealset.maingrid",
    }]
})
