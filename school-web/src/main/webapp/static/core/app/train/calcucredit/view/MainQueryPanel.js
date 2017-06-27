Ext.define("core.train.calcucredit.view.MainQueryPanel", {
	extend: "core.base.view.BaseQueryForm",
	alias: "widget.calcucredit.mainquerypanel",
	layout: "form",
	frame: false,
	height: 150,

	items: [{
		xtype: "container",
		layout: "column",
		items: [{
			columnWidth:0.5,
			xtype: "basequeryfield",
			name: "beginDate",
			fieldLabel: "开始日期",
			//queryType: "datetimefield",
			queryType: "datefield",
			format :'Y年m月d日'
		},{
			columnWidth:0.5,
			xtype: "basequeryfield",
			name: "endDate",
			fieldLabel: "结束日期",			
			//queryType: "datetimefield",
			queryType: "datefield",
			format :'Y年m月d日'
		}]
	}, {
		xtype: "container",
		layout: "column",
		items: [ {
			columnWidth: 0.3,
			xtype: "basequeryfield",
			name: "className",
			fieldLabel: "班级名称",
			queryType: "textfield",
		}, {
			columnWidth: 0.3,
			xtype: "basequeryfield",
			name: "classNumb",
			fieldLabel: "班级编号",
			queryType: "textfield",
		},{
			columnWidth: 0.3,
			xtype: "basequeryfield",
			name: "classCategory",
			fieldLabel: "班级类型",
			queryType: "basecombobox",
			config: {
				ddCode: "ZXXBJLX"
			},
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