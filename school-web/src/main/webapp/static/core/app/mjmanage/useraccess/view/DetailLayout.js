Ext.define("core.mjmanage.useraccess.view.DetailLayout",{
	extend:"core.base.view.BasePanel",
	alias : 'widget.mjmanage.useraccess.detaillayout',
	funCode:"useraccess_detail",
	funData: {
		action: comm.get('baseUrl') + "/BaseMjUserright", //请求Action
		pkName: "uuid",
		defaultObj: {
		}
	},
	 /*设置最小宽度，并且自动滚动*/
    minWidth:1000,
    scrollable:true,

    layout : "fit",
    /*关联此视图控制器*/
	controller: 'mjmanage.useraccess.detailcontroller',
	
	items: [{
		xtype: "mjmanage.useraccess.useraccessgrid"
	}]
})