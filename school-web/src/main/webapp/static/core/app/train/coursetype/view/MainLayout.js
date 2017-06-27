Ext.define("core.train.coursetype.view.MainLayout", {
    extend: "core.base.view.BasePanel",
    alias: "widget.coursetype.mainlayout",
    /** 引入必须的文件 */
    requires: [ 
        'core.train.coursetype.controller.MainController',
        "core.train.coursetype.view.DetailLayout",
    ],    
    /** 关联此视图控制器 */
    controller: 'coursetype.mainController',

    /** 页面代码定义 */
    funCode: "coursetype_main",
    detCode: "coursetype_detail",
    detLayout: "coursetype.detaillayout",
    /*标注这个视图控制器的别名，以此提供给window处使用*/
    otherController:'coursetype.otherController',
    layout:'fit',
    border:false,
    funData: {
        action: comm.get("baseUrl") + "/TrainCoursecategory", //请求Action
        whereSql: "", //表格查询条件
        orderSql: "", //表格排序条件
        filter: "",
        pkName: "uuid",
        width: 600,
        height: 340,
        defaultObj: {
            //orderIndex:1
        },
        tabConfig:{         //zzk：2017-6-1加入，用于对tab操作提供基本配置数据
            addTitle:'添加课程分类',
            editTitle:'编辑课程分类',
            detailTitle:'课程分类详情'
        }
    },
    items: [{
        xtype: "coursetype.coursecategorytree",
        //title:'课程分类',
        //border:true,
        /*style:{
            border: '1px solid #ddd'
        },*/
        frame:false
    }]
})
