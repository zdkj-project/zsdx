Ext.define("core.base.view.BasePanel",{
	extend : 'Ext.panel.Panel',
	alias : 'widget.basepanel',
	layout:"fit",
    border:false,
	funCode: "",	//模块名_main
    detCode: '',	//模块名_detail
    detLayout: '', //模块名.detaillayout

    funData: {	//一些特殊数据
    	action: '', 	//请求Action
        whereSql: "", 	//表格查询条件
        orderSql: " order by orderIndex", //表格排序条件
        pkName: "uuid",
        defaultObj:{},
        //...more...
    }		
   
});