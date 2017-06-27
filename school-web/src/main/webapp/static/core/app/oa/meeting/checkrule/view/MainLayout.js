Ext.define("core.oa.meeting.checkrule.view.MainLayout", {
    extend: "core.base.view.BasePanel",
    alias: "widget.checkrule.mainlayout",
    /** 引入必须的文件 */
    requires: [
        'core.oa.meeting.checkrule.controller.MainController',
        'core.oa.meeting.checkrule.view.MainGrid',
        'core.oa.meeting.checkrule.view.MainQueryPanel',
        "core.oa.meeting.checkrule.view.DetailLayout",
    ],
    /** 关联此视图控制器 */
    controller: 'checkrule.mainController',
    /** 页面代码定义 */
    funCode: "checkrule_main",
    detCode: "checkrule_detail",
    detLayout: "checkrule.detaillayout",
    /*标注这个视图控制器的别名，以此提供给window处使用*/
    otherController: 'checkrule.otherController',
    layout: 'border',
    border: false,
    funData: {
        action: comm.get("baseUrl") + "/OaMeetingcheckrule", //请求Action
        whereSql: "", //表格查询条件
        orderSql: "", //表格排序条件
        filter: "",
        pkName: "uuid",
        width: 800,
        height: 550,
        defaultObj: {
            inBefore: "30",
            beLate: "5",
            absenteeism: "10"
        },
        tabConfig:{         //zzk：2017-6-1加入，用于对tab操作提供基本配置数据
            addTitle:'添加规则',
            editTitle:'编辑规则',
            detailTitle:'规则详情'
        }
    },

     /*设置最小宽度，并且自动滚动*/
    minWidth:1200,
    scrollable:true,
    
    items: [{
        xtype: "checkrule.maingrid",
        region: "center"
    }]
})