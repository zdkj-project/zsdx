Ext.define("core.oa.meeting.meetinginfo.view.MainLayout", {
    extend: "core.base.view.BasePanel",
    alias: "widget.meetinginfo.mainlayout",
    /** 引入必须的文件 */
    requires: [ 
        'core.oa.meeting.meetinginfo.controller.MainController',
        //'core.oa.meeting.meetinginfo.model.MainGridModel',
        //'core.oa.meeting.meetinginfo.store.MainGridStore', 
        'core.oa.meeting.meetinginfo.view.MainGrid',
        'core.oa.meeting.meetinginfo.view.MainQueryPanel',
        "core.oa.meeting.meetinginfo.view.DetailLayout",
    ],    
    /** 关联此视图控制器 */
    controller: 'meetinginfo.mainController',
    /** 页面代码定义 */
    funCode: "meetinginfo_main",
    detCode: "meetinginfo_detail",
    detLayout: "meetinginfo.detaillayout",
    /*标注这个视图控制器的别名，以此提供给window处使用*/
    otherController:'meetinginfo.otherController',
    layout:'border',
    border:false,
    funData: {
        action: comm.get("baseUrl") + "/OaMeeting", //请求Action
        whereSql: "", //表格查询条件
        orderSql: "", //表格排序条件
        filter: "",
        pkName: "uuid",
        width: 1000,
        height: 700,
        defaultObj: {
            needChecking:true
        },
        tabConfig:{         //zzk：2017-6-1加入，用于对tab操作提供基本配置数据
            addTitle:'添加会议',
            editTitle:'编辑会议',
            detailTitle:'会议详情'
        }
    },

    /*设置最小宽度，并且自动滚动*/
    minWidth:1200,
    scrollable:true,

    items: [{
            xtype: "meetinginfo.maingrid",
            region: "center"
    }]
})
