Ext.define("core.system.ip.view.ipMainLayout", {
    extend: "core.base.view.BasePanel",
    alias: 'widget.ip.mainlayout',
    
    requires: [    
    	"core.system.ip.view.ipMainLayout",
    	"core.system.ip.view.ipDetailLayout",
        "core.system.ip.view.ipGrid",
        "core.system.ip.view.ipDetailForm"
   
    ],
    /*关联此视图控制器*/
    controller: 'ip.ipController',
    /*控制器中获取组件*/
    funCode: "ip_main",
    detCode: 'ip_detail',
    detLayout: 'ip.detaillayout',
    funData: {
        action: comm.get('baseUrl') + "/SysIp", //请求Action
        whereSql: "", //表格查询条件
        orderSql: " order by orderIndex", //表格排序条件
        pkName: "uuid",
        defaultObj: {
        },
        
        /*7.10 lhy*/
       tabConfig:{
        addTitle:'添加接口',
        reactTitle:'编辑接口',
        deleteTitle:'删除'
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
            xtype: "ip.ipgrid",
            region: "center"
        }]
    }]
})