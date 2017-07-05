Ext.define("core.train.coursecheckrule.view.MainLayout", {
    extend: "core.base.view.BasePanel",
    alias: "widget.coursecheckrule.mainlayout",
    /** 引入必须的文件 */
    requires: [ 
        'core.train.coursecheckrule.controller.MainController',
        //'core.train.coursecheckrule.model.MainGridModel',
        //'core.train.coursecheckrule.store.MainGridStore', 
        //'core.train.coursecheckrule.view.MainGrid',
        //'core.train.coursecheckrule.view.MainQueryPanel',
        "core.train.coursecheckrule.view.DetailLayout",
    ],    
    /** 关联此视图控制器 */
    controller: 'coursecheckrule.mainController',
    /** 页面代码定义 */
    funCode: "coursecheckrule_main",
    detCode: "coursecheckrule_detail",
    detLayout: "coursecheckrule.detaillayout",
    /*标注这个视图控制器的别名，以此提供给window处使用*/
    otherController:'coursecheckrule.otherController',
    layout:'border',
    border:false,
    funData: {
        action: comm.get("baseUrl") + "/TrainCheckrule", //请求Action
        whereSql: "", //表格查询条件
        orderSql: "", //表格排序条件
        filter: "",
        pkName: "uuid",
        width: 800,
        height: 420,
        defaultObj: {},
        tabConfig: {         //zzk：2017-6-1加入，用于对tab操作提供基本配置数据
            addTitle: '添加规则',
            editTitle: '编辑规则',
            detailTitle: '规则详细'
        }
    },

    /*设置最小宽度，并且自动滚动*/
    minWidth:1200,
    scrollable:true,
    
    items: [{
            xtype: "coursecheckrule.maingrid",
            region: "center"
    }]
})
