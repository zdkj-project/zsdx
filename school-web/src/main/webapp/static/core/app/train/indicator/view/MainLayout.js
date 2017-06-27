Ext.define("core.train.indicator.view.MainLayout", {
    extend: "core.base.view.BasePanel",
    alias: "widget.indicator.mainlayout",
    /** 引入必须的文件 */
    requires: [ 
        'core.train.indicator.controller.MainController',
        'core.train.indicator.view.MainGrid',
        'core.train.indicator.view.MainQueryPanel',
        "core.train.indicator.view.DetailLayout",
    ],    
    /** 关联此视图控制器 */
    controller: 'indicator.mainController',
    /** 页面代码定义 */
    funCode: "indicator_main",
    detCode: "indicator_detail",
    detLayout: "indicator.detaillayout",
    /*标注这个视图控制器的别名，以此提供给window处使用*/
    otherController:'indicator.otherController',
    layout:'fit',
    border:false,
    funData: {
        action: comm.get("baseUrl") + "/TrainEvalIndicator", //请求Action
        whereSql: "", //表格查询条件
        orderSql: "", //表格排序条件
        filter: "",
        pkName: "uuid",
        defaultObj: {} ,
        tabConfig: {
            addTitle: '添加指标',
            editTitle: '编辑指标',
            detailTitle: '指标详情'
        }
    },

    /*设置最小宽度，并且自动滚动*/
    minWidth:1200,
    scrollable:true,
    
    items: [{
        xtype: "indicator.maingrid",
    }]
})
