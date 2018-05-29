/**
 * 程序布局放大中间的部分
 */
Ext.define("core.base.view.CenterView",{
	extend: 'Ext.tab.Panel',
	alias: 'widget.centerview',
	id:'centerid',
	//margins: '2 0 0 0',
	border : 0,
	bodyStyle: 'padding:0px',
	items:[{
		title:'首页',
		iconCls:'home',
		bodyPadding :2,
		layout:'fit',
		items:{
			xtype:'panel',
			html:"<h1>首页做点啥呢！</h1>"
		},
		tabConfig  : {//标签配置参数
        }
	}]
});