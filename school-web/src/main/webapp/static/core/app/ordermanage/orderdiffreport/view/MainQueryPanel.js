Ext.define("core.ordermanage.orderdiffreport.view.MainQueryPanel", {
	extend: "core.base.view.BaseQueryForm",
	alias: "widget.orderdiffreport.mainquerypanel",
	layout: "form",
	frame: false,
	height: 100,

	fieldDefaults: { // 统一设置表单字段默认属性
        labelSeparator: '：', // 分隔符
        labelWidth:120,
        labelAlign: "right",
        width:'100%'
    },   
   
	items: [{
		xtype: "container",
		layout: "column",
		items: [{
			columnWidth:0.25,
			xtype: "basequeryfield",	
			queryType: "datetimefield",
			dateType:'date',		//指定这个组件的格式，date或者datetime
			dataType:'date',		//指定查询设置filter时的进行判断的类型，date或者datetime
			operationType:">=",	
			name: "beginDate",
			fieldLabel: "订餐开始日期",
			format:"Y年m月d日"
		},{
			columnWidth:0.25,
			xtype: "basequeryfield",		
			queryType: "datetimefield",
			dateType:'date',
			dataType:'date',
			operationType:"<=",		//运算符
			name: "endDate",
			fieldLabel: "订餐结束日期",	
			format:"Y年m月d日"
		},{
			columnWidth: 0.5,
	        xtype: 'checkboxgroup',
	        fieldLabel: '统计方式',
	        // Arrange checkboxes into two columns, distributed vertically
	        columns: 5,
	        vertical: true,
	        items: [
	            { boxLabel: '按年', name: 'GROUP_TYPE', inputValue: 'YEAR(dinnerDate)',checked: false},
	            { boxLabel: '按月', name: 'GROUP_TYPE', inputValue: 'MONTH(dinnerDate)',checked: false  },
	            { boxLabel: '按日', name: 'GROUP_TYPE', inputValue: 'DAY(dinnerDate)',checked: true },	          
	        ]
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