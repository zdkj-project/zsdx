Ext.define("core.train.teacher.view.MainLayout", {
    extend: "core.base.view.BasePanel",
    alias: "widget.teacher.mainlayout",
    /** 引入必须的文件 */
    requires: [
        'core.train.teacher.controller.MainController',
        'core.train.teacher.view.MainGrid',
        'core.train.teacher.view.MainQueryPanel',
        "core.train.teacher.view.DetailLayout",
        "core.train.teacher.view.DetailHtmlPanel",

    ],
    /** 关联此视图控制器 */
    controller: 'teacher.mainController',
    /** 页面代码定义 */
    funCode: "teacher_main",
    detCode: "teacher_detail",
    detLayout: "teacher.detaillayout",
    /*标注这个视图控制器的别名，以此提供给window处使用*/
    otherController: 'teacher.otherController',
    layout: 'fit',
    border: false,
    funData: {
        action: comm.get("baseUrl") + "/TrainTeacher", //请求Action
        whereSql: "", //表格查询条件
        orderSql: "", //表格排序条件
        filter: "",
        pkName: "uuid",
        width: 1000,
        height: 650,
        defaultObj: {
            xbm:"1",
            inout:"1",
            xlm:"21"
        },
        tabConfig: {         //zzk：2017-6-1加入，用于对tab操作提供基本配置数据
            addTitle: '添加师资',
            editTitle: '编辑师资',
            detailTitle: '课程师资'
        }
    },
    /*设置最小宽度，并且自动滚动*/
    minWidth:1200,
    scrollable:true,
    
    items: [{
        xtype: "teacher.maingrid",
    }]
})