Ext.define("core.train.coursechkresult.view.MainQueryPanel", {
	extend: "core.base.view.BaseQueryForm",
	alias: "widget.coursechkresult.mainquerypanel",
	layout: "form",
	frame: false,
	height: 100,

	items: [{
		xtype: "container",
		layout: "column",
		items: [{
			columnWidth:0.25,
			xtype: "basequeryfield",
			name: "beginDate",
			fieldLabel: "开始日期",
			queryType: "datetimefield",
			dateType:'date',		//指定这个组件的格式，date或者datetime
			dataType:'date',		//指定查询设置filter时的进行判断的类型，date或者datetime
			operationType:">=",	
		},{
			columnWidth:0.25,
			xtype: "basequeryfield",
			name: "endDate",
			fieldLabel: "结束日期",
			queryType: "datetimefield",
			dateType:'date',
			dataType:'date',
			operationType:"<=",		//运算符
		},{
			columnWidth: 0.25,
			xtype: "basequeryfield",
			name: "className",
			fieldLabel: "班级名称",
			queryType: "textfield",
		},{
			columnWidth: 0.25,
			xtype: "basequeryfield",
			name: "classCategory",
			fieldLabel: "班级类型",
			queryType: "basecombobox",
			config: {
				ddCode: "ZXXBJLX"
			}
		}]
	}],
	buttonAlign: "center",
	buttons: [{
		xtype: 'button',
		text: '搜 索',
		ref: 'gridSearchFormOk',
		iconCls: 'x-fa fa-search',
	}, {
		xtype: 'button',
		text: '重 置',
		ref: 'gridSearchFormReset',
		iconCls: 'x-fa fa-undo',
	}]
});