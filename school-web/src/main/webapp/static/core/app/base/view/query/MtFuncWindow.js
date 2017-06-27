Ext.define("core.base.view.query.MtFuncWindow", {
	extend: "Ext.window.Window",
	modal: true,
	maximizable: true,
	title: "功能查询",
	alias: 'widget.mtfuncwindow',
	layout: "fit",
	closeAction: "destroy",
	buttons: [{
		text: '确定',
		ref: 'ssOkBtn',
		iconCls: 'x-fa fa-check-square'

	}, {
		text: '取消',
		ref: 'ssCancelBtn',
		iconCls: 'x-fa fa-reply'
	}],
	buttonAlign: "center",
	initComponent: function() {
		var self = this;
		this.callParent(arguments);
	}
})