Ext.define("core.system.ip.view.ipMainLayout", {
    extend: "core.base.view.BasePanel",
    alias: 'widget.ip.mainlayout',
    
    requires: [    
    	"core.system.ip.view.ipMainLayout",
    	"core.system.ip.view.ipDetailLayout",
        "core.system.ip.view.ipGrid",
        "core.system.ip.view.ipDetailForm"
   
    ],
    
    controller: 'ip.ipController',
    funCode: "ip_main",
    detCode: 'ip_detail',
    detLayout: 'ip.detaillayout',
    funData: {
        action: comm.get('baseUrl') + "/SysIp", //请求Action
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
            xtype: "ip.ipgrid",
            region: "center"
        }]
    }]
})