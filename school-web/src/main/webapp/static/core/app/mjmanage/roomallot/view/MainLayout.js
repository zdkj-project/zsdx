Ext.define("core.mjmanage.roomallot.view.MainLayout", {
	extend: "core.base.view.BasePanel",
	alias: 'widget.mjmanage.roomallot.mainlayout',

    requires: [   
        "core.mjmanage.roomallot.controller.MainController",
    ],
    
    /** 关联此视图控制器 */
    controller: 'mjmanage.roomallot.maincontroller',

	funCode: "roomallot_main",
	detCode: 'roomallot_detail',
	detLayout: 'mjmanage.roomallot.selectteacherlayout',

	 /*标注这个视图控制器的别名，以此提供给window处使用*/
    otherController:'mjmanage.roomallot.othercontroller',
 
	funData: {
		action: comm.get('baseUrl') + "/BaseOfficeAllot", //请求Action
		pkName: "uuid",
		defaultObj: {
           
        },
	},

    minWidth:1000,
    scrollable:'x',
    layout:'border',

	items: [{
		xtype: "mjmanage.roomallot.roomallottree",
		region: "west",
		split:true,
		width:250
		//width: comm.get("clientWidth") * 0.2
	}, {
		xtype: "mjmanage.roomallot.maingrid",
		region: "center",
		
	}]
})
