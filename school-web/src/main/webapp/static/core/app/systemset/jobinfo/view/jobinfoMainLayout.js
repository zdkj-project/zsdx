Ext.define("core.systemset.jobinfo.view.jobinfoMainLayout", {
    extend: "core.base.view.BasePanel",
    alias: 'widget.jobinfo.mainlayout',
    
    requires: [   
    	"core.systemset.jobinfo.controller.jobinfoController",
        "core.systemset.jobinfo.view.jobinfoMainLayout",
        "core.systemset.jobinfo.view.jobinfoDetailLayout",
        "core.systemset.jobinfo.view.jobinfoGrid",
        "core.systemset.jobinfo.view.jobinfoDetailForm"
   
    ],
    
    /** 关联此视图控制器 */
    controller: 'jobinfo.jobinfoController',
    /** 页面代码定义 */
    funCode: "jobinfo_main",
    detCode: "jobinfo_detail",
    detLayout: "jobinfo.detaillayout",
    /*标注这个视图控制器的别名，以此提供给window处使用*/
    otherController:'jobinfo.otherController',
    layout:'fit',
    border:false,
    funData: {
        action: comm.get('baseUrl') + "/basejob", //请求Action
        whereSql: "", //表格查询条件
        orderSql: " order by orderIndex", //表格排序条件
        pkName: "uuid",
        defaultObj: {
        },
        tabConfig:{         //zzk：2017-6-1加入，用于对tab操作提供基本配置数据
        	addTitle:'添加职位',
        	editTitle:'编辑职位',
        	detailTitle:'职位详情'
        }
    },
    /*设置最小宽度，并且自动滚动*/
    minWidth:1200,
    scrollable:true,
    items: [{
        xtype: "jobinfo.jobinfogrid"
    }]
})