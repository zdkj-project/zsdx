Ext.define("core.mjmanage.basegateway.view.MainLayout", {
	extend: "core.base.view.BasePanel",
	alias: 'widget.mjmanage.basegateway.mainlayout',

    requires: [   
        "core.mjmanage.basegateway.controller.MainController",
    ],
    
    /** 关联此视图控制器 */
    controller: 'mjmanage.basegateway.maincontroller',

	funCode: "basegateway_main",
	detCode: 'basegateway_detail',
	detLayout: 'mjmanage.basegateway.detaillayout',

	 /*标注这个视图控制器的别名，以此提供给window处使用*/
    otherController:'mjmanage.basegateway.othercontroller',
 
	funData: {
		action: comm.get('baseUrl') + "/PtGateway", //请求Action
		pkName: "uuid",
		defaultObj: {
           gatewayStatus: 0 
        },
       // whereSql:" where isDelete=0",
	    tabConfig:{         //zzk：2017-6-1加入，用于对tab操作提供基本配置数据
            titleField:'gatewayName',   //指定这个模块，主表格界面的名称的字段名，用于显示在tab标签页上面
            addTitle:'添加网关',
            editTitle:'编辑网关',
            detailTitle:'网关详情'
        }
	},

    minWidth:1000,
    scrollable:'x',
    layout:'border',

	items: [{
		xtype: "mjmanage.basegateway.ptgatewaytree",
		region: "west",
		//margin:'0 5 0 0',
		split:true,
		//collapsible:true,
		width: 250
	}, {
		xtype: "mjmanage.basegateway.miangrid",
		region: "center",
		/*flex:1,
		minWidth: 700*/
	}]
})
