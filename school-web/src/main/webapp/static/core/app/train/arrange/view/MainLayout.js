Ext.define("core.train.arrange.view.MainLayout", {
    extend: "core.base.view.BasePanel",
    alias: "widget.arrange.mainlayout",
    /** 引入必须的文件 */
    requires: [
        'core.train.arrange.controller.MainController',
        'core.train.arrange.view.MainGrid',
        'core.train.arrange.view.MainQueryPanel',
        'core.train.arrange.view.ArrangeRoomDetailForm',     //若不写此代码，则会在使用时才会加载它所需要的代码     
        'core.train.arrange.view.ArrangeSiteDetailForm'  
    ],
    /** 关联此视图控制器 */
    controller: 'arrange.mainController',

    /** 页面代码定义 */
    funCode: "arrange_main",
    detCode: "arrange_detail",
    detLayout: "arrange.detaillayout",

    /*标注这个视图控制器的别名，以此提供给window处使用*/
    otherController: 'arrange.otherController',

    layout: 'fit',
    border: false,
    funData: {
        action: comm.get("baseUrl") + "/TrainClass", //请求Action
        whereSql: "", //表格查询条件
        orderSql: "", //表格排序条件
        filter: "",
        pkName: "uuid",
        width: 1000,     //定义弹出window的宽度
        height: 600,    //定义弹出window的高度
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
        xtype: "arrange.maingrid",            
    }]
});
