Ext.define("core.train.course.view.MainLayout", {
    extend: "core.base.view.BasePanel",
    alias: "widget.course.mainlayout",
    /** 引入必须的文件 */
    requires: [
        'core.train.course.controller.MainController',
        //'core.train.course.model.MainGridModel',
        //'core.train.course.store.MainGridStore',
        'core.train.course.view.MainGrid',
        'core.train.course.view.MainQueryPanel',
        "core.train.course.view.DetailLayout",
    ],
    /** 关联此视图控制器 */
    controller: 'course.mainController',
    /** 页面代码定义 */
    funCode: "course_main",
    detCode: "course_detail",
    detLayout: "course.detaillayout",
    /*标注这个视图控制器的别名，以此提供给window处使用*/
    otherController: 'course.otherController',
    layout: 'border',
    border: false,
    funData: {
        action: comm.get("baseUrl") + "/TrainCourseinfo", //请求Action
        whereSql: "", //表格查询条件
        orderSql: "", //表格排序条件
        filter: "",
        pkName: "uuid",
        defaultObj: {
            orderIndex: 1,
            teachType: '01',
            periodTime:150,
            credits:4
        },
        tabConfig: {         //zzk：2017-6-1加入，用于对tab操作提供基本配置数据
            addTitle: '添加课程',
            editTitle: '编辑课程',
            detailTitle: '课程详情'
        }
    },
    /*设置最小宽度，并且自动滚动*/
    minWidth: 1200,
    scrollable: true,

    items: [{
        xtype: "course.coursecategorytree",
        region: "west",
        width: 200,
        split: true,
        style: {
            border: '1px solid #ddd'
        },
        frame: false
    }, {
        xtype: "course.maingrid",
        region: "center",
        flex: 1.5,
        style: {
            border: '1px solid #ddd'
        },
    }]
})
