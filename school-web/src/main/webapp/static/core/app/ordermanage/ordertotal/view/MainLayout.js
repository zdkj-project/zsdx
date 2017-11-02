Ext.define("core.ordermanage.ordertotal.view.MainLayout", {
    extend: "core.base.view.BasePanel",
    alias: "widget.ordertotal.mainlayout",
    /** 引入必须的文件 */
    requires: [
        'core.ordermanage.ordertotal.controller.MainController'
    ],
    /** 关联此视图控制器 */
    controller: 'ordertotal.mainController',

    /** 页面代码定义 */
    funCode: "ordertotal_main",
    detCode: "ordertotal_detail",
    detLayout: "ordertotal.detaillayout",

    /*标注这个视图控制器的别名，以此提供给window处使用*/
    otherController: 'ordertotal.otherController',

    layout: 'fit',
    border: false,
    funData: {
        action: comm.get("baseUrl") + "/TrainTeacherOrder", //请求Action
        whereSql: "", //表格查询条件
        orderSql: "", //表格排序条件
        filter: "",
        pkName: "uuid",
        width: 1000,     //定义弹出window的宽度
        height: 600,    //定义弹出window的高度
        defaultObj: {},
        tabConfig:{         //zzk：2017-6-1加入，用于对tab操作提供基本配置数据
            detailTitle:'订餐人员'
        }
    },

    /*设置最小宽度，并且自动滚动*/
    minWidth:1200,
    scrollable:true,

    items: [{
        xtype: "ordertotal.maingrid",   
    }]
});
