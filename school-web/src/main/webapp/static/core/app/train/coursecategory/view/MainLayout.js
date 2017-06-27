Ext.define("core.train.coursecategory.view.MainLayout", {
    extend: "core.base.view.BasePanel",
    alias: "widget.coursecategory.mainlayout",
    /** 引入必须的文件 */
    requires: [ 
        'core.train.coursecategory.controller.MainController',
        //'core.train.coursecategory.model.MainGridModel',
        //'core.train.coursecategory.store.MainGridStore', 
        'core.train.coursecategory.view.MainGrid',
        'core.train.coursecategory.view.MainQueryPanel',
        "core.train.coursecategory.view.DetailLayout",
    ],    
    /** 关联此视图控制器 */
    controller: 'coursecategory.mainController',
    /** 页面代码定义 */
    funCode: "coursecategory_main",
    detCode: "coursecategory_detail",
    detLayout: "coursecategory.detaillayout",
    /*标注这个视图控制器的别名，以此提供给window处使用*/
    otherController:'coursecategory.otherController',
    layout:'border',
    border:false,
    funData: {
        action: comm.get("baseUrl") + "/TrainCourseinfo", //请求Action
        whereSql: "", //表格查询条件
        orderSql: "", //表格排序条件
        filter: "",
        pkName: "uuid",
        width: 600,
        height: 600,
        defaultObj: {},
    },
    items: [{
            xtype: "coursecategory.maingrid",
            region: "center"
    }]
})
