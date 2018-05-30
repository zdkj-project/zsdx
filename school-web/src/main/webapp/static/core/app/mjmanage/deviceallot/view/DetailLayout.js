Ext.define("core.mjmanage.deviceallot.view.DetailLayout",{
	extend:"core.base.view.BasePanel",
	alias : 'widget.mjmanage.deviceallot.detaillayout',
	funCode:"deviceallot_detaillayout",
	funData: {
		action: comm.get('baseUrl') + "/PtTerm", //请求Action
		pkName: "uuid",
		defaultObj: {
		}
	},
	 /*设置最小宽度，并且自动滚动*/
    minWidth:1000,
    scrollable:true,

    /*关联此视图控制器*/
	controller: 'mjmanage.deviceallot.detailcontroller',
	
	items: [{
		xtype: "mjmanage.deviceallot.detailform"
	}]
})