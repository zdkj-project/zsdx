Ext.define("core.train.trainee.view.MainLayout", {
    extend: "core.base.view.BasePanel",
    alias: "widget.trainee.mainlayout",
    /** 引入必须的文件 */
    requires: [ 
        'core.train.trainee.controller.MainController',
        'core.train.trainee.view.MainGrid',
        'core.train.trainee.view.MainQueryPanel',
        "core.train.trainee.view.DetailLayout",
    ],    
    /** 关联此视图控制器 */
    controller: 'trainee.mainController',
    /** 页面代码定义 */
    funCode: "trainee_main",
    detCode: "trainee_detail",
    detLayout: "trainee.detaillayout",
    /*标注这个视图控制器的别名，以此提供给window处使用*/
    otherController:'trainee.otherController',
    layout:'fit',
    border:false,
    funData: {
        action: comm.get("baseUrl") + "/TrainTrainee", //请求Action
        whereSql: "", //表格查询条件
        orderSql: "", //表格排序条件
        filter: "",
        pkName: "uuid",
        defaultObj: {
            xbm:"1",
            mzm:"1",
            zzmmm:"1",
            traineeCategory:'02',
            xlm:"21",
            xwm:"4"
        },
        tabConfig: {
            addTitle: '添加学员',
            editTitle: '编辑学员',
            detailTitle: '学员详情'
        }
    },

    /*设置最小宽度，并且自动滚动*/
    minWidth:1200,
    scrollable:true,
    
    items: [{
        xtype: "trainee.maingrid",
    }]
})
