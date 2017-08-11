Ext.define("core.oa.meeting.checkresult.view.MainLayout", {
    extend: "core.base.view.BasePanel",
    alias: "widget.checkresult.mainlayout",
    /** 引入必须的文件 */
    requires: [ 
        'core.oa.meeting.checkresult.controller.MainController',
        //'core.oa.meeting.checkresult.model.MainGridModel',
        //'core.oa.meeting.checkresult.store.MainGridStore', 
        'core.oa.meeting.checkresult.view.MainGrid',
        'core.oa.meeting.checkresult.view.MainQueryPanel',
        "core.oa.meeting.checkresult.view.DetailLayout",
    ],    
    /** 关联此视图控制器 */
    controller: 'checkresult.mainController',
    /** 页面代码定义 */
    funCode: "checkresult_main",
    detCode: "checkresult_detail",
    detLayout: "checkresult.detaillayout",
    /*标注这个视图控制器的别名，以此提供给window处使用*/
    otherController:'checkresult.otherController',
    layout:'fit',
    border:false,
    funData: {
        action: comm.get("baseUrl") + "/OaMeeting", //请求Action
        whereSql: "", //表格查询条件
        orderSql: "", //表格排序条件
        filter: "",
        pkName: "uuid",
        width: 1000,
        height: 500,
        defaultObj: {},
        tabConfig:{         //zzk：2017-6-1加入，用于对tab操作提供基本配置数据
            addTitle:'添加考勤',
            editTitle:'编辑考勤',
            detailTitle:'考勤详情'
        }
    },

    /*设置最小宽度，并且自动滚动*/
    minWidth:1200,
    scrollable:true,
    

    items: [{
            xtype: "checkresult.maingrid"
    }]
})
