Ext.define("core.train.calcucredit.view.MainLayout", {
    extend: "core.base.view.BasePanel",
    alias: "widget.calcucredit.mainlayout",
    /** 引入必须的文件 */
    requires: [ 
        'core.train.calcucredit.controller.MainController',
        'core.train.calcucredit.view.MainGrid',
        'core.train.calcucredit.view.MainQueryPanel',
        "core.train.calcucredit.view.DetailLayout",
    ],    
    /** 关联此视图控制器 */
    controller: 'calcucredit.mainController',
    /** 页面代码定义 */
    funCode: "calcucredit_main",
    detCode: "calcucredit_detail",
    detLayout: "calcucredit.detaillayout",
    /*标注这个视图控制器的别名，以此提供给window处使用*/
    otherController:'calcucredit.otherController',
    layout:'border',
    border:false,
    funData: {
        action: comm.get("baseUrl") + "/TrainClass", //请求Action
        whereSql: "", //表格查询条件
        orderSql: "", //表格排序条件
        filter: "",
        pkName: "uuid",
        width: 1000,
        height: 600,
        defaultObj: {},
    },

    /*设置最小宽度，并且自动滚动*/
    minWidth:1200,
    scrollable:true,

    items: [{
        xtype: "calcucredit.maingrid",
        region: "west",
        width: 440,
        split: true,
        style: {
            border: '1px solid #ddd'
        },
        frame: false
    }, {
        xtype: "calcucredit.traineesgrid",
        region: "center",
        flex: 1.5,
        style: {
            border: '1px solid #ddd'
        }
    }]
})
