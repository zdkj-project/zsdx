/**
 * 系统主程序的主页面布局
 * 
 */
Ext.define("core.base.view.MainView",{
	extend: 'Ext.panel.Panel',
	border:0,
	layout : 'border',
	alias: 'widget.mainView',
    initComponent: function(){
    	var north = {
			region : 'north',
			id:'app-header',
            xtype: 'panel',
            height: 50,
            html: comm.get('SysInfo')["title"]
		};
		var west = {
				xtype:'westview',
				region : 'west'
		};
		var center = {
			xtype : 'panel',
			region : 'center',
			layout : 'fit',
			ref : 'maincenterview',
			margins: '2 0 0 0',
			border : 0,
			items : [{
				xtype:'centerview',
				border : 0
			}]
		};
    	this.items = [north,west,center];
    	
        this.callParent(arguments);
    }	
})
