Ext.define("core.train.class.view.MainLayout", {
    extend: "core.base.view.BasePanel",
    alias: "widget.class.mainlayout",
    /** 引入必须的文件 */
    requires: [ 
        'core.train.class.controller.MainController',
        //'core.train.class.model.MainGridModel',
        //'core.train.class.store.MainGridStore', 
        //'core.train.class.view.MainGrid',
        //'core.train.class.view.MainQueryPanel',
        "core.train.class.view.DetailLayout",
        //"core.train.class.view.CourseGrid",
        //"core.train.class.view.StudentGrid"
        "core.train.class.view.SelectStudentLayout"
    ],    
    /** 关联此视图控制器 */
    controller: 'class.mainController',
    /** 页面代码定义 */
    funCode: "class_main",
    detCode: "class_detail",
    detLayout: "class.detaillayout",
    /*标注这个视图控制器的别名，以此提供给window处使用*/
    otherController:'class.otherController',
    layout:'fit',
    border:false,
    funData: {
        action: comm.get("baseUrl") + "/TrainClass", //请求Action
        whereSql: "", //表格查询条件
        orderSql: "", //表格排序条件
        filter: "",
        pkName: "uuid",
        width: 800,
        height: 460,
        defaultObj: {},
        tabConfig:{         //zzk：2017-6-1加入，用于对tab操作提供基本配置数据
            addTitle:'添加班级',
            editTitle:'编辑班级',
            detailTitle:'班级详情'
        }
    },

    /*设置最小宽度，并且自动滚动*/
    minWidth:1200,
    scrollable:true,

    items: [{
        xtype: "class.maingrid",
        /*region: "center",
        flex:2,
        style:{
            border: '1px solid #ddd'
        },*/
    }]
})
