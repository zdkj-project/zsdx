Ext.define("core.train.dinnertotal.view.MainQueryPanel", {
	extend: "core.base.view.BaseQueryForm",
	alias: "widget.dinnertotal.mainquerypanel",
	layout: "form",
	frame: false,
	height: 100,

	fieldDefaults: { // 统一设置表单字段默认属性
        labelSeparator: '：', // 分隔符
        labelWidth:100,
        labelAlign: "right",
        width:'100%'
    },   
   
	items: [{
		xtype: "container",
		layout: "column",
		items: [{
			columnWidth:0.2,
			xtype: "basequeryfield",	
			queryType: "datetimefield",
			dateType:'date',		//指定这个组件的格式，date或者datetime
			dataType:'date',		//指定查询设置filter时的进行判断的类型，date或者datetime
			operationType:">=",	
			name: "beginDate",
			fieldLabel: "登记开始日期",
		},{
			columnWidth:0.2,
			xtype: "basequeryfield",		
			queryType: "datetimefield",
			dateType:'date',
			dataType:'date',
			operationType:"<=",		//运算符
			name: "endDate",
			fieldLabel: "登记结束日期",
		},{
			columnWidth: 0.2,
			xtype: "basequeryfield",
			name: "className",
			fieldLabel: "班级名称",
			queryType: "textfield",		
		}, {
			columnWidth: 0.2,
			xtype: "basequeryfield",
			name: "classNumb",
			fieldLabel: "班级编号",
			queryType: "textfield",
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