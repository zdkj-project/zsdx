Ext.define("core.reportcenter.teacherconsumereport.view.MainQueryPanel", {
	extend: "core.base.view.BaseQueryForm",
	alias: "widget.teacherconsumereport.mainquerypanel",
	layout: "form",
	frame: false,
	height: 180,

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
			columnWidth:0.25,
			xtype: "basequeryfield",	
			queryType: "datetimefield",
			dateType:'date',		//指定这个组件的格式，date或者datetime
			dataType:'date',		//指定查询设置filter时的进行判断的类型，date或者datetime
			operationType:">=",	
			name: "beginDate",
			fieldLabel: "消费开始日期",
			format:"Y年m月d日"
		},{
			columnWidth:0.25,
			xtype: "basequeryfield",		
			queryType: "datetimefield",
			dateType:'date',
			dataType:'date',
			operationType:"<=",		//运算符
			name: "endDate",
			fieldLabel: "消费结束日期",	
			format:"Y年m月d日"
		}, {
			columnWidth: 0.25,
			xtype: "basequeryfield",
			name: "EmployeeName",
			fieldLabel: "人员姓名",
			queryType: "textfield",
		},{
			columnWidth: 0.25,
			xtype: "basequeryfield",
			name: "EmployeeStrID",
			fieldLabel: "人员编号",
			queryType: "textfield",
		}]
	},{
		xtype: "container",
		layout: "column",
		items: [{
			
			columnWidth: 0.25,
			xtype: "basequeryfield",
			name: "TermName",
			fieldLabel: "消费设备",
			queryType: "textfield",
		},{
			columnWidth: 0.25,
			xtype: "basequeryfield",
			name: "AccountName",
			fieldLabel: "结算账户",
			queryType: "textfield",
		},{
			columnWidth: 0.25,
			xtype: "basequeryfield",
			name: "MealTypeID",
			fieldLabel: "就餐类型",
			queryType: "combobox",		
			store: Ext.create('Ext.data.Store', {
            	fields: ['name', 'value'],
	            data : [
	                {"name":"全部", "value":""},
	                {"name":"早餐", "value":"1"},	              
	                {"name":"午餐", "value":"2"},
	                {"name":"晚餐", "value":"3"},
	                {"name":"夜宵", "value":"4"}
	            ]
	        }),
	        queryMode: 'local',
	        displayField: 'name',
	        valueField: 'value',
	        value:"",
	        editable:false	
		}]
	},{
		xtype: "container",
		layout: "column",
		items: [{
			columnWidth: 0.8,
	        xtype: 'checkboxgroup',
	        fieldLabel: '统计方式',
	        // Arrange checkboxes into two columns, distributed vertically
	        //columns: 2,
	        vertical: true,
	        items: [
	            { boxLabel: '按年', name: 'GROUP_TYPE', inputValue: 'YEAR(ConsumeDate)',checked: false},
	            { boxLabel: '按月', name: 'GROUP_TYPE', inputValue: 'MONTH(ConsumeDate)',checked: false  },
	            { boxLabel: '按日', name: 'GROUP_TYPE', inputValue: 'DAY(ConsumeDate)',checked: false },
	            { boxLabel: '人员姓名', name: 'GROUP_TYPE', inputValue: 'EmployeeName' },	          
	            { boxLabel: '人员编号', name: 'GROUP_TYPE', inputValue: 'EmployeeStrID' },
	            { boxLabel: '消费设备', name: 'GROUP_TYPE', inputValue: 'TermName' },
	            { boxLabel: '结算账户', name: 'GROUP_TYPE', inputValue: 'AccountName' },
	            { boxLabel: '就餐方式', name: 'GROUP_TYPE', inputValue: 'MealTypeName' },
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