Ext.define("core.systemset.jobinfo.view.jobinfoDetailLayout",{
	extend:"core.base.view.BasePanel",
	alias : 'widget.jobinfo.detaillayout',
	funCode:"jobinfo_detail",
	funData: {
		action: comm.get('baseUrl') + "/basejob", //请求Action
		whereSql: "", //表格查询条件
		orderSql: " order by orderIndex", //表格排序条件
		pkName: "uuid"
		// defaultObj: {
		// 	 actBegin: new Date(),
		// 	 signBeing:new Date()
		// }
	},
	 /*设置最小宽度，并且自动滚动*/
    minWidth:1200,
    scrollable:true,

    /*关联此视图控制器*/
	controller: 'jobinfo.detailController',
	items: [{
		xtype: "jobinfo.deailform"
	}]
})