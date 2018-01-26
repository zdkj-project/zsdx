Ext.define("core.train.pushinfo.view.MainLayout", {
	extend: "core.base.view.BasePanel",
	alias: 'widget.pushinfo.mainlayout',

    requires: [   
        "core.train.pushinfo.controller.MainController",
    ],
    
    /** 关联此视图控制器 */
    controller: 'pushinfo.maincontroller',
    viewModel: 'pushinfo.mainModel',
    
	funCode: "pushinfo_main",
	detCode: 'pushinfo_detail',
	detLayout: 'pushinfo.detaillayout',

	 /*标注这个视图控制器的别名，以此提供给window处使用*/
    otherController:'pushinfo.othercontroller',
 
	funData: {
		action: comm.get('baseUrl') + "/PushInfo", //请求Action
		pkName: "uuid",
		defaultObj: {
        },
       	
	},

    minWidth:1000,
    scrollable:'x',
    layout:'border',

	items: [{
		xtype: "pushinfo.infotypegrid",
		region: "west",
		split:true,
		width: 150
	}, {
		xtype: "pushinfo.maingrid",
		region: "center",
		
	}]
})
