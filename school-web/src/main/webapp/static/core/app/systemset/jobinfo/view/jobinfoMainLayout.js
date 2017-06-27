Ext.define("core.systemset.jobinfo.view.jobinfoMainLayout", {
    extend: "core.base.view.BasePanel",
    alias: 'widget.jobinfo.mainlayout',
    
    requires: [    
        "core.systemset.jobinfo.view.jobinfoMainLayout",
        "core.systemset.jobinfo.view.jobinfoDetailLayout",
        "core.systemset.jobinfo.view.jobinfoGrid",
        "core.systemset.jobinfo.view.jobinfoDetailForm"
   
    ],
    
    controller: 'jobinfo.jobinfoController',
    funCode: "jobinfo_main",
    detCode: 'jobinfo_detail',
    detLayout: 'jobinfo.detaillayout',
    funData: {
        action: comm.get('baseUrl') + "/basejob", //请求Action
        whereSql: "", //表格查询条件
        orderSql: " order by orderIndex", //表格排序条件
        pkName: "uuid",
        defaultObj: {
        }
    },
    items: [{
        xtype: 'basecenterpanel',
        items: [/*{
            xtype: "basequerypanel",
            layout:'form',
            region: "north",
                items: [{
                    xtype: "basequeryfield",
                    queryType: "textfield",
                    fieldLabel: "名称",
                    name: "jobName"
                }]
        },*/ {
            xtype: "jobinfo.jobinfogrid",
            region: "center"
        }]
    }]
})