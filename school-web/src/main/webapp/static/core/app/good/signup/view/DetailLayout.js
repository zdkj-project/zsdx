Ext.define("core.good.signup.view.DetailLayout",{
	extend:"Ext.panel.Panel",

	requires: [    
        'core.good.signup.controller.DetailController',
        "core.good.signup.view.DetailForm"
    ],
    controller: 'signup.detailController',

	alias : 'widget.signup.detaillayout',

	funCode:"signup_detail",
	funData: {
		action:  "/signup", //请求Action
		whereSql: "", //表格查询条件
		orderSql: " order by orderIndex", //表格排序条件
		pkName: "uuid"
		// defaultObj: {
		// 	 actBegin: new Date(),
		// 	 signBeing:new Date()
		// }
	},
	layout:'fit',

	

	/*以下五个属性用来，将form变为window */
    floating: true,
    centered: true,
    closable:true,
    modal: true,
    draggable:true,


	items: [{
		xtype: "signup.deteailform"
	}]
})