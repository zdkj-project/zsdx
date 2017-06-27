Ext.define("core.oa.terminal.view.mainLayout", {
    extend: "core.base.view.BasePanel",
    
    requires: [    
        "core.oa.terminal.view.mainLayout",
        "core.oa.terminal.view.detailLayout",
        "core.oa.terminal.view.listGrid",
        "core.oa.terminal.view.detailForm",
        "core.oa.terminal.view.readonlyForm"
   
    ],
    
    controller: 'terminal.terminalController',
    //otherController:'dept.otherController',
    
    alias: "widget.terminal.mainlayout",
    layout: "border",
    //bodyPadding: 2,
    border: false,
    funCode: "terminal_main",
    detCode: "terminal_detail",
    detLayout: "terminal.detaillayout",
    funData: {
        action: comm.get("baseUrl") + "/OaInfoterm", //请求Action
        whereSql: "", //表格查询条件
        orderSql: "", //表格排序条件
        filter: "",
        pkName: "uuid",
        width: 400,
        height: 300,
        defaultObj: {
            beforeNumber:1,
            termCount:30,
            termType:"1"
        },
    },
    items: [/*{
        xtype: 'basecenterpanel',
        region: "center",
        items: [{
            xtype: "basequerypanel",
            layout: 'form',
            region: "north",
            height: 100,
            items: [{
                xtype: "container",
                layout: "hbox", // 从左往右的布局
                items: [{
                    flex: 1,
                    labelAlign: "right",
                    fieldLabel: "房间名称",
                    xtype: "basequeryfield",
                    queryType: "textfield",
                    name: "roomName"
                }, {
                    flex: 1,
                    labelAlign: "right",
                    fieldLabel: "终端号",
                    xtype: "basequeryfield",
                    queryType: "textfield",
                    name: "termCode"
                }]
            }]
        },*/ {
            xtype: "terminal.grid",
            region: "center"
        /*}]*/
    }]
})