Ext.define("core.train.coursechkresult.view.MainLayout", {
    extend: "core.base.view.BasePanel",
    alias: "widget.coursechkresult.mainlayout",
    /** 引入必须的文件 */
    requires: [ 
        'core.train.coursechkresult.controller.MainController',
        'core.train.coursechkresult.view.MainGrid',
        'core.train.coursechkresult.view.MainQueryPanel',
        "core.train.coursechkresult.view.DetailLayout",
        "core.train.coursechkresult.view.ClassStudentGrid",
        "core.train.coursechkresult.view.ClassCourseGrid"
    ],    
    /** 关联此视图控制器 */
    controller: 'coursechkresult.mainController',
    /** 页面代码定义 */
    funCode: "coursechkresult_main",
    detCode: "coursechkresult_detail",
    detLayout: "coursechkresult.detaillayout",
    /*标注这个视图控制器的别名，以此提供给window处使用*/
    otherController:'coursechkresult.otherController',
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
        tabConfig: {         //zzk：2017-6-1加入，用于对tab操作提供基本配置数据
            detailTitle: '考勤情况'
        }
    },
    /*设置最小宽度，并且自动滚动*/
    minWidth:1200,
    scrollable:true,

    items: [{
            xtype: "coursechkresult.maingrid",
            region: "center"
    }]
})
