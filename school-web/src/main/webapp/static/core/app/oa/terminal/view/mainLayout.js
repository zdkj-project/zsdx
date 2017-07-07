Ext.define("core.oa.terminal.view.mainLayout", {
    extend: "core.base.view.BasePanel",
    alias: "widget.terminal.mainlayout",
    requires: [  
    	"core.oa.terminal.controller.MainController",
        "core.oa.terminal.view.mainLayout",
        "core.oa.terminal.view.detailLayout",
        "core.oa.terminal.view.listGrid",
        "core.oa.terminal.view.detailForm",
        "core.oa.terminal.view.readonlyForm"
   
    ],
    
    controller: 'terminal.terminalController',
    
    otherController:'jobinfo.otherController',
    layout:'border',
    border:false,
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
        tabConfig:{         //zzk：2017-6-1加入，用于对tab操作提供基本配置数据
        	addTitle:'添加终端',
        	editTitle:'编辑终端',
        	detailTitle:'终端详情'
        }
    },
    items: [{
            xtype: "terminal.grid",
            region: "center"
    }]
})