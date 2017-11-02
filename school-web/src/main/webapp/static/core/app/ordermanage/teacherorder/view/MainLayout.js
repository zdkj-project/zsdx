Ext.define("core.ordermanage.teacherorder.view.MainLayout", {
    extend: "core.base.view.BasePanel",
    alias: "widget.teacherorder.mainlayout",
    /** 引入必须的文件 */
    requires: [
        'core.ordermanage.teacherorder.controller.MainController'
    ],
    /** 关联此视图控制器 */
    controller: 'teacherorder.mainController',

    /** 页面代码定义 */
    funCode: "teacherorder_main",
    detCode: "teacherorder_detail",
    detLayout: "teacherorder.detaillayout",

    /*标注这个视图控制器的别名，以此提供给window处使用*/
    otherController: 'teacherorder.otherController',

    
    border: false,
    funData: {
        action: comm.get("baseUrl") + "/TrainTeacherOrderDesc", //请求Action
        whereSql: "", //表格查询条件
        orderSql: "", //表格排序条件
        filter: "",
        pkName: "uuid",
        width: 1000,     //定义弹出window的宽度
        height: 600,    //定义弹出window的高度
        defaultObj: {},
        tabConfig:{         //zzk：2017-6-1加入，用于对tab操作提供基本配置数据
            addTitle:'',
            editTitle:'',
            detailTitle:''
        }
    },
    style: {
        backgroundColor:'red',
    },  
    /*设置最小宽度，并且自动滚动*/
    minWidth:1200,
    scrollable:'x',
    layout: 'border',
    bodyStyle: {
        background: '#fff'
    },
    items: [{
        xtype: "teacherorder.detailhtmlpanel",  
        height:240,
        region: "north",
    },{
        xtype: "teacherorder.maingrid",
        //margin:'10',
        margin: '5 20 5 10',
        //height:200,
        style:{
            border: '1px solid #f5f5f5'
        },
        region: "center",
    }]
});
