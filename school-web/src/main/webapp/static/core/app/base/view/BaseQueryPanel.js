Ext.define("core.base.view.BaseQueryPanel", {
	extend: 'Ext.form.Panel',
	alias: 'widget.basequerypanel',
	layout: "column",
	title: "组合查询",
	border: 1,
	frame: true,
	autoScroll: true,
	animCollapse: true,
	collapsible: true,
	bodyPadding: '10 10 0 10',

	defaults: {
		margin:"0",
        xtype : 'textfield',
        labelAlign : 'right',
        msgTarget: 'qtip',
        labelSeparator: '：', // 分隔符
        labelWidth:100, 
        //columnWidth : .5
	},
	buttonAlign: "center",
	buttons: [{
		text: '查询',
		ref: 'queryBtn',
		iconCls: 'x-fa fa-search',
        formBind: true, //只要表单数据正常的时候，才会允许查询
        //disabled: true
	}, {
		text: '重置',
		ref: 'resetBtn',
		iconCls: 'x-fa fa-undo'
	}]
});