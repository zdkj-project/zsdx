Ext.define("core.systemset.dictionary.view.itemGrid", {
	extend: "core.base.view.BaseGrid",
	alias: "widget.dic.itemgrid",
	title: "字典项列表",
	dataUrl: comm.get('baseUrl') + "/BaseDicitem/list",
	model: 'com.zd.school.plartform.baseset.model.BaseDicitem',
	al: true,
	//排序字段及模式定义
	defSort: [{
		property: 'orderIndex',
		direction: 'ASC'
	},{
		property: 'itemCode',
		direction: 'ASC'
	}],
	extParams: {
		'filter': "[{'type':'string','comparison':'=','value':'ROOT','field':'dicId'}]"
	},

	tbar: [{
		xtype: 'button',
		text: '添加',
		ref: 'gridAdd',
        iconCls: 'x-fa fa-plus-circle',
	}, {
		xtype: 'button',
		text: '编辑',
		ref: 'gridEdit',
        iconCls: 'x-fa fa-pencil-square',
		disabled: true
	}, {
		xtype: 'button',
		text: '删除',
		ref: 'gridDelete',
        iconCls: 'x-fa fa-minus-circle',
	}/*,, {
		xtype: 'button',
		text: '启用',
		ref: 'gridRestore',
		iconCls: 'table_unlock'
	}*/],
	panelTopBar:false,
	panelButtomBar:false,
    columns:  { 
	 defaults:{
         //flex:1,
         align:'center',
         titleAlign:"center"
     },
		items: [{
			text: "主键",
			dataIndex: "uuid",
			hidden: true
		}, {
			text: "字典项名称",
			dataIndex: "itemName",
			//flex:1
			width:130
		}, {
			text: "字典项编码",
			dataIndex: "itemCode",
			//flex:1
			width:130
		}]
	}
});