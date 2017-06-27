Ext.define("core.oa.meeting.meetinginfo.view.MainQueryPanel", {
	extend: "core.base.view.BaseQueryForm",
	alias: "widget.meetinginfo.mainquerypanel",
	layout: "form",
	frame: false,
	height: 130,
	items: [{
		xtype: "container",
		layout: "column",
		items: [{
			columnWidth:0.3,
			labelAlign: "right",
			xtype: "basequeryfield",
			name: "meetingTitle",
			fieldLabel: "会议主题",
			queryType: "textfield",
		}, {
			columnWidth:0.3,
			labelAlign: "right",
			xtype: "basequeryfield",
			name: "meetingState",
			fieldLabel: "会议状态",
			queryType: "basecombobox",
			config: {
				ddCode: "MEETINGSTATE"
			}
		}, {
			columnWidth:0.3,
			labelAlign: "right",
			xtype: "basequeryfield",
			name: "meetingCategory",
			fieldLabel: "会议类型",
			queryType: "basecombobox",
			config: {
				ddCode: " MEETINGCATEGORY"
			}
		}]
	}, {
		xtype: "container",
		layout: "column",
		items: [{
			columnWidth:0.5,
			labelAlign: "right",
			xtype: "basequeryfield",
			name: "beginTime",
			fieldLabel: "开始时间",
			queryType: "datetimefield",

		}, {
			columnWidth:0.5,
			labelAlign: "right",
			xtype: "basequeryfield",
			name: "endTime",
			fieldLabel: "结束时间",
			queryType: "datetimefield",
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