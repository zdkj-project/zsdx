Ext.define('core.base.view.query.TreeView', {
	extend: 'Ext.tree.Panel',
	frame: false,
	animCollapse: false,
	alias: 'widget.mttreeview',
	rootVisible: false,
	multiSelect: false,
	width: comm.get("clientWidth") * 0.5,
	height: comm.get("resolutionHeight") * 0.9,
	autoScroll: true,
	animate: true,
	initComponent: function() {
		var me=this;
	
		/*此代码有问题，
		this.store = Ext.create("core.base.store.query.TreeStore", {
			url: this.url,
			model: 'com.zd.school.plartform.system.model.SysMenuTree',
			treeObj: me
		});*/

		var params={};
		if (this.params) {
			params = Ext.apply(params, this.params)
		}
		if (!this.multiSelect) {		
			params.excludes = "checked";
		}

		this.store=Ext.create('Ext.data.TreeStore', {
            defaultRootId: "ROOT",
            autoLoad: true,
            model: factory.ModelFactory.getModelByName(this.model, "checked").modelName,   
            proxy: {
                type: 'ajax',
                url: this.url,
                extraParams: params,
                reader: {
                    type: 'json'
                },
                writer: {
                    type: 'json'
                }
            }
        });

		
		
		/* 此功能暂时移除，在TreeStore中有bug
		var qc = Ext.create('Ext.form.field.ComboBox', {
			queryMode: 'local',
			store: new Ext.data.Store({
				fields: ['id', 'text', 'parentText']
			}),
			hideTrigger: true,
			valueField: 'id',
			displayField: 'text',
			ref: 'queryTreeCBB',
			emptyText: '输入查询信息...',
			flex: 1,
			listConfig: function(df) {
				return "{text}<tpl if='parentText'><div style='color:#C0C0C0;'>({parentText})</div></tpl>";
			}
		});
		this.dockedItems = [{
			xtype: 'toolbar',
			dock: 'top',
			layout: 'hbox',
			items: [qc]
		}]
		*/

		this.callParent(arguments);
	}
});