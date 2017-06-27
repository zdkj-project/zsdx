Ext.define("core.oa.roomterminal.view.mainLayout", {
    extend: "core.base.view.BasePanel",
    alias: "widget.roomterminal.mainlayout",
    
    requires: [    
        "core.oa.roomterminal.view.mainLayout",
        "core.oa.roomterminal.view.roomTree",
        "core.oa.roomterminal.view.listGrid",
        "core.oa.roomterminal.view.detailLayout",
        "core.oa.roomterminal.view.selectGrid",
        "core.oa.roomterminal.view.roomForm",
        "core.oa.roomterminal.store.roomTreeStore"
   
    ],
    
    controller: 'roomterminal.roomterminalController',
    otherController:'roomterminal.otherController',
    
    layout: "border",
    //bodyPadding: 2,
    border: false,
    funCode: "roomterminal_main",
    detCode: "roomterminal_detail",
    detLayout: "roomterminal.detaillayout",
    funData: {
        action: comm.get("baseUrl") + "/OaInfoterm", //请求Action
        whereSql: "", //表格查询条件
        orderSql: "", //表格排序条件
        filter: "",
        pkName: "uuid",
        width: 600,
        height: 380,
        defaultObj: {
        },
    },
    items: [{
        //collapsible: true,
        split: true,
        xtype: "roomterminal.roomtree",
        region: "west",
        style:{
            border: '1px solid #ddd'
        },
        width: comm.get("clientWidth") * 0.2
    }, {
        xtype: "roomterminal.listgrid",
        style:{
            border: '1px solid #ddd'
        },
        region: "center"
    }]
})