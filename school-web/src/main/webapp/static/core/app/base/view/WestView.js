/**
 * 程序布局放大左边的部分
 */
Ext.define("core.base.view.WestView",{
	extend: 'Ext.panel.Panel',
	alias: 'widget.westview',
	mixins: {
		menuUtil : 'core.util.MenuUtil'
	},
	collapsible: true,
	split: true,
	defaults: {
		bodyStyle: 'padding:2px'
	}, 	
	border:1,
	margins: '2 2 0 0',
	width: 225,
	minSize: 100,
	maxSize: 250,
	tools:[{
		type:'refresh',
		qtip: '刷新',
	    handler: function(event,toolEl, panel){
			//前台清理
	    	factory.DDCache.clearAll();
	    	//后台清理
	    	var suppUtil=Ext.create("core.util.SuppleUtil");
	    	suppUtil.ajax({url:"/pc/cacheAction!clearAll.action"});
	    	window.location.reload();
	    }
	  }],
    initComponent: function(){//清除缓存信息
    	this.items = this.buildMenuTree();
    	this.layout = 'accordion';
    		this.layoutConfig = {
				titleCollapse: false,
				animate: true,
				activeOnTop: true
			}
		this.title = '功能导航';
        this.callParent(arguments);
    }
});



