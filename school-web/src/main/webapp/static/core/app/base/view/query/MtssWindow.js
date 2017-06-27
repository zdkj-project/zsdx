Ext.define("core.base.view.query.MtssWindow", {
	extend: "Ext.window.Window",
	modal: true,
	maximizable: true,
	title: "树形组件",
	alias: 'widget.mtsswinview',
	layout: "fit",
	closeAction: "destroy",
	initComponent: function() {
		var config = this.config;
		var items = {
			xtype: this.queryType,
			model: this.model,
			multiSelect: this.multiSelect,
		};
		items = Ext.apply(items, config);
		this.items = items;
		this.buttonAlign = "center";
		if (this.haveButton) {
			this.buttons = [{
				text: '确定',
				ref: 'ssOkBtn',
				iconCls: 'x-fa fa-check-square'

			}, {
				text: '取消',
				ref: 'ssCancelBtn',
				iconCls: 'x-fa fa-reply'
			}];
		}
		this.callParent(arguments);
	}
})