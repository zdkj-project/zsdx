Ext.define("core.system.menu.view.MainLayout", {
	extend: "core.base.view.BasePanel",
	alias: 'widget.menu.mainlayout',

	requires: [    
        'core.system.menu.controller.MenuController',
        'core.system.menu.view.MenuTree',
        'core.system.menu.store.MenuStore', 
        'core.system.menu.view.MenuForm',
        "core.system.menu.view.DetailLayout",
   
       
    ],

    controller: 'menu.menuController',


	funCode: "menu_main",
	detCode: "menu_detail",
	detLayout: "menu.detaillayout",
	
	
	funData: {
		action: comm.get('baseUrl') + "/BaseMenu", //请求controller路径
		whereSql: "and isDelete='0'",
		orderSql: " order by parentNode,isHidden,orderIndex asc",
		pkName: "uuid", //主键id    
		defaultObj: {
			orderIndex: 1
		},
		tabConfig:{         //zzk：2017-6-1加入，用于对tab操作提供基本配置数据
	    	addTitle:'添加下级',
	    	editTitle:'编辑菜单',
	    	detailTitle:'菜单详细'
	        }
	    },
	    /*设置最小宽度，并且自动滚动*/
	    minWidth:1200,
	    scrollable:true,
	    
	items: [{
		xtype: "menu.menutree",
		/*style:{
            border: '1px solid #ddd'
        },*/
	}]
})