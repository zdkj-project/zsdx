Ext.define("core.good.news.view.MainQueryPanel", {
	extend: 'core.base.view.BaseQueryPanel',

	alias: 'widget.news.mainquerypanel',
 
    
    frame: true,

    height:150,
    //layout: "column",
    layout:"hbox",  
	defaults: {
        margin:"0",
        xtype : 'textfield',
        labelAlign : 'right',
        msgTarget: 'qtip',
        labelSeparator: '：', // 分隔符
        labelWidth:110, 
        //columnWidth : 0.4
        flex:1
    },
	items: [{    
        xtype: "basequeryfield",
        queryType: "textfield",
        fieldLabel: "快讯标题",
        name: "newTitle"
    },{
        xtype: "basequeryfield",
        queryType: "datefield",
        fieldLabel: "快讯发布时间",
        name: "createTime"
    },{
        xtype:'label',  //站位
        flex:1,
    }]
	
	// buttonAlign: "center",
	// buttons: [{
	// 	text: '查询',
	// 	ref: 'queryBtn',
	// 	iconCls: 'tree_ok'
	// }, {
	// 	text: '重置',
	// 	ref: 'resetBtn',
	// 	iconCls: 'tree_delete'
	// }]
});